package org.digijava.kernel.services;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOfflineVersion implements Comparable<AmpOfflineVersion>, Serializable {

    private static final Pattern PATTERN = Pattern.compile("(\\d{1,9}).(\\d{1,9}).(\\d{1,9})(?:-(.*))?");

    private final int major;
    private final int minor;
    private final int patch;
    private final String suffix;

    @JsonCreator
    public AmpOfflineVersion(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new RuntimeException(String.format("Version %s does not match pattern %s.", str, PATTERN));
        }
        major = Integer.parseInt(matcher.group(1));
        minor = Integer.parseInt(matcher.group(2));
        patch = Integer.parseInt(matcher.group(3));
        suffix = matcher.group(4);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    @JsonValue
    public String toString() {
        String v = String.format("%d.%d.%d", major, minor, patch);
        if (suffix != null) {
            v += "-" + suffix;
        }
        return v;
    }

    @Override
    public int compareTo(AmpOfflineVersion o) {
        if (this == o) {
            return 0;
        }
        int c = Integer.compare(major, o.major);
        if (c != 0) {
            return c;
        }
        c = Integer.compare(minor, o.minor);
        if (c != 0) {
            return c;
        }
        return Integer.compare(patch, o.patch);
    }
}
