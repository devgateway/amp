package org.digijava.module.admin.action;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.admin.helper.FieldInfo;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.form.DataImporterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
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
        fieldsInfo = getEntityFieldsInfo(AmpActivityFields.class);

        request.setAttribute("fieldsInfo",fieldsInfo);
        if (request.getParameter("Upload")!=null) {
            DataImporterForm dataImporterForm = (DataImporterForm)form;
            logger.info(" this is the action "+request.getParameter("Upload"));

            InputStream fileInputStream = dataImporterForm.getUploadedFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet
            List<List<String>> excelData = new ArrayList<>();

            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();
                List<String> rowData = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    rowData.add(cell.getStringCellValue()); // Assuming all cells contain string values
                }
                excelData.add(rowData);
            }
            workbook.close();
            logger.info("Excel Data" + excelData);
            logger.info("Selected Pairs" + selectedPairs);


        }
        return mapping.findForward("importData");
    }
    private List<String> getEntityFieldsInfo(Class<?> entityClass) {
        List<String> fieldsInfos = new ArrayList<>();
//        Field[] fields = entityClass.getDeclaredFields();
//        for (Field field : fields) {
//            // Extract field information and group them by subclass
//            Class<?> declaringClass = field.getDeclaringClass();
//            String subclass = declaringClass.getSimpleName();
//            String fieldName = field.getName();
//            String fieldType = field.getType().getSimpleName();
//            fieldsInfos.add(new FieldInfo(subclass, fieldName, fieldType));
//        }
        fieldsInfos.add("{projectTitle}");
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
