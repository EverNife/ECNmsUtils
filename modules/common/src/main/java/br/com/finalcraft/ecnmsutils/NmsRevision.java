package br.com.finalcraft.ecnmsutils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The CraftBukkit revisions this library has a bridge for, each with the releases that run it.
 */
public enum NmsRevision {
    V1_7_R4("1.7.10"),
    V1_12_R1("1.12", "1.12.1", "1.12.2"),
    V1_16_R3("1.16.4", "1.16.5"),
    V1_20_R1("1.20", "1.20.1"),
    V1_20_R2("1.20.2"),
    V1_21_R1("1.21", "1.21.1");

    private static final Pattern RELOCATED_REVISION = Pattern.compile("v\\d+_\\d+_R\\d+");

    private final List<String> releases;

    NmsRevision(String... releases) {
        this.releases = Arrays.asList(releases);
    }

    /** The package segment CraftBukkit relocates into for this revision: {@code v1_21_R1}. */
    public String getPackageSegment() {
        return "v" + name().substring(1);
    }

    /** The releases that run this revision, as {@code Bukkit.getBukkitVersion()} starts them. */
    public List<String> getReleases() {
        return releases;
    }

    /**
     * The revision of a server reporting these two strings - {@code Bukkit.getServer().getClass().getPackage().getName()}
     * and {@code Bukkit.getBukkitVersion()}.
     *
     * <p>A relocated CraftBukkit package decides alone: the NMS classes live under it, so a revision
     * that is not served stays unserved whatever release the server reports. Only the plain
     * {@code org.bukkit.craftbukkit} of 1.20.5 onwards leaves the reported release as the evidence.
     */
    public static Optional<NmsRevision> resolve(String craftBukkitPackage, String bukkitVersion) {
        String segment = craftBukkitPackage == null ? "" : craftBukkitPackage.substring(craftBukkitPackage.lastIndexOf('.') + 1);
        if (RELOCATED_REVISION.matcher(segment).matches()) {
            for (NmsRevision revision : values()) {
                if (revision.getPackageSegment().equals(segment)) {
                    return Optional.of(revision);
                }
            }
            return Optional.empty();
        }

        String release = bukkitVersion == null ? "" : bukkitVersion.split("-", 2)[0];
        for (NmsRevision revision : values()) {
            if (revision.releases.contains(release)) {
                return Optional.of(revision);
            }
        }
        return Optional.empty();
    }

    /** {@code "V1_7_R4 [1.7.10], V1_12_R1 [1.12, 1.12.1, 1.12.2], ..."} - for error messages. */
    static String describeServed() {
        StringBuilder served = new StringBuilder();
        for (NmsRevision revision : values()) {
            if (served.length() > 0) {
                served.append(", ");
            }
            served.append(revision.name()).append(' ').append(revision.releases);
        }
        return served.toString();
    }

    /** The bridge class of this revision, which lives in the era module the jar bundles. */
    String getBridgeClassName() {
        return "br.com.finalcraft.ecnmsutils." + getPackageSegment() + ".Nms" + name();
    }

    INMSUtils createBridge() throws ReflectiveOperationException {
        return (INMSUtils) Class.forName(getBridgeClassName()).getDeclaredConstructor().newInstance();
    }

}
