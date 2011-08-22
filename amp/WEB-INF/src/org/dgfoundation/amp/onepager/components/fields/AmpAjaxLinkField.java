/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.translation.LabelTranslatorBehaviour;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Encaspulates an ajax link of type {@link AjaxLink}
 * @author aartimon@dginternational.org
 * @since May 9, 2011
 */
public abstract class AmpAjaxLinkField extends AmpFieldPanel<Void> {

	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;

	public IndicatingAjaxLink getButton() {
		return button;
	}

	/**
	 * Escalated method invoker for wrapped {@link AjaxButton#onSubmit()}
	 * @param target
	 * @param form
	 */
	protected abstract void onClick(AjaxRequestTarget target);
	
	/**
	 * 
	 * @param id
	 * @param fmName
	 */
	public AmpAjaxLinkField(String id, String fmName, String buttonCaption) {
		super(id, fmName,true);
		button = new IndicatingAjaxLink("fieldButton") {
			private static final long serialVersionUID = -5699378405978605979L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpAjaxLinkField.this.onClick(target);
			}
		};

		AmpAuthWebSession session = (AmpAuthWebSession) getSession();
		Site site = session.getSite();
		
		String genKey = TranslatorWorker.generateTrnKey(buttonCaption);
		String translatedValue;
		button.add(new AttributeModifier("value", new Model(buttonCaption)));
		try {
			translatedValue = TranslatorWorker.getInstance(genKey).
									translateFromTree(genKey, site.getId().longValue(), session.getLocale().getLanguage(), 
											buttonCaption, TranslatorWorker.TRNTYPE_LOCAL, null);
			button.add(new AttributeModifier("value", new Model(translatedValue)));
		} catch (WorkerException e) {
			logger.error("Can't translate:", e);
		}
		
		if (TranslatorUtil.isTranslatorMode(getSession())){
			button.setOutputMarkupId(true);
			button.add(new LabelTranslatorBehaviour());
			button.add(new AttributeAppender("style", new Model("text-decoration: underline; color: #0CAD0C;"), ""));
			button.add(new SimpleAttributeModifier("key", genKey));
		}
		
		add(button);
	}

	public AmpAjaxLinkField(String id, String fmName, String buttonCaption, AmpFMTypes fmType) {
		this(id, fmName, buttonCaption);
		this.fmType = fmType;
	}
}
