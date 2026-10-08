package br.com.finalcraft.ecnmsutils.v1_16_R3;

import br.com.finalcraft.ecnmsutils.entities.ForgeFakePlayers;
import br.com.finalcraft.ecnmsutils.entities.NmsEntities;
import net.minecraft.server.v1_16_R3.Entity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Player;

final class EntitiesV1_16_R3 implements NmsEntities {

    @Override
    public String getRegistryName(org.bukkit.entity.Entity bukkitEntity) {
        return ((CraftEntity) bukkitEntity).getHandle().getSaveID();
    }

    @Override
    public org.bukkit.entity.Entity asBukkit(Object minecraftEntity) {
        return ((Entity) minecraftEntity).getBukkitEntity();
    }

    @Override
    public Object asMinecraft(org.bukkit.entity.Entity bukkitEntity) {
        return ((CraftEntity) bukkitEntity).getHandle();
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return ForgeFakePlayers.isFakePlayer(asMinecraft(player));
    }

}
