package net.digitalpear.snifferiety.common.util.biome_conditions;

import com.mojang.serialization.Codec;
import net.digitalpear.snifferiety.registry.data.SnifferietyRegistries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;

public abstract class BiomeCondition {
    public static final Codec<BiomeCondition> TYPE_CODEC = SnifferietyRegistries.BIOME_CONDITION_TYPE.getCodec().dispatch("biome_condtion", BiomeCondition::getType, BiomeConditionType::codec);

    public abstract boolean test(RegistryEntry<Biome> biome, Random random);


    protected abstract BiomeConditionType<?> getType();
}
