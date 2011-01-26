/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;

/**
 * Basic class for AMP components. This component wraps a feature manager connectivity, receiving
 * the feature name and the fmType (field,feature,module). It then queries the FM utils and 
 * determines if this component is visible or not and if this component is enabled or disabled.
 * This class can be used directly to create panels or extended to make amp field /feature panels
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public abstract class AmpComponentPanel<T> extends Panel implements
		AmpFMConfigurable {

	protected String fmName;
	protected AmpFMTypes fmType;
	protected WebMarkupContainer fmBorder;
	protected IndicatingAjaxButton visibleFmButton;
	protected IndicatingAjaxButton enabledFmButton;
	protected static Logger logger = Logger.getLogger(AmpComponentPanel.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5847159396251223479L;

	/**
	 * Constructs a new object using the component id, fmName and fmType
	 * @see AmpFMTypes
	 * @param id the component id
	 * @param fmName the feature manager name
	 * @param fmType the feature type
	 */
	public AmpComponentPanel (String id,String fmName, AmpFMTypes fmType) {
		this(id,null,fmName,fmType);
	}

	
	/**
	 * Constructs a new object using only the component id and the FM Name.
	 * Assumes the FM Type is {@link AmpFMTypes#FEATURE}
	 * @param id
	 * @param fmName
	 */
	public AmpComponentPanel(String id,String fmName) {
		this(id,null,fmName,AmpFMTypes.FEATURE);
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpComponentPanel(String id, IModel<T> model,String fmName, AmpFMTypes fmBehavior) {
		super(id, model);
		setOutputMarkupId(true);
		this.fmName=fmName;
		this.fmType=fmBehavior;
		setEnabled(FMUtil.isFmMode(getSession())?true:FMUtil.isFmEnabled(this));
		setVisible(FMUtil.isFmMode(getSession())?true:FMUtil.isFmVisible(this));
		fmBorder = new TransparentWebMarkupContainer("fmBorder");
		add(fmBorder);
		visibleFmButton=new IndicatingAjaxButton("visibleFmButton",new Model<String>(FMUtil.isFmVisible(this)?"Hide":"Show")) {	
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				FMUtil.switchFmVisible(AmpComponentPanel.this);
				visibleFmButton.setDefaultModel(new Model<String>(FMUtil.isFmVisible(AmpComponentPanel.this)?"Hide":"Show"));
				target.addComponent(this);
			}
		};
		visibleFmButton.setOutputMarkupId(true);
		visibleFmButton.setVisible(false);
		add(visibleFmButton);
		
		enabledFmButton=new IndicatingAjaxButton("enabledFmButton",new Model<String>(FMUtil.isFmEnabled(this)?"Disable":"Enable")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				FMUtil.switchFmEnabled(AmpComponentPanel.this);
				enabledFmButton.setDefaultModel(new Model<String>(FMUtil.isFmEnabled(AmpComponentPanel.this)?"Disable":"Enable"));
				target.addComponent(this);
			}
		};
		enabledFmButton.setOutputMarkupId(true);
		enabledFmButton.setVisible(false);
		add(enabledFmButton);
	}
	
	public AmpComponentPanel(String id, IModel<T> model,String fmName) {
		this(id,model,fmName,AmpFMTypes.FEATURE);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public AmpFMTypes getFMType() {
		return fmType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFMName() {
		return fmName;
	}
	
	@Override
	protected void onBeforeRender() {
		if(FMUtil.isFmMode(getSession())) {
			visibleFmButton.setVisible(true);
			enabledFmButton.setVisible(true);
		}
		String style=FMUtil.isFmMode(getSession())?"border: 2px blue solid; padding: 4px;":"";
		fmBorder.add(new AttributeModifier("style", true, new Model(style)));
		super.onBeforeRender();
	}

}
