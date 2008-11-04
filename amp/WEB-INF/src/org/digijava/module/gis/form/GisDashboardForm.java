package org.digijava.module.gis.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * Form for GIS Dashboard actions.
 * @author Irakli Kobiashvili
 *
 */
public class GisDashboardForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private Collection sectorCollection;
    private List availYears;

    public Collection getSectorCollection() {
        return sectorCollection;
    }

    public List getAvailYears() {
        return availYears;
    }

    public void setSectorCollection(Collection sectorCollection) {
        this.sectorCollection = sectorCollection;
    }

    public void setAvailYears(List availYears) {
        this.availYears = availYears;
    }
}
