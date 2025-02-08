package net.digitalpear.snifferiety.common.util.biome_conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;

public class KeyMatchBiomeCondition extends BiomeCondition{
    public static final Codec<KeyMatchBiomeCondition> CODEC = RecordCodecBuilder.create(tagMatchBiomeConditionInstance -> tagMatchBiomeConditionInstance.group(
            RegistryKey.createCodec(RegistryKeys.BIOME).fieldOf("biome").forGetter(condition -> condition.biomeRegistryKey),
            Codec.BOOL.orElse(false).fieldOf("inverted").forGetter(condition -> condition.inverted)
    ).apply(tagMatchBiomeConditionInstance, KeyMatchBiomeCondition::new));

    private final RegistryKey<Biome> biomeRegistryKey;
    private final boolean inverted;

    public KeyMatchBiomeCondition(RegistryKey<Biome> biomeRegistryKey, boolean inverted){
        this.biomeRegistryKey = biomeRegistryKey;
        this.inverted = inverted;
    }

    @Override
    public boolean test(RegistryEntry<Biome> biome, Random random) {
        if (biome.getKey().isPresent()){
            if (inverted){
                return !biome.getKey().get().getValue().equals(biomeRegistryKey.getValue());
            }
            else {
                return biome.getKey().get().getValue().equals(biomeRegistryKey.getValue());
            }
        }
        return false;
    }

    @Override
    protected BiomeConditionType<?> getType() {
        return BiomeConditionType.KEY_MATCH;
    }
}
