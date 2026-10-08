package br.com.finalcraft.ecnmsutils.entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Entities through the server's own entity classes.
 */
public interface NmsEntities {

    /** The entity type's registry name as the server saves it ({@code Pig} on 1.7.10, {@code minecraft:pig} later). */
    String getRegistryName(Entity entity);

    Entity asBukkit(Object minecraftEntity);

    Object asMinecraft(Entity entity);

    /** {@code true} for a Forge or NeoForge {@code FakePlayer}; always {@code false} on a server without either. */
    boolean isFakePlayer(Player player);

}
