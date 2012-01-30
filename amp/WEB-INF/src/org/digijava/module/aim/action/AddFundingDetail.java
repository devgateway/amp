package org.digijava.module.aim.action;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.gateperm.core.GatePermConst;

//import au.com.bytecode.opencsv.CSVReader;

/**
 * @author jose
 */
public class AddFundingDetail extends Action {

	private static Logger logger = Logger.getLogger(AddFundingDetail.class);

	private ArrayList<FundingDetail> fundingDetails = null;

	private String event;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		event = formBean.getFunding().getEvent();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);
		
		if (event!=null) {
			if (event.equals("importFundingDetail") && formBean.getFileImport() != null) //If this is an import, means we have to look for the file and parse it
			{
				String error = importFunding(formBean, teamMember);
				if (error != null && !error.equals("")){
					try {
						ActionMessages errors = new ActionMessages();
						errors.add("title", new ActionMessage("error.aim.addActivity.importFunding.error", TranslatorWorker.translateText("Error in the structure/data of the file. Please review.",request)));
						saveErrors(request, errors);								
					} catch (WorkerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	
	
				return mapping.findForward("forward");
			}
			else if(event.equals("importFundingDetailDone")){
				return mapping.findForward("confirmed");
			}
		}

		String currCode = Constants.DEFAULT_CURRENCY;
		if (teamMember.getAppSettings() != null) {
			ApplicationSettings appSettings = teamMember.getAppSettings();
			if (appSettings.getCurrencyId() != null) {
				currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
			}
		}

		long index = formBean.getFunding().getTransIndexId();
        if(event!=null && event.length()>3){
			String subEvent = event.substring(0,3);
			FundingDetail fd = null;
			if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
				if (formBean.getFunding().getFundingDetails() == null) {
					fundingDetails = new ArrayList<FundingDetail>();
					fd = getFundingDetail(currCode);
					fundingDetails.add(fd);
				} else {
					fundingDetails = new ArrayList<FundingDetail>(formBean.getFunding().getFundingDetails());
					if (subEvent.equals("del")) {
						FundingDetail temp = new FundingDetail();
						temp.setIndexId(index);
	                                        temp=(FundingDetail)fundingDetails.get(fundingDetails.indexOf(temp));
	                                        Iterator <FundingDetail> iter=fundingDetails.iterator();
	                                        while(iter.hasNext()){
	                                              FundingDetail det=iter.next();
	                                              if (det.getTransactionType()==Constants.DISBURSEMENT&&
	                                                  det.getDisbOrderId() != null &&
	                                                  det.getDisbOrderId().equals(temp.getDisbOrderId())) {
	                                                      det.setDisbOrderId(null);
	                                              }
	                                        }
						fundingDetails.remove(temp);
					} else {
						fd = getFundingDetail(currCode);
						fundingDetails.add(fd);
					}
				}
				if (fd != null && fd.getTransactionType() == 0) {
					formBean.getFunding().setNumComm(formBean.getFunding().getNumComm() + 1);
				} else if (fd != null && fd.getTransactionType() == 1) {
					formBean.getFunding().setNumDisb(formBean.getFunding().getNumDisb() + 1);
				} else if (fd != null && fd.getTransactionType() == 2) {
					formBean.getFunding().setNumExp(formBean.getFunding().getNumExp() + 1);
				}
	                        else if (fd != null && fd.getTransactionType() == 4) {
	                                int numDisbOrder=formBean.getFunding().getNumDisbOrder() + 1;
	                                formBean.getFunding().setNumDisbOrder(numDisbOrder);
	                                Iterator<FundingDetail> iter=fundingDetails.iterator();
	                                long max=100;
	                                while(iter.hasNext()){
	                                       FundingDetail det=iter.next();
	                                       if(det.getDisbOrderId()!=null&&!det.getDisbOrderId().equals("")){
	                                                 int id = Integer.parseInt(det.getDisbOrderId());
	                                                 if (max < id) {
	                                                   max = id;
	                                                 }
	
	                                       }
	
	
	                                }
	                                fd.setDisbOrderId(""+(++max));
	                        }
				formBean.getFunding().setFundingDetails(fundingDetails);
			}
        }
		formBean.getFunding().setEvent(null);
		formBean.getFunding().setDupFunding(true);
		formBean.getFunding().setFirstSubmit(false);
		return mapping.findForward("forward");
	}

	private FundingDetail getFundingDetail(String currCode) {
		FundingDetail fundingDetail = new FundingDetail();

		if (event.equalsIgnoreCase("addCommitments")) {
			fundingDetail.setTransactionType(Constants.COMMITMENT);
		} else if (event.equalsIgnoreCase("addDisbursements")) {
			fundingDetail.setTransactionType(Constants.DISBURSEMENT);
		} else if (event.equalsIgnoreCase("addExpenditures")) {
			fundingDetail.setTransactionType(Constants.EXPENDITURE);
			fundingDetail.setClassification("");
		}
                else if (event.equalsIgnoreCase("addDisbursementOrders")) {
                       fundingDetail.setTransactionType(Constants.DISBURSEMENT_ORDER);
                       fundingDetail.setClassification("");

               }
		fundingDetail.setCurrencyCode(currCode);
		fundingDetail.setAdjustmentType(Constants.ACTUAL);
        fundingDetail.setIndexId(System.currentTimeMillis());
		fundingDetail.setIndex(fundingDetails.size());
		fundingDetail.setReportingDate(new Date(System.currentTimeMillis()));
		return fundingDetail;
	}
	
	private String importFunding(EditActivityForm eaForm, TeamMember tm) {
		//Do all the preparation needed for the adding of disb/comm/etc
		ArrayList<FundingDetail> fundingDetails = new ArrayList<FundingDetail>();
		FundingDetail fd = null;
		String currCode = CurrencyUtil.getAmpcurrency( tm.getAppSettings().getCurrencyId() ).getCurrencyCode();
		FormFile formFile = eaForm.getFileImport();
		String error = "";
		try {
			InputStreamReader isr = new InputStreamReader(formFile.getInputStream());
			/*CSVReader reader = new CSVReader(isr);
			
			
			String [] nextLine;
			Boolean firstLine = true;
			while ((nextLine = reader.readNext()) != null) {
				if(firstLine){
					firstLine = false;
				}
				else
				{
					if (nextLine.length == 7) //Type, Amount, Date
					{
						//commitment/disbursement/expenditure,actual/planned,amount,currency,day,month,year
						//     0                             ,      1       ,   2  ,   3    , 4 ,  5  , 6
						
						FundingDetail fundingDetail = new FundingDetail();
						if (nextLine[0].equals("commitment")) fundingDetail.setTransactionType(Constants.COMMITMENT);
						if (nextLine[0].equals("disbursement")) fundingDetail.setTransactionType(Constants.DISBURSEMENT);
						if (nextLine[0].equals("expenditure")) fundingDetail.setTransactionType(Constants.EXPENDITURE);
						if (nextLine[1].equals("actual")) fundingDetail.setAdjustmentType(Constants.ACTUAL);
						if (nextLine[1].equals("planned")) fundingDetail.setAdjustmentType(Constants.PLANNED);
						BigDecimal fundingAmount = FormatHelper.parseBigDecimal(nextLine[2]);
						fundingDetail.setTransactionAmount(fundingAmount.toPlainString());
						fundingDetail.setCurrencyCode(nextLine[3]);
						//Put together the date
						GregorianCalendar calendar = new GregorianCalendar();
						calendar.set(Integer.parseInt(nextLine[6]), Integer.parseInt(nextLine[5])-1, Integer.parseInt(nextLine[4]));
						fundingDetail.setTransactionDate(FormatHelper.formatDate(calendar.getTime()));
						fundingDetail.setClassification("");
						fundingDetail.setIndexId(System.currentTimeMillis());
						fundingDetail.setIndex(fundingDetails.size());
						fundingDetails.add(fundingDetail);			
						
					}
				}
			}			*/
			eaForm.getFunding().setFundingDetails(fundingDetails);
		} catch (NumberFormatException e)
		{
			error = "Number format problem while importing. Please verify the file.";
			logger.error(error);
			e.printStackTrace();
		} catch (Exception e) {
			error = "Error importing file. Please verify that the columns are correct.";
			logger.error(error);
			e.printStackTrace();
		}
		
		return error;
	}
}
