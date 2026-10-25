package br.com.finalcraft.ecnmsutils.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * The {@link AnvilSession.Window} every revision shares, written against the Bukkit view of the anvil's three
 * slots. The revision adds only what needs NMS: the repair cost it pins to 0 after each result.
 */
public class InventoryAnvilWindow implements AnvilSession.Window {

    /** The anvil's result slot; 0 and 1 are the inputs. */
    public static final int RESULT_SLOT = 2;

    private final Player player;
    private final Inventory slots;
    private final AnvilRequest request;
    private final List<String> lore;
    private final Runnable afterResult;

    /**
     * @param slots        the Bukkit inventory over the anvil's three slots
     * @param titleInLore  true where the client draws no window title (before 1.14): the title goes in the icon's lore
     * @param afterResult  what the revision does after the result slot changes (pin the repair cost to 0)
     */
    public InventoryAnvilWindow(Player player, Inventory slots, AnvilRequest request, boolean titleInLore, Runnable afterResult) {
        this.player = player;
        this.slots = slots;
        this.request = request;
        this.lore = titleInLore && !request.getTitle().isEmpty() ? Collections.singletonList(request.getTitle()) : null;
        this.afterResult = afterResult;
    }

    /** Places the icon named after the initial text, shows that text as the result and returns the session. */
    public final AnvilSession start() {
        slots.setItem(0, named(request.getInitialText()));
        AnvilSession session = new AnvilSession(request, this);
        session.onRename(request.getInitialText());
        return session;
    }

    @Override
    public void showResult(String text) {
        slots.setItem(RESULT_SLOT, named(text));
        afterResult.run();
    }

    @Override
    public void showRejection(String message) {
        showResult(message);
    }

    @Override
    public void clearAndClose() {
        clear();
        player.closeInventory();
    }

    /** Empties the three slots, so a closing anvil has nothing to hand back to the player. */
    public final void clear() {
        for (int slot = 0; slot <= RESULT_SLOT; slot++) {
            slots.setItem(slot, null);
        }
    }

    private ItemStack named(String name) {
        ItemStack item = request.getIcon();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item; //air has no meta to carry a name
        }
        if (!name.isEmpty()) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

}
