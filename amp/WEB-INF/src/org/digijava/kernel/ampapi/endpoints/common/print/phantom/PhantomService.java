package org.digijava.kernel.ampapi.endpoints.common.print.phantom;

import com.github.jarlakxen.embedphantomjs.ExecutionTimeout;
import com.github.jarlakxen.embedphantomjs.PhantomJSReference;
import com.github.jarlakxen.embedphantomjs.executor.PhantomJSFileExecutor;
import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by esoliani on 22/07/16.
 */
public class PhantomService {

    private static final String PHANTOM_SCRIPT_FILE = "phantom-script.js";
    private static final int TIMEOUT = 40;  // TODO make time configurable probably dynamic
    private static volatile boolean initialized = false;
    private static PhantomJSFileExecutor executor;
    private static File scriptFile;
    private static final Logger LOGGER = Logger.getLogger(PhantomService.class);

    public static String createImage(final String uri, final String imagePath, final Integer width,
                                     final Integer height, final String scriptPath)
            throws ExecutionException, InterruptedException {
        if (!initialized) {
            init();
        }
        return executor.execute(scriptFile, uri, imagePath, width.toString(), height.toString(), scriptPath).get();
    }

    private static synchronized void init() {
        try {
            if(!initialized) {
                LOGGER.info("Initializing the phantom service");
                executor = new PhantomJSFileExecutor(
                        PhantomJSReference.create()
                        .addCommandLineOptions("--ssl-protocol=any").build(),
                        new ExecutionTimeout(TIMEOUT, TimeUnit.SECONDS));
                scriptFile = new File(PhantomService.class.getResource(PHANTOM_SCRIPT_FILE).getFile());
                initialized = true;
            }
        } catch (Exception e) {
            LOGGER.error("Error initializing the phantom service", e);
        }
    }
}
