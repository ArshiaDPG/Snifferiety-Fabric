package net.digitalpear.snifferiety.registry;

import net.digitalpear.snifferiety.Snifferiety;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class SeedProperties {
    private int weight;
    private TagKey<Block> whitelist;
    private TagKey<Block> blacklist;
    public static final TagKey<Block> NOTHING = TagKey.of(RegistryKeys.BLOCK, new Identifier(Snifferiety.MOD_ID, "nothing"));
    public static final TagKey<Block> EVERYTHING = TagKey.of(RegistryKeys.BLOCK, new Identifier(Snifferiety.MOD_ID, "everything"));


    public SeedProperties(int weight, TagKey<Block> whitelist, TagKey<Block> blacklist){
        requireNonNullAndAxisProperty(weight, "weight");
        requireNonNullAndAxisProperty(whitelist, "whitelist");
        requireNonNullAndAxisProperty(blacklist, "blacklist");

        this.weight = weight;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public SeedProperties(int weight, TagKey<Block> whitelist){
        requireNonNullAndAxisProperty(weight, "weight");
        requireNonNullAndAxisProperty(whitelist, "whitelist");

        this.weight = weight;
        this.whitelist = whitelist;
        this.blacklist = NOTHING;
    }
    public SeedProperties(int weight){
        requireNonNullAndAxisProperty(weight, "weight");

        this.weight = weight;
        this.whitelist = BlockTags.SNIFFER_DIGGABLE_BLOCK;
        this.blacklist = NOTHING;
    }

    public int getWeight() {
        return weight;
    }

    public TagKey<Block> getBlacklist() {
        return blacklist;
    }

    public TagKey<Block> getWhitelist() {
        return whitelist;
    }

    private static void requireNonNullAndAxisProperty(TagKey<Block> item, String name) {
        Objects.requireNonNull(item, name + " cannot be null");
    }
    private static void requireNonNullAndAxisProperty(Integer item, String name) {
        Objects.requireNonNull(item, name + " cannot be null");
    }
}