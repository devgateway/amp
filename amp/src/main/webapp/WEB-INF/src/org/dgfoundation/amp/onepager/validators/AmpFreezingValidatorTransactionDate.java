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
        Boolean affectedByFreezing = org.apache.wicket.Session.get()
                .getMetaData(OnePagerConst.ACTIVITY_IS_AFFECTED_BY_FREEZING);
        if (validatable.getValue() == null || validatable.getValue().trim().equals("") || affectedByFreezing == null
                || !affectedByFreezing) {
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
        AmpActivityFrozen ampActivityFrozen = org.apache.wicket.Session.get()
                .getMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION);
        if (ampActivityFrozen != null) {
            if (ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart() == null
                    && ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd() == null) {
                validatable.error(getValidationError("AmpFreezingEventTransactionDateValidatorNoOpenPeriod", null, null,
                        dateFormatter));

            } else {
                if (ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd() == null) {
                    if (transactionDate.compareTo(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart()) < 0) {
                        validatable.error(getValidationError("AmpFreezingEventTransactionDateValidatorNoEnd",
                                ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart(), null, dateFormatter));
                    }
                } else {
                    if (transactionDate.compareTo(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart()) < 0
                            || transactionDate
                                    .compareTo(ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd()) > 0) {
                        validatable.error(getValidationError("AmpFreezingEventTransactionDateValidator",
                                ampActivityFrozen.getDataFreezeEvent().getOpenPeriodStart(),
                                ampActivityFrozen.getDataFreezeEvent().getOpenPeriodEnd(), dateFormatter));
                    }
                }
            }
        }
    }

    private ValidationError getValidationError(String key, Date start, Date end, SimpleDateFormat dateFormatter) {
        ValidationError error = new ValidationError();
        error.addKey(key);
        if (start != null) {
            error.setVariable("openPeriodFrom", dateFormatter.format(start));
        }
        if (end != null) {
            error.setVariable("openPeriodTo", dateFormatter.format(end));
        }
        return error;
    }
}
