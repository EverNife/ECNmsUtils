package br.com.finalcraft.ecnmsutils;

import br.com.finalcraft.ecnmsutils.entities.NmsEntities;
import br.com.finalcraft.ecnmsutils.items.NmsItems;
import br.com.finalcraft.ecnmsutils.registries.NmsRegistries;
import br.com.finalcraft.ecnmsutils.worlds.NmsWorlds;

/**
 * What {@link NMSUtils#get()} answers where no bridge works: every subject refuses, saying why.
 */
final class UnsupportedNms implements INMSUtils {

    private final String reason;
    private final Throwable cause;

    UnsupportedNms(String reason, Throwable cause) {
        this.reason = reason;
        this.cause = cause;
    }

    UnsupportedOperationException refusal() {
        return new UnsupportedOperationException("ECNmsUtils has no NMS bridge here: " + reason + ". "
                + "It serves " + NmsRevision.describeServed() + ". "
                + "Check NMSUtils.isSupported() before reaching for an NMS subject.", cause);
    }

    @Override
    public NmsRevision revision() {
        throw refusal();
    }

    @Override
    public NmsItems items() {
        throw refusal();
    }

    @Override
    public NmsEntities entities() {
        throw refusal();
    }

    @Override
    public NmsWorlds worlds() {
        throw refusal();
    }

    @Override
    public NmsRegistries registries() {
        throw refusal();
    }

}
