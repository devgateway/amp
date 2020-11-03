/**
 * 
 */
package org.digijava.module.aim.annotations.interchange;


import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Stores activity fields related constants used in annotations for reuse
 * @author Nadejda Mandrescu
 */
public class ActivityFieldsConstants {
    public static final String AMP_ACTIVITY_ID = "Internal ID";
    public static final String CREATED_DATE = "Creation Date";
    public static final String UPDATE_DATE = "Update Date";
    public static final String PROJECT_CODE = "Project Code";
    public static final String AMP_ID = "AMP Id";
    public static final String TEAM = "Team";
    public static final String WORKSPACES_EDIT = "Workspaces Edit";
    public static final String PROJECT_TITLE = "Project Title";
    public static final String IS_DRAFT = "Is Draft";
    public static final String CHANGE_TYPE = "Change Type";
    public static final String LAST_IMPORTED_AT = "Last Imported At";
    public static final String LAST_IMPORTED_BY = "Last Imported By";
    public static final String CREATED_BY = "Created By";
    public static final String APPROVED_BY = "Approved By";
    public static final String APPROVAL_DATE = "Approval Date";
    public static final String APPROVAL_STATUS = "Approval Status";
    public static final String MODIFIED_BY = "Modified By";
    public static final String ACTIVITY_TYPE = "Activity Type";
    public static final String DONOR_CONTACT = "Donor Contact Information";
    public static final String PROJECT_COORDINATOR_CONTACT = "Project Coordinator Contact Information";
    public static final String SECTOR_MINISTRY_CONTACT = "Sector Ministry Contact Information";
    public static final String MOFED_CONTACT = "Mofed Contact Information";
    public static final String IMPL_EXECUTING_AGENCY_CONTACT = "Implementing/Executing Agency Contact Information";
    public static final String PRIMARY_CONTACT = "Mark as primary";
    public static final String FUNDINGS = "Fundings";
    public static final String ADJUSTMENT_TYPE = "adjustment_type";
    public static final String FUNDING_DETAILS = "Funding Details";
    public static final String MTEF_PROJECTIONS = "MTEF Projections";
    public static final String LOCATIONS = "Locations";
    public static final String IMPLEMENTATION_LEVEL = "Implementation Level";
    public static final String IMPLEMENTATION_LOCATION = "Implementation Location";
    public static final String ORG_ROLE = "Org. Role";
    public static final String COMPONENTS = "Components";
    public static final String COMPONENT_TITLE = "Component Title";
    public static final String COMPONENT_DESCRIPTION = "Description";
    public static final String COMPONENT_TYPE = "Component Type";
    public static final String COMPONENT_ORGANIZATION = "Component Organization";
    public static final String COMPONENT_SECOND_REPORTING_ORGANIZATION = "Component Second Responsible Organization";
    public static final String COMPONENT_FUNDING = "Funding";
    public static final String COMPONENT_FUNDING_CURRENCY = "Currency";
    public static final String COMPONENT_FUNDING_DESCRIPTION = "Description";
    public static final String COMPONENT_FUNDING_AMOUNT = "Amount";
    public static final String COMPONENT_FUNDING_TRANSACTION_DATE = "Transaction Date";
    public static final String COMPONENT_FUNDING_ADJUSTMENT_TYPE = CategoryConstants.ADJUSTMENT_TYPE_NAME;
    public static final String COMPONENT_FUNDING_TRANSACTION_TYPE = "Transaction Type";
    public static final String DONOR_ORGANIZATION = "Donor Organization";
    public static final String RESPONSIBLE_ORGANIZATION = "Responsible Organization";
    public static final String EXECUTING_AGENCY = "Executing Agency";
    public static final String IMPLEMENTING_AGENCY = "Implementing Agency";
    public static final String BENEFICIARY_AGENCY = "Beneficiary Agency";
    public static final String CONTRACTING_AGENCY = "Contracting Agency";
    public static final String REGIONAL_GROUP = "Regional Group";
    public static final String SECTOR_GROUP = "Sector Group";
    public static final String ACTIVITY_GROUP = "activity_group";
    public static final String VERSION = "version";
    public static final String DISASTER_RESPONSE = "Disaster Response";

    public static final String NATIONAL_PLAN_OBJECTIVE = "National Plan Objective";
    public static final String PRIMARY_PROGRAMS = "Primary Programs";
    public static final String SECONDARY_PROGRAMS = "Secondary Programs";
    public static final String TERTIARY_PROGRAMS = "Tertiary Programs";

    public static final String PRIMARY_SECTORS = "Primary Sectors";
    public static final String SECONDARY_SECTORS = "Secondary Sectors";
    public static final String TERTIARY_SECTORS = "Tertiary Sectors";
    public static final String TAG_SECTORS = "Tag Sectors";
    
    public static final String IATI_IDENTIFIER = "IATI Identifier";
    public static final String UUID = "UUID";

    public static final class Locations {
        private Locations() {
        }

        public static final String LOCATION = "Location";
    }

    public static final class Funding {
        private Funding() {
        }

        public static final class Details {
            private Details() {
            }

            public static final String PLEDGE = "Pledge";
        }
    }

    public static final class RegionalFunding {
        private RegionalFunding() {
        }

        public static final String LOCATION = "Region Location";
    }

    public static final Map<String, String> ORG_ROLE_CODES = new ImmutableMap.Builder<String, String>()
            .put(FieldMap.underscorify(DONOR_ORGANIZATION), Constants.FUNDING_AGENCY)
            .put(FieldMap.underscorify(RESPONSIBLE_ORGANIZATION), Constants.RESPONSIBLE_ORGANISATION)
            .put(FieldMap.underscorify(EXECUTING_AGENCY), Constants.EXECUTING_AGENCY)
            .put(FieldMap.underscorify(IMPLEMENTING_AGENCY), Constants.IMPLEMENTING_AGENCY)
            .put(FieldMap.underscorify(BENEFICIARY_AGENCY), Constants.BENEFICIARY_AGENCY)
            .put(FieldMap.underscorify(CONTRACTING_AGENCY), Constants.CONTRACTING_AGENCY)
            .put(FieldMap.underscorify(REGIONAL_GROUP), Constants.REGIONAL_GROUP)
            .put(FieldMap.underscorify(SECTOR_GROUP), Constants.SECTOR_GROUP)
            .build();
}
