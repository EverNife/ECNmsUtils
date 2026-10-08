package br.com.finalcraft.ecnmsutils;

import br.com.finalcraft.ecnmsutils.entities.NmsEntities;
import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.ecnmsutils.registries.NmsRegistries;
import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;

/**
 * The NMS bridge of one Minecraft revision, split by subject. Obtain it through {@link NMSUtils#get()}.
 */
public interface INMSUtils {

    /** The revision this bridge was written for. */
    NmsRevision revision();

    NmsItems items();

    NmsEntities entities();

    NmsWorlds worlds();

    NmsRegistries registries();

}
