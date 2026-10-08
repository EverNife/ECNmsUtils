package br.com.finalcraft.ecnmsutils.v1_12_R1;

import br.com.finalcraft.ecnmsutils.entities.ForgeFakePlayers;
import br.com.finalcraft.ecnmsutils.entities.NmsEntities;
import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;
import br.com.finalcraft.everylibs.reflection.FieldAccessor;
import net.minecraft.server.v1_12_R1.Entity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Player;

final class EntitiesV1_12_R1 implements NmsEntities {

    private final FieldAccessor<Entity> entity = FCReflectionUtil.getFields().getField(CraftEntity.class, "entity", Entity.class);

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
        return entity.get(bukkitEntity);
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return ForgeFakePlayers.isFakePlayer(asMinecraft(player));
    }

}
