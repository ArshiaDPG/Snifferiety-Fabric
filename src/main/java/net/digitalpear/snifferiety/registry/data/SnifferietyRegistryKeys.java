package net.digitalpear.snifferiety.registry.data;

import net.digitalpear.snifferiety.Snifferiety;
import net.digitalpear.snifferiety.common.util.SnifferSeedEntry;
import net.digitalpear.snifferiety.common.util.biome_conditions.BiomeConditionType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class SnifferietyRegistryKeys {
    public static final RegistryKey<Registry<SnifferSeedEntry>> SNIFFER_SEED_ENTRIES = of(Identifier.of("minecraft","sniffer_seed_entries"));
    public static final RegistryKey<Registry<BiomeConditionType<?>>> BIOME_CONDITION_TYPE = of(Snifferiety.id("biome_condition_type"));

    private static <T> RegistryKey<Registry<T>> of(Identifier id) {
        return RegistryKey.ofRegistry(id);
    }
    public static void init(){
        DynamicRegistries.register(SNIFFER_SEED_ENTRIES, SnifferSeedEntry.CODEC);
    }
}
