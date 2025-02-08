package net.digitalpear.snifferiety.mixin;


import net.digitalpear.snifferiety.Snifferiety;
import net.digitalpear.snifferiety.common.util.SnifferSeedEntry;
import net.digitalpear.snifferiety.registry.SnifferSeedEntries;
import net.digitalpear.snifferiety.common.util.RandomCollection;
import net.digitalpear.snifferiety.registry.data.SnifferietyRegistryKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;


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
        RandomCollection<ItemStack> itemRandomCollection = new RandomCollection<>();
        BlockPos pos = getDigPos().down();

        /*
            Filter based on block that is being dug and biome it is in.
         */
        Stream<SnifferSeedEntry> entryStream = this.getWorld().getRegistryManager().get(SnifferietyRegistryKeys.SNIFFER_SEED_ENTRIES).stream();
        entryStream.filter(snifferSeedEntry -> snifferSeedEntry.checkBlockConditions(this.getWorld().getBlockState(pos), this.getRandom()) && snifferSeedEntry.checkBiomeConditions(this.getWorld().getBiome(pos), this.getRandom())).forEach(snifferSeedEntry -> {
            if (!snifferSeedEntry.getItem().isOf(Items.AIR)){
                itemRandomCollection.add(snifferSeedEntry.getWeight(), snifferSeedEntry.getItem());
//                Snifferiety.LOGGER.info("Added seed: " + snifferSeedEntry.getItem().getName() + " with weight " + snifferSeedEntry.getWeight());
//                Snifferiety.LOGGER.info("---------------------");
            }
        });
        return itemRandomCollection.next();
    }


    /*
        Check if block is diggable
     */
    @Inject(method = "isDiggable", at = @At("RETURN"), cancellable = true)
    private void injectMethod(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(SnifferSeedEntries.checkDiggability(this.getWorld(), pos));
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityType.SNIFFER.create(world);
    }
}