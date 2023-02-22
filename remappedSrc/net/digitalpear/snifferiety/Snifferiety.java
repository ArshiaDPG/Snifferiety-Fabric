package net.digitalpear.snifferiety;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;

public class Snifferiety implements ModInitializer {




    @Override
    public void onInitialize() {

        SnifferSeedRegistry.register(Items.APPLE, 4);
    }




}
