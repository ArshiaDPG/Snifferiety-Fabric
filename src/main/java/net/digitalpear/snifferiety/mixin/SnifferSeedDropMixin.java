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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(SnifferEntity.class)
public class SnifferSeedDropMixin extends AnimalEntity {
    protected SnifferSeedDropMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "dropSeeds", at = @At("STORE"), ordinal = 0)
    private ItemStack getSeed(ItemStack itemStack){
        /*
            Add items from map to the list.
         */
        RandomCollection<Item> itemRandomCollection = new RandomCollection<>();
        SnifferSeedRegistry.SNIFFER_DROP_MAP.forEach(((seed, weight) -> itemRandomCollection.add(weight, seed)));

        return new ItemStack(itemRandomCollection.next());
    }


    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EntityType.SNIFFER.create(world);
    }
}