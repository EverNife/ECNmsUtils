package br.com.finalcraft.ecnmsutils.v1_16_R3;

import br.com.finalcraft.ecnmsutils.INMSUtils;
import br.com.finalcraft.ecnmsutils.NmsRevision;
import br.com.finalcraft.ecnmsutils.entities.NmsEntities;
import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.ecnmsutils.registries.NmsOreDictionary;
import br.com.finalcraft.ecnmsutils.registries.NmsRegistries;
import br.com.finalcraft.ecnmsutils.registries.TagOreDictionary;
import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;

public final class NmsV1_16_R3 implements INMSUtils {

    private final ItemsV1_16_R3 items = new ItemsV1_16_R3();
    private final EntitiesV1_16_R3 entities = new EntitiesV1_16_R3();
    private final WorldsV1_16_R3 worlds = new WorldsV1_16_R3();
    private final NmsOreDictionary oreDictionary = new TagOreDictionary();
    private final NmsRegistries registries = () -> oreDictionary;

    @Override
    public NmsRevision revision() {
        return NmsRevision.V1_16_R3;
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
