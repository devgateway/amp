package org.digijava.module.fundingpledges.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Location;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.form.DocumentShim;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.digijava.module.fundingpledges.form.TransientDocumentShim;

/**
 * misnamed centralized place for the AJAX callback of the PledgeForm (did not want to pollute the application with dozens of Action's which have 2-3 lines of usable code each)
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
				pledgeForm.deleteUniquelyIdentifiable(pledgeForm.getSelectedDocsList(), Long.parseLong(request.getParameter("id")));
				return null;
			}
			
//			if (extraAction.equals("pledge_document_refresh_add")){
//				return null;
//			}
			
			if (extraAction.equals("file_upload")){
				return maintainFileUpload(pledgeForm, request, response);
			}
			
			if (extraAction.equals("pledge_document_submit")){
				// DocumentShim entry has already been added as part of the file upload process 
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
		response.setContentType("application/json");
		JSONArray json = new JSONArray();
		try {
			FormFile file = pledgeForm.getFiles();
			TemporaryDocumentData tdd = new TemporaryDocumentData();
			tdd.setTitle("");
			tdd.setName(file.getFileName());
			tdd.setDescription("");
			tdd.setNotes("");
			tdd.setFileSize(file.getFileSize() / 1024 / 1024);
			tdd.setFormFile(file);
			ActionMessages errors = new ActionMessages();
			//NodeWrapper wrapper = tdd.saveToRepository(request, errors);
			DocumentShim docShim = new TransientDocumentShim(tdd);
			pledgeForm.addNewDocument(docShim);

			JSONObject jsono = new JSONObject();
			jsono.put("name", docShim.getFileName());
			jsono.put("size", docShim.getFileSizeInBytes());
			jsono.put("url", "dummy?" + docShim.getUniqueId());
//					jsono.put("thumbnail_url", "upload?getthumb=" + item.getName());
//					jsono.put("delete_url", "upload?delfile=" + item.getName());
//					jsono.put("delete_type", "GET");
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
}

