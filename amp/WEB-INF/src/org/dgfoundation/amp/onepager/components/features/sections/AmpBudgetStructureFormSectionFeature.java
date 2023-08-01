package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpBudgetSectionFormTableFeature;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

public class AmpBudgetStructureFormSectionFeature extends AmpFormSectionFeaturePanel{
    
    private static final long serialVersionUID = -7210378948067632773L;

    public AmpBudgetStructureFormSectionFeature(String id, String fmName,
            IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;
        AmpBudgetSectionFormTableFeature bsTable = new AmpBudgetSectionFormTableFeature(
                "bsTable", "Budget Structure", am,true);
        add(bsTable);
    }

}
