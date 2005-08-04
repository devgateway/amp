
/*
 * AdvancedReport.java @Author Ronald B Created: 27-July-2005
 */

package org.digijava.module.aim.action;


import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportData;
import org.digijava.kernel.persistence.PersistenceManager;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ReportUtil;


public class AdvancedReport extends Action {

	private static Logger logger = Logger.getLogger(Login.class);
	private String str="";
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		AdvancedReportForm formBean = (AdvancedReportForm) form;

		HttpSession httpSession = request.getSession();
		TeamMember teamMember=(TeamMember)httpSession.getAttribute("currentMember");
		if(teamMember==null)
			return mapping.findForward("index");
		Long ampTeamId=teamMember.getTeamId();
		logger.debug("Team Id: " + ampTeamId);
		String perspective = "DN";
		
		if(formBean.getPerspective() == null)
		{
			perspective = teamMember.getAppSettings().getPerspective();
		}
		else
			perspective = formBean.getPerspectiveFilter();
		if(perspective.equals("Donor"))
			perspective="DN";
		if(perspective.equals("MOFED"))
			perspective="MA";
		formBean.setPerspectiveFilter(perspective);

		Query query;
		Session session;
		String sqlQuery;
		Iterator iter;
		Collection coll = new ArrayList();
		AmpColumns ampColumns;
		
		try
		{
			session = PersistenceManager.getSession();
			// Fills the column that can be selected from AMP_COLUMNS
			if(formBean.getAmpColumns() == null)
				formBean.setAmpColumns(ReportUtil.getColumnList());
			else
				logger.info(" AmpColumns is not NULL........");
			
			// add columns that are available
			if(request.getParameter("check") != null && request.getParameter("check").equals("add"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getAmpColumns(), formBean.getAddedColumns(), formBean.getSelectedColumns(), formBean);
				formBean.setSelectedColumns(null);
			}
			// Remove the columns selected
			if(request.getParameter("check") != null && request.getParameter("check").equals("delete"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getAddedColumns(), formBean.getAmpColumns() , formBean.getRemoveColumns(), formBean);
				formBean.setRemoveColumns(null);
			}
			// Goto Step 2.
			if(request.getParameter("check") != null && request.getParameter("check").equals("5"))
			{
				logger.info("In here  generating data..........");
				if(formBean.getAddedColumns() != null)
				{
					coll = formBean.getAddedColumns();
					if(coll != null)
					{
						formBean.setFinalData( ReportUtil.generateQuery(coll, ampTeamId) );
					}
				}
				if(formBean.getReportTitle() != null){
					logger.info(" Title Value " + formBean.getReportTitle());
				}
				return mapping.findForward("GenerateReport");
			}

			// Step 4 : Report Details
			if(request.getParameter("check") != null && request.getParameter("check").equals("4"))
			{
				logger.info("In here  Getting Report Details..........");
				return mapping.findForward("ReportDetails");
			}
			// step 3 : 
			if(request.getParameter("check") != null && request.getParameter("check").equals("3"))
			{
				logger.info("In here  chart process####..........");

				return mapping.findForward("GenerateChart");
			}

			// Step 4 : Report Details
			if(request.getParameter("check") != null && request.getParameter("check").equals("4"))
			{
				logger.info("In here  Getting Report Details.........." + formBean.getReportTitle());
				return mapping.findForward("ReportDetails");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		return mapping.findForward("forward");
	}// end of function execute
	
	
	public void updateData(Collection src, Collection dest, Long []selCol, AdvancedReportForm formBean)
	{
		if(str.equals("delete"))
		{
			if(src == null)
				return;
		}
		Iterator iter;
		Collection coll = new ArrayList();
		Collection temp = new ArrayList();
		Collection  dup= new ArrayList();
		AmpColumns ampColumns;
		boolean flag=false;
		AmpColumns colTemp = null;
		try
		{
			if(selCol != null)
			{
				if(dest == null)
				{
					dup = src;
					coll.clear();
					temp.clear();
				}
				else
				{
					temp.clear();
					dup = src;
					coll = dest;
				}

				for(int i=0; i < selCol.length; i++)
				{
					iter = src.iterator();// change needed
					temp.clear();
					while(iter.hasNext())
					{
						ampColumns = (AmpColumns) iter.next();
						if(ampColumns.getColumnId().compareTo(selCol[i]) == 0)
						{
							coll.add(ampColumns);
							colTemp = ampColumns;
							flag = true;
						}
						else
						{
							if(temp.contains(ampColumns) == false)
								temp.add(ampColumns);
						}
					}
					if(flag == true && colTemp != null)
					{
						dup.remove(colTemp);
						flag = false;
						src = dup;
					}
				}

				if(str.equals("add"))
				{
					formBean.setAddedColumns(coll);
					formBean.setAmpColumns(temp);
				}
				if(str.equals("delete"))
				{
					formBean.setAddedColumns(temp);
					formBean.setAmpColumns(coll);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
	}// end of Function ...........
}
