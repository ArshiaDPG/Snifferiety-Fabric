package net.digitalpear.snifferiety.registry;

import net.digitalpear.snifferiety.Snifferiety;
import net.digitalpear.snifferiety.common.util.SnifferSeedEntry;
import net.digitalpear.snifferiety.common.util.biome_conditions.KeyMatchBiomeCondition;
import net.digitalpear.snifferiety.common.util.biome_conditions.TagMatchBiomeCondition;
import net.digitalpear.snifferiety.registry.data.SnifferietyRegistryKeys;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.List;

public class SnifferSeedEntries {
    public static List<RegistryKey<SnifferSeedEntry>> ENTRIES = new ArrayList<>();

    public static final RegistryKey<SnifferSeedEntry> TORCHFLOWER_SEEDS = of("torchflower_seeds");
    public static final RegistryKey<SnifferSeedEntry> PITCHER_POD = of("pitcher_pod");
    public static final RegistryKey<SnifferSeedEntry> CHERRY_SAPLING = of("cherry_sapling");

    public static void bootstrap(Registerable<SnifferSeedEntry> registry) {
        registry.register(TORCHFLOWER_SEEDS, defaultSeed(Items.TORCHFLOWER_SEEDS));
        registry.register(PITCHER_POD, defaultSeed(Items.PITCHER_POD));
        registry.register(CHERRY_SAPLING, new SnifferSeedEntry(new ItemStack(Items.CHERRY_SAPLING), 400, List.of(new TagMatchRuleTest(BlockTags.DIRT)), List.of(new KeyMatchBiomeCondition(BiomeKeys.FOREST, true))));
    }
    public static SnifferSeedEntry defaultSeed(Item item){
        RuleTest defaultRuleTest = new TagMatchRuleTest(BlockTags.SNIFFER_DIGGABLE_BLOCK);
        int defaultWeight = 77;
        return new SnifferSeedEntry(new ItemStack(item), defaultWeight, List.of(defaultRuleTest));
    }

    public static RegistryKey<SnifferSeedEntry> of(String id) {
        RegistryKey<SnifferSeedEntry> entryRegistryKey = RegistryKey.of(SnifferietyRegistryKeys.SNIFFER_SEED_ENTRIES, Snifferiety.id(id));
        ENTRIES.add(entryRegistryKey);
        return entryRegistryKey;
    }

    public static Boolean checkDiggability(World entityWorld, BlockPos pos) {
        BlockState state = entityWorld.getBlockState(pos);
        return entityWorld.getRegistryManager().get(SnifferietyRegistryKeys.SNIFFER_SEED_ENTRIES).stream().anyMatch(properties -> properties.getBlockConditions() != null && properties.checkBlockConditions(state, entityWorld.getRandom()) && properties.checkBiomeConditions(entityWorld.getBiome(pos), entityWorld.getRandom()));
    }
}
