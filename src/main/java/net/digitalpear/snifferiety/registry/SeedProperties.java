package net.digitalpear.snifferiety.registry;

import net.digitalpear.snifferiety.Snifferiety;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
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

    public SeedProperties(int weight, TagKey<Block> blacklist, TagKey<Block> whitelist){
        requireNonNullAndAxisProperty(weight, "weight");
        requireNonNullAndAxisProperty(whitelist, "whitelist");
        requireNonNullAndAxisProperty(blacklist, "blacklist");

        this.weight = weight;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }
    public SeedProperties(int weight, TagKey<Block> blacklist){
        requireNonNullAndAxisProperty(weight, "weight");
        requireNonNullAndAxisProperty(blacklist, "blacklist");

        this.weight = weight;
        this.whitelist = BlockTags.SNIFFER_DIGGABLE_BLOCK;
        this.blacklist = blacklist;
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

    private static void requireNonNullAndAxisProperty(TagKey<Block> tag, String name) {
        Objects.requireNonNull(tag, name + " cannot be null");
    }
    private static void requireNonNullAndAxisProperty(Integer weight, String name) {
        Objects.requireNonNull(weight, name + " cannot be null");
    }

    public void setBlacklist(TagKey<Block> blacklist) {
        this.blacklist = blacklist;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWhitelist(TagKey<Block> whitelist) {
        this.whitelist = whitelist;
    }
}