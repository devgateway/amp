/*
 * ShowCurrencyRates.java
 * Created: 01-May-2005
 */
package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.form.CurrencyRateForm;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.currencyrates.CurrencyRatesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class ShowCurrencyRates extends Action {
    
    private static final long SEVEN_DAYS = 604800000; // in miliseconds
    // 7 * 24 * 60 * 60 * 1000
    
    private static final int ABSOLUTELY_ALL_ACTIVE_RATES = -1;  

    private static Logger logger = Logger.getLogger(ShowCurrencyRates.class);

    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        CurrencyRateForm crForm = (CurrencyRateForm) form;
                Boolean isFromAdminHome=crForm.isClean();
        HttpSession httpSession = request.getSession();
        
        ActionMessages errors = null;
        errors = (ActionMessages) httpSession.getAttribute("CurrencyRateFileUploadError");
        if(errors!=null) {
            saveErrors(request, errors);
            httpSession.setAttribute("CurrencyRateFileUploadError",null);
        }
        
        String baseCurrency             = FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY );
        if ( baseCurrency == null )
          baseCurrency          = "USD";

        try {

        if (crForm.getDoAction() != null &&
                crForm.getDoAction().equals("delete")) {
            CurrencyUtil.deleteCurrencyRates(crForm.getSelectedRates());
            AbstractCache ratesCache = DigiCacheManager.getInstance().getCache(ArConstants.EXCHANGE_RATES_CACHE);
            ratesCache.clear();
            crForm.setAllRates(null);
            crForm.setDoAction("");
        }

        if (crForm.getDoAction() != null &&
                crForm.getDoAction().equals("loadRates")) {
            if (crForm.getRatesFile() != null &&
                    crForm.getRatesFile().length() > 0) {
                logger.info("File name = " + crForm.getRatesFile());
                Collection currRates = CurrencyRateLoader.getCurrencyRates(
                        crForm.getRatesFile());
                CurrencyUtil.saveCurrencyRates(currRates, baseCurrency);
                crForm.setDoAction("");
                crForm.setAllRates(null);
            }
        }

        int page = 1;
        String temp = request.getParameter("page");
        if (temp != null) {
            page = Integer.parseInt(temp.trim());
        }



        if (crForm.getFilterByDateFrom() == null
                || crForm.getFilterByDateFrom().trim().length() == 0||(isFromAdminHome!=null && isFromAdminHome)) {
//          crForm.setFilterByDateFrom(Constants.CURRENCY_RATE_DEAFULT_END_DATE);//AMP-1421
            crForm.setFilterByDateFrom(DateTimeUtil.formatDate(new Date()));
                        crForm.setClean(false);
        }



            crForm.setPrevFromDate(crForm.getFilterByDateFrom());
            Date toDate = DateConversion.getDate(crForm.getFilterByDateFrom());

            Calendar cal=Calendar.getInstance();
            cal.setTime(toDate);
            int timePeriod=crForm.getTimePeriod();
            if(timePeriod==ShowCurrencyRates.ABSOLUTELY_ALL_ACTIVE_RATES){
                crForm.setAllRates(CurrencyUtil.getAllCurrencyRates());
            }
            else{
               switch (timePeriod) {
               case 1:  cal.add(Calendar.DATE,-7);    break;
               case 2:  cal.add(Calendar.DATE, -14);  break;
               case 3:  cal.add(Calendar.MONTH,-1);   break;
               case 4:  cal.add(Calendar.MONTH, -4);  break;
               case 5:  cal.add(Calendar.YEAR, -1);   break;
               default:break;
               }
               Date fromDate=cal.getTime();
               crForm.setAllRates(CurrencyUtil.getActiveCurrencyRates(fromDate, toDate));
            }

        ArrayList tempList = new ArrayList();
        Iterator itr = null;

        boolean filtered = false;
        if (crForm.getFilterByCurrCode() != null &&
                crForm.getFilterByCurrCode().trim().length() > 0)  {
            logger.debug("Filtering based on currency code ....");
            itr = crForm.getAllRates().iterator();
            CurrencyRates cRates = null;
            while (itr.hasNext()) {
                cRates = (CurrencyRates) itr.next();
                if (cRates.getCurrencyCode().equals(
                        crForm.getFilterByCurrCode())) {
                    tempList.add(cRates);
                }
            }
            filtered = true;
        }

        if (!filtered) {
            tempList = new ArrayList(crForm.getAllRates());
        }

        if (crForm.getNumResultsPerPage() == 0) {
            crForm.setNumResultsPerPage(Constants.NUM_RECORDS);
        }

        if(tempList.size() > 0) {
            int numPages = tempList.size() / crForm.getNumResultsPerPage();
            numPages += (tempList.size() % crForm.getNumResultsPerPage() != 0) ? 1 : 0;
            if(page > numPages)
                page = numPages;

            int stIndex = (page - 1) * crForm.getNumResultsPerPage();
            int edIndex = page * crForm.getNumResultsPerPage();
            if(edIndex > tempList.size()) {
                edIndex = tempList.size();
            }
            Collection pages = null;
            if(numPages > 1) {
                pages = new ArrayList();
                for(int i = 0; i < numPages; i++) {
                    Integer pageNum = new Integer(i + 1);
                    pages.add(pageNum);
                }
            }
            crForm.setPages(pages);

            crForm.setCurrencyRates(new ArrayList());
            for(int i = stIndex; i < edIndex; i++) {
                crForm.getCurrencyRates().add(tempList.get(i));
            }
        } else {
            crForm.setCurrencyRates(null);
            crForm.setPages(null);
        }

        crForm.setCurrentPage(new Integer(page));       
        crForm.setCurrencyCodes(CurrencyUtil.getActiveAmpCurrencyByName(true));
        crForm.setTimePeriods(getTimePeriods());
        crForm.setUpdateCRateAmount(null);
        crForm.setUpdateCRateCode(null);
        crForm.setUpdateCRateDate(null);
        crForm.setUpdateCRateId(null);

        } catch (Exception e) {
            logger.error("Exception " + e);
            e.printStackTrace(System.out);
        }
        String lastUpdate = CurrencyRatesService.getStringLastTimeUpdate(CurrencyUtil.RATE_FROM_WEB_SERVICE);
        crForm.setLastRateUpdate(lastUpdate);
        return mapping.findForward("forward");
    }
    
    private List<LabelValueBean>  getTimePeriods(){
        List<LabelValueBean> timePeriods=new ArrayList<LabelValueBean>();
        timePeriods.add(new LabelValueBean("7 Days","1"));
        timePeriods.add(new LabelValueBean("2 weeks","2"));
        timePeriods.add(new LabelValueBean("Month","3"));
        timePeriods.add(new LabelValueBean("Quarter","4"));
        timePeriods.add(new LabelValueBean("Year","5"));
        timePeriods.add(new LabelValueBean("ALL",String.valueOf(ShowCurrencyRates.ABSOLUTELY_ALL_ACTIVE_RATES)));       
        return timePeriods;
    }

}

