package net.digitalpear.snifferiety;

import net.digitalpear.snifferiety.registry.data.SnifferietyRegistries;
import net.digitalpear.snifferiety.registry.data.SnifferietyRegistryKeys;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Snifferiety implements ModInitializer {

    public static final String MOD_ID = "snifferiety";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String text) {
        return new Identifier(MOD_ID, text);
    }


    public static final TagKey<Biome> IGNORE = TagKey.of(RegistryKeys.BIOME, Snifferiety.id("ignore"));


    @Override
    public void onInitialize() {
        SnifferietyRegistryKeys.init();
        SnifferietyRegistries.init();
    }
}
