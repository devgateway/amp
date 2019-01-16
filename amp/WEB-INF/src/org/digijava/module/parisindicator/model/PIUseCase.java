package org.digijava.module.parisindicator.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
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
import org.digijava.module.contentrepository.util.DocumentOrganizationManager;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.parisindicator.form.PIForm;
import org.digijava.module.parisindicator.helper.PIAbstractReport;
import org.digijava.module.parisindicator.helper.PIReport10a;
import org.digijava.module.parisindicator.helper.PIReport10b;
import org.digijava.module.parisindicator.helper.PIReport3;
import org.digijava.module.parisindicator.helper.PIReport4;
import org.digijava.module.parisindicator.helper.PIReport5a;
import org.digijava.module.parisindicator.helper.PIReport5b;
import org.digijava.module.parisindicator.helper.PIReport6;
import org.digijava.module.parisindicator.helper.PIReport7;
import org.digijava.module.parisindicator.helper.PIReport9;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.parisindicator.util.PIUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

public class PIUseCase {

    private static Logger logger = Logger.getLogger(PIUseCase.class);

    /*
     * Receives the form and populates the collections used in the filters. TODO: Replace the form variable for all collections to decouple the Action from the UseCase, so this method can be used from
     * elsewhere.
     */
    public PIForm setupFiltersData(PIForm form, HttpServletRequest request, ServletContext ampContext) {
        /*
         * if (form.getStatuses() == null || form.getStatuses().isEmpty()) { form.setStatuses(DbUtil.getAmpStatusFromCM(request)); } if (form.getDonors() == null || form.getDonors().isEmpty()) {
         * form.setDonors(DbUtil.getAllDonorOrgs()); } if (form.getSectors() == null || form.getSectors().isEmpty()) { form.setSectors(SectorUtil.getAmpSectors()); } if (form.getDonorGroups() == null
         * || form.getDonorGroups().isEmpty()) { form.setDonorGroups(DbUtil.getAllOrgGroups()); } if (form.getFinancingInstruments() == null || form.getFinancingInstruments().isEmpty()) {
         * form.setFinancingInstruments(DbUtil.getAllFinancingInstruments()); }
         */
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
            Collection<AmpOrgGroup> donorGroups = DbUtil.getAllVisibleOrgGroups();
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
     * Resets all filters to their base values, some values must be null as default and other needs always a value like the calendar, currency, etc.
     */
    public void resetFilterSelections(PIForm form, ApplicationSettings appSettings) {

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
    }

    /*
     * Gets the collection of PI reports from the DB and filters any report if necessary.
     */
    public Collection<AmpAhsurveyIndicator> setupAvailablePIReports() {
        Collection<AmpAhsurveyIndicator> list = new ArrayList<AmpAhsurveyIndicator>();
        list = DbUtil.getAllAhSurveyIndicators();
        Iterator<AmpAhsurveyIndicator> iter = list.iterator();
        while (iter.hasNext()) {
            AmpAhsurveyIndicator aux = iter.next();
            if (checkReportName(aux.getIndicatorCode()) == null) {
                iter.remove();
            }
        }
        return list;
    }

    /*
     * Checks the report name with the available PI reports.
     */
    public String checkReportName(String name) {
        String ret = null;
        if (name == null) {
        } else if (name.equals(PIConstants.PARIS_INDICATOR_REPORT_3) || name.equals(PIConstants.PARIS_INDICATOR_REPORT_4) || name.equals(PIConstants.PARIS_INDICATOR_REPORT_5a)
                || name.equals(PIConstants.PARIS_INDICATOR_REPORT_5b) || name.equals(PIConstants.PARIS_INDICATOR_REPORT_6) || name.equals(PIConstants.PARIS_INDICATOR_REPORT_7)
                || name.equals(PIConstants.PARIS_INDICATOR_REPORT_9) || name.equals(PIConstants.PARIS_INDICATOR_REPORT_10a) || name.equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
            ret = name;
        }
        return ret;
    }

    /*
     * Returns an indicator object from the list of available reports given the report code.
     */
    public AmpAhsurveyIndicator getPIReport(String code) {
        AmpAhsurveyIndicator ret = null;
        Collection<AmpAhsurveyIndicator> list = this.setupAvailablePIReports();
        Iterator<AmpAhsurveyIndicator> iter = list.iterator();
        while (iter.hasNext()) {
            AmpAhsurveyIndicator aux = iter.next();
            if (aux.getIndicatorCode().equals(code)) {
                ret = aux;
            }
        }
        return ret;
    }

    /*
     * Executes part of the common logic for all reports and then creates the concrete report.
     */
    public PIAbstractReport createReport(PIForm form, HttpServletRequest request) throws Exception {
        // Create the report.
        PIAbstractReport report = null;
        if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_3)) {
            report = new PIReport3();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_4)) {
            report = new PIReport4();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5a)) {
            report = new PIReport5a();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5b)) {
            report = new PIReport5b();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_6)) {
            report = new PIReport6();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_7)) {
            report = new PIReport7();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_9)) {
            report = new PIReport9();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10a)) {
            report = new PIReport10a();
        } else if (form.getPiReport().getIndicatorCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
            report = new PIReport10b();
        }

        // Get the common info from surveys and apply some filters.
        Collection<PIReportAbstractRow> preMainReportRows = null;
        AmpFiscalCalendar auxCalendar = DbUtil.getAmpFiscalCalendar(new Long(form.getSelectedCalendar()));
        AmpCurrency auxCurrency = CurrencyUtil.getAmpcurrency(form.getSelectedCurrency());
        Collection<AmpOrganisation> auxDonors = PIUtils.getDonorsCollection(form.getSelectedDonors());
        Collection<AmpOrgGroup> auxDonorGroups = PIUtils.getDonorGroups(form.getSelectedDonorGroups());
        Collection<AmpSector> auxSectors = PIUtils.getSectors(form.getSelectedSectors());
        Collection<AmpCategoryValue> auxStatuses = PIUtils.getStatuses(form.getSelectedStatuses());
        Collection<AmpCategoryValue> auxFinancingInstruments = PIUtils.getFinancingInstruments(form.getSelectedFinancingIstruments());
        long currentDate = Calendar.getInstance().getTimeInMillis();
        if (!report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10a) && !report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
            Collection<AmpAhsurvey> commonData = getCommonSurveyData(auxDonors, auxDonorGroups);
            logger.warn("PI Time 1: " + (Calendar.getInstance().getTimeInMillis() - currentDate));

            // Execute the logic for generating each report.
            currentDate = Calendar.getInstance().getTimeInMillis();
            preMainReportRows = report.generateReport(commonData, form.getSelectedStartYear(), form.getSelectedEndYear(), auxCalendar, auxCurrency, auxSectors, auxStatuses, auxFinancingInstruments);
            logger.warn("PI Time 2: " + (Calendar.getInstance().getTimeInMillis() - currentDate));
        } else {
            if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10a)) {
                Collection<AmpOrganisation> commonData10a = getCommonSurveyDataForPI10a(auxDonors, auxDonorGroups);

                // Execute the logic for generating each report.
                preMainReportRows = report.generateReport10a(commonData10a, form.getSelectedStartYear(), form.getSelectedEndYear(), auxCalendar, auxCurrency, auxSectors, auxStatuses,
                        auxFinancingInstruments);
            }
            if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_10b)) {
                Collection<NodeWrapper> commonData10b = getCommonSurveyDataForPI10b(auxDonors, auxDonorGroups, request);

                // Execute the logic for generating each report.
                preMainReportRows = report.generateReport10b(commonData10b, form.getSelectedStartYear(), form.getSelectedEndYear(), auxCalendar, auxCurrency, auxSectors, auxStatuses,
                        auxFinancingInstruments);
            }
        }

        // Postprocess the report if needed.
        currentDate = Calendar.getInstance().getTimeInMillis();
        Collection<PIReportAbstractRow> postMainReportRows = report.reportPostProcess(preMainReportRows, form.getSelectedStartYear(), form.getSelectedEndYear());
        logger.warn("PI Time 3: " + (Calendar.getInstance().getTimeInMillis() - currentDate));

        if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5a)) {
            PIReport5a auxReport = new PIReport5a();
            int[][] miniTable = auxReport.createMiniTable(postMainReportRows, form.getSelectedStartYear(), form.getSelectedEndYear());
            report.setMiniTable(miniTable);
        } else if (report.getReportCode().equals(PIConstants.PARIS_INDICATOR_REPORT_5b)) {
            PIReport5b auxReport = new PIReport5b();
            int[][] miniTable = auxReport.createMiniTable(postMainReportRows, form.getSelectedStartYear(), form.getSelectedEndYear());
            report.setMiniTable(miniTable);
        }

        report.setReportRows(postMainReportRows);

        return report;
    }

    /*
     * Returns a collection of AmpAhSurvey objects filtered by donor and donor group.
     */
    private Collection<AmpAhsurvey> getCommonSurveyData(Collection<AmpOrganisation> filterDonors, Collection<AmpOrgGroup> filterDonorGroups) {

        Collection<AmpAhsurvey> commonData = null;
        Collection<AmpAhsurvey> commonDataUnique = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            DetachedCriteria liveActivityVersions = DetachedCriteria.forClass(AmpActivity.class).setProjection(Projections.property("ampActivityId"));

            // TODO: we need Hibernate 4 to use Criteria Queries for this (needs nested subqueries with multiple params)
            SQLQuery latestSurveysOnlySQL = session.createSQLQuery("SELECT s.amp_ahsurvey_id AS survey_ids FROM "
                    + "(select max(survey_date) AS max_date,amp_activity_id from amp_ahsurvey group by amp_activity_id) AS r "
                    + "INNER JOIN amp_ahsurvey s ON s.amp_activity_id=r.amp_activity_id AND s.survey_date=r.max_date" + " UNION "
                    + "select amp_ahsurvey_id AS survey_ids from amp_ahsurvey where survey_date is null and amp_activity_id "
                    + "not in (select amp_activity_id from amp_ahsurvey where survey_date is not null);");
            latestSurveysOnlySQL.addScalar("survey_ids", LongType.INSTANCE);
            List<Long> latestSurveysOnlyList = latestSurveysOnlySQL.list();

            // Set the query to return AmpAhSurvey objects.
            Criteria criteria = session.createCriteria(AmpAhsurvey.class);
            criteria.add(Property.forName("ampActivityId").in(liveActivityVersions));

            // IMPORTANT NOTICE: ".setFetchMode("responses", FetchMode.JOIN)" will force the query to join each survey with its responses, resulting in 2 things:
            // 1) Something we want: to retrieve all responses right now with one query and avoid thousands of selects (AMP-16944).
            // 2) Something we dont want: the list will have duplicated all the surveys, something we will correct later with "commonDataUnique = new HashSet<AmpAhsurvey>(commonData)"
            criteria.setFetchMode("ampActivityId.funding", FetchMode.JOIN).setFetchMode("ampActivityId.funding.fundingDetails", FetchMode.JOIN).setFetchMode("responses", FetchMode.JOIN);

            // Link to amp_activity view to use only the last version of an activity.
            // criteria.createAlias("ampActivityId", "activityTable");

            criteria.add(Property.forName("ampAHSurveyId").in(latestSurveysOnlyList));

            criteria.createAlias("pointOfDeliveryDonor", "podd1");
            // Explanation: Hibernate will automatically detect prior alias 'podd1' to amp_organisation
            // and use it to link with amp_org_group to create alias 'podd2', then using the same logic
            // will link 'podd2' to create 'podd3', and thats the alias I can use to access amp_org_type.
            // Trying to use podd2 to access amp_org_type fields will fail!!!
            criteria.createAlias("pointOfDeliveryDonor.orgGrpId", "podd2");
            criteria.createAlias("pointOfDeliveryDonor.orgGrpId.orgType", "podd3");

            // Set the filter for Multilateral and Bilateral PoDDs.
            criteria.add(Restrictions.in("podd3.orgTypeCode", new String[] { PIConstants.ORG_GRP_MULTILATERAL, PIConstants.ORG_GRP_BILATERAL }));
            // If needed, filter for organizations.
            if (filterDonors != null) {
                criteria.add(Restrictions.in("pointOfDeliveryDonor", filterDonors));
            }
            // If needed, filter for organization groups.
            if (filterDonorGroups != null) {
                criteria.add(Restrictions.in("podd1.orgGrpId", filterDonorGroups));
            }
            commonData = criteria.list();

            // As explained above here we convert a list with duplicated surveys into a Set with no duplicates.
            commonDataUnique = new LinkedHashSet<AmpAhsurvey>(commonData);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        return commonDataUnique;
    }

    private Collection<AmpOrganisation> getCommonSurveyDataForPI10a(Collection<AmpOrganisation> filterDonors, Collection<AmpOrgGroup> filterDonorGroups) {

        Collection<AmpOrganisation> commonData = new HashSet<AmpOrganisation>();
        Session session = null;
        try {
            // TODO: change this code to use restrictions like the
            // getCommonSurveyData() method.
            session = PersistenceManager.getRequestDBSession();
            List<AmpCalendar> calendars = session.createCriteria(AmpCalendar.class).list();
            Iterator<AmpCalendar> iterCalendar = calendars.iterator();
            while (iterCalendar.hasNext()) {
                AmpCalendar auxCalendar = iterCalendar.next();
                Set<AmpOrganisation> auxOrganisations = auxCalendar.getOrganisations();
                if (auxOrganisations != null) {
                    Iterator<AmpOrganisation> iterOrganisations = auxOrganisations.iterator();
                    while (iterOrganisations.hasNext()) {
                        AmpOrganisation auxOrganisation = iterOrganisations.next();
                        AmpOrgGroup auxOrgGrp = auxOrganisation.getOrgGrpId();
                        // Filter for Multilateral and Bilateral PoDDs.
                        if (auxOrgGrp.getOrgType().getOrgTypeCode().equals(PIConstants.ORG_GRP_BILATERAL) || auxOrgGrp.getOrgType().getOrgTypeCode().equals(PIConstants.ORG_GRP_MULTILATERAL)) {
                            boolean add = true;
                            // If needed, filter for organizations.
                            if (filterDonors != null) {
                                if (!PIUtils.containOrganisations(filterDonors, auxOrganisation)) {
                                    add = false;
                                }
                            }
                            // If needed, filter for organization groups.
                            if (filterDonorGroups != null) {
                                if (!PIUtils.containOrgGrps(filterDonorGroups, auxOrgGrp)) {
                                    add = false;
                                }
                            }
                            if (add) {
                                commonData.add(auxOrganisation);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return commonData;
    }

    private Collection<NodeWrapper> getCommonSurveyDataForPI10b(Collection<AmpOrganisation> filterDonors, Collection<AmpOrgGroup> filterDonorGroups, HttpServletRequest request) {

        Collection<NodeWrapper> commonData = new HashSet<NodeWrapper>();
        try {

            javax.jcr.Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);
            // Iterate all AmpTeamMembers.
            Iterator<AmpTeamMember> iterTeamMembers = TeamMemberUtil.getAllTeamMembers().iterator();
            while (iterTeamMembers.hasNext()) {
                // Create helper TeamMember.
                TeamMember auxTeamMember = new TeamMember(iterTeamMembers.next());
                
                // Get the main team node for this team member.
                Node teamNode = DocumentManagerUtil.getTeamNode(jcrWriteSession, auxTeamMember.getTeamId());

                // Iterate documents and get organizations.
                Iterator<Node> iter = teamNode.getNodes();
                while (iter.hasNext()) {
                    Node nextNode = (Node) iter.next();
                    NodeWrapper nextWrapper = new NodeWrapper(nextNode);

                    // Check document type.
                    AmpCategoryValue docType = CategoryManagerUtil.getAmpCategoryValueFromDb(nextWrapper.getCmDocTypeId(), true);
                    if (docType != null) {
                        if (docType.getValue().equalsIgnoreCase(TranslatorWorker.translateText(CategoryConstants.RESOURCE_TYPE_COUNTRY_ANALYTIC_REPORT_KEY))) {

                            // Only add documents that have at least 1 donor.
                            Collection<AmpOrganisation> auxOrganizations = DocumentOrganizationManager.getInstance()
                                    .getOrganizationsByUUID(nextWrapper.getUuid());
                            Iterator<AmpOrganisation> iterOrgs = auxOrganizations.iterator();
                            while (iterOrgs.hasNext()) {
                                AmpOrganisation auxOrganisation = iterOrgs.next();
                                boolean add = true;
                                // Filter by donor type.
                                if (!auxOrganisation.getOrgGrpId().getOrgType().getOrgTypeCode().equals(PIConstants.ORG_GRP_BILATERAL)
                                        && !auxOrganisation.getOrgGrpId().getOrgType().getOrgTypeCode().equals(PIConstants.ORG_GRP_MULTILATERAL)) {
                                    add = false;
                                }

                                // If needed, filter for organizations.
                                if (filterDonors != null) {
                                    if (!PIUtils.containOrganisations(filterDonors, auxOrganisation)) {
                                        add = false;
                                    }
                                }
                                // If needed, filter for organization
                                // groups.
                                if (filterDonorGroups != null) {
                                    if (!PIUtils.containOrgGrps(filterDonorGroups, auxOrganisation.getOrgGrpId())) {
                                        add = false;
                                    }
                                }
                                if (add) {
                                    commonData.add(nextWrapper);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return commonData;
    }
}
