package br.com.finalcraft.ecnmsutils.registries;

/**
 * The server's registries that a plugin queries by name.
 */
public interface NmsRegistries {

    /**
     * The item dictionary of this revision, in the names the revision itself uses: the Forge
     * {@code OreDictionary} up to 1.12.2 ({@code ingotCopper}, {@code logWood}), item tags from 1.16.5
     * on ({@code minecraft:logs}, {@code forge:ingots/copper}, {@code c:ingots/copper}). Never {@code null}.
     */
    NmsOreDictionary oreDictionary();

}
