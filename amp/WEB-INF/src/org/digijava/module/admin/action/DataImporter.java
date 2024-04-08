package org.digijava.module.admin.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImportRules;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.admin.util.model.ActivityGroup;
import org.digijava.module.admin.util.model.DonorOrganization;
import org.digijava.module.admin.util.model.ImportDataModel;
import org.digijava.module.admin.util.model.Sector;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.form.DataImporterForm;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataImporter extends Action {
    Logger logger = LoggerFactory.getLogger(DataImporter.class);


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // List of fields
        List<String> fieldsInfo = getEntityFieldsInfo();
        request.setAttribute("fieldsInfo", fieldsInfo);
        DataImporterForm dataImporterForm = (DataImporterForm)form;


        if (request.getParameter("uploadTemplate")!=null) {
            logger.info(" this is the action "+request.getParameter("Upload"));

            InputStream fileInputStream = dataImporterForm.getTemplateFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            StringBuilder headers = new StringBuilder();
            headers.append("  <label for=\"columnName\">Select Column Name:</label>\n<select id=\"columnName\">");
            Iterator<Cell> cellIterator = headerRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                headers.append("<option>").append(cell.getStringCellValue()).append("</option>");
            }
            headers.append("</select>");
            response.setHeader("selectTag",headers.toString());

            logger.info("Headers: "+dataImporterForm.getFileHeaders());

            workbook.close();
            }




            if (request.getParameter("addField")!=null) {
            logger.info(" this is the action "+request.getParameter("addField"));

            String columnName = request.getParameter("columnName");
            String selectedField = request.getParameter("selectedField");
            dataImporterForm.getColumnPairs().put(columnName, selectedField);
            logger.info("Datas:"+dataImporterForm.getColumnPairs());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString( dataImporterForm.getColumnPairs());

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("updatedMap",json);

        }


        if (request.getParameter("removeField")!=null) {
            logger.info(" this is the action "+request.getParameter("removeField"));

            String columnName = request.getParameter("columnName");
            String selectedField = request.getParameter("selectedField");
            dataImporterForm.getColumnPairs().put(columnName, selectedField);
            removeMapItem(dataImporterForm.getColumnPairs(),columnName,selectedField);
            logger.info("Datas:"+dataImporterForm.getColumnPairs());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString( dataImporterForm.getColumnPairs());

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("updatedMap",json);

        }

        if (request.getParameter("Upload")!=null) {
            logger.info(" this is the action Upload "+request.getParameter("Upload"));

            InputStream fileInputStream = dataImporterForm.getUploadedFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                parseData(dataImporterForm.getColumnPairs(),workbook,i, request);
            }

            workbook.close();


        }
        return mapping.findForward("importData");
    }

    private void removeMapItem(Map<String,String> map,String columnName, String selectedField)
    {
        // Check if the entry's key and value match the criteria
        // Remove the entry
        map.entrySet().removeIf(entry -> columnName.equals(entry.getKey()) && selectedField.equals(entry.getValue()));
    }


    private void parseData(Map<String,String> config, Workbook workbook, int sheetNumber, HttpServletRequest request) throws JsonProcessingException {
        Session session = PersistenceManager.getRequestDBSession();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        Sheet sheet = workbook.getSheetAt(sheetNumber);
        for (Row row : sheet) {
            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setModified_by(TeamMemberUtil.getCurrentAmpTeamMember(request).getAmpTeamMemId());
            importDataModel.setIs_draft(true);
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
            importDataModel.setCreation_date(now.format(formatter));
            setStatus(importDataModel, session);

//            ampActivityVersion.setApprovalStatus(ApprovalStatus.CREATED);
            if (row.getRowNum() == 0) {
                continue;
            }
            if (row.getRowNum()<=5) {

                for (Map.Entry<String, String> entry : config.entrySet()) {
                    Cell cell = row.getCell(getColumnIndexByName(sheet, entry.getKey()));
                    switch (entry.getValue()) {
                        case "{projectName}":
                            importDataModel.setProject_title(cell.getStringCellValue());
                            break;
                        case "{projectDescription}":
                            importDataModel.setDescription(cell.getStringCellValue());
                            break;
                        case "{projectLocation}":
//                        ampActivityVersion.addLocation(new AmpActivityLocation());
                            break;
                        case "{primarySector}":
                            updateSectors(importDataModel, cell.getStringCellValue(), session, true);
                            break;
                        case "{secondarySector}":
                            updateSectors(importDataModel, cell.getStringCellValue(), session, false);
                            break;
                        case "{donorAgency}":
                            updateOrgs(importDataModel, cell.getStringCellValue(), session, "donor");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + entry.getValue());
                    }

                }
            importTheData(importDataModel);

            }
//            logger.info("Activity here: "+importDataModel);

        }

    }
    private void setStatus(ImportDataModel importDataModel,Session session)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String statusStr = TranslatorWorker.translateText("Ongoing project");

//        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s WHERE s.category_value LIKE :value";
        String hql = "SELECT s FROM " + AmpCategoryValue.class.getName() + " s " +
                "JOIN " + AmpCategoryClass.class.getName() + " c " +
                "WHERE s.ampCategoryClass.id = c.id " +  // Join condition
                "AND s.categoryValue LIKE :value " +     // Additional conditions
                "AND c.keyname = :categoryKey";          // Additional conditions

        Query query= session.createQuery(hql);
        query.setParameter("value", "%" + statusStr + "%");
        AmpCategoryValue value= (AmpCategoryValue) query.uniqueResult();
        importDataModel.setActivity_status(value.getId());

    }
    private void importTheData(ImportDataModel importDataModel) throws JsonProcessingException {
        logger.info("Trying to import tha data...");
        ActivityImportRules rules = new ActivityImportRules(false, false,
                true);
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = objectMapper
                .convertValue(importDataModel, new TypeReference<Map<String, Object>>() {});
    logger.info("Data map "+map);
        JsonApiResponse<ActivitySummary> response= ActivityInterchangeUtils.importActivity(map, false, rules,  "activity");
        logger.info("Import Response: "+objectMapper.writeValueAsString(response));
    }

    private void updateFunding(AmpActivityVersion ampActivityVersion,String donorName,Session session)
    {

    }

    private void updateSectors(ImportDataModel importDataModel, String name, Session session, boolean primary)
    {
        if (!session.isOpen()) {
            session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT s FROM " + AmpSector.class.getName() + " s WHERE s.name LIKE :name";
        Query query= session.createQuery(hql);
        query.setParameter("name", "%" + name + "%");
        List<AmpSector> sectors =query.list();
        if (sectors!=null && !sectors.isEmpty()) {
           Sector sector1 = new Sector();
            sector1.setSector(sectors.get(0).getAmpSectorId());
            if (primary) {
                importDataModel.getPrimary_sectors().add(sector1);
            }
            else
            {
                importDataModel.getSecondary_sectors().add(sector1);

            }
        }

    }

    private void updateOrgs(ImportDataModel importDataModel, String name, Session session, String type)
    {
        if (!session.isOpen()) {
        session=PersistenceManager.getRequestDBSession();
        }
        String hql = "SELECT o FROM " + AmpOrganisation.class.getName() + " o WHERE o.name LIKE :name";

        Query query= session.createQuery(hql);
        query.setParameter("name", "%" + name + "%");
        List<AmpOrganisation> organisations =query.list();
        if (organisations!=null && !organisations.isEmpty()) {
            if (Objects.equals(type, "donor")) {
                DonorOrganization donorOrganization = new DonorOrganization();
            donorOrganization.setOrganization(organisations.get(0).getAmpOrgId());
            donorOrganization.setPercentage(100.0);
                importDataModel.getDonor_organization().add(donorOrganization);
            }

        }

    }


    private static int getColumnIndexByName(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && columnName.equals(cell.getStringCellValue())) {
                return i;
            }
        }
        return -1;
    }
    private List<String> getEntityFieldsInfo() {
        List<String> fieldsInfos = new ArrayList<>();
        fieldsInfos.add("{projectName}");
        fieldsInfos.add("{projectDescription}");
        fieldsInfos.add("{primarySector}");
        fieldsInfos.add("{secondarySector}");
        fieldsInfos.add("{projectLocation}");
        fieldsInfos.add("{projectStartDate}");
        fieldsInfos.add("{projectEndDate}");
        fieldsInfos.add("{donorAgency}");
        fieldsInfos.add("{executingAgency}");
        fieldsInfos.add("{implementingAgency}");
        fieldsInfos.add("{actualDisbursement}");
        fieldsInfos.add("{actualCommitment}");
        fieldsInfos.add("{plannedDisbursement}");
        fieldsInfos.add("{plannedCommitment}");
        return fieldsInfos;
    }
}
