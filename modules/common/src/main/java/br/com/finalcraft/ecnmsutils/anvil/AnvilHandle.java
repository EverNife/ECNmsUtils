package br.com.finalcraft.ecnmsutils.anvil;

/**
 * One open anvil prompt, as {@link NmsAnvil#open} returned it. Main thread only.
 */
public interface AnvilHandle {

    /** True while this prompt's window is the one the player has open. */
    boolean isOpen();

    /** Closes the window without an answer: {@code onClose} runs (once). No-op when already closed. */
    void close();

}
