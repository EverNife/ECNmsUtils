package br.com.finalcraft.ecnmsutils.v1_20_R2;

import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;
import net.minecraft.server.level.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;

final class WorldsV1_20_R2 implements NmsWorlds {

    @Override
    public World asBukkit(Object minecraftWorld) {
        return ((WorldServer) minecraftWorld).getWorld();
    }

    @Override
    public Object asMinecraft(World world) {
        return ((CraftWorld) world).getHandle();
    }

}
