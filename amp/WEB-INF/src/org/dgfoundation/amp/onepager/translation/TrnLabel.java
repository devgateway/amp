/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;

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
	private static final Logger logger = Logger.getLogger(TrnLabel.class);

	private CharSequence key;
	private String value;

	/**
	 * NOT MEANT FOR GENERAL USE, only translation classes may use
	 * If you really know what you're doing and need a TrnLabel with
	 * the key specified as atribute in html, then use TrnLabel.getPlainTrnLabel(String id, IModel<?> model)
	 * @param id
	 */
	public TrnLabel(String id, String label) {
		super(id, label);
		super.setDefaultModelObject(translate(label));
		addKeyAttribute(TranslatorWorker.generateTrnKey(value));
		trnLabel();
	}

	private void addKeyAttribute(String key){
		this.key = key;
		if (TranslatorUtil.isTranslatorMode(getSession())){
			if (key == null)
				throw new IllegalArgumentException("Parameter \"key\" can't be null!");
			this.add(new SimpleAttributeModifier("key", key));
		}
	}
	
	private void trnLabel(){
		if (TranslatorUtil.isTranslatorMode(getSession())){
			super.setOutputMarkupId(true);
			this.add(new LabelTranslatorBehaviour());
			this.add(new AttributeAppender("style", new Model("text-decoration: underline; color: #0CAD0C;"), ""));
		}
	}
	
	private String translate(String value){
		AmpAuthWebSession session = (AmpAuthWebSession) getSession();
		Site site = session.getSite();
		
		String genKey = TranslatorWorker.generateTrnKey(value);
		String translatedValue;
		try {
			translatedValue = TranslatorWorker.getInstance(genKey).
									translateFromTree(genKey, site.getId().longValue(), session.getLocale().getLanguage(), 
											value, TranslatorWorker.TRNTYPE_LOCAL, null);
			this.value = translatedValue;
			return translatedValue;
		} catch (WorkerException e) {
			logger.error("Can't translate:", e);
		}
		return "";
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		if (tag.getAttribute("key") == null && key == null)
			throw new IllegalArgumentException("Attribute \"key\", required for translator, not specified! If you can't include it in html, please use the constructor that has a \"CharSequence key\" parameter. Tag in cause:" + tag);
	}
	
	/**
	 * Returns a new TrnLabel which <b>has the key specified as attribute in html</b>!
	 */
	public static TrnLabel getPlainTrnLabel(String id, String label){
		return new TrnLabel(id, label);
	}
	
}
