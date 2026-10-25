package br.com.finalcraft.ecnmsutils.anvil;

/**
 * What {@link AnvilRequest#getOnInput()} answers for the text the player typed.
 */
public final class AnvilVerdict {

    private static final AnvilVerdict ACCEPT = new AnvilVerdict(true, "");

    private final boolean accepted;
    private final String message;

    private AnvilVerdict(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }

    /** Takes the answer: the slots are cleared and the window closes, without {@code onClose}. */
    public static AnvilVerdict accept() {
        return ACCEPT;
    }

    /**
     * Refuses the answer: the window stays open and {@code message} becomes the result item's name.
     *
     * @throws IllegalArgumentException when {@code message} is null or blank - a silent refusal looks like a frozen anvil
     */
    public static AnvilVerdict reject(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("A rejection needs a message: it becomes the result item's name, so the player "
                    + "learns why the answer was refused. Use AnvilVerdict.accept() to take the answer.");
        }
        return new AnvilVerdict(false, message);
    }

    public boolean isAccepted() {
        return accepted;
    }

    /** The rejection message; {@code ""} when accepted. */
    public String getMessage() {
        return message;
    }

}
