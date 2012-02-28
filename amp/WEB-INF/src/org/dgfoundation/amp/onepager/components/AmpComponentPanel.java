/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.sections.AmpFormSectionFeaturePanel;
import org.dgfoundation.amp.onepager.helper.OnepagerSection;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.web.pages.OnePager;

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
	protected IndicatingAjaxLink visibleFmButton;
	protected IndicatingAjaxLink enabledFmButton;
	protected IndicatingAjaxLink upButton;
	protected IndicatingAjaxLink downButton;
	protected IndicatingAjaxLink foldButton;
	protected AjaxCheckBox cascadeFmToChildren;
	
	protected boolean ignoreFmVisibility = false;
	protected boolean ignoreFmButtonsVisibility = false;
	
	public IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}
	
	public String getShorterFmName() {
		int maxLen=26;
		if(fmType.equals(AmpFMTypes.MODULE)) maxLen=13; 
		if(getFMName().length()>maxLen) return getFMName().substring(0,maxLen)+".."; 
			else return getFMName();
	}
	
	public AjaxCheckBox getCascadeFmToChildren() {
		return cascadeFmToChildren;
	}


	public void setCascadeFmToChildren(AjaxCheckBox cascadeFmToChildren) {
		this.cascadeFmToChildren = cascadeFmToChildren;
	}

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
		this(id,null,fmName,AmpFMTypes.MODULE);
	}
	
	public void setIgnoreFmVisibility(boolean ignoreFmVisibility) {
		this.ignoreFmVisibility = ignoreFmVisibility;
	}

	/**
	 * Switch visibility for this fm Control. Change Hide with Show for the FM Button
	 * @param target the ajax target
	 */
	public void switchFmVisible(AjaxRequestTarget target) {
		FMUtil.switchFmVisible(AmpComponentPanel.this);
		visibleFmButton.add(new AttributeModifier("value", new Model((FMUtil.isFmVisible(AmpComponentPanel.this)?"Hide":"Show")+ " "+getShorterFmName())));
		target.addComponent(this);
		target.appendJavascript(OnePagerUtil.getToggleChildrenJS(this));
	}

	/**
	 * Switch enabling/disabling for this fm Control. Change Hide with Show for the FM Button
	 * @param target the ajax target
	 */
	public void switchFmEnabled(AjaxRequestTarget target) {
		FMUtil.switchFmEnabled(AmpComponentPanel.this);
		enabledFmButton.add(new AttributeModifier("value", new Model((FMUtil.isFmEnabled(AmpComponentPanel.this)?"Disable":"Enable") + " "+getShorterFmName())));
		target.addComponent(this);
		target.appendJavascript(OnePagerUtil.getToggleChildrenJS(this));
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
		fmBorder = new TransparentWebMarkupContainer("fmBorder");
		add(fmBorder);
		visibleFmButton=new IndicatingAjaxLink("visibleFmButton") {	
			@Override
			public void onClick(AjaxRequestTarget target) {
				switchFmVisible(target);
				if(cascadeFmToChildren.getModelObject()!=null && cascadeFmToChildren.getModelObject()) 
					OnePagerUtil.cascadeFmVisible(target,FMUtil.isFmVisible(AmpComponentPanel.this), AmpComponentPanel.this);				
			}
		};
		visibleFmButton.setOutputMarkupId(true);
		visibleFmButton.setVisible(false);
		add(visibleFmButton);
		
		enabledFmButton=new IndicatingAjaxLink("enabledFmButton") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				switchFmEnabled(target);
				if(cascadeFmToChildren.getModelObject()!=null && cascadeFmToChildren.getModelObject()) 
					OnePagerUtil.cascadeFmEnabled(target,FMUtil.isFmEnabled(AmpComponentPanel.this), AmpComponentPanel.this);  
			}
		};
		
		cascadeFmToChildren=new IndicatingAjaxCheckBox("cascadeFmToChildren",new Model<Boolean>()) {			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				
			}
		};
		
		cascadeFmToChildren.setOutputMarkupId(true);
		cascadeFmToChildren.setVisible(false);
		cascadeFmToChildren.add(new AttributeModifier("title", new Model("Cascade to children")));
		add(cascadeFmToChildren);
		
		enabledFmButton.setOutputMarkupId(true);
		enabledFmButton.setVisible(false);
		add(enabledFmButton);
		
		
		upButton = new IndicatingAjaxLink("upButton") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				OnepagerSection os = OnePager.findByName(this.getParent().getClass().getName());
				OnepagerSection tmpOs = OnePager.findByPosition(os.getPosition() - 1);
				if (tmpOs == null || os == null)
					return;
				tmpOs.setPosition(tmpOs.getPosition() + 1);
				os.setPosition(os.getPosition() - 1);
				target.appendJavascript("window.location.reload()");
			}
		};
		add(upButton);
		
		downButton = new IndicatingAjaxLink("downButton") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				OnepagerSection os = OnePager.findByName(this.getParent().getClass().getName());
				OnepagerSection tmpOs = OnePager.findByPosition(os.getPosition() + 1);
				if (tmpOs == null || os == null)
					return;
				tmpOs.setPosition(tmpOs.getPosition() - 1);
				os.setPosition(os.getPosition() + 1);
				target.appendJavascript("window.location.reload()");
			}
		};
		add(downButton);
		
		foldButton = new IndicatingAjaxLink("foldButton") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				OnepagerSection os = OnePager.findByName(this.getParent().getClass().getName());
				if (os == null)
					return;
				os.setFolded(!os.isFolded());
				target.appendJavascript("window.location.reload()"); 
			}
		};
		add(foldButton);

		boolean fmMode = ((AmpAuthWebSession)getSession()).isFmMode();
		if (this instanceof AmpFormSectionFeaturePanel && fmMode){
			upButton.add(new AttributeModifier("title", new Model("Up")));
			upButton.add(new AttributeModifier("src", new Model("/TEMPLATE/ampTemplate/img_2/onepager/up.png")));		
			downButton.add(new AttributeModifier("title", new Model("Down")));
			downButton.add(new AttributeModifier("src", new Model("/TEMPLATE/ampTemplate/img_2/onepager/down.png")));		
		}
		else{
			upButton.setVisible(false);	
			downButton.setVisible(false);
			foldButton.setVisible(false);
		}
		
		if (ignoreFmVisibility){
			visibleFmButton.setEnabled(false);
		}
	}
	
	public AmpComponentPanel(String id, IModel<T> model,String fmName) {
		this(id,model,fmName,AmpFMTypes.MODULE);
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
		boolean fmMode = ((AmpAuthWebSession)getSession()).isFmMode();
		/**
		 * Do not reverse the order of fmEnabled and fmVisible
		 */
		boolean fmEnabled = FMUtil.isFmEnabled(this);
		boolean fmVisible = FMUtil.isFmVisible(this);
		
		setEnabled(fmMode?true:fmEnabled);
		if (!ignoreFmVisibility)
			setVisible(fmMode?true:fmVisible);
		
		enabledFmButton.add(new AttributeModifier("title", new Model((fmEnabled?"Disable":"Enable")+ " "+getFMName())));
		enabledFmButton.add(new AttributeModifier("src", new Model("/TEMPLATE/ampTemplate/img_2/onepager/" + (fmEnabled?"enable.png":"disable.png"))));
		visibleFmButton.add(new AttributeModifier("title", new Model((fmVisible?"Hide":"Show")+ " "+getFMName())));
		visibleFmButton.add(new AttributeModifier("src", new Model("/TEMPLATE/ampTemplate/img_2/onepager/" + (fmVisible?"alt_enable.png":"alt_disable.png"))));

		
		if (this instanceof AmpFormSectionFeaturePanel && fmMode){
			OnepagerSection tmpos = OnePager.findByName(this.getClass().getName());
			if (tmpos != null){
				foldButton.add(new AttributeModifier("title", new Model((tmpos.isFolded()?"Unfold":"Fold"))));
				foldButton.add(new AttributeModifier("src", new Model("/TEMPLATE/ampTemplate/img_2/onepager/"+(tmpos.isFolded()?"fold.png":"unfold.png"))));		
			}
		}
		
		if(fmMode && !ignoreFmButtonsVisibility) {
			visibleFmButton.setVisible(true);
			//enabledFmButton.setVisible(true);
			cascadeFmToChildren.setVisible(true);
			String style="border: 1px dashed #9E334D; padding: 4px;";
			fmBorder.add(new AttributeModifier("style", true, new Model(style)));
		}
		super.onBeforeRender();
	}
}
