/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * @author aartimon@dginternational.org
 * since Oct 19, 2010
 */
public class AmpCommentSimpleWrapper extends AmpFieldPanel {

    public AmpCommentSimpleWrapper(String id, String fmName, IModel<AmpActivityVersion> activityModel) {
        super(id, fmName, true);
        this.fmType = AmpFMTypes.MODULE;
        
        AmpCommentPanel acp = new AmpCommentPanel("comments", fmName, activityModel);
        acp.setOutputMarkupId(true);
        add(acp);
    }

}
