package br.com.finalcraft.ecnmsutils.blocks;

import org.bukkit.Material;

/**
 * Blocks through the server's own block registry.
 */
public interface NmsBlocks {

    /**
     * The namespaced registry name of the BLOCK behind {@code material}, read from the block registry - not the item's:
     * redstone dust on the ground is {@code minecraft:redstone_wire} while its item is {@code minecraft:redstone}, and on
     * the revisions with block metadata every variant (red and white wool) answers its block's one name.
     *
     * @throws IllegalArgumentException when {@code material} is not a block ({@link Material#isBlock()} is false)
     */
    String getRegistryName(Material material);

}
