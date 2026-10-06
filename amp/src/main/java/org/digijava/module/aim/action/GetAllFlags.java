package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

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
