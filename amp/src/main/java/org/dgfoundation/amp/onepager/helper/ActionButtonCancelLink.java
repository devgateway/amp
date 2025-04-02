package org.dgfoundation.amp.onepager.helper;

import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;

public class ActionButtonCancelLink {
    
    private final AmpButtonField actionButton;
    private final AmpAjaxLinkField cancelActionLink;
    
    public ActionButtonCancelLink(AmpButtonField actionButton, AmpAjaxLinkField cancelActionLink) {
        this.actionButton = actionButton;
        this.cancelActionLink = cancelActionLink;
    }
    
    public AmpButtonField getActionButton() {
        return actionButton;
    }
    
    public AmpAjaxLinkField getCancelActionLink() {
        return cancelActionLink;
    }
}
