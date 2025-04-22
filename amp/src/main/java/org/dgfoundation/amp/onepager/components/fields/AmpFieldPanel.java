/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.behaviors.ComponentVisualErrorBehavior2;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.Iterator;
import java.util.List;

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
    protected Label titleTooltip;
    protected WebMarkupContainer newLine;
    protected WebMarkupContainer newLineTooltip;
    protected FormComponent<?> formComponent;
    IndicatingAjaxLink editTooltipLink;
    IndicatingAjaxLink editTitleLink;
    protected String fmName;
    protected WebMarkupContainer tooltipIcon;
    protected boolean hideLabel;
    protected boolean showTooltipIfLabelHidden;
    protected boolean showTranslatorIconIfLabelHidden;
    protected boolean tooltipTranslatorEnabled = true;
    protected boolean titleTranslatorEnabled = true;
    protected String labelText;
    private boolean hideTooltipIcon = true;

    /**
     * @see #addFormComponent(FormComponent)
     * @param fc
     */
    protected void initFormComponent(FormComponent<?> fc) {
        formComponent = fc;
        fc.setLabel(new Model<String>(TranslatorUtil.getTranslatedText(labelText)));
        fc.add(visualErrorBehavior());

        if (!ajaxFormChoiceComponentUpdatingBehaviorAppliesTo(fc)) {
            fc.add(new AjaxFormComponentUpdatingBehavior(getActionMonitored(fc)) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(formComponent);
                    onAjaxOnUpdate(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target,
                        RuntimeException e) {
                    target.add(formComponent);
                    onAjaxOnError(target);
                }
            });
        } else {
            fc.add(new AjaxFormChoiceComponentUpdatingBehavior() {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(formComponent);
                    onAjaxOnUpdate(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target,
                        RuntimeException e) {
                    target.add(formComponent);
                    onAjaxOnError(target);
                }
            });
        }
    }

    public static boolean ajaxFormChoiceComponentUpdatingBehaviorAppliesTo(
            Component component) {
        return (component instanceof RadioChoice)
                || (component instanceof CheckBoxMultipleChoice)
                || (component instanceof RadioGroup)
                || (component instanceof CheckGroup);
    }

    public static String getActionMonitored(Component component) {
        if (component instanceof DateTextField)
            return "onchange";
        return "onblur";
    }

    /**
     * Override this to add functionality to the onUpdate method when the
     * "onblur" event is generated on the form component
     * 
     * @param target
     */
    protected void onAjaxOnUpdate(AjaxRequestTarget target) {
    }

    /**
     * Override this to add functionality to the onUpdate method when the
     * "onblur" event is generated on the form component
     * 
     * @param target
     */
    protected void onAjaxOnError(AjaxRequestTarget target) {
    }

    /**
     * This is a special {@link AjaxEventBehavior} crafted for visual ajax error
     * feedback. This can come in several flavors, for example the
     * {@link AbstractChoice}S is using a different kind of event, By default we
     * use {@link ComponentVisualErrorBehavior2}
     * 
     * @return the event behavior to be added to the form component, triggering
     *         ajax validation
     */
    protected Behavior visualErrorBehavior() {

        return new ComponentVisualErrorBehavior2();

        // return new ComponentVisualErrorBehavior("onchange",
        // feedbackContainer);
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

    public AmpFieldPanel(String id, String fmName, boolean hideLabel) {
        this(id, fmName, hideLabel,"",false,false,"");
    }
    public AmpFieldPanel(String id, String fmName, boolean hideLabel,String tooltip,boolean showTranslatorIconIfLabelHidden,boolean showTooltipIfLabelHidden,String aditionalTooltipKey) {
        this(id, null,showTooltipIfLabelHidden,aditionalTooltipKey, fmName, hideLabel,tooltip,showTranslatorIconIfLabelHidden);
    }
    public AmpFieldPanel(String id, String fmName, boolean hideLabel,boolean hideNewLine) {
        this(id,fmName,false,"",false,"",hideLabel,hideNewLine);
    }
    public AmpFieldPanel(String id, String fmName, boolean showTooltipIfLabelHidden,String aditionalTooltipKey,boolean showTranslatorIconIfLabelHidden,String tooltip,boolean hideLabel,boolean hideNewLine) {
        this(id,  null,showTooltipIfLabelHidden,aditionalTooltipKey, fmName,hideLabel, hideNewLine,tooltip,showTranslatorIconIfLabelHidden);
    }
    /**
     * @param id
     *            the component id
     * @param fmName
     *            the FM name Constructs a new AMPFieldPanel, with the component
     *            id and the feature manager name for this field
     */
    public AmpFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel) {
        this(id, model,false,"", fmName,hideLabel,"",false);    
    }
    /**
     * @param id
     *            the component id
     * @param fmName
     *            the FM name Constructs a new AMPFieldPanel, with the component
     *            id and the feature manager name for this field
     */
    public AmpFieldPanel(String id, IModel<T> model,boolean showTooltipIfLabelHidden,String aditionalTooltipKey, String fmName,
            boolean hideLabel,String tooltip,boolean showTranslatorIconIfLabelHidden) {
        this(id, model,showTooltipIfLabelHidden, aditionalTooltipKey,fmName, hideLabel, hideLabel,tooltip,showTranslatorIconIfLabelHidden);

    }

    public AmpFieldPanel(String id, String fmName) {
        this(id, fmName, false);
    }

    public AmpFieldPanel(String id, IModel<T> model, String fmName) {
        this(id, model, fmName, false);
    }
    public AmpFieldPanel(String id, IModel<T> model, String fmName,
            boolean hideLabel, boolean hideNewLine,
            final boolean showReqStarForNotReqComp) {
        this(id, model,false,"", fmName,hideLabel, hideNewLine,showReqStarForNotReqComp,"",false);
    }
    public AmpFieldPanel(String id, IModel<T> model, boolean showTooltipIfLabelHidden,String aditionalTooltipKey,String fmName,
            boolean hideLabel, boolean hideNewLine,final boolean showReqStarForNotReqComp,
            String tooltip,boolean showTranslatorIconIfLabelHidden) {
        this(id, model, showTooltipIfLabelHidden,aditionalTooltipKey,fmName, hideLabel, hideNewLine,
                showReqStarForNotReqComp, false,tooltip,showTranslatorIconIfLabelHidden);
    }
    public AmpFieldPanel(String id, IModel<T> model, final String fmName,
            boolean hideLabel, boolean hideNewLine,
            final boolean showReqStarForNotReqComp, boolean enableReqStar) {
        this(id, model,false,"", fmName,hideLabel, hideNewLine,showReqStarForNotReqComp, 
                enableReqStar,"",false);
    }
    /**
     * If we need to provide a default tooltip we should pass it to the constructor
     * other wise the default will be no tooltip
     * @param id
     * @param model
     * @param fmName
     * @param hideLabel
     * @param hideNewLine
     * @param showReqStarForNotReqComp
     * @param enableReqStar
     * @param tooltip 
     */
    
    public AmpFieldPanel(String id, IModel<T> model, boolean showTooltipIfLabelHidden,String aditionalTooltipKey,final String fmName,
            boolean hideLabel, boolean hideNewLine,
            final boolean showReqStarForNotReqComp, boolean enableReqStar,String tooltip,boolean showTranslatorIconIfLabelHidden) {
        super(id, model, fmName, AmpFMTypes.MODULE);
        this.fmType = AmpFMTypes.MODULE;
        this.hideLabel=hideLabel;
        setOutputMarkupId(true);
        this.fmName = fmName;
        this.showTooltipIfLabelHidden=showTooltipIfLabelHidden;
        this.showTranslatorIconIfLabelHidden = showTranslatorIconIfLabelHidden;
        
        configureLabelText();
        configureTranslatorLinks();
        
        if (StringUtils.isEmpty(labelText)) {
            labelText = fmName;
        }
        
        //we show the edittooltip icon only if we are in translator mode and
        //the label is not hidden or we choose to show it even if hidden 
        final boolean showTooltipEditor = isTooltipEditorVisible();

        final boolean showTitleEditor = isTitleEditorVisible();

        Label requiredStar = new Label("requiredStar", new Model<String>("")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onBeforeRender() {
                super.onBeforeRender();
                if (((formComponent != null && formComponent.isRequired()) || showReqStarForNotReqComp)) {
                    this.setDefaultModelObject("<font color=\"red\">*</font>");
                    this.add(new AttributeModifier("style", new Model<String>(
                            "padding-left: -5px;")));
                    this.setEscapeModelStrings(false);
                } else
                    this.add(new AttributeModifier("style", new Model<String>(
                            "display: none;")));
            }
        };
        // requiredStar.setVisible(!hideNewLine||enableReqStar);
        add(requiredStar);

        editTitleLink = new IndicatingAjaxLink("editTitleLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript(getTranslatorTooltipJavascript());

            }


        };
        editTitleLink.setVisible(showTitleEditor);
        editTitleLink.add(new AttributeModifier("data-ot", TranslatorWorker.translateText("Please click to edit field label")));
        add(editTitleLink);

        //for the edit of the tooltip

        if(!"".equals(aditionalTooltipKey) && aditionalTooltipKey.trim().length()>0){
            aditionalTooltipKey="_"+aditionalTooltipKey;
        }
        titleTooltip = new TrnLabel("tooltipEditor",tooltip,TranslatorWorker.generateTrnKey("tooltip_" + this.getFMName()+aditionalTooltipKey),true) {

            /**
             * 
             */
            private static final long serialVersionUID = 7873518570342980535L;
            private Behavior titleBehavior = new AttributeModifier("title","Original field name: " + fmName);
            
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (((AmpAuthWebSession) getSession()).isFmMode()) {
                    titleTooltip.add(titleBehavior);
                } else {
                    List<? extends Behavior> list = this.getBehaviors();
                    Iterator<? extends Behavior> it = list.iterator();
                    while (it.hasNext()) {
                        Behavior behavior = (Behavior) it.next();
                        if (behavior == titleBehavior)
                            this.remove(behavior);
                    }
                }
            }
        };
        //please see at the beggining of the method the conditions to show the tooltip editor
        titleTooltip.setVisible(showTooltipEditor);
        titleTooltip.setOutputMarkupId(true);
        add(titleTooltip);
        newLineTooltip = new WebMarkupContainer("newLineTooltip");
        newLineTooltip.setVisible(showTooltipEditor);
        add(newLineTooltip);
        
        editTooltipLink = new IndicatingAjaxLink("editTooltipLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("spawnEditBox('"+ titleTooltip.getMarkupId() +"');");
                
            }
            
 
        };
        editTooltipLink.setVisible(showTooltipEditor);
        editTooltipLink.add(new AttributeModifier("data-ot",TranslatorWorker.translateText("Please click to enter tooltip, save an empty value for disabling the tooltip")));
        add(editTooltipLink);
        tooltipIcon= new WebMarkupContainer("tooltip_icon");
        
        tooltipIcon.setVisible(false);
        add(tooltipIcon);
        
        

        titleLabel = new TrnLabel("fieldLabel", labelText) {
            private Behavior titleBehavior = new AttributeModifier("title",
                    "Original field name: " + labelText);

            @Override
            protected void onConfigure() {
                setTranslatable(titleTranslatorEnabled); // if a field should be translatable or not in TranslationMode()
                super.onConfigure();
                if (((AmpAuthWebSession) getSession()).isFmMode()) {
                    titleLabel.add(titleBehavior);
                } else {
                    List<? extends Behavior> list = this.getBehaviors();
                    Iterator<? extends Behavior> it = list.iterator();
                    while (it.hasNext()) {
                        Behavior behavior = (Behavior) it.next();
                        if (behavior == titleBehavior)
                            this.remove(behavior);
                    }
                }
                //when we configure the title we add the tooltip
                if(titleTooltip.getDefaultModel().getObject()!=null && !"".equals(titleTooltip.getDefaultModel().getObject().toString()) && titleTooltip.getDefaultModel().getObject().toString().trim().length()>0 ){
                    addTooltip();
                    
                }
            }
        };
        titleLabel.setVisible(!hideLabel);
        add(titleLabel);
        newLine = new WebMarkupContainer("newLine");
        newLine.setVisible(!hideNewLine);
        add(newLine);
    }
    
    /**
     * on standard fields we add the tooltip to the label if you need it added on the component itself please override these method
     * please see {@link AmpButtonField} or {@link AmpAjaxLinkField}
     */
    protected void addTooltip(){ 
        // 
        boolean visible = (!hideLabel || showTooltipIfLabelHidden) && this.hideTooltipIcon;
        if(visible){
            titleLabel.add(new AttributeModifier("data-ot",titleTooltip.getDefaultModel().getObject().toString()));
            tooltipIcon.add(new AttributeModifier("data-ot",titleTooltip.getDefaultModel().getObject().toString()));
        }
        tooltipIcon.setVisible(visible);
    }
    
    public AmpFieldPanel(String id, IModel<T> model, String fmName,
            boolean hideLabel, boolean hideNewLine) {
        this(id,  model,false,"", fmName,hideLabel, hideNewLine,"",false);
    }
    public AmpFieldPanel(String id, IModel<T> model, boolean showTooltipIfLabelHidden,String aditionalTooltipKey,String fmName,
            boolean hideLabel, boolean hideNewLine,String tooltip,boolean showTranslatorIconIfLabelHidden) {
        this(id, model,showTooltipIfLabelHidden,aditionalTooltipKey, fmName, hideLabel, hideNewLine, false,tooltip,showTranslatorIconIfLabelHidden);
    }

    /**
     * Override to implement custom converters for the enclosing container
     * 
     * @param type
     * @return
     */
    public IConverter getInternalConverter(final Class<?> type) {
        return null;
    }

    public Label getTitleTooltip() {
        return titleTooltip;
    }

    public void setTitleTooltip(Label titleTooltip) {
        this.titleTooltip = titleTooltip;
    }
    
    public boolean isShowTranslatorIconIfLabelHidden() {
        return showTranslatorIconIfLabelHidden;
    }

    public void setShowTranslatorIconIfLabelHidden(
            boolean showTranslatorIconIfLabelHidden) {
        this.showTranslatorIconIfLabelHidden = showTranslatorIconIfLabelHidden;
    }

    protected String getTranslatorTooltipJavascript(){
        return "spawnEditBox('"+ titleLabel.getMarkupId() +"');";
    }
    
    public boolean isTooltipTranslatorEnabled() {
        return tooltipTranslatorEnabled;
    }

    public void setTooltipTranslatorEnabled(boolean tooltipTranslatorEnabled) {
        this.tooltipTranslatorEnabled = tooltipTranslatorEnabled;
    }

    public boolean isTitleTranslatorEnabled() {
        return titleTranslatorEnabled;
    }

    public void setTitleTranslatorEnabled(boolean titleTranslatorEnabled) {
        this.titleTranslatorEnabled = titleTranslatorEnabled;
    }
    
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }
    
    protected void configureTranslatorLinks(){ 
    }
    
    protected void configureLabelText(){
    }

    private boolean isTooltipEditorVisible() {
        return TranslatorUtil.isTranslatorMode(getSession()) && (!this.hideLabel || this.showTooltipIfLabelHidden ) && tooltipTranslatorEnabled;
    }

    private boolean isTitleEditorVisible() {
        return (TranslatorUtil.isTranslatorMode(getSession()) && (!this.hideLabel || this.showTranslatorIconIfLabelHidden)) && titleTranslatorEnabled;
    }

    public void setTooltipVisible(boolean visible) {
        this.hideTooltipIcon = visible;
        this.titleTooltip.setVisible(visible && isTooltipEditorVisible());
        this.editTooltipLink.setVisible(visible && isTooltipEditorVisible());
        this.editTitleLink.setVisible(visible && isTitleEditorVisible());
    }
}
