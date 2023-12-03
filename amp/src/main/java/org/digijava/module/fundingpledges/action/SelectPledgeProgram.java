package org.digijava.module.fundingpledges.action;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.fundingpledges.action.constants.PledgeActionsConstants;
import org.digijava.module.fundingpledges.form.DocumentShim;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.fundingpledges.form.TransientDocumentShim;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * misnamed centralized place for the AJAX callback of the PledgeForm (did not want to pollute the application with dozens of Actions which have 2-3 lines of usable code each)
 * activities are discriminated using the parameter "extraAction"
 * @author simple
 *
 */
public class SelectPledgeProgram extends Action {

    private static Logger logger = Logger.getLogger(SelectPledgeProgram.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception 
    {
        // entry point for sectors and programs AJAX
        PledgeForm pledgeForm = (PledgeForm) form;
        
        String extraAction = request.getParameter("extraAction");
        if (extraAction != null)
        {
            // {id: 'program_id_select', action: 'rootThemeChanged', attr: 'rootThemeId'},
            if (extraAction.equals("pledge_program_rootThemeChanged"))
            {
                pledgeForm.setSelectedRootProgram(Long.parseLong(request.getParameter("rootThemeId")));
                return null;
            }
                        
            if (extraAction.equals("pledge_program_submit"))
            {
                String[] ids = request.getParameter("selected_program").split(",");
                for(String id: ids){
                    Long lid = Long.parseLong(id);
                    pledgeForm.addSelectedProgram(lid);
                }
                ARUtil.writeResponse(response, "ok");
                return null;
            }
            
            if (extraAction.equals("pledge_program_delete")){
                pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedProgs(), Long.parseLong(request.getParameter("id")));
                return null;
            }
        
            if (extraAction.equals("pledge_sector_rootSectorChanged"))
            {
                pledgeForm.setSelectedRootSector(Long.parseLong(request.getParameter("rootSectorId")));
                return null;
            }
            
            if (extraAction.equals("pledge_sector_submit"))
            {
                String[] ids = request.getParameter("selected_sector").split(",");
                for(String id: ids) {
                    Long sid = Long.parseLong(id);
                    pledgeForm.addSelectedSector(sid);
                }
                ARUtil.writeResponse(response, "ok");
                return null;
            }

            if (extraAction.equals("pledge_sector_divide_percentage")) {
                pledgeForm.dividePercentageSector();
                ARUtil.writeResponse(response, "ok");
                return null;
            }
            
            if (extraAction.equals("pledge_sector_delete")){
                pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedSectors(), Long.parseLong(request.getParameter("id")));
                return null;
            }

            if (extraAction.equals("pledge_funding_submit")){
                pledgeForm.addNewPledgeFundingEntry();
                ARUtil.writeResponse(response, "ok");
                return null;
            }
            if (extraAction.equals("pledge_funding_delete")){
                pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedFunding(), Long.parseLong(request.getParameter("id")));
                return null;
            }
            
            if (extraAction.equals("pledge_document_delete")){
                pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedDocs(), Long.parseLong(request.getParameter("id")));
                return null;
            }

            if (extraAction.equals(PledgeActionsConstants.PLEDGE_DOCUMENT_DELETE_ALL_NON_SUBMITTED)) {
                Optional.ofNullable(Arrays.asList(request.getParameter("idsToDelete").
                        split("\\s*,\\s*"))).orElse(Collections.emptyList()).stream().forEach(documentId -> {
                    pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedDocs(),
                            Long.parseLong(documentId));
                });
                return null;
            }

            if (extraAction.equals("file_upload")) {
                return maintainFileUpload(pledgeForm, request, response);
            }
            
            if (extraAction.equals("pledge_document_submit")){
                // DocumentShim entry has already been added as part of the file upload process 
                ARUtil.writeResponse(response, "ok");
                return null;
            }
            
            if (extraAction.equals("cancel")){
                AddPledge.markPledgeEditorClosed(request.getSession());
                ARUtil.writeResponse(response, "ok");
                return null;
            }

            // {id: 'program_item_select', action: 'themeSelected', attr: 'themeId'} - ignored
            // {id: 'sector_item_select', action: 'sectorSelected', attr: 'sectorId'} - ignored
        }
        return mapping.findForward("forward");
    }
    
    protected ActionForward maintainFileUpload(PledgeForm pledgeForm, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws IOException{
        // shamelessly copypasted from https://github.com/klaalo/jQuery-File-Upload-Java/blob/master/src/info/sudr/file/UploadServlet.java
        
        PrintWriter writer = response.getWriter();
        response.setContentType(decideContentType(request)); //"application/json"
        //boolean escape = response.getContentType().equals("text/")
        JSONArray json = new JSONArray();
        try {
            FormFile file = pledgeForm.getFiles();
            TemporaryDocumentData tdd = new TemporaryDocumentData();
            tdd.setTitle("");
            tdd.setName(file.getFileName());
            tdd.setDescription("");
            tdd.setNotes("");
            tdd.setFileSize((double)file.getFileSize() / 1024 / 1024);
            tdd.setFormFile(file);
            ActionMessages errors = new ActionMessages();
            //NodeWrapper wrapper = tdd.saveToRepository(request, errors);
            DocumentShim docShim = new TransientDocumentShim(tdd);
            pledgeForm.addNewDocument(docShim);

            JSONObject jsono = new JSONObject();
            jsono.put("name", docShim.getFileName());
            jsono.put("size", docShim.getFileSizeInBytes());
            jsono.put("url", "dummy?" + docShim.getUniqueId());
//                  jsono.put("thumbnail_url", "upload?getthumb=" + item.getName());
//                  jsono.put("delete_url", "upload?delfile=" + item.getName());
//                  jsono.put("delete_type", "GET");
            json.add(jsono);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            writer.write(json.toString());
            writer.close();
        }
        return null;
    }
    
    /**
     * partial copy-paste off AbstractFileUploadResource.java
     * @param request
     */
    protected String decideContentType(javax.servlet.http.HttpServletRequest request){
        String accept = request.getHeader("Accept");
        if (wantsHtml(accept)){
            // Internet Explorer
            return "text/plain";
        }
        else{
            // a real browser
            return "application/json";
        }
    }

    protected boolean wantsHtml(String acceptHeader)
    {       
        return acceptHeader != null && acceptHeader.contains("text/html");
    }
}

