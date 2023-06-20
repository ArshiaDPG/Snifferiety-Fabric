package net.digitalpear.snifferiety.registry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
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
    private static final Map<Item, TagKey<Biome>> BIOME_FILTER_MAP = new HashMap<>();

    public static Map<Item, SeedProperties> getSnifferDropMap(){
        Map<Item, SeedProperties> MAP = SNIFFER_DROP_MAP;
        return MAP;
    }

    public static Map<Item, TagKey<Biome>> getBiomeFilterMap() {
        Map<Item, TagKey<Biome>> MAP = BIOME_FILTER_MAP;
        return MAP;
    }

    public static void registerBiomeFilter(Item seed, TagKey<Biome> biomes){
        if (BIOME_FILTER_MAP.containsKey(seed)){
            LOGGER.debug("Changed old biome filter value from {} to {} with {}", biomes, seed, biomes);
        }
        BIOME_FILTER_MAP.put(seed, biomes);
    }

    @Deprecated
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

    public static boolean isBiomeValid(Item seed, World world, BlockPos pos){
        if (!BIOME_FILTER_MAP.containsKey(seed)){
            return true;
        }
        return world.getBiome(pos).isIn(BIOME_FILTER_MAP.get(seed));
    }

    public static boolean willItemDropFromBlock(SeedProperties seedProperties, BlockState blockState) {
        if (seedProperties.getBlacklist() != SeedProperties.NOTHING || seedProperties.getWhitelist() != SeedProperties.NOTHING) {

            if (seedProperties.getBlacklist() == SeedProperties.NOTHING) {
                return blockState.isIn(seedProperties.getWhitelist());

            } else if (seedProperties.getWhitelist() == SeedProperties.NOTHING) {
                return !blockState.isIn(seedProperties.getBlacklist());

            } else return blockState.isIn(seedProperties.getWhitelist()) && !blockState.isIn(seedProperties.getBlacklist());
        }
        return false;
    }

    public static boolean checkDiggability(World world, BlockPos pos){
        if (!world.getBlockState(pos.up()).isAir()) {
            return false;
        }

        for (Map.Entry<Item, SeedProperties> entry : SnifferSeedRegistry.getSnifferDropMap().entrySet()) {
            SeedProperties seedProperties = entry.getValue();
            if (seedProperties.getBlacklist() == null && seedProperties.getWhitelist() != null &&
                    world.getBlockState(pos).isIn(seedProperties.getWhitelist())) {
                return true;
            } else if (seedProperties.getWhitelist() == null && seedProperties.getBlacklist() != null &&
                    !world.getBlockState(pos).isIn(seedProperties.getBlacklist())) {
                return true;
            } else if (seedProperties.getWhitelist() != null && seedProperties.getBlacklist() != null &&
                    world.getBlockState(pos).isIn(seedProperties.getWhitelist()) && !world.getBlockState(pos).isIn(seedProperties.getBlacklist())) {
                return true;
            }
        }
        return false;
    }
}
