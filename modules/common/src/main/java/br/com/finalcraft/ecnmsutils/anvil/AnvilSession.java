package br.com.finalcraft.ecnmsutils.anvil;

/**
 * The era-neutral life of one anvil prompt. The revision's container forwards its four events here (rename,
 * result click, close, programmatic close) and lets this class decide what the {@link Window} shows. Every call
 * arrives on the main thread; the session is the {@link AnvilHandle} the caller gets.
 */
public final class AnvilSession implements AnvilHandle {

    /** What the revision does to the real window; every call arrives on the main thread. */
    public interface Window {

        /** The result slot shows the icon named {@code text}, at repair cost 0. */
        void showResult(String text);

        /** The result slot shows the icon named {@code message}; the window stays open. */
        void showRejection(String message);

        /** Empties the three slots, then closes the window. */
        void clearAndClose();

    }

    private final AnvilRequest request;
    private final Window window;
    private String currentText;
    private boolean accepted;
    private boolean closed;

    public AnvilSession(AnvilRequest request, Window window) {
        this.request = request;
        this.window = window;
        this.currentText = request.getInitialText();
    }

    /** The client's rename packet: keeps {@code text} as the answer so far and shows it in the result slot. */
    public void onRename(String text) {
        if (closed) {
            return;
        }
        currentText = text;
        window.showResult(text);
    }

    /** The player clicked the result slot: {@code onInput} decides. */
    public void onResultClick() {
        if (closed) {
            return;
        }
        AnvilVerdict verdict = request.getOnInput().apply(currentText);
        if (closed) {
            return; //onInput closed the handle itself (and may have opened another window we must not touch)
        }
        if (verdict.isAccepted()) {
            accepted = true;
            window.clearAndClose();
            closed = true; //the window's close hook may already have run; nothing else fires either way
        } else {
            window.showRejection(verdict.getMessage());
        }
    }

    /** The container's close hook, or an open that never happened: runs {@code onClose} once, unless accepted. */
    public void onClosed() {
        if (closed) {
            return;
        }
        closed = true;
        if (!accepted) {
            request.getOnClose().run();
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        window.clearAndClose();
        onClosed();
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    /** The answer so far: the initial text until the first rename. */
    public String getCurrentText() {
        return currentText;
    }

}
