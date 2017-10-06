package org.dgfoundation.amp.onepager.components.fields;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpFundingSummaryPanel extends AmpComponentPanel<Void> implements
        AmpRequiredComponentContainer {
    Label typeOfAssistanceInfo;
    Label financingIdInfo;
    Label financingInstrumentInfo;
    Label fundingStatusInfo;
    Label modeOfPaymentInfo;

    public Label getTypeOfAssistanceInfo() {
        return typeOfAssistanceInfo;
    }

    public Label getFinancingIdInfo() {
        return financingIdInfo;
    }

    public Label getFinancingInstrumentInfo() {
        return financingInstrumentInfo;
    }

    public Label getFundingStatusInfo() {
        return fundingStatusInfo;
    }

    public Label getModeOfPaymentInfo() {
        return modeOfPaymentInfo;
    }

    @Override
    public List<FormComponent<?>> getRequiredFormComponents() {
        // TODO Auto-generated method stub
        return null;
    }

    public AmpFundingSummaryPanel(String id, String fmName,
            IModel<AmpFunding> fundingModel) throws Exception {
        super(id, fmName);

        typeOfAssistanceInfo = new Label("typeOfAssistanceInfo",
                new PropertyModel<AmpCategoryValue>(fundingModel,
                        "typeOfAssistance"));
        typeOfAssistanceInfo.setOutputMarkupId(true);
        add(typeOfAssistanceInfo);

        financingInstrumentInfo = new Label("financingInstrumentInfo",
                new PropertyModel<String>(fundingModel, "financingInstrument"));
        financingInstrumentInfo.setOutputMarkupId(true);
        add(financingInstrumentInfo);

        financingIdInfo = new Label("financingIdInfo",
                new PropertyModel<String>(fundingModel, "financingId"));
        financingIdInfo.setOutputMarkupId(true);
        add(financingIdInfo);

        fundingStatusInfo = new Label("fundingStatusInfo",
                new PropertyModel<String>(fundingModel, "fundingStatus"));
        add(fundingStatusInfo);
        modeOfPaymentInfo = new Label("modeOfPaymentInfo",
                new PropertyModel<String>(fundingModel, "modeOfPayment"));
        add(modeOfPaymentInfo);
    }
}
