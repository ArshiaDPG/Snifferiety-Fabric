package net.digitalpear.snifferiety;

import net.digitalpear.snifferiety.mapcollection.SnifferJsonReader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Snifferiety implements ModInitializer {

    public static final String MOD_ID = "snifferiety";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String text) {
        return new Identifier(MOD_ID, text);
    }

    @Override
    public void onInitialize() {

        //Examples of usage

        /*
            SnifferSeedRegistry.register(Items.APPLE, 40);

            SnifferSeedRegistry.register(40, Items.APPLE, Items.CACTUS, etc.);
         */

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SnifferJsonReader());
    }
}
