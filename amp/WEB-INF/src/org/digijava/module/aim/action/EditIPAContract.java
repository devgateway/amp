    package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.IPAContractForm;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * @author mihai
 * 
 */
public class EditIPAContract extends MultiAction {

    /**
     * 
     */
    public EditIPAContract() {
        super();
    // TODO Auto-generated constructor stub
    }

    public boolean hasUnselectedItems(List items) {
        Iterator i = items.iterator();
        while (i.hasNext()) {
            String element = (String) i.next();
            if ("-1".equals(element)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasInvalidAmounts(List items) {
        Iterator i = items.iterator();
        while (i.hasNext()) {
            String element = (String) i.next();
            try {
               if(!element.equals("")){
               Double.parseDouble(element);
               }
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    /*
     * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        eaf.setCurrencies(CurrencyUtil.getAmpCurrency());
        Collection<AmpCategoryValue> catValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IPA_ACTIVITY_CATEGORY_KEY, true);
        Collection<AmpCategoryValue> statuses = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IPA_STATUS_KEY, true);
        Collection<AmpCategoryValue> types = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IPA_TYPE_KEY, true);
        eaf.setActivitiyCategories(catValues);
        eaf.setStatuses(statuses);
        eaf.setTypes(types);
        eaf.setOrganisations(DbUtil.getAmpOrganisations());

        return modeSelect(mapping, form, request, response);
    }

    public ActionForward modeDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        EditActivityForm eaf = (EditActivityForm) session.getAttribute("eaf");
        Integer indexId = new Integer(request.getParameter("indexId"));
        eaf.getContracts().remove(indexId - 1);
        //request.setAttribute("close", "close");
        return modeFinalize(mapping, form, request, response);
       
    }

    public ActionForward modeEdit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        EditActivityForm eaf = (EditActivityForm) session.getAttribute("eaf");
        IPAContractForm euaf = (IPAContractForm) form;
        Integer indexId = new Integer(request.getParameter("indexId")) - 1;
        IPAContract contract = (IPAContract) eaf.getContracts().get(indexId);
        euaf.setIndexId(indexId);
        euaf.setContractName(contract.getContractName());
        euaf.setDescription(contract.getDescription());
        euaf.setId(contract.getId());
        if (contract.getActivityCategory() != null) {
            euaf.setActivityCategoryId(contract.getActivityCategory().getId());
        }
        if (contract.getStatus() != null) {
            euaf.setStatusId(contract.getStatus().getId());
        }
        
        if (contract.getType()!= null) {
            euaf.setTypeId(contract.getType().getId());
        }
        if (contract.getOrganization() != null) {
            euaf.setContrOrg(contract.getOrganization().getAmpOrgId());
        }

        if (contract.getStartOfTendering() != null) {
            euaf.setStartOfTendering(DateTimeUtil.parseDateForPicker2(contract.getStartOfTendering()));
        }
        if (contract.getSignatureOfContract() != null) {
            euaf.setSignatureOfContract(DateTimeUtil.parseDateForPicker2(contract.getSignatureOfContract()));
        }
        if (contract.getContractCompletion() != null) {
            euaf.setContractCompletion(DateTimeUtil.parseDateForPicker2(contract.getContractCompletion()));
        }
        if (contract.getTotalECContribIBAmount() != null) {
            euaf.setTotalECContribIBAmount(String.valueOf(contract.getTotalECContribIBAmount()));
        }
        if (contract.getTotalECContribIBCurrency() != null) {
            euaf.setTotalECContribIBCurrency(contract.getTotalECContribIBCurrency().getAmpCurrencyId());
        }
        if (contract.getTotalECContribINVAmount() != null) {
            euaf.setTotalECContribINVAmount(String.valueOf(contract.getTotalECContribINVAmount()));
        }
        if (contract.getTotalECContribINVCurrency() != null) {
            euaf.setTotalECContribINVCurrency(contract.getTotalECContribINVCurrency().getAmpCurrencyId());
        }
        if (contract.getTotalNationalContribCentralAmount() != null) {
            euaf.setTotalNationalContribCentralAmount(String.valueOf(contract.getTotalNationalContribCentralAmount()));
        }
        if (contract.getTotalNationalContribCentralCurrency() != null) {
            euaf.setTotalNationalContribCentralCurrency(contract.getTotalNationalContribCentralCurrency().getAmpCurrencyId());
        }
        if (contract.getTotalNationalContribRegionalAmount() != null) {
            euaf.setTotalNationalContribRegionalAmount(String.valueOf(contract.getTotalNationalContribRegionalAmount()));
        }
        if (contract.getTotalNationalContribRegionalCurrency() != null) {
            euaf.setTotalNationalContribRegionalCurrency(contract.getTotalNationalContribRegionalCurrency().getAmpCurrencyId());
        }
        if (contract.getTotalNationalContribIFIAmount() != null) {
            euaf.setTotalNationalContribIFIAmount(String.valueOf(contract.getTotalNationalContribIFIAmount()));
        }

        if (contract.getTotalNationalContribIFICurrency() != null) {
            euaf.setTotalNationalContribIFICurrency(contract.getTotalNationalContribIFICurrency().getAmpCurrencyId());
        }
        if (contract.getTotalPrivateContribAmount() != null) {

            euaf.setTotalPrivateContribAmount(String.valueOf(contract.getTotalPrivateContribAmount()));
        }
        if (contract.getTotalPrivateContribCurrency() != null) {
            euaf.setTotalPrivateContribCurrency(contract.getTotalPrivateContribCurrency().getAmpCurrencyId());
        }
        if (contract.getDisbursements() != null) {
            ArrayList<IPAContractDisbursement> disbs = new ArrayList(contract.getDisbursements());
            euaf.setContractDisbursements(disbs);
        }



        return modeFinalize(mapping, form, request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (request.getParameter("editEU") != null) {
            return modeEdit(mapping, form, request, response);
        }
        if (request.getParameter("new") != null) {
            return modeNew(mapping, form, request, response);
        }
        if (request.getParameter("save") != null) {
            return modeValidateSave(mapping, form, request, response);
        }
        if (request.getParameter("addFields") != null) {
            return modeAddFields(mapping, form, request, response);
        }
        if (request.getParameter("removeFields") != null) {
            return modeRemoveFields(mapping, form, request, response);
        }
        if (request.getParameter("deleteEU") != null) {
            return modeDelete(mapping, form, request, response);
        }
        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeNew(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        eaf.setContractDisbursements(new ArrayList<IPAContractDisbursement>());
        eaf.setIndexId(-1);
        Long currId=new Long(-1);
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
          if (tm != null && tm.getAppSettings() != null && tm.getAppSettings()
          .getCurrencyId() != null) {
            
              AmpCurrency curr=CurrencyUtil.
                  getAmpcurrency(
                      tm.getAppSettings()
                      .getCurrencyId());
              if(curr!=null){
                      currId = curr.getAmpCurrencyId();
              }
          }
        eaf.setTotalECContribIBCurrency(currId);
        eaf.setTotalECContribINVCurrency(currId);
        eaf.setTotalNationalContribCentralCurrency(currId);
        eaf.setTotalNationalContribIFICurrency(currId);
        eaf.setTotalNationalContribRegionalCurrency(currId);
        eaf.setTotalPrivateContribCurrency(currId);
              

        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeValidateSave(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors errors = new ActionErrors();
        IPAContractForm eaf = (IPAContractForm) form;
        List<String> amountsList = new ArrayList<String>();
        amountsList.add(eaf.getTotalECContribIBAmount());
        amountsList.add(eaf.getTotalECContribINVAmount());

        amountsList.add(eaf.getTotalNationalContribCentralAmount());
        amountsList.add(eaf.getTotalNationalContribIFIAmount());
        amountsList.add(eaf.getTotalNationalContribRegionalAmount());

        amountsList.add(eaf.getTotalPrivateContribAmount());


        if (hasInvalidAmounts(amountsList)) {
            errors.add("title", new ActionError(
                    "error.aim.ipacontract.invalidAmountFormat"));
        }
        //if(hasUnselectedItems(eaf.())) errors.add("title", new ActionError(
        //"error.aim.euactivity.selectDonor"));

        saveErrors(request, errors);

        if (!errors.isEmpty()) {
            return modeFinalize(mapping, form, request, response);
        }

        return modeSave(mapping, form, request, response);

    }

    public ActionForward modeFinalize(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;

        return mapping.findForward("forward");



    }

    public ActionForward modeAddFields(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        IPAContractDisbursement icd = new IPAContractDisbursement();
        eaf.getContractDisbursements().add(icd);
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
          if (tm != null && tm.getAppSettings() != null && tm.getAppSettings()
          .getCurrencyId() != null) {
              AmpCurrency curr=CurrencyUtil.
                  getAmpcurrency(
                      tm.getAppSettings()
                      .getCurrencyId());
              icd.setCurrency(curr);
 
          }
       
        

        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeRemoveFields(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPAContractForm eaf = (IPAContractForm) form;
        Long[] idx=eaf.getSelContractDisbursements();
        List toRemove=new ArrayList();
        if(idx!=null&&idx.length>0){
            for(int i=0; i<idx.length;i++){
               IPAContractDisbursement contrDisb=eaf.getContractDisbursement(idx[i].intValue()-1);
               toRemove.add(contrDisb);
            }
            eaf.getContractDisbursements().removeAll(toRemove);
            eaf.setSelContractDisbursements(null);
        }

        return modeFinalize(mapping, form, request, response);
    }

    public ActionForward modeSave(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // generate business objects:
        HttpSession httpSess = request.getSession();
        IPAContractForm euaf = (IPAContractForm) form;
        EditActivityForm eaf = (EditActivityForm) httpSess.getAttribute("eaf");
        IPAContract eua = null;

        eua = new IPAContract();
        if (euaf.getId() != null && euaf.getId() != 0) {
            eua.setId(euaf.getId());
        }
        eua.setContractName(euaf.getContractName());
        eua.setDescription(euaf.getDescription());
        Long orgId = euaf.getContrOrg();
        if (orgId != null && orgId != -1) {
            eua.setOrganization(DbUtil.getOrganisation(orgId));
        }
        if (euaf.getActivityCategoryId() != null) {
            eua.setActivityCategory(CategoryManagerUtil.getAmpCategoryValueFromDb(euaf.getActivityCategoryId()));
        }
        if (euaf.getStatusId() != null) {
            eua.setStatus(CategoryManagerUtil.getAmpCategoryValueFromDb(euaf.getStatusId()));
        }
        if (euaf.getTypeId() != null) {
            eua.setType(CategoryManagerUtil.getAmpCategoryValueFromDb(euaf.getTypeId()));
        }
        if (euaf.getStartOfTendering() != null&&!euaf.getStartOfTendering().equals("")) {
            eua.setStartOfTendering(DateTimeUtil.parseDateForPicker(euaf.getStartOfTendering()));
        }
        if (euaf.getSignatureOfContract() != null&&!euaf.getSignatureOfContract().equals("")) {
            eua.setSignatureOfContract(DateTimeUtil.parseDateForPicker(euaf.getSignatureOfContract()));
        }
        if (euaf.getContractCompletion() != null&&!euaf.getContractCompletion().equals("")) {
            eua.setContractCompletion(DateTimeUtil.parseDateForPicker(euaf.getContractCompletion()));
        }
        if (euaf.getTotalECContribIBAmount() != null&&!euaf.getTotalECContribIBAmount().equals("")) {
            eua.setTotalECContribIBAmount(Double.parseDouble(euaf.getTotalECContribIBAmount()));
            if (euaf.getTotalECContribIBCurrency() != null&&!euaf.getTotalECContribIBCurrency().equals("")) {
            eua.setTotalECContribIBCurrency(CurrencyUtil.getAmpcurrency(euaf.getTotalECContribIBCurrency()));
        }
        }
       
        if (euaf.getTotalECContribINVAmount() != null&&!euaf.getTotalECContribINVAmount().equals("")) {

            eua.setTotalECContribINVAmount(Double.parseDouble(euaf.getTotalECContribINVAmount()));
              if (euaf.getTotalECContribINVCurrency() != null) {
            eua.setTotalECContribINVCurrency(CurrencyUtil.getAmpcurrency(euaf.getTotalECContribINVCurrency()));
        }
        }
      
        if (euaf.getTotalNationalContribCentralAmount() != null&&!euaf.getTotalNationalContribCentralAmount().equals("")) {
            eua.setTotalNationalContribCentralAmount(Double.parseDouble(euaf.getTotalNationalContribCentralAmount()));
            if (euaf.getTotalNationalContribCentralCurrency() != null) {
            eua.setTotalNationalContribCentralCurrency(CurrencyUtil.getAmpcurrency(euaf.getTotalNationalContribCentralCurrency()));
        }
        }
        
        if (euaf.getTotalNationalContribRegionalAmount() != null&&!euaf.getTotalNationalContribRegionalAmount().equals("")) {

            eua.setTotalNationalContribRegionalAmount(Double.parseDouble(euaf.getTotalNationalContribRegionalAmount()));
             if (euaf.getTotalNationalContribRegionalCurrency() != null) {
            eua.setTotalNationalContribRegionalCurrency(CurrencyUtil.getAmpcurrency(euaf.getTotalNationalContribRegionalCurrency()));
        }
        }
       
        if (euaf.getTotalNationalContribIFIAmount() != null&&!euaf.getTotalNationalContribIFIAmount().equals("")) {
            eua.setTotalNationalContribIFIAmount(Double.parseDouble(euaf.getTotalNationalContribIFIAmount()));
            if (euaf.getTotalNationalContribIFICurrency() != null) {
            eua.setTotalNationalContribIFICurrency(CurrencyUtil.getAmpcurrency(euaf.getTotalNationalContribIFICurrency()));
        }
        }
        
        if (euaf.getTotalPrivateContribAmount() != null&&!euaf.getTotalPrivateContribAmount().equals("")) {
            eua.setTotalPrivateContribAmount(Double.parseDouble(euaf.getTotalPrivateContribAmount()));
             if (euaf.getTotalPrivateContribCurrency() != null) {
            eua.setTotalPrivateContribCurrency(CurrencyUtil.getAmpcurrency(euaf.getTotalPrivateContribCurrency()));
        }
        }
       
         if (eaf.getContracts() == null) {
                eaf.setContracts(new ArrayList());
        }
        if (euaf.getContractDisbursements() != null) {
            HashSet disbs = new HashSet(euaf.getContractDisbursements());
            eua.setDisbursements(disbs);
           
            Iterator<IPAContractDisbursement> disbIter = disbs.iterator();
            while (disbIter.hasNext()) {
                IPAContractDisbursement disb = disbIter.next();
                disb.setContract(eua);
            }
           
        }
         if (euaf.getIndexId() != null && euaf.getIndexId() != -1) {
                eaf.getContracts().set(euaf.getIndexId(), eua);
            } else {
                eaf.getContracts().add(eua);
         }
        request.setAttribute("close", "close");
        return modeFinalize(mapping, form, request, response);
    }
    }
