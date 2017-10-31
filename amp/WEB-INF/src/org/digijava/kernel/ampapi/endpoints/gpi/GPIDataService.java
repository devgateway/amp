package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.math.NumberUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.gpi.reports.GPIDocument;
import org.dgfoundation.amp.gpi.reports.GPIDonorActivityDocument;
import org.dgfoundation.amp.gpi.reports.GPIRemark;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion.GPINiQuestionType;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponseDocument;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.translation.exotic.AmpDateFormatter;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

/**
 * 
 * @author gerald
 *
 */
public class GPIDataService {
    public static JsonBean getAidOnBudgetById(Long id) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        AmpGPINiAidOnBudget aidOnBudget = GPIUtils.getAidOnBudgetById(id);
        if (aidOnBudget != null) {
            return modelToJsonBean(aidOnBudget);
        } else {
            return null;
        }
    }

    public static JsonBean getAidOnBudgetList(Integer offset, Integer count, String orderBy, String sort) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        Integer total = GPIUtils.getAidOnBudgetCount();
        List<AmpGPINiAidOnBudget> aidOnBudgetList = GPIUtils.getAidOnBudgetList(offset, count, orderBy, sort, total);
        JsonBean data = new JsonBean();
        List<JsonBean> lst = new ArrayList<>();
        for (AmpGPINiAidOnBudget aidOnBudget : aidOnBudgetList) {
            lst.add(modelToJsonBean(aidOnBudget));
        }

        data.set("data", lst);
        data.set(GPIEPConstants.TOTAL_RECORDS, total);
        return data;
    }

    private static JsonBean modelToJsonBean(AmpGPINiAidOnBudget aidOnBudget) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        JsonBean data = new JsonBean();
        data.set(GPIEPConstants.FIELD_ID, aidOnBudget.getAmpGPINiAidOnBudgetId());
        data.set(GPIEPConstants.FIELD_DONOR_ID, aidOnBudget.getDonor().getAmpOrgId());
        data.set(GPIEPConstants.FIELD_CURRENCY_CODE, aidOnBudget.getCurrency().getCurrencyCode());
        data.set(GPIEPConstants.FIELD_AMOUNT, aidOnBudget.getAmount());
        data.set(GPIEPConstants.FIELD_DATE,
                DateTimeUtil.formatDate(aidOnBudget.getIndicatorDate(), GPIEPConstants.DATE_FORMAT));
        return data;
    }

    private static AmpGPINiAidOnBudget getAidOnBudget(JsonBean data) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        Long id;
        AmpGPINiAidOnBudget aidOnBudget = null;
        if (data.getString(GPIEPConstants.FIELD_ID) != null
                && NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
            id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
            aidOnBudget = GPIUtils.getAidOnBudgetById(id);
        } else {
            aidOnBudget = new AmpGPINiAidOnBudget();
        }

        return aidOnBudget;
    }

    private static AmpGPINiAidOnBudget updateModel(AmpGPINiAidOnBudget aidOnBudget, JsonBean data) {
        if (data.get(GPIEPConstants.FIELD_CURRENCY_CODE) != null) {
            String currencyCode = data.getString((GPIEPConstants.FIELD_CURRENCY_CODE));
            aidOnBudget.setCurrency(CurrencyUtil.getAmpcurrency(currencyCode));
        }

        if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
            Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
            aidOnBudget.setDonor(GPIUtils.getOrganisation(donorId));
        }

        if (data.get(GPIEPConstants.FIELD_AMOUNT) != null) {
            aidOnBudget.setAmount((Double.parseDouble(String.valueOf(data.get(GPIEPConstants.FIELD_AMOUNT)))));
        }

        if (data.getString(GPIEPConstants.FIELD_DATE) != null) {
            Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
            aidOnBudget.setIndicatorDate(date);
        }

        return aidOnBudget;
    }

    public static JsonBean saveAidOnBudget(JsonBean data) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        JsonBean result = new JsonBean();
        List<JsonBean> validationErrors = validateAidOnBudget(data);
        if (validationErrors.size() == 0) {
            AmpGPINiAidOnBudget aidOnBudget = getAidOnBudget(data);
            updateModel(aidOnBudget, data);
            GPIUtils.saveAidOnBudget(aidOnBudget);
            JsonBean saved = modelToJsonBean(aidOnBudget);
            result.set(GPIEPConstants.DATA, saved);
            result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVED);
            if (data.get(GPIEPConstants.CID) != null) {
                saved.set(GPIEPConstants.CID, data.get(GPIEPConstants.CID));
            }
        } else {
            result.set(GPIEPConstants.DATA, data);
            result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVE_FAILED);
            result.set(GPIEPConstants.ERRORS, validationErrors);
        }

        return result;
    }

    public static List<JsonBean> saveAidOnBudget(List<JsonBean> aidOnBudgetList) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        List<JsonBean> results = new ArrayList<>();
        for (JsonBean aidOnBudget : aidOnBudgetList) {
            results.add(saveAidOnBudget(aidOnBudget));
        }

        return results;
    }

    private static List<JsonBean> validateAidOnBudget(JsonBean data) {
        List<JsonBean> validationErrors = new ArrayList<>();
        Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
        Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
        Long id = null;
        if (data.get(GPIEPConstants.FIELD_ID) != null) {
            id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
        }

        if (GPIUtils.checkAidOnBudgetExists(id, donorId, date)) {
            JsonBean error = new JsonBean();
            error.set(ApiError.getErrorCode(GPIErrors.AID_ON_BUDGET_DATE_DONOR_COMBINATION_EXISTS),
                    GPIErrors.AID_ON_BUDGET_DATE_DONOR_COMBINATION_EXISTS.description);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public static JsonBean deleteAidOnBudgetById(Long id) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        JsonBean result = new JsonBean();
        GPIUtils.deleteAidOnBudget(id);
        result.set(GPIEPConstants.RESULT, GPIEPConstants.DELETED);
        return result;
    }

    public static JsonBean saveDonorNotes(JsonBean data) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        JsonBean result = new JsonBean();
        List<JsonBean> validationErrors = validateDonorNotes(data);

        if (validationErrors.isEmpty()) {
            AmpGPINiDonorNotes donorNotes = getOrCreateDonorNotes(data);
            GPIUtils.saveDonorNotes(donorNotes);
            JsonBean saved = modelToJsonBean(donorNotes);
            result.set(GPIEPConstants.DATA, saved);
            result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVED);
            if (data.get(GPIEPConstants.CID) != null) {
                saved.set(GPIEPConstants.CID, data.get(GPIEPConstants.CID));
            }

        } else {
            result.set(GPIEPConstants.DATA, data);
            result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVE_FAILED);
            result.set(GPIEPConstants.ERRORS, validationErrors);

        }
        return result;
    }

    public static List<JsonBean> saveDonorNotes(List<JsonBean> donorNotesList) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        List<JsonBean> results = new ArrayList<>();
        for (JsonBean donorNotes : donorNotesList) {
            results.add(saveDonorNotes(donorNotes));
        }
        return results;
    }

    private static AmpGPINiDonorNotes getOrCreateDonorNotes(JsonBean data) {
        Long id;
        AmpGPINiDonorNotes donorNotes;
        if (data.getString(GPIEPConstants.FIELD_ID) != null
                && NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
            id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
            donorNotes = GPIUtils.getDonorNotesById(id);
        } else {
            donorNotes = new AmpGPINiDonorNotes();
        }

        donorNotes.setNotes(data.getString(GPIEPConstants.FIELD_NOTES));
        if (data.getString(GPIEPConstants.FIELD_NOTES_DATE) != null) {
            Date notesDate = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_NOTES_DATE),
                    GPIEPConstants.DATE_FORMAT);
            donorNotes.setNotesDate(notesDate);
        }

        if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
            Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
            donorNotes.setDonor(GPIUtils.getOrganisation(donorId));
        }

        donorNotes.setIndicatorCode(data.getString(GPIEPConstants.FIELD_INDICATOR_CODE));

        return donorNotes;
    }

    private static JsonBean modelToJsonBean(AmpGPINiDonorNotes donorNotes) {
        JsonBean data = new JsonBean();
        data.set(GPIEPConstants.FIELD_ID, donorNotes.getAmpGPINiDonorNotesId());
        data.set(GPIEPConstants.FIELD_DONOR_ID, donorNotes.getDonor().getAmpOrgId());
        data.set(GPIEPConstants.FIELD_NOTES, donorNotes.getNotes());
        data.set(GPIEPConstants.FIELD_INDICATOR_CODE, donorNotes.getIndicatorCode());
        data.set(GPIEPConstants.FIELD_NOTES_DATE,
                DateTimeUtil.formatDate(donorNotes.getNotesDate(), GPIEPConstants.DATE_FORMAT));
        return data;
    }

    private static List<JsonBean> validateDonorNotes(JsonBean data) {
        List<JsonBean> validationErrors = new ArrayList<>();
        Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
        Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_NOTES_DATE), GPIEPConstants.DATE_FORMAT);
        Long id = null;
        if (data.get(GPIEPConstants.FIELD_ID) != null) {
            id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
        }

        if (GPIUtils.checkDonorNotesExists(id, donorId, date, data.getString(GPIEPConstants.FIELD_INDICATOR_CODE))) {
            JsonBean error = new JsonBean();
            error.set(ApiError.getErrorCode(GPIErrors.DONOR_NOTES_DATE_DONOR_COMBINATION_EXISTS),
                    GPIErrors.DONOR_NOTES_DATE_DONOR_COMBINATION_EXISTS.description);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public static JsonBean getDonorNotesList(Integer offset, Integer count, String orderBy, String sort,
            String indicatorCode) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        Integer total = GPIUtils.getDonorNotesCount(indicatorCode);
        List<AmpGPINiDonorNotes> notesList = GPIUtils.getDonorNotesList(offset, count, orderBy, sort, total,
                indicatorCode);
        JsonBean data = new JsonBean();
        List<JsonBean> lst = new ArrayList<>();

        for (AmpGPINiDonorNotes notes : notesList) {
            lst.add(modelToJsonBean(notes));
        }

        data.set("data", lst);
        data.set(GPIEPConstants.TOTAL_RECORDS, total);
        return data;
    }

    public static JsonBean deleteDonorNotesById(Long id) {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        JsonBean result = new JsonBean();
        GPIUtils.deleteDonorNotes(id);
        result.set(GPIEPConstants.RESULT, GPIEPConstants.DELETED);
        return result;
    }

    private static boolean hasGPIDataRights() {
        TeamMember tm = TeamUtil.getCurrentMember();
        AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
        return atm.getUser().hasNationalCoordinatorGroup() || atm.getUser().hasVerifiedDonor();
    }

    public static List<JsonBean> getUsersVerifiedOrganizations() {
        if (Boolean.FALSE.equals(hasGPIDataRights())) {
            ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
        }

        TeamMember tm = TeamUtil.getCurrentMember();
        AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
        Set<AmpOrganisation> verifiedOrgs = atm.getUser().getAssignedOrgs();
        List<JsonBean> orgs = new ArrayList<>();

        for (AmpOrganisation verifiedOrg : verifiedOrgs) {
            JsonBean org = new JsonBean();
            org.set("id", verifiedOrg.getAmpOrgId());
            org.set("name", verifiedOrg.getName());
            orgs.add(org);
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

    public static List<JsonBean> getYears() {
        List<AmpFiscalCalendar> calendars = FiscalCalendarUtil.getAllAmpFiscalCalendars();
        List<JsonBean> result = new ArrayList<>();
        int numberOfYears = getNumberOfYears(calendars);
        for (AmpFiscalCalendar calendar : calendars) {
            JsonBean yearRange = new JsonBean();
            yearRange.set("calendarId", calendar.getAmpFiscalCalId());
            int startYear = AmpARFilter.getDefaultYear(AmpARFilter.getEffectiveSettings(), calendar, true);
            int endYear = startYear + numberOfYears;
            List<JsonBean> years = new ArrayList<>();
            for (int i = startYear; i <= endYear; i++) {
                JsonBean yearObject = new JsonBean();
                yearObject.set("year", i);
                Date start = GPIUtils.getYearStartDate(calendar, i);
                Date end = GPIUtils.getYearEndDate(calendar, i);
                yearObject.set("start", DateTimeUtil.formatDate(start, GPIEPConstants.DATE_FORMAT));
                yearObject.set("end", DateTimeUtil.formatDate(end, GPIEPConstants.DATE_FORMAT));
                years.add(yearObject);
            }
            yearRange.set("years", years);
            result.add(yearRange);
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
    
    public static List<JsonBean> getDonors() {
        return GPIUtils.getDonors();

    }

}
