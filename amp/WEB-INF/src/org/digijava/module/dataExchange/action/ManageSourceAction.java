/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.engine.DEImportBuilder;
import org.digijava.module.dataExchange.engine.FileSourceBuilder;
import org.digijava.module.dataExchange.form.ManageSourceForm;
import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.SourceSettingDAO;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.springframework.util.FileCopyUtils;

import com.lowagie.text.pdf.codec.Base64.InputStream;

/**
 * @author Alex Gartner
 *
 */
public class ManageSourceAction extends MultiAction {

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManageSourceForm msForm	= (ManageSourceForm) form;
		String htmlView	= request.getParameter("htmlView");
		if ("true".equals(htmlView)) {
			return mapping.findForward("forward");
		}
		if ( "showDetails".equals(msForm.getAction()) && 
				msForm.getSelectedSourceId() != null && msForm.getSelectedSourceId() > 0 ) 
			return modeShowSourceDetails(mapping, msForm, request, response);
		if ( msForm.getExecutingSourceId() != null ) {
			
		}
		if ( "delete".equals( msForm.getAction() ) && msForm.getSelectedSourceId() != null) {
			new SessionSourceSettingDAO().deleteObject(msForm.getSelectedSourceId() );
			//return null;
		}
		if ( "execute".equals( msForm.getAction() ) ) {
			//request.setAttribute("htmlView","true");
			modeExecuteSource(mapping, msForm, request, response, "idml");
			return mapping.findForward("showSources");
		}
		
		if ( "executeIATI".equals( msForm.getAction() ) ) {
			//request.setAttribute("htmlView","true");
			modeExecuteSource(mapping, msForm, request, response,"iati");
			//return mapping.findForward("forward");
		}
		
	//	modeExecuteSource(mapping, msForm, request, response);
		return modeShowSourceList(mapping, msForm, request, response);
	}
	
	public ActionForward modeShowSourceList(ActionMapping mapping, ManageSourceForm msForm,	HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		int allSourcesAmount = new SessionSourceSettingDAO().getAllAmpSourceSettingsObjectsCount();
		int lastPage = 1;
		if (allSourcesAmount > Constants.RECORDS_AMOUNT_PER_PAGE) {
			lastPage = allSourcesAmount% Constants.RECORDS_AMOUNT_PER_PAGE==0 ? allSourcesAmount / Constants.RECORDS_AMOUNT_PER_PAGE : allSourcesAmount / Constants.RECORDS_AMOUNT_PER_PAGE +1;
		}
		
		
		int startIndex = 0;
		if (msForm.getPage() != 0 ) {
			startIndex = Constants.RECORDS_AMOUNT_PER_PAGE *( msForm.getPage()-1);
		}
		
		//get sources
		List<DESourceSetting> sources		= new SessionSourceSettingDAO().getPagedAmpSourceSettingsObjects(startIndex, msForm.getSort());
		msForm.setPagedSources(sources);
		
		if(msForm.getPage() == 0){
			msForm.setCurrentPage(1);
		}else{
			msForm.setCurrentPage(msForm.getPage());
		}
		
		msForm.setLastPage(lastPage);
		return mapping.findForward("showSources");
		
	}
	public ActionForward modeShowSourceListOld(ActionMapping mapping, ManageSourceForm msForm,	HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//sorting options
		String sortBy = request.getParameter("sort");
		String sortDir = request.getParameter("dir");
		//pagination options
		String startIndex = request.getParameter("startIndex");
		String results = request.getParameter("results");
		int allSourcesAmount = new SessionSourceSettingDAO().getAllAmpSourceSettingsObjectsCount();
		//get sources
		List<DESourceSetting> sources		= new SessionSourceSettingDAO().getPagedAmpSourceSettingsObjects(new Integer(startIndex).intValue(), sortBy);

        JSONObject json = null;
        try {
		    json = new JSONObject();
        } catch (Exception ex) {
            System.out.print("a");
        }
		JSONArray jsonArray = new JSONArray();
		
		//fill array
		if(sources!=null && sources.size() > 0){
			for (Iterator<DESourceSetting> it = sources.iterator(); it.hasNext();) {
				DESourceSetting source = (DESourceSetting) it.next();
				JSONObject jsource = new JSONObject();
				jsource.put("ID", source.getId());				
				jsource.put("Name", source.getName());
				jsource.put("Source", source.getSource());
				jsource.put("Workspace", source.getImportWorkspace().getName());				
				jsonArray.add(jsource);
			}			
		}
    	
		
		if (sources!=null) {
			json.put("recordsReturned", sources.size());
		}else{
			json.put("recordsReturned", 0);
		}
		
		json.put("totalRecords", allSourcesAmount);
		json.put("startIndex", startIndex);
		json.put("sort", null);
		json.put("dir", "asc");
		json.put("pageSize", 10);
		json.put("rowsPerPage", 10);

		json.put("SourceSetting", jsonArray);

		response.setContentType("text/json-comment-filtered");
		OutputStreamWriter outputStream = null;

		try {
			outputStream = new OutputStreamWriter(response.getOutputStream(),"UTF-8");
			outputStream.write(json.toString());
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}		
		
//		response.setCharacterEncoding("UTF-16");
//		response.setContentType("text/xml");
//		PrintStream ps						= new PrintStream( response.getOutputStream(), false, "UTF-16" );
//		List<DESourceSetting> sources		= new SessionSourceSettingDAO().getAllAmpSourceSettingsObjects();
//		XmlCreator xmlCreator	= new XmlCreator(sources);
//		ps.print(xmlCreator.createXml());
		return null;
	}
	
	public ActionForward modeShowSourceDetails(ActionMapping mapping, ManageSourceForm msForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		SourceSettingDAO dao	= new SessionSourceSettingDAO();
		DESourceSetting ss		= dao.getSourceSettingById( msForm.getSelectedSourceId() );
		
		msForm.setName( ss.getName() );
		msForm.setSource( ss.getSource() );
		msForm.setStrategy( ss.getImportStrategy() );
		msForm.setApprovalStatus(ss.getApprovalStatus() );
		msForm.setImportWorkspaceName( ss.getImportWorkspace().getName() );
		msForm.setLanguage( ss.getLanguageId() );
		msForm.setFields( new ArrayList<String>() );
		msForm.getFields().addAll( ss.getFields() );
		msForm.setDbId( ss.getId() );
		msForm.setUniqueIdentifier( ss.getUniqueIdentifier() );
		
		return mapping.findForward("showSourceDetails");
	}
	
	public void modeExecuteSource(ActionMapping mapping, ManageSourceForm msForm,
			HttpServletRequest request, HttpServletResponse response, String type)
			throws Exception {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			Sdm attachedFile = new SessionSourceSettingDAO().getSourceSettingById( msForm.getExecutingSourceId()).getAttachedFile();
			SdmItem item = null;
			if (attachedFile!=null) {
				for (SdmItem sdmItem : (Set<SdmItem>)attachedFile.getItems()) {
					item = sdmItem;
					break;
				}
				ByteArrayInputStream inStream = new ByteArrayInputStream(item.getContent());
				FileCopyUtils.copy(inStream, outputStream);
			}	
			
			//FileCopyUtils.copy(msForm.getXmlFile().getInputStream(), outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = outputStream.toString();
		DESourceSetting ss	= new SessionSourceSettingDAO().getSourceSettingById( msForm.getExecutingSourceId() );
		if(ss.getLogs() == null)
			ss.setLogs(new ArrayList<DELogPerExecution>());
		
		FileSourceBuilder fsb	= new FileSourceBuilder(ss, result);
		DEImportItem 	deItem  = new DEImportItem(fsb);
		DEImportBuilder deib 	= new DEImportBuilder(deItem);
//		deib.run(request);
		try {
			if("iati".compareTo(type) ==0)
				deib.runIATI(request,"check",null); 
			if("idml".compareTo(type) ==0)
				deib.run(request);
		} catch (Exception e) {
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTemplate", TranslatorWorker.translateText("Execution file doesn't exist or is corrupted.",request)));				
			saveErrors(request, errors);
			// TODO: handle exception
		}	

	}

}
