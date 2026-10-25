package br.com.finalcraft.ecnmsutils;

import br.com.finalcraft.ecnmsutils.anvil.NmsAnvil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** NMSUtils finds each bridge by name; this catches a renamed or missing one, or a missing anvil, in the packaged jar. */
class BridgeClassesTest {

    @ParameterizedTest
    @EnumSource(NmsRevision.class)
    void everyRevisionHasItsBridge(NmsRevision revision) throws ClassNotFoundException {
        // Not initialized: the bridge links against a CraftBukkit that is not on this classpath.
        Class<?> bridge = Class.forName(revision.getBridgeClassName(), false, getClass().getClassLoader());

        assertTrue(INMSUtils.class.isAssignableFrom(bridge), bridge.getName());
    }

    @ParameterizedTest
    @EnumSource(NmsRevision.class)
    void everyRevisionHasItsAnvil(NmsRevision revision) throws ClassNotFoundException {
        Class<?> anvil = Class.forName("br.com.finalcraft.ecnmsutils." + revision.getPackageSegment() + ".Anvil" + revision.name(),
                false, getClass().getClassLoader());

        assertTrue(NmsAnvil.class.isAssignableFrom(anvil), anvil.getName());
    }

    @ParameterizedTest
    @EnumSource(NmsRevision.class)
    void anAnvilWhoseContainerCannotLinkIsUnavailableInsteadOfBreakingTheBridge(NmsRevision revision) throws ReflectiveOperationException {
        // Initialized, so verified: no CraftBukkit here, as on a server whose remapper rejects the container subclass.
        Class<?> type = Class.forName("br.com.finalcraft.ecnmsutils." + revision.getPackageSegment() + ".Anvil" + revision.name(),
                true, getClass().getClassLoader());
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);

        NmsAnvil anvil = (NmsAnvil) constructor.newInstance();

        assertFalse(anvil.isAvailable(), anvil.getClass().getName());
    }

}
