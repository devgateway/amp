package org.digijava.module.gis.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

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
public class GisDemoForm extends ActionForm {
    public GisDemoForm() {
    }

    public Collection getSectorCollection() {
        return sectorCollection;
    }

    public void setSectorCollection(Collection sectorCollection) {
        this.sectorCollection = sectorCollection;
    }

    private Collection sectorCollection;
}
