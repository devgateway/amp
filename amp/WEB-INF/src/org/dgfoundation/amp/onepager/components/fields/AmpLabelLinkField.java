package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.basic.Label;

/**
 *
 * @author aartimon@developmentgateway.org
 * @since 22 Feb 2013
 */
public abstract class AmpLabelLinkField extends AmpLinkField {

    public AmpLabelLinkField(String id, String fmName, String labelCaption) {
        super(id, fmName, true, true);
        Label label = new Label("label", labelCaption);
        getLink().add(label);

    }
}
