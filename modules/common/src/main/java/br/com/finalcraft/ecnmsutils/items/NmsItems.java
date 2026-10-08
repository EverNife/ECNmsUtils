package br.com.finalcraft.ecnmsutils.items;

import org.bukkit.inventory.ItemStack;

/**
 * Items through the server's own item registry and classes.
 */
public interface NmsItems {

    /** The namespaced registry name of the item, {@code minecraft:stone}. */
    String getRegistryName(ItemStack itemStack);

    /**
     * A new stack of the item registered as {@code registryName}.
     *
     * @param meta the damage/metadata value; {@code 0} leaves it untouched
     * @throws IllegalArgumentException when no item is registered under that name
     */
    ItemStack fromRegistryName(String registryName, int amount, int meta);

    /** The display name the client would show for the item, rarity color included. */
    String getLocalizedName(ItemStack itemStack);

    /**
     * The Bukkit view of a Minecraft item stack. On 1.7.10 it is a mirror - writes through it reach
     * the Minecraft stack; on every later revision it is a copy.
     */
    ItemStack asBukkit(Object minecraftItemStack);

    /** A Minecraft copy of the stack. */
    Object asMinecraft(ItemStack itemStack);

    /**
     * The stack as a CraftItemStack that owns a Minecraft handle: a plain Bukkit stack is converted,
     * a CraftItemStack without a handle gets one. Use the returned stack.
     */
    ItemStack validateHandle(ItemStack itemStack);

    boolean isTool(ItemStack itemStack);

    boolean isSword(ItemStack itemStack);

    boolean isArmor(ItemStack itemStack);

    boolean isHelmet(ItemStack itemStack);

    boolean isChestplate(ItemStack itemStack);

    boolean isLeggings(ItemStack itemStack);

    boolean isBoots(ItemStack itemStack);

}
