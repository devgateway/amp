package org.digijava.module.gis.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * Form for GIS Dashboard actions.
 * @author Irakli Kobiashvili
 *
 */
public class GisDashboardForm extends ActionForm {

	private static final long serialVersionUID = 1L;

        private Collection sectorCollection;
    public Collection getSectorCollection() {
        return sectorCollection;
    }

    public void setSectorCollection(Collection sectorCollection) {
        this.sectorCollection = sectorCollection;
    }
}
