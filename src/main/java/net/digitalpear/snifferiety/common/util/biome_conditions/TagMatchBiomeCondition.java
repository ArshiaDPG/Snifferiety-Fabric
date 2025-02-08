package net.digitalpear.snifferiety.common.util.biome_conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;

public class TagMatchBiomeCondition extends BiomeCondition{
    public static final Codec<TagMatchBiomeCondition> CODEC = RecordCodecBuilder.create(tagMatchBiomeConditionInstance -> tagMatchBiomeConditionInstance.group(
            TagKey.unprefixedCodec(RegistryKeys.BIOME).fieldOf("tag").forGetter(condition -> condition.tag),
            Codec.BOOL.orElse(false).fieldOf("inverted").forGetter(condition -> condition.inverted)
    ).apply(tagMatchBiomeConditionInstance, TagMatchBiomeCondition::new));

    private final TagKey<Biome> tag;
    private final boolean inverted;
    public TagMatchBiomeCondition(TagKey<Biome> tag, boolean inverted){
        this.tag = tag;
        this.inverted = inverted;
    }

    @Override
    public boolean test(RegistryEntry<Biome> biome, Random random) {
        if (inverted){
            return !biome.isIn(tag);
        }
        return biome.isIn(tag);
    }

    @Override
    protected BiomeConditionType<?> getType() {
        return BiomeConditionType.TAG_MATCH;
    }
}
