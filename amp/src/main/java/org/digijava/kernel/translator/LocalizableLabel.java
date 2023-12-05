package org.digijava.kernel.translator;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * A label that can be localized and supports parameterization. It uses underneath {@link MessageFormat} to format
 * the label.
 *
 * To retrieve translated label just call toString() method.
 *
 * Labels can be nested.
 *
 * @author Octavian Ciubotaru
 */
public class LocalizableLabel {

    private static Logger logger = Logger.getLogger(LocalizableLabel.class);

    private String label;
    private Object[] arguments;

    public LocalizableLabel(String label, Object... arguments) {
        this.label = label;
        this.arguments = arguments;
    }

    public String toString() {
        String translatedLabel = TranslatorWorker.translateText(label);
        if (arguments.length > 0) {
            try {
                return MessageFormat.format(translatedLabel, arguments);
            } catch (IllegalArgumentException e) {
                logger.error(String.format("Failed to format a label with pattern %s and arguments %s",
                        label, Arrays.asList(arguments)));
            }
        }
        
        return translatedLabel;
    }
}
