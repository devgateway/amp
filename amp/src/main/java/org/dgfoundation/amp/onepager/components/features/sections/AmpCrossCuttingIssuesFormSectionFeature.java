/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * @author aartimon@dginternational.org since Oct 26, 2010
 */
public class AmpCrossCuttingIssuesFormSectionFeature extends
        AmpFormSectionFeaturePanel {

    private static final long serialVersionUID = -6654390083784446344L;

    public AmpCrossCuttingIssuesFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        
        add(new AmpTextAreaFieldPanel("equalOpportunity",
                new PropertyModel<String>(am, "equalOpportunity"),
                "Equal Opportunity", true));

        add(new AmpTextAreaFieldPanel("environment",
                new PropertyModel<String>(am, "environment"),
                "Environment", true));

        add(new AmpTextAreaFieldPanel("minorities",
                new PropertyModel<String>(am, "minorities"),
                "Minorities", true));
    }

}
