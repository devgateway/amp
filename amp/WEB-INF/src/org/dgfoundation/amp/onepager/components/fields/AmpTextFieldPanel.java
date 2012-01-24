/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * @author mpostelnicu@dgateway.org
 * since Sep 23, 2010
 */
public class AmpTextFieldPanel<T> extends AmpFieldPanel<T> {

	private static final long serialVersionUID = 611374046300554626L;
	protected TextField<T> textContainer;


	public TextField<T> getTextContainer() {
		return textContainer;
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpTextFieldPanel(String id, IModel<T> model, String fmName) {
		this(id,model,fmName,false);
	}

	public AmpTextFieldPanel(String id, IModel<T> model, String fmName, AmpFMTypes fmType) {
		this(id, model, fmName);
		this.fmType = fmType;
	}

	
	public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel) {
		super(id, fmName, hideLabel);
		textContainer = new TextField<T>("textContainer",model) {
			@Override
			public IConverter getConverter(Class<?> type) {
				if(getInternalConverter(type)!=null) return getInternalConverter(type);
				return super.getConverter(type);
			}
		};
		textContainer.setOutputMarkupId(true);
		addFormComponent(textContainer);
	}
	
	public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine) {
		super(id, fmName, hideLabel, hideNewLine);
		textContainer = new TextField<T>("textContainer",model) {
			@Override
			public IConverter getConverter(Class<?> type) {
				if(getInternalConverter(type)!=null) return getInternalConverter(type);
				return super.getConverter(type);
			}
		};
		textContainer.setOutputMarkupId(true);
		addFormComponent(textContainer);
	}
	
	
	
	/**
	 * You can use this constructor to specify the class 
	 * for the convertor in TextField
	 * 
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @param hideNewLine
	 * @param clazz  Class is needed for convertor
	 */
	public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine, Class<T> clazz) {
		super(id, fmName, hideLabel, hideNewLine);
		textContainer = new TextField<T>("textContainer",model, clazz);
		textContainer.setOutputMarkupId(true);
		addFormComponent(textContainer);
	}
	
}
