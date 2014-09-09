package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.WorkspaceForm;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.search.util.SearchUtil;

public class WorkspaceManager extends Action {

	private static Logger logger = Logger.getLogger(WorkspaceManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
		
		int NUM_RECORDS = 10000;
		Collection<AmpTeam> workspaces = new ArrayList<AmpTeam>();
		WorkspaceForm wsForm = (WorkspaceForm) form;


		if((request.getParameter("reset")!=null && request.getParameter("reset").equalsIgnoreCase("true"))){
			wsForm.setKeyword(null);
			wsForm.setWorkspaceType("all");
			wsForm.setNumPerPage(-1);
		}
                if(session.getAttribute("fromPage")!=null){
                    wsForm.setCurrentPage((Integer)session.getAttribute("fromPage"));
                    session.removeAttribute("fromPage");
                }
                if(session.getAttribute("selectedRow")!=null){
                    wsForm.setCurrentRow((Integer)session.getAttribute("selectedRow"));
                    session.removeAttribute("selectedRow");
                }
                if(session.getAttribute("selectedWs")!=null){
                    wsForm.setSelectedWs((Long)session.getAttribute("selectedWs"));
                    session.removeAttribute("selectedWs");
                }
                
		String keyword = wsForm.getKeyword();
		Collection<String> keywords=new ArrayList<String>();
		StringTokenizer st = new StringTokenizer("");
		if(keyword!=null)
		 st = new StringTokenizer(keyword);
	     while (st.hasMoreTokens()) {
	         keywords.add(st.nextToken().toLowerCase());
	     }

		String temp = request.getParameter("page"); 
		if (temp != null) {
			wsForm.setPage(Integer.parseInt(temp));
		} else if (wsForm.getPage() <= 0) {
			wsForm.setPage(1);
		}
		
		Collection<AmpTeam> ampWorkspaces = TeamUtil.getAllTeams();
		
		//all teams need to be refreshed after any change, AMP-17917
		//if (ampWorkspaces == null || reloadWorkspaces)

		{
//			ampWorkspaces = TeamUtil.getAllTeams();
			session.setAttribute("ampWorkspaces", ampWorkspaces);
		}

		if(wsForm.getNumPerPage()!=-1) {
			NUM_RECORDS =wsForm.getNumPerPage();
		}else {
			NUM_RECORDS =ampWorkspaces.size();
		}

		/*
		 * check whether the numPages is less than the page . if yes return
		 * error.
		 */

		int currPage = wsForm.getPage();
		int stIndex = ((currPage - 1) * NUM_RECORDS);
		int edIndex = stIndex + NUM_RECORDS;

		Collection<AmpTeam> colAt=new ArrayList<AmpTeam>();
		for (AmpTeam at : ampWorkspaces) {
			at.setChildrenWorkspaces(TeamUtil.getAllChildrenWorkspaces(at.getAmpTeamId()));
			colAt.add(at);
		}
		Vector<AmpTeam> vect = new Vector<AmpTeam>();
		//vect.addAll(ampWorkspaces);
		vect.addAll(colAt);
		String workspaceType = wsForm.getWorkspaceType();
		Long workspaceGroup = wsForm.getWorkspaceGroup();
		for (AmpTeam ampTeam : vect) {
			Boolean addTeam = false;
			if(workspaceType!=null){
			  if("all".equals(workspaceType)) {
				  addTeam = true;
			  }else if("team".equals(workspaceType) && "Team".equals(ampTeam.getAccessType())) {
				  addTeam = true;
			  }else if("management".equals(workspaceType) && "Management".equals(ampTeam.getAccessType())) {
				  addTeam = true;
			  }else if("computed".equals(workspaceType) && ampTeam.getComputation()!=null && ampTeam.getComputation()) {
				  addTeam = true;
			  }
			}
			if (workspaceGroup != null && workspaceGroup != 0 && addTeam) {
				if(ampTeam.getWorkspaceGroup() != null && (ampTeam.getWorkspaceGroup().getId().compareTo(workspaceGroup) == 0))
				{
				  addTeam = true;
				}
				else
				{
				  addTeam = false;
				}
			}
			if(addTeam) workspaces.add(ampTeam);
		}
		
		//pages
		int numPages = 0;
		if (NUM_RECORDS!=0) {numPages=workspaces.size() / NUM_RECORDS;
		numPages += (workspaces.size() % NUM_RECORDS != 0) ? 1 : 0;
		}
		//workspaces for current page
		if(edIndex>workspaces.size()){
			edIndex=workspaces.size();
		}
		workspaces=((List)workspaces).subList(stIndex, edIndex);
		
		Collection<Integer> pages = null;
		if (numPages > 1) {
			pages = new ArrayList<Integer>();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}
		Collection<AmpTeam> workspacesFiltered=new ArrayList<AmpTeam>();
		if(!workspaces.isEmpty())
		{
			logger.error("indirect llk debugging: workspaces not empty");
			for (Iterator<AmpTeam> it = workspaces.iterator(); it.hasNext();) {
				AmpTeam team = (AmpTeam) it.next();
				logger.error("indirect llk debugging: team is " + team.toString());
				boolean found=false;
				for (Iterator jt = keywords.iterator(); jt.hasNext();) {
					String keyw = (String) jt.next();
					logger.error("indirect llk debugging:----------keyword is: " + keyw);
					/*test comment*/
					/*            Locale currentLocale = RequestUtils.getNavigationLanguage(request);*/
					java.util.Locale currentLocale = new java.util.Locale(TLSUtils.getEffectiveLangCode());
					if ((team.getDescription() != null && (SearchUtil.stringContainsKeyword(team.getDescription(), keyw, currentLocale))) 
							|| (team.getName() != null) && SearchUtil.stringContainsKeyword(team.getName(), keyw, currentLocale))
						{
							logger.error("indirect llk debugging:---------- FOUND! " + team.toString());
							found = true;
							break;
						}
					
					
					if( (team.getDescription()!=null && team.getDescription().toLowerCase().contains(keyw)) 
							|| (team.getName() !=null && team.getName().toLowerCase().contains(keyw)) ) {
						logger.error("indirect llk debugging:---------- FOUND! " + team.toString());
						found=true;break;
					}
				}
				if(found) workspacesFiltered.add(team);
				logger.error("FOUND, therefore, adding team:");
			}
		}
		if(workspacesFiltered.isEmpty() && keywords.isEmpty())
		  wsForm.setWorkspaces(workspaces);
		else wsForm.setWorkspaces(workspacesFiltered);
		wsForm.setPages(pages);

		return mapping.findForward("forward");
	}
}
