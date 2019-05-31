package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.field.APIFieldUtil.readFieldValueOrDefault;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

/**
 * Organization group of the donor funding must match organization group of the linked pledges.
 *
 * @author Octavian Ciubotaru
 */
public class PledgeOrgValidator implements ConstraintValidator {

    private static final String PLEDGE_FIELD_NAME =
            FieldMap.underscorify(ActivityFieldsConstants.Funding.Details.PLEDGE);

    public static final String DONOR_ORG_GROUP_ID = "donorOrgGrpId";
    public static final String FUNDING_PLEDGE_ID = "fundingPledgeId";

    public static final String FUNDING_ORGANIZATION_VALID_PRESENT_KEY = "funding_organization_group_valid";

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {

        AmpFunding funding = (AmpFunding) value;

        AmpOrganisation donor = funding.getAmpDonorOrgId();
        if (donor == null) {
            return true;
        }
        Long donorOrgGroupId = donor.getOrgGrpId().getAmpOrgGrpId();

        context.disableDefaultConstraintViolation();
        boolean valid = true;

        for (APIField transactionTypeField : type.getFieldsForFieldNameInternal("fundingDetails")) {
            Collection<AmpFundingDetail> transactions = transactionTypeField.getFieldAccessor().get(value);
            for (AmpFundingDetail transaction : transactions) {
                APIField pledgeField = transactionTypeField.getField(PLEDGE_FIELD_NAME);
                FundingPledges fundingPledge = readFieldValueOrDefault(pledgeField, transaction, null);

                if (fundingPledge != null) {
                    Long pledgeOrgGroupId = fundingPledge.getOrganizationGroup().getAmpOrgGrpId();
                    if (!donorOrgGroupId.equals(pledgeOrgGroupId)) {

                        context.buildConstraintViolation(ActivityErrors.FUNDING_PLEDGE_ORG_GROUP_MISMATCH)
                                .addPropertyNode(transactionTypeField.getFieldName())
                                .addPropertyNode(pledgeField.getFieldName())
                                .addAttribute(DONOR_ORG_GROUP_ID, donorOrgGroupId)
                                .addAttribute(FUNDING_PLEDGE_ID, fundingPledge.getId())
                                .addConstraintViolation();

                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return null;
    }
}
