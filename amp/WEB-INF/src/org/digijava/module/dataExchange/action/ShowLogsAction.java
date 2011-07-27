/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.PrintStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.form.ShowLogsForm;
import org.digijava.module.dataExchange.util.ImportLogDAO;
import org.digijava.module.dataExchange.util.SessionImportLogDAO;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.XmlCreator;

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
		List<DELogPerExecution> logs		= null;
		if ( myForm.getSelectedSourceId() == null || myForm.getSelectedSourceId() <= 0 )
			logs	= new SessionImportLogDAO().getAllAmpLogPerExecutionObjects();
		else {
			logs	= new SessionImportLogDAO().getAmpLogPerExectutionObjsBySourceSetting(myForm.getSelectedSourceId());
		}
		myForm.setLogs(logs);
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
	
	public ActionForward modeShowItemLogs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
		ShowLogsForm myForm					= (ShowLogsForm) form;
		List<DELogPerItem> logItems		= null;
		if ( myForm.getSelectedLogPerExecId() == null || myForm.getSelectedLogPerExecId() <= 0 )
			logItems	= new SessionImportLogDAO().getAllAmpLogPerItem();
		else {
			logItems	= new SessionImportLogDAO().getAmpLogPerItemObjsByExec( myForm.getSelectedLogPerExecId() );
			myForm.setSelectedLogPerExecId(null);
		}
		myForm.setLogItems(logItems);
		/**
		 * old code -- to be deleted !
		 *
		 * 
		 * response.setCharacterEncoding("UTF-16");
		response.setContentType("text/xml");
		PrintStream ps						= new PrintStream( response.getOutputStream(), false, "UTF-16" );
		
		List<DELogPerItem> logItems		= null;
		if ( myForm.getSelectedLogPerExecId() == null || myForm.getSelectedLogPerExecId() <= 0 )
			logItems	= new SessionImportLogDAO().getAllAmpLogPerItem();
		else {
			logItems	= new SessionImportLogDAO().getAmpLogPerItemObjsByExec( myForm.getSelectedLogPerExecId() );
			myForm.setSelectedLogPerExecId(null);
		}
		XmlCreator xmlCreator	= new XmlCreator(logItems);
		ps.print(xmlCreator.createXml());
		 */
		
	 	
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

}
