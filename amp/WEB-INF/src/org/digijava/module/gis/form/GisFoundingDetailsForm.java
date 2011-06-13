package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.util.GisFilterForm;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class GisFoundingDetailsForm extends ActionForm {
    public GisFoundingDetailsForm() {
    }

    private GisFilterForm gisFilterForm;

    public GisFilterForm getGisFilterForm() {
        return gisFilterForm;
    }

    public void setGisFilterForm(GisFilterForm gisFilterForm) {
        this.gisFilterForm = gisFilterForm;
    }
}
