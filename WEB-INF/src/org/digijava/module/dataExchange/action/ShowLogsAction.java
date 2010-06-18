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
import org.digijava.module.dataExchange.form.ShowLogsForm;
import org.digijava.module.dataExchange.util.SessionImportLogDAO;
import org.digijava.module.dataExchange.util.XmlCreator;
import org.digijava.module.dataExchange.util.XmlWrappable;

/**
 * @author Alex Gartner
 *
 */
public class ShowLogsAction extends MultiAction {

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
			return mapping.findForward("forward");
		}
		return modeShowActionLogs(mapping, form, request, response);
	}
	
	public ActionForward modeShowActionLogs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
		ShowLogsForm myForm					= (ShowLogsForm) form;
		
		response.setContentType("text/xml");
		PrintStream ps						= new PrintStream( response.getOutputStream() );
		
		List<DELogPerExecution> logs		= null;
		if ( myForm.getSelectedSourceId() == null || myForm.getSelectedSourceId() <= 0 )
			logs	= new SessionImportLogDAO().getAllAmpLogPerExecutionObjects();
		else
			logs	= new SessionImportLogDAO().getAmpLogPerExectutionObjsBySourceSetting(myForm.getSelectedSourceId() );	
		XmlCreator xmlCreator	= new XmlCreator(logs);
		ps.print(xmlCreator.createXml());
		//ps.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><ResultSet xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:yahoo:lcl\" totalResultsAvailable=\"2\" totalResultsReturned=\"2\" firstResultPosition=\"1\"><Result><Name>Gigi</Name><Age>22</Age><Test>TRALALA</Test></Result><Result><Name>Lolo</Name><Age>32</Age><Test>DIDIDADADA</Test></Result></ResultSet>");
	 	
		return null;
//		return mapping.findForward("feed");
	}

}
