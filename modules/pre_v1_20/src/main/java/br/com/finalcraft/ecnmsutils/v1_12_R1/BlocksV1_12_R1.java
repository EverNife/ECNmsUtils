package br.com.finalcraft.ecnmsutils.v1_12_R1;

import br.com.finalcraft.ecnmsutils.blocks.NmsBlocks;
import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;

final class BlocksV1_12_R1 implements NmsBlocks {

    @Override
    public String getRegistryName(Material material) {
        if (!material.isBlock()) {
            throw new IllegalArgumentException(material + " is an item, not a block, so the block registry has no name for it. "
                    + "Ask NMSUtils.get().items().getRegistryName(ItemStack) for an item's name.");
        }
        Block block = CraftMagicNumbers.getBlock(material);
        MinecraftKey key = Block.REGISTRY.b(block);
        return key.b() + ":" + key.getKey();
    }

}
