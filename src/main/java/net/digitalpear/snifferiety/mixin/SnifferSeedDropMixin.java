package net.digitalpear.snifferiety.mixin;


import net.digitalpear.snifferiety.RandomCollection;
import net.digitalpear.snifferiety.SnifferSeedRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        /*
            Add items from map to the list.
         */
        RandomCollection<Item> itemRandomCollection = new RandomCollection<>();

        /*
            Filter based on biome
         */
        SnifferSeedRegistry.getSnifferDropMap().forEach(((seed, weight) -> {
            if (SnifferSeedRegistry.getSnifferDropWhitelist().containsKey(seed)){
                if (SnifferSeedRegistry.getSnifferDropWhitelist().get(seed).contains(world.getBlockState(getDigPos().down()).getBlock())){
                    itemRandomCollection.add(weight, seed);
                }
            }
            else{
                itemRandomCollection.add(weight, seed);
            }

        }));
        return new ItemStack(itemRandomCollection.next());
    }

    @Inject(method = "isDiggable", at = @At("RETURN"), cancellable = true)
    private void injectMethod(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.world.getBlockState(pos.up()).isAir()){
            SnifferSeedRegistry.getSnifferDropWhitelist().forEach((seed, blocks) -> {
                if (blocks.contains(world.getBlockState(pos).getBlock())){
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