/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Translatable label
 * 
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 */
public class TrnLabel extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CharSequence key;

	/**
	 * NOT MEANT FOR GENERAL USE, only translation classes may use
	 * If you really know what you're doing and need a TrnLabel with
	 * the key specified as atribute in html, then use TrnLabel.getPlainTrnLabel(String id, IModel<?> model)
	 * @param id
	 */
	protected TrnLabel(String id) {
		super(id);
		trnLabel();
	}

	/**
	 * NOT MEANT FOR GENERAL USE, only translation classes may use
	 * If you really know what you're doing and need a TrnLabel with
	 * the key specified as atribute in html, then use TrnLabel.getPlainTrnLabel(String id, IModel<?> model)
	 * @param id
	 */
	protected TrnLabel(String id, String label) {
		super(id, label);
		trnLabel();
	}

	/**
	 * NOT MEANT FOR GENERAL USE, only translation classes may use
	 * If you really know what you're doing and need a TrnLabel with
	 * the key specified as atribute in html, then use TrnLabel.getPlainTrnLabel(String id, IModel<?> model)
	 * @param id
	 */
	protected TrnLabel(String id, IModel<?> model) {
		super(id, model);
		trnLabel();
	}
	
	public TrnLabel(String id, String label, String key){
		this(id, label);
		addKeyAttribute(key);
	}
	
	public TrnLabel(String id, IModel<?> model, String key) {
		this(id, model);
		addKeyAttribute(key);
	}	
	
	private void addKeyAttribute(String key){
		if (TranslatorUtil.isTranslatorMode(getSession())){
			if (key == null)
				throw new IllegalArgumentException("Parameter \"key\" can't be null!");
			this.add(new SimpleAttributeModifier("key", key));
			this.key = key;
		}
	}
	
	private void trnLabel(){
		if (TranslatorUtil.isTranslatorMode(getSession())){
			super.setOutputMarkupId(true);
			this.add(new LabelTranslatorBehaviour());
			this.add(new AttributeAppender("style", new Model("text-decoration: underline; color: #0CAD0C;"), ""));
		}
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		if (TranslatorUtil.isTranslatorMode(getSession())){
			if (tag.getAttribute("key") == null && key == null)
				throw new IllegalArgumentException("Attribute \"key\", required for translator, not specified! If you can't include it in html, please use the constructor that has a \"CharSequence key\" parameter. Tag in cause:" + tag);
		}
	}
	
	/**
	 * Returns a new TrnLabel which <b>has the key specified as attribute in html</b>!
	 */
	public static TrnLabel getPlainTrnLabel(String id, IModel<?> model){
		return new TrnLabel(id, model);
	}
	
}
