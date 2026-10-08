package br.com.finalcraft.ecnmsutils.entities;

import br.com.finalcraft.everylibs.reflection.FCReflectionUtil;

/**
 * The {@code FakePlayer} check every revision shares; the class only exists on Forge and NeoForge hybrids.
 */
public final class ForgeFakePlayers {

    private static final Class<?> FAKE_PLAYER = FCReflectionUtil.getClasses().getFirstClass(
            "net.minecraftforge.common.util.FakePlayer",
            "net.neoforged.neoforge.common.util.FakePlayer"
    );

    private ForgeFakePlayers() {
    }

    public static boolean isFakePlayer(Object minecraftPlayer) {
        return FAKE_PLAYER != null && FAKE_PLAYER.isInstance(minecraftPlayer);
    }

}
