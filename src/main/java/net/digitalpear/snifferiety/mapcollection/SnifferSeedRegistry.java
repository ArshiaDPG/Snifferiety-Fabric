package net.digitalpear.snifferiety.mapcollection;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SnifferSeedRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnifferSeedRegistry.class);


    /*
        List of items and their weights.
     */
    private static final Map<Item, Integer> SNIFFER_DROP_MAP = new HashMap<>();
    private static final Map<Item, TagKey<Block>> SNIFFER_DROP_WHITELIST = new HashMap<>();

    public static Map<Item, Integer> getSnifferDropMap(){
        return SNIFFER_DROP_MAP;
    }
    public static Map<Item, TagKey<Block>> getSnifferDropWhitelist(){
        return SNIFFER_DROP_WHITELIST;
    }




    public static void registerWhiteListable(ItemConvertible seed, int weight, TagKey<Block> whitelist){
        register(seed, weight);



        requireNonNullAndAxisProperty("whitelist blocks", whitelist);

        if (SNIFFER_DROP_WHITELIST.containsKey(seed)){
            Item old = getKey(seed.asItem(), SNIFFER_DROP_WHITELIST);
            SNIFFER_DROP_WHITELIST.put(seed.asItem(), whitelist);
            LOGGER.debug("Replaced old whitelist mapping from {} to {} with weight {}", old, seed, whitelist.id());
        }
        else{
            SNIFFER_DROP_WHITELIST.put(seed.asItem(), whitelist);
            LOGGER.debug("Set new whitelist mapping {} with weight {}.", seed, whitelist.id());
        }
        SNIFFER_DROP_WHITELIST.put(seed.asItem(), whitelist);
    }

    public static void register(ItemConvertible seed, int weight) {
        /*
            Make sure neither input value is null.
         */
        requireNonNullAndAxisProperty(seed, "seed item");
        requireNonNullAndAxisProperty(weight, "drop weight");

        /*
            If item already exists in the list, the override it.
         */
        if (SNIFFER_DROP_MAP.containsKey(seed)){
            Item old = getKey(SNIFFER_DROP_MAP, seed.asItem());
            SNIFFER_DROP_MAP.put(seed.asItem(), weight);
            LOGGER.debug("Replaced old sniffing mapping from {} to {} with weight {}", old, seed, weight);
        }
        else{
            SNIFFER_DROP_MAP.put(seed.asItem(), weight);
            LOGGER.debug("Set new sniffing mapping {} with weight {}.", seed, weight);
        }
    }


    public static void register(int weight, ItemConvertible... seeds){
        Arrays.stream(seeds).toList().forEach(itemConvertible -> register(itemConvertible, weight));
    }
    public static void registerWhiteListable(int weight, TagKey<Block> whitelist, ItemConvertible... seeds){
        Arrays.stream(seeds).toList().forEach(seed -> registerWhiteListable(seed, weight, whitelist));
    }

    private static void requireNonNullAndAxisProperty(String name, TagKey<Block> blockTagKey) {
        Objects.requireNonNull(blockTagKey, name + " cannot be null");
    }
    private static void requireNonNullAndAxisProperty(ItemConvertible item, String name) {
        Objects.requireNonNull(item, name + " cannot be null");
    }
    private static void requireNonNullAndAxisProperty(int weight, String name) {
        Objects.requireNonNull(weight, name + " cannot be null");
    }

    public static Item getKey(Item key, Map<Item, TagKey<Block>> map)
    {
        if(map.containsKey(key)) {
            return key;
        }
        return null;
    }
    public static Item getKey(Map<Item, Integer> map, Item key)
    {
        if(map.containsKey(key)) {
            return key;
        }
        return null;
    }
}