package net.digitalpear.snifferiety.common.datagen;

import net.digitalpear.snifferiety.Snifferiety;
import net.digitalpear.snifferiety.common.util.SnifferSeedEntry;
import net.digitalpear.snifferiety.registry.SnifferSeedEntries;
import net.digitalpear.snifferiety.registry.data.SnifferietyRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SnifferSeedPropertiesProvider extends FabricDynamicRegistryProvider {
    public SnifferSeedPropertiesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        SnifferSeedEntries.ENTRIES.forEach(snifferSeedEntryRegistryKey -> {
            add(wrapperLookup, entries, snifferSeedEntryRegistryKey);
        });
    }
    public static void add(RegistryWrapper.WrapperLookup registries, Entries entries, RegistryKey<SnifferSeedEntry> resourceKey) {
        RegistryWrapper.Impl<SnifferSeedEntry> configuredFeatureRegistryLookup = registries.getWrapperOrThrow(SnifferietyRegistryKeys.SNIFFER_SEED_ENTRIES);

        entries.add(resourceKey, configuredFeatureRegistryLookup.getOrThrow(resourceKey).value());
    }

    @Override
    public String getName() {
        return "Sniffer Seed Properties";
    }
}
