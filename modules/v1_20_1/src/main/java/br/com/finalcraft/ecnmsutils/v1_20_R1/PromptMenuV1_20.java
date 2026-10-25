package br.com.finalcraft.ecnmsutils.v1_20_R1;

import br.com.finalcraft.ecnmsutils.anvil.AnvilHandle;
import br.com.finalcraft.ecnmsutils.anvil.AnvilRequest;
import br.com.finalcraft.ecnmsutils.anvil.AnvilSession;
import br.com.finalcraft.ecnmsutils.anvil.InventoryAnvilWindow;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAnvil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

/**
 * The anvil prompt of 1.20.1 and 1.20.2, whose NMS names are the same: each revision's container subclass
 * forwards its hooks here and supplies only what lives in its own CraftBukkit package (the player's handle and
 * inventory, the title component).
 */
public final class PromptMenuV1_20 {

    private final InventoryView view;
    private final InventoryAnvilWindow window;
    private final AnvilSession session;

    /** Call from the container's constructor, after {@code super}. */
    public PromptMenuV1_20(ContainerAnvil container, Player player, AnvilRequest request, IChatBaseComponent title) {
        container.setTitle(title); //the Bukkit view reads the title once, on its creation
        //through Container: ContainerAnvil's covariant getBukkitView names 1.20.1's CraftInventoryView, absent on 1.20.2
        view = ((Container) container).getBukkitView();
        window = new InventoryAnvilWindow(player, view.getTopInventory(), request, false, () -> container.w.a(0));
        session = window.start();
    }

    /** The text CraftBukkit's opener can parse back: it has no component for an empty title. */
    public static String titleText(AnvilRequest request) {
        return request.getTitle().isEmpty() ? " " : request.getTitle();
    }

    public AnvilHandle open(Player player) {
        //CraftBukkit's opener: InventoryOpenEvent, open packet, active container, slot sync - and none of the
        //obfuscated player fields that differ between 1.20.1 and 1.20.2
        player.openInventory(view);
        if (player.getOpenInventory() != view) {
            session.onClosed(); //another plugin cancelled the InventoryOpenEvent
        }
        return session;
    }

    public void rename(String text) {
        session.onRename(text);
    }

    /** Every click is swallowed - the icon stays put and no player item enters a slot that gets wiped. */
    public void click(int slot) {
        if (slot == InventoryAnvilWindow.RESULT_SLOT) {
            session.onResultClick();
        }
    }

    /** Before vanilla's close hook: nothing left in the slots to hand back. */
    public void beforeClose() {
        window.clear();
    }

    public void afterClose() {
        session.onClosed();
    }

}
