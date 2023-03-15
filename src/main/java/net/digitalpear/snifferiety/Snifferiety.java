package net.digitalpear.snifferiety;

import net.digitalpear.snifferiety.util.SnifferJsonReader;
import net.digitalpear.snifferiety.registry.SnifferSeedRegistry;
import net.digitalpear.snifferiety.registry.SeedProperties;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
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
        /*
            Examples of usage

        SnifferSeedRegistry.register(ITEM, new SeedProperties(WEIGHT));

        SnifferSeedRegistry.register(ITEM, new SeedProperties(WEIGHT, WHITELIST));

        SnifferSeedRegistry.register(ITEM, new SeedProperties(WEIGHT, WHITELIST, BLACKLIST));
         */

        SnifferSeedRegistry.register(Items.TORCHFLOWER_SEEDS, new SeedProperties(70));

        SnifferSeedRegistry.register(Items.CACTUS, new SeedProperties(70, BlockTags.SAND));

        SnifferSeedRegistry.register(Items.IRON_NUGGET, new SeedProperties(70, BlockTags.SHOVEL_MINEABLE, BlockTags.SAND));


        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SnifferJsonReader());
    }
}
