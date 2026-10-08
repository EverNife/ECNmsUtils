package br.com.finalcraft.ecnmsutils.v1_16_R3;

import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

final class WorldsV1_16_R3 implements NmsWorlds {

    @Override
    public World asBukkit(Object minecraftWorld) {
        return ((WorldServer) minecraftWorld).getWorld();
    }

    @Override
    public Object asMinecraft(World world) {
        return ((CraftWorld) world).getHandle();
    }

}
