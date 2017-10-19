/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

/**
 * Yes/No radio field that wraps around a boolean variable
 * 
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 * @deprecated extend {@link AmpGroupFieldPanel} not {@link AmpFieldPanel}
 */
public class AmpBooleanChoiceField extends AmpFieldPanel<Boolean>{

    private static final String[] DEFAULT_CHOICES={"Yes","No"};
    private static final long serialVersionUID = 0L;
    private RadioChoice<Boolean> choiceContainer;

    /**
     * Constructs a boolean choice with Yes/No as the visible choices
     * @param id
     * @param model
     * @param fmName
     * @throws Exception
     */
    public AmpBooleanChoiceField(String id, IModel<Boolean> model,  String fmName)  {
        this(id,model,new Model<String[]>(DEFAULT_CHOICES),fmName);
        this.fmType = AmpFMTypes.MODULE;
    }
    
    /**
     * Constructs a boolean choice with customized visible choices
     * @param id
     * @param model
     * @param choicesModel the model that returns the visible choices for true/false
     * @param fmName
     * @throws Exception
     */
    public AmpBooleanChoiceField(String id, IModel<Boolean> model, final IModel<String[]> choicesModel, String fmName)  {
        super(id,fmName);
        
        List<Boolean> choices = new ArrayList<Boolean>();
        choices.add(true);
        choices.add(false);
        
        IChoiceRenderer<? super Boolean> renderer = new ChoiceRenderer<Boolean>(){
            private static final long serialVersionUID = 1L;
            @Override
            public Object getDisplayValue(Boolean object) {
                if (object)
                    return TranslatorUtil.getTranslation(choicesModel.getObject()[0]);
                else
                    return TranslatorUtil.getTranslation(choicesModel.getObject()[1]);
            }
        };
        choiceContainer = new RadioChoice<Boolean>("choice", model, choices, renderer);
        
        /*
         * We set the suffix in order to overwrite the default one which is <br/>
         * so that we can show the radio fields horizontally  
         */
        choiceContainer.setSuffix(" ");
        addFormComponent(choiceContainer);
    }

    public RadioChoice<Boolean> getChoiceContainer() {
        return choiceContainer;
    }

    public void setChoiceContainer(RadioChoice<Boolean> choiceContainer) {
        this.choiceContainer = choiceContainer;
    }
    
}
