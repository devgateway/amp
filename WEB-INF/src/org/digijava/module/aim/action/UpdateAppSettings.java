package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.UpdateAppSettingsForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UpdateAppSettings extends Action {

	private static Logger logger = Logger.getLogger(UpdateAppSettings.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		UpdateAppSettingsForm uForm = (UpdateAppSettingsForm) form;
		logger.debug("In updtate app settings");

		HttpSession session = request.getSession();

		if (session.getAttribute("currentMember") == null) {
			return mapping.findForward("index");
		}

		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		if (request.getParameter("updated") != null
				&& request.getParameter("updated").equals("true")) {
			uForm.setUpdated(true);
		} else {
			uForm.setUpdated(false);
		}

		if (uForm.getType() == null || uForm.getType().trim().equals("")) {
			String path = mapping.getPath();
			logger.debug("path = " + path);
			AmpApplicationSettings ampAppSettings = null;
			if (path != null
					&& (path.trim().equals("/aim/defaultSettings") || path
							.trim().equals("/defaultSettings"))) {
				if (tm.getTeamHead() == false) {
					return mapping.findForward("viewMyDesktop");
				}
				uForm.setType("default");
				uForm.setTeamName(tm.getTeamName());
				ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
			} else if (path != null
					&& (path.trim().equals("/aim/customizeSettings") || path
							.trim().equals("/customizeSettings"))) {
				uForm.setType("userSpecific");
				uForm.setMemberName(tm.getMemberName());
				ampAppSettings = DbUtil.getMemberAppSettings(tm.getMemberId());
			}
			// added by mouhamad for burkina on 21/02/08
			HttpSession httpSession = request.getSession();
			String name = "- " + ampAppSettings.getCurrency().getCurrencyName();
			httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);

			// AMP-3168 Currency conversion in team workspace setup
			httpSession.setAttribute("reportCurrencyCode", ampAppSettings
					.getCurrency().getCurrencyCode());
			AmpARFilter filter = (AmpARFilter) httpSession
					.getAttribute(ArConstants.REPORTS_FILTER);
			if (filter != null) {
				filter.setCurrency(ampAppSettings.getCurrency());
				httpSession.setAttribute(ArConstants.REPORTS_FILTER, filter);
			}
			if (ampAppSettings != null) {
				uForm.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
				uForm.setDefRecsPerPage(ampAppSettings
						.getDefaultRecordsPerPage().intValue());

				Integer reportsPerPage = ampAppSettings
						.getDefaultReportsPerPage();
				if (reportsPerPage == null) {
					reportsPerPage = 0;
				}
				Integer reportStartYear = ampAppSettings.getReportStartYear();
				if (reportStartYear == null) {
					reportStartYear = 0;
				}
				Integer reportEndYear = ampAppSettings.getReportEndYear();
				if (reportEndYear == null) {
					reportEndYear = 0;
				}
				uForm.setDefReportsPerPage(reportsPerPage);
				uForm.setReportStartYear(reportStartYear);
				uForm.setReportEndYear(reportEndYear);
				uForm.setLanguage(ampAppSettings.getLanguage());
				uForm.setValidation(ampAppSettings.getValidation());
				uForm.setCurrencyId(ampAppSettings.getCurrency()
						.getAmpCurrencyId());
                if(ampAppSettings.getFiscalCalendar()!=null){
				uForm.setFisCalendarId(ampAppSettings.getFiscalCalendar()
						.getAmpFiscalCalId());
                }

				if (ampAppSettings.getDefaultTeamReport() != null)
					uForm.setDefaultReportForTeamId(ampAppSettings
							.getDefaultTeamReport().getAmpReportId());
				else
					uForm.setDefaultReportForTeamId(new Long(0));
			}
			/* Select only the reports that are shown as tabs */
			List<AmpReports> reports = TeamUtil.getAllTeamReports(tm.getTeamId(), null,
					null, null, true, tm.getMemberId());
			if (reports != null) {
				Iterator iterator = reports.iterator();
				while (iterator.hasNext()) {
					AmpReports ampreport = (AmpReports) iterator.next();
					if (ampreport.getDrilldownTab() == null
							|| !ampreport.getDrilldownTab().booleanValue()) {
						iterator.remove();
					}
				}
			}
			
			Collections.sort(reports, 
					new Comparator<AmpReports> () {
						public int compare(AmpReports o1, AmpReports o2) {
							return o1.getName().compareTo( o2.getName() );
						}
					}
				);
			
			uForm.setReports(reports);
			uForm.setCurrencies(CurrencyUtil
					.getAllCurrencies(CurrencyUtil.ALL_ACTIVE));
			uForm.setFisCalendars(DbUtil.getAllFisCalenders());

			// set Navigation languages
			Set languages = SiteUtils.getUserLanguages(RequestUtils
					.getSite(request));

			HashMap translations = new HashMap();
			Iterator iterator = TrnUtil.getLanguages(
					RequestUtils.getNavigationLanguage(request).getCode())
					.iterator();
			while (iterator.hasNext()) {
				TrnLocale item = (TrnLocale) iterator.next();
				translations.put(item.getCode(), item);
			}
			// sort languages
			List sortedLanguages = new ArrayList();
			iterator = languages.iterator();
			while (iterator.hasNext()) {
				Locale item = (Locale) iterator.next();
				sortedLanguages.add(translations.get(item.getCode()));
			}
			Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);
			uForm.setLanguages(sortedLanguages);

			logger.info("TYpe =" + uForm.getType());
			if (uForm.getType().equalsIgnoreCase("userSpecific")) {
				logger.info("mapping to showUserSettings");
				return mapping.findForward("showUserSettings");
			} else {
				return mapping.findForward("showDefaultSettings");
			}
		} else {
			logger.debug("In saving");
			AmpApplicationSettings ampAppSettings = null;
			if (uForm.getSave() != null) {
				ampAppSettings = new AmpApplicationSettings();
				ampAppSettings.setAmpAppSettingsId(uForm.getAppSettingsId());
				ampAppSettings.setDefaultRecordsPerPage(new Integer(uForm
						.getDefRecsPerPage()));
				ampAppSettings.setReportStartYear((new Integer(uForm
						.getReportStartYear())));
				ampAppSettings.setReportEndYear((new Integer(uForm
						.getReportEndYear())));

				ampAppSettings.setDefaultReportsPerPage(uForm
						.getDefReportsPerPage());
				ampAppSettings.setCurrency(CurrencyUtil.getAmpcurrency(uForm
						.getCurrencyId()));
				ampAppSettings.setFiscalCalendar(DbUtil
						.getAmpFiscalCalendar(uForm.getFisCalendarId()));
				ampAppSettings.setLanguage(uForm.getLanguage());
				ampAppSettings.setValidation(uForm.getValidation());
				ampAppSettings.setTeam(TeamUtil.getAmpTeam(tm.getTeamId()));

				AmpReports ampReport = DbUtil.getAmpReports(uForm
						.getDefaultReportForTeamId());
				HttpSession httpSession = request.getSession();
				AmpReports defaultAmpReport = (AmpReports) httpSession
						.getAttribute(Constants.DEFAULT_TEAM_REPORT);
				/**
				 * Just checking whether defaultTeamReport has changed
				 */
				if ((defaultAmpReport == null && ampReport != null)
						|| (defaultAmpReport != null && ampReport == null)
						|| (defaultAmpReport != null && ampReport != null && defaultAmpReport
								.getAmpReportId().longValue() != ampReport
								.getAmpReportId().longValue())) {
					ampAppSettings.setDefaultTeamReport(ampReport);
					httpSession.setAttribute(Constants.DEFAULT_TEAM_REPORT,
							ampAppSettings.getDefaultTeamReport());
					httpSession.setAttribute("filterCurrentReport",
							ampAppSettings.getDefaultTeamReport());
					// this.updateAllTeamMembersDefaultReport( tm.getTeamId(),
					// ampReport);
				}
				// added by mouhamad for burkina on 21/02/08
				String name = "- "
						+ ampAppSettings.getCurrency().getCurrencyName();
				httpSession.setAttribute(ArConstants.SELECTED_CURRENCY, name);
				// end
				if (uForm.getType().equals("userSpecific")) {
					ampAppSettings.setMember(TeamMemberUtil.getAmpTeamMember(tm
							.getMemberId()));
					ampAppSettings.setUseDefault(new Boolean(false));
				} else {
					/* change all members settings whose has 'useDefault' set */
					Iterator itr = TeamMemberUtil.getAllTeamMembers(
							tm.getTeamId()).iterator();
					logger.debug("before while");
					while (itr.hasNext()) {
						TeamMember member = (TeamMember) itr.next();
						AmpApplicationSettings memSettings = DbUtil
								.getMemberAppSettings(member.getMemberId());

						if (memSettings != null)
							if (memSettings.getUseDefault().booleanValue() == true) {

								AmpTeamMember ampMember = TeamMemberUtil
										.getAmpTeamMember(member.getMemberId());
								restoreApplicationSettings(memSettings,
										ampAppSettings, ampMember);

							}
					}
				}

				try {
					DbUtil.update(ampAppSettings);
					uForm.setUpdated(true);
				} catch (Exception e) {
					uForm.setUpdated(false);
				}
			} else if (uForm.getRestore() != null) {
				ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
				AmpApplicationSettings memSettings = DbUtil
						.getMemberAppSettings(tm.getMemberId());
				AmpTeamMember member = TeamMemberUtil.getAmpTeamMember(tm
						.getMemberId());
				try {
					restoreApplicationSettings(memSettings, ampAppSettings,
							member);
					uForm.setUpdated(true);
				} catch (Exception e) {
					uForm.setUpdated(false);
				}
			}
			AmpApplicationSettings tempSettings = DbUtil
					.getMemberAppSettings(tm.getMemberId());
			ApplicationSettings applicationSettings = getReloadedAppSettings(tempSettings);
			tm.setAppSettings(applicationSettings);
			if (session.getAttribute(Constants.CURRENT_MEMBER) != null) {
				session.removeAttribute(Constants.CURRENT_MEMBER);
				session.setAttribute(Constants.CURRENT_MEMBER, tm);
			}

			AmpARFilter arf = (AmpARFilter) session
					.getAttribute(ArConstants.REPORTS_FILTER);

			logger.debug("settings updated");

			session.setAttribute(Constants.DESKTOP_SETTINGS_CHANGED,
					new Boolean(true));

			uForm.setUpdateFlag(false);
			SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

			String context = SiteUtils.getSiteURL(currentDomain, request
					.getScheme(), request.getServerPort(), request
					.getContextPath());
			if (uForm.getType().equals("default")) {
				uForm.setType(null);
				String url = context + "/translation/switchLanguage.do?code="
						+ ampAppSettings.getLanguage() + "&rfr=" + context
						+ "/aim/defaultSettings.do~updated="
						+ uForm.getUpdated();
				response.sendRedirect(url);
				logger.debug("redirecting " + url + " ....");
				// return mapping.findForward("default");
				return null;
			} else if (uForm.getType().equals("userSpecific")) {
				uForm.setType(null);
				String url = context + "/translation/switchLanguage.do?code="
						+ ampAppSettings.getLanguage() + "&rfr=" + context
						+ "/aim/customizeSettings.do~updated="
						+ uForm.getUpdated();
				response.sendRedirect(url);
				logger.debug("redirecting " + url + " ....");
				// return mapping.findForward("userSpecific");
				return null;
			} else {
				return mapping.findForward("index");
			}
		}
	}

	/* restoreApplicationSettings( oldSettings , newSettings , member) */
	public void restoreApplicationSettings(AmpApplicationSettings oldSettings,
			AmpApplicationSettings newSettings, AmpTeamMember ampMember) {

		logger.debug("In restoreApplicationSettings() ");

		/* set all values except id from oldSettings to newSettings */
		oldSettings.setDefaultRecordsPerPage(newSettings
				.getDefaultRecordsPerPage());
		oldSettings.setDefaultReportsPerPage(newSettings
				.getDefaultReportsPerPage());
		oldSettings.setCurrency(newSettings.getCurrency());
		oldSettings.setFiscalCalendar(newSettings.getFiscalCalendar());
		oldSettings.setLanguage(newSettings.getLanguage());
		oldSettings.setValidation(newSettings.getValidation());
		oldSettings.setTeam(newSettings.getTeam());
		oldSettings.setMember(ampMember);
		oldSettings.setReportStartYear(newSettings.getReportStartYear());
		oldSettings.setReportEndYear(newSettings.getReportEndYear());

		oldSettings.setUseDefault(new Boolean(true));
		oldSettings.setDefaultTeamReport(newSettings.getDefaultTeamReport());
		DbUtil.update(oldSettings);
		logger.debug("restoreApplicationSettings() returning");
	}

	public ApplicationSettings getReloadedAppSettings(
			AmpApplicationSettings ampAppSettings) {
		ApplicationSettings appSettings = new ApplicationSettings();
		appSettings.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
		appSettings.setDefRecsPerPage(ampAppSettings.getDefaultRecordsPerPage()
				.intValue());
		appSettings.setReportStartYear(ampAppSettings.getReportStartYear());
		appSettings.setReportEndYear(ampAppSettings.getReportEndYear());

		appSettings.setDefReportsPerPage(ampAppSettings
				.getDefaultReportsPerPage());
		appSettings.setCurrencyId(ampAppSettings.getCurrency()
				.getAmpCurrencyId());
		appSettings.setFisCalId(ampAppSettings.getFiscalCalendar()
				.getAmpFiscalCalId());
		appSettings.setLanguage(ampAppSettings.getLanguage());
		appSettings.setValidation(ampAppSettings.getValidation());
		appSettings.setDefaultAmpReport(ampAppSettings.getDefaultTeamReport());
		return appSettings;
	}

	private void updateAllTeamMembersDefaultReport(Long teamId,
			AmpReports ampReport) {
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			Transaction tx = session.beginTransaction();
			String queryString = "SELECT a FROM "
					+ AmpApplicationSettings.class.getName() + " a WHERE  "
					+ "a.team=:teamId";
			Query query = session.createQuery(queryString);
			query.setParameter("teamId", teamId, Hibernate.LONG);
			Collection reports = query.list();
			Iterator iterator = reports.iterator();

			while (iterator.hasNext()) {
				AmpApplicationSettings setting = (AmpApplicationSettings) iterator
						.next();
				setting.setDefaultTeamReport(ampReport);
			}

			tx.commit();
			session.flush();

		} catch (Exception ex) {
			logger.error("Unable to get fundingDetails :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}

	}
}
