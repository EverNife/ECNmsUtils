package br.com.finalcraft.ecnmsutils.v1_20_R2;

import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;
import br.com.finalcraft.everylibs.reflection.FieldAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.EnumItemRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemAir;
import net.minecraft.world.item.ItemArmor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemSword;
import net.minecraft.world.item.ItemTool;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;

import java.util.Objects;

final class ItemsV1_20_R2 implements NmsItems {

    private final FieldAccessor<ItemStack> handle = FCReflectionUtil.getFields().getField(CraftItemStack.class, "handle", ItemStack.class);

    @Override
    public String getRegistryName(org.bukkit.inventory.ItemStack itemStack) {
        return BuiltInRegistries.i.b(requireItem(itemStack)).toString();
    }

    @Override
    public org.bukkit.inventory.ItemStack fromRegistryName(String registryName, int amount, int meta) {
        Item item = BuiltInRegistries.i.a(new MinecraftKey(registryName));
        if (item instanceof ItemAir) {
            throw new IllegalArgumentException("No item is registered as \"" + registryName + "\" on this server. "
                    + "Pass a registry name such as minecraft:stone - getRegistryName(ItemStack) prints the one an item has.");
        }
        ItemStack nmsStack = new ItemStack(item, amount);
        if (meta != 0) {
            nmsStack.b(meta);
        }
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack itemStack) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        EnumItemRarity rarity = nmsItem.C();
        return (rarity == EnumItemRarity.a ? "" : rarity.e.toString()) + nmsItem.d().m(nmsItem).getString();
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
            handle.set(itemStack, new ItemStack(CraftMagicNumbers.getItem(itemStack.getType(), itemStack.getDurability())));
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
        return armorSlot(itemStack) == EnumItemSlot.f;
    }

    @Override
    public boolean isChestplate(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == EnumItemSlot.e;
    }

    @Override
    public boolean isLeggings(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == EnumItemSlot.d;
    }

    @Override
    public boolean isBoots(org.bukkit.inventory.ItemStack itemStack) {
        return armorSlot(itemStack) == EnumItemSlot.c;
    }

    private EnumItemSlot armorSlot(org.bukkit.inventory.ItemStack itemStack) {
        Item item = itemOf(itemStack);
        return item instanceof ItemArmor ? ((ItemArmor) item).g() : null;
    }

    /** The Minecraft item behind the stack, or {@code null} without one; reads the handle instead of copying when it can. */
    private Item itemOf(org.bukkit.inventory.ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "itemStack");
        ItemStack nmsStack = itemStack instanceof CraftItemStack ? handle.get(itemStack) : CraftItemStack.asNMSCopy(itemStack);
        return nmsStack == null ? null : nmsStack.d();
    }

    private Item requireItem(org.bukkit.inventory.ItemStack itemStack) {
        Item item = itemOf(itemStack);
        if (item == null) {
            throw new IllegalArgumentException("The " + itemStack.getType() + " stack has no Minecraft item behind it, so it has no registry name.");
        }
        return item;
    }

}
