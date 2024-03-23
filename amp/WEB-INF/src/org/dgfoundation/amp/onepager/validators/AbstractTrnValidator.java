/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.validation.ValidationError;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author mihai
 * Access AMP validation translation messages
 * @deprecated use i18n from wicket !!
 */
public abstract class AbstractTrnValidator<T> extends AmpSemanticValidator<T> {

    private Component component;
    private static final Logger logger = Logger
            .getLogger(AbstractTrnValidator.class);

    public AbstractTrnValidator(Component component) {
        this.component = component;
    }



    public ValidationError getTrnError(String errorString) {
        ValidationError error = new ValidationError();
        String genKey = TranslatorWorker.generateTrnKey(errorString);
        AmpAuthWebSession session = (AmpAuthWebSession) component.getSession();
        Site site = session.getSite();
        try {
            String translatedValue = TranslatorWorker.getInstance(genKey)
                    .translateFromTree(genKey, site,
                            session.getLocale().getLanguage(), errorString,
                            TranslatorWorker.TRNTYPE_LOCAL, null);

            error.setMessage(translatedValue);
        } catch (WorkerException e) {
            logger.error("Can't translate:", e);
        }
        return error;
    }
}
