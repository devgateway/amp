package org.digijava.module.aim.action.reportsimpexp;


import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.ar.impexp.impl.ReportsExpTransformerMain;
import org.digijava.module.aim.ar.util.ReportsAccess;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.reportsimpexp.ImpExpForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.reportsimpexp.ReportsImpExpConstants;

public class ReportsExportAction extends MultiAction {
	
	private static Logger logger 		= Logger.getLogger(ReportsExportAction.class);
	
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		ImpExpForm myForm					= (ImpExpForm) form;
		String action		= request.getParameter(ReportsImpExpConstants.ACTION);
		if ( ReportsImpExpConstants.ACTION_NEW.equals(action) ) {
			myForm.setIncludedJsp("export.jsp");
			myForm.setSelectedTeamIds(null);
			myForm.setSelectedUserIds(null);
		}
		return modeSelect(mapping, form, request, response);
	}
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String action		= request.getParameter(ReportsImpExpConstants.ACTION);
		
		if ( ReportsImpExpConstants.ACTION_NEW.equals(action) || ReportsImpExpConstants.ACTION_SELECTION_STEP.equals(action)  ) 
			return modeShow(mapping, form, request, response);
		else if ( ReportsImpExpConstants.ACTION_ADD_STEP.equals(action) ) 
			return modeProcess(mapping, form, request, response);
		else if ( ReportsImpExpConstants.ACTION_EXPORT.equals(action) ) 
			return modeExport(mapping, form, request, response);
		return null;
	}
	
	public ActionForward modeShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String action		= request.getParameter(ReportsImpExpConstants.ACTION);
		
		ImpExpForm myForm					= (ImpExpForm) form;
		List<Long> teamIds					= null;
		List<Long> userIds					= null;
		
		if ( myForm.getSelectedTeamIds() != null  && myForm.getSelectedTeamIds().length > 0 )
			teamIds									= Arrays.asList( myForm.getSelectedTeamIds() );
		
		if ( myForm.getSelectedUserIds() != null && myForm.getSelectedUserIds().length > 0 ) 
			userIds									= Arrays.asList( myForm.getSelectedUserIds() );
		
		
		ReportsAccess reportsAccess		= new ReportsAccess(myForm.getShowTabs(), teamIds, userIds);
		myForm.setReportsList( reportsAccess.retrieveReports() );
		
		if ( myForm.getReportsList() != null ) {
			HashSet<AmpTeam> teams	= new HashSet<AmpTeam>();
			HashSet<User> users				= new HashSet<User>();
			
			for ( AmpReports r: myForm.getReportsList() ) {
				teams.add( r.getOwnerId().getAmpTeam() );
				users.add( r.getOwnerId().getUser() );
			}
			
			myForm.setDisplayedReportIds( new Long[myForm.getReportsList().size()] ) ;
			for (int i=0; i<myForm.getReportsList().size(); i++) {
				myForm.getDisplayedReportIds()[i]	= myForm.getReportsList().get(i).getId();
			}
			
			if ( ReportsImpExpConstants.ACTION_NEW.equals(action) ) {
				TreeSet<KeyValue> teamsKV	= new TreeSet<KeyValue>( KeyValue.valueComparator );
				for ( AmpTeam t: teams ) {
					teamsKV.add( new KeyValue(t.getAmpTeamId()+"", t.getName()) );
				}
				myForm.setAvailableTeams(teamsKV);
				
				TreeSet<KeyValue> usersKV		= new TreeSet<KeyValue>( KeyValue.valueComparator );
				for ( User u: users ) {
					usersKV.add( new KeyValue(u.getId()+"", u.getName()) );
				}
				myForm.setAvailableUsers(usersKV);
			}
			
			
			if ( myForm.getSelectedReports() != null )
				myForm.setSelectedReportIds( myForm.getSelectedReports().keySet().toArray(new Long [myForm.getSelectedReports().size()]) );
			
			if ( myForm.getShowTabs() ) {
				myForm.setExportReportsClass(ReportsImpExpConstants.CSS_CLASS_ENABLED);
				myForm.setExportTabsClass(ReportsImpExpConstants.CSS_CLASS_SELECTED);
				
				myForm.setExportReportsAction(ReportsImpExpConstants.ACTION_NEW);
				myForm.setExportTabsAction(ReportsImpExpConstants.ACTION_SELECTION_STEP);
				
			}
			else {
				myForm.setExportReportsClass(ReportsImpExpConstants.CSS_CLASS_SELECTED);
				myForm.setExportTabsClass(ReportsImpExpConstants.CSS_CLASS_ENABLED);
				
				myForm.setExportReportsAction(ReportsImpExpConstants.ACTION_SELECTION_STEP);
				myForm.setExportTabsAction(ReportsImpExpConstants.ACTION_NEW);
				
			} 
			
		}
		
		
		return mapping.findForward("forward");
	}
	
	public ActionForward modeProcess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ImpExpForm myForm					= (ImpExpForm) form;
		if ( myForm.getSelectedReportIds() != null && myForm.getSelectedReportIds().length > 0 ) {
			if ( myForm.getSelectedReports()  == null )
				myForm.setSelectedReports( new HashMap<Long, AmpReports>() );
			
			if ( myForm.getDisplayedReportIds() != null ) {
				for ( int i=0; i<myForm.getDisplayedReportIds().length; i++ ) {
					myForm.getSelectedReports().remove( myForm.getDisplayedReportIds()[i] );
				}
			}
			if ( myForm.getSelectedReportIds() != null )
				for ( int i=0; i<myForm.getSelectedReportIds().length; i++ ) {
					AmpReports r		= 
						ReportsExportAction.getReportWithIdFromCollection(myForm.getSelectedReportIds()[i], myForm.getReportsList() );
					if ( r != null)
						myForm.getSelectedReports().put(r.getAmpReportId(), r);
				}
		}
		TreeSet<String> reportNames		= new TreeSet<String>();
		if ( myForm.getSelectedReports() != null && myForm.getSelectedReports().size()>0 ) {
			for ( AmpReports r: myForm.getSelectedReports().values() ) {
				reportNames.add( r.getName() );
			}
		}
		myForm.setSelectedReportsString( Util.collectionToString(reportNames) );
		return modeShow(mapping, form, request, response);
	}
	
	public ActionForward modeExport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ImpExpForm myForm					= (ImpExpForm) form;
		String filename								= "reportsExport.xml";
		if ( myForm.getShowTabs() )
			filename									= "tabsExport.xml";
		
		response.setContentType("text/xml");
		response.setHeader("content-disposition", "attachment; filename=" + filename);
		if ( myForm.getSelectedReports() != null && myForm.getSelectedReports().size() > 0 ) {
			ReportsExpTransformerMain mainTransformer	= new ReportsExpTransformerMain();
			mainTransformer.transform( myForm.getSelectedReports().keySet() );
			mainTransformer.marshall( response.getOutputStream() );
		}
		return null;
	}
	
	private static AmpReports getReportWithIdFromCollection(Long id, Collection<AmpReports> coll) {
		Iterator<AmpReports> iter			= coll.iterator();
		while ( iter.hasNext() ) {
			AmpReports r	= iter.next();
			if ( r.getAmpReportId().equals(id) ) 
				return r;
		}
		return null;
	}
}
