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
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.DataImporterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

public class DataImporter extends Action {
    Logger logger = LoggerFactory.getLogger(DataImporter.class);
    private static final long serialVersionUID = 1L;
    private List<String> fieldsInfo; // List of field information
    private String uploadedFileName;
    private String localDirectory = "/src/main/resources/uploads/";

    private Map<String, String> selectedPairs;




    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fieldsInfo = getEntityFieldsInfo();
        request.setAttribute("fieldsInfo",fieldsInfo);
        DataImporterForm dataImporterForm = (DataImporterForm)form;

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
            response.getWriter().write(json);
        }

        if (request.getParameter("Upload")!=null) {
            logger.info(" this is the action "+request.getParameter("Upload"));

            InputStream fileInputStream = dataImporterForm.getUploadedFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            parseData(dataImporterForm.getColumnPairs(),workbook);
            workbook.close();


        }
        return mapping.findForward("importData");
    }
    private void parseData(Map<String,String> config, Workbook workbook)
    {
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            AmpActivityVersion ampActivityVersion = new AmpActivityVersion();
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

                }

            }
            logger.info("Activity here: "+ampActivityVersion);
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
        return -1; // Column not found
    }
    private List<String> getEntityFieldsInfo() {
        List<String> fieldsInfos = new ArrayList<>();
        fieldsInfos.add("{projectName}");
        fieldsInfos.add("{projectDescription}");
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
