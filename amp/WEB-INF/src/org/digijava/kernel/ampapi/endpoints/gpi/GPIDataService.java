package org.digijava.kernel.ampapi.endpoints.gpi;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.gpi.reports.GPIDocument;
import org.dgfoundation.amp.gpi.reports.GPIDonorActivityDocument;
import org.dgfoundation.amp.gpi.reports.GPIRemark;
import org.digijava.kernel.ampapi.endpoints.dto.*;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.errors.GenericErrors;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion.GPINiQuestionType;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @author gerald
 *
 */
public class GPIDataService {
    public static AmpGPINiAidOnBudget getAidOnBudgetById(Long id) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        return GPIUtils.getAidOnBudgetById(id);
    }

    public static ResultPage<AmpGPINiAidOnBudget> getAidOnBudgetList(
            Integer offset, Integer count, String orderBy, String sort) {

        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        Integer total = GPIUtils.getAidOnBudgetCount();
        List<AmpGPINiAidOnBudget> aidOnBudgetList = GPIUtils.getAidOnBudgetList(offset, count, orderBy, sort, total);
        return new ResultPage<>(aidOnBudgetList, total);
    }

    public static SaveResult<AmpGPINiAidOnBudget> saveAidOnBudget(AmpGPINiAidOnBudget data) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        List<Map<String, String>> validationErrors = validateAidOnBudget(data);
        if (validationErrors.size() == 0) {
            GPIUtils.saveAidOnBudget(data);
            return new SaveResult<>(data);
        } else {
            return new SaveResult<>(data, validationErrors);
        }
    }

    public static List<SaveResult<AmpGPINiAidOnBudget>> saveAidOnBudget(List<AmpGPINiAidOnBudget> aidOnBudgetList) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        List<SaveResult<AmpGPINiAidOnBudget>> results = new ArrayList<>();
        for (AmpGPINiAidOnBudget aidOnBudget : aidOnBudgetList) {
            results.add(saveAidOnBudget(aidOnBudget));
        }

        return results;
    }

    private static List<Map<String, String>> validateAidOnBudget(AmpGPINiAidOnBudget data) {
        List<Map<String, String>> validationErrors = new ArrayList<>();
        Long donorId = data.getDonor().getAmpOrgId();
        Date date = data.getIndicatorDate();
        Long id = data.getAmpGPINiAidOnBudgetId();

        if (GPIUtils.checkAidOnBudgetExists(id, donorId, date)) {
            Map<String, String> error = new HashMap<>();
            error.put(GPIErrors.DATE_DONOR_COMBINATION_EXISTS.getErrorId(),
                    GPIErrors.DATE_DONOR_COMBINATION_EXISTS.description);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public static void deleteAidOnBudgetById(Long id) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        GPIUtils.deleteAidOnBudget(id);
    }

    public static SaveResult<AmpGPINiDonorNotes> saveDonorNotes(AmpGPINiDonorNotes note) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        List<Map<String, String>> validationErrors = validateDonorNotes(note);
        if (validationErrors.isEmpty()) {

            Date date = DateTimeUtil.parseDate("2018-11-25", "yyyy-MM-dd");
            System.out.println(date.getTime());
            System.out.println(note.getNotesDate().getTime());

            GPIUtils.saveDonorNotes(note);
            return new SaveResult<>(note);
        } else {
            return new SaveResult<>(note, validationErrors);
        }
    }

    public static List<SaveResult<AmpGPINiDonorNotes>> saveDonorNotes(List<AmpGPINiDonorNotes> notes) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        List<SaveResult<AmpGPINiDonorNotes>> results = new ArrayList<>();
        for (AmpGPINiDonorNotes note : notes) {
            results.add(saveDonorNotes(note));
        }
        return results;
    }

    private static List<Map<String, String>> validateDonorNotes(AmpGPINiDonorNotes data) {
        List<Map<String, String>> validationErrors = new ArrayList<>();
        Long donorId = data.getDonor().getAmpOrgId();
        Date date = data.getNotesDate();
        Long id = data.getAmpGPINiDonorNotesId();

        if (GPIUtils.checkDonorNotesExists(id, donorId, date, data.getIndicatorCode())) {
            Map<String, String> error = new HashMap<>();
            error.put(GPIErrors.DATE_DONOR_COMBINATION_EXISTS.getErrorId(),
                    GPIErrors.DATE_DONOR_COMBINATION_EXISTS.description);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public static ResultPage<AmpGPINiDonorNotes> getDonorNotesList(
            Integer offset, Integer count, String orderBy, String sort, String indicatorCode) {

        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        Integer total = GPIUtils.getDonorNotesCount(indicatorCode);
        List<AmpGPINiDonorNotes> notesList = GPIUtils.getDonorNotesList(offset, count, orderBy, sort, total,
                indicatorCode);

        return new ResultPage<>(notesList, total);
    }

    public static void deleteDonorNotesById(Long id) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        GPIUtils.deleteDonorNotes(id);
    }

    private static boolean hasGPIDataRights() {
        TeamMember tm = TeamUtil.getCurrentMember();
        AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
        return atm.getUser().hasNationalCoordinatorGroup() || atm.getUser().hasVerifiedDonor();
    }

    public static List<Org> getUsersVerifiedOrganizations() {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponseService.reportForbiddenAccess(GenericErrors.UNAUTHORIZED);
        }

        TeamMember tm = TeamUtil.getCurrentMember();
        AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
        Set<AmpOrganisation> verifiedOrgs = atm.getUser().getAssignedOrgs();
        List<Org> orgs = new ArrayList<>();

        for (AmpOrganisation verifiedOrg : verifiedOrgs) {
            orgs.add(new Org(verifiedOrg.getAmpOrgId(), verifiedOrg.getName(), verifiedOrg.getAcronym()));
        }

        return orgs;
    }

    /**
     * get the list of remarks used in indicator 1 and 5a
     * 
     * @param indicatorCode
     * @param donorIds
     * @param donorType
     * @param from
     * @param to
     * @return
     */
    public static List<GPIRemark> getGPIRemarks(String indicatorCode, List<Long> donorIds, String donorType, Long from,
            Long to) {

        List<GPIRemark> remarks = new ArrayList<>();
        AmpDateFormatter dateFormatter = AmpDateFormatterFactory.getLocalizedFormatter(DateTimeUtil.getGlobalPattern());
        List<AmpGPINiDonorNotes> donorNotes = GPIUtils.getNotesByCode(indicatorCode);
        List<AmpGPINiDonorNotes> filteredNotes = GPIUtils.filterNotes(donorNotes, donorIds, donorType, from, to);
        filteredNotes.forEach(n -> {
            remarks.add(new GPIRemark(n.getDonor().getName(), dateFormatter.format(n.getNotesDate()), n.getNotes()));
        });

        return remarks;
    }

    /**
     * 
     * @param activityDonors
     * @return
     */
    public static List<GPIDonorActivityDocument> getGPIDocuments(List<GPIDonorActivityDocument> activityDonors) {
        List<GPIDonorActivityDocument> gpiDocuments = new ArrayList<>();
        List<AmpGPINiSurveyResponseDocument> surveyDocuments = new ArrayList<>();

        if (activityDonors != null) {
            Session dbSession = PersistenceManager.getSession();
            String queryString = "SELECT surveyDocuments FROM " + AmpGPINiSurveyResponseDocument.class.getName()
                    + " surveyDocuments";
            Query query = dbSession.createQuery(queryString);
            surveyDocuments = query.list();
        }

        gpiDocuments = filterDocuments(surveyDocuments, activityDonors);

        return gpiDocuments;
    }

    /**
     * 
     * @param donorId
     * @param activityId
     * @return
     */
    public static List<GPIDonorActivityDocument> getGPIDocuments(String donorId, String activityId) {
        List<GPIDonorActivityDocument> donorActList = new ArrayList<>();
        GPIDonorActivityDocument actDonorDocument = new GPIDonorActivityDocument(donorId, activityId);
        donorActList.add(actDonorDocument);

        return getGPIDocuments(donorActList);
    }

    /**
     * Get filtered documents for specific donors and activities
     * 
     * @param documents
     * @param activityDonors
     * @return
     */
    private static List<GPIDonorActivityDocument> filterDocuments(List<AmpGPINiSurveyResponseDocument> documents,
            List<GPIDonorActivityDocument> activityDonors) {

        Set<Long> donorIds = activityDonors.stream().map(ad -> Long.valueOf(ad.getDonorId()))
                .collect(Collectors.toSet());

        Set<Long> activityIds = activityDonors.stream().map(ad -> Long.valueOf(ad.getActivityId()))
                .collect(Collectors.toSet());

        List<AmpGPINiSurveyResponseDocument> filteredDocuments = documents.stream()
                .filter(doc -> donorIds.contains(
                        doc.getSurveyResponse().getAmpGPINiSurvey().getAmpOrgRole().getOrganisation().getAmpOrgId()))
                .filter(doc -> activityIds.contains(
                        doc.getSurveyResponse().getAmpGPINiSurvey().getAmpOrgRole().getActivity().getAmpActivityId()))
                .collect(Collectors.toList());

        List<GPIDonorActivityDocument> donorActivityDocuments = getGrouppedDocuments(filteredDocuments);
        donorActivityDocuments.sort(getGPIDocumentComparator());

        return donorActivityDocuments;
    }

    /**
     * Transform the list of AmpGPINiSurveyResponseDocument in a list of
     * GPIDonorActivityDocument
     * 
     * @param filteredDocuments
     */
    private static List<GPIDonorActivityDocument> getGrouppedDocuments(
            List<AmpGPINiSurveyResponseDocument> filteredDocuments) {

        List<GPIDonorActivityDocument> donorActivityDocuments = new ArrayList<>();
        Map<Long, Map<Long, List<AmpGPINiSurveyResponseDocument>>> grouppedDocuments = 
                new HashMap<Long, Map<Long, List<AmpGPINiSurveyResponseDocument>>>();

        filteredDocuments.forEach(doc -> {
            AmpOrgRole orgRole = doc.getSurveyResponse().getAmpGPINiSurvey().getAmpOrgRole();
            Long donorId = orgRole.getOrganisation().getAmpOrgId();
            Long actId = orgRole.getActivity().getAmpActivityId();

            Map<Long, List<AmpGPINiSurveyResponseDocument>> actDocMap = grouppedDocuments.containsKey(donorId)
                    ? grouppedDocuments.get(donorId) : new HashMap<Long, List<AmpGPINiSurveyResponseDocument>>();

            List<AmpGPINiSurveyResponseDocument> actDocs = actDocMap.containsKey(actId) ? actDocMap.get(actId)
                    : new ArrayList<>();

            actDocs.add(doc);
            actDocMap.put(actId, actDocs);
            grouppedDocuments.put(donorId, actDocMap);
        });

        grouppedDocuments.entrySet().forEach(donor -> {
            donor.getValue().entrySet().forEach(act -> {
                List<GPIDocument> supportiveDocs = new ArrayList<>();
                act.getValue().forEach(doc -> {
                    HttpServletRequest req = TLSUtils.getRequest();
                    Node node = DocumentManagerUtil.getWriteNode(doc.getUuid(), req);
                    if (node != null) {
                        NodeWrapper nw = new NodeWrapper(node);
                        GPIDocument gpiDoc = new GPIDocument();
                        gpiDoc.setTitle(nw.getTitle());
                        gpiDoc.setDescription(doc.getSurveyResponse().getAmpGPINiQuestion().getDescription());
                        gpiDoc.setQuestion(doc.getSurveyResponse().getAmpGPINiQuestion().getCode());

                        if (nw.getWebLink() == null) {
                            gpiDoc.setUrl(getURLForDownload(req, doc.getUuid()));
                            gpiDoc.setType(GPINiQuestionType.DOCUMENT.toString());
                        } else {
                            gpiDoc.setUrl(nw.getWebLink());
                            gpiDoc.setType(GPINiQuestionType.LINK.toString());
                        }

                        supportiveDocs.add(gpiDoc);
                    }
                });
                donorActivityDocuments.add(new GPIDonorActivityDocument(String.valueOf(donor.getKey()),
                        String.valueOf(act.getKey()), supportiveDocs));
            });
        });

        return donorActivityDocuments;
    }

    /**
     * Get the URL for downloading the document
     * 
     * @param req
     * @param uuid
     * @return
     */
    public static String getURLForDownload(HttpServletRequest req, String uuid) {
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath(); //

        StringBuilder downloadUrl = new StringBuilder();
        downloadUrl.append(scheme).append("://").append(serverName);

        if (serverPort != GPIEPConstants.DEFAULT_HTTP_PORT && serverPort != GPIEPConstants.DEFAULT_HTTPS_PORT) {
            downloadUrl.append(":").append(serverPort);
        }

        downloadUrl.append(contextPath);
        downloadUrl.append("/contentrepository/downloadFile.do?uuid=").append(uuid);

        return downloadUrl.toString();
    }

    private static Comparator<GPIDonorActivityDocument> getGPIDocumentComparator() {
        return (GPIDonorActivityDocument ad1, GPIDonorActivityDocument ad2) -> {
            if (ad1.getDonorId().compareTo(ad2.getDonorId()) == 0) {
                return ad1.getActivityId().compareTo(ad2.getActivityId());
            } else {
                return ad1.getDonorId().compareTo(ad2.getDonorId());
            }
        };
    }

    public static List<YearsForCalendar> getYears() {
        List<AmpFiscalCalendar> calendars = FiscalCalendarUtil.getAllAmpFiscalCalendars();
        List<YearsForCalendar> result = new ArrayList<>();
        int numberOfYears = getNumberOfYears(calendars);
        for (AmpFiscalCalendar calendar : calendars) {
            int startYear = AmpARFilter.getDefaultYear(AmpARFilter.getEffectiveSettings(), calendar, true);
            int endYear = startYear + numberOfYears;
            List<YearWithRange> years = new ArrayList<>();
            for (int i = startYear; i <= endYear; i++) {
                Date start = GPIUtils.getYearStartDate(calendar, i);
                Date end = GPIUtils.getYearEndDate(calendar, i);
                years.add(new YearWithRange(i, start, end));
            }
            result.add(new YearsForCalendar(calendar.getAmpFiscalCalId(), years));
        }
        return result;
    }
    
   private static Integer getNumberOfYears(List<AmpFiscalCalendar> calendars) {
        for (AmpFiscalCalendar calendar : calendars) {
            if (calendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_GREGORIAN.getValue())) {
                int currentYear = FiscalCalendarUtil.getCurrentYear();
                int startYear = AmpARFilter.getDefaultYear(AmpARFilter.getEffectiveSettings(), calendar, true);
                return currentYear - startYear;
            }
        }

        return 0;
    }
    public static String getConvertedDate(Long fromCalId, Long toCalId, String dateAsString) {
        
        if (fromCalId == toCalId) {
            return dateAsString;
        }
        
        AmpFiscalCalendar fromCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(fromCalId);
        AmpFiscalCalendar toCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(toCalId);
        
        if (fromCalendar == null) {
            throw new ApiRuntimeException(ApiError.toError("Invalid fromCalId [" + fromCalId + "]"));
        }
        
        if (toCalendar == null) {
            throw new ApiRuntimeException(ApiError.toError("Invalid toCalId [" + toCalId + "]"));
        }
        
        DateTime dateTime = new DateTime();
        Scanner scanner = new Scanner(dateAsString).useDelimiter("[^\\d]+");
        try {
            dateTime = dateTime.withDate(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        } catch (Exception e) {
            throw new ApiRuntimeException(ApiError.toError("Error creating the date [" + dateAsString + "]. "
                    + "It should have the format yyyy-MM-dd"));
        }
        
        DateTime convertedDate = FiscalCalendarUtil.convertDate(fromCalendar, dateTime.toDate(), toCalendar);

        return String.format("%d-%02d-%02d", 
                convertedDate.getYear(), convertedDate.getMonthOfYear(), convertedDate.getDayOfMonth());
    }
    
    public static List<Org> getDonors() {
        return GPIUtils.getDonors();

    }

}
