package net.digitalpear.snifferiety.registry.data;

import net.digitalpear.snifferiety.common.util.biome_conditions.BiomeConditionType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.SimpleRegistry;

public class SnifferietyRegistries {

    public static final SimpleRegistry<BiomeConditionType<?>> BIOME_CONDITION_TYPE = FabricRegistryBuilder.createSimple(SnifferietyRegistryKeys.BIOME_CONDITION_TYPE).buildAndRegister();

    public static void init() {

    }
}
