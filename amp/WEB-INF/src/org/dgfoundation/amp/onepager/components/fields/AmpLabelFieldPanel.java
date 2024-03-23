/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;

/**
 * Represents a simple amp label field (text) 
 * This label is immutable, if you need it to change 
 * based on changes of the model you should use {@link#AmpLabelInformationFieldPanel}
 * @author mpostelnicu@dgateway.org
 * since Nov 3, 2010
 */
public class AmpLabelFieldPanel<T> extends AmpFieldPanel<T> {
    protected Label valueLabel;

    public Label getValueLabel() {
        return valueLabel;
    }

    public void setValueLabel(Label valueLabel) {
        this.valueLabel = valueLabel;
    }
    
    public AmpLabelFieldPanel(String id, IModel<T> model, String fmName) {
        this(id,model,fmName,false);
    }

    public AmpLabelFieldPanel(String id, IModel<T> model, String fmName, boolean hideLabel) {
        super(id, model, fmName,hideLabel);
        valueLabel=new TrnLabel("valueLabel",(model.getObject() != null ? model.getObject().toString() : " "));
        add(valueLabel);
    }
    
    public AmpLabelFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine,boolean modelo) {
        super(id, model, fmName,hideLabel,hideNewLine);
        if(modelo){
        valueLabel=new Label("valueLabel",model);
        }else{
        valueLabel=new TrnLabel("valueLabel",(model.getObject() != null ? model.getObject().toString() : " "));
        }
        add(valueLabel);
    }

}
