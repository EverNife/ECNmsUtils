package br.com.finalcraft.ecnmsutils.registries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OreDictEntryTest {

    @Test
    void equalByName() {
        assertEquals(new OreDictEntry("ingotCopper"), new OreDictEntry("ingotCopper"));
        assertEquals(new OreDictEntry("ingotCopper").hashCode(), new OreDictEntry("ingotCopper").hashCode());
        assertNotEquals(new OreDictEntry("ingotCopper"), new OreDictEntry("ingotTin"));
    }

    @Test
    void toStringNamesTheEntry() {
        assertEquals("OreDictEntry(oreName=minecraft:logs)", new OreDictEntry("minecraft:logs").toString());
    }

    @Test
    void refusesNullName() {
        assertThrows(NullPointerException.class, () -> new OreDictEntry(null));
    }

}
