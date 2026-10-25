package br.com.finalcraft.ecnmsutils.anvil;

import br.com.finalcraft.ecnmsutils.NmsRevision;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * The part of every revision's {@link NmsAnvil} that knows no NMS: the thread and availability guards.
 *
 * <p>The revision keeps its container subclass as the nested class {@code PromptContainer} and reaches it only
 * from {@link #openPrompt}, so a server that rejects that subclass fails the probe instead of the whole bridge.
 */
public abstract class AbstractNmsAnvil implements NmsAnvil {

    private final NmsRevision revision;
    private volatile Throwable refusal;
    private volatile boolean probed;

    protected AbstractNmsAnvil(NmsRevision revision) {
        this.revision = revision;
    }

    @Override
    public final boolean isAvailable() {
        if (!probed) {
            try {
                Class.forName(getClass().getName() + "$PromptContainer", true, getClass().getClassLoader());
            } catch (ClassNotFoundException | LinkageError e) {
                refusal = e;
            }
            probed = true;
        }
        return refusal == null;
    }

    @Override
    public final AnvilHandle open(Player player, AnvilRequest request) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(request, "request");
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("NmsAnvil.open must run on the main thread (Bukkit.isPrimaryThread() is false). "
                    + "Schedule it with your plugin's scheduler and open there.");
        }
        if (!isAvailable()) {
            throw new IllegalStateException("This server's internals refused the anvil of " + revision + " (" + refusal + "). "
                    + "Ask through another channel - NMSUtils.get().anvil().isAvailable() tells you beforehand.", refusal);
        }
        player.closeInventory();
        return openPrompt(player, request);
    }

    /** Opens the prompt on the main thread, the player's previous inventory already closed. */
    protected abstract AnvilHandle openPrompt(Player player, AnvilRequest request);

}
