package net.digitalpear.snifferiety.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SnifferSeedRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnifferSeedRegistry.class);

    private static final Map<Item, SeedProperties> SNIFFER_DROP_MAP = new HashMap<>();
    private static final Map<Item, TagKey<Biome>> BIOME_WHITELIST_MAP = new HashMap<>();
    private static final Map<Item, TagKey<Biome>> BIOME_BLACKLIST_MAP = new HashMap<>();

    public static Map<Item, SeedProperties> getSnifferDropMap(){
        return SNIFFER_DROP_MAP;
    }

    public static Map<Item, TagKey<Biome>> getBiomeWhitelistMap() {
        return BIOME_WHITELIST_MAP;
    }

    public static Map<Item, TagKey<Biome>> getBiomeBlacklistMap() {
        return BIOME_BLACKLIST_MAP;
    }

    public static void registerBiomeWhitelist(Item seed, TagKey<Biome> biomes){
        if (BIOME_WHITELIST_MAP.containsKey(seed)){
            LOGGER.debug("Changed old biome whitelist value from {} to {} with {}", biomes, seed, biomes);
        }
        BIOME_WHITELIST_MAP.put(seed, biomes);
    }
    public static void registerBiomeBlacklist(Item seed, TagKey<Biome> biomes){
        if (BIOME_BLACKLIST_MAP.containsKey(seed)){
            LOGGER.debug("Changed old biome blacklist value from {} to {} with {}", biomes, seed, biomes);
        }
        BIOME_BLACKLIST_MAP.put(seed, biomes);
    }

    public static void register(ItemConvertible seed, int weight) {
        register(seed, new SeedProperties(weight));
    }

    public static void register(ItemConvertible seed, SeedProperties seedProperties) {
        requireNonNullAndAxisProperty(seed, "seed item");
        requireNonNullAndAxisProperty(seedProperties, "seed properties");


        if (SNIFFER_DROP_MAP.containsKey(seed)){
            Item old = getKey(SNIFFER_DROP_MAP, seed.asItem());
            SNIFFER_DROP_MAP.put(seed.asItem(), seedProperties);
            LOGGER.debug("Replaced old sniffing mapping from {} to {} with seed properties {}", old, seed, seedProperties.getWeight());
        }
        else{
            SNIFFER_DROP_MAP.put(seed.asItem(), seedProperties);
            LOGGER.debug("Set new sniffing mapping {} with seed properties {}.", seed, seedProperties.getWeight());
        }
        if (FabricLoader.getInstance().isDevelopmentEnvironment()){
            LOGGER.info("Item " + Registries.ITEM.getId(seed.asItem()) + " will drop with whitelist " + seedProperties.getWhitelist().id() + ", and blacklist " + seedProperties.getBlacklist().id() + ".");

        }
    }

    private static void requireNonNullAndAxisProperty(SeedProperties seedProperties, String name) {
        Objects.requireNonNull(seedProperties, name + " cannot be null");
    }
    private static void requireNonNullAndAxisProperty(ItemConvertible item, String name) {
        Objects.requireNonNull(item, name + " cannot be null");
    }
    private static Item getKey(Map<Item, SeedProperties> map, Item key)
    {
        if(map.containsKey(key)) {
            return key;
        }
        return null;
    }



    private static boolean checkWhitelist(Item seed, World world, BlockPos pos){
        if (!BIOME_WHITELIST_MAP.containsKey(seed)){
            return true;
        }
        return world.getBiome(pos).isIn(BIOME_WHITELIST_MAP.get(seed));
    }
    private static boolean checkBlacklist(Item seed, World world, BlockPos pos){
        if (!BIOME_BLACKLIST_MAP.containsKey(seed)){
            return true;
        }
        return !world.getBiome(pos).isIn(BIOME_BLACKLIST_MAP.get(seed));
    }
    public static boolean isBiomeValid(Item seed, World world, BlockPos pos){
        return checkBlacklist(seed, world, pos) && checkWhitelist(seed, world, pos);
    }



    private static boolean checkBlockWhitelist(Item seed, BlockState state){
        if (getSnifferDropMap().get(seed).getWhitelist().id() == SeedProperties.NOTHING.id()){
            return true;
        }
        return state.isIn(getSnifferDropMap().get(seed).getWhitelist());
    }
    private static boolean checkBlockBlacklist(Item seed, BlockState state){
        if (getSnifferDropMap().get(seed).getBlacklist().id() == SeedProperties.NOTHING.id()){
            return true;
        }
        return !state.isIn(getSnifferDropMap().get(seed).getBlacklist());
    }
    public static boolean willItemDropFromBlock(Item seed, BlockState blockState) {
        return checkBlockWhitelist(seed, blockState) && checkBlockBlacklist(seed, blockState);
    }




    private static boolean checkBlockWhitelist(Item seed, World world, BlockPos pos){
        if (getSnifferDropMap().get(seed).getWhitelist().id() == SeedProperties.NOTHING.id()){
            return true;
        }
        return world.getBlockState(pos).isIn(getSnifferDropMap().get(seed).getWhitelist());
    }
    private static boolean checkBlockBlacklist(Item seed, World world, BlockPos pos){
        if (getSnifferDropMap().get(seed).getBlacklist().id() == SeedProperties.NOTHING.id()){
            return true;
        }
        return !world.getBlockState(pos).isIn(getSnifferDropMap().get(seed).getBlacklist());
    }
    public static boolean checkDiggability(World world, BlockPos pos){
        if (!world.getBlockState(pos.up()).isAir()) {
            return false;
        }
        return SnifferSeedRegistry.getSnifferDropMap().keySet().stream().anyMatch(item -> checkBlockWhitelist(item, world, pos) && checkBlockBlacklist(item, world, pos));
    }
}
