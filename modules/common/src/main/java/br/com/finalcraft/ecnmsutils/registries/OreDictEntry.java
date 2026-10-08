package br.com.finalcraft.ecnmsutils.registries;

import br.com.finalcraft.ecnmsutils.NMSUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * One name of the {@link NmsOreDictionary}, equal to any other entry of the same name.
 */
public final class OreDictEntry {

    private final String oreName;

    public OreDictEntry(String oreName) {
        this.oreName = Objects.requireNonNull(oreName, "oreName");
    }

    public String getOreName() {
        return oreName;
    }

    /** The items under this name right now - read live, never cached. */
    public List<ItemStack> getItemStacks() {
        return NMSUtils.get().registries().oreDictionary().getOreItemStacks(oreName);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof OreDictEntry && oreName.equals(((OreDictEntry) other).oreName);
    }

    @Override
    public int hashCode() {
        return oreName.hashCode();
    }

    @Override
    public String toString() {
        return "OreDictEntry(oreName=" + oreName + ")";
    }

}
