package org.digijava.module.aim.action;

import java.lang.reflect.Method;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.OrgProfileReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.donorReport.DonorReportHelper;
import org.digijava.module.aim.helper.donorReport.OrgProfileReportHelper;
import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.OrganizationReportColumn;
import org.digijava.module.aim.helper.donorReport.OrganizationReportRecord;
import org.digijava.module.aim.helper.donorReport.PropertyType;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.aim.util.DbUtil;

public class OrganizationProfileReportGenerator extends DispatchAction {

    private final String[] COLUMNS = { "Organization Name",
            "Organization Acronym", "Organization Type", "Organization Group",
            "DAC Code", "ISO Code", "Organization Code",
            "Budget Organization Code", "Fiscal Calendar",
            "Sector Preferences", "Contact Information", "Address",
            "Description", "Organization Primary Purpose",
            "Registration Number in MinPlan", "Registration Date in MinPlan",
            "Legal Personality Number",
            "Legal personality registration date in the country of origin",
            "Operation approval date in the country of origin",
            "Line Ministry Registration Number",
            "Receipt of legal personality act in DRC",
            "Registration date of Line Ministry", "Recipients",
            "Country Of Origin", "Tax Number",
            "Organization Intervention Location", "Organization website",
            "Organization Address Abroad(Internation NGO)",
            "Staff Information", "Budget Information", "Funding Org Id" };

    private static Logger logger = Logger
            .getLogger(OrganizationProfileReportGenerator.class);

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return loadWizard(mapping, form, request, response);
    }

    public ActionForward loadWizard(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        OrgProfileReportForm orgProfileForm = (OrgProfileReportForm) form;
        orgProfileForm.setColumns(COLUMNS);
        if(request.getParameter("reset")!=null){
            orgProfileForm.setSelectedColumns(null);
        }
        HashMap<String, DonorReportHelper> map = null;
        try {
            map = new HashMap<String, DonorReportHelper>();
            Method[] methods = AmpOrganisation.class.getMethods();
            for (Method method : methods) {
                OrganizationReportColumn annotation = method
                        .getAnnotation(OrganizationReportColumn.class);
                if (annotation != null) {
                    PropertyType type = annotation.propertyType();
                    Class<?> clazz = annotation.returnedClass();
                    String[] subHeaders = null;
                    if (!clazz.equals(Object.class)) {
                        OrgProfileValue orgProfileValue = (OrgProfileValue) clazz.newInstance();
                        subHeaders = orgProfileValue.getSubHeaders();
                    }
                    DonorReportHelper helper = new DonorReportHelper(method,
                            type, (subHeaders == null) ? null
                                    : Arrays.asList(subHeaders));
                    map.put(annotation.columnName(), helper);
                }
            }
            orgProfileForm.setMap(map);
        } catch (Exception ex) {
            logger.error("error!" + ex.getMessage());
        }

        List<AmpOrganisation> organizations = DbUtil.getAmpOrganisations();
        orgProfileForm.setOrgTypes(DbUtil.getAllOrgTypes());
        //For issue 
        orgProfileForm.setGroups(DbUtil.getAllOrganisationGroup());
        orgProfileForm.setOrganizations(organizations);
        return mapping.findForward("forward");
    }

    public ActionForward generateExcelFile(ActionMapping mapping,
            ActionForm form, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        OrgProfileReportForm orgProfileForm = (OrgProfileReportForm) form;
        Site site = RequestUtils.getSite(request);
        Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
        Long siteId = site.getId();
        String locale = navigationLanguage.getCode();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=Export.xls");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("export");
        // title cells
        HSSFCellStyle titleCS = wb.createCellStyle();
        titleCS.setWrapText(true);
        titleCS.setFillForegroundColor(HSSFColor.BROWN.index);
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontHeader.setFontHeightInPoints((short) 14);
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleCS.setFont(fontHeader);

        HSSFCellStyle subHeadertitleCS = wb.createCellStyle();
        subHeadertitleCS.setWrapText(true);
        subHeadertitleCS.setFillForegroundColor(HSSFColor.BROWN.index);
        HSSFFont fontSubHeader = wb.createFont();
        fontSubHeader.setFontName(HSSFFont.FONT_ARIAL);
        fontSubHeader.setFontHeightInPoints((short) 10);
        fontSubHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        subHeadertitleCS.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        subHeadertitleCS.setFont(fontSubHeader);
        
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setWrapText(true); //enabling multiple lines


        int rowIndex = 0;
        int cellIndex = 0;

        HSSFRow titleRow = sheet.createRow(rowIndex++);
        HSSFCell titleCell = titleRow.createCell(cellIndex++);
        HSSFRichTextString title = new HSSFRichTextString(
                TranslatorWorker.translateText("Donor Profile Report", locale,
                        siteId));
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleCS);

        List<OrganizationReportRecord> records = orgProfileForm.getRecords();

        if (records != null) {
            for (OrganizationReportRecord record : records) {
                cellIndex = 0;
                HSSFRow row = sheet.createRow(rowIndex++);
                HSSFCell mainHeaderCell = row.createCell(cellIndex);
                mainHeaderCell.setCellValue(record.getOrganizationName());
                mainHeaderCell.setCellStyle(titleCS);

                for (OrgProfileReportHelper helper : record.getHelpers()) {

                    if (helper.isAllTypeProperty()
                            || helper.isNgoOnlyProperty() == record.isNgo()) {
                        if (helper.getSubHeaders() == null
                                || helper.getSubHeaders().isEmpty()) {

                            if (helper.getValues() != null
                                    && !helper.getValues().isEmpty()) {

                                row = sheet.createRow(rowIndex++);
                                cellIndex = 0;

                                row.createCell(cellIndex++).setCellValue(
                                        TranslatorWorker.translateText(helper.getColumnName(), locale, siteId));

                                HSSFCell cell = row.createCell(cellIndex++);
                                cell.setCellStyle(cs);
                                StringBuilder values = new StringBuilder();
                                for (List<String> value : helper.getValues()) {
                                    for (String val : value) {
                                        values.append(val);
                                        values.append('\n');
                                    }
                                }
                                cell.setCellValue(values.toString());
                            }
                        } else {
                            cellIndex = 0;
                            HSSFCell headerCell = row.createCell(cellIndex);
                            headerCell.setCellValue(helper.getColumnName());
                            headerCell.setCellStyle(subHeadertitleCS);
                            CellRangeAddress region = new CellRangeAddress(
                                    rowIndex - 1, rowIndex - 1, cellIndex,
                                    cellIndex + helper.getSubHeaders().size()
                                            - 1);
                            sheet.addMergedRegion(region);
                            row = sheet.createRow(rowIndex++);
                            for (String subHeader : helper.getSubHeaders()) {
                                HSSFCell subHeaderCell = row
                                        .createCell(cellIndex++);
                                HSSFRichTextString subHeadertitle = new HSSFRichTextString(
                                        subHeader);
                                subHeaderCell.setCellValue(subHeadertitle);
                                subHeaderCell.setCellStyle(subHeadertitleCS);
                            }
                            if (helper.getValues() != null
                                    && !helper.getValues().isEmpty()) {
                                for (int i = 0; i < helper.getValues().size(); i++) {
                                    if (i % helper.getSubHeaders().size() == 0) {
                                        row = sheet.createRow(rowIndex++);
                                        cellIndex = 0;
                                    }

                                    StringBuilder values = new StringBuilder();
                                    if(helper.getValues().get(i)!=null && helper.getValues().get(i).size()>0){
                                        for (String val : helper.getValues().get(i)) {
                                            values.append(val);
                                            values.append('\n');
                                        }
                                    }
                                    HSSFCell cell = row.createCell(cellIndex++);
                                    cell.setCellStyle(cs);
                                    cell.setCellValue(values.toString());
                                }

                            }
                        }

                    }
                }
                row = sheet.createRow(rowIndex++);
            }
        }
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
        wb.write(response.getOutputStream());
        return null;

    }

    public ActionForward makeReport(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        OrgProfileReportForm orgProfileForm = (OrgProfileReportForm) form;
        String[] selectedColumns = orgProfileForm.getSelectedColumnsList()
                .split("&selectedColumns=");
        orgProfileForm.setSelectedColumns(selectedColumns);
        HashMap<String, DonorReportHelper> map = orgProfileForm.getMap();
        Long siteId = RequestUtils.getSiteDomain(request).getSite().getId();
        String locale = RequestUtils.getNavigationLanguage(request).getCode();
        List<AmpOrganisation> filteredOrganizations = DbUtil
                .getAmpOrganisations(orgProfileForm.getSelectedOrgId(),
                        orgProfileForm.getSelectedOrgGropId(),
                        orgProfileForm.getSelectedTypeId());
        orgProfileForm.setRecords(new ArrayList<OrganizationReportRecord>());
        if (filteredOrganizations != null) {
            for (AmpOrganisation org : filteredOrganizations) {
                OrganizationReportRecord record = new OrganizationReportRecord();
                orgProfileForm.getRecords().add(record);
                record.setOrganizationName(org.getName());
                record.setOrgId(org.getAmpOrgId());
                String classification = org.getOrgGrpId().getOrgType()
                        .getClassification();
                if (classification != null) {
                    record.setNgo(classification.equals(Constants.ORG_TYPE_NGO));
                } else {
                    record.setNgo(false);
                }
                record.setHelpers(new ArrayList<OrgProfileReportHelper>());
                for (String selectedColumn : selectedColumns) {
                    DonorReportHelper helper = map.get(selectedColumn);
                    OrgProfileReportHelper reportHelper = new OrgProfileReportHelper();
                    reportHelper.setColumnName(selectedColumn);
                    reportHelper.setNgoOnlyProperty(helper.getType().equals(
                            PropertyType.NGO));
                    reportHelper.setAllTypeProperty(helper.getType().equals(
                            PropertyType.BOTH));
                    reportHelper.setValues(new ArrayList<List<String>>());
                    reportHelper.setSubHeaders(helper.getSubHeaders());
                    Method method = helper.getMethod();
                    method.setAccessible(true);
                    Object obj = method.invoke(org, null);
                    if (obj != null) {
                        if (obj.getClass().equals(Date.class)) {
                            reportHelper.getValues().add(
                                    Arrays.asList(new String[] { FormatHelper
                                            .formatDate((Date) obj) }));
                        } else {
                            if (Collection.class.isAssignableFrom(obj
                                    .getClass())) {
                                Collection col = (Collection) obj;
                                for (Object el : col) {
                                    OrgProfileValue orgProfileValue = (OrgProfileValue) el;
                                    List<ValueTranslatabePair> values = orgProfileValue
                                            .getValuesForOrgReport();
                                    fillValues(siteId, locale, reportHelper,
                                            values);

                                }
                            } else {
                                if (OrgProfileValue.class.isInstance(obj)) {
                                    OrgProfileValue orgProfileValue = (OrgProfileValue) obj;
                                    List<ValueTranslatabePair> values = orgProfileValue
                                            .getValuesForOrgReport();
                                    fillValues(siteId, locale, reportHelper,
                                            values);
                                } else {
                                    reportHelper
                                            .getValues()
                                            .add(Arrays
                                                    .asList(new String[]{(obj == null) ? ""
                                                            : obj.toString()}));
                                }
                            }
                        }
                    }
                    record.getHelpers().add(reportHelper);
                }
            }
        }
        return mapping.findForward("showReport");
    }

    public void fillValues(Long siteId, String locale,
            OrgProfileReportHelper reportHelper,
            List<ValueTranslatabePair> values) throws WorkerException {
        if (values != null) {
            for (ValueTranslatabePair value : values) {
                if (value.isTranslatable() && value.getValues() != null) {
                    List<String> translated = new ArrayList<String>();
                    for (String val : value.getValues()) {
                        translated.add(TranslatorWorker.translateText(val,
                                locale, siteId));

                    }
                    reportHelper.getValues().add(translated);

                } else {
                    reportHelper.getValues().add(value.getValues());
                }
            }
        }
    }
    //AMP-16507
    public ActionForward typeChange(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        OrgProfileReportForm orgProfileForm = (OrgProfileReportForm) form;
        //
        if(orgProfileForm.getActionFlag()!=null && orgProfileForm.getActionFlag().equals("orgType")){
            //We filter groups for the selected donnor type
            if(orgProfileForm.getDonorType()!=-1){
            orgProfileForm.setGroups(DbUtil.searchForOrganisationGroupByType(orgProfileForm.getDonorType()));
            //we filter organizatios for the selected groupttype
            orgProfileForm.setOrganizations(DbUtil.getAmpOrganisations(null, null, orgProfileForm.getDonorType()));
            }else{
                //if it was -1 everything gets seleted
                orgProfileForm.setGroups(DbUtil.getAllOrganisationGroup());
                orgProfileForm.setOrganizations(DbUtil.getAmpOrganisations());
            }
            
        }
        if(orgProfileForm.getActionFlag()!=null && orgProfileForm.getActionFlag().equals("orgGroup")){
            if(orgProfileForm.getOrgGroup()!=-1){
            //we filter organization for the selected group
                orgProfileForm.setOrganizations(DbUtil.getAmpOrganisations(null, orgProfileForm.getOrgGroup(), null));
            }else{
                //if it was -1 then we get all organizations
                orgProfileForm.setOrganizations(DbUtil.getAmpOrganisations());
            }
        }
        
        return mapping.findForward("showReport");
    }
}
