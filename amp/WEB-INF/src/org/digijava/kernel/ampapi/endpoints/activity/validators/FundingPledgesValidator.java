package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

/**
 * Check if activity.funding.fundingDetails.pledge has the same organization group 
 * used in activity.funding.donor_organization_id 
 *
 * @author Viorel Chihai
 */
public class FundingPledgesValidator extends InputValidator {

    private static final String FUNDING_PATH = "fundings";
    private static final String SRC_ORG = "donor_organization_id";

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        return true;
    }

    public boolean isPledgeValid(ObjectImporter importer, Object pledgeObj) {
        Map<String, Object> fundingJson = (Map<String, Object>) importer.getBranchJsonVisitor().get(FUNDING_PATH);
        if (pledgeObj != null && pledgeObj instanceof Number) {
            Long id = getLong(pledgeObj);
            FundingPledges pledge = (FundingPledges) PersistenceManager.getSession().load(FundingPledges.class, id);
            if (pledge != null) {
                Long pledgeOrgGrpId = pledge.getOrganizationGroup().getAmpOrgGrpId();
                
                Object donorObj = fundingJson.get(SRC_ORG);
                if (donorObj != null && donorObj instanceof Number) {
                    Long donorId = getLong(donorObj);
                    AmpOrganisation donor = (AmpOrganisation) PersistenceManager.getSession()
                            .load(AmpOrganisation.class, donorId);
                    
                    if (donor != null) {
                        Long donorOrgGrpId = donor.getOrgGrpId().getAmpOrgGrpId();
                        
                        return pledgeOrgGrpId == donorOrgGrpId;
                    }
                }
            }
        }
        
        return true;
    }

    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FUNDING_PLEDGE_ORG_GROUP_MISMATCH;
    }
}
