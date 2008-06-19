package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class GetDesktopLinks extends TilesAction {

	public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

	
		TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
		if (tm != null) {
			Collection links = TeamMemberUtil.getMemberLinks(tm.getMemberId());
			session.setAttribute(Constants.MY_LINKS, links);

			Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);
			javax.jcr.Node userNode = DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, tm);
			javax.jcr.Node teamNode = DocumentManagerUtil.getTeamNode(jcrWriteSession, tm);

			java.util.List<DocumentData> userDocData = DocumentManagerUtil.getDocuments(userNode.getNodes(), request, true, false);
			java.util.List<DocumentData> teamDocData = DocumentManagerUtil.getDocuments(teamNode.getNodes(), request, true, false);
			userDocData.addAll(teamDocData);

			Collections.sort(userDocData);
			ArrayList<DocumentData> returList=new ArrayList<DocumentData>();
			int i=0;
			for (DocumentData documentData : userDocData) {
				if (i==5) break;
					returList.add(documentData);
				i++;
			}
			
			
			
			session.setAttribute(Constants.MY_LINKS, links);
			session.setAttribute(Constants.MY_DOCUMENTS, returList);
		}

		return null;
	}
}
