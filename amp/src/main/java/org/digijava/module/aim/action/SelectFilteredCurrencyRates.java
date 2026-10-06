/**
 * 
 */
package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFilteredCurrencyRate;
import org.digijava.module.aim.form.SelectFilteredCurrencyRatesForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FilteredCurrencyRateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Alex Gartner
 *
 */
public class SelectFilteredCurrencyRates extends MultiAction {

    public static final String   ACTION_PARAMETER       = "action";
    public static final String   ACTION_SHOW                    = "show";
    public static final String   ACTION_DELETE              = "delete";
    public static final String   ACTION_ADD                     = "add";
    
    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // TODO Auto-generated method stub
        return modeSelect(mapping, form, request, response);
    }

    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ActionMessages errors                                           = new ActionMessages();
        String action       = request.getParameter(ACTION_PARAMETER);
        
        if ( action == null || ACTION_SHOW.equals(action) ) {
            return modeShow(mapping, form, request, response, errors);
        }
            
        if ( ACTION_ADD.equals(action) ) {
            return modeAdd(mapping, form, request, response, errors);
        }
        
        if ( ACTION_DELETE.equals(action) ) {
            return modeDelete(mapping, form, request, response, errors);
        }
        
        return null;
    }
    public ActionForward modeShow(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response, ActionMessages errors)
            throws Exception {
        
        SelectFilteredCurrencyRatesForm myForm          = (SelectFilteredCurrencyRatesForm) form;
        Collection<AmpCurrency> existingCurrencies  = CurrencyUtil.getAllCurrencies( CurrencyUtil.ALL_ACTIVE );
        
        if ( existingCurrencies != null ) {
            myForm.setExistingCurrencies( new ArrayList<KeyValue>(existingCurrencies.size()) );
            for ( AmpCurrency curr: existingCurrencies ) {
                KeyValue kv     = new KeyValue(curr.getCurrencyCode(), curr.getCurrencyCode());
                myForm.getExistingCurrencies().add(kv);
            }
        }
        FilteredCurrencyRateUtil filteredCurrencyRateUtil                               = new FilteredCurrencyRateUtil();
        List<AmpFilteredCurrencyRate> existingFilteredRates         = filteredCurrencyRateUtil.getAllFilteredCurrencyRates();
        myForm.setExistingFilteredRates( existingFilteredRates );
        
        this.saveErrors(request, errors);
        
        return mapping.findForward("forward");
    }
    
    public ActionForward modeAdd(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response,  ActionMessages errors)
            throws Exception {
        
        SelectFilteredCurrencyRatesForm myForm                      = (SelectFilteredCurrencyRatesForm) form;
        FilteredCurrencyRateUtil filteredCurrencyRateUtil       = new FilteredCurrencyRateUtil();
        
        
        AmpCurrency toCurrency          = CurrencyUtil.getAmpcurrency( myForm.getToCurrencyCode().trim() );
        AmpCurrency fromCurrency        = CurrencyUtil.getAmpcurrency( myForm.getFromCurrencyCode().trim() );
        
        if ( toCurrency == null ) {
            errors.add(
                    "title", new ActionMessage("error.aim.addFilteredRate.cannotAddFilteredRate", myForm.getToCurrencyCode() )
            );
        }
        else if ( fromCurrency == null ) {
            errors.add(
                    "title", new ActionMessage("error.aim.addFilteredRate.cannotAddFilteredRate", myForm.getFromCurrencyCode() )
            );
        }
        else if ( filteredCurrencyRateUtil.checkPairExistance(myForm.getToCurrencyCode().trim(), myForm.getFromCurrencyCode().trim()) || 
                filteredCurrencyRateUtil.checkPairExistance(myForm.getFromCurrencyCode().trim(), myForm.getToCurrencyCode().trim()) ) {
            
            errors.add(
                    "title", new ActionMessage("error.aim.addFilteredRate.selectionExists")
            );
        }
        else {
            AmpFilteredCurrencyRate f   = new  AmpFilteredCurrencyRate();
            f.setToCurrency(toCurrency);
            f.setFromCurrency(fromCurrency);
            
            filteredCurrencyRateUtil.addFilteredCurrencyRate(f);
        }
        
        return modeShow(mapping, form, request, response, errors);
    }
    
    public ActionForward modeDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response, ActionMessages errors)
            throws Exception {
        
        SelectFilteredCurrencyRatesForm myForm          = (SelectFilteredCurrencyRatesForm) form;
        if ( myForm.getSelectedFilteredRates() != null && myForm.getSelectedFilteredRates().length > 0 ) {
            FilteredCurrencyRateUtil filteredCurrencyRateUtil                               = new FilteredCurrencyRateUtil();
            for (int i=0; i<myForm.getSelectedFilteredRates().length; i++) {
                filteredCurrencyRateUtil.deleteFilteredCurrencyRate( myForm.getSelectedFilteredRates()[i] );
            }
            
        }
        
        return modeShow(mapping, form, request, response, errors);
    }
}
