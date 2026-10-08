package br.com.finalcraft.ecnmsutils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NmsRevisionTest {

    @ParameterizedTest
    @EnumSource(NmsRevision.class)
    void relocatedPackageNamesItsRevision(NmsRevision revision) {
        String craftBukkitPackage = "org.bukkit.craftbukkit." + revision.getPackageSegment();
        // The release is deliberately wrong: a relocated package decides alone.
        assertEquals(Optional.of(revision), NmsRevision.resolve(craftBukkitPackage, "9.9.9-R0.1-SNAPSHOT"));
    }

    @ParameterizedTest
    @EnumSource(NmsRevision.class)
    void plainPackageFallsBackToEveryRelease(NmsRevision revision) {
        for (String release : revision.getReleases()) {
            assertEquals(Optional.of(revision), NmsRevision.resolve("org.bukkit.craftbukkit", release + "-R0.1-SNAPSHOT"), release);
        }
    }

    @Test
    void paperOneTwentyOneOneWithoutRelocationIsServed() {
        assertEquals(Optional.of(NmsRevision.V1_21_R1), NmsRevision.resolve("org.bukkit.craftbukkit", "1.21.1-R0.1-SNAPSHOT"));
    }

    @ParameterizedTest
    @CsvSource({
            "org.bukkit.craftbukkit.v1_8_R3, 1.8.8-R0.1-SNAPSHOT",   // relocated, not served
            "org.bukkit.craftbukkit.v1_16_R1, 1.16.5-R0.1-SNAPSHOT", // relocated revision wins over a served release
            "org.bukkit.craftbukkit, 1.21.4-R0.1-SNAPSHOT",          // plain, release not served
            "org.bukkit.craftbukkit, garbage",
    })
    void unknownServerResolvesToNothing(String craftBukkitPackage, String bukkitVersion) {
        assertEquals(Optional.empty(), NmsRevision.resolve(craftBukkitPackage, bukkitVersion));
    }

    @Test
    void nullStringsResolveToNothing() {
        assertEquals(Optional.empty(), NmsRevision.resolve(null, null));
    }

    @Test
    void bridgeClassFollowsThePackageLayout() {
        assertEquals("br.com.finalcraft.ecnmsutils.v1_7_R4.NmsV1_7_R4", NmsRevision.V1_7_R4.getBridgeClassName());
    }

}
