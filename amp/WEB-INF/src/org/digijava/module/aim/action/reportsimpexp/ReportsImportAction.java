package org.digijava.module.aim.action.reportsimpexp;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.ar.impexp.impl.ReportsImpTransformerMain;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.reportsimpexp.ImpExpForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.util.reportsimpexp.ReportsImpExpConstants;

public class ReportsImportAction extends MultiAction {

	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ImpExpForm myForm					= (ImpExpForm) form;
		String action		= request.getParameter(ReportsImpExpConstants.ACTION);
		if ( ReportsImpExpConstants.ACTION_NEW.equals(action) ) {
			Collection<AmpTeam> teams		= TeamUtil.getAllTeamsRequestSession();
			Set<KeyValue> allTeams			= new TreeSet<KeyValue>( KeyValue.valueComparator );
			if ( teams != null ) {
				for (AmpTeam team : teams) {
					AmpTeamMember tLead	= TeamMemberUtil.getTeamHead(team.getAmpTeamId());
					if ( tLead  != null ) {
						allTeams.add( new KeyValue(tLead.getAmpTeamMemId()+"", "   " +  team.getName()) );
					}
				}
			}
			myForm.setAllAvailableTeams(allTeams);
			myForm.setImportClass( ReportsImpExpConstants.CSS_CLASS_SELECTED );
			myForm.setIncludedJsp("import.jsp");
			
		}
		return modeSelect(mapping, form, request, response);
	}

	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String action		= request.getParameter(ReportsImpExpConstants.ACTION);
		if ( ReportsImpExpConstants.ACTION_IMPORT_FILE.equals(action) ) {
			return modeProcessImportFile(mapping, form, request, response);
		}
		else if ( ReportsImpExpConstants.ACTION_IMPORT.equals(action) )
			return modeProcessImport(mapping, form, request, response);
		return modeShow(mapping, form, request, response);
	}
	
	public ActionForward modeShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("forward");
		
	}
	public ActionForward modeProcessImportFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ImpExpForm myForm					= (ImpExpForm) form;
		FormFile formFile							= null;
		
		if ( myForm.getShowTabs() )
			formFile		= myForm.getFormFileTabs();
		else 
			formFile		= myForm.getFormFileReports();
		
		if ( formFile != null ) {
				ReportsImpTransformerMain mainTransformer 	= new ReportsImpTransformerMain();
				mainTransformer.unmarshall( formFile.getInputStream() );
				myForm.setReportsList( mainTransformer.transform() ); 
		}
		return modeShow(mapping, form, request, response);
	}
	public ActionForward modeProcessImport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionErrors errors						= new ActionErrors();
		ImpExpForm myForm					= (ImpExpForm) form;
		
		if ( myForm.getImportReportIndexes() != null && myForm.getImportTeamIds() != null ) {
			for ( int i=0; i<myForm.getImportReportIndexes().length; i++ ) {
				AmpReports currentReport			= myForm.getReportsList().get( myForm.getImportReportIndexes()[i] );
				String reportName						= currentReport.getName();
				for ( int j=0; j<myForm.getImportTeamIds().length; j++ ) {
					AmpTeamMember tLead		= TeamMemberUtil.getAmpTeamMember( myForm.getImportTeamIds()[j] );
					AmpTeam team						= tLead.getAmpTeam();
					
					currentReport.setName(reportName);
					currentReport.setOwnerId(tLead);
					
					if ( AdvancedReportUtil.checkDuplicateReportName(currentReport.getName(), tLead.getAmpTeamMemId(), null) ) {
						 Site site = RequestUtils.getSite(request);
						 //
						 //requirements for translation purposes
						 TranslatorWorker translator = TranslatorWorker.getInstance();
						 Long siteId = site.getId();
						 String locale = RequestUtils.getNavigationLanguage(request).getCode();
						 String translatedText = "imported";
						 try{
							 translatedText = TranslatorWorker.translateText("imported", locale, siteId);
						 } catch (WorkerException e) {
							e.printStackTrace();
						 }
						 currentReport.setName( currentReport.getName() + " (" + translatedText + ")" ) ;
					}
					
					AdvancedReportUtil.saveReport(currentReport, team.getAmpTeamId(), tLead.getAmpTeamMemId(), true);
					ReportWizardAction.duplicateReportData(currentReport);
				}
			}
			ActionError error					= new ActionError("error.aim.reportsimpexp.importDone");
			errors.add( "title", error );
		}
		else {
			ActionError error					= new ActionError("error.aim.reportsimpexp.errorDuringImport");
			errors.add( "title", error );
		}
	
		this.saveErrors(request, errors);
		myForm.setReportsList(null);
		return modeShow(mapping, form, request, response);
	}
	

}
