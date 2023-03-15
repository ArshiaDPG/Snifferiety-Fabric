package net.digitalpear.snifferiety.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SnifferSeedRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnifferSeedRegistry.class);

    private static final Map<Item, SeedProperties> SNIFFER_DROP_MAP = new HashMap<>();
    public static Map<Item, SeedProperties> getSnifferDropMap(){
        return SNIFFER_DROP_MAP;
    }

    public static void register(ItemConvertible seed, SeedProperties seedProperties) {
        requireNonNullAndAxisProperty(seed, "seed item");
        requireNonNullAndAxisProperty(seedProperties, "seed properties");


        if (SNIFFER_DROP_MAP.containsKey(seed)){
            Item old = getKey(SNIFFER_DROP_MAP, seed.asItem());
            SNIFFER_DROP_MAP.put(seed.asItem(), seedProperties);
            LOGGER.debug("Replaced old sniffing mapping from {} to {} with weight {}", old, seed, seedProperties.getWeight());
        }
        else{
            SNIFFER_DROP_MAP.put(seed.asItem(), seedProperties);
            LOGGER.debug("Set new sniffing mapping {} with weight {}.", seed, seedProperties.getWeight());
        }
    }

    private static void requireNonNullAndAxisProperty(SeedProperties seedProperties, String name) {
        Objects.requireNonNull(seedProperties, name + " cannot be null");
    }
    private static void requireNonNullAndAxisProperty(ItemConvertible item, String name) {
        Objects.requireNonNull(item, name + " cannot be null");
    }
    public static Item getKey(Map<Item, SeedProperties> map, Item key)
    {
        if(map.containsKey(key)) {
            return key;
        }
        return null;
    }


}
