package br.com.finalcraft.ecnmsutils.v1_7_R4;

import br.com.finalcraft.ecnmsutils.NmsRevision;
import br.com.finalcraft.ecnmsutils.anvil.AbstractNmsAnvil;
import br.com.finalcraft.ecnmsutils.anvil.AnvilHandle;
import br.com.finalcraft.ecnmsutils.anvil.AnvilRequest;
import br.com.finalcraft.ecnmsutils.anvil.AnvilSession;
import br.com.finalcraft.ecnmsutils.anvil.InventoryAnvilWindow;
import net.minecraft.server.v1_7_R4.ContainerAnvil;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

final class AnvilV1_7_R4 extends AbstractNmsAnvil {

    /** The window type the 1.7.10 protocol gives the anvil. */
    private static final int ANVIL_WINDOW = 8;

    AnvilV1_7_R4() {
        super(NmsRevision.V1_7_R4);
    }

    @Override
    protected AnvilHandle openPrompt(Player player, AnvilRequest request) {
        return PromptContainer.open(player, request);
    }

    static final class PromptContainer extends ContainerAnvil {

        private final InventoryAnvilWindow window;
        private final AnvilSession session;

        private PromptContainer(Player player, final EntityPlayer handle, AnvilRequest request) {
            super(handle.inventory, handle.world, 0, 0, 0, handle);
            checkReachable = false; //no anvil block backs this window, so there is nothing to stand near
            windowId = handle.nextContainerCounter();
            //the 1.7.10 client draws its own heading: the title travels in the icon's lore
            window = new InventoryAnvilWindow(player, getBukkitView().getTopInventory(), request, true, () -> a = 0) {
                @Override
                public void showRejection(String message) {
                    super.showRejection(message);
                    //this client empties its result slot on click and ignores a lone slot update: re-send the
                    //window and its slots (the text field falls back to the icon's name - a client limit)
                    handle.playerConnection.sendPacket(new PacketPlayOutOpenWindow(windowId, ANVIL_WINDOW, "Repairing", 9, true));
                    handle.updateInventory(PromptContainer.this);
                }
            };
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
        public ItemStack clickItem(int slot, int button, int mode, EntityHuman human) {
            if (slot == InventoryAnvilWindow.RESULT_SLOT) {
                session.onResultClick();
            }
            return null; //nothing moves: the icon stays put and no player item enters a slot that gets wiped
        }

        @Override
        public void b(EntityHuman human) {
            window.clear();
            super.b(human);
            session.onClosed();
        }

    }

}
