package br.com.finalcraft.ecnmsutils.registries;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Names that group items - "every copper ingot, whichever mod adds it". Every answer is read live:
 * the dictionary changes while the server runs (CraftTweaker, a datapack reload).
 *
 * <p>A server with no dictionary at all - plain Spigot 1.12.2, which has neither Forge nor tags -
 * answers empty everywhere instead of throwing.
 */
public interface NmsOreDictionary {

    boolean hasOreName(String oreName);

    List<String> getAllOreNames();

    /** The names the item carries. */
    List<String> getOreNamesFrom(ItemStack itemStack);

    /** The items under the name; empty for an unknown name. */
    List<ItemStack> getOreItemStacks(String oreName);

    List<OreDictEntry> getAllOreEntries();

}
