package br.com.finalcraft.ecnmsutils.worlds;

import org.bukkit.World;

/**
 * Worlds through the server's own world classes.
 */
public interface NmsWorlds {

    World asBukkit(Object minecraftWorld);

    Object asMinecraft(World world);

}
