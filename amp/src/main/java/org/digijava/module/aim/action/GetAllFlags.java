package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.util.FeaturesUtil;

public class GetAllFlags extends Action {

    private static Logger logger = Logger.getLogger(GetAllFlags.class);

    public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        ActionErrors errors = (ActionErrors)request.getSession().getAttribute("uploadFlagErrors");
        if(errors!=null){
            saveErrors(request, errors);
            request.getSession().removeAttribute("uploadFlagErrors");
        }
        
        FlagUploaderForm fuForm = (FlagUploaderForm) form;
        fuForm.setCntryFlags(FeaturesUtil.getAllCountryFlags());
        Collection<CountryBean> countries = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);

        fuForm.setCountries(countries);

        return mapping.findForward("forward");
    }

}
