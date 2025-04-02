package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
/**
 * To
 * @author Julian de Anquin jdeanquin@developmentgateway.org
 *
 */
public class AmpLabelInformationFieldPanel<T> extends AmpFieldPanel<T> {
    protected Label valueLabel;

    public Label getValueLabel() {
        return valueLabel;
    }

    public void setValueLabel(Label valueLabel) {
        this.valueLabel = valueLabel;
    }

    public AmpLabelInformationFieldPanel(String id, IModel<T> model, String fmName) {
        super (id, model, fmName, false, true);
        if (model != null) {
            valueLabel = new Label("valueLabel", model);
        }
        
        add(valueLabel);
        this.setOutputMarkupId(true);
    }
}
