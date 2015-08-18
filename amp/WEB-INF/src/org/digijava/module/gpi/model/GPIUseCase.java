package org.digijava.module.gpi.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.action.GetSectors;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpGPISurveyIndicator;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.GPIDefaultFilters;
import org.digijava.module.aim.dbentity.GPISetup;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.GPISetupUtil;
import org.digijava.module.aim.util.HierarchyListableUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocToOrgDAO;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.gpi.form.GPIForm;
import org.digijava.module.gpi.helper.GPIAbstractReport;
import org.digijava.module.gpi.helper.GPIReport1;
import org.digijava.module.gpi.helper.GPIReport5a;
import org.digijava.module.gpi.helper.GPIReport6;
import org.digijava.module.gpi.helper.GPIReport9b;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.util.GPIConstants;
import org.digijava.module.gpi.util.GPIUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

public class GPIUseCase {

	private static Logger logger = Logger.getLogger(GPIUseCase.class);

	/*
	 * Receives the form and populates the collections used in the filters.
	 * TODO: Replace the form variable for all collections to decouple the
	 * Action from the UseCase, so this method can be used from elsewhere.
	 */
	public GPIForm setupFiltersData(GPIForm form, HttpServletRequest request, ServletContext ampContext) {
		HttpSession session=request.getSession();
		if (form.getCalendars() == null || form.getCalendars().isEmpty()) {
			form.setCalendars(DbUtil.getAllFisCalenders());
		}
		if (form.getCurrencyTypes() == null || form.getCurrencyTypes().isEmpty()) {
			form.setCurrencyTypes(CurrencyUtil.getAllCurrencies(CurrencyUtil.ALL_ACTIVE));
		}
		if (form.getFinancingInstrumentsElements() == null || form.getFinancingInstrumentsElements().isEmpty()) {
			form.setFinancingInstrumentsElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
			Collection<AmpCategoryValue> finInstrValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
			HierarchyListableImplementation rootFinancingInstrument = new HierarchyListableImplementation("All Financing Instrument Values", "0", finInstrValues);
			GroupingElement<HierarchyListableImplementation> finInstrElement = new GroupingElement<HierarchyListableImplementation>("Financing Instrument", "filter_financing_instr_div",
					rootFinancingInstrument, "selectedFinancingIstruments");
			form.getFinancingInstrumentsElements().add(finInstrElement);
		}		 	 	
		
		if (form.getDonorElements() == null || form.getDonorElements().isEmpty()) {
			Collection<AmpOrgGroup> donorGroups = DbUtil.getAllOrgGroups();
			form.setDonorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
			HierarchyListableImplementation rootOrgGroup = new HierarchyListableImplementation("All Donor Groups", "0", donorGroups);
			GroupingElement<HierarchyListableImplementation> donorGroupElement = new GroupingElement<HierarchyListableImplementation>("Donor Groups", "filter_donor_groups_div", rootOrgGroup,
					"selectedDonorGroups");
			form.getDonorElements().add(donorGroupElement);
			HierarchyListableUtil.changeTranslateable(donorGroupElement.getRootHierarchyListable(), false);

			Collection<AmpOrganisation> donors = DbUtil.getAllDonorOrgs();
			HierarchyListableImplementation rootDonors = new HierarchyListableImplementation("All Donors", "0", donors);
			GroupingElement<HierarchyListableImplementation> donorsElement = new GroupingElement<HierarchyListableImplementation>("Donor Agencies", "filter_donor_agencies_div", rootDonors,
					"selectedDonors");
			form.getDonorElements().add(donorsElement);
			HierarchyListableUtil.changeTranslateable(donorsElement.getRootHierarchyListable(), false);
			
			List<AmpOrgType> donorTypes = DbUtil.getAllOrgTypesOfPortfolio();
			Collections.sort(donorTypes, new DbUtil.HelperAmpOrgTypeNameComparator()); 	 	
	 	 	HierarchyListableUtil.changeTranslateable(donorTypes, false);
	 	 	form.setDonorTypeElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
			HierarchyListableImplementation rootOrgType = new HierarchyListableImplementation("All Donor Types", "0", donorTypes);
	 	 	GroupingElement<HierarchyListableImplementation> donorTypeElement = new GroupingElement<HierarchyListableImplementation>("Donor Types", "filter_donor_types_div", rootOrgType, "selectedDonorTypes");
			form.getDonorElements().add(donorTypeElement);
		}
			
		if (form.getSectorStatusesElements() == null || form.getSectorStatusesElements().isEmpty()) {
			form.setSectorStatusesElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
			Collection<AmpCategoryValue> activityStatusValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
			HierarchyListableImplementation rootActivityStatus = new HierarchyListableImplementation("All", "0", activityStatusValues);
			GroupingElement<HierarchyListableImplementation> activityStatusElement = new GroupingElement<HierarchyListableImplementation>("Status", "filter_activity_status_div", rootActivityStatus,
					"selectedStatuses");
			form.getSectorStatusesElements().add(activityStatusElement);

			if (FeaturesUtil.isVisibleField("Sector")) {
				HierarchyListableImplementation rootAmpSectors = new HierarchyListableImplementation("Primary Sectors", Integer.toString(0),
							SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME));
				GroupingElement<HierarchyListableImplementation> sectorsElement = new GroupingElement<HierarchyListableImplementation>("Primary Sectors", "filter_sectors_div", rootAmpSectors,
						"selectedSectors");
				form.getSectorStatusesElements().add(sectorsElement);
				HierarchyListableUtil.changeTranslateable(sectorsElement.getRootHierarchyListable(), false);
			}

			if (FeaturesUtil.isVisibleField("Secondary Sector")) {
				HierarchyListableImplementation rootSecondaryAmpSectors = new HierarchyListableImplementation("Secondary Sectors", "0", 
							SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME));
				GroupingElement<HierarchyListableImplementation> secondarySectorsElement = new GroupingElement<HierarchyListableImplementation>("Secondary Sectors", "filter_secondary_sectors_div",
						rootSecondaryAmpSectors, "selectedSectors");
				form.getSectorStatusesElements().add(secondarySectorsElement);
				HierarchyListableUtil.changeTranslateable(secondarySectorsElement.getRootHierarchyListable(), false);
			}

			if (FeaturesUtil.isVisibleField("Tertiary Sector")) {
				HierarchyListableImplementation rootTertiaryAmpSectors = new HierarchyListableImplementation("Tertiary Sector", "0", 
							SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME));
				GroupingElement<HierarchyListableImplementation> tertiarySectorsElement = new GroupingElement<HierarchyListableImplementation>("Tertiary Sectors", "filter_tertiary_sectors_div",
						rootTertiaryAmpSectors, "selectedSectors");
				form.getSectorStatusesElements().add(tertiarySectorsElement);
				HierarchyListableUtil.changeTranslateable(tertiarySectorsElement.getRootHierarchyListable(), false);
			}

		}
		return form;
	}

	/*
	 * Resets all filters to their base values, some values must be null as
	 * default and other needs always a value like the calendar, currency, etc.
	 */
	public void resetFilterSelections(GPIForm form, ApplicationSettings appSettings) {

		if (appSettings.getFisCalId() != null) {
			form.setSelectedCalendar(DbUtil.getAmpFiscalCalendar(appSettings.getFisCalId()).getAmpFiscalCalId().toString());
		} else {
			form.setSelectedCalendar(DbUtil.getAmpFiscalCalendar(DbUtil.getBaseFiscalCalendar()).getAmpFiscalCalId().toString());
		}
		form.setDefaultCalendar(form.getSelectedCalendar());
		form.setSelectedCurrency(CurrencyUtil.getAmpcurrency(appSettings.getCurrencyId()).getCurrencyCode());
		form.setDefaultCurrency(form.getSelectedCurrency());
		form.setSelectedEndYear(Calendar.getInstance().get(Calendar.YEAR));
		form.setDefaultEndYear(form.getSelectedEndYear());
		form.setSelectedStartYear(Calendar.getInstance().get(Calendar.YEAR) - 2);
		form.setDefaultStartYear(form.getSelectedStartYear());
		form.setStartYears(new int[10]);
		form.setEndYears(new int[10]);
		int auxYear = form.getSelectedEndYear() - 5;
		for (int i = 0; i < 10; i++) {
			form.getStartYears()[i] = auxYear + i;
			form.getEndYears()[i] = auxYear + i;
		}

		form.setSelectedDonors(null);
		form.setSelectedDonorGroups(null);
		form.setSelectedSectors(null);
		form.setSelectedStatuses(null);
		form.setSelectedFinancingIstruments(null);
		
		// Look for default donor types.
		Collection<String> auxDonorTypes = new ArrayList<String>();
		try {
			auxDonorTypes = GPISetupUtil.getSavedFilters(GPIDefaultFilters.GPI_DEFAULT_FILTER_ORG_GROUP);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] donorTypes = auxDonorTypes.toArray(new String[auxDonorTypes.size()]);
		form.setSelectedDonorTypes(donorTypes);
	}

	/*
	 * Gets the collection of GPI reports from the DB and filters any report if
	 * necessary.
	 */
	public Collection<AmpGPISurveyIndicator> setupAvailableGPIReports() {
		Collection<AmpGPISurveyIndicator> list = new ArrayList<AmpGPISurveyIndicator>();
		list = DbUtil.getAllGPISurveyIndicators(false);
		Iterator<AmpGPISurveyIndicator> iter = list.iterator();
		while (iter.hasNext()) {
			AmpGPISurveyIndicator aux = iter.next();
			if (checkReportName(aux.getIndicatorCode()) == null) {
				iter.remove();
			} else if (!Boolean.TRUE.equals(aux.getShowAsIndicator())) {
				// Dont show some indicators in the tab list.
				iter.remove();
			}
		}
		return list;
	}

	/*
	 * Checks the report name with the available GPI reports.
	 */
	public String checkReportName(String name) {
		String ret = null;
		if (name == null) {
		} else if (name.equals(GPIConstants.GPI_REPORT_1) || name.equals(GPIConstants.GPI_REPORT_5a) || name.equals(GPIConstants.GPI_REPORT_6) || name.equals(GPIConstants.GPI_REPORT_9b)) {
			ret = name;
		}
		return ret;
	}

	/*
	 * Returns an indicator object from the list of available reports given the
	 * report code.
	 */
	public AmpGPISurveyIndicator getGPIReport(String code) {
		AmpGPISurveyIndicator ret = null;
		Collection<AmpGPISurveyIndicator> list = this.setupAvailableGPIReports();
		Iterator<AmpGPISurveyIndicator> iter = list.iterator();
		while (iter.hasNext()) {
			AmpGPISurveyIndicator aux = iter.next();
			if (aux.getIndicatorCode().equals(code)) {
				ret = aux;
			}
		}
		return ret;
	}

	/*
	 * Executes part of the common logic for all reports and then creates the
	 * concrete report.
	 */
	public GPIAbstractReport createReport(GPIForm form, HttpServletRequest request) throws Exception {
		// Create the report.
		GPISetup setup = GPISetupUtil.getSetup();
		form.setSetup(setup);
		GPIAbstractReport report = null;
		if (form.getGPIReport().getIndicatorCode().equals(GPIConstants.GPI_REPORT_1)) {
			report = new GPIReport1();
		} else if (form.getGPIReport().getIndicatorCode().equals(GPIConstants.GPI_REPORT_5a)) {
			report = new GPIReport5a();
		} else if (form.getGPIReport().getIndicatorCode().equals(GPIConstants.GPI_REPORT_9b)) {
			report = new GPIReport9b();
		} else if (form.getGPIReport().getIndicatorCode().equals(GPIConstants.GPI_REPORT_6)) {
			report = new GPIReport6();
		}

		// Setup common filters.
		Collection<GPIReportAbstractRow> preMainReportRows = null;
		GPIFilter filter = new GPIFilter();
		filter.setCalendar(DbUtil.getAmpFiscalCalendar(new Long(form.getSelectedCalendar())));
		filter.setCurrency(CurrencyUtil.getAmpcurrency(form.getSelectedCurrency()));
		filter.setDonors(GPIUtils.getDonorsCollection(form.getSelectedDonors()));
		filter.setDonorTypes(GPIUtils.getDonorTypes(form.getSelectedDonorTypes()));
		filter.setDonorGroups(GPIUtils.getDonorGroups(form.getSelectedDonorGroups()));
		filter.setSectors(GPIUtils.getSectors(form.getSelectedSectors()));
		filter.setStatuses(GPIUtils.getStatuses(form.getSelectedStatuses()));
		filter.setFinancingInstruments(GPIUtils.getFinancingInstruments(form.getSelectedFinancingIstruments()));
		filter.setStartYear(form.getSelectedStartYear());
		filter.setEndYer(form.getSelectedEndYear());

		// Get all surveys.
		Collection<AmpActivityVersion> commonData = getCommonSurveyData(filter);

		// Execute the logic for generating each report.
		preMainReportRows = report.generateReport(commonData, filter);

		// Postprocess the report if needed.
		Collection<GPIReportAbstractRow> postMainReportRows = report.reportPostProcess(preMainReportRows, form.getSelectedStartYear(), form.getSelectedEndYear());

		report.setReportRows(postMainReportRows);

		return report;
	}

	/*
	 * Return a collection with all common columns needed later to generate each report.
	 */
	private Collection getCommonSurveyData(GPIFilter filter) {
		logger.warn("commonData");
		long time = Calendar.getInstance().getTimeInMillis();
		Collection<AmpActivityVersion> commonData = null;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			String selectQueryString = new StringBuilder()
					.append("SELECT aa.amp_activity_id, af.amp_funding_id, afd.amp_fund_detail_id, afd.transaction_date, aog.amp_org_grp_id, aog.org_grp_name, ")
					.append( "(SELECT COUNT(*) FROM amp_gpi_survey ags WHERE ags.amp_activity_id = aa.amp_activity_id) AS surveys, afd.transaction_amount, afd.transaction_type, ac.currency_code, acv.category_value ")
					.append(" FROM amp_activity aa JOIN amp_funding af ON aa.amp_activity_id = af.amp_activity_id")
					.append(" JOIN amp_funding_detail afd ON af.amp_funding_id = afd.amp_funding_id")
					.append(" JOIN amp_organisation ao ON af.amp_donor_org_id = ao.amp_org_id")
					.append(" JOIN amp_currency ac ON afd.amp_currency_id = ac.amp_currency_id")
					.append(" JOIN amp_category_value acv ON afd.adjustment_type = acv.id")
					.append(" JOIN amp_org_group aog ON ao.org_grp_id = aog.amp_org_grp_id WHERE aa.amp_team_id IS NOT NULL ").toString();
			String where = " ";
			String endQueryString = " ORDER BY aa.amp_activity_id, af.amp_funding_id, afd.amp_fund_detail_id, afd.transaction_date, aog.amp_org_grp_id";
			if (filter.getSectors() != null) {
				String sectors = "";
				Iterator<AmpSector> iSectors = filter.getSectors().iterator();
				while (iSectors.hasNext()) {
					sectors += iSectors.next().getAmpSectorId() + ",";
				}
				sectors = sectors.substring(0, sectors.length() - 1);
				where += " AND aa.amp_activity_id IN (SELECT aas.amp_activity_id FROM amp_activity_sector aas WHERE aas.amp_sector_id IN ("
						+ sectors + ")) ";
			}
			if (filter.getStatuses() != null && filter.getStatuses().size() > 0) {
				where += " AND aa.amp_activity_id IN (SELECT amp_activity_id FROM amp_activities_categoryvalues aacv WHERE amp_categoryvalue_id IN (@@status@@)) ";
				String ids = "";
				Iterator<AmpCategoryValue> iStatus = filter.getStatuses().iterator();
				while (iStatus.hasNext()) {
					AmpCategoryValue cv = iStatus.next();
					ids += cv.getUniqueId() + ",";
				}
				ids = ids.substring(0, ids.length() - 1);
				where = where.replace("@@status@@", ids);
			}
			if (filter.getDonors() != null) {
				String donors = "";
				Iterator<AmpOrganisation> iDonors = filter.getDonors().iterator();
				while (iDonors.hasNext()) {
					donors += iDonors.next().getAmpOrgId() + ",";
				}
				donors = donors.substring(0, donors.length() - 1);
				where += " AND af.amp_donor_org_id IN (" + donors + ") ";
			}
			if (filter.getDonorGroups() != null) {
				String dg = "";
				Iterator<AmpOrgGroup> iDG = filter.getDonorGroups().iterator();
				while (iDG.hasNext()) {
					dg += iDG.next().getAmpOrgGrpId() + ",";
				}
				dg = dg.substring(0, dg.length() - 1);
				where += " AND aog.amp_org_grp_id IN (" + dg + ") ";
			}
			if (filter.getDonorTypes() != null) {
				String dt = "";
				Iterator<AmpOrgType> iDT = filter.getDonorTypes().iterator();
				while (iDT.hasNext()) {
					dt += iDT.next().getAmpOrgTypeId() + ",";
				}
				dt = dt.substring(0, dt.length() - 1);
				where += " AND aog.org_type IN (" + dt + ") ";
			}

			Query query = session.createSQLQuery(selectQueryString + where + endQueryString);
			logger.warn(query.getQueryString());
			commonData = query.list();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.warn("commonData: " + ((Calendar.getInstance().getTimeInMillis() - time) / 1000) + "s");
		return commonData;
	}
}