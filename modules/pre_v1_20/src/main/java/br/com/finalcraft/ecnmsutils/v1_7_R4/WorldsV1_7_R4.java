package br.com.finalcraft.ecnmsutils.v1_7_R4;

import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

final class WorldsV1_7_R4 implements NmsWorlds {

    @Override
    public World asBukkit(Object minecraftWorld) {
        return ((WorldServer) minecraftWorld).getWorld();
    }

    @Override
    public Object asMinecraft(World world) {
        return ((CraftWorld) world).getHandle();
    }

}
