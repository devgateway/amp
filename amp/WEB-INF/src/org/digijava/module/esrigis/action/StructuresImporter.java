package org.digijava.module.esrigis.action;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.esrigis.form.StructuresImporterForm;
import org.digijava.module.esrigis.helpers.DbHelper;

import java.util.Collections;
import java.util.HashSet;

import au.com.bytecode.opencsv.CSVReader;

public class StructuresImporter extends Action {

    public static Logger logger = Logger.getLogger(StructuresImporter.class);
    @SuppressWarnings("unchecked")
    public static final List<String> CSV_CONTENT_TYPES  = Collections.unmodifiableList(
                                Arrays.asList("text/plain", "text/csv", "application/vnd.ms-excel")
                            );
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        StructuresImporterForm sform  = (StructuresImporterForm) form;
        
        if (request.getParameter("importPerform") != null && sform.getUploadedFile()!=null && sform.getUploadedFile().getFileSize()>0) {
            String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
            String locale = RequestUtils.getNavigationLanguage(request).getCode();
            ActionMessages errors = new ActionMessages();
            
            if(!("text/plain".equalsIgnoreCase(sform.getUploadedFile().getContentType()) || ("application/vnd.ms-excel".equalsIgnoreCase(sform.getUploadedFile().getContentType())) ||
                    ("text/csv".equalsIgnoreCase(sform.getUploadedFile().getContentType())))){
                
                errors.add(
                        ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage(
                                "error.aim.structureImporter.wrongContentType",
                                TranslatorWorker
                                        .translateText("The file to import must be an text/csv file.")));
                saveErrors(request, errors);
            }else{
                try{
                    InputStreamReader isr = new InputStreamReader(sform.getUploadedFile().getInputStream());
                    CSVReader reader = new CSVReader(isr);
                    String [] nextLine;
                    Boolean firstLine = true;
                    ArrayList<String> errors2 = new ArrayList<String>();
                    while ((nextLine = reader.readNext()) != null) {
                        if(firstLine){
                            firstLine = false;
                        }else if(nextLine.length>1){
                            sform.setErrors(errors2);
                            String ampId = nextLine[0].trim();
                            AmpStructure st = new AmpStructure();
                            st.setTitle(nextLine[1]);
                            st.setLatitude(nextLine[2]);
                            st.setLongitude(nextLine[3]);
                            st.setType(DbHelper.getStructureTypesByName(nextLine[4].trim()));
                            st.setActivity(DbHelper.getActivityByAmpId(ampId));
                            st.setDescription(nextLine[5].trim());
                            st.setCreationdate(new Timestamp(System.currentTimeMillis()));
                            if (StringUtils.isNotBlank(st.getTitle()) && st.getType() != null
                                    && st.getActivity() != null) {
                                DbHelper.saveStructure(st);
                            } else {
                                String errorline = ArrayUtils.toString(nextLine).replace("{", "");
                                errorline = errorline.replace("}","");
                                if (st.getType()==null) {
                                    errorline += " (Reason: Wrong structure type)";
                                }
                                sform.getErrors().add(ArrayUtils.toString(errorline));
                            }
                            
                        }
                    }
                    
                    errors.add(
                            ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage(
                                    "aim.structureImporter.success",
                                    TranslatorWorker
                                            .translateText(
                                                    "Structures import done successfully.")));
                    //saved as error just to be shown in red
                    saveErrors(request, errors);
                    
                    
                }catch(Exception e){
                    e.printStackTrace();
                    errors.add(
                            ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage(
                                    "error.aim.structureImporter.error",
                                    TranslatorWorker
                                            .translateText(
                                                    "An error occurred while processing the file.")));
                    saveErrors(request, errors);
                }
                
            }
        }
        return mapping.findForward("forward");
        
    }
    
    public boolean isCsvFile(String fileContentType) {
        if ( fileContentType != null && fileContentType.length() > 0 
                && CSV_CONTENT_TYPES.contains(fileContentType) ) {
            return true;
        }
        return false;
    }
    
}
