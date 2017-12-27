package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityContracts;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

import java.util.Date;

public class AmpActivityContractsField extends AmpFeaturePanel<Boolean> {

    public AmpActivityContractsField(String id, IModel<AmpActivityVersion> activityModel,
                                     final IModel<AmpActivityContracts> ampActivityContractsModel, String fmName) {
        super(id, fmName, true);
        this.fmType = AmpFMTypes.MODULE;


        AmpTextAreaFieldPanel contractDescrition =
                new AmpTextAreaFieldPanel("contractDescription", new PropertyModel<String>(ampActivityContractsModel,
                        "contractDescription"), "activity "
                        + "contract description",
                        false, false, false,
                        true, true);
        add(contractDescrition);

        AmpDatePickerFieldPanel contractDate = new AmpDatePickerFieldPanel(
                "contractDate", new PropertyModel<Date>(ampActivityContractsModel,
                "contractDate"), null,
                "Activity Contract Name");
        add(contractDate);

        AmpTextFieldPanel<Double>
                contractAmount = new AmpTextFieldPanel<Double>("contractAmount",
                new PropertyModel<Double>(ampActivityContractsModel, "contractAmount"), "Activity contract ammount",
                false,
                true);
        add(contractAmount);

    }
}
