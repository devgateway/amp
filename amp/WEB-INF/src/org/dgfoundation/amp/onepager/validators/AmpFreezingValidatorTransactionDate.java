package org.dgfoundation.amp.onepager.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.digijava.module.aim.dbentity.AmpActivityFrozen;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

public class AmpFreezingValidatorTransactionDate extends AmpSemanticValidator<String> {
    /**
     * 
     */
    private static final long serialVersionUID = -912259426160326848L;

    public AmpFreezingValidatorTransactionDate() {

    }

    @Override
    public void semanticValidate(IValidatable<String> validatable) {
        if (validatable.getValue() == null || validatable.getValue().trim().equals("")) {
            return;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT));
        Date transactionDate;
        try {
            transactionDate = dateFormatter.parse(validatable.getValue());
        } catch (ParseException e) {
            // we should actually never reach here since were formating in the
            // model
            throw new RuntimeException("Date unparseable");
        }
        Boolean affectedByFreezing = org.apache.wicket.Session.get()
                .getMetaData(OnePagerConst.ACTIVITY_IS_AFFECTED_BY_FREEZING);
        if (validatable.getValue() == null || !affectedByFreezing) {
            return;
        }
        AmpActivityFrozen ampActivityFrozen = org.apache.wicket.Session.get()
                .getMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION);
        if (transactionDate.compareTo(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart()) < 0
                || transactionDate.compareTo(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd()) > 0) {
            ValidationError error = new ValidationError();
            error.addKey("AmpFreezingEventTransactionDateValidator");
            error.setVariable("openPeriodFrom",
                    dateFormatter.format(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart()));
            error.setVariable("openPeriodTo",
                    dateFormatter.format(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd()));
            validatable.error(error);
        }

    }
}