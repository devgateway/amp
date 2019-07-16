package org.digijava.module.aim.dbentity;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.util.ISO8601DateSerializer;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AmpOfflineRelease implements Comparable<AmpOfflineRelease>, Cloneable {

    public static final String WINDOWS = "windows";
    public static final String MAC_OS = "osx";

    private static final String VER_REGEX = "(?<ver>\\d+\\.\\d+\\.\\d+(?:-\\w+)?)";
    private static final String OS_REGEX = "(?<os>" + WINDOWS + "|redhat|debian|" + MAC_OS + ")";
    private static final String ARCH_REGEX = "(?<arch>32|64)";
    private static final String UA_REGEX = "AMPOffline/" + VER_REGEX + " \\(" + OS_REGEX + "; " + ARCH_REGEX + "\\).*";
    private static final Pattern UA_PATTERN = Pattern.compile(UA_REGEX);

    private static final Comparator<AmpOfflineRelease> NATURAL = Comparator.comparing(AmpOfflineRelease::getVersion)
            .thenComparing(AmpOfflineRelease::getOs)
            .thenComparing(AmpOfflineRelease::getArch);

    private static final Map<String, String> OS_TO_EXT = new ImmutableMap.Builder<String, String>()
            .put(WINDOWS, "exe")
            .put("redhat", "rpm")
            .put("debian", "deb")
            .put(MAC_OS, "zip")
            .build();

    private Long id;

    @javax.validation.constraints.Pattern(regexp = VER_REGEX)
    private String version;

    @javax.validation.constraints.Pattern(regexp = OS_REGEX)
    private String os;

    @javax.validation.constraints.Pattern(regexp = ARCH_REGEX)
    private String arch;

    private boolean critical;

    @JsonSerialize(using = ISO8601DateSerializer.class)
    private Date date;

    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toFileName() {
        String criticalSuffix = critical ? "-critical" : "";
        return "amp-offline-" + version + criticalSuffix + "-" + arch + "." + OS_TO_EXT.get(os);
    }

    public static AmpOfflineRelease fromUserAgent(String userAgent) {
        Matcher matcher = UA_PATTERN.matcher(userAgent);
        if (matcher.matches()) {
            AmpOfflineRelease release = new AmpOfflineRelease();
            release.setVersion(matcher.group("ver"));
            release.setOs(matcher.group("os"));
            release.setArch(matcher.group("arch"));
            return release;
        } else {
            throw new IllegalArgumentException(
                    String.format("User agent %s does not match pattern %s.", userAgent, matcher.pattern()));
        }
    }

    @Override
    public int compareTo(AmpOfflineRelease o) {
        return Objects.compare(this, o, NATURAL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AmpOfflineRelease that = (AmpOfflineRelease) o;
        return Objects.equals(version, that.version)
                && Objects.equals(os, that.os)
                && Objects.equals(arch, that.arch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, os, arch);
    }

    public AmpOfflineRelease clone() {
        try {
            return (AmpOfflineRelease) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failure", e);
        }
    }
}
