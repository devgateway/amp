package org.dgfoundation.amp.onepager.components.features.sections;

import java.text.NumberFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.dgfoundation.amp.onepager.components.features.tables.AmpBudgetSectionFormTableFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpProgramFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.TotalBudgetStructureUpdateEvent;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.ProgramUtil;

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
