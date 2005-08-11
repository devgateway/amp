
/*
 * AdvancedReport.java @Author Ronald B Created: 27-July-2005
 */

package org.digijava.module.aim.action;


import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.kernel.persistence.PersistenceManager;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ReportUtil;


import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;


public class AdvancedReport extends Action {

	private static Logger logger = Logger.getLogger(Login.class);
	private String str="";
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		AdvancedReportForm formBean = (AdvancedReportForm) form;
		
		HttpSession httpSession = request.getSession();
		Query query;
		Session session = null;
		Transaction tx = null;
		String sqlQuery;
		Iterator iter;
		Collection coll = new ArrayList();
		AmpColumns ampColumns;
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

		try
		{
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			// Fills the column that can be selected from AMP_COLUMNS
			if(formBean.getAmpColumns() == null)
			{
				formBean.setAmpColumns(ReportUtil.getColumnList());
				formBean.setReportTitle("");
			}
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
				logger.info("In here  Getting Report Details..........");
				return mapping.findForward("ReportDetails");
			}
			
			// Move the selected column Up : Step 1 ie Select Columns
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveUp"))
				moveColumns(formBean, "MoveUp");

			// Move the selected column Down : Step 1 ie Select Columns
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveDown"))
				moveColumns(formBean, "MoveDown");

			
			// save Report
			if(request.getParameter("check") != null && request.getParameter("check").equals("SaveReport"))
			{
				boolean flag = false;
				logger.info("---------Start--Report --- Save -------------");
				ActionErrors errors = new ActionErrors();	
				if(formBean.getReportTitle() != null)
				{
					if(formBean.getReportTitle().trim().length() == 0)
					{
							errors.add("title", new ActionError("error.aim.addActivity.titleMissing"));
							saveErrors(request, errors);
							flag = true;
					}
				}
				
				if(flag == false)
				{
					boolean found = false;
					String queryString = "select report.name from " + AmpReports.class.getName() + " report ";
					logger.info( " Query 2 :" + queryString);
					query = session.createQuery(queryString);
					iter = query.list().iterator();

					if(query!=null)
					{
						iter = query.list().iterator();
						while(iter.hasNext())
						{
							String str = (String) iter.next();
							if( formBean.getReportTitle().trim().equals(str) )
							{
		                		errors.add("DuplicateReportName", new ActionError("error.aim.reportManager.DuplicateReportName"));
								saveErrors(request, errors);
								found = true;
								return mapping.findForward("MissingReportDetails");
								//break;
							}
							else
								found = false;
						}
					}

					if(found == false)
	                {
						AmpReports ampReports = new AmpReports();
						String descr = "/"+formBean.getReportTitle().replaceAll(" " , "");
						descr = descr + ".do";
						ampReports.setDescription(descr);
						ampReports.setName(formBean.getReportTitle());
					
						// saving the selected columns for the report
						Set columns = new HashSet();
						iter = formBean.getAddedColumns().iterator();
						while(iter.hasNext())
						{
							AmpColumns cols = (AmpColumns)iter.next();
							columns.add(cols);
						}
						ampReports.setColumns(columns);
						
						session.save(ampReports);
						ampReports.setDescription("/advancedReport.do?view=reset~reportId="+ampReports.getAmpReportId());
						session.update(ampReports);
						tx.commit(); // commit the transcation
						
						// Clears the values of the Previous report 
						formBean.setAmpColumns(null);
						formBean.setAddedColumns(null);

	                }
					logger.info("-------------Stop-----------");
				}
				
				return mapping.findForward("viewMyDesktop");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e) {
					logger.error("Release session faliled :" + e);
				}
			}
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
	
	
	// Function to move Columns Up and Down
	
	private static void moveColumns(AdvancedReportForm formBean, String option)
	{
		Iterator iter= null;
		AmpColumns ampColumns = null;
		
		logger.info(  formBean.getAddedColumns().size() +" : Move Up : "+ formBean.getMoveColumn());
		if(formBean.getAddedColumns().size() == 1)
			logger.info(" Cannot move field up.......");
		else
		{
			Long lg = new Long(formBean.getMoveColumn());
			ArrayList temp = new ArrayList();
			AmpColumns curr = null, prev = null , next = null;
			int index = 0;
			
			temp.addAll(formBean.getAddedColumns());
			iter = temp.iterator();		
			while(iter.hasNext())
			{
				ampColumns = (AmpColumns) iter.next();
				
				if(option.compareTo("MoveUp") == 0)
				{
					if(lg.compareTo(ampColumns.getColumnId()) == 0 )
					{
						logger.info(" Found : " + temp.indexOf(ampColumns));
						if(temp.indexOf(ampColumns) > 0)
						{
							curr = (AmpColumns)temp.get(temp.indexOf(ampColumns));
							prev = (AmpColumns)temp.get(temp.indexOf(ampColumns)-1);
							index = temp.indexOf(ampColumns);
	
							temp.set(index, prev);
							temp.set(index-1, curr);
							formBean.setAddedColumns(temp);
							break;
						}
						else
						{
							logger.info("Cannot  Swap.........");
							break;								
						}
					}
				} // This code moves the selected Column Up

				if(option.compareTo("MoveDown") == 0)
				{
					if(lg.compareTo(ampColumns.getColumnId()) == 0 )
					{
						logger.info(" Found : " + temp.indexOf(ampColumns));
						if( (temp.indexOf(ampColumns)+1) < formBean.getAddedColumns().size())
						{
							curr = (AmpColumns)temp.get(temp.indexOf(ampColumns));
							next = (AmpColumns)temp.get(temp.indexOf(ampColumns)+1);
							index = temp.indexOf(ampColumns);
							temp.set(index, next);
							temp.set(index+1, curr);
							formBean.setAddedColumns(temp);
							break;
						}
						else
						{
							logger.info("Cannot  Swap.........");
							break;								
						}
					}
				} // This code moves the selected Column Down

			}
		}

	}

}
