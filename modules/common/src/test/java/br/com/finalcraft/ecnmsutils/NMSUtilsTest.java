package br.com.finalcraft.ecnmsutils;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The Bukkit API is on this classpath, but no server is running. */
class NMSUtilsTest {

    @Test
    void withoutServerGetIsNeverNullAndNotSupported() {
        INMSUtils bridge = NMSUtils.get();

        assertNotNull(bridge);
        assertSame(bridge, NMSUtils.get());
        assertFalse(NMSUtils.isSupported());
        assertEquals(Optional.empty(), NMSUtils.revision());
    }

    @Test
    void everySubjectOfTheUnsupportedBridgeRefuses() {
        INMSUtils bridge = NMSUtils.get();

        assertThrows(UnsupportedOperationException.class, bridge::items);
        assertThrows(UnsupportedOperationException.class, bridge::entities);
        assertThrows(UnsupportedOperationException.class, bridge::worlds);
        assertThrows(UnsupportedOperationException.class, bridge::registries);
        assertThrows(UnsupportedOperationException.class, bridge::revision);
    }

    @Test
    void refusalNamesTheDetectedServerAndTheServedOnes() {
        UnsupportedNms bridge = new UnsupportedNms("the server org.bukkit.craftbukkit.v1_8_R3 / 1.8.8-R0.1-SNAPSHOT runs a revision this library has no bridge for", null);

        String message = assertThrows(UnsupportedOperationException.class, bridge::items).getMessage();

        assertTrue(message.contains("1.8.8-R0.1-SNAPSHOT"), message);
        assertTrue(message.contains("V1_21_R1"), message);
        assertTrue(message.contains("NMSUtils.isSupported()"), message);
    }

    @Test
    void refusalWithoutServerSaysSo() {
        String message = assertThrows(UnsupportedOperationException.class, () -> NMSUtils.get().items()).getMessage();

        assertTrue(message.contains("no Bukkit server is running"), message);
    }

}
