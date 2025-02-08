package net.digitalpear.snifferiety.common.util.biome_conditions;

import com.mojang.serialization.Codec;
import net.digitalpear.snifferiety.Snifferiety;
import net.digitalpear.snifferiety.registry.data.SnifferietyRegistries;
import net.minecraft.registry.Registry;

public interface BiomeConditionType<T extends BiomeCondition> {
    BiomeConditionType<AlwaysTrueBiomeCondition> ALWAYS_TRUE = register("always_true", AlwaysTrueBiomeCondition.CODEC);
    BiomeConditionType<KeyMatchBiomeCondition> KEY_MATCH = register("key_match", KeyMatchBiomeCondition.CODEC);
    BiomeConditionType<TagMatchBiomeCondition> TAG_MATCH = register("tag_match", TagMatchBiomeCondition.CODEC);

    static void init() {
    }

    Codec<T> codec();
    static <P extends BiomeCondition> BiomeConditionType<P> register(String id, Codec<P> codec) {
        return Registry.register(SnifferietyRegistries.BIOME_CONDITION_TYPE, Snifferiety.id(id), () -> codec);
    }
}
