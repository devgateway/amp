package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.nireports.testcases.IHardcodedNames;

import java.util.Arrays;

/**
 * Contains the hardcoded mapping of some funding categories and columns, used by hardcoded funding cells.
 * See {@link FundingIdsMapper} for the way those are generated.
 * @author acartaleanu
 *
 */
public class HardcodedFundingNames extends IHardcodedNames {
            
    @Override
    protected void populateMaps() {
        category("donor_org_id", Arrays.asList(
                param("UNDP", 21695),
                param("World Bank", 21697),
                param("Norway", 21694),
                param("Finland", 21698),
                param("Ministry of Finance", 21699),
                param("USAID", 21696),
                param("Ministry of Economy", 21700),
                param("72 Local Public Administrations from RM", 21378),
                param("Water Org", 21701),
                param("Water Foundation", 21702)));
            category("funding_status_id", Arrays.asList(
                param("Partially acquired", 2090),
                param("Request sent no answer", 2091),
                param("No Information", 2089),
                param("Totally acquired", 2092)));
            category("agreement_id", Arrays.asList(
                param("Incomplete agreement en", 3),
                param("Agreement 1 Title", 1),
                param("Second agreement", 2)));
            category("recipient_role", Arrays.asList(
                param("Related Institution", 6),
                param("Regional Group", 12),
                param("Responsible Organization", 13),
                param("Donor", 1),
                param("Implementing Agency", 2),
                param("Beneficiary Agency", 5),
                param("Executing Agency", 4),
                param("Reporting Agency", 3),
                param("Sector Group", 11),
                param("Monitoring Agency", 7),
                param("Contracting Agency", 8)));
            category("pledge_id", Arrays.asList(
                param("Heavily used pledge", 6),
                param("ACVL Pledge Name 2", 4),
                param("free text name 2", 5),
                param("Test pledge 1", 3)));
            category("mode_of_payment_id", Arrays.asList(
                param("No Information", 2095),
                param("Direct payment", 2094),
                param("Cash", 2093),
                param("Non-Cash", 2096),
                param("Reimbursable", 2097)));
            category("terms_assist_id", Arrays.asList(
                param("default type of assistance", 2119),
                param("second type of assistance", 2124)));
            category("source_role", Arrays.asList(
                param("Related Institution", 6),
                param("Regional Group", 12),
                param("Responsible Organization", 13),
                param("Donor", 1),
                param("Implementing Agency", 2),
                param("Beneficiary Agency", 5),
                param("Executing Agency", 4),
                param("Reporting Agency", 3),
                param("Sector Group", 11),
                param("Monitoring Agency", 7),
                param("Contracting Agency", 8)));
            category("adjustment_type", Arrays.asList(
                param("Actual", 272),
                param("Planned", 271),
                param("Pipeline", 273)));
            category("recipient_org", Arrays.asList(
                param("UNDP", 21695),
                param("World Bank", 21697),
                param("Norway", 21694),
                param("Finland", 21698),
                param("Ministry of Finance", 21699),
                param("USAID", 21696),
                param("Ministry of Economy", 21700),
                param("72 Local Public Administrations from RM", 21378),
                param("Water Org", 21701),
                param("Water Foundation", 21702)));
            category("transaction_type", Arrays.asList(
                param("disbursement", 1),
                param("expenditure", 2),
                param("annual proposed project cost", 15),
                param("mtef projection", 3),
                param("pledges commitment", 5),
                param("release of funds", 8),
                param("commitment", 0),
                param("pledge", 7),
                param("pledges disbursement", 6),
                param("disbursement order", 4),
                param("estimated donor disbursement", 9)));
            category("financing_instrument_id", Arrays.asList(
                param("default financing instrument", 2120),
                param("second financing instrument", 2125)));
    }
}
