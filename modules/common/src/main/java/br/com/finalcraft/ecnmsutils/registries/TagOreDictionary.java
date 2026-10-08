package br.com.finalcraft.ecnmsutils.registries;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * The item tags of 1.16.5 onwards ({@code minecraft:logs}, {@code forge:ingots/copper}), read through
 * the Bukkit tag API - on a hybrid it carries the modded tags and items too.
 */
public final class TagOreDictionary implements NmsOreDictionary {

    private static Iterable<Tag<Material>> itemTags() {
        return Bukkit.getTags(Tag.REGISTRY_ITEMS, Material.class);
    }

    private static Tag<Material> find(String oreName) {
        for (Tag<Material> tag : itemTags()) {
            if (tag.getKey().toString().equals(oreName)) {
                return tag;
            }
        }
        return null;
    }

    @Override
    public boolean hasOreName(String oreName) {
        return find(oreName) != null;
    }

    @Override
    public List<String> getAllOreNames() {
        List<String> oreNames = new ArrayList<>();
        for (Tag<Material> tag : itemTags()) {
            oreNames.add(tag.getKey().toString());
        }
        return oreNames;
    }

    @Override
    public List<String> getOreNamesFrom(ItemStack itemStack) {
        List<String> oreNames = new ArrayList<>();
        for (Tag<Material> tag : itemTags()) {
            if (tag.isTagged(itemStack.getType())) {
                oreNames.add(tag.getKey().toString());
            }
        }
        return oreNames;
    }

    @Override
    public List<ItemStack> getOreItemStacks(String oreName) {
        List<ItemStack> itemStacks = new ArrayList<>();
        Tag<Material> tag = find(oreName);
        if (tag != null) {
            for (Material material : tag.getValues()) {
                itemStacks.add(new ItemStack(material));
            }
        }
        return itemStacks;
    }

    @Override
    public List<OreDictEntry> getAllOreEntries() {
        List<OreDictEntry> entries = new ArrayList<>();
        for (String oreName : getAllOreNames()) {
            entries.add(new OreDictEntry(oreName));
        }
        return entries;
    }

}
