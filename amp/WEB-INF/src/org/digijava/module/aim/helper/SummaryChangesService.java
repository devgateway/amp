package org.digijava.module.aim.helper;

import org.apache.commons.lang.time.DateUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Aldo Picca
 *
 */
public class SummaryChangesService {

    /**
     * Return a list of activities that were modified in the 24 hours prior to the date.
     * @param fromDate filter by date
     * @return list of activities.
     */
    public static List<AmpActivityVersion> getActivitiesChanged(Date fromDate) {
        List<AmpActivityVersion> activities = new ArrayList<AmpActivityVersion>();
        Session session = PersistenceManager.getRequestDBSession();

        String queryString = String.format(
                "select ampAct from %s ampAct "
                        + " WHERE ampAct.ampActivityId in ( "
                        + " select act.ampActivityId from %s act "
                        + " where draft = false "
                        + " and not amp_team_id is null "
                        + " and date_updated >= :fromDate "
                        + " and approval_status in ( %s )"
                        + " and exists (select actappr from %s actappr "
                        + "             where approval_status = '%s' "
                        + " and act.ampActivityGroup.ampActivityGroupId = actappr.ampActivityGroup.ampActivityGroupId ) "
                        + " ) "
                        + " and ampAct.team.id IN (select ampTeamId from %s WHERE isolated = false) ",
                AmpActivityVersion.class.getName(), AmpActivity.class.getName(), Constants
                        .ACTIVITY_NEEDS_APPROVAL_STATUS, AmpActivityVersion.class.getName(), Constants
                        .APPROVED_STATUS, AmpTeam.class.getName());

        Query query = session.createQuery(queryString);
        query.setDate("fromDate", DateUtils.addDays(fromDate, -1));

        return query.list();

    }

}
