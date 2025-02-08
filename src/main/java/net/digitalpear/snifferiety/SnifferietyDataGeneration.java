package net.digitalpear.snifferiety;

import net.digitalpear.snifferiety.common.datagen.SnifferSeedPropertiesProvider;
import net.digitalpear.snifferiety.registry.SnifferSeedEntries;
import net.digitalpear.snifferiety.registry.data.SnifferietyRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;

public class SnifferietyDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SnifferSeedPropertiesProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(SnifferietyRegistryKeys.SNIFFER_SEED_ENTRIES, SnifferSeedEntries::bootstrap);
    }
}
