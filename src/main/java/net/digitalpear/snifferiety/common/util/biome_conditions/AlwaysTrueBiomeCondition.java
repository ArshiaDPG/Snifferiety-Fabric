package net.digitalpear.snifferiety.common.util.biome_conditions;

import com.mojang.serialization.Codec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;

public class AlwaysTrueBiomeCondition extends BiomeCondition{
    public static final AlwaysTrueBiomeCondition INSTANCE = new AlwaysTrueBiomeCondition();
    public static final Codec<AlwaysTrueBiomeCondition> CODEC = Codec.unit(() -> INSTANCE);
    @Override
    public boolean test(RegistryEntry<Biome> biome, Random random) {
        return true;
    }

    @Override
    protected BiomeConditionType<?> getType() {
        return BiomeConditionType.ALWAYS_TRUE;
    }
}
