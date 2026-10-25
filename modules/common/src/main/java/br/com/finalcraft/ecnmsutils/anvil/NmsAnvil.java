package br.com.finalcraft.ecnmsutils.anvil;

import org.bukkit.entity.Player;

/**
 * A virtual anvil used as a text prompt: no anvil block, no repair cost, no item ever handed to the player.
 */
public interface NmsAnvil {

    /**
     * Whether this revision's anvil classes link on the running server. False means a server whose internals
     * differ from the CraftBukkit this bridge was written against (a hybrid whose remapper rejects the container
     * subclass, for instance); the caller then asks through another channel. Probed once, on the first call;
     * any thread.
     */
    boolean isAvailable();

    /**
     * Opens a virtual anvil for {@code player}: the request's icon in the first slot, named after its initial
     * text (what the client pre-fills in the text field), the title on the window from 1.14 on and in the icon's
     * lore before that. Whatever the player types reaches the request's {@code onInput} when they click the
     * result slot; {@link AnvilVerdict#accept()} clears the slots and closes the window,
     * {@link AnvilVerdict#reject(String)} keeps it open with the message as the result item's name. Repair cost is
     * always 0, no item ever reaches the player's inventory and every click inside the window is swallowed.
     *
     * <p>Main thread only. Closes whatever inventory the player has open first. Both callbacks run on the main
     * thread. An {@code InventoryOpenEvent} cancelled by another plugin returns a handle that is already closed,
     * after running {@code onClose}.
     *
     * @throws IllegalStateException off the main thread, or when {@link #isAvailable()} is false
     */
    AnvilHandle open(Player player, AnvilRequest request);

}
