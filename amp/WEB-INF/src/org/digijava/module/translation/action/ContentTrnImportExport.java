package org.digijava.module.translation.action;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedPropertyDescription;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.translation.entity.ContentTrnClassFieldPair;
import org.digijava.module.translation.form.ContentTrnImportExportForm;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.ContentTrnObjectType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 9/23/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class ContentTrnImportExport extends DispatchAction {
    private static Logger logger    = Logger.getLogger(ContentTrnImportExport.class);
    private static String[] PERMAMENT_HEADER_ITEMS = {"Object Class", "Object ID", "Field Name"};


    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws DgException {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request, HttpServletResponse response) throws DgException {

        ContentTrnImportExportForm contentTrnImportExportForm = (ContentTrnImportExportForm) form;

        //List<AmpContentTranslation> allTrns = ContentTranslationUtil.getAllContentTranslations();




        contentTrnImportExportForm.setExistingTrnTypes(ContentTranslationUtil.getContentTranslationUniques());

        return mapping.findForward("forward");
    }

    public ActionForward showExport(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request, HttpServletResponse response) throws DgException {

        ContentTrnImportExportForm contentTrnImportExportForm = (ContentTrnImportExportForm) form;

        //List<AmpContentTranslation> allTrns = ContentTranslationUtil.getAllContentTranslations();



        Collection<ContentTrnObjectType> types = ContentTranslationUtil.getContentTranslationUniques();
        String[] preselectedTypes = new String[types.size()];
        int idx = 0;
        for (ContentTrnObjectType type : types) {
            preselectedTypes[idx] = type.getObjectType();
            idx++;
        }
        contentTrnImportExportForm.setSelectedContentTypes(preselectedTypes);
        contentTrnImportExportForm.setExistingTrnTypes(types);

        return mapping.findForward("forwardExport");
    }

    public ActionForward export(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response) throws DgException {

        ContentTrnImportExportForm contentTrnImportExportForm = (ContentTrnImportExportForm) form;
        List <String>selTypes = Arrays.asList(contentTrnImportExportForm.getSelectedContentTypes());
        List<AmpContentTranslation> allTrns = ContentTranslationUtil.getContentTranslationsByTypes(selTypes);


        Collection<ContentTrnObjectType> types = ContentTranslationUtil.getContentTranslationUniques();
        Set<String>langs = new HashSet<String>();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Content Translations");

        //Header
        HSSFRow row = sheet.createRow(0);
        for (ContentTrnObjectType type : types) {
            if (selTypes.contains(type.getObjectType())) {
                for (String lang : type.getLangs()) {
                    langs.add(lang);
                }
            }
        }

        for (int hIdx = 0; hIdx < PERMAMENT_HEADER_ITEMS.length; hIdx ++) {
            HSSFCell hCell = row.createCell(hIdx);
            hCell.setCellValue(PERMAMENT_HEADER_ITEMS[hIdx]);
        }

        Iterator <String> it = langs.iterator();
        int hLangIdx = 0;
        while (it.hasNext()) {
            HSSFCell hCell = row.createCell(hLangIdx + PERMAMENT_HEADER_ITEMS.length);
            hCell.setCellValue(it.next());
            hLangIdx ++;
        }
        //Content
        Map <Long, Set<AmpContentTranslation>> objectIdGrouper = new HashMap<Long, Set<AmpContentTranslation>>();
        for (AmpContentTranslation trn : allTrns) {
            if (!objectIdGrouper.containsKey(trn.getObjectId())) {
                objectIdGrouper.put(trn.getObjectId(), new HashSet<AmpContentTranslation>());
            }
            ((Set<AmpContentTranslation>)objectIdGrouper.get(trn.getObjectId())).add(trn);
        }

        Set <Long> objIdsSet = objectIdGrouper.keySet();
        List<Long> objIds = new ArrayList(objIdsSet);
        Collections.sort(objIds);

        for (Long key : objIds) {
            Set<AmpContentTranslation> objTranslationData = objectIdGrouper.get(key);
            //Group fields and langs
            Map<ContentTrnClassFieldPair, Map<String, AmpContentTranslation>> groupByField =
                    new HashMap<ContentTrnClassFieldPair, Map<String, AmpContentTranslation>>();
            for (AmpContentTranslation obj : objTranslationData) {
                ContentTrnClassFieldPair checkKey =
                        new ContentTrnClassFieldPair(obj.getObjectClass(), obj.getFieldName());
                if (!groupByField.containsKey(checkKey)) {
                    groupByField.put(checkKey, new HashMap<String, AmpContentTranslation>());
                }
                groupByField.get(checkKey).put(obj.getLocale(), obj);
            }

            //Write appropreate rows/columns
            Set <ContentTrnClassFieldPair> groupKeys = groupByField.keySet();

            for (ContentTrnClassFieldPair groupKey : groupKeys) {
                HSSFRow contentRow = sheet.createRow(sheet.getLastRowNum() + 1);
                Map<String, AmpContentTranslation> obj = groupByField.get(groupKey);
                AmpContentTranslation commonObj = obj.get(obj.keySet().iterator().next());
                contentRow.createCell(0).setCellValue(groupKey.getObjClass());
                contentRow.createCell(1).setCellValue(commonObj.getObjectId());
                contentRow.createCell(2).setCellValue(groupKey.getObjField());
                int counter = 0;
                for (String lang : langs) {

                    AmpContentTranslation forLang = obj.get(lang);
                    String trn = null;
                    if (forLang != null) {
                        trn = forLang.getTranslation();
                    } else {
                        trn = "";
                    }
                    contentRow.createCell(counter + PERMAMENT_HEADER_ITEMS.length).setCellValue(trn);
                    counter++;
                }
            }
        }

        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "inline; filename=contentTranslations.xls");

        try {

            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //return view(mapping, form, request, response);
        return null;
    }

    public ActionForward showImport (ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws DgException {

        ContentTrnImportExportForm contentTrnImportExportForm = (ContentTrnImportExportForm) form;

        return mapping.findForward("forwardImport");
    }

    public ActionForward importing (ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws DgException {

        ContentTrnImportExportForm contentTrnImportExportForm = (ContentTrnImportExportForm) form;
        FormFile ff = contentTrnImportExportForm.getUpFile();
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(ff.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        HSSFSheet sheet = wb.getSheetAt(0);
        int RowCount = sheet.getLastRowNum();


        List<String> headerCols = new ArrayList<String>();

        HSSFRow headerRow = sheet.getRow(0);
        for (int cellIdx = headerRow.getFirstCellNum(); cellIdx < headerRow.getLastCellNum(); cellIdx ++) {
            headerCols.add(headerRow.getCell(cellIdx).getStringCellValue());
        }

        Set <AmpContentTranslation> objectsForDb = new HashSet<AmpContentTranslation>();
        List<AmpContentTranslation> allTrns = ContentTranslationUtil.getContentTranslationsByTypes();
        Set<String> allowedLocales = new HashSet<String>(TranslatorUtil.getLanguages());
        for (int rowIdx = 1; rowIdx < RowCount; rowIdx ++) {
            HSSFRow contentRow = sheet.getRow(rowIdx);
            for (int trnColumnIdx = PERMAMENT_HEADER_ITEMS.length; trnColumnIdx < contentRow.getLastCellNum(); trnColumnIdx ++) {
                String trn = contentRow.getCell(trnColumnIdx).getStringCellValue().trim();
                if (!trn.isEmpty()) {
                    String objectClass = contentRow.getCell(0).getStringCellValue();
                    Long objectId = new Double(contentRow.getCell(1).getNumericCellValue()).longValue();
                    String fieldName = contentRow.getCell(2).getStringCellValue();
                    String locale = headerCols.get(trnColumnIdx);

                    AmpContentTranslation trnItem = ContentTranslationUtil.getByTypeObjidFieldLocale(allTrns, objectClass, objectId, fieldName, locale);
                    locale = locale.trim();
                    if (!allowedLocales.contains(locale))
                        throw new RuntimeException("disallowed locale: " + locale);
                    //If new item
                    if (trnItem == null) {
                        if( isValidEntry(rowIdx+1, objectClass, fieldName, objectId, locale, trn) ) {
                        trnItem = new AmpContentTranslation();
                        trnItem.setObjectClass(objectClass);
                        trnItem.setObjectId(objectId);
                        trnItem.setFieldName(fieldName);
                        trnItem.setLocale(locale);
                        trnItem.setTranslation(trn);
                        objectsForDb.add(trnItem);
                        }
                    } else if (!trnItem.getTranslation().equals(trn)){ //If needs to be updated
                        trnItem.setTranslation(trn);
                        objectsForDb.add(trnItem);
                    }
                }
            }
        }

        ContentTranslationUtil.saveOrUpdateContentTrns(objectsForDb);



        return view(mapping, form, request, response);
    }
    
    private boolean isValidEntry(int rowIdx, String objectClass, String fieldName, Long objectId, String locale, String trn) throws DgException{
        String strErr = null; 
        boolean isCritical = false;
        try {
            Class<?> clazz = Class.forName(objectClass);
            InternationalizedPropertyDescription property = InternationalizedModelDescription.getForProperty(clazz, fieldName);
            if( property == null ){
                strErr = String.format("Cause: Field \"%s\" not found in \"%s\" class.",  fieldName, objectClass);
                isCritical = true;
            }else if (DbUtil.getObject(clazz, objectId) == null ){
                strErr = String.format("Cause: No record found for \"%s\" class with id=\"%d\"", objectClass, objectId);
            }else if (!property.field.isAnnotationPresent(TranslatableField.class)) {
                strErr = String.format("Cause: the field \"%s\" from class \"%s\" is not translatable", fieldName, objectClass);
            }// else "No content was predefined to translate, but it seems that we should allow new fields (without any content, even in "en") to be defined in the import
        } catch(ClassNotFoundException ex) {
            strErr = String.format("Cause: Class \"%s\" not found.", objectClass);
            isCritical = true;
            logger.debug(ex.getMessage());
        }
        if( strErr!=null ) {
            strErr = String.format("Ignoring invalid entry at row %d: (%s, %s, %d, %s, %s). %s", rowIdx, objectClass, fieldName, objectId, locale, trn, strErr);
            logger.warn(strErr);
            if (isCritical) 
                throw new DgException(strErr);
        }
        return strErr==null;
    }
}
