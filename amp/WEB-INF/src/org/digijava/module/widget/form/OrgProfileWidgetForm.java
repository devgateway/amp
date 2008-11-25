
package org.digijava.module.widget.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;

/**
 *
 * @author medea
 */
public class OrgProfileWidgetForm extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    
    private List<AmpWidgetOrgProfile> orgProfilePages;
    private Long type;
    private Long id;
    private Long[] selPlaces;
    private List<AmpDaWidgetPlace>places;

    public Long[] getSelPlaces() {
        return selPlaces;
    }

    public void setSelPlaces(Long[] selPlaces) {
        this.selPlaces = selPlaces;
    }


    public List<AmpDaWidgetPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<AmpDaWidgetPlace> places) {
        this.places = places;
    }
 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public List<AmpWidgetOrgProfile> getOrgProfilePages() {
        return orgProfilePages;
    }

    public void setOrgProfilePages(List<AmpWidgetOrgProfile> orgProfilePages) {
        this.orgProfilePages = orgProfilePages;
    }

}
