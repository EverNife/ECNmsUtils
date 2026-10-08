package br.com.finalcraft.ecnmsutils.v1_21_R1;

import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;
import net.minecraft.server.level.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;

final class WorldsV1_21_R1 implements NmsWorlds {

    @Override
    public World asBukkit(Object minecraftWorld) {
        return ((WorldServer) minecraftWorld).getWorld();
    }

    @Override
    public Object asMinecraft(World world) {
        return ((CraftWorld) world).getHandle();
    }

}
