package br.com.finalcraft.ecnmsutils.v1_21_R1;

import br.com.finalcraft.ecnmsutils.INMSUtils;
import br.com.finalcraft.ecnmsutils.NmsRevision;
import br.com.finalcraft.ecnmsutils.entities.NmsEntities;
import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.ecnmsutils.registries.NmsOreDictionary;
import br.com.finalcraft.ecnmsutils.registries.NmsRegistries;
import br.com.finalcraft.ecnmsutils.registries.TagOreDictionary;
import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;

public final class NmsV1_21_R1 implements INMSUtils {

    private final ItemsV1_21_R1 items = new ItemsV1_21_R1();
    private final EntitiesV1_21_R1 entities = new EntitiesV1_21_R1();
    private final WorldsV1_21_R1 worlds = new WorldsV1_21_R1();
    private final NmsOreDictionary oreDictionary = new TagOreDictionary();
    private final NmsRegistries registries = () -> oreDictionary;

    @Override
    public NmsRevision revision() {
        return NmsRevision.V1_21_R1;
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
