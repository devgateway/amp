package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import org.digijava.module.aim.form.CommitmentbySectorForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ViewDisbursementbySector extends Action
{
	private static Logger logger = Logger.getLogger(ViewDisbursementbySector.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		CommitmentbySectorForm formBean = (CommitmentbySectorForm) form;
				
		int currentYear = 2001;
		int forcastYear1 = currentYear+1;
		int forcastYear2 = currentYear+2;
		int forcastYear3 = currentYear+3;

		formBean.setReport(new ArrayList()) ;
		//getting all the projects list
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
			
			String sectorname = new String();
			String subsectorname = new String();
			List sector = SectorUtil.getAmpSectors(activity.getAmpActivityId());
			if(sector.size() == 0 )
			{  
			  logger.debug("No Sectors returned");
			}
			else
			{
			  logger.debug("Sectors returned");
			  Iterator iters = sector.iterator() ;
			  //Loop through the sectors
			  while(iters.hasNext())
			  {	
				  
				  AmpSector ampSector = (AmpSector) iters.next();
				  logger.debug("AmpActivity Id is : " + activity.getAmpActivityId());
				  if( ampSector.getParentSectorId() == null)
				  {
					logger.debug("Main Sector:"+ ampSector.getName() + " " + ampSector.getAmpSectorId()) ;
					sectorname = ampSector.getName();
					subsectorname = "No subsector";
				  }
				  else
					{
					logger.debug("Sub sector : "+ ampSector.getName() + " " + ampSector.getAmpSectorId()) ;
					logger.debug("Parent Sector : "+ ampSector.getParentSectorId().getName()) ;
					sectorname = ampSector.getParentSectorId().getName();
					subsectorname = ampSector.getName();
				  }
				
				  //Get the funding details for each sector classification for each project
					Collection dbreturn1 = DbUtil.getAmpFunding(activity.getAmpActivityId());

					if(dbreturn1.size() != 0 )
					{	
					  Iterator iter1 = dbreturn1.iterator() ;
					  while( iter1.hasNext() )
					  {	 
						Report report1 = new Report();
						AmpFunding ampFunding = (AmpFunding)iter1.next() ;
						report1.setDonor(ampFunding.getAmpDonorOrgId().getName());
						report1.setTitle(activity.getName());
						report1.setStatus(statusStr);
						report1.setSectorname(sectorname);
						report1.setSubSector(subsectorname);
						report1.setStartDate(DateConversion.ConvertDateToString(ampFunding.getActualStartDate()));
						report1.setCloseDate(DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()));

						//DbUtil.getFundDetails(fundingId,new Integer(0),new Integer(1));	
						double plannedDisbursement = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(1),new Integer(0));			
						double actualDisbursement = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId() ,new Integer(1),new Integer(1));			
						if( plannedDisbursement == 0.0)
						{
								report1.setPlannedDisbursement(0.0);
						}
						else
						{	
								report1.setPlannedDisbursement(plannedDisbursement);
						}
						if( actualDisbursement == 0.0)
						{
							report1.setActualDisbursement(0.0);
						}
						else
						{	
								report1.setActualDisbursement(actualDisbursement);
						}
					 
						//Collecting Planned Disbursements for the forcast years
						double plannedDisbursementForcast1 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0), new Integer(forcastYear1));			
						if( plannedDisbursementForcast1 == 0.0)
						{
						  report1.setPlannedCommitmentForcast1(0.0 );
						}
						else
						{
						  report1.setPlannedCommitmentForcast1(plannedDisbursementForcast1 );

						}

						double plannedDisbursementForcast2 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0), new Integer(forcastYear2));			
						if( plannedDisbursementForcast2 == 0.0)
						{
						  report1.setPlannedCommitmentForcast2(0.0 );
						}
						else
						{	
						  report1.setPlannedCommitmentForcast2(plannedDisbursementForcast2 );

						}

						double plannedDisbursementForcast3 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0), new Integer(forcastYear3));			
						if( plannedDisbursementForcast3 == 0.0)
						{
						   report1.setPlannedCommitmentForcast3(0.0 );
						}
						else
						{	
						  report1.setPlannedCommitmentForcast3(plannedDisbursementForcast3 );
						}

						formBean.getReport().add(report1) ;

					  }//while
					}//if
					else
					{
						// report.setActualCommitment(0.0);
						// report.setPlannedCommitment(0.0);
						Report report = new Report();
						report.setTitle(activity.getName());
						report.setStatus(statusStr);
						report.setSectorname(sectorname);
						report.setSubSector(subsectorname);
						report.setDonor("No Donor");
						report.setStartDate("DD/MM/YY");
						report.setCloseDate("DD/MM/YY");
						formBean.getReport().add(report) ;	
					}//else 	
			  }//sector while
			}
		}//while projects iterator
		
		formBean.setForcastYear1(forcastYear1);
		formBean.setForcastYear2(forcastYear2);
		formBean.setForcastYear3(forcastYear3);
		return mapping.findForward("forward");
	}
}

