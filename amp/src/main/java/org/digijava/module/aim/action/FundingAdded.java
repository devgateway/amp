package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FundingAdded extends Action {

    private static Logger logger = Logger.getLogger(FundingAdded.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        throw new RuntimeException("SaveActivity::execute: not implemented!");

//      EditActivityForm eaForm = (EditActivityForm) form;
//
//      HttpSession session = request.getSession();
//      TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
//
//      Iterator fundOrgsItr = eaForm.getFunding().getFundingOrganizations().iterator();
//      FundingOrganization fundOrg = null;
//      boolean found = false;
//      int fundOrgOffset = 0;
//      while (fundOrgsItr.hasNext()) {
//          fundOrg = (FundingOrganization) fundOrgsItr.next();
//          if (fundOrg.getAmpOrgId().equals(eaForm.getFunding().getOrgId())) {
//              found = true;
//              break;
//          }
//          fundOrgOffset++;
//      }
//      //
//      int offset = -1;
//      if (found) {
//          if (eaForm.getFunding().isEditFunding()) {
//              offset = eaForm.getFunding().getOffset();
//          }
//      }
//      //      
//      double totalComms   = 0;
//      double totalDisbs   = 0;
//      double totalExps    = 0;
//      //boolean isBigger = false;     
//      //
//      if (eaForm.getFunding().getFundingDetails() != null) {
//          Iterator itr = eaForm.getFunding().getFundingDetails().iterator();
//          while (itr.hasNext()) {
//              FundingDetail fundDet = (FundingDetail) itr.next();
//              //
//              double amount = this.getAmountInDefaultCurrency(fundDet, tm.getAppSettings());                  
//              if (( fundDet.getTransactionType() == Constants.COMMITMENT )&&
//                      (fundDet.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())))
//                  totalComms  += amount;
//              else 
//                  if (( fundDet.getTransactionType() == Constants.DISBURSEMENT )&&
//                          (fundDet.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())))
//                          totalDisbs  += amount;
//                  else 
//                      if (( fundDet.getTransactionType() == Constants.EXPENDITURE )&&
//                              (fundDet.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())))
//                          totalExps   += amount;
//          }
////            String alert = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALERT_IF_DISBURSMENT_BIGGER_COMMITMENTS);
//          //
////            if (Boolean.parseBoolean(alert)) {
////                if (totalDisbs > totalComms) {
////                    eaForm.setTotDisbIsBiggerThanTotCom(true);
////                    isBigger = true;
////                } else {
////                    eaForm.setTotDisbIsBiggerThanTotCom(false);
////                }
////            }
//      }
//      EditActivityForm.Funding currentFunding = null;
//      //if (!isBigger) {
//          currentFunding = eaForm.getFunding();
//          //
//          Funding newFund = new Funding(currentFunding);
//          //
//          if (currentFunding.getFundingId() != null && currentFunding.getFundingId().longValue() > 0) {
//              newFund.setFundingId(currentFunding.getFundingId().longValue());
//          } else {
//              newFund.setFundingId(System.currentTimeMillis());
//          }
//          //newFund.setAmpTermsAssist(DbUtil.getAssistanceType(eaForm.getAssistanceType()));
//          //newFund.setTypeOfAssistance( CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getAssistanceType()) );
//          //newFund.setOrgFundingId(currentFunding.getOrgFundingId());
//          //newFund.setFinancingInstrument(CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getModality()));
//          //newFund.setConditions(currentFunding.getFundingConditions());
//          newFund.setDonorObjective(currentFunding.getDonorObjective());
//          newFund.setTypeOfAssistance( CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getAssistanceType()) );
//          newFund.setFinancingInstrument( CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getModality()) );
//          newFund.setFundingStatus( CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getFundingStatus()) );
//          newFund.setModeOfPayment( CategoryManagerUtil.getAmpCategoryValueFromDb(currentFunding.getModeOfPayment()) );
//          newFund.setOrgFundingId(currentFunding.getOrgFundingId());
//          newFund.setSourceRole(currentFunding.getSourceRole());
//          newFund.setConditions(currentFunding.getFundingConditions());
//          newFund.setActStartDate(currentFunding.getActualStartDate());
//          newFund.setActCloseDate(currentFunding.getActualCompletionDate());
//            newFund.setCapitalSpendingPercentage(currentFunding.getCapitalSpendingPercentage());
//
//          //
//          Collection mtefProjections=new ArrayList();
//          if (currentFunding.getFundingMTEFProjections() != null) {
//              Iterator itr = currentFunding.getFundingMTEFProjections().iterator();
//              while (itr.hasNext()) {
//                  MTEFProjection mtef = (MTEFProjection) itr.next();
//              
//                  if ( mtef.getAmount() == null ) //This MTEFProjection has been created in AddFunding action 
//                          continue;               // but if projections are disabled then the amount will be empty so this shouldn't be taken into consideration
//                  String formattedAmt = CurrencyWorker.formatAmount(
//                          mtef.getAmount());
//                  mtef.setAmount(formattedAmt);
//                  if (mtef.getCurrencyCode() != null
//                          && mtef.getCurrencyCode().trim().length() != 0) {
//                      AmpCurrency currency = CurrencyUtil.getCurrencyByCode(mtef.getCurrencyCode());
//                      mtef.setCurrencyName(currency.getCountryName());
//                  }
//                  if (mtef.getReportingOrganizationId() != null
//                          && mtef.getReportingOrganizationId().intValue() != 0) {
//                      AmpOrganisation org = DbUtil.getOrganisation(mtef
//                              .getReportingOrganizationId());
//                      mtef.setReportingOrganizationName(org.getName());
//                  
//                  }
//                  mtefProjections.add(mtef);
//              }
//          }       
//          Collection fundDetails = new ArrayList();
//          if (currentFunding.getFundingDetails() != null) {
//              Iterator itr = currentFunding.getFundingDetails().iterator();               
//              itr = currentFunding.getFundingDetails().iterator();
//              while (itr.hasNext()) {
//                  FundingDetail fundDet = (FundingDetail) itr.next();
//                  String formattedAmt = CurrencyWorker.formatAmount(
//                          fundDet.getTransactionAmount());
//                  fundDet.setTransactionAmount(formattedAmt);
//                  //
//                  if (fundDet.getCurrencyCode() != null
//                          && fundDet.getCurrencyCode().trim().length() != 0) {
//                      AmpCurrency currency = CurrencyUtil.getCurrencyByCode(fundDet
//                              .getCurrencyCode());
//                      fundDet.setCurrencyName(currency.getCountryName());
//                  }
//                  if (fundDet.getReportingOrganizationId() != null
//                          && fundDet.getReportingOrganizationId().intValue() != 0) {
//                      AmpOrganisation org = DbUtil.getOrganisation(fundDet
//                              .getReportingOrganizationId());
//                      fundDet.setReportingOrganizationName(org.getName());
//                  }
//                  
//                  //
//                  fundDetails.add(fundDet);
//              }
//          }
//          //
//          List sortedList = new ArrayList(fundDetails);
//          Collections.sort(sortedList,FundingValidator.dateComp);
//
//          ArrayList fundList = new ArrayList();
//          if (fundOrg.getFundings() != null) {
//              fundList = new ArrayList(fundOrg.getFundings());
//          }
//
//          currentFunding.setDupFunding(false);
//          currentFunding.setFirstSubmit(false);
//
//          if (currentFunding.getFundingDetails() != null) 
//          {
//              int i=0;
//              Iterator fundItr1 = currentFunding.getFundingDetails().iterator();
//              while(fundItr1.hasNext())
//              {
//                  i++;
//                  FundingDetail fundDetItr1 = (FundingDetail) fundItr1.next();
//                  Iterator fundItr2 = currentFunding.getFundingDetails().iterator();
//                  int j=0;
//                  while(fundItr2.hasNext())
//                  {
//                      j++;
//                      FundingDetail fundDetItr2 = (FundingDetail) fundItr2.next();
//                      if(j>i)
//                      {
//                          if((fundDetItr2.getAdjustmentTypeName().getValue().equalsIgnoreCase(fundDetItr1.getAdjustmentTypeName().getValue()))&&
//                          (fundDetItr2.getCurrencyCode().equalsIgnoreCase(fundDetItr1.getCurrencyCode()))&&
//                          (fundDetItr2.getTransactionAmount().equalsIgnoreCase(fundDetItr1.getTransactionAmount()))&&
//                          (fundDetItr2.getTransactionDate().equalsIgnoreCase(fundDetItr1.getTransactionDate()))&&
//                          (fundDetItr2.getTransactionType()==fundDetItr1.getTransactionType()))
//                          {
//                              currentFunding.setDupFunding(true);
//                              currentFunding.setFirstSubmit(true);
//                          }
//                      }
//                  }
//              }
//          }
//          //
//          newFund.setFundingDetails(sortedList);
//          newFund.setMtefProjections(mtefProjections);
//          if (offset != -1)
//              fundList.set(offset, newFund);
//          else
//              fundList.add(newFund);
//          //
//          fundOrg.setFundings(fundList);
//          ArrayList fundingOrgs = new ArrayList();
//          if (currentFunding.getFundingOrganizations() != null) {
//              fundingOrgs = new ArrayList(currentFunding.getFundingOrganizations());
//              fundingOrgs.set(fundOrgOffset, fundOrg);
//          }
//          //
//          this.updateTotals(eaForm, tm);
//      //} 
//      //
//      String currCode = CurrencyUtil.getAmpcurrency( tm.getAppSettings().getCurrencyId() ).getCurrencyCode();
//      eaForm.setCurrCode( currCode );
//      eaForm.setStep("3");
//
//      return mapping.findForward("forward");
    }
    
//  private double[] getFundingAmounts(EditActivityForm form, TeamMember tm) {
//      double totalComms   = 0;
//      double totalDisbs   = 0;
//      double totalExps    = 0;        
//      //
//      Collection <FundingOrganization> orgs   = form.getFunding().getFundingOrganizations();
//      if ( orgs != null ) {
//          Iterator<FundingOrganization> iterOrg   = orgs.iterator();
//          while ( iterOrg.hasNext() ) {
//              Collection<Funding> funds       = iterOrg.next().getFundings();
//              if ( funds != null ) {
//                  Iterator<Funding> iterFund  = funds.iterator(); 
//                  while ( iterFund.hasNext() ) {
//                      Collection<FundingDetail> details   = iterFund.next().getFundingDetails();
//                      if ( details != null ) {
//                          Iterator<FundingDetail> iterDet = details.iterator();
//                          while ( iterDet.hasNext() ) {
//                              FundingDetail detail        = iterDet.next();
//                              double amount               = this.getAmountInDefaultCurrency(detail, tm.getAppSettings());                 
//                              if (( detail.getTransactionType() == Constants.COMMITMENT )&&
//                                      (detail.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())))
//                                          totalComms  += amount;
//                              else 
//                                  if (( detail.getTransactionType() == Constants.DISBURSEMENT )&&
//                                          (detail.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())))
//                                          totalDisbs  += amount;
//                                  else 
//                                      if (( detail.getTransactionType() == Constants.EXPENDITURE )&&
//                                              (detail.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())))
//                                          totalExps   += amount;
//                          }
//                      }
//                  }
//              }
//              
//          }
//      }
//      double[] amounts = {totalComms, totalDisbs, totalExps};
//      //
//      return amounts;
//  }
//  
//  private void updateTotals ( EditActivityForm form, TeamMember tm ) {
//      double [] amounts = getFundingAmounts(form, tm);
//      //
//      form.getFunding().setTotalCommitments(FormatHelper.formatNumber(amounts[0]));
//      form.getFunding().setTotalDisbursements(FormatHelper.formatNumber(amounts[1]));
//      form.getFunding().setTotalExpenditures(FormatHelper.formatNumber(amounts[2])    );
//  }
//  
//  private double getAmountInDefaultCurrency(FundingDetail fundDet, ApplicationSettings appSet) {      
//      java.sql.Date dt = new java.sql.Date(DateConversion.getDate(fundDet.getTransactionDate()).getTime());
//      double frmExRt = Util.getExchange(fundDet.getCurrencyCode(),dt);
//      String toCurrCode = CurrencyUtil.getAmpcurrency( appSet.getCurrencyId() ).getCurrencyCode();
//      double toExRt = Util.getExchange(toCurrCode,dt);
//      double amt = CurrencyWorker.convert1(FormatHelper.parseDouble(fundDet.getTransactionAmount()),frmExRt,toExRt);
//      //
//      return amt;     
//  }
}
