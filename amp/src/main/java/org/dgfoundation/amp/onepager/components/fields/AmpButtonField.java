/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.translation.LabelTranslatorBehaviour;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.Iterator;
import java.util.List;



/**
 * Encaspulates a html button of type {@link AjaxButton}
 * 
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public abstract class AmpButtonField extends AmpFieldPanel<Void> {

    private static final long serialVersionUID = 3042844165981373890L;
    protected IndicatingAjaxButton button;
    private String genKey;
    private static final Behavior LABEL_TRANSLATOR_BEHAVIOR = new LabelTranslatorBehaviour();

    public IndicatingAjaxButton getButton() {
        return button;
    }

    
    /**
     * Escalated method invoker for wrapped {@link AjaxButton#onSubmit()}
     * 
     * @param target
     * @param form
     */
    protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);
    protected  void updateAjaxAttributes(AjaxRequestAttributes attributes){
        
    }
    

    /**
     * 
     * @param id
     * @param fmName
     */
    public AmpButtonField(String id, String fmName) {
        this(id, fmName, false, false);
    }

    protected void onError(AjaxRequestTarget target, Form<?> form) {
        // TODO Auto-generated method stub

    }

    public AmpButtonField(String id, String fmName, boolean hideLabel) {
        this(id, fmName, hideLabel, true);
    }

    public AmpButtonField(String id, String fmName, boolean hideLabel, boolean hideNewLine) {
        this(id,  fmName,  null,  hideLabel,  hideNewLine);
    }
    
    
    public AmpButtonField(String id, String fmName, String buttonCaption, boolean hideLabel, boolean hideNewLine) {
        //we show even if the label is hidden
        //we do not provide additional key to the tooltip
        //we do not provide a default tooltip
        super(id, fmName,true,"",true,"", hideLabel, hideNewLine);
        button = new IndicatingAjaxButton("fieldButton", new Model<String>(
                fmName)) {
            private static final long serialVersionUID = -5699378405978605979L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                AmpButtonField.this.onSubmit(target, form);
            }
            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
            {
                super.updateAjaxAttributes(attributes);
                
                AmpButtonField.this.updateAjaxAttributes(attributes);
            }
            @Override
            protected void onError(final AjaxRequestTarget target, Form<?> form) {
                AmpButtonField.this.onError(target, form);

                // visit form children and add to the ajax request the invalid
                // ones
                form.visitChildren(FormComponent.class,
                        new IVisitor<FormComponent, Void>() {
                            @Override
                            public void component(FormComponent component,
                                    IVisit<Void> visit) {
                                if (!component.isValid()) {
                                    
                                    //some of the fields that need to show errors are HiddenFieldS. These are cumulative error fields, that show error for groups of other fields
                                    //like for example a list of sectors with percentages
                                    //when these AmpCollectionValidatorFieldS are detected, their validation is revisited
                                    if (component instanceof HiddenField) {                                 
                                         target.appendJavaScript("$('#"+ component.getMarkupId() +"').parents().show();");
                                         target.appendJavaScript("$(window).scrollTop($('#"+component.getParent().getMarkupId()+"').position().top)");
                                         target.add(component);
                                         if(component.getParent() instanceof AmpCollectionValidatorField<?, ?>) 
                                             ((AmpCollectionValidatorField)component.getParent()).reloadValidationField(target);                                    

                                    } else {
                                        target.focusComponent(component);
                                        String js = null;
                                        
                                        //we simulate onClick over AmpGroupFieldS because radiochoices are treated differently they can't receive onChange.
                                        //For the rest of the components we use onChange
                                        if(component instanceof RadioChoice<?> || component instanceof CheckBoxMultipleChoice
                                                || component  instanceof RadioGroup<?> || component instanceof CheckGroup) 
                                            js=String.format("$('#%s').click();",component.getMarkupId());                                      
                                        else                                            
                                            js=String.format("$('#%s').change();",component.getMarkupId());
                                        
                                        target.appendJavaScript(js);
                                        target.add(component);
                                    }
                                }
                            }
                        });

            }
        };
        
        AmpAuthWebSession session = (AmpAuthWebSession) getSession();
        Site site = session.getSite();
        
        genKey = TranslatorWorker.generateTrnKey(buttonCaption==null?fmName:buttonCaption);
        String translatedValue;
        button.add(new AttributeModifier("value", new Model(buttonCaption==null?fmName:buttonCaption)));
        try {
            translatedValue = TranslatorWorker.getInstance(genKey).
                                    translateFromTree(genKey, site, session.getLocale().getLanguage(), 
                                            buttonCaption==null?fmName:buttonCaption, TranslatorWorker.TRNTYPE_LOCAL, null);
            button.add(new AttributeModifier("value", new Model(translatedValue)));
        } catch (WorkerException e) {
            logger.error("Can't translate:", e);
        }
        
        //since buttons dont have title we add the tooltip directly to the button itself
        if(!"".equals(titleTooltip.getDefaultModel().getObject().toString()) && titleTooltip.getDefaultModel().getObject().toString().trim().length()>0 ){
            
        }
        addFormComponent(button);
    }
    
    protected void addTooltip(){ 
        button.add(new AttributeModifier("data-ot",titleTooltip.getDefaultModel().getObject().toString()));
    }
    public AmpButtonField(String id, String fmName, AmpFMTypes fmType,
            boolean hideNewLine) {
        this(id, fmName, true, hideNewLine);
    }

    public AmpButtonField(String id, String fmName, AmpFMTypes fmType) {
        this(id, fmName);
        this.fmType = fmType;
    }
    
    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (TranslatorUtil.isTranslatorMode(getSession())){
            button.setOutputMarkupId(true);
            button.add(LABEL_TRANSLATOR_BEHAVIOR);
            button.add(new AttributeAppender("style", new Model<String>("text-decoration: underline; color: #0CAD0C;"), ""));
            button.add(new AttributeModifier("key", genKey));
        }
        else{
            button.add(AttributeModifier.remove("key"));
            button.add(AttributeModifier.remove("style"));
            List<? extends Behavior> list = this.getBehaviors();
            Iterator<? extends Behavior> it = list.iterator();
            while (it.hasNext()) {
                Behavior behavior = (Behavior) it.next();
                if (behavior == LABEL_TRANSLATOR_BEHAVIOR)
                    this.remove(behavior);
            }
        }
            
        
    }
    protected String getTranslatorTooltipJavascript(){
        return "spawnEditBox('"+ button.getMarkupId() +"');";
    }

}
