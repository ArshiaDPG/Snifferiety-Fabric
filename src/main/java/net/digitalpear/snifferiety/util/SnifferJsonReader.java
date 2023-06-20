package net.digitalpear.snifferiety.util;

import com.google.gson.*;
import net.digitalpear.snifferiety.Snifferiety;
import net.digitalpear.snifferiety.registry.SeedProperties;
import net.digitalpear.snifferiety.registry.SnifferSeedRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.biome.Biome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

                                SeedProperties seedProperties = new SeedProperties(70);

                                Item seed = Registries.ITEM.get(new Identifier(entry.getAsJsonObject().get("seed").getAsString()));

                                /*
                                    Override default values if they are present in the entry.
                                 */
                                if (entry.getAsJsonObject().has("weight")){
                                    seedProperties.setWeight(entry.getAsJsonObject().get("weight").getAsInt());
                                }
                                if (entry.getAsJsonObject().has("block_whitelist")){
                                    List<String> whitelist = List.of(entry.getAsJsonObject().get("block_whitelist").getAsString().split(":"));
                                    seedProperties.setWhitelist(TagKey.of(RegistryKeys.BLOCK, new Identifier(whitelist.get(0),whitelist.get(1))));
                                }
                                if (entry.getAsJsonObject().has("block_blacklist")){
                                    List<String> blacklist = List.of(entry.getAsJsonObject().get("block_blacklist").getAsString().split(":"));
                                    seedProperties.setBlacklist(TagKey.of(RegistryKeys.BLOCK, new Identifier(blacklist.get(0),blacklist.get(1))));
                                }

                                SnifferSeedRegistry.register(seed, seedProperties);

                                /*
                                    Add to biome blacklist and whitelist if values are present in the entry.
                                 */
                                if (entry.getAsJsonObject().has("biome_blacklist")){
                                    List<String> blacklist = List.of(entry.getAsJsonObject().get("biome_blacklist").getAsString().split(":"));
                                    SnifferSeedRegistry.registerBiomeBlacklist(seed, TagKey.of(RegistryKeys.BIOME, new Identifier(blacklist.get(0),blacklist.get(1))));
                                }
                                if (entry.getAsJsonObject().has("biome_whitelist")){
                                    List<String> whitelist = List.of(entry.getAsJsonObject().get("biome_whitelist").getAsString().split(":"));
                                    SnifferSeedRegistry.registerBiomeWhitelist(seed, TagKey.of(RegistryKeys.BIOME, new Identifier(whitelist.get(0),whitelist.get(1))));
                                }
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
