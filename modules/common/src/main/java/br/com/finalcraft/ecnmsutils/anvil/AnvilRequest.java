package br.com.finalcraft.ecnmsutils.anvil;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.Function;

/**
 * What one anvil prompt shows and how it answers. Built, handed to {@link NmsAnvil#open}, then read by the bridge;
 * only {@code onInput} is required.
 */
public final class AnvilRequest {

    private final Function<String, AnvilVerdict> onInput;
    private String title = "";
    private String initialText = "";
    private ItemStack icon = new ItemStack(Material.PAPER);
    private Runnable onClose = () -> { };

    /**
     * @param onInput receives the text exactly as the client sent it (no trim) when the player clicks the result
     *                slot, on the main thread; an exception it throws reaches the server's packet handling
     */
    public AnvilRequest(Function<String, AnvilVerdict> onInput) {
        this.onInput = Objects.requireNonNull(onInput, "onInput is what the anvil answers with - pass a "
                + "Function<String, AnvilVerdict> that returns AnvilVerdict.accept() or AnvilVerdict.reject(message).");
    }

    /**
     * The window title, {@code §} colors kept. Drawn on the window from 1.14 on, in the icon's lore before that;
     * empty (the default) leaves the heading blank from 1.14 on.
     */
    public AnvilRequest setTitle(String title) {
        this.title = title == null ? "" : title;
        return this;
    }

    /** The text the field starts with, as the icon's name; empty (the default) keeps the icon's own name. */
    public AnvilRequest setInitialText(String initialText) {
        this.initialText = initialText == null ? "" : initialText;
        return this;
    }

    /** The item in the first slot; copied, never mutated. Default: paper. */
    public AnvilRequest setIcon(ItemStack icon) {
        this.icon = Objects.requireNonNull(icon, "icon").clone();
        return this;
    }

    /** Runs once when the window ends without an accepted answer, on the main thread. Default: nothing. */
    public AnvilRequest setOnClose(Runnable onClose) {
        this.onClose = Objects.requireNonNull(onClose, "onClose");
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getInitialText() {
        return initialText;
    }

    /** A copy of the icon. */
    public ItemStack getIcon() {
        return icon.clone();
    }

    public Function<String, AnvilVerdict> getOnInput() {
        return onInput;
    }

    public Runnable getOnClose() {
        return onClose;
    }

}
