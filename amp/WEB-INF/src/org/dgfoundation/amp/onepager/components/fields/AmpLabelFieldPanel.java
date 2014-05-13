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
	
	public AmpLabelFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine) {
		super(id, model, fmName,hideLabel,hideNewLine);
		valueLabel=new TrnLabel("valueLabel",(model.getObject() != null ? model.getObject().toString() : " "));
		add(valueLabel);
	}

}
