/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.form.NewAddLocationForm;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.util.LocationUtil;

/**
 *
 * @author medea
 */
public class NewLocationManager extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        NewAddLocationForm addRegForm = (NewAddLocationForm) form;
        Collection<AmpCategoryValue> values = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.IMPLEMENTATION_LOCATION_KEY);
        addRegForm.setCategoryValues(new ArrayList(values));
        addRegForm.setLocations(LocationUtil.getAllLocations(addRegForm.getIds()));
        return mapping.findForward("forward");
       
        
    }
}
