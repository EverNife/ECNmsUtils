package br.com.finalcraft.ecnmsutils.v1_21_R1;

import br.com.finalcraft.ecnmsutils.NmsRevision;
import br.com.finalcraft.ecnmsutils.anvil.AbstractNmsAnvil;
import br.com.finalcraft.ecnmsutils.anvil.AnvilHandle;
import br.com.finalcraft.ecnmsutils.anvil.AnvilRequest;
import br.com.finalcraft.ecnmsutils.anvil.AnvilSession;
import br.com.finalcraft.ecnmsutils.anvil.InventoryAnvilWindow;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.InventoryClickType;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

final class AnvilV1_21_R1 extends AbstractNmsAnvil {

    AnvilV1_21_R1() {
        super(NmsRevision.V1_21_R1);
    }

    @Override
    protected AnvilHandle openPrompt(Player player, AnvilRequest request) {
        return PromptContainer.open(player, request);
    }

    static final class PromptContainer extends ContainerAnvil {

        private final InventoryAnvilWindow window;
        private final AnvilSession session;

        private PromptContainer(Player player, AnvilRequest request) {
            super(((CraftPlayer) player).getHandle().nextContainerCounter(), ((CraftInventoryPlayer) player.getInventory()).getInventory());
            checkReachable = false; //no anvil block backs this window, so there is nothing to stand near
            //the Bukkit view reads the title once; CraftBukkit's opener re-parses it and has no component for ""
            setTitle(CraftChatMessage.fromString(request.getTitle().isEmpty() ? " " : request.getTitle())[0]);
            window = new InventoryAnvilWindow(player, bukkitView(this).getTopInventory(), request, false, () -> w.a(0));
            session = window.start();
        }

        static AnvilHandle open(Player player, AnvilRequest request) {
            PromptContainer container = new PromptContainer(player, request);
            InventoryView view = bukkitView(container);
            //CraftBukkit's opener: InventoryOpenEvent, open packet, active container, slot sync
            player.openInventory(view);
            if (player.getOpenInventory() != view) {
                container.session.onClosed(); //another plugin cancelled the InventoryOpenEvent
            }
            return container.session;
        }

        //through Container: ContainerAnvil's covariant getBukkitView names Spigot's CraftInventoryView, which Paper and
        //Arclight 1.21.1 replaced with CraftAnvilView - a NoSuchMethodError there
        private static InventoryView bukkitView(Container container) {
            return container.getBukkitView();
        }

        @Override
        public boolean a(String text) {
            session.onRename(text);
            return true;
        }

        @Override
        public void m() {
            //vanilla's recompute would write a repair cost and a repaired item; the result slot is the session's
        }

        @Override
        public void a(int slot, int button, InventoryClickType type, EntityHuman human) {
            if (slot == InventoryAnvilWindow.RESULT_SLOT) {
                session.onResultClick();
            } //nothing moves: the icon stays put and no player item enters a slot that gets wiped
        }

        @Override
        public void a(EntityHuman human) {
            //1.21 renamed the close hook from b(EntityHuman), which is now stillValid
            window.clear();
            super.a(human);
            session.onClosed();
        }

    }

}
