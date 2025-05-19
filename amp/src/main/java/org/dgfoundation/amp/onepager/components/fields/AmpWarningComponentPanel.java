/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;

/**
 * Display a warning message + dynamic values
 * 
 * @author mpostelnicu@gmail.com
 * @since July 25, 2013
 */
public class AmpWarningComponentPanel<T> extends AmpComponentPanel<String> {

    protected MultiLineLabel warningContent;
    

    public MultiLineLabel getWarning() {
        return warningContent;
    }

    public void setWarning(MultiLineLabel warning) {
        this.warningContent = warning;
    }

    public AmpWarningComponentPanel(String id, String fmName,
            IModel<String> warningContentModel) {
        super(id, warningContentModel, fmName);

        warningContent = new MultiLineLabel("warningContent", warningContentModel);
        add(warningContent);
    }
}
