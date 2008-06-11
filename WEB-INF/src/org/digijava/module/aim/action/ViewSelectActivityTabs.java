/*
 * ViewChannelOverview.java
 */

package org.digijava.module.aim.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
public class ViewSelectActivityTabs extends Action {

	private static Logger logger = Logger.getLogger(ViewSelectActivityTabs.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws java.lang.Exception
	{
		HttpSession session = request.getSession();
		
		for (Enumeration enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
			System.out.println("ViewSelectActivityTabs.execute() "+enumeration.nextElement());
		}
				
		return mapping.findForward("channelOverview");
	}
}
