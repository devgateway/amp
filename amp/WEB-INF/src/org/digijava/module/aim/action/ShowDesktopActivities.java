package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.DesktopForm;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DesktopUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;

public class ShowDesktopActivities extends TilesAction {

	private static Logger logger = Logger.getLogger(ShowDesktopActivities.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {


		HttpSession session = request.getSession();

		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;

		ServletContext ampContext = getServlet().getServletContext();
		
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		DesktopForm dForm = (DesktopForm) form;

		if (session.getAttribute(Constants.DIRTY_ACTIVITY_LIST) != null) {
			Collection col = (Collection) session.getAttribute(Constants.DIRTY_ACTIVITY_LIST);
			if (dForm.getActivities() != null) {
				dForm.getActivities().removeAll(col);
			} else {
				dForm.setActivities(new ArrayList());
			}
			dForm.getActivities().addAll(col);
			session.removeAttribute(Constants.DIRTY_ACTIVITY_LIST);
		}

		if ("true".equals(dForm.getResetFliters())) {
			dForm.setFltrActivityRisks(new Integer(0));
			dForm.setFltrCalendar(tm.getAppSettings().getFisCalId().longValue());
			dForm.setFltrCurrency(CurrencyUtil.getCurrency(tm.getAppSettings().getCurrencyId()).getCurrencyCode());
			dForm.setFltrDonor(null);
			dForm.setFltrFrmYear(0);
			dForm.setFltrSector(null);
			dForm.setFltrStatus(null);
			dForm.setFltrToYear(0);
			dForm.setResetFliters("false");
		}

		logger.info("FltrSector : " + dForm.getFltrSector());

		int currPage = 1;
		byte srtField = Constants.SORT_FIELD_PROJECT;

		Boolean settingsChanged = (Boolean) session.getAttribute(
				Constants.DESKTOP_SETTINGS_CHANGED);

		boolean loadFilters = true;
		Boolean b = (Boolean) session.getAttribute(Constants.DSKTP_FLTR_CHANGED);
		if (b != null) {
			if (b.booleanValue() != true) {
				loadFilters = false;
			}
		}
		
		session.setAttribute(Constants.DSKTP_FLTR_CHANGED,new Boolean(false));

		if (loadFilters) {
			dForm.setCalendars(null);
			dForm.setCurrencies(null);
			dForm.setDonors(null);
			dForm.setSectors(null);
			dForm.setStatus(null);

			Collection filters = DbUtil.getFilters(tm.getTeamId(),Constants.DESKTOP);
			if (filters != null && filters.size() > 0) {
				// filters present
				dForm.setFiltersPresent(true);
				Iterator itr = filters.iterator();
				while (itr.hasNext()) {
					AmpFilters filter = (AmpFilters) itr.next();
					if (filter.getFilterName().equalsIgnoreCase(Constants.CALENDAR_FILTER)) {
						// adding calendar and year range filters
						dForm.setCalendars(DbUtil.getAllFisCalenders());
						int currYear = Calendar.getInstance().get(Calendar.YEAR);
						int yearRange[] = new int[(currYear+Constants.TO_YEAR_RANGE) - (currYear-Constants.FROM_YEAR_RANGE) + 1];
						for (int j = 0, i = (currYear-Constants.FROM_YEAR_RANGE); i <= (currYear+Constants.TO_YEAR_RANGE);i++,j++) {
							yearRange[j] = i;
						}
						dForm.setYearRange(yearRange);
						//dForm.setFltrFrmYear(yearRange[0]);
						//dForm.setFltrToYear(yearRange[yearRange.length-1]);
						//dForm.setFltrCalendar(tm.getAppSettings().getFisCalId().longValue());
					} else if (filter.getFilterName().equalsIgnoreCase(Constants.CURRENCY_FILTER)) {
						// adding currency filter
						Collection col = CurrencyUtil.getAmpCurrency();
						dForm.setCurrencies(col);
						String currCode = null;
						if (col != null) {
							Iterator itr1 = col.iterator();
							while (itr1.hasNext()) {
								AmpCurrency curr = (AmpCurrency) itr1.next();
								if (curr.getAmpCurrencyId().equals(tm.getAppSettings().getCurrencyId())) {
									currCode = curr.getCurrencyCode();
									break;
								}
							}
						}
						dForm.setFltrCurrency(currCode);
					} else if (filter.getFilterName().equalsIgnoreCase(Constants.DONOR_FILTER)) {
						// adding donor filter
						dForm.setDonors(TeamUtil.getAllDonorsToDesktop(tm.getTeamId()));
						dForm.setFltrDonor(null);
					} else if (filter.getFilterName().equalsIgnoreCase(Constants.SECTOR_FILTER)) {
						// adding sector filter
						dForm.setSectors(new ArrayList());
						Iterator iter = SectorUtil.getAmpSectors().iterator() ;
						while(iter.hasNext()) {
							AmpSector ampSector = (AmpSector) iter.next();
							if(ampSector.getName().length() > 35) {
								String temp=ampSector.getName().substring(0,34) + "...";
								ampSector.setName(temp);
							}
							dForm.getSectors().add(ampSector);

							Iterator iter1 = SectorUtil.getAmpSubSectors(
									ampSector.getAmpSectorId()).iterator();
							while(iter1.hasNext()) {
								AmpSector ampSubSector = (AmpSector) iter1.next();
								if(ampSubSector.getName().length() > 35) {
									ampSubSector.setName("--" + ampSubSector.getName().substring(0,34) + "...");
								} else {
									ampSubSector.setName("--" + ampSubSector.getName());
								}
								dForm.getSectors().add(ampSubSector);

								Iterator iter2 = SectorUtil.getAmpSubSectors(
										ampSubSector.getAmpSectorId()).iterator();
								while(iter2.hasNext()) {
									AmpSector ampSubSubSector = (AmpSector) iter2.next();
									if(ampSubSubSector.getName().length() > 35) {
										ampSubSubSector.setName("----" + ampSubSubSector.getName().substring(0,34) + "...");
									} else {
										ampSubSubSector.setName("----" + ampSubSubSector.getName());
									}
									dForm.getSectors().add(ampSubSubSector);
								}
							}
						}
					} else if (filter.getFilterName().equalsIgnoreCase(Constants.STATUS_FILTER)) {
						dForm.setStatus(DbUtil.getAmpStatus());
						dForm.setFltrStatus(null);
					} else if (filter.getFilterName().equalsIgnoreCase(Constants.ACTIVITY_RISK_FILTER)) {
						if (ampContext.getAttribute(Constants.ME_FEATURE) != null) {
							dForm.setActivityRisks(MEIndicatorsUtil.getAllIndicatorRisks());
						}
					}
				}
			} else {
				// filters not present
				dForm.setFiltersPresent(false);
			}

			// Loading Line Ministry & Ministry of Planning rank drop-downs
			dForm.setLineMinRank("-1");
			dForm.setPlanMinRank("-1");
			dForm.setActRankColl(new ArrayList());
			for (int i = 1; i < 6; i++) {
				dForm.getActRankColl().add(new Integer(i));
			}
		}

		String view = request.getParameter("view");
		if ("page".equalsIgnoreCase(view)) {
			String page = request.getParameter("page");
			if (page != null) {
				try {
					currPage = Integer.parseInt(page);
				} catch (NumberFormatException nfe) {
					logger.error("Trying to parse " + page + " to int");
				}
				int actSize = dForm.getActivities().size();
				int numRecs = tm.getAppSettings().getDefRecsPerPage();

				int stIndex = (currPage-1) * numRecs;
				int edIndex = ((stIndex + numRecs) > actSize) ? actSize : (stIndex + numRecs);
				dForm.setStIndex(stIndex);
				dForm.setEdIndex(edIndex);
				dForm.setCurrentPage(new Integer(currPage));
			}
		} else if ("sorted".equalsIgnoreCase(view)){
			String srtFld = request.getParameter("srt");
			if (srtFld != null) {
				try {
					srtField = Byte.parseByte(srtFld);
				} catch (NumberFormatException nfe) {
					logger.error("Trying to parse " + srtFld + " to byte");
				}
			}
			if (srtField == dForm.getSrtFld()) {
				dForm.setSrtAsc(!dForm.isSrtAsc());
			} else {
				dForm.setSrtFld(srtField);
				dForm.setSrtAsc(true);
			}

			if (dForm.getSrtFld() == Constants.SORT_FIELD_PROJECT) {
				Collections.sort(dForm.getActivities(),projNameComparator);
			} else if (dForm.getSrtFld() == Constants.SORT_FIELD_AMPID) {
				Collections.sort(dForm.getActivities(),ampIdComparator);
			} else if (dForm.getSrtFld() == Constants.SORT_FIELD_DONOR) {
				Collections.sort(dForm.getActivities(),donorComparator);
			} else if (dForm.getSrtFld() == Constants.SORT_FIELD_AMOUNT) {
				Collections.sort(dForm.getActivities(),amountComparator);
			}
			if (!dForm.isSrtAsc()) {
				Collections.reverse(dForm.getActivities());
			}
		} else if ((dForm.isTotalCalculated() == false) ||
				(settingsChanged != null && settingsChanged.booleanValue() == true)) {
			dForm.setCurrentPage(new Integer(1));
			String currCode = null;
			Iterator itr = null;
			if (dForm.getCurrencies() != null && dForm.getFltrCurrency() != null
					&& dForm.getFltrCurrency().trim().length() > 0) {
				currCode = dForm.getFltrCurrency();
			} else {
				itr = CurrencyUtil.getAmpCurrency().iterator();
				while (itr.hasNext()) {
					AmpCurrency curr = (AmpCurrency) itr.next();
					if (curr.getAmpCurrencyId().equals(tm.getAppSettings().getCurrencyId())) {
						currCode = curr.getCurrencyCode();
						dForm.setFltrCurrency(currCode);
						break;
					}
				}
			}

			itr = null;

			double grandTotal = DesktopUtil.updateProjectTotals(
					dForm.getActivities(),currCode);

			dForm.setTotalCommitments(mf.format(grandTotal));
			dForm.setDefCurrency(currCode);
			dForm.setPages(new ArrayList());
			dForm.setCurrentPage(new Integer(currPage));

			int actSize = dForm.getActivities().size();
			dForm.setActivityCount(actSize);
			int numRecs = tm.getAppSettings().getDefRecsPerPage();

			int stIndex = (dForm.getCurrentPage().intValue()-1) * numRecs;
			int edIndex = ((stIndex + numRecs) > actSize) ? actSize : (stIndex + numRecs);

			dForm.setStIndex(stIndex);
			dForm.setEdIndex(edIndex);

			int pageNos = actSize / numRecs;
			if ((actSize % numRecs) > 0) pageNos ++;
			for (int i = 1;i <= pageNos;i ++) {
				dForm.getPages().add(new Integer(i));
			}
			Collections.sort(dForm.getActivities(),projNameComparator);
			dForm.setSrtAsc(true);
			dForm.setTotalCalculated(true);
			dForm.setSrtFld(srtField);
			session.setAttribute(Constants.DESKTOP_SETTINGS_CHANGED,new Boolean(false));
		} else {
			Collections.sort(dForm.getActivities(),projNameComparator);
			dForm.setSrtAsc(true);
			dForm.setSrtFld(srtField);
		}
		return null;
	}

	public static Comparator projNameComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			return p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
		}
	};

	private static Comparator ampIdComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			return p1.getAmpId().compareTo(p2.getAmpId());
		}
	};

	private static Comparator donorComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			ArrayList list1 = new ArrayList();
			if (p1.getDonor() != null &&
					p1.getDonor().size() > 0) {
				Iterator itr = p1.getDonor().iterator();
				while (itr.hasNext()) {
					AmpProjectDonor org = (AmpProjectDonor) itr.next();
					list1.add(org.getDonorName());
				}
			} else {
				list1.add("");
			}
			ArrayList list2 = new ArrayList();
			if (p2.getDonor() != null &&
					p2.getDonor().size() > 0) {
				Iterator itr = p2.getDonor().iterator();
				while (itr.hasNext()) {
					AmpProjectDonor org = (AmpProjectDonor) itr.next();
					list2.add(org.getDonorName());
				}
			} else {
				list2.add("");
			}
			Collections.sort(list1);
			Collections.sort(list2);
			String s1 = (String) list1.get(0);
			String s2 = (String) list2.get(0);
			return s1.compareTo(s2);
		}
	};

	private static Comparator amountComparator = new Comparator() {
		public int compare(Object o1,Object o2) {
			AmpProject p1 = (AmpProject) o1;
			AmpProject p2 = (AmpProject) o2;
			Double amt1 = new Double(0);
			Double amt2 = new Double(0);
			if (p1.getTotalCommited() != null && p1.getTotalCommited().length() > 0)
				amt1 = new Double(Double.parseDouble(p1.getTotalCommited().replaceAll(",","")));
			if (p2.getTotalCommited() != null && p2.getTotalCommited().length() > 0)
				amt2 = new Double(Double.parseDouble(p2.getTotalCommited().replaceAll(",","")));
			return amt1.compareTo(amt2);
		}
	};
}
