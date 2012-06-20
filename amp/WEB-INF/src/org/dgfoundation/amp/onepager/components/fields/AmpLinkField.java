/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Encaspulates a html link of type {@link IndicatingAjaxLink}
 * 
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public abstract class AmpLinkField extends AmpFieldPanel<Void> {

	private static final long serialVersionUID = 3042844165981373890L;
	protected IndicatingAjaxLink<String> link;

	public IndicatingAjaxLink<String> getLink() {
		return link;
	}

	/**
	 * Escalated method invoker for wrapped
	 * {@link IndicatingAjaxLink#onClick(AjaxRequestTarget)}
	 * 
	 * @param target
	 */
	protected abstract void onClick(AjaxRequestTarget target);

	public AmpLinkField(String id, String fmName) {
		this(id,fmName,null);
	}
	
	/**
	 * Constructs a new wrapped {@link IndicatingAjaxLink} with optional confirmation question. The question string is fetched by qModel param 
	 * @param id
	 * @param fmName
	 * @param qModel the string Model of the confirmation question, if any. Use Null otherwise
	 */
	public AmpLinkField(String id, String fmName,final IModel<String> qModel) {
		this(id, fmName, qModel, true, true);
	}
	
	public AmpLinkField(String id, String fmName, boolean hideLabel, boolean hideNewLine) {
		this(id,fmName,null, hideLabel, hideNewLine);
	}
	
	public AmpLinkField(String id, String fmName,final IModel<String> qModel, boolean hideLabel, boolean hideNewLine) {
		super(id, fmName, hideLabel, hideNewLine);
		link = new IndicatingAjaxLink<String>(
				"fieldLink", new Model<String>(fmName)) {
			private static final long serialVersionUID = -5699378405978605979L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpLinkField.this.onClick(target);
			}
			
			@Override
		    protected IAjaxCallDecorator getAjaxCallDecorator() {
				if(qModel==null) return super.getAjaxCallDecorator(); else
		        return new AjaxCallDecorator() {
					@Override
					public CharSequence decorateScript(Component c,
							CharSequence script) {
		                return "if(!confirm('"+qModel.getObject()+"')) return false;" + script;
					}
		        };

		    }

		};
		add(link);
	}

}
