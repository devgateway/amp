/*
 * FilterDesktopActivities.java
 * Created: 29-May-2006
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.form.DesktopForm;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Commitments;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Sector;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DesktopUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

/**
 * @deprecated
* FilterDesktopActivities.java
* TODO description here
* @author mihai
* @package org.digijava.module.aim.action
* @since 19.12.2007
 */
public class FilterDesktopActivities extends Action {

	private static Logger logger = Logger.getLogger(FilterDesktopActivities.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		TeamMember tm  = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		ArrayList activities = null;
		DesktopForm dForm = (DesktopForm) form;
		if ("true".equals(dForm.getResetFliters())) {

			activities = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
		} else {
			Collection temp = DesktopUtil.getDesktopActivities(tm.getTeamId(),tm.getMemberId(),
					tm.getTeamHead());

			activities = new ArrayList(temp);
			long calId = dForm.getFltrCalendar();
			//AMP-2212
			AmpFiscalCalendar fiscalCal=FiscalCalendarUtil.getAmpFiscalCalendar(calId);
			
			if (dForm.getFltrFrmYear() > 0 ||
					dForm.getFltrToYear() > 0) {

				int fromYear = (dForm.getFltrFrmYear() > 0) ? dForm.getFltrFrmYear() : 0;
				int toYear = (dForm.getFltrToYear() > 0) ? dForm.getFltrToYear() : 9999;

				if (fiscalCal.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()) ) {

					for (int i = 0;i < activities.size();i ++) {
						AmpProject proj = (AmpProject) activities.get(i);
						Collection newComm = new ArrayList();
						if (proj.getCommitmentList() != null) {
							Iterator itr = proj.getCommitmentList().iterator();
							while (itr.hasNext()) {
								Commitments comm = (Commitments) itr.next();
								GregorianCalendar gc = new GregorianCalendar();
								gc.setTime(comm.getTransactionDate());
								EthiopianCalendar ethCal = (new EthiopianCalendar()).getEthiopianDate(gc);
								if (ethCal.ethFiscalYear >= fromYear && ethCal.ethFiscalYear <= toYear) {
									newComm.add(comm);
								}
							}
						}
						proj.setCommitmentList(newComm);
					}
				} else {
					for (int i = 0;i < activities.size();i ++) {
						AmpProject proj = (AmpProject) activities.get(i);
						Collection newComm = new ArrayList();
						if (proj.getCommitmentList() != null) {
							Iterator itr = proj.getCommitmentList().iterator();
							while (itr.hasNext()) {
								Commitments comm = (Commitments) itr.next();
								Date tDate = comm.getTransactionDate();
								Date calStDate = FiscalCalendarUtil.getCalendarStartDate(new Long(calId),fromYear);
								Date calEdDate = FiscalCalendarUtil.getCalendarEndDate(new Long(calId),toYear);

								if ((tDate.after(calStDate) || (tDate.equals(calStDate)))
										&& (tDate.before(calEdDate) || (tDate.equals(calEdDate)))) {
									newComm.add(comm);
								}
							}
						}
						proj.setCommitmentList(newComm);
					}
				}

			}
/*			boolean flag = false;
			if (dForm.getFltrDonor() != null
					&& dForm.getFltrDonor().length > 0) {
				// Filter activities based on Donors
				long dnr[] = dForm.getFltrDonor();
				boolean allSelected = false;
				for (int i = 0; i < dnr.length;i++) {
					if (dnr[i] == -1) {
						allSelected = true;
						break;
					}
				}

				if (!allSelected) {
					if (activities != null && activities.size() > 0) {
						for (int i = 0;i < activities.size();i ++) {
							AmpProject proj = (AmpProject) activities.get(i);
							Iterator itr = proj.getDonor().iterator();
							while (itr.hasNext()) {
								AmpProjectDonor pDnr = (AmpProjectDonor) itr.next();
								for (int j = 0;j < dnr.length;j ++) {
									if (pDnr.getAmpDonorId().longValue() == dnr[j]) {
										flag = true;
										break;
									}
								}
								if (flag) break;

							}
							if (!flag) {
								activities.remove(proj);
								i--;
							}
							flag = false;
						}
					}
				}
			}
*/
			boolean flag = false;
			if (dForm.getFltrDonor() != null
					&& dForm.getFltrDonor().length > 0) {
				// Filter activities based on Donor Groups
				long dnr[] = dForm.getFltrDonor();
				boolean allSelected = false;
				for (int i = 0; i < dnr.length;i++) {
					if (dnr[i] == -1) {
						allSelected = true;
						break;
					}
				}

				if (!allSelected) {
					if (activities != null && activities.size() > 0) {
						for (int i = 0;i < activities.size();i ++) {
							AmpProject proj = (AmpProject) activities.get(i);
							Iterator itr = proj.getDonor().iterator();
							while (itr.hasNext()) {
								AmpProjectDonor pDnr = (AmpProjectDonor) itr.next();
								AmpOrgGroup grp = DbUtil.getAmpOrgGroup(pDnr.getAmpDonorId());
								
								for (int j = 0;j < dnr.length;j ++) {
									if (grp.getAmpOrgGrpId().longValue() == dnr[j]) {
										flag = true;
										break;
									}
								}
								if (flag) break;

							}
							if (!flag) {
								activities.remove(proj);
								i--;
							}
							flag = false;
						}
					}
				}
			}

			if (dForm.getFltrStatus() != null &&
					dForm.getFltrStatus().length > 0) {
				// Filter activities based on Status
				long sts[] = dForm.getFltrStatus();
				boolean allSelected = false;
				for (int i = 0; i < sts.length;i++) {
					if (sts[i] == -1) {
						allSelected = true;
						break;
					}
				}
				if (!allSelected) {
					if (activities != null && activities.size() > 0) {
						for (int i = 0;(i < activities.size() && i >= 0);i ++) {
							AmpProject proj = (AmpProject) activities.get(i);
							for (int j = 0;j < sts.length;j ++) {
								if (proj.getStatusId().longValue() != sts[j]) {
									activities.remove(proj);
									i--;
								}
							}
						}
					}
				}
			}
			if (dForm.getFltrSector() != null &&
					dForm.getFltrSector().length > 0) {
				// Filter activities based on Sector
				long secs[] = dForm.getFltrSector();
				boolean allSelected = false;
				for (int i = 0; i < secs.length;i++) {
					if (secs[i] == -1) {
						allSelected = true;
						break;
					}
				}
				if (!allSelected) {
					if (activities != null && activities.size() > 0) {
						for (int i = 0;i < activities.size();i ++) {
							AmpProject proj = (AmpProject) activities.get(i);
							Iterator itr = proj.getSector().iterator();
							while (itr.hasNext()) {
								Sector sec = (Sector) itr.next();
								for (int j = 0;j < secs.length;j ++) {
									if (sec.getSectorId().longValue() == secs[j]) {
										flag = true;
										break;
									}
								}
								if (flag) break;
							}
							if (!flag) {
								activities.remove(proj);
								i--;
							}
							flag = false;
						}
					}
				}
			}
			if (request.getParameter("risk") != null) {
				String risk = request.getParameter("risk");
				int riskValue = MEIndicatorsUtil.getRiskRatingValue(risk);
				dForm.setFltrActivityRisks(new Integer(riskValue));
			}
			if (dForm.getFltrActivityRisks() != null && dForm.getFltrActivityRisks().intValue() != 0) {
				// Filter activities based on activity risk
				if (activities != null && activities.size() > 0) {
					for (int i = 0;i < activities.size();i ++) {
						AmpProject proj = (AmpProject) activities.get(i);
						if (proj.getActivityRisk() != dForm.getFltrActivityRisks().intValue()) {
							activities.remove(proj);
							i--;
						}
					}
				}
			}
			// Filtering by Line Ministry Rank here
			try {
				if (activities != null && activities.size() > 0) {
					int rank = Integer.parseInt(dForm.getLineMinRank());
					logger.debug("LineMinRank: " + rank);
					if (rank > 0 && rank < 6) {
						for (int i = 0;i < activities.size();i ++) {
							AmpProject proj = (AmpProject) activities.get(i);
							if (null == proj.getLineMinRank() || 
									proj.getLineMinRank().intValue() != rank) {
								activities.remove(proj);
								i--;
							}
						}
					} 
					/*
					// Sorting in ascending order by Line Ministry Rank here
					if (rank == -2) {
						Collections.sort(activities, actLineRankComparator);
					}
					// Sorting in descending order by Line Ministry Rank here
					if (rank == -3) {
						Collections.sort(activities, ractLineRankComparator);
					}	*/
				}
			}
			catch (NumberFormatException nex) {
				logger.debug("Line Ministry rank is not a number: " + nex);
				nex.printStackTrace(System.out);
			}
			// Filtering by Plan Ministry Rank here
			try {
				if (activities != null && activities.size() > 0) {
					int rank = Integer.parseInt(dForm.getPlanMinRank());
					logger.debug("planMinRank: " + rank);
					if (rank > 0 && rank < 6) {
						for (int i = 0;i < activities.size();i ++) {
							AmpProject proj = (AmpProject) activities.get(i);
							if (null == proj.getPlanMinRank() || 
									proj.getPlanMinRank().intValue() != rank) {
								activities.remove(proj);
								i--;
							}
						}
					}
					/*
					// Sorting in ascending order by Plan Ministry Rank here
					if (rank == -2) {
						Collections.sort(activities, actPlanRankComparator);
					}
					// Sorting in descending order by Plan Ministry Rank here
					if (rank == -3) {
						Collections.sort(activities, ractPlanRankComparator);
					}	*/
				}
			}
			catch (NumberFormatException nex) {
				logger.debug("Plan Ministry rank is not a number: " + nex);
				nex.printStackTrace(System.out);
			}
		}


		dForm.setActivities(activities);
		dForm.setTotalCalculated(false);
		dForm.setSearchKey(null);

		logger.info("FltrSector : " + dForm.getFltrSector());

		return mapping.findForward("forward");
	}
	/*
	private static Comparator actLineRankComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			if (p1.getLineMinRank() != null && p2.getLineMinRank() != null)
				return p1.getLineMinRank().compareTo(p2.getLineMinRank());
			else
				return 0;
		}
	};
	private static Comparator ractLineRankComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			if (p1.getLineMinRank() != null && p2.getLineMinRank() != null)
				return -(p1.getLineMinRank().compareTo(p2.getLineMinRank()));
			else
				return 0;
		}
	};
	private static Comparator actPlanRankComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			if (p1.getPlanMinRank() != null && p2.getPlanMinRank() != null)
				return p1.getPlanMinRank().compareTo(p2.getPlanMinRank());
			else
				return 0;
		}
	};
	private static Comparator ractPlanRankComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			if (p1.getPlanMinRank() != null && p2.getPlanMinRank() != null)
				return -(p1.getPlanMinRank().compareTo(p2.getPlanMinRank()));
			else
				return 0;
		}
	};	*/
}
