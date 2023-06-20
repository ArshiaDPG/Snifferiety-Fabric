package net.digitalpear.snifferiety.mixin;


import net.digitalpear.snifferiety.registry.SnifferSeedRegistry;
import net.digitalpear.snifferiety.util.RandomCollection;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    /*
        Add items to drop based on chosen block
     */
    @ModifyVariable(method = "dropSeeds", at = @At("STORE"), ordinal = 0)
    private ItemStack getSeed(ItemStack itemStack){
        RandomCollection<Item> itemRandomCollection = new RandomCollection<>();
        BlockPos pos = getDigPos().down();

        /*
            Filter based on block that is being dug.
         */
        SnifferSeedRegistry.getSnifferDropMap().forEach((item, seedProperties) -> {
            if (SnifferSeedRegistry.willItemDropFromBlock(seedProperties, this.getEntityWorld().getBlockState(pos)) && SnifferSeedRegistry.isBiomeValid(item,getWorld(), pos)) {
                itemRandomCollection.add(seedProperties.getWeight(), item);
            }
        });

        return new ItemStack(itemRandomCollection.next());
    }


    /*
        Check if block is diggable
     */
    @Inject(method = "isDiggable", at = @At("RETURN"), cancellable = true)
    private void injectMethod(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(SnifferSeedRegistry.checkDiggability(this.getEntityWorld(), pos));
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityType.SNIFFER.create(world);
    }
}