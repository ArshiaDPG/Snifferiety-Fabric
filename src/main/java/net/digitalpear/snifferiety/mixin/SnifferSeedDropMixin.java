package net.digitalpear.snifferiety.mixin;


import net.digitalpear.snifferiety.registry.SnifferSeedRegistry;
import net.digitalpear.snifferiety.util.RandomCollection;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.impl.biome.modification.BiomeSelectionContextImpl;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(SnifferEntity.class)
public abstract class SnifferSeedDropMixin extends AnimalEntity {
    @Shadow protected abstract BlockPos getDigPos();

    protected SnifferSeedDropMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "dropSeeds", at = @At("STORE"), ordinal = 0)
    private ItemStack getSeed(ItemStack itemStack){
        RandomCollection<Item> itemRandomCollection = new RandomCollection<>();

        /*
            Filter based on block that is being dug.
         */
        SnifferSeedRegistry.getSnifferDropMap().forEach((item, seedProperties) -> {
            if (world.getBlockState(getDigPos().down()).isIn(seedProperties.getWhitelist()) &&
                    !world.getBlockState(getDigPos().down()).isIn(seedProperties.getBlacklist())){

                itemRandomCollection.add(seedProperties.getWeight(), item);
            }
        });

        return new ItemStack(itemRandomCollection.next());
    }


    @Inject(method = "isDiggable", at = @At("RETURN"), cancellable = true)
    private void injectMethod(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.world.getBlockState(pos.up()).isAir()){
            SnifferSeedRegistry.getSnifferDropMap().forEach((item, seedProperties) -> {
                if (world.getBlockState(pos).isIn(seedProperties.getWhitelist()) &&
                        !world.getBlockState(pos).isIn(seedProperties.getBlacklist())){

                    cir.setReturnValue(true);
                }
            });
        }
    }


    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityType.SNIFFER.create(world);
    }
}