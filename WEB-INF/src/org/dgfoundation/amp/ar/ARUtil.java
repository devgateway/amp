/**
 * ARUtil.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.AmpDonors;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public final class ARUtil {
	/**
	 * 
	 * @param getTabs if null gets both reports and tabs, on true only tabs, on false only reports
	 * @return list of public reports
	 */
	public static ArrayList getAllPublicReports(Boolean getTabs) {
		Session session = null;
		ArrayList col = new ArrayList();
		String tabFilter	= "";

		if ( getTabs!=null ) {
			tabFilter	= "r.drilldownTab=:getTabs AND";
		}
		try {

			session = PersistenceManager.getSession();
			String queryString = "select r from " + AmpReports.class.getName()
					+ " r " + "where ( " + tabFilter + " r.publicReport=true)";
			Query qry = session.createQuery(queryString);
			if ( getTabs!=null )
				qry.setBoolean("getTabs", getTabs);

			Iterator itrTemp = qry.list().iterator();
			AmpReports ar = null;
			while (itrTemp.hasNext()) {
				ar = (AmpReports) itrTemp.next();
				col.add(ar);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
				logger.debug(ex.toString());
			}
		}
		return col;
	}

	protected static Logger logger = Logger.getLogger(ARUtil.class);

	public static Constructor getConstrByParamNo(Class c, int paramNo) {
		Constructor[] clist = c.getConstructors();
		for (int j = 0; j < clist.length; j++) {
			if (clist[j].getParameterTypes().length == paramNo)
				return clist[j];
		}
		logger.error("Cannot find a constructor with " + paramNo
				+ " parameters for class " + c.getName());
		return null;
	}

	public static GroupReportData generateReport(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		String ampReportId = request.getParameter("ampReportId");
		if (ampReportId == null)
			ampReportId = (String) request.getAttribute("ampReportId");
		HttpSession httpSession = request.getSession();
		Session session = PersistenceManager.getSession();

		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute("currentMember");

		AmpReports r = (AmpReports) session.get(AmpReports.class, new Long(
				ampReportId));

		// the siteid and locale are set for translation purposes
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		String siteId = site.getSiteId();
		String locale = navigationLanguage.getCode();

		r.setSiteId(siteId);
		r.setLocale(locale);

		httpSession.setAttribute("reportMeta", r);

		if (teamMember != null)
			logger.info("Report '" + r.getName() + "' requested by user "
					+ teamMember.getEmail() + " from team "
					+ teamMember.getTeamName());

		AmpARFilter af = (AmpARFilter) httpSession
				.getAttribute(ArConstants.REPORTS_FILTER);
		if (af == null)
			af = new AmpARFilter();
		af.readRequestData(request);
		httpSession.setAttribute(ArConstants.REPORTS_FILTER, af);

		AmpReportGenerator arg = new AmpReportGenerator(r, af, request);

		arg.generate();

		PersistenceManager.releaseSession(session);

		return arg.getReport();

	}

	public static Collection getFilterDonors(AmpTeam ampTeam) {
		ArrayList dbReturnSet = null;
		ArrayList ret = new ArrayList();
		dbReturnSet = DbUtil.getAmpDonors(ampTeam.getAmpTeamId());
		// logger.debug("Donor Size: " + dbReturnSet.size());
		Iterator iter = dbReturnSet.iterator();

		while (iter.hasNext()) {
			AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next();
			if (ampOrganisation.getAcronym().length() > 20) {
				String temp = ampOrganisation.getAcronym().substring(0, 20)
						+ "...";
				ampOrganisation.setAcronym(temp);
			}
			ret.add(ampOrganisation);
		}

		return ret;
	}

	public static Collection filterDonorGroups(Collection donorGroups) {
		Collection ret = new ArrayList<AmpOrgGroup>();
		if (donorGroups == null) {
			logger
					.error("Collection of AmpOrgGroup should NOT be null in filterDonorGroups");
			return ret;
		}
		Iterator iter = donorGroups.iterator();
		while (iter.hasNext()) {
			AmpOrgGroup grp = (AmpOrgGroup) iter.next();
			if (grp.getOrgType() != null
					&& grp.getOrgType().getOrgType() != null
					&& (grp.getOrgType().getOrgType().toLowerCase().contains(
							"gov") || grp.getOrgType().getOrgType()
							.toLowerCase().contains("gouv"))) {
				continue;
			}
			ret.add(grp);
		}
		return ret;
	}

	/**
	 * Checks if the hierarchy with the specified name as a parameter is present amount the given collection
	 * @param hierarchies the collection of hierarchies that the report holds persisted
	 * @param hierarchyName the name of the hierarchy to check
	 * @return true if the hierarchy exists
	 */
	public static boolean hasHierarchy(Collection<AmpReportHierarchy> hierarchies,String hierarchyName) {
		for (AmpReportHierarchy h : hierarchies) {
			if(h.getColumn().getColumnName().equals(hierarchyName)) return true;
		}
		return false;
	}
	
	public static boolean containsMeasure(String measureName, Set measures) {
		if (measureName == null)
			return false;
		Iterator i = measures.iterator();
		while (i.hasNext()) {
			AmpMeasures element = ((AmpReportMeasures) i.next()).getMeasure();
			if (element.getMeasureName().indexOf(measureName) != -1)
				return true;
		}
		return false;
	}

	public static List createOrderedHierarchies(Collection columns,
			Collection hierarchies) {
		List orderedColumns = new ArrayList(hierarchies.size());
		for (int x = 0; x < hierarchies.size() + columns.size(); x++) {
			Iterator i = hierarchies.iterator();
			while (i.hasNext()) {
				AmpReportHierarchy element = (AmpReportHierarchy) i.next();
				int order = Integer.parseInt(element.getLevelId());
				if (order - 1 == x)
					orderedColumns.add(element);
			}
		}
		return orderedColumns;
	}

	/**
	 * Creates a list in the index order of the report wizard selected column
	 * order.
	 * 
	 * @param columns
	 * @param hierarchies
	 *            this set is needed because also hierarchies were ordered by
	 *            the same values and we need the max
	 * @return
	 */
	public static List createOrderedColumns(Collection columns, Set hierarchies) {
		List orderedColumns = new ArrayList(columns.size());
		for (int x = 0; x < columns.size() + hierarchies.size(); x++) {
			Iterator i = columns.iterator();
			while (i.hasNext()) {
				AmpReportColumn element = (AmpReportColumn) i.next();
				int order = Integer.parseInt(element.getOrderId());
				if (order - 1 == x)
					orderedColumns.add(element);
			}
		}
		return orderedColumns;
	}
}
