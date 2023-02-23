package net.digitalpear.snifferiety.mapcollection;

import com.google.gson.*;
import net.digitalpear.snifferiety.SnifferSeedRegistry;
import net.digitalpear.snifferiety.Snifferiety;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

/*
    As taken from https://github.com/0rc1nus/Galosphere-Main/blob/c89199e0847021582618b33e478d7357cb5a827f/src/main/java/net/orcinus/galosphere/crafting/LumiereReformingManager.java#L31

    Made by 0rc1nus.
 */

public class SnifferJsonReader extends JsonDataLoader implements ResourceReloader, IdentifiableResourceReloadListener {
    private static final Gson GSON_INSTANCE = (new GsonBuilder()).create();

    public SnifferJsonReader() {
        super(GSON_INSTANCE, "loot_tables/gameplay");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        Identifier resourceLocation = Snifferiety.id("loot_tables/gameplay/sniffer_seeds.json");
            try {
                for (Resource iResource : manager.getAllResources(resourceLocation)) {
                    try (Reader reader = new BufferedReader(new InputStreamReader(iResource.getInputStream(), StandardCharsets.UTF_8))) {
                        JsonObject jsonObject = JsonHelper.deserialize(GSON_INSTANCE, reader, JsonObject.class);
                        if (jsonObject != null) {
                            JsonArray entryList = jsonObject.get("entries").getAsJsonArray();
                            for (JsonElement entry : entryList) {
                                SnifferSeedRegistry.register(Registries.ITEM.get(new Identifier(
                                                entry.getAsJsonObject().get("seed").getAsString())),
                                        entry.getAsJsonObject().get("weight").getAsInt());
                            }
                        }
                    } catch (RuntimeException | IOException exception) {
                        Snifferiety.LOGGER.info("Couldn't read table list {} in data pack {}", resourceLocation, iResource.getResourcePackName(), exception);
                    }
                }
            } catch (NoSuchElementException exception) {
                Snifferiety.LOGGER.error("Couldn't read table from {}", resourceLocation, exception);
            }
    }
    @Override
    public Identifier getFabricId() {
        return Snifferiety.id("loot_tables/gameplay");
    }
}
