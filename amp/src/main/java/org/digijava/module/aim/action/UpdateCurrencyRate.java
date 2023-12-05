/*
 * UpdateCurrencyRate.java
 * Created : 03-May-2005
 */
package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.form.CurrencyRateForm;
import org.digijava.module.aim.helper.CurrencyRates;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;


public class UpdateCurrencyRate extends Action {

    private static final long SEVEN_DAYS = 604800000; // in miliseconds
    // 7 * 24 * 60 * 60 * 1000

    private static Logger logger = Logger.getLogger(UpdateCurrencyRate.class);

    /**
     * VERY BASIC SANITY CHECK. DO NOT USE AS AN AUTHORITATIVE FUNCTION FOR TAKING THE DECISION
     * @param dateStr
     * @return
     */
    public static String malawi_date_swap(String dateStr)
    {
        StringTokenizer tok = new StringTokenizer(dateStr, "/");
        if (tok.countTokens() != 3)
            return dateStr;
        
        String month = tok.nextToken();
        String day = tok.nextToken();
        String year = tok.nextToken();
        return day + "/" + month + "/" + year;
    }
    
    /**
     * VERY BASIC SANITY CHECK. DO NOT USE AS AN AUTHORITATIVE FUNCTION FOR TAKING THE DECISION
     * @param dateStr
     * @return
     */
    public static boolean is_valid_date(String dateStr)
    {
        StringTokenizer tok = new StringTokenizer(dateStr, "/");
        if (tok.countTokens() != 3)
            return false;
        try
        {
            String day = tok.nextToken();
            String month = tok.nextToken();
            String year = tok.nextToken();
            Integer yearNr = Integer.parseInt(year);
            Integer monthNr = Integer.parseInt(month);
            Integer dayNr = Integer.parseInt(day);
            Date date = new Date(yearNr - 1900, monthNr - 1, dayNr);
            
            if (dayNr <= 0 || dayNr > 31)
                return false;
            if (monthNr <= 0 || monthNr > 12)
                return false;
            if (yearNr <= 1960 || yearNr >= 3000)
                return false; // hopefully this code will not be running 987 years from now
            if (date.getDate() != dayNr)
                return false;
            if (date.getMonth() != monthNr - 1)
                return false;
            if (date.getYear() != yearNr - 1900)
                return false;
            
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        CurrencyRateForm crForm = (CurrencyRateForm) form;
        if ("".equals(crForm.getDoAction())) {
            crForm = populateFromRequest (crForm,request);
        }
        CurrencyRates currencyRates = null;
        Collection<CurrencyRates> col = new ArrayList<CurrencyRates>();
        
        String baseCurrency             = FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.BASE_CURRENCY );
        if ( baseCurrency == null )
          baseCurrency          = "USD";

        logger.debug("Reset :" + crForm.isReset());

        /*
         *  Starts here
         *  Reads the data from the uploaded CSV file
         *  the data should be in the following format
         *  the 3-letter Currency code,the exchange rate,
         *  the date of the exchange rate in dd/mm/yyyy format
         *  all these three data in a single line - example :
         *  JPY,105.0,10/07/2006
         *  CAD,1.1264,10/07/2006
         *  ....goes on
         */
        
            
                if (crForm.getDoAction() == null ||
                        crForm.getDoAction().equals("file"))
                {
                try {
                            FormFile f = crForm.getCurrRateFile();
                            InputStream is = f.getInputStream();
                            BufferedReader in = new BufferedReader(new InputStreamReader(is));
                            String line = null;
                            StringTokenizer st = null;
                            
                            Set<String> allCurrencyCodes = new HashSet<String>(); // set of all currency codes in the to-be-imported file, to be checked against the existing currencies
                            
                            while ((line = in.readLine()) != null)
                            {
                                String separator=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.EXCHANGE_RATE_SEPARATOR);
                                if(separator==null || "".compareTo(separator)==0)
                                    st = new StringTokenizer(line,",");
                                else st = new StringTokenizer(line, separator);
                                
                                if(st.countTokens()==3)
                                {
                                    String code = st.nextToken().trim();
                                    String rateToken = st.nextToken().trim();
                                    Double rate = FormatHelper.parseDouble(rateToken);
                                    if (rate == null)
                                        throw new RuntimeException("could not parse rate; the erroneous token is: " + rateToken);
                                    String date = st.nextToken().trim();
                                    //date = malawi_date_swap(date);
                                    if (!is_valid_date(date))
                                        throw new RuntimeException("invalid date, please use a dd/mm/yyyy format: " + date);
                                    
                                    // to avoid "amp_currency_rate_check" constraint violation in DB
                                    if(!baseCurrency.equalsIgnoreCase(code)) {
                                        currencyRates = new CurrencyRates();
                                        currencyRates.setCurrencyCode(code);
                                        currencyRates.setExchangeRate(new Double(rate));
                                        //DateTimeUtil.parseDate(date).toString();
                                        currencyRates.setExchangeRateDate(date);
                                        allCurrencyCodes.add(currencyRates.getCurrencyCode());
                                        col.add(currencyRates);
                                    }
                                } 
                            }
                            
                            Set<String> allExistingActiveCurrencies = new HashSet<String>();
                            for(AmpCurrency currency:CurrencyUtil.getAllCurrencies(1))
                                allExistingActiveCurrencies.add(currency.getCurrencyCode());
                                
                            for(String currencyCode:allCurrencyCodes)
                                if (!allExistingActiveCurrencies.contains(currencyCode))
                                    throw new RuntimeException("Currency code does not exist in the active currencies: " + currencyCode + "; please configure a currency with this code first and then import the file OR please check the currency rates file");
                            CurrencyUtil.saveCurrencyRates(col, baseCurrency);
                
                            Date toDate = DateConversion.getDate(crForm.getFilterByDateFrom());
                            long stDt = toDate.getTime();
                            stDt -= SEVEN_DAYS;
                            Date fromDate = new Date(stDt);
                            crForm.setAllRates(CurrencyUtil.getActiveCurrencyRates(fromDate, toDate));
        
                    //return mapping.findForward("fileload");
                    } catch (Exception e) {
                                // TODO: handle exception
                        ActionMessages errors = new ActionMessages();
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                                "error.aim.uploadCurrencyRates.fileCorrupted", e.getMessage()));
                        HttpSession httpSession = request.getSession();
                        httpSession.setAttribute("CurrencyRateFileUploadError", errors);
                        //saveErrors(request, errors);
                        //e.printStackTrace();
                        }
                }
                  /*
                   *    Ends here
                   */
                  else{
                    if (crForm.getDoAction() == null ||
                        crForm.getDoAction().equals("showRates")) {
                      if (crForm.getUpdateCRateCode() != null &&
                          crForm.getUpdateCRateDate() != null) {
                        Date date = DateConversion.getDate(crForm.getUpdateCRateDate());
                        Double rate=Util.getExchange(crForm.getUpdateCRateCode(),
                                new java.sql.Date(date.getTime()));
                       
                        String amount="0";
                        if((rate!=null)&&(rate!=0)){
                            //amount=FormatHelper.formatNumber(1/rate);
                            amount=FormatHelper.formatNumberNotRounded(rate);
                        }
                        crForm.setUpdateCRateAmount(amount);
                        crForm.setReset(false);
                      }
                      else {
                        crForm.setUpdateCRateAmount(null);
                      }
                      if (crForm.getCurrencyCodes() == null) {
                        crForm.setCurrencyCodes(CurrencyUtil.getActiveAmpCurrencyByName());
                        crForm.setUpdateCRateCode(null);
                        crForm.setUpdateCRateDate(null);
                        crForm.setUpdateCRateId(null);
                      }

                    }
                    else {
                        AmpCurrencyRate cRate = new AmpCurrencyRate();
                        if (crForm.getUpdateCRateAmount() != null) {
                        //String amountRate=DecimalToText.removeCommas(crForm.getUpdateCRateAmount());
                          //Double rate = new Double(Double.parseDouble(crForm.getUpdateCRateAmount()));
                     //AMP-2600: not use removeCommas because we can use comma as decimal separator
                        Double amountRate=FormatHelper.parseDouble(crForm.getUpdateCRateAmount());
                        //Double rate = new Double(Double.parseDouble(amountRate));
                        Double rate= 0d;
                        if (amountRate!=null){
                            //rate= new Double(1/amountRate);
                            rate= new Double(amountRate);
                        }
                      
                          cRate.setExchangeRate(rate);
                        }
                        if (crForm.getUpdateCRateDate() != null) {
                         cRate.setExchangeRateDate(DateTimeUtil.parseDate(crForm.getUpdateCRateDate())); 
                          //cRate.setExchangeRateDate(DateConversion.getDate(crForm.getUpdateCRateDate()));
                        }

                        cRate.setToCurrencyCode(crForm.getUpdateCRateCode());
                        cRate.setFromCurrencyCode(baseCurrency);
                        cRate.setDataSource(CurrencyUtil.RATE_BY_HAND);
                        if (crForm.getDoAction().equals("validateRateExistance")) {
                            
                            if (!CurrencyUtil.isCurrencyRateInDatabase(cRate)) {
                                crForm.setDoAction("saveRate");
                                saveRate (cRate,crForm);
                                writeNonCacheAbleResponse (response,"false");
                                
                            }
                            else {
                                writeNonCacheAbleResponse (response,"true");
                            }
                            return null;
                        }
                        else {
                            saveRate (cRate,crForm);
                        }
                    }
                  }
        return mapping.findForward("forward");
    }
    
    private void saveRate (AmpCurrencyRate cRate,CurrencyRateForm crForm) throws Exception {
        if(cRate.getExchangeRate()!=null && cRate.getExchangeRateDate()!=null && crForm.getDoAction().equalsIgnoreCase("saveRate"))
          CurrencyUtil.saveCurrencyRate(cRate,false);
        else 
          logger.warn("Either exchange rate or exchange rate date is null");
        AbstractCache ratesCache = DigiCacheManager.getInstance().getCache(ArConstants.EXCHANGE_RATES_CACHE);
        ratesCache.clear();

        crForm.setAllRates(null);
        crForm.setUpdateCRateAmount(null);
        crForm.setUpdateCRateCode(null);
        crForm.setUpdateCRateDate(null);
        crForm.setUpdateCRateId(null);
        crForm.setDoAction(null);

    }
    
    private CurrencyRateForm populateFromRequest (CurrencyRateForm form, HttpServletRequest request) {
        form.setDoAction(request.getParameter("doAction"));
        form.setUpdateCRateCode(request.getParameter("updateCRateCode"));
        form.setUpdateCRateAmount(request.getParameter("updateCRateAmount"));
        form.setUpdateCRateDate(request.getParameter("updateCRateDate"));
        return form;
    }
    
    private void writeNonCacheAbleResponse (HttpServletResponse response, String responseBody) throws IOException {
        response.setHeader("Cache-Control","no-cache");
        PrintWriter out = response.getWriter();     
        out.println(responseBody);
        out.flush();
        out.close();
        
    }
    

}
