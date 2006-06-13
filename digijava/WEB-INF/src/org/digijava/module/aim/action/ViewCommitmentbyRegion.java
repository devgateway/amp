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
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.form.CommitmentbyDonorForm;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;

public class ViewCommitmentbyRegion extends Action
{
	private static Logger logger = Logger.getLogger(ViewCommitmentbyRegion.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		CommitmentbyDonorForm formBean = (CommitmentbyDonorForm) form;
		formBean.setReport(new ArrayList()) ;
		
		ArrayList projects = DbUtil.getProjects();
		Iterator iter = null ;
		iter = projects.iterator() ;
		while ( iter.hasNext() )
		{
			AmpActivity activity=(AmpActivity) iter.next();
			
			List location = LocationUtil.getAmpLocations(activity.getAmpActivityId());
			if(location.size() == 0 )
			{  
				  logger.debug("No Locations returned");
			}
			else
			{
				Iterator iters = location.iterator() ;
				while(iters.hasNext())
				{
					AmpLocation ampLocation = (AmpLocation) iters.next();
					logger.debug("AmpActivity Id is : " + activity.getAmpActivityId());
					logger.debug("REGION IS :" + ampLocation.getRegion()); 
					logger.debug("COUNTRY IS :" + ampLocation.getCountry());
					
					//Get the funding details for each project
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
						report1.setStatus(activity.getStatus().getName());
						report1.setCountry(ampLocation.getRegion());
						report1.setRegion(ampLocation.getCountry());
						report1.setStartDate(DateConversion.ConvertDateToString(ampFunding.getActualStartDate()));
						report1.setCloseDate(DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()));

						//Getting Cummulative planned and Actual Commitment	
						double plannedCommitment = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(0),new Integer(0));			
						double actualCommitment = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId() ,new Integer(0),new Integer(1));			
						if( plannedCommitment == 0.0)
						{
							logger.debug("PLANNED COMMITMENT AMOUNT BY DONOR IS NULL");
							report1.setPlannedCommitment(0.0);
						}
						else
						{	
							logger.debug("PLANNED COMMITMENT AMOUNT BY DONOR IS :" + plannedCommitment );
							report1.setPlannedCommitment(plannedCommitment);
						}
						if( actualCommitment == 0.0)
						{
							logger.debug("ACTUAL COMMITMENT AMOUNT BY DONOR IS NULL:" );
							report1.setActualCommitment(0.0);
						}
						else
						{	
							logger.debug("ACTUAL COMMITMENT AMOUNT BY DONOR IS :" + plannedCommitment );
							report1.setActualCommitment(actualCommitment);
						}
	 
						//Getting Cummulative planned and Actual Disbursement	
						double plannedDisbursement = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId(),new Integer(1),new Integer(0));			
						double actualDisbursement = DbUtil.getFundDetails((Long)ampFunding.getAmpFundingId() ,new Integer(1),new Integer(1));			
						if( plannedDisbursement == 0.0)
						{
						  logger.debug("PLANNED Disbursement AMOUNT BY DONOR IS NULL");
						  report1.setPlannedDisbursement(0.0);
						}
						else
						{	logger.debug("PLANNED Disbursement AMOUNT BY DONOR IS :" + plannedDisbursement);

						  report1.setPlannedDisbursement(plannedDisbursement);
						}
						if( actualDisbursement == 0.0)
						{
							logger.debug("ACTUAL Disbursement AMOUNT BY DONOR IS NULL:" );
							report1.setActualDisbursement(0.0);
						}
						else
						{	
							logger.debug("ACTUAL Disbursement AMOUNT BY DONOR IS :" + plannedDisbursement);
							report1.setActualDisbursement(actualDisbursement);
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
						report.setStatus(activity.getStatus().getName());
						report.setCountry("null");
						report.setRegion("null");
						report.setDonor("No Donor");
						report.setStartDate("DD/MM/YY");
						report.setCloseDate("DD/MM/YY");
						formBean.getReport().add(report) ;	
					}//else 
					
				}//while
			}//else
		}
		
		return mapping.findForward("forward");
	}
}



