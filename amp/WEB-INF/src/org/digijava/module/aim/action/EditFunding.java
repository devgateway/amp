package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditFunding extends Action {

    private static Logger logger = Logger.getLogger(EditFunding.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        throw new RuntimeException("not implemented");
//      EditActivityForm formBean = (EditActivityForm) form;
//
//      //this is needed to aknowledge that we are still under EDIT ACTIVITY mode:
//        request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);
//      
//      Long orgId = formBean.getFunding().getOrgId();
//      int offset = formBean.getFunding().getOffset();
//      int numComm = 0;
//      int numDisb = 0;
//        int numDisbOrder=0;
//      int numExp = 0;
//      //
//      formBean.getFunding().setAssistanceType(null);
//      formBean.getFunding().setOrgFundingId("");
//      formBean.getFunding().setSignatureDate("");
//      formBean.getFunding().setFundingDetails(null);
//      formBean.getFunding().setFundingMTEFProjections(null);
//      //
//      formBean.getOldFunding().setAssistanceType(null);
//      formBean.getOldFunding().setOrgFundingId("");
//      formBean.getOldFunding().setSignatureDate("");
//      formBean.getOldFunding().setFundingDetails(null);
//      formBean.getOldFunding().setFundingMTEFProjections(null);
//      formBean.setTotDisbIsBiggerThanTotCom(false);
//      
//      List<KeyValue> availableMTEFProjectionYears     = 
//          AddFunding.generateAvailableMTEFProjectionYears( formBean.getFunding().getFundingMTEFProjections() ); 
//      formBean.getFunding().setAvailableMTEFProjectionYears( availableMTEFProjectionYears );
//  
//      int defaultIndex                = availableMTEFProjectionYears.size() - 1 - AddMTEFProjection.ADDITIONAL_AVAILABLE_YEARS;
//      formBean.getFunding().setSelectedMTEFProjectionYear( Integer.parseInt( 
//              availableMTEFProjectionYears.get(defaultIndex).getKey() )
//      );
//      
//  
//      ArrayList<FundingOrganization> fundingOrganizations = new ArrayList<FundingOrganization>(formBean.getFunding().getFundingOrganizations());
//      if (fundingOrganizations != null) {
//          FundingOrganization fundingOrganization = null;
//          for (int i = 0; i < fundingOrganizations.size(); i++) {
//              fundingOrganization = (FundingOrganization) fundingOrganizations.get(i);
//              if (fundingOrganization.getAmpOrgId().equals(orgId)) {
//                  formBean.getFunding().setOrgName(fundingOrganization.getOrgName());
//                  formBean.getOldFunding().setOrgName(fundingOrganization.getOrgName());
//                  int index = 0;
//                  ArrayList<Funding> fundings = new ArrayList<Funding>(fundingOrganization.getFundings());
//                  Funding funding = null;
//                  for (int j = 0; j < fundings.size(); j++) {
//                      if (index == offset) {
//                          funding = (Funding) fundings.get(j);
//                          if (funding != null) {
//                              //formBean.setAssistanceType(funding.getAmpTermsAssist().getAmpTermsAssistId());
//                              if (funding.getTypeOfAssistance()!=null) {
//                                  formBean.getFunding().setAssistanceType(funding.getTypeOfAssistance().getId());
//                                  formBean.getOldFunding().setAssistanceType(funding.getTypeOfAssistance().getId());
//                              } else {
//                                  formBean.getFunding().setAssistanceType(new Long(0));
//                                  formBean.getOldFunding().setAssistanceType(new Long(0));
//                              }
//                              //
//                                formBean.getFunding().setCapitalSpendingPercentage(funding.getCapitalSpendingPercentage());
//                              formBean.getFunding().setOrgFundingId(funding.getOrgFundingId());
//                              formBean.getOldFunding().setOrgFundingId(funding.getOrgFundingId());
//                              //
//                              if (funding.getFinancingInstrument() != null) {
//                                  formBean.getFunding().setModality(funding.getFinancingInstrument().getId());
//                                  formBean.getFunding().setFundingConditions(funding.getConditions());
//                                  formBean.getFunding().setFundingMTEFProjections( new ArrayList<MTEFProjection> (funding.getMtefProjections()) );
//                                  formBean.getFunding().setFundingDetails(new ArrayList<FundingDetail>());
//                                  //
//                                  formBean.getOldFunding().setModality(funding.getFinancingInstrument().getId());
//                                  formBean.getOldFunding().setFundingConditions(funding.getConditions());
//                                  formBean.getOldFunding().setFundingMTEFProjections( new ArrayList<MTEFProjection> (funding.getMtefProjections()) );
//                                  formBean.getOldFunding().setFundingDetails(new ArrayList<FundingDetail>());
//                              }
//                              if ( funding.getFundingStatus() != null ) {
//                                  formBean.getFunding().setFundingStatus( funding.getFundingStatus().getId() );
//                                  formBean.getOldFunding().setFundingStatus( funding.getFundingStatus().getId() );
//                              }
//                              
//                              if ( funding.getModeOfPayment() != null ) {
//                                  formBean.getFunding().setModeOfPayment( funding.getModeOfPayment().getId() );
//                                  formBean.getOldFunding().setModeOfPayment( funding.getModeOfPayment().getId() );
//                              }
//                              
//                              formBean.getFunding().setDonorObjective(funding.getDonorObjective());
//                              formBean.getOldFunding().setDonorObjective(funding.getDonorObjective());
//                              formBean.getFunding().setFundingConditions(funding.getConditions());
//                              formBean.getOldFunding().setFundingConditions(funding.getConditions());
//
//                              formBean.getFunding().setActualStartDate(funding.getActStartDate());
//                              formBean.getOldFunding().setActualStartDate(funding.getActStartDate());
//                              formBean.getFunding().setActualCompletionDate(funding.getActCloseDate());
//                              formBean.getOldFunding().setActualCompletionDate(funding.getActCloseDate());
//                              
//                              if (funding.getFundingDetails() != null) {
//                                  //
//                                  Iterator<FundingDetail> itr = funding.getFundingDetails().iterator();
//                                  FundingDetail fd = null;
//                                  while (itr.hasNext()) {
//                                      fd = itr.next();
//                                      //
//                                      if (fd.getTransactionType() == 0) {
//                                          numComm ++;
//                                      } else if (fd.getTransactionType() == 1) {
//                                          numDisb ++;
//                                      } else if (fd.getTransactionType() == 2) {
//                                          numExp ++;
//                                      }
//                                        else if (fd.getTransactionType() == 4) {
//                                          numDisbOrder ++;
//                                        }
//                                      //
//                                      if (fd.getTransactionAmount() == "") {
//                                          fd.setTransactionAmount("0");
//                                      }
//                                  }
//                                  //
//                                  formBean.getFunding().getFundingDetails().addAll(funding.getFundingDetails());
//                                  formBean.getOldFunding().getFundingDetails().addAll(funding.getFundingDetails());
//                                  //
//                              }
//                          }
//                          break;
//                      }
//                      index++;
//                  }
//                  break;
//              }
//          }
//      }
//      
//      //
//      formBean.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());
//      //
//      formBean.getFunding().setNumComm(numComm);
//      formBean.getFunding().setNumDisb(numDisb);
//      formBean.getFunding().setNumExp(numExp);
//        formBean.getFunding().setNumDisbOrder(numDisbOrder);
//      formBean.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));
//      formBean.getFunding().setOrganizations(DbUtil.getAllOrganisation());
//      formBean.getFunding().setEditFunding(true);
//      formBean.getFunding().setDupFunding(true);
//      formBean.getFunding().setFirstSubmit(false);
//      //
//      formBean.getOldFunding().setNumComm(numComm);
//      formBean.getOldFunding().setNumDisb(numDisb);
//      formBean.getOldFunding().setNumExp(numExp);
//        formBean.getOldFunding().setNumDisbOrder(numDisbOrder);
//      formBean.getOldFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));
//      formBean.getOldFunding().setOrganizations(DbUtil.getAllOrganisation());
//      formBean.getOldFunding().setEditFunding(true);
//      formBean.getOldFunding().setDupFunding(true);
//      formBean.getOldFunding().setFirstSubmit(false);
//      //
//      
//       // load donor related pledges
//      formBean.getFunding().setPledgeslist(PledgesEntityHelper.getPledgesByDonor(orgId));
//
//
//      return mapping.findForward("forward");
    }
}
