package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;

/**
 * 
 * @author aartimon@developmentgateway.org
 * @since Nov 14, 2012
 *
 */

public class AutocompleteAcronymTitleModel extends Model<String>{
    private static final long serialVersionUID = 1L;
    private IModel<String> acronymModel;
    private IModel<String> titleModel;
    private String nullText;
    
    public AutocompleteAcronymTitleModel(IModel<String> acronymModel, IModel<String> titleModel, String nullText) {
        this.acronymModel = acronymModel;
        this.titleModel = titleModel;
        this.nullText = nullText;
    }
    
    public String getObject() {
        String ret = "";
        if ((acronymModel == null || acronymModel.getObject() == null) && titleModel.getObject() == null) {
            return nullText;
        }
        
        if (acronymModel != null){
            ret = ret + AmpAutocompleteFieldPanel.ACRONYM_DELIMITER_START +
                    acronymModel.getObject() + 
                    AmpAutocompleteFieldPanel.ACRONYM_DELIMITER_STOP;
        }
        ret = ret + titleModel.getObject();
        return ret;
    }
}
