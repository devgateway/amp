package org.digijava.module.admin.action;

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
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.form.DataImporterForm;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
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
            headers.append("<select id=\"columnName\">");
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
            logger.info(" this is the action "+request.getParameter("Upload"));

            InputStream fileInputStream = dataImporterForm.getUploadedFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                parseData(dataImporterForm.getColumnPairs(),workbook,i);
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


    private void parseData(Map<String,String> config, Workbook workbook, int sheetNumber)
    {
        Session session = PersistenceManager.getRequestDBSession();

        Sheet sheet = workbook.getSheetAt(sheetNumber);
        for (Row row : sheet) {
            AmpActivityVersion ampActivityVersion = new AmpActivityVersion();
            ampActivityVersion.setApprovalStatus(ApprovalStatus.valueOf("created"));
            if (row.getRowNum() == 0) {
                continue;
            }

            for (Map.Entry<String,String> entry : config.entrySet())
            {
                Cell cell = row.getCell(getColumnIndexByName(sheet, entry.getKey()));
                switch (entry.getValue())
                {
                    case "{projectName}":
                        ampActivityVersion.setName(cell.getStringCellValue());
                        break;
                    case "{projectDescription}":
                        ampActivityVersion.setDescription(cell.getStringCellValue());
                        break;
                    case "{projectLocation}":
                        ampActivityVersion.addLocation(new AmpActivityLocation());
                        break;
                    case "{primarySector}":
                        updateSectors(ampActivityVersion, cell.getStringCellValue(), session);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + entry.getValue());
                }

            }
            logger.info("Activity here: "+ampActivityVersion);
            session.save(ampActivityVersion);

        }

    }

    private void updateFunding(AmpActivityVersion ampActivityVersion,String donorName,Session session)
    {

    }

    private void updateSectors(AmpActivityVersion ampActivityVersion, String name, Session session)
    {
        String hql = "SELECT "+ AmpSector.class.getName() +" s WHERE s.name LIKE %:name%";
        Query query= session.createQuery(hql);
        query.setString("name", name);
        AmpSector sector =(AmpSector) query.uniqueResult();
        if (sector!=null) {
            AmpActivitySector ampActivitySector = new AmpActivitySector();
            ampActivitySector.setSectorId(sector);
            ampActivityVersion.getSectors().add(ampActivitySector);
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
