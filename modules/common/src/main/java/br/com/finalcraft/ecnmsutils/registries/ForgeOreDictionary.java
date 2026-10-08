package br.com.finalcraft.ecnmsutils.registries;

import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;
import br.com.finalcraft.everylibs.reflection.FieldAccessor;
import br.com.finalcraft.everylibs.reflection.MethodInvoker;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * The Forge {@code OreDictionary} of the 1.7.10 and 1.12.2 hybrids, read by reflection - the API is
 * the same on both, only the Minecraft item stack class differs. Without Forge it answers empty.
 */
public final class ForgeOreDictionary implements NmsOreDictionary {

    private static final String ORE_DICTIONARY = "net.minecraftforge.oredict.OreDictionary";

    private final Class<?> minecraftItemStack;
    private final Function<Object, ItemStack> toBukkitCopy;
    private final Function<ItemStack, Object> toMinecraft;

    private boolean resolved;
    private Forge forge; // null after resolution means no Forge on this server

    /**
     * @param toBukkitCopy must copy: the stacks it receives are the ones the dictionary itself holds
     */
    public ForgeOreDictionary(Class<?> minecraftItemStack, Function<Object, ItemStack> toBukkitCopy, Function<ItemStack, Object> toMinecraft) {
        this.minecraftItemStack = minecraftItemStack;
        this.toBukkitCopy = toBukkitCopy;
        this.toMinecraft = toMinecraft;
    }

    private synchronized Forge forge() {
        if (!resolved) {
            Class<?> oreDictionary = FCReflectionUtil.getClasses().getClass(ORE_DICTIONARY);
            forge = oreDictionary == null ? null : new Forge(oreDictionary, minecraftItemStack);
            resolved = true;
        }
        return forge;
    }

    @Override
    public boolean hasOreName(String oreName) {
        Forge forge = forge();
        return forge != null && forge.nameToId.get(null).containsKey(oreName);
    }

    @Override
    public List<String> getAllOreNames() {
        Forge forge = forge();
        return forge == null ? new ArrayList<>() : new ArrayList<>(forge.nameToId.get(null).keySet());
    }

    @Override
    public List<String> getOreNamesFrom(ItemStack itemStack) {
        Forge forge = forge();
        Object minecraftStack = forge == null ? null : toMinecraft.apply(itemStack);
        if (minecraftStack == null) {
            return new ArrayList<>(); // no Forge, or air
        }
        List<String> oreNames = new ArrayList<>();
        for (int oreId : forge.getOreIDs.invoke(null, minecraftStack)) {
            oreNames.add(forge.getOreName.invoke(null, oreId));
        }
        return oreNames;
    }

    @Override
    public List<ItemStack> getOreItemStacks(String oreName) {
        Forge forge = forge();
        if (forge == null) {
            return new ArrayList<>();
        }
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Object minecraftStack : forge.getOres.invoke(null, oreName, false)) {
            itemStacks.add(toBukkitCopy.apply(minecraftStack));
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

    private static final class Forge {
        final FieldAccessor<Map<String, Integer>> nameToId;
        final MethodInvoker<List<?>> getOres;
        final MethodInvoker<int[]> getOreIDs;
        final MethodInvoker<String> getOreName;

        Forge(Class<?> oreDictionary, Class<?> minecraftItemStack) {
            nameToId = FCReflectionUtil.getFields().getField(oreDictionary, "nameToId");
            getOres = FCReflectionUtil.getMethods().getMethod(oreDictionary, "getOres", String.class, boolean.class);
            getOreIDs = FCReflectionUtil.getMethods().getMethod(oreDictionary, "getOreIDs", minecraftItemStack);
            getOreName = FCReflectionUtil.getMethods().getMethod(oreDictionary, "getOreName", int.class);
        }
    }

}
