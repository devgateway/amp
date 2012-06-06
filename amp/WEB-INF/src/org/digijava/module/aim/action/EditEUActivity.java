/**
 *
 */
package org.digijava.module.aim.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.EUActivity;
import org.digijava.module.aim.dbentity.EUActivityContribution;
import org.digijava.module.aim.form.EUActivityForm;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Session;

/**
 * @author mihai
 *
 */
public class EditEUActivity extends MultiAction {
	
	private String siteId;
	private String locale;
	
	/**
	 *
	 */
	public EditEUActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean hasUnselectedItems(List items) {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Object element = (Object) i.next();
			if (element == null || "-1".equals(element.toString()) || "0".equals(element.toString())) {
				return true;
			}
		}
		return false;
	}


	public boolean hasInvalidAmounts(List items) {
		Iterator i=items.iterator();
		while (i.hasNext()) {
				String element = (String) i.next();
				try {
					Double.parseDouble(element);
			} catch (NumberFormatException e) {
			 return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		eaf.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());
		//eaf.setDonors(DbUtil.getAllOrganisation());
		eaf.setFinTypes(DbUtil.getAllAssistanceTypesFromCM());
		eaf.setFinInstrs(DbUtil.getAllFinancingInstruments());

		eaf.getContrAmountList().clear();
		eaf.getContrCurrIdList().clear();
		eaf.getContrDonorIdList().clear();
		eaf.getContrDonorNameList().clear();
		eaf.getContrFinInstrIdList().clear();
		eaf.getContrFinTypeIdList().clear();

		if(eaf.getContrAmount()!=null) {
			eaf.getContrAmountList().addAll(Arrays.asList(eaf.getContrAmount()));
			eaf.getContrCurrIdList().addAll(Arrays.asList(eaf.getContrCurrId()));
			eaf.getContrDonorIdList().addAll(Arrays.asList(eaf.getContrDonorId()));
			eaf.getContrDonorNameList().addAll(Arrays.asList(eaf.getContrDonorName()));
			
			if (eaf.getContrFinInstrId() != null) {
				List auxInstrs = Arrays.asList(eaf.getContrFinInstrId());
				Iterator iterInstr = auxInstrs.iterator();
				while(iterInstr.hasNext()) {
					Long auxInst = (Long) iterInstr.next();
					if(auxInst != null) {
						eaf.getContrFinInstrIdList().add(auxInst);
					}
				}
				//eaf.getContrFinInstrIdList().addAll(Arrays.asList(eaf.getContrFinInstrId()));
			}
			if (eaf.getContrFinTypeId() != null) {
				List auxFinTypes = Arrays.asList(eaf.getContrFinTypeId());
				Iterator iterFin = auxFinTypes.iterator();
				while(iterFin.hasNext()) {
					Long auxFinType = (Long) iterFin.next();
					if(auxFinType != null) {
						eaf.getContrFinTypeIdList().add(auxFinType);
					}
				}
				//eaf.getContrFinTypeIdList().addAll(Arrays.asList(eaf.getContrFinTypeId()));
			}
		}


		return modeSelect(mapping, form, request, response);
	}


	public ActionForward modeDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session=request.getSession();
		EditActivityForm eaf=(EditActivityForm) session.getAttribute("eaf");
		Integer IndexId=new Integer(request.getParameter("indexId"));
		eaf.getCosting().getCosts().remove(IndexId.intValue());

		request.setAttribute("close", "close");
		return modeFinalize(mapping, form, request, response);
	}


	public ActionForward modeEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session=request.getSession();

		EditActivityForm eaf=(EditActivityForm) session.getAttribute("eaf");
		EUActivityForm euaf = (EUActivityForm) form;

		Integer IndexId=new Integer(request.getParameter("indexId"));
		euaf.setEditingIndexId(IndexId);

		EUActivity element=(EUActivity) eaf.getCosting().getCosts().get(IndexId.intValue());
				euaf.setId(element.getId());
				euaf.setAssumptions(element.getAssumptions());
				euaf.setInputs(element.getInputs());
				euaf.setName(element.getName());
				euaf.setProgress(element.getProgress());
				euaf.setTextId(element.getTextId());
				euaf.setTotalCost(element.getTotalCost().toString());
				euaf.setTotalCostCurrencyId(element.getTotalCostCurrency().getAmpCurrencyId());
				euaf.setDueDate(DateTimeUtil.parseDateForPicker2(element.getDueDate()));

				euaf.getContrAmountList().clear();
				euaf.getContrCurrIdList().clear();
				euaf.getContrDonorIdList().clear();
				euaf.getContrDonorNameList().clear();

				euaf.getContrFinInstrIdList().clear();
				euaf.getContrFinTypeIdList().clear();
				
				Iterator ii=element.getContributions().iterator();
				while (ii.hasNext()) {
					EUActivityContribution element2 = (EUActivityContribution) ii.next();
					euaf.getContrAmountList().add(element2.getAmount().toString());
					euaf.getContrCurrIdList().add(element2.getAmountCurrency().getAmpCurrencyId().toString());
					AmpOrganisation o =  DbUtil.getOrganisation( element2.getDonor().getAmpOrgId());
					euaf.getContrDonorIdList().add(o.getAmpOrgId().toString());
					euaf.getContrDonorNameList().add(o.getName());
					euaf.getContrFinInstrIdList().add(element2.getFinancingInstr().getId());
					euaf.getContrFinTypeIdList().add(element2.getFinancingTypeCategVal().getId());
				}
		return modeFinalize(mapping,form,request,response);
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
		if (request.getParameter("editEU") != null)
			return modeEdit(mapping, form, request, response);
		if (request.getParameter("new") != null)
			return modeNew(mapping, form, request, response);
		if (request.getParameter("save") != null)
			return modeValidateSave(mapping, form, request, response);
		if (request.getParameter("addFields") != null)
			return modeAddFields(mapping, form, request, response);
		if (request.getParameter("removeFields") != null)
			return modeRemoveFields(mapping, form, request, response);
		if (request.getParameter("deleteEU") != null)
			return modeDelete(mapping, form, request, response);
		return modeFinalize(mapping, form, request, response);
	}

	public ActionForward modeNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		HttpSession session=request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		Long currencyId = tm.getAppSettings().getCurrencyId();
		eaf.clear();
		eaf.setTotalCostCurrencyId(currencyId);
		eaf.getContrCurrIdList().set(0,currencyId);
		eaf.getContrDonorIdList().set(0,new Long(-1));
		eaf.getContrDonorNameList().set(0,"");
         
		
		//System.out.println("DueDate:"+eaf.getDueDate());
		return modeFinalize(mapping, form, request, response);
	}

	public ActionForward modeValidateSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String baseCurrCode		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
		if ( baseCurrCode == null ) {
			baseCurrCode	= "USD";
		}
		
		AmpCurrency baseCurr		= CurrencyUtil.getAmpcurrency(baseCurrCode);
		
                      HttpSession session=request.getSession();
		ActionMessages errors = new ActionMessages();
                TeamMember tm = (TeamMember) session.getAttribute("currentMember");
                Site site = RequestUtils.getSite(request);
        		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
        		siteId = site.getId()+"";
        		locale = navigationLanguage.getCode();	
        		
		Long currencyId = tm.getAppSettings().getCurrencyId();
                String defaultCurCode="";
                String defaultCurName="";
                if(currencyId!=null){
                  AmpCurrency defcurr=CurrencyUtil.getAmpcurrency(currencyId);
                  defaultCurCode=defcurr.getCurrencyCode();
                  defaultCurName=defcurr.getCurrencyName();
                }
		EUActivityForm eaf = (EUActivityForm) form;
                Long totalCostCurrId=eaf.getTotalCostCurrencyId();
                AmpCurrency  totalCostCurr=CurrencyUtil.getAmpcurrency(totalCostCurrId);
                String totalCurCode = totalCostCurr.getCurrencyCode();
                double totalCostExRate = CurrencyUtil.getExchangeRate(
                          totalCurCode);
                if (totalCostExRate == 1.0 && !totalCurCode.equals(baseCurrCode)) {
                  errors.add("title", new ActionMessage(
                      "error.aim.addActivity.noExchangeRateIsDefined", 
                      TranslatorWorker.translateText("There is no exchange rate defined for the currency: ",request) + 
                      TranslatorWorker.translateText(totalCostCurr.getCurrencyName(), request) + 
                      TranslatorWorker.translateText(" please use the default currency: ", request)  + 
                      TranslatorWorker.translateText(baseCurr.getCurrencyName(), request)  )
                  );
                }
                   else{
                     Object[] currencies = eaf.getContrCurrId();
                     if (currencies != null) {
                       for (int i = 0; i < currencies.length; i++) {
                         Long id = Long.parseLong( (String) currencies[i]);
                         AmpCurrency curr = CurrencyUtil.getAmpcurrency(id);
                         String currCode = curr.getCurrencyCode();
                         double exchangeRate = CurrencyUtil.getExchangeRate(
                             currCode);
                         if (exchangeRate == 1.0 &&!currCode.equals( baseCurrCode )) {
                           errors.add("title", new ActionMessage(
                               "error.aim.addActivity.noExchangeRateIsDefined", 
                               TranslatorWorker.translateText("There is no exchange rate defined for the currency: ",request) + 
                               TranslatorWorker.translateText(totalCostCurr.getCurrencyName(),request) + 
                               TranslatorWorker.translateText(" please use the default currency: ",request) + 
                               TranslatorWorker.translateText(baseCurr.getCurrencyName(), request)  )
                           );
                           break;
                         }

                       }
                     }
                   }
		try {
			Double.parseDouble(eaf.getTotalCost());
		} catch (NumberFormatException e) {
			errors.add("title", new ActionMessage(
					"error.aim.euactivity.invalidAmountFormat", TranslatorWorker.translateText("Please enter valid numerical amounts",locale,siteId)));
		}

		try {
			//System.out.println("DueDate:"+eaf.getDueDate());
			DateTimeUtil.parseDateForPicker(eaf.getDueDate());
			
		} catch (ParseException e) {
			//System.out.println("Exception:"+e);
			errors.add("title", new ActionMessage(
					"error.aim.euactivity.dueDate", TranslatorWorker.translateText("Please pick the Due Date",locale,siteId)));
		}
		if("".equalsIgnoreCase(eaf.getName())) errors.add("title", new ActionMessage(
		"error.aim.euactivity.noactivityname", TranslatorWorker.translateText("Please enter the activity name",locale,siteId)));
		if(hasInvalidAmounts(eaf.getContrAmountList())) errors.add("title", new ActionMessage(
		"error.aim.euactivity.invalidAmountFormat", TranslatorWorker.translateText("Please enter valid numerical amounts",locale,siteId)));
		if(hasUnselectedItems(eaf.getContrDonorIdList())) errors.add("title", new ActionMessage(
		"error.aim.euactivity.selectDonor", TranslatorWorker.translateText("Please pick the Donors from the drop down lists",locale,siteId)));
		if(hasUnselectedItems(eaf.getContrCurrIdList())) errors.add("title", new ActionMessage(
		"error.aim.euactivity.selectCurrency", TranslatorWorker.translateText("Please pick the Currencies from the drop down lists",locale,siteId)));
		if(hasUnselectedItems(eaf.getContrFinInstrIdList())) errors.add("title", new ActionMessage(
		"error.aim.euactivity.contrFinInstr", TranslatorWorker.translateText("Please pick the Financing Instruments from the drop down lists",locale,siteId)));
		if(hasUnselectedItems(eaf.getContrFinTypeIdList())) errors.add("title", new ActionMessage(
		"error.aim.euactivity.contrFinType", TranslatorWorker.translateText("Please pick the Financing Types from the drop down lists",locale,siteId)));



		saveErrors(request, errors);

		if(!errors.isEmpty()) return modeFinalize(mapping, form, request, response);

		return modeSave(mapping, form, request, response);

	}


	public ActionForward modeFinalize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		eaf.setContrAmount(eaf.getContrAmountList().toArray());
		eaf.setContrCurrId(eaf.getContrCurrIdList().toArray());
		eaf.setContrDonorId(eaf.getContrDonorIdList().toArray());
		eaf.setContrDonorName(eaf.getContrDonorNameList().toArray());
		Long []x=new Long[1];
		Long []y=new Long[1];
		eaf.setContrFinInstrId(  (Long[]) eaf.getContrFinInstrIdList().toArray(x));
		eaf.setContrFinTypeId(  (Long[]) eaf.getContrFinTypeIdList().toArray(y));
		return mapping.findForward("forward");
	}

	public ActionForward modeAddFields(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
		HttpSession session=request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		Long currencyId = tm.getAppSettings().getCurrencyId();
		eaf.getContrAmountList().add(new String(""));
		eaf.getContrCurrIdList().add(currencyId);
		eaf.getContrDonorNameList().add("");
		eaf.getContrDonorIdList().add(new Long(-1));
		eaf.getContrFinInstrIdList().add(new Long(-1));
		eaf.getContrFinTypeIdList().add(new Long(-1));

		return modeFinalize(mapping, form, 			request, response);
	}

	public ActionForward modeRemoveFields(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EUActivityForm eaf = (EUActivityForm) form;
        if (eaf.getDeleteContrib() != null && eaf.getDeleteContrib().length > 0) {
		for(int i=0;i<eaf.getDeleteContrib().length;i++) {
			eaf.getContrAmountList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			/*eaf.getContrCurrIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrDonorIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrDonorNameList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrFinInstrIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);
			eaf.getContrFinTypeIdList().set(Integer.parseInt(eaf.getDeleteContrib()[i]),null);*/
			
			//eaf.getContrDonorNameList().set(Long.parseLong(eaf.getDeleteContrib().), element)
		}
        }

		for(int i=0;i<eaf.getContrAmountList().size();i++) {
			if(eaf.getContrAmountList().get(i)==null) {
				eaf.getContrAmountList().remove(i);
				eaf.getContrCurrIdList().remove(i);
				eaf.getContrDonorIdList().remove(i);
				eaf.getContrFinInstrIdList().remove(i);
				eaf.getContrFinTypeIdList().remove(i);
				eaf.getContrDonorNameList().remove(i);
			}
		}


		return modeFinalize(mapping, form, request, response);
	}


	public ActionForward modeSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// generate business objects:
		HttpSession httpSess=request.getSession();
		Session sess = PersistenceManager.getRequestDBSession();
		EUActivityForm euaf = (EUActivityForm) form;
		EditActivityForm eaf=(EditActivityForm) httpSess.getAttribute("eaf");
		EUActivity eua =null ;

		eua=new EUActivity();
		if(euaf.getEditingIndexId()!=null)
			eaf.getCosting().getCosts().set(euaf.getEditingIndexId().intValue(),eua);



		eua.setAssumptions(euaf.getAssumptions());
		eua.setDueDate(DateTimeUtil.parseDateForPicker(euaf.getDueDate()));
		eua.setInputs(euaf.getInputs());
		eua.setName(euaf.getName());
		eua.setProgress(euaf.getProgress());
		eua.setTextId(euaf.getTextId());
		eua.setTotalCost(new Double(euaf.getTotalCost()));
		eua.setTotalCostCurrency((AmpCurrency) sess.load(AmpCurrency.class, euaf
				.getTotalCostCurrencyId()));
		eua.setTransactionDate(new Date(System.currentTimeMillis()));

		// create the contribution objects:
		eua.getContributions().clear();
		for (int i = 0; i < euaf.getContrAmountList().size(); i++) {
			Long financingInstrumentId	=  (Long) euaf.getContrFinInstrIdList().get(i) ;
			Long typeOfAssistanceId		= (Long) euaf.getContrFinTypeIdList().get(i) ;

			EUActivityContribution eac=new EUActivityContribution();
			eac.setEuActivity(eua);
			eac.setAmount(new Double((String) euaf.getContrAmountList().get(i)));
			eac.setAmountCurrency((AmpCurrency) sess.load(AmpCurrency.class,new Long((String) euaf.getContrCurrIdList().get(i))));
			eac.setDonor((AmpOrganisation) sess.load(AmpOrganisation.class,new Long((String) euaf.getContrDonorIdList().get(i))));
			//eac.setFinancingInstrument((AmpModality) sess.load(AmpModality.class,new Long((String) euaf.getContrFinInstrIdList().get(i))));
			eac.setFinancingInstr( CategoryManagerUtil.getAmpCategoryValueFromDb(financingInstrumentId) );
			eac.setFinancingTypeCategVal( CategoryManagerUtil.getAmpCategoryValueFromDb(typeOfAssistanceId) );
			eac.setTransactionDate(new Date(System.currentTimeMillis()));

			eua.getContributions().add(eac);
		}


		if(eaf.getCosting().getCosts()==null) eaf.getCosting().setCosts(new ArrayList());
		if(euaf.getEditingIndexId()==null) eaf.getCosting().getCosts().add(eua);

		PersistenceManager.releaseSession(sess);
		request.setAttribute("close", "close");
		return modeFinalize(mapping, form, request, response);
	}

}
