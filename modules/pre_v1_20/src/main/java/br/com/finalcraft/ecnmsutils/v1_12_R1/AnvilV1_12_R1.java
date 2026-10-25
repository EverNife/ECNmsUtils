package br.com.finalcraft.ecnmsutils.v1_12_R1;

import br.com.finalcraft.ecnmsutils.NmsRevision;
import br.com.finalcraft.ecnmsutils.anvil.AbstractNmsAnvil;
import br.com.finalcraft.ecnmsutils.anvil.AnvilHandle;
import br.com.finalcraft.ecnmsutils.anvil.AnvilRequest;
import br.com.finalcraft.ecnmsutils.anvil.AnvilSession;
import br.com.finalcraft.ecnmsutils.anvil.InventoryAnvilWindow;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ContainerAnvil;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.InventoryClickType;
import net.minecraft.server.v1_12_R1.ItemStack;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

final class AnvilV1_12_R1 extends AbstractNmsAnvil {

    AnvilV1_12_R1() {
        super(NmsRevision.V1_12_R1);
    }

    @Override
    protected AnvilHandle openPrompt(Player player, AnvilRequest request) {
        return PromptContainer.open(player, request);
    }

    static final class PromptContainer extends ContainerAnvil {

        private final InventoryAnvilWindow window;
        private final AnvilSession session;

        private PromptContainer(Player player, EntityPlayer handle, AnvilRequest request) {
            super(handle.inventory, handle.world, BlockPosition.ZERO, handle);
            checkReachable = false; //no anvil block backs this window, so there is nothing to stand near
            windowId = handle.nextContainerCounter();
            //the 1.12 client draws its own "Repair & Name" heading: the title travels in the icon's lore
            window = new InventoryAnvilWindow(player, getBukkitView().getTopInventory(), request, true, () -> levelCost = 0);
            session = window.start();
        }

        static AnvilHandle open(Player player, AnvilRequest request) {
            PromptContainer container = new PromptContainer(player, ((CraftPlayer) player).getHandle(), request);
            InventoryView view = container.getBukkitView();
            //CraftBukkit's opener: InventoryOpenEvent, open packet, active container, slot sync
            player.openInventory(view);
            if (player.getOpenInventory() != view) {
                container.session.onClosed(); //another plugin cancelled the InventoryOpenEvent
            }
            return container.session;
        }

        @Override
        public void a(String text) {
            session.onRename(text);
        }

        @Override
        public void e() {
            //vanilla's recompute would write a repair cost and a repaired item; the result slot is the session's
        }

        @Override
        public ItemStack a(int slot, int button, InventoryClickType type, EntityHuman human) {
            if (slot == InventoryAnvilWindow.RESULT_SLOT) {
                session.onResultClick();
            }
            return ItemStack.a; //nothing moves: the icon stays put and no player item enters a slot that gets wiped
        }

        @Override
        public void b(EntityHuman human) {
            window.clear();
            super.b(human);
            session.onClosed();
        }

    }

}
