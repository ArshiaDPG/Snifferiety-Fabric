package net.digitalpear.snifferiety;

import net.digitalpear.snifferiety.util.SnifferJsonReader;
import net.digitalpear.snifferiety.registry.SnifferSeedRegistry;
import net.digitalpear.snifferiety.registry.SeedProperties;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Blocks;
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

        SnifferSeedRegistry.register(Blocks.CACTUS, new SeedProperties(70, SeedProperties.NOTHING, BlockTags.SAND));

        SnifferSeedRegistry.register(Items.IRON_NUGGET, new SeedProperties(70, BlockTags.SAND, BlockTags.SHOVEL_MINEABLE));
         */

        SnifferSeedRegistry.register(Blocks.CACTUS, new SeedProperties(70, SeedProperties.NOTHING, BlockTags.SAND));

        SnifferSeedRegistry.register(Items.IRON_NUGGET, new SeedProperties(70, BlockTags.SAND, BlockTags.SHOVEL_MINEABLE));


        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SnifferJsonReader());
    }
}
