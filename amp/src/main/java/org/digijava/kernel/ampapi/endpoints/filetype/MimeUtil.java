package org.digijava.kernel.ampapi.endpoints.filetype;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Octavian Ciubotaru
 */
public final class MimeUtil {

    private MimeUtil() {
    }

    public static String detectMimeType(File file, String defaultMimeType) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            return defaultMimeType;
        }
    }
}
