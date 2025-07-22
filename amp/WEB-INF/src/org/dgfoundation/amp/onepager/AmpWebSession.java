/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 4, 2010
 */
public class AmpWebSession extends WebSession {

    private static final long serialVersionUID = -3391413377263782681L;
    private boolean translatorMode;
    private boolean fmMode;

    public boolean isTranslatorMode() {
        return translatorMode;
    }

    public void setTranslatorMode(boolean translatorMode) {
        this.translatorMode = translatorMode;
    }

    public boolean isFmMode() {
        return fmMode;
    }

    public void setFmMode(boolean fmMode) {
        this.fmMode = fmMode;
    }

    /**
     * @param request
     */
    public AmpWebSession(Request request) {
        super(request);
        fmMode=false;
        translatorMode=false;
    }
    

}
