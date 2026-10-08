package br.com.finalcraft.ecnmsutils;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.Optional;

/**
 * Entry point: the NMS bridge of the running server.
 */
public final class NMSUtils {

    private static volatile INMSUtils current;

    private NMSUtils() {
    }

    /**
     * The bridge for the running server, resolved on first call and kept for the life of the JVM.
     *
     * <p>Never {@code null}. On a server this library does not serve - or with no Bukkit server at
     * all - it is a bridge whose every subject throws {@link UnsupportedOperationException} naming the
     * server it saw. Code that can run there branches on {@link #isSupported()} first.
     */
    public static INMSUtils get() {
        INMSUtils bridge = current;
        if (bridge == null) {
            synchronized (NMSUtils.class) {
                if (current == null) {
                    current = load();
                }
                bridge = current;
            }
        }
        return bridge;
    }

    /** {@code true} when {@link #get()} is a working bridge for this server. */
    public static boolean isSupported() {
        return !(get() instanceof UnsupportedNms);
    }

    /** The revision being served, or empty when {@link #isSupported()} is {@code false}. */
    public static Optional<NmsRevision> revision() {
        return isSupported() ? Optional.of(get().revision()) : Optional.empty();
    }

    private static INMSUtils load() {
        String craftBukkitPackage;
        String bukkitVersion;
        try {
            Server server = Bukkit.getServer();
            if (server == null) {
                return new UnsupportedNms("no Bukkit server is running", null);
            }
            craftBukkitPackage = server.getClass().getPackage().getName();
            bukkitVersion = Bukkit.getBukkitVersion();
        } catch (LinkageError e) {
            return new UnsupportedNms("the Bukkit API is not on the classpath", e);
        }

        String detected = craftBukkitPackage + " / " + bukkitVersion;
        Optional<NmsRevision> revision = NmsRevision.resolve(craftBukkitPackage, bukkitVersion);
        if (!revision.isPresent()) {
            return new UnsupportedNms("the server " + detected + " runs a revision this library has no bridge for", null);
        }

        try {
            return revision.get().createBridge();
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            return new UnsupportedNms("the " + revision.get() + " bridge failed to load on " + detected, e);
        }
    }

}
