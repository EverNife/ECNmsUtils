package br.com.finalcraft.ecnmsutils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** NMSUtils finds each bridge by name; this catches a renamed or missing one in the packaged jar. */
class BridgeClassesTest {

    @ParameterizedTest
    @EnumSource(NmsRevision.class)
    void everyRevisionHasItsBridge(NmsRevision revision) throws ClassNotFoundException {
        // Not initialized: the bridge links against a CraftBukkit that is not on this classpath.
        Class<?> bridge = Class.forName(revision.getBridgeClassName(), false, getClass().getClassLoader());

        assertTrue(INMSUtils.class.isAssignableFrom(bridge), bridge.getName());
    }

}
