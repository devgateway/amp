package org.digijava.kernel.xmlpatches;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class AmpFilterDateUpdateJob {

    protected Logger logger = LoggerFactory.getLogger(AmpFilterDateUpdateJob.class);

    private DateFormat oldFormat = new SimpleDateFormat(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT));
    private SimpleDateFormat newFormat = new SimpleDateFormat(AmpARFilter.SDF_IN_FORMAT_STRING);

    private void updateAllDates() {
        PersistenceManager.doWorkInTransaction(connection -> {
            Map<Long, String> states = SQLUtils.collectKeyValue(connection,
                    "select id, \"value\" from amp_filter_data where property_name in (\n" +
                    "'fromDate', 'toDate',\n" +
                    "'fromActivityStartDate', 'toActivityStartDate',\n" +
                    "'fromActivityActualCompletionDate', 'toActivityActualCompletionDate',\n" +
                    "'fromActivityFinalContractingDate', 'toActivityFinalContractingDate',\n" +
                    "'fromProposedApprovalDate', 'toProposedApprovalDate',\n" +
                    "'fromEffectiveFundingDate', 'toEffectiveFundingDate',\n" +
                    "'fromFundingClosingDate', 'toFundingClosingDate')");
            for (Map.Entry<Long, String> state : states.entrySet()) {
                updateDate(connection, state.getKey(), state.getValue());
            }
        });
    }

    private void updateDate(Connection connection, Long id, String oldDate) {
        if (StringUtils.isNotEmpty(oldDate)) {
            try {
                String newDate = newFormat.format(oldFormat.parse(oldDate));
                if (!newDate.equals(oldDate)) {
                    SQLUtils.executeQuery(connection,
                            String.format("update amp_filter_data set \"value\"=%s where id=%d",
                            SQLUtils.stringifyObject(newDate), id));
                }
            } catch (ParseException e) {
                logger.error(String.format("Failed to convert date from amp_filter_data, id=%d, date=%s, reason=%s",
                        id, oldDate, e.getMessage()));
            }
        }
    }

    public static void run() {
        new AmpFilterDateUpdateJob().updateAllDates();
    }
}
