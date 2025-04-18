/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpInternalIdsFormTableFeature;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Organisation IDs from the activity form. This is also an AMP Feature
 * 
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpInternalIdsFormSectionFeature extends
        AmpFormSectionFeaturePanel {

    private static final long serialVersionUID = -6654390083784446344L;

    public AmpInternalIdsFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        AmpInternalIdsFormTableFeature internalIdsTable = new AmpInternalIdsFormTableFeature(
                "internalIdsTable", "Internal IDs", am);
        add(internalIdsTable);
    }

}
