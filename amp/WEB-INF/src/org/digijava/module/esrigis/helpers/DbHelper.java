package org.digijava.module.esrigis.helpers;

/**
 * @author Diego Dimunzio
 */

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class DbHelper {
	private static Logger logger = Logger.getLogger(DbHelper.class);

	public static List<AmpActivity> getActivities(MapFilter filter)throws DgException {
		Long[] orgGroupIds = filter.getSelOrgGroupIds();
		List<AmpActivity> activities = null;
		Long[] orgIds = filter.getOrgIds();

		int transactionType = filter.getTransactionType();
		TeamMember teamMember = filter.getTeamMember();
		// apply calendar filter
		Long fiscalCalendarId = filter.getFiscalCalendarId();

		Date startDate = QueryUtil.getStartDate(fiscalCalendarId, filter.getYear().intValue() - filter.getYearsInRange());
		Date endDate = QueryUtil.getEndDate(fiscalCalendarId, filter.getYear().intValue());
		Long[] locationIds = filter.getSelLocationIds();
		boolean locationCondition = locationIds != null
				&& locationIds.length > 0 && !locationIds[0].equals(-1l);
		Long[] sectorIds = filter.getSelSectorIds();
		boolean sectorCondition = sectorIds != null && sectorIds.length > 0
				&& !sectorIds[0].equals(-1l);
		/*
		 * We are selecting sectors which are funded In selected year by the
		 * selected organization
		 */
		try {
			String oql = "select distinct act from ";
			oql += AmpFundingDetail.class.getName()
					+ " as fd inner join fd.ampFundingId f ";
			oql += " inner join f.ampActivityId act ";
			oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			if (locationCondition) {
				oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
			}
			if (sectorCondition) {
				oql += " inner join act.sectors actsec inner join actsec.sectorId sec ";
			}
			oql += "  where fd.adjustmentType = 1";
			if (filter.getTransactionType() < 2) {
				oql += " and fd.transactionType =:transactionType  ";
			} else {
				oql += " and (fd.transactionType =0 or  fd.transactionType =1) ";
			}
			if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
				if (orgGroupIds != null && orgGroupIds.length > 0
						&& orgGroupIds[0] != -1) {
					oql += QueryUtil.getOrganizationQuery(true, orgIds, orgGroupIds);
				}
			} else {
				oql += QueryUtil.getOrganizationQuery(false, orgIds, orgGroupIds);
			}
			oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";

			if (filter.getFromPublicView() != null
					&& filter.getFromPublicView() == true) {
				oql += QueryUtil.getTeamQueryManagement();
			} else {
				oql += QueryUtil.getTeamQuery(teamMember);
			}
			if (locationCondition) {
				oql += " and loc.id in (" + QueryUtil.getInStatement(locationIds) + ") ";
			}
			if (sectorCondition) {
				oql += " and sec.id in (" + QueryUtil.getInStatement(sectorIds) + ") ";
			}

			if (filter.getShowOnlyApprovedActivities() != null
					&& filter.getShowOnlyApprovedActivities()) {
				oql += ActivityUtil.getApprovedActivityQueryString("act");
			}

			Session session = PersistenceManager.getRequestDBSession();
			Query query = session.createQuery(oql);
			query.setDate("startDate", startDate);
			query.setDate("endDate", endDate);
			if (filter.getTransactionType() < 2) { // the option comm&disb is
				query.setLong("transactionType", transactionType);
			}

			activities = query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load activities from db", e);
		}
		return activities;

	}
}
