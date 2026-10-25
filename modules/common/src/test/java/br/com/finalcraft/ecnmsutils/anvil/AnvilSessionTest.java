package br.com.finalcraft.ecnmsutils.anvil;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnvilSessionTest {

    /** Records what the session asked of the window. */
    private static final class RecordingWindow implements AnvilSession.Window {
        final List<String> calls = new ArrayList<>();

        @Override
        public void showResult(String text) {
            calls.add("result:" + text);
        }

        @Override
        public void showRejection(String message) {
            calls.add("reject:" + message);
        }

        @Override
        public void clearAndClose() {
            calls.add("close");
        }
    }

    private final RecordingWindow window = new RecordingWindow();
    private final AtomicInteger closes = new AtomicInteger();
    private final List<String> inputs = new ArrayList<>();

    private AnvilSession session(Function<String, AnvilVerdict> answer) {
        AnvilRequest request = new AnvilRequest(text -> {
            inputs.add(text);
            return answer.apply(text);
        }).setInitialText("start").setOnClose(closes::incrementAndGet);
        return new AnvilSession(request, window);
    }

    @Test
    void acceptClosesWithoutFiringOnClose() {
        AnvilSession session = session(text -> AnvilVerdict.accept());

        session.onRename("Steve");
        session.onResultClick();
        session.onClosed(); //the container's close hook, run by clearAndClose on a real server

        assertEquals(Arrays.asList("result:Steve", "close"), window.calls);
        assertEquals(Collections.singletonList("Steve"), inputs);
        assertEquals(0, closes.get());
        assertFalse(session.isOpen());
    }

    @Test
    void rejectKeepsOpenAndShowsMessage() {
        AnvilSession session = session(text -> AnvilVerdict.reject("Not a number"));

        session.onRename(" 12a ");
        session.onResultClick();

        assertEquals(Arrays.asList("result: 12a ", "reject:Not a number"), window.calls);
        assertEquals(Collections.singletonList(" 12a "), inputs); //exactly as the client sent it
        assertTrue(session.isOpen());
        assertEquals(0, closes.get());
    }

    @Test
    void playerCloseFiresOnCloseOnce() {
        AnvilSession session = session(text -> AnvilVerdict.accept());

        session.onClosed();
        session.onClosed();
        session.close();
        session.onResultClick();

        assertEquals(1, closes.get());
        assertFalse(session.isOpen());
        assertTrue(window.calls.isEmpty(), window.calls.toString());
        assertTrue(inputs.isEmpty());
    }

    @Test
    void closeAfterAcceptIsNoOp() {
        AnvilSession session = session(text -> AnvilVerdict.accept());

        session.onResultClick();
        session.close();
        session.onClosed();

        assertEquals(Collections.singletonList("close"), window.calls);
        assertEquals(0, closes.get());
    }

    @Test
    void resultClickWithoutRenameUsesInitialText() {
        AnvilSession session = session(text -> AnvilVerdict.reject("again"));

        assertEquals("start", session.getCurrentText());
        session.onResultClick();

        assertEquals(Collections.singletonList("start"), inputs);
    }

    @Test
    void rejectWithoutMessageIsRefused() {
        assertThrows(IllegalArgumentException.class, () -> AnvilVerdict.reject(null));
        assertThrows(IllegalArgumentException.class, () -> AnvilVerdict.reject(""));
        String message = assertThrows(IllegalArgumentException.class, () -> AnvilVerdict.reject("  ")).getMessage();

        assertTrue(message.contains("AnvilVerdict.accept()"), message);
    }

    @Test
    void programmaticCloseClearsThenFiresOnClose() {
        AnvilSession session = session(text -> AnvilVerdict.accept());

        session.close();

        assertEquals(Collections.singletonList("close"), window.calls);
        assertEquals(1, closes.get());
    }

    @Test
    void requestWithoutOnInputIsRefused() {
        String message = assertThrows(NullPointerException.class, () -> new AnvilRequest(null)).getMessage();

        assertTrue(message.contains("AnvilVerdict.reject(message)"), message);
    }

}
