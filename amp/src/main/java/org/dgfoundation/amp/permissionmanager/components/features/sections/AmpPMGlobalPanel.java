/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;

/**
 * 
 * @author dmihaila@dginternational.org since Jan 20, 2010
 */

public class AmpPMGlobalPanel extends AmpFieldPanel {
    public AmpPMGlobalPanel(String id, String fmName) {
        super(id, fmName, true);
        super.setOutputMarkupId(true);
    }
    
}
