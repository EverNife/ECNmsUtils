package br.com.finalcraft.ecnmsutils.v1_12_R1;

import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;
import br.com.finalcraft.everylibs.reflection.FieldAccessor;
import net.minecraft.server.v1_12_R1.EnumItemRarity;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemArmor;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.ItemSword;
import net.minecraft.server.v1_12_R1.ItemTool;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;

import java.util.Objects;

final class ItemsV1_12_R1 implements NmsItems {

    private final FieldAccessor<ItemStack> handle = FCReflectionUtil.getFields().getField(CraftItemStack.class, "handle", ItemStack.class);

    @Override
    public String getRegistryName(org.bukkit.inventory.ItemStack itemStack) {
        MinecraftKey key = Item.REGISTRY.b(requireItem(itemStack));
        return key.b() + ":" + key.getKey();
    }

    @Override
    public org.bukkit.inventory.ItemStack fromRegistryName(String registryName, int amount, int meta) {
        Item item = Item.REGISTRY.get(new MinecraftKey(registryName));
        if (item == null) {
            throw new IllegalArgumentException("No item is registered as \"" + registryName + "\" on this server. "
                    + "Pass a registry name such as minecraft:stone - getRegistryName(ItemStack) prints the one an item has.");
        }
        return CraftItemStack.asBukkitCopy(new ItemStack(item, amount, meta));
    }

    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack itemStack) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        EnumItemRarity rarity = nmsItem.v();
        return (rarity == EnumItemRarity.COMMON ? "" : rarity.e.toString()) + nmsItem.getItem().b(nmsItem);
    }

    @Override
    public org.bukkit.inventory.ItemStack asBukkit(Object minecraftItemStack) {
        return CraftItemStack.asBukkitCopy((ItemStack) minecraftItemStack);
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
        return armorSlot(itemStack) == EnumItemSlot.HEAD;
    }

    @Override
    public boolean isChestplate(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == EnumItemSlot.CHEST;
    }

    @Override
    public boolean isLeggings(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == EnumItemSlot.LEGS;
    }

    @Override
    public boolean isBoots(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == EnumItemSlot.FEET;
    }

    private EnumItemSlot armorSlot(org.bukkit.inventory.ItemStack itemStack) {
        Item item = itemOf(itemStack);
        return item instanceof ItemArmor ? ((ItemArmor) item).c : null;
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
