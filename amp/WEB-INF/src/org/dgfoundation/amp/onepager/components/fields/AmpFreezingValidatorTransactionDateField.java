package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpFreezingValidatorTransactionDate;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.text.SimpleDateFormat;

/**
 * Transaction date validator
 * 
 * @author Julian de Anquin
 *
 */
public class AmpFreezingValidatorTransactionDateField extends AmpSimpleValidatorField<AmpFundingDetail, String> {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     * @param responseComponentInput
     * @param fmName
     */
    public AmpFreezingValidatorTransactionDateField(String id, IModel<AmpFundingDetail> ampFundingDetail,
            String fmName) {
        super(id, ampFundingDetail, fmName, new AmpFreezingValidatorTransactionDate());
        //we don't skype drafts
        this.setShouldValidateDrafts(true);
        hiddenContainer.setType(String.class);

    }

    public IModel<String> getHiddenContainerModel(IModel<AmpFundingDetail> fundingMOdel) {
        Model<String> model = new Model<String>() {

            /**
             * 
             */
            private static final long serialVersionUID = -3319915415186987566L;

            @Override
            public String getObject() {
                //Ideally this should be date but for that we need to extend
                //AmpHiddenFieldPanel to be able to override a formatter
                SimpleDateFormat dateFormatter = new SimpleDateFormat(
                        FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT));
                if (fundingMOdel.getObject().getTransactionDate() != null) {
                    return dateFormatter.format(fundingMOdel.getObject().getTransactionDate());
                } else {
                    return "";
                }
            }
        };
        return model;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
    }
}
