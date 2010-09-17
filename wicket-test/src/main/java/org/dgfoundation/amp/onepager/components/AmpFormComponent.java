/**
 * 
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.util.FMUtil;

/**
 * @author mihai
 * 
 */
public class AmpFormComponent<T> extends FormComponent<T> implements AmpFMConfigurable {

	private static final long serialVersionUID = 7777061320952473979L;

	protected String fmName;
	protected AmpFMBehavior fmBehavior;

	@Override
	public String getFMName() {
		return fmName;
	}
	
	@Override
	public AmpFMBehavior getFMBehavior() {
		return fmBehavior;
	}

	public AmpFormComponent(String id, IModel<T> model, String fmName, AmpFMBehavior fmBehavior) {
		super(id,model);
		this.fmName = fmName;
		this.fmBehavior = fmBehavior;
		setEnabled(FMUtil.isFMEnabled(this));
		setVisible(FMUtil.isFMVisible(this));
	}
	
	public AmpFormComponent(String id, IModel<T> model, String fmName) {
		super(id,model);
		this.fmName = fmName;
		this.fmBehavior = AmpFMBehavior.FIELD;
		setEnabled(FMUtil.isFMEnabled(this));
		setVisible(FMUtil.isFMVisible(this));
	}
	

	
}
