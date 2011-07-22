/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.engine.DEImportBuilder;
import org.digijava.module.dataExchange.engine.FileSourceBuilder;
import org.digijava.module.dataExchange.form.ManageSourceForm;
import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.util.SourceSettingDAO;
import org.digijava.module.dataExchange.util.XmlCreator;
import org.springframework.util.FileCopyUtils;

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
			return null;
		}
		if ( "execute".equals( msForm.getAction() ) ) {
			request.setAttribute("htmlView","true");
			modeExecuteSource(mapping, msForm, request, response);
			return mapping.findForward("showSources");
		}
		
	//	modeExecuteSource(mapping, msForm, request, response);
		
		return modeShowSourceList(mapping, msForm, request, response);
	}
	
	public ActionForward modeShowSourceList(ActionMapping mapping, ManageSourceForm msForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		response.setCharacterEncoding("UTF-16");
		response.setContentType("text/xml");
		PrintStream ps						= new PrintStream( response.getOutputStream(), false, "UTF-16" );
		List<DESourceSetting> sources		= new SessionSourceSettingDAO().getAllAmpSourceSettingsObjects();
		XmlCreator xmlCreator	= new XmlCreator(sources);
		ps.print(xmlCreator.createXml());
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
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			FileCopyUtils.copy(msForm.getXmlFile().getInputStream(), outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = outputStream.toString();
		DESourceSetting ss	= new SessionSourceSettingDAO().getSourceSettingById( msForm.getExecutingSourceId() );
//		DESourceSetting ss	= new SessionSourceSettingDAO().getSourceSettingById( new Long(1));
		if(ss.getLogs() == null)
			ss.setLogs(new ArrayList<DELogPerExecution>());
		
		FileSourceBuilder fsb	= new FileSourceBuilder(ss, result);
		DEImportItem 	deItem  = new DEImportItem(fsb);
		DEImportBuilder deib 	= new DEImportBuilder(deItem);
		deib.run(request);
//		deib.runIATI(request);
	}

}
