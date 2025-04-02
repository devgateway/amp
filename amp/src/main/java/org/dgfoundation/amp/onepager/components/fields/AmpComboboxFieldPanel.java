/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * @author mpostelnicu@dgateway.org
 * @deprecated
 * since Oct 21, 2010
 */
public class AmpComboboxFieldPanel<T> extends AmpFieldPanel<T> {

    

    protected final AbstractAmpAutoCompleteTextField<T> autoComplete;

    public AbstractAmpAutoCompleteTextField<T> getAutoComplete() {
        return autoComplete;
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpComboboxFieldPanel(String id, String fmName, final AbstractAmpAutoCompleteTextField<T> autoComplete){
        this(id, fmName, autoComplete,false,false);
    }
    
    public AmpComboboxFieldPanel(String id, String fmName, final AbstractAmpAutoCompleteTextField<T> autoComplete, AmpFMTypes fmType){
        this(id, fmName, autoComplete);
        this.fmType = fmType;
    }
    
    public AmpComboboxFieldPanel(String id, String fmName, final AbstractAmpAutoCompleteTextField<T> autoComplete, boolean newLineVisible, boolean hideLabel) {
        super(id, fmName,hideLabel);
        this.autoComplete = autoComplete;
        newLine.setVisible(newLineVisible);
        add(autoComplete);
        AjaxLink link=new AjaxLink("dropdownLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                //simulate onKeyDown event, this will trigger ajax-autocomplete.js to show all options
                String js=String.format("e=jQuery.Event('keydown'); e.which=40; $('#%s').trigger(e);",autoComplete.getMarkupId());
                target.focusComponent(autoComplete);
                target.appendJavaScript(js);
            }
        };
        //link.add(new Image("dropdown",new DropDownImageResourceRef()));
        add(link);
    }
    
}
