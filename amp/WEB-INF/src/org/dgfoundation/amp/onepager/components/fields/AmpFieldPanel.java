/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.behaviors.ChoiceComponentVisualErrorBehavior;
import org.dgfoundation.amp.onepager.behaviors.ComponentVisualErrorBehavior;
import org.dgfoundation.amp.onepager.behaviors.ComponentVisualErrorBehavior2;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;

/**
 * Component to be extended directly by AMP Field Types. An AMP field contains a
 * label with the field name, in bold, an immediately below the real form
 * control It also holds a tooltip with help information about the field
 * (fetched from FM Utils) The default {@link AmpComponentPanel#fmType} is
 * obviously {@link AmpFMTypes#FIELD}
 * 
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public abstract class AmpFieldPanel<T> extends AmpComponentPanel<T> {

	private static final long serialVersionUID = -1378188138882414240L;
	protected Label titleLabel;
	protected WebMarkupContainer newLine;
	protected FormComponent<?> formComponent;
	protected String fmName;
	//protected BeautyTipBehavior tooltipContainer;

	/**
	 * @see #addFormComponent(FormComponent)
	 * @param fc
	 */
	protected void initFormComponent(FormComponent<?> fc) {
		formComponent = fc;
		fc.setLabel(new Model<String>(TranslatorUtil.getTranslatedText(fmName)));		
		fc.add(visualErrorBehavior());
		
		if (!ajaxFormChoiceComponentUpdatingBehaviorAppliesTo(fc)){
			fc.add(new AjaxFormComponentUpdatingBehavior(getActionMonitored(fc)){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(formComponent);
					onAjaxOnUpdate(target);
				}
				@Override
				protected void onError(AjaxRequestTarget target, RuntimeException e) {
					target.add(formComponent);
					onAjaxOnError(target);
				}
			});
		}
		else{
			fc.add(new AjaxFormChoiceComponentUpdatingBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(formComponent);
					onAjaxOnUpdate(target);
				}
				@Override
				protected void onError(AjaxRequestTarget target, RuntimeException e) {
					target.add(formComponent);
					onAjaxOnError(target);
				}
			});			
		}
	}
	
	
	public static boolean ajaxFormChoiceComponentUpdatingBehaviorAppliesTo(Component component){
		return (component instanceof RadioChoice) ||
			(component instanceof CheckBoxMultipleChoice) || (component instanceof RadioGroup) ||
			(component instanceof CheckGroup);
	}
	
	public static String getActionMonitored(Component component){
		 if (component instanceof DateTextField)
			 return "onchange";
		 return "onblur";
	}
	
	/**
	 * Override this to add functionality to the onUpdate method
	 * when the "onblur" event is generated on the form component
	 * @param target
	 */
	protected void onAjaxOnUpdate(AjaxRequestTarget target){
	}
	
	/**
	 * Override this to add functionality to the onUpdate method
	 * when the "onblur" event is generated on the form component
	 * @param target
	 */
	protected void onAjaxOnError(AjaxRequestTarget target){
	}
	
	
	/**
	 * This is a special {@link AjaxEventBehavior} crafted for visual ajax error feedback.
	 * This can come in several flavors, for example the {@link AbstractChoice}S is using a different kind of event, {@link ChoiceComponentVisualErrorBehavior}
	 * By default we use {@link ComponentVisualErrorBehavior}
	 * @return the event behavior to be added to the form component, triggering ajax validation
	 */
	protected Behavior visualErrorBehavior() {
		
		return new ComponentVisualErrorBehavior2();
		
		//return new ComponentVisualErrorBehavior("onchange", feedbackContainer);
	}

	/**
	 * use this to add form components that require validation. This method
	 * initializes validation entities first, so that your component can later
	 * show an ajax style validation error. Do not use the
	 * {@link #add(org.apache.wicket.Component...)} directly, this method also
	 * invokes it.
	 * 
	 * @param fc
	 *            The form component to have its validation entities initialized
	 *            and then added as component
	 */
	protected void addFormComponent(FormComponent<?> fc) {
		initFormComponent(fc);
		add(fc);
	}

	public WebMarkupContainer getNewLine() {
		return newLine;
	}

	public Label getTitleLabel() {
		return titleLabel;
	}

//	public BeautyTipBehavior getTooltipContainer() {
//		return tooltipContainer;
//	}

	public AmpFieldPanel(String id, String fmName, boolean hideLabel) {
		this(id,null,fmName,hideLabel);
	}
	
	public AmpFieldPanel(String id, String fmName, boolean hideLabel, boolean hideNewLine) {
		this(id,null,fmName,hideLabel, hideNewLine);
	}

	
	/**
	 * @param id
	 *            the component id
	 * @param fmName
	 *            the FM name Constructs a new AMPFieldPanel, with the component
	 *            id and the feature manager name for this field
	 */
	public AmpFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel) {
		this(id, model,fmName, hideLabel, hideLabel);

	}


	public AmpFieldPanel(String id, String fmName) {
		this(id, fmName, false);
	}

	public AmpFieldPanel(String id, IModel<T> model, String fmName) {
		this(id,model,fmName,false);
	}
	
	public AmpFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine, final boolean showReqStarForNotReqComp){
		super(id, model,fmName, AmpFMTypes.MODULE);
		this.fmType = AmpFMTypes.MODULE;
		
		setOutputMarkupId(true);
		this.fmName = fmName;
		
		Label requiredStar = new Label("requiredStar", new Model("")){
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				if (((formComponent!=null && formComponent.isRequired())||showReqStarForNotReqComp) && titleLabel.isVisible()){
					this.setDefaultModelObject("<font color=\"red\">*</font>");
					this.add(new AttributeAppender("style", new Model("margin-left: -5px"), ";"));
					this.setEscapeModelStrings(false);
				} 
				else
					this.add(new AttributeAppender("style", new Model("display: none"), ";"));
			}
		};
		add(requiredStar);
		titleLabel = new TrnLabel("fieldLabel", fmName);
		if (((AmpAuthWebSession)getSession()).isFmMode()){
			titleLabel.add(new SimpleAttributeModifier("title", "Original field name: " + fmName));
		}
		titleLabel.setVisible(!hideLabel);
		add(titleLabel);
		newLine = new WebMarkupContainer("newLine");
		newLine.setVisible(!hideNewLine);
		add(newLine);

		String tooltipText = FMUtil.getTooltip(this);
//		if (tooltipText != null) {
//			tooltipContainer = new BeautyTipBehavior(tooltipText);
//			tooltipContainer.setPositionPreference(TipPosition.right);
//			add(tooltipContainer);
//		}

	}

	
	public AmpFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine) {
		this(id, model, fmName,hideLabel, hideNewLine, false);
	}

	
	/**
	 * Override to implement custom converters for the enclosing container
	 * @param type
	 * @return
	 */
	public IConverter getInternalConverter(final Class<?> type) {
		return null;
	}

}
