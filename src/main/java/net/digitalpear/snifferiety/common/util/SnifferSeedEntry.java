package net.digitalpear.snifferiety.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.digitalpear.snifferiety.common.util.biome_conditions.AlwaysTrueBiomeCondition;
import net.digitalpear.snifferiety.common.util.biome_conditions.BiomeCondition;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class SnifferSeedEntry {
    public static final Codec<SnifferSeedEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(c -> c.item),
            Codecs.POSITIVE_INT.fieldOf("weight").forGetter(c -> c.weight),
            Codec.list(RuleTest.TYPE_CODEC).fieldOf("block_conditions").orElse(List.of(AlwaysTrueRuleTest.INSTANCE)).forGetter(c -> c.blockConditions),
            Codec.list(BiomeCondition.TYPE_CODEC).fieldOf("biome_conditions").orElse(List.of(AlwaysTrueBiomeCondition.INSTANCE)).forGetter(snifferSeedEntry -> snifferSeedEntry.biomeConditions)
    ).apply(instance, SnifferSeedEntry::new));

    private final ItemStack item;
    private final int weight;
    private final List<RuleTest> blockConditions;
    private final List<BiomeCondition> biomeConditions;

    public SnifferSeedEntry(ItemStack item, int weight, List<RuleTest> blockConditions, List<BiomeCondition> biomeConditions){
        this.item = item;
        this.weight = weight;
        this.blockConditions = blockConditions;
        this.biomeConditions = biomeConditions;
    }
    public SnifferSeedEntry(ItemStack item, int weight, List<RuleTest> blockConditions){
        this(item, weight, blockConditions, List.of(AlwaysTrueBiomeCondition.INSTANCE));
    }


    public ItemStack getItem() {
        return item;
    }

    public int getWeight() {
        return weight;
    }

    public List<RuleTest> getBlockConditions() {
        return blockConditions;
    }

    public List<BiomeCondition> getBiomeConditions() {
        return biomeConditions;
    }

    public boolean checkBlockConditions(BlockState blockState, Random random){
        return getBlockConditions().stream().allMatch(blockCondition -> blockCondition.test(blockState, random));
    }
    public boolean checkBiomeConditions(RegistryEntry<Biome> biome, Random random){
        return getBiomeConditions().stream().allMatch(biomeCondition -> biomeCondition.test(biome, random));
    }
}