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
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.CommitmentbyDonorForm;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;

public class ViewCommitmentbyDonor extends Action
{
	private static Logger logger = Logger.getLogger(ViewCommitmentbyDonor.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		CommitmentbyDonorForm formBean = (CommitmentbyDonorForm) form;
		String currencyCode = formBean.getCurrency() ;
		
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
			AmpActivity activity			=(AmpActivity) iter.next();
			AmpCategoryValue statusValue	= CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, activity.getCategories());
			String statusStr				= null;
			if (statusValue != null)
				statusStr		= statusValue.getValue();
			
			logger.debug("Title :" + activity.getName());
			logger.debug("Id :" + activity.getAmpActivityId());
			logger.debug("Status :" + statusStr);
			//logger.debug("Sectors :" + activity..getSectors().size() );
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
			logger.debug("sectors name are " + SectorStr);
			  //Getting fund details
			  Collection dbreturn = DbUtil.getAmpFunding(activity.getAmpActivityId());
	
			  if(dbreturn.size() != 0 )
			  {	
				  Iterator iter1 = dbreturn.iterator() ;
				  int i=1;
				  while( iter1.hasNext() )
				  {	  Report report1 = new Report();
					  AmpFunding ampFunding = (AmpFunding)iter1.next() ;
					  logger.debug("DONOR ORGS No : " + i + " is " + ampFunding.getAmpDonorOrgId().getName());
					  i++;
					  String perspective = "DN";
					  
					  //for each donor, get the commited transaction_amount, year and currency
					  //DbUtil.getDonorFund((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0),perspective);
					  logger.debug(" After collecting Donor fund Details");
					  logger.debug("START DATE : " + (Date)ampFunding.getActualStartDate());
					  logger.debug("CLOSE DATE : " + (Date)ampFunding.getActualCompletionDate());
					  report1.setDonor(ampFunding.getAmpDonorOrgId().getName());
					  report1.setTitle(activity.getName());
					  report1.setStatus(statusStr);
					  report1.setStartDate(DateConversion.ConvertDateToString(ampFunding.getActualStartDate()));
					  report1.setCloseDate(DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()));
					  report1.setSector(SectorStr);
					  //DbUtil.getFundDetails(fundingId,new Integer(0),new Integer(1));	
					 // double plannedCommitment = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0));			
					 // double actualCommitment = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId() ,new Integer(0),new Integer(1));
					 double plannedCommitment = DbUtil.getDonorFund((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0),perspective);			
					 double actualCommitment = DbUtil.getDonorFund((Long)ampFunding.getAmpFundingId() ,new Integer(0),new Integer(1),perspective);			
   					 if( plannedCommitment == 0.0)
					 {
					  logger.debug("PLANNED TRANSACTION AMOUNT BY DONOR IS NULL:" );
					    report1.setPlannedCommitment(0.0);
					 }
					 else
					 {	
					    report1.setPlannedCommitment(plannedCommitment);
					  logger.debug("PLANNED TRANSACTION AMOUNT BY DONOR IS :" + plannedCommitment );
					 }
					 if( actualCommitment == 0.0)
					 {
					  logger.debug("ACTUAL TRANSACTION AMOUNT BY DONOR IS NULL:" );
					    report1.setActualCommitment(0.0);
					 }
					 else
					 {	
					    report1.setActualCommitment(actualCommitment);
					  logger.debug("ACTUAL TRANSACTION AMOUNT BY DONOR IS :" + plannedCommitment );
					 }
					 
					//Collecting Planned Commitments for the forcast years
					  double plannedCommitmentForcast1 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0), new Integer(forcastYear1));			
					  if( plannedCommitmentForcast1 == 0.0)
					  {
						  ////System.out.println("FORCAST YEAR1 PLANNED COMMITMENT AMOUNT:" + plannedCommitmentForcast1 );
						  report1.setPlannedCommitmentForcast1(0.0 );
					  }
					  else
					  {
						  report1.setPlannedCommitmentForcast1(plannedCommitmentForcast1 );

					  }

					  double plannedCommitmentForcast2 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0), new Integer(forcastYear2));			
					  if( plannedCommitmentForcast2 == 0.0)
					  {
						  ////System.out.println("FORCAST YEAR2 PLANNED COMMITMENT AMOUNT:" + plannedCommitmentForcast2 );
						  report1.setPlannedCommitmentForcast2(0.0 );
					  }
					  else
					  {	
						  report1.setPlannedCommitmentForcast2(plannedCommitmentForcast2 );

					  }

					  double plannedCommitmentForcast3 = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0), new Integer(forcastYear3));			
					  if( plannedCommitmentForcast2 == 0.0)
					  {
						  ////System.out.println("FORCAST YEAR3 PLANNED COMMITMENT AMOUNT:" + plannedCommitmentForcast3 );
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
				report.setSector(SectorStr);
				report.setTitle(activity.getName());
				report.setStatus(statusStr);
				report.setDonor("No Donor");
				report.setStartDate("DD/MM/YY");
				report.setCloseDate("DD/MM/YY");
				formBean.getReport().add(report) ;	
			  } 
		}//while projects iterator
		
		formBean.setForcastYear1(forcastYear1);
		formBean.setForcastYear2(forcastYear2);
		formBean.setForcastYear3(forcastYear3);
		//if ( ( currencyCode != null) && (!currencyCode.equals("USD")) )
			//formBean = CurrencyChange.convert(formBean,currencyCode) ;

		return mapping.findForward("forward");
	}
}
