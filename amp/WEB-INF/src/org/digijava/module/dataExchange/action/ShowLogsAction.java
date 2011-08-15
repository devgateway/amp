/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.engine.DEImportBuilder;
import org.digijava.module.dataExchange.engine.FileSourceBuilder;
import org.digijava.module.dataExchange.form.ShowLogsForm;
import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.digijava.module.dataExchange.util.ImportLogDAO;
import org.digijava.module.dataExchange.util.SessionImportLogDAO;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.XmlCreator;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.springframework.util.FileCopyUtils;
/**
 * @author Alex Gartner
 *
 */
public class ShowLogsAction extends MultiAction {

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
		ShowLogsForm myForm					= (ShowLogsForm) form;
		if(request.getParameter("reset")!=null && request.getParameter("reset").equals("true")){
			resetForm(myForm);
		}
		// TODO Auto-generated method stub
		String htmlView	= request.getParameter("htmlView");
		if ("true".equals(htmlView)) {
			String sourceIdStr		= request.getParameter("selectedSourceId");
			Long sourceId	= null;
			if ( sourceIdStr != null ) {
				try {
					sourceId	= Long.parseLong(sourceIdStr);
					myForm.setSelectedSourceId(sourceId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//return mapping.findForward("forward"); commented by me
		}
		if ( myForm.getSelectedLogPerExecId() != null )
			return modeShowItemLogs(mapping, myForm, request, response);
		if ( myForm.getSelectedLogPerItemId() != null )
			return modeShowItemLogDetails(mapping, myForm, request, response);
		
		return modeShowActionLogs(mapping, form, request, response);
	}
	
	public ActionForward modeShowActionLogs(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ShowLogsForm myForm					= (ShowLogsForm) form;
		
		String actType	= request.getParameter("actionType");
		String itemId	= request.getParameter("itemId");
		//import one activity
		if(actType!=null )
			if("saveAct".compareTo(actType)==0){
				importActivity(request, myForm, itemId);
			}
			else
				if("saveAllAct".compareTo(actType)==0){
					{
						String[] selectedActivities = myForm.getSelectedActivities();
						//Object o = request.getParameter("idss");
						if(selectedActivities != null) 
							for (int i = 0; i < selectedActivities.length; i++) {
								importActivity(request, myForm, selectedActivities[i]);
							}
					}
			
		}
		
		List<DELogPerExecution> logs		= null;
		int lastPage = 1;
		if ( myForm.getSelectedSourceId() == null || myForm.getSelectedSourceId() <= 0 )
			logs	= new SessionImportLogDAO().getAllAmpLogPerExecutionObjects();
		else {
			int allSourcesAmount = new SessionImportLogDAO().getAmpLogPerExectutionObjsCountBySourceSetting(myForm.getSelectedSourceId());
			
			if (allSourcesAmount > Constants.RECORDS_AMOUNT_PER_PAGE) {
				lastPage = allSourcesAmount % Constants.RECORDS_AMOUNT_PER_PAGE==0 ? allSourcesAmount / Constants.RECORDS_AMOUNT_PER_PAGE : allSourcesAmount / Constants.RECORDS_AMOUNT_PER_PAGE +1;
			}
			
			int startIndex = 0;
			if (myForm.getPage() != 0) {
				startIndex = Constants.RECORDS_AMOUNT_PER_PAGE * (myForm.getPage() -1 );
			}
			logs	= new SessionImportLogDAO().getAmpLogPerExectutionObjsBySourceSetting(myForm.getSelectedSourceId(),startIndex, myForm.getSortBy());
		}
		myForm.setLogs(logs);
		
		if(myForm.getPage() == 0){
			myForm.setCurrentPage(1);
		}else{
			myForm.setCurrentPage(myForm.getPage());
		}
		
		myForm.setLastPage(lastPage);
		DESourceSetting ss	= new SessionSourceSettingDAO().getSourceSettingById( myForm.getSelectedSourceId());
		if (ss !=null) {
			myForm.setSelectedSourceName(ss.getName());
		}
		List<DESourceSetting> sources		= new SessionSourceSettingDAO().getAllAmpSourceSettingsObjects();
		myForm.setAvailableSourceSettings(sources);
		return mapping.findForward("forward");  
		
		/**
		 * old method body- commented by dare
		 * 
		 * 
		 * // TODO Auto-generated method stub
		
		ShowLogsForm myForm					= (ShowLogsForm) form;
		response.setCharacterEncoding("UTF-16");
		response.setContentType("text/xml");
		PrintStream ps						= new PrintStream( response.getOutputStream(), false, "UTF-16" );
		
		List<DELogPerExecution> logs		= null;
		if ( myForm.getSelectedSourceId() == null || myForm.getSelectedSourceId() <= 0 )
			logs	= new SessionImportLogDAO().getAllAmpLogPerExecutionObjects();
		else {
			logs	= new SessionImportLogDAO().getAmpLogPerExectutionObjsBySourceSetting(myForm.getSelectedSourceId() );
			//myForm.setSelectedSourceId(null);
		}
		XmlCreator xmlCreator	= new XmlCreator(logs);
		ps.print(xmlCreator.createXml());
		//ps.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><ResultSet xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:yahoo:lcl\" totalResultsAvailable=\"2\" totalResultsReturned=\"2\" firstResultPosition=\"1\"><Result><Name>Gigi</Name><Age>22</Age><Test>TRALALA</Test></Result><Result><Name>Lolo</Name><Age>32</Age><Test>DIDIDADADA</Test></Result></ResultSet>");
	 	
		return null;
//		return mapping.findForward("feed");
		 * 
		 */
		
	}

	private void importActivity(HttpServletRequest request, ShowLogsForm myForm, String itemId) throws SQLException, DgException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			Sdm attachedFile = new SessionSourceSettingDAO().getSourceSettingById(myForm.getSelectedSourceId()).getAttachedFile();
			SdmItem item = null;
			if (attachedFile!=null) {
				for (SdmItem sdmItem : (Set<SdmItem>)attachedFile.getItems()) {
					item = sdmItem;
					break;
				}
				ByteArrayInputStream inStream = new ByteArrayInputStream(item.getContent());
				FileCopyUtils.copy(inStream, outputStream);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = outputStream.toString();
		DESourceSetting ss	= new SessionSourceSettingDAO().getSourceSettingById( myForm.getSelectedSourceId() );
		if(ss.getLogs() == null)
			ss.setLogs(new ArrayList<DELogPerExecution>());
		
		FileSourceBuilder fsb	= new FileSourceBuilder(ss, result);
		DEImportItem 	deItem  = new DEImportItem(fsb);
		DEImportBuilder deib 	= new DEImportBuilder(deItem);
		if(itemId != null)
			{
				deib.runIATI(request,"import",itemId);
			}
	}
	
	public ActionForward modeShowItemLogs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
		ShowLogsForm myForm					= (ShowLogsForm) form;
		List<DELogPerItem> logItems		= null;
		Long selectedLogPerExecId = null;
		if ( myForm.getSelectedLogPerExecId() == null || myForm.getSelectedLogPerExecId() <= 0 )
			logItems	= new SessionImportLogDAO().getAllAmpLogPerItem();
		else {
			logItems	= new SessionImportLogDAO().getAmpLogPerItemObjsByExec( myForm.getSelectedLogPerExecId() );
			selectedLogPerExecId = myForm.getSelectedLogPerExecId();
			myForm.setSelectedLogPerExecId(null);
		}
		myForm.setLogItems(logItems);
		
		//set can import
		Boolean canImport = canImportActivities(myForm,selectedLogPerExecId);
	 	myForm.setCanImport(canImport);
		return mapping.findForward("viewExecutionLog");
	}
	public ActionForward modeShowItemLogDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ShowLogsForm myForm			= (ShowLogsForm) form;
		ImportLogDAO	dao			= new SessionImportLogDAO();
		DELogPerItem selLPI			= (DELogPerItem)dao.loadObject(DELogPerItem.class, myForm.getSelectedLogPerItemId() );
		myForm.setLpi(selLPI);
		myForm.setSelectedLogPerItemId(null);
		return mapping.findForward("showLogItemDetails");
	}

	private Boolean canImportActivities(ShowLogsForm myForm, Long selectedLogPerExecId)
			throws SQLException, DgException {
		DELogPerExecution logAux = (DELogPerExecution) new SessionImportLogDAO().loadObject(DELogPerExecution.class,selectedLogPerExecId);
		if("Import activities".compareTo(logAux.getDescription()) == 0) return false;
		else
			if("Check feed source".compareTo(logAux.getDescription()) == 0) {
				List<DELogPerExecution> logs = new SessionImportLogDAO().getAmpLogPerExectutionObjsBySourceSetting(myForm.getSelectedSourceId(),0, "date_desc");
				for (Iterator<DELogPerExecution> iterator = logs.iterator(); iterator.hasNext();) {
					DELogPerExecution deLogPerExecution = (DELogPerExecution) iterator.next();
					if(selectedLogPerExecId.compareTo(deLogPerExecution.getId()) == 0)
						return true;
					return false;
				}
			}
		return false;
	}

	private void resetForm(ShowLogsForm myForm){
		myForm.setAvailableSourceSettings(null);
		myForm.setCurrentPage(null);
		myForm.setLastPage(1);
		myForm.setLogItems(null);
		myForm.setLogs(null);
		myForm.setPage(1);
		myForm.setSelectedLogPerExecId(null);
		myForm.setSelectedLogPerItemId(null);
		myForm.setSortBy(null);
	}
}
