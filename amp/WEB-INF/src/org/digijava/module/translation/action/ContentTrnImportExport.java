package org.digijava.module.translation.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.translation.entity.ContentTrnClassFieldPair;
import org.digijava.module.translation.form.ContentTrnImportExportForm;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.ContentTrnObjectType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 9/23/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class ContentTrnImportExport extends DispatchAction {
    private static Logger logger	= Logger.getLogger(ContentTrnImportExport.class);
    private static int PERMAMENT_COLUMN_COUNT = 3;


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

        //Header
        for (ContentTrnObjectType type : types) {
            if (selTypes.contains(type.getObjectType())) {
                for (String lang : type.getLangs()) {
                    langs.add(lang);
                }
            }
        }
        StringBuilder csvContent = new StringBuilder("Object Class, Object ID, Field Name, ");
        Iterator <String> it = langs.iterator();
        while (it.hasNext()) {
            csvContent.append(it.next());
            if (it.hasNext()) {
                csvContent.append(",");
            }
        }
        csvContent.append("\n");

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
            //for (Map<String, AmpContentTranslation> obj : groupByField.values()) {
            for (ContentTrnClassFieldPair groupKey : groupKeys) {
                Map<String, AmpContentTranslation> obj = groupByField.get(groupKey);
                AmpContentTranslation commonObj = obj.get(obj.keySet().iterator().next());
                csvContent.append(groupKey.getObjClass()).append(",");
                csvContent.append(commonObj.getObjectId()).append(",");
                csvContent.append(groupKey.getObjField()).append(",");
                int counter = 0;
                for (String lang : langs) {
                    counter++;
                    AmpContentTranslation forLang = obj.get(lang);
                    if (forLang != null) {
                        csvContent.append("\"").append(forLang.getTranslation().replaceAll("\n", " ")).append("\"");
                    } else {
                        csvContent.append("\"\"");
                    }
                    if (langs.size() > counter) csvContent.append(",");
                }
                csvContent.append("\n");
            }
        }


        response.setContentType("application/download; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "inline; filename=contentTranslations.csv ");

        try {
            String csvStr = csvContent.toString();
            ServletOutputStream out = response.getOutputStream();
            Writer wrt = new OutputStreamWriter(out, "UTF-8");
            wrt.write(csvStr);
            wrt.close();
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

        String csvContent = null;
        try {
            csvContent = new String(ff.getFileData(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        StringTokenizer rowTokenizer = new StringTokenizer(csvContent, "\n");

        List<String> rows = new ArrayList<String>();
        while (rowTokenizer.hasMoreElements()) {
            rows.add(rowTokenizer.nextToken());
        }

        List<String> headerCols = new ArrayList<String>();
        if (!rows.isEmpty()) {
            StringTokenizer headerColsTokenizer = new StringTokenizer(rows.get(0), ",");
            while (headerColsTokenizer.hasMoreElements()) {
                headerCols.add(headerColsTokenizer.nextToken().trim());
            }
        }

        boolean headerSkipped = false;
        Set <AmpContentTranslation> objectsForDb = new HashSet<AmpContentTranslation>();
        List<AmpContentTranslation> allTrns = ContentTranslationUtil.getContentTranslationsByTypes();

        for (String row : rows) {
            if (headerSkipped) {
                String[] rowData = row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                for (int trnColumnIdx = PERMAMENT_COLUMN_COUNT; trnColumnIdx < rowData.length; trnColumnIdx ++) {
                    String trn = rowData[trnColumnIdx].trim();
                    String trnNoQuotes = trn.substring(1, trn.length() - 1);

                    if (!trnNoQuotes.trim().isEmpty()) {

                        String objectClass = rowData[0];
                        Long objectId = Long.parseLong(rowData[1]);
                        String fieldName = rowData[2];
                        String locale = headerCols.get(trnColumnIdx);

                        AmpContentTranslation trnItem = ContentTranslationUtil.getByTypeObjidFieldLocale(allTrns,
                                objectClass, objectId, fieldName, locale);

                        //If new item
                        if (trnItem == null) {
                            trnItem = new AmpContentTranslation();
                            trnItem.setObjectClass(objectClass);
                            trnItem.setObjectId(objectId);
                            trnItem.setFieldName(fieldName);
                            trnItem.setLocale(locale);
                            trnItem.setTranslation(trnNoQuotes);
                            objectsForDb.add(trnItem);
                        } else if (!trnItem.getTranslation().equals(trnNoQuotes)){ //If needs to be updated
                            trnItem.setTranslation(trnNoQuotes);
                            objectsForDb.add(trnItem);
                        }
                    }
                }
            }
            headerSkipped = true;
        }

        ContentTranslationUtil.saveOrUpdateContentTrns(objectsForDb);



        return view(mapping, form, request, response);
    }

}
