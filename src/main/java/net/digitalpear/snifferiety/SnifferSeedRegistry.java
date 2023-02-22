package net.digitalpear.snifferiety;

import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SnifferSeedRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnifferSeedRegistry.class);
    public static final Map<Item, Integer> SNIFFER_DROP_MAP = new HashMap<>();

    public static void register(Item seed, int weight) {
        requireNonNullAndAxisProperty(seed, "seed item");
        requireNonNullAndAxisProperty(weight, "drop weight");

        if (SNIFFER_DROP_MAP.containsKey(seed)){
            Item old = getKey(SNIFFER_DROP_MAP, seed);
            SNIFFER_DROP_MAP.put(seed, weight);
            LOGGER.debug("Replaced old sniffing mapping from {} to {} with weight {}", old, seed, weight);
        }
        else{
            SNIFFER_DROP_MAP.put(seed, weight);
            LOGGER.debug("Set new sniffing mapping {} with weight {}.", seed, weight);
        }
    }


    private static void requireNonNullAndAxisProperty(Item item, String name) {
        Objects.requireNonNull(item, name + " cannot be null");
    }
    private static void requireNonNullAndAxisProperty(int weight, String name) {
        Objects.requireNonNull(weight, name + " cannot be null");
    }

    public static Item getKey(Map<Item, Integer> map,Item key)
    {
        if(map.containsKey(key)) {
            return key;
        }
        return null;
    }
}
