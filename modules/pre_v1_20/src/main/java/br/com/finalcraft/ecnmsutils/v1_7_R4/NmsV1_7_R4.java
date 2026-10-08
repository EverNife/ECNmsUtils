package br.com.finalcraft.ecnmsutils.v1_7_R4;

import br.com.finalcraft.ecnmsutils.INMSUtils;
import br.com.finalcraft.ecnmsutils.NmsRevision;
import br.com.finalcraft.ecnmsutils.entities.NmsEntities;
import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.ecnmsutils.registries.ForgeOreDictionary;
import br.com.finalcraft.ecnmsutils.registries.NmsOreDictionary;
import br.com.finalcraft.ecnmsutils.registries.NmsRegistries;
import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;
import net.minecraft.server.v1_7_R4.ItemStack;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;

public final class NmsV1_7_R4 implements INMSUtils {

    private final ItemsV1_7_R4 items = new ItemsV1_7_R4();
    private final EntitiesV1_7_R4 entities = new EntitiesV1_7_R4();
    private final WorldsV1_7_R4 worlds = new WorldsV1_7_R4();
    private final NmsOreDictionary oreDictionary = new ForgeOreDictionary(ItemStack.class,
            stack -> CraftItemStack.asBukkitCopy((ItemStack) stack), items::asMinecraft);
    private final NmsRegistries registries = () -> oreDictionary;

    @Override
    public NmsRevision revision() {
        return NmsRevision.V1_7_R4;
    }

    @Override
    public NmsItems items() {
        return items;
    }

    @Override
    public NmsEntities entities() {
        return entities;
    }

    @Override
    public NmsWorlds worlds() {
        return worlds;
    }

    @Override
    public NmsRegistries registries() {
        return registries;
    }

}
