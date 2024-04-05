/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.models.AmpMultiValueDropDownChoiceModel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.Set;

/**
 * Implements a category radio choice/checkbox choice field aspect. This component shows a
 * radio choice group or a checkbox choice group based on the settings received from the related
 * {@link AmpCategoryClass} <br/>
 * Since it wraps a {@link RadioChoice} and a {@link CheckBoxMultipleChoice} it
 * has to receive a list of {@link AmpCategoryValue}S
 * 
 * @see AmpCategoryValue
 * @see AmpCategoryClass
 * @see CategoryManagerUtil#getAmpCategoryValueCollectionByKey(String, Boolean,
 *      javax.servlet.http.HttpServletRequest)
 * @author mpostelnicu@dgateway.org since Sep 27, 2010
 */
public class AmpCategoryGroupFieldPanel extends
        AmpCategoryFieldPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 5227940320820286119L;


    /**
     * @param id
     * @param model
     * @param fmName
     * @param nullValid
     *            see {@link RadioChoice#isNullValid()} This parameter is
     *            ignored if the container is {@link CheckBoxMultipleChoice}
     * @throws Exception
     */
    public AmpCategoryGroupFieldPanel(String id, String categoryKey,
            IModel<Set<AmpCategoryValue>> model, String fmName,
            boolean ordered, Boolean nullValid, Boolean isMultiselect) throws Exception {
        super(id, categoryKey,fmName,ordered,isMultiselect);
        if (selectedMultiselect)
            choiceContainer = new CheckBoxMultipleChoice<AmpCategoryValue>(
                    "choice", model, choices,new TranslatedChoiceRenderer<AmpCategoryValue>());
        else
            choiceContainer = new RadioChoice<AmpCategoryValue>(
                    "choice",
                    new AmpMultiValueDropDownChoiceModel(model), choices,new TranslatedChoiceRenderer<AmpCategoryValue>())
                    .setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }

    public AmpCategoryGroupFieldPanel(String id, String categoryKey,
            IModel<Set<AmpCategoryValue>> model, String fmName,
            boolean ordered, Boolean nullValid, Boolean isMultiselect, AmpFMTypes fmType) throws Exception {
        this(id, categoryKey, model, fmName, ordered, nullValid, isMultiselect);
        this.fmType = fmType;
    }
    
    /**
     * Creates a category select field panel for use as a singleselect
     * @param id
     * @param categoryKey
     * @param model
     * @param fmName
     * @param ordered
     * @param nullValid
     * @throws Exception
     */
    public AmpCategoryGroupFieldPanel(String id, String categoryKey,
            IModel<AmpCategoryValue> model, String fmName,
            boolean ordered, Boolean nullValid, boolean hideLabel) throws Exception {
        super(id,categoryKey,fmName,ordered,false,null,hideLabel);
        choiceContainer = new RadioChoice<AmpCategoryValue>(
                "choice",model, choices, new TranslatedChoiceRenderer<AmpCategoryValue>())
                .setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }

    /**
     * Create a category select field panel that is dependent on another category
     * @param id
     * @param categoryKey
     * @param model
     * @param fmName
     * @param ordered
     * @param nullValid
     * @param hideLabel
     * @param dependantModel 
     * @throws Exception
     */
    public AmpCategoryGroupFieldPanel(String id, String categoryKey,
            IModel<AmpCategoryValue> model, String fmName,
            boolean ordered, Boolean nullValid, boolean hideLabel, 
            IModel<Set<AmpCategoryValue>> dependantModel) throws Exception {
        super(id,categoryKey,fmName,ordered,false,dependantModel,hideLabel);
        if (choices.getObject() != null && choices.getObject().size() == 1){
            AmpCategoryValue defValue = choices.getObject().get(0);
            if (model.getObject() == null){
                model.setObject(defValue);
            }
        }
        choiceContainer = new RadioChoice<AmpCategoryValue>(
                "choice",model, choices, new TranslatedChoiceRenderer<AmpCategoryValue>())
                .setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }

    public AmpCategoryGroupFieldPanel(String id, String categoryKey,
            IModel<AmpCategoryValue> model, String fmName,
            boolean ordered, Boolean nullValid) throws Exception {
        this(id,categoryKey,model,fmName,ordered,nullValid,false);
    }

}
