package org.digijava.kernel.ampapi.endpoints.common.print;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class ScreenshotService {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotService.class);

    /**
     * Environment variable that points to the folder containing the chrome binary.
     */
    private static final String CHROME_PATH_ENV_NAME = "CHROME_PATH";

    private static final String CHROME_NO_SANDBOX_ENV_NAME = "CHROME_NO_SANDBOX";

    private ScreenshotService() {
    }

    public static void takeScreenshot(File html, File outputFile, int w, int h)
            throws IOException, InterruptedException {

        File chrome = new File(System.getenv(CHROME_PATH_ENV_NAME), "chrome");

        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(chrome.getAbsolutePath());
        String chromeNoSandbox = System.getenv(CHROME_NO_SANDBOX_ENV_NAME);
        if ("1".equals(chromeNoSandbox)) {
            cmd.add("--no-sandbox");
        }
        // Use explicit new headless mode (required for Chrome 112+).
        cmd.add("--headless=new");
        cmd.add("--disable-gpu");
        // Allow loading stylesheets from the AMP server when it uses an
        // internal/self-signed TLS certificate inside the container.
        cmd.add("--ignore-certificate-errors");
        // Allow loading resources from file:// pages (CSS, fonts).
        cmd.add("--allow-file-access-from-files");
        cmd.add(String.format("--window-size=%d,%d", w, h));
        cmd.add("--screenshot=" + outputFile.getAbsolutePath());
        cmd.add(html.getAbsolutePath());

        Process process = new ProcessBuilder()
                .command(cmd)
                .redirectErrorStream(true)
                .start();

        int exitValue = process.waitFor();

        if (logger.isDebugEnabled()) {
            String output = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
            if (output != null && !output.isEmpty()) {
                logger.debug("Process output:\n" + output);
            }
            logger.debug("test");
        }

        if (exitValue != 0) {
            throw new RuntimeException("Failed to create a screenshot. Chrome exited with code: " + exitValue);
        }
    }
}
