package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.CommitmentbyDonorForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ViewDisbursementbyDonor extends Action
{
	private static Logger logger = Logger.getLogger(ViewDisbursementbyDonor.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		CommitmentbyDonorForm formBean = (CommitmentbyDonorForm) form;
				
		int currentYear = 2001;
		int forcastYear1 = currentYear+1;
		int forcastYear2 = currentYear+2;
		int forcastYear3 = currentYear+3;

		formBean.setReport(new ArrayList()) ;
		ArrayList projects = DbUtil.getProjects();
		Iterator iter = null ;
		iter = projects.iterator() ;
		while ( iter.hasNext() )
		{	
			AmpActivity activity=(AmpActivity) iter.next();
			AmpCategoryValue statusValue	= CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
			String statusStr				= null;
			if (statusValue != null)
				statusStr		= statusValue.getValue();
			
			logger.debug("Title :" + activity.getName());
			logger.debug("Id :" + activity.getAmpActivityId());
			logger.debug("Status :" + statusStr);
			//get the sector for the activity.getAmpActivityId()
			Collection Sectors = SectorUtil.getAmpSectors(activity.getAmpActivityId());
			logger.debug("sector size is " + Sectors.size());
			StringBuffer SectorStr = new StringBuffer();
			if(Sectors.size() != 0)
			{
				Iterator iterSector = Sectors.iterator() ;
				while(iterSector.hasNext())
				{
					AmpSector ampSector = new AmpSector();
					ampSector = (AmpSector) iterSector.next();
					logger.debug("Sector name is" + ampSector.getName());
					SectorStr.append(ampSector.getName());
					SectorStr.append(",");
				}
			}
			else
			{
				SectorStr.append("No Sectors");
			}		
			  //Getting fund details
			  Collection dbreturn = DbUtil.getAmpFunding(activity.getAmpActivityId());
	
			  if(dbreturn.size() != 0 )
			  {	
				  Iterator iter1 = dbreturn.iterator() ;
				  while( iter1.hasNext() )
				  {	  Report report1 = new Report();
					  AmpFunding ampFunding = (AmpFunding)iter1.next() ;
					  logger.debug("DONOR ORGS : " + ampFunding.getAmpDonorOrgId().getName());
					  logger.debug("START DATE : " + (Date)ampFunding.getActualStartDate());
					  logger.debug("CLOSE DATE : " + (Date)ampFunding.getActualCompletionDate());
					  report1.setDonor(ampFunding.getAmpDonorOrgId().getName());
					  report1.setTitle(activity.getName());
					  report1.setStatus(statusStr);
					  report1.setStartDate(DateConversion.ConvertDateToString(ampFunding.getActualStartDate()));
					  report1.setCloseDate(DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()));
					  report1.setSector(SectorStr);
					  //DbUtil.getFundDetails(fundingId,new Integer(0),new Integer(1));	
					  double plannedDisbursement = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(1),new Integer(0));			
					  double actualDisbursement = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId() ,new Integer(1),new Integer(1));			
   					 if( plannedDisbursement == 0.0)
					 {
					  logger.debug("PLANNED TRANSACTION AMOUNT BY DONOR IS NULL:" );
					    report1.setPlannedDisbursement(0.0);
					 }
					 else
					 {	
					    report1.setPlannedCommitment(plannedDisbursement);
					  logger.debug("PLANNED TRANSACTION AMOUNT BY DONOR IS :" + plannedDisbursement );
					 }
					 if( actualDisbursement == 0.0)
					 {
					  logger.debug("ACTUAL TRANSACTION AMOUNT BY DONOR IS NULL:" );
					    report1.setActualDisbursement(0.0);
					 }
					 else
					 {	
					    report1.setActualCommitment(actualDisbursement);
					  logger.debug("ACTUAL TRANSACTION AMOUNT BY DONOR IS :" + plannedDisbursement );
					 }
					 
					//Collecting Planned Commitments for the forcast years
					  double plannedDisbursementForcast1 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(1),new Integer(0), new Integer(forcastYear1));			
					  if( plannedDisbursementForcast1 == 0.0)
					  {
						  //////System.out.println("FORCAST YEAR1 PLANNED COMMITMENT AMOUNT:" + plannedDisbursementForcast1 );
						  report1.setPlannedCommitmentForcast1(0.0 );
					  }
					  else
					  {
						  report1.setPlannedCommitmentForcast1(plannedDisbursementForcast1 );

					  }

					  double plannedDisbursementForcast2 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(1),new Integer(0), new Integer(forcastYear2));			
					  if( plannedDisbursementForcast2 == 0.0)
					  {
						  //////System.out.println("FORCAST YEAR2 PLANNED COMMITMENT AMOUNT:" + plannedDisbursementForcast2 );
						  report1.setPlannedCommitmentForcast2(0.0 );
					  }
					  else
					  {	
						  report1.setPlannedCommitmentForcast2(plannedDisbursementForcast2 );

					  }

					  double plannedCommitmentForcast3 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(1),new Integer(0), new Integer(forcastYear3));			
					  if( plannedDisbursementForcast2 == 0.0)
					  {
						  //////System.out.println("FORCAST YEAR3 PLANNED COMMITMENT AMOUNT:" + plannedCommitmentForcast3 );
						  report1.setPlannedCommitmentForcast3(0.0 );
					  }
					  else
					  {	
						  report1.setPlannedCommitmentForcast3(plannedCommitmentForcast3 );
					  }

					formBean.getReport().add(report1) ;

				  }//while
			  }//if
			  else
			  {
				  logger.debug("FUNDING INFO RETURNED NULL");
				 // report.setActualCommitment(0.0);
				 // report.setPlannedCommitment(0.0);
				Report report = new Report();
				report.setTitle(activity.getName());
				report.setStatus(statusStr);
				report.setDonor("No Donor");
				report.setSector(SectorStr);
				report.setStartDate("DD/MM/YY");
				report.setCloseDate("DD/MM/YY");
				formBean.getReport().add(report) ;	
			  } 
		}//while projects iterator
		
		formBean.setForcastYear1(forcastYear1);
		formBean.setForcastYear2(forcastYear2);
		formBean.setForcastYear3(forcastYear3);
		return mapping.findForward("forward");
	}
}
