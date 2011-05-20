/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.behaviors.ComponentVisualErrorBehavior;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.FeedbackLabel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMUtil;

import com.visural.wicket.behavior.beautytips.BeautyTipBehavior;
import com.visural.wicket.behavior.beautytips.TipPosition;

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
	protected FeedbackLabel feedbackLabel;
	protected String fmName;
	protected WebMarkupContainer feedbackContainer;
	//protected BeautyTipBehavior tooltipContainer;

	/**
	 * @see #addFormComponent(FormComponent)
	 * @param fc
	 */
	protected void initFormComponent(FormComponent<?> fc) {
		formComponent = fc;
		feedbackLabel.setComponent(fc);
		fc.setLabel(new Model<String>(fmName));
		fc.add(new ComponentVisualErrorBehavior("onchange", feedbackContainer));
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

	
	public AmpFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine) {
		super(id, model,fmName, AmpFMTypes.FIELD);
		setOutputMarkupId(true);
		this.fmName = fmName;
		titleLabel = new TrnLabel("fieldLabel", new Model<String>(fmName),
				fmName);
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

		feedbackLabel = new FeedbackLabel("feedback");
		feedbackLabel.setOutputMarkupId(true);
		feedbackContainer = new WebMarkupContainer("feedbackContainer");
		feedbackContainer.setVisible(false);
		feedbackContainer.setOutputMarkupId(true);
		feedbackContainer.add(feedbackLabel);
		add(feedbackContainer);
	}

	
}
