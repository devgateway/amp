package org.digijava.module.aim.action;
/*
* @ author Govind G Dalwani
*/
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
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
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.currency.inflation.CCExchangeRate;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.menu.MenuStructure;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.GlobalSettingsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.services.auditcleaner.AuditCleaner;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.currencyrates.CurrencyRatesService;

public class GlobalSettings extends Action {
    private static Logger logger                = Logger.getLogger(GlobalSettings.class);
    private ActionMessages errors                   = new ActionMessages();

    private void flushSessionObjects(HttpSession session) {
        // probably should flush all RCD from all sessions
        //session.removeAttribute(ArConstants.REPORTS_Z_FILTER);
        // TODO-CONSTANTIN should we:
        // 1) clean data for ALL sessions? (correct but disruptive) 
        // 2) clean data for current session? (kinda useless for an admin)
        // 3) do nothing?
        ReportContextData.clearSession();
    }
    
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        boolean refreshGlobalSettingsCache          = false;
        boolean regenerateCCExchanteRates = false;
        HttpSession session = request.getSession();
        errors = new ActionMessages();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                    return mapping.findForward("index");
                }
            }
        
        GlobalSettingsForm gsForm = (GlobalSettingsForm) form;
        if(request.getParameter("save")!=null){
            String save = request.getParameter("save");
            logger.info(" this is the action "+save);
            flushSessionObjects(session);
    
            logger.info(" id is "+gsForm.getGlobalId()+"   name is "+gsForm.getGlobalSettingsName()+ "  value is... "+gsForm.getGsfValue());
            dailyCurrencyRatesChanges(gsForm);
            try {
                DbUtil.updateGlobalSetting(gsForm.getGlobalId(), gsForm.getGsfValue());
            } catch (AMPException ex) {
                ActionMessage ae = new ActionMessage("error.aim.globalSettings.valueIsNotOfType", ex.getMessage());
                errors.add("title", ae);
            }
            auditTrialCleanerChanges(gsForm);
            refreshGlobalSettingsCache  = true;         
        }
        
        
        if (request.getParameter("saveAll") != null) {
            flushSessionObjects(session);
            String allValues = gsForm.getAllValues();
            StringTokenizer token = new StringTokenizer(allValues, "&");
            AmpGlobalSettings projectValidationSetting = FeaturesUtil.getGlobalSettingsCache().get(GlobalSettingsConstants.PROJECTS_VALIDATION);
            AmpGlobalSettings baseCurrencyGS = FeaturesUtil.getGlobalSettingsCache().get(GlobalSettingsConstants.BASE_CURRENCY);
            while (token.hasMoreTokens()) {
                String element = URLDecoder.decode(token.nextToken(), "UTF-8");
                String[] nameValue = element.split("=");                
                Long id = getLongOrNull(nameValue[0]);
                String newValue = nameValue.length < 2 ? "" : nameValue[1];
                if (projectValidationSetting.getGlobalId().equals(id) && !newValue.equals(projectValidationSetting.getGlobalSettingsValue())) {
                    resetWorkspaceValidationSettings(newValue);
                }
                if (baseCurrencyGS.getGlobalId().equals(id) && !newValue.equals(baseCurrencyGS.getGlobalSettingsValue())) {
                    regenerateCCExchanteRates = true;
                }
                // allow empty fields, like Public Portal URL when Public Portal = false
                //we ad a struts error that was added befor inside the methods
                try {

                    DbUtil.updateGlobalSetting(id, newValue);

                } catch (AMPException ex) {

                    ActionMessage ae = new ActionMessage("error.aim.globalSettings.valueIsNotOfType", ex.getMessage());
                    errors.add("title", ae);
                }
            }
            
            //this.updateGlobalSetting(gsForm.getGlobalId(), gsForm.getGsfValue());
            //ActionMessages errors = new ActionMessages();
            refreshGlobalSettingsCache  = true;
            dailyCurrencyRatesChanges(null);
            auditTrialCleanerChanges();
            DigiCacheManager.getInstance().getCache(ArConstants.EXCHANGE_RATES_CACHE).clear();
        }
        
        List<AmpGlobalSettings> col = FeaturesUtil.getGlobalSettings();
        if (refreshGlobalSettingsCache) {
            FeaturesUtil.buildGlobalSettingsCache(col);
            //FeaturesUtil.logGlobalSettingsCache();
            org.digijava.module.aim.helper.GlobalSettings globalSettings = (org.digijava.module.aim.helper.GlobalSettings) getServlet().getServletContext().getAttribute(Constants.GLOBAL_SETTINGS);
            globalSettings.setShowComponentFundingByYear(FeaturesUtil.isShowComponentFundingByYear());
            FeaturesUtil.switchLogicInstance();         
            
            ServletContext ampContext = this.getServlet().getServletContext();
            
            AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
            AmpTemplatesVisibility currentTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();
            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
            FeaturesUtil.setAmpTreeVisibility(ampContext, session,ampTreeVisibility);
            
            // update menu if support email changed
            MenuStructure.recreate();
        }
        gsForm.setGsfCol(col);
        for (AmpGlobalSettings ampGS:col)
        {
            //TODO: Add a new field to identify fields that need multiselect activated.
            if ((ampGS.getGlobalSettingsValue() != null) && (ampGS.getGlobalSettingsValue().indexOf(";") != -1))
            {
                ampGS.setListOfValues(ampGS.getGlobalSettingsValue().split(";"));
            }
            
            gsForm.setGlobalSettingType(ampGS.getGlobalSettingsName(), ampGS.getGlobalSettingsPossibleValues());
            
            /**
             *  Getting the name of the criteria for possible values:
             *  if v_view_name => the values are taken from the specified view
             *  if t_type => the value is checked to be of the specified type
             */
            String possibleValuesTable      = ampGS.getGlobalSettingsPossibleValues();
            Collection<KeyValue> possibleValues     = null;
            Map<String, String> possibleValuesDictionary    = null;
            if ( possibleValuesTable != null && possibleValuesTable.length() != 0 && possibleValuesTable.startsWith("v_") ) {
                possibleValues              = DbUtil.getPossibleValues(possibleValuesTable);
                possibleValuesDictionary    = new HashMap<String, String>();
                for(KeyValue keyValue:possibleValues){
                    possibleValuesDictionary.put(keyValue.getKey(), keyValue.getValue());
                }
            }
            gsForm.setPossibleValues( ampGS.getGlobalSettingsName(), possibleValues );
            gsForm.setPossibleValuesDictionary( ampGS.getGlobalSettingsName(), possibleValuesDictionary );
        }
        Collection<CountryBean> countries = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);
        gsForm.setCountryNameCol(countries);
        
        if (regenerateCCExchanteRates)
            CCExchangeRate.regenerateConstantCurrenciesExchangeRates(false);

        saveErrors(request, errors);
        return mapping.findForward("viewGS");
    }
    
    /**
     * updates workspaces's validation depending whether the GlobalSettings has changed to "off" or  "on"
     * @param gsNewValue
     */
    private void resetWorkspaceValidationSettings(String gsNewValue) {
        String query ="UPDATE " + AmpApplicationSettings.class.getName();
        if (gsNewValue.toLowerCase().equals("on")) {
            query += " SET validation='" + Constants.PROJECT_VALIDATION_FOR_ALL_EDITS
                    + "' WHERE validation='" + Constants.PROJECT_VALIDATION_OFF + "'";
        } 
        else if (gsNewValue.toLowerCase().equals("off")) {
            query += " SET validation='" + Constants.PROJECT_VALIDATION_OFF + "'";
        }
        PersistenceManager.getSession()
            .createQuery(query)
            .executeUpdate();
    }
    
    /**
     * parses a String as a Long. Returns null in case it fails to do so
     * @param s
     * @return
     */
    private static Long getLongOrNull(String s) {
        try {
            return Long.parseLong(s);
        }
        catch(Exception e) {return null;}
    }

    @SuppressWarnings("unchecked")
    private void dailyCurrencyRatesChanges() {
        
        String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_ENALBLED);
        boolean update = (value.compareToIgnoreCase("On") == 0);
        
        String hour = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_HOUR);
        String timeout = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_TIMEOUT);

        if(update){
            CurrencyRatesService.startCurrencyRatesService(hour, timeout);
        }
        else{
            CurrencyRatesService.stopCurrencyRatesService();
        }
    }
    
    /**
     * 
     */
    private void auditTrialCleanerChanges() {
        String value = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTOMATIC_AUDIT_LOGGER_CLEANUP);
        if ("-1".equalsIgnoreCase(value)) {
            if ( AuditCleaner.getInstance().isRunning()){
                AuditCleaner.getInstance().stop();
            }
        } else {
            if (!AuditCleaner.getInstance().isRunning()) {
                AuditCleaner.getInstance().start();
            }
        }
    }
    
    /**
     * 
     * @param gsForm
     */
    private void auditTrialCleanerChanges(GlobalSettingsForm gsForm){
        if (gsForm.getGlobalSettingsName().compareTo(GlobalSettingsConstants.AUTOMATIC_AUDIT_LOGGER_CLEANUP) ==0){
            if ("-1".equalsIgnoreCase(gsForm.getGsfValue())){
                if (AuditCleaner.getInstance().isRunning()){
                    AuditCleaner.getInstance().stop();
                }
            }else{
                if(!AuditCleaner.getInstance().isRunning()){
                    AuditCleaner.getInstance().start();
                }
            }
        }
    }
    
    /**
     * @param gsForm
     */
    @SuppressWarnings("unchecked")
    private void dailyCurrencyRatesChanges(GlobalSettingsForm gsForm) {
        if(gsForm==null){
            dailyCurrencyRatesChanges();
        }
        else{
            if(gsForm.getGlobalSettingsName().compareTo(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_ENALBLED)==0){
                if(gsForm.getGsfValue().compareTo("On")==0){
                    Collection<AmpGlobalSettings> ampGSCollection = gsForm.getGsfCol();
                    String hour=null;
                    String timeout=null;
                    for(AmpGlobalSettings ampGS : ampGSCollection)
                    {
                        
                        if(ampGS.getGlobalSettingsName().compareTo(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_HOUR)==0){
                            hour=ampGS.getGlobalSettingsValue();
                        }
                        if(ampGS.getGlobalSettingsName().compareTo(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_TIMEOUT)==0){
                            timeout=ampGS.getGlobalSettingsValue();
                        }
                    }
                    CurrencyRatesService.startCurrencyRatesService(hour, timeout );
                    
                }else{
                    CurrencyRatesService.stopCurrencyRatesService();
                }
            }
            if(gsForm.getGlobalSettingsName().compareTo(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_HOUR)==0){
                Collection<AmpGlobalSettings> ampGSCollection = gsForm.getGsfCol();
                for(AmpGlobalSettings ampGS : ampGSCollection)
                {
                    int val = ampGS.getGlobalSettingsName().compareTo(GlobalSettingsConstants.DAILY_CURRENCY_RATES_UPDATE_ENALBLED);
                    if(val==0){
                        if(ampGS.getGlobalSettingsValue().compareTo("On")==0){
                            CurrencyRatesService.startCurrencyRatesService(gsForm.getGsfValue(),"9");
                        }
                    }
                }
            }
        }
    }
    



    private List<KeyValue> getPossibleValues(String tableName) {
        List<KeyValue> ret = new ArrayList<>();

        if (tableName == null || tableName.length() == 0)
            return ret;

        List<Object[]> ls   = PersistenceManager.getSession().createSQLQuery("select id, value from " + tableName).list();
        for(Object[] obj:ls){
            KeyValue keyValue = new KeyValue(PersistenceManager.getString(obj[0]), PersistenceManager.getString(obj[1]));
            ret.add( keyValue );
        }
        return ret;
    }

    /**
     * This method is potentially dangerous since does not consider leap years
     * Do not use java.util.Calendar since it will be much slower
     * @param month number of month starting from 1
     * @return
     */
    public static int numOfDaysInMonth(int month) {
        return numOfDaysInMonth(month, -1);
    }

    /**
     * Do not use java.util.Calendar since it will be much slower
     * @param month number of month starting from 1
     * @param year the year (leap year matters)
     * @return
     */
    public static int numOfDaysInMonth(int month, int year) {
        if (month > 0 && month < 13) {
            int maxDays = 0;
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    maxDays = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11: 
                    maxDays = 30;
                    break;
                case 2:
                    if (year > 0) {
                        maxDays = year % 4 == 0 ? 29 : 28;
                    } else {
                        maxDays = 28;
                    }
                    break;
            }
            return maxDays;
        }
        return 0;
    }
}
