package br.com.finalcraft.ecnmsutils.v1_7_R4;

import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;
import br.com.finalcraft.everylibs.reflection.FieldAccessor;
import net.minecraft.server.v1_7_R4.EnumItemRarity;
import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.ItemArmor;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.ItemSword;
import net.minecraft.server.v1_7_R4.ItemTool;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftMagicNumbers;

import java.util.Objects;

final class ItemsV1_7_R4 implements NmsItems {

    private static final int NO_ARMOR = -1;

    private final FieldAccessor<ItemStack> handle = FCReflectionUtil.getFields().getField(CraftItemStack.class, "handle", ItemStack.class);

    @Override
    public String getRegistryName(org.bukkit.inventory.ItemStack itemStack) {
        return Item.REGISTRY.c(requireItem(itemStack));
    }

    @Override
    public org.bukkit.inventory.ItemStack fromRegistryName(String registryName, int amount, int meta) {
        Item item = (Item) Item.REGISTRY.get(registryName);
        if (item == null) {
            throw new IllegalArgumentException("No item is registered as \"" + registryName + "\" on this server. "
                    + "Pass a registry name such as minecraft:stone - getRegistryName(ItemStack) prints the one an item has.");
        }
        return CraftItemStack.asCraftMirror(new ItemStack(item, amount, meta));
    }

    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack itemStack) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        EnumItemRarity rarity = nmsItem.w();
        return (rarity == EnumItemRarity.COMMON ? "" : rarity.e.toString()) + nmsItem.getItem().n(nmsItem);
    }

    @Override
    public org.bukkit.inventory.ItemStack asBukkit(Object minecraftItemStack) {
        return CraftItemStack.asCraftMirror((ItemStack) minecraftItemStack);
    }

    @Override
    public Object asMinecraft(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public org.bukkit.inventory.ItemStack validateHandle(org.bukkit.inventory.ItemStack itemStack) {
        if (!(itemStack instanceof CraftItemStack)) {
            itemStack = CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(itemStack));
        }
        if (handle.get(itemStack) == null) {
            handle.set(itemStack, new ItemStack(CraftMagicNumbers.getItem(itemStack.getType()), itemStack.getAmount(), itemStack.getDurability()));
        }
        return itemStack;
    }

    @Override
    public boolean isTool(org.bukkit.inventory.ItemStack itemStack) {
        return itemOf(itemStack) instanceof ItemTool;
    }

    @Override
    public boolean isSword(org.bukkit.inventory.ItemStack itemStack) {
        return itemOf(itemStack) instanceof ItemSword;
    }

    @Override
    public boolean isArmor(org.bukkit.inventory.ItemStack itemStack) {
        return itemOf(itemStack) instanceof ItemArmor;
    }

    @Override
    public boolean isHelmet(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == 0;
    }

    @Override
    public boolean isChestplate(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == 1;
    }

    @Override
    public boolean isLeggings(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == 2;
    }

    @Override
    public boolean isBoots(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == 3;
    }

    private int armorSlot(org.bukkit.inventory.ItemStack itemStack) {
        Item item = itemOf(itemStack);
        return item instanceof ItemArmor ? ((ItemArmor) item).b : NO_ARMOR;
    }

    /** The Minecraft item behind the stack, or {@code null} for air; reads the handle instead of copying when it can. */
    private Item itemOf(org.bukkit.inventory.ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "itemStack");
        ItemStack nmsStack = itemStack instanceof CraftItemStack ? handle.get(itemStack) : CraftItemStack.asNMSCopy(itemStack);
        return nmsStack == null ? null : nmsStack.getItem();
    }

    private Item requireItem(org.bukkit.inventory.ItemStack itemStack) {
        Item item = itemOf(itemStack);
        if (item == null) {
            throw new IllegalArgumentException("The " + itemStack.getType() + " stack has no Minecraft item behind it, so it has no registry name.");
        }
        return item;
    }

}
