/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.ViewContractingForm;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.gateperm.core.GatePermConst;


/**
 *
 * 
 */
public class ContractingBreakdown extends TilesAction {
    private static Logger logger = Logger.getLogger(ContractingBreakdown.class);
	
	public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form,
							HttpServletRequest request, HttpServletResponse response) {
	
		logger.debug("In get Contracting list action");
		
			HttpSession session = request.getSession();
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			ViewContractingForm contrForm =(ViewContractingForm) form;
			request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
		      if (tm != null && tm.getAppSettings() != null && tm.getAppSettings()
		          .getCurrencyId() != null) {
		              String currCode="";
		              AmpCurrency curr=CurrencyUtil.
		                  getAmpcurrency(
		                      tm.getAppSettings()
		                      .getCurrencyId());
		              if(curr!=null){
		                      currCode = curr.getCurrencyCode();
		              }
		              contrForm.setCurrCode(currCode);
		      }
		      
			Long actId = null;
			
				contrForm.setTabIndex(request.getParameter("tabIndex"));
				String activityId = request.getParameter("ampActivityId");
				if (null != activityId && activityId.trim().length() > 0) {
					actId = Long.valueOf(activityId);
					logger.debug("actId : " + actId);
					contrForm.setAmpActivityId(actId);
				}
			
			
			
			List<IPAContract> contracts = ActivityUtil.getIPAContracts(actId);
			ArrayList<IPAContract> newContracts=new ArrayList<IPAContract>();
			String cc=contrForm.getCurrCode();
			for(Iterator<IPAContract> it= contracts.iterator(); it.hasNext();)
			{
				IPAContract contract=(IPAContract) it.next();
				double usdAmount1=0;  
	    		double finalAmount1=0; 
				if (contract.getDisbursements() != null) {
		             ArrayList<IPAContractDisbursement> disbs = new ArrayList<IPAContractDisbursement>(contract.getDisbursements());
		             
		             //if there is no disbursement global currency saved in db we'll use the default from edit activity form
		             
		            if(contract.getDibusrsementsGlobalCurrency()!=null)
		         	   cc=contract.getDibusrsementsGlobalCurrency().getCurrencyCode();
		            double td=0;
		            double usdAmount=0;  
		    		double finalAmount=0; 

		    		   for(Iterator<IPAContractDisbursement> j=disbs.iterator();j.hasNext();)
		       	  	{
		       		  IPAContractDisbursement cd=(IPAContractDisbursement) j.next();
		       		  // converting the amount to the currency from the top and adding to the final sum.
		       		  if(cd.getAmount()!=null)
		       			  {
		       			  	try {
								usdAmount = CurrencyWorker.convertToUSD(cd.getAmount().doubleValue(),cd.getCurrCode());
							} catch (AimException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		       			  	try {
								finalAmount = CurrencyWorker.convertFromUSD(usdAmount,cc);
							} catch (AimException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		       			  	td+=finalAmount;
		       			  }
		       	  	}
		       	  	
		       	  	//eaf.getCurrCode();
		    		   
		       	  	contract.setTotalDisbursements(td);
				}
				
				 if(contract.getTotalAmount()!=null)
			   	  	{
					 try {
							usdAmount1 = CurrencyWorker.convertToUSD(contract.getTotalAmount().doubleValue(),contract.getTotalAmountCurrency().getCurrencyCode());
						} catch (AimException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	       			  	try {
							finalAmount1 = CurrencyWorker.convertFromUSD(usdAmount1,cc);
						} catch (AimException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					 
					 double amountRate=0;
					 if(finalAmount1!=0) amountRate=contract.getTotalDisbursements().doubleValue()/finalAmount1;
			   	  		contract.setExecutionRate(amountRate);
			   	  	//System.out.println("2 execution rate: "+amountRate);
			   	  	}
			   	  	else {
			   	  		contract.setExecutionRate(new Double(0));
			   	  	}
				
				newContracts.add(contract);
			}

			contrForm.setContracts(newContracts);
			
			return null;
			
		}
		
	}


