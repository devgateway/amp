/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.SetModel;
import org.dgfoundation.amp.onepager.models.AmpMultiValueDropDownChoiceModel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Implements a category select/multiselect field aspect. This component shows a
 * dropdown/multi select based on the settings received from the related
 * {@link AmpCategoryClass} <br/>
 * Since it wraps a {@link DropDownChoice} and a {@link ListMultipleChoice} it
 * has to receive a list of {@link AmpCategoryValue}S
 * 
 * @see AmpCategoryValue
 * @see AmpCategoryClass
 * @see CategoryManagerUtil#getAmpCategoryValueCollectionByKey(String, Boolean,
 *      javax.servlet.http.HttpServletRequest)
 * @author mpostelnicu@dgateway.org since Sep 23, 2010
 */
public class AmpCategorySelectFieldPanel extends AmpCategoryFieldPanel{

    private static final long serialVersionUID = 5227940320820286119L;
    protected AbstractChoice<?, AmpCategoryValue> choiceContainer;
    protected IModel<Set<AmpCategoryValue>> choiceModel;

    public IModel<Set<AmpCategoryValue>> getChoiceModel() {
        return choiceModel;
    }

    public AbstractChoice<?, AmpCategoryValue> getChoiceContainer() {
        return choiceContainer;
    }
    
    /**
     * @see AmpCategorySelectFieldPanel#AmpCategorySelectFieldPanel(String, String, IModel, String, boolean, Boolean, Boolean, IModel)
     */
    public AmpCategorySelectFieldPanel(String id, String categoryKey,
            IModel<Set<AmpCategoryValue>> model, String fmName,
            boolean ordered, Boolean nullValid, Boolean isMultiselect) throws Exception {
        this(id,categoryKey,model,fmName,ordered,nullValid,isMultiselect, (IModel)null);
    }

    public AmpCategorySelectFieldPanel(String id, String categoryKey,
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
    public AmpCategorySelectFieldPanel(String id, String categoryKey,
            IModel<AmpCategoryValue> model, String fmName,
            boolean ordered, Boolean nullValid, boolean hideLabel,
            IModel<Set<AmpCategoryValue>> relatedChoicesModel, boolean hideNewLine) throws Exception {
        super(id,categoryKey,fmName,ordered,false,relatedChoicesModel,hideLabel, hideNewLine);
        choiceContainer = new DropDownChoice<AmpCategoryValue>(
                "choice",model, choices, new TranslatedChoiceRenderer<AmpCategoryValue>())
                .setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }

    public AmpCategorySelectFieldPanel(String id, String categoryKey,
                                       IModel<AmpCategoryValue> model, String fmName,
                                       boolean ordered, Boolean nullValid, boolean hideLabel) throws Exception {
        this(id, categoryKey, model, fmName, ordered, nullValid, hideLabel, null, false);
    }
    
    public AmpCategorySelectFieldPanel(String id, String categoryKey,
            IModel<AmpCategoryValue> model, String fmName,
            boolean ordered, Boolean nullValid) throws Exception {
        this(id,categoryKey,model,fmName,ordered,nullValid,false);
    }

    /**
     * Constructs a new category manager control of type select (single select or multiple select).
     * @param id {@inheritDoc}
     * @param model {@inheritDoc}
     * @param fmName {@inheritDoc}
     * @param nullValid {@inheritDoc}
     *            see {@link DropDownChoice#isNullValid()} This parameter is
     *            ignored if the container is {@link ListMultipleChoice}
     * @throws Exception
     */
    public AmpCategorySelectFieldPanel(String id, String categoryKey,
            IModel<Set<AmpCategoryValue>> model, String fmName,
            boolean ordered, Boolean nullValid, Boolean isMultiselect,IModel<Set<AmpCategoryValue>> relatedChoicesModel) throws Exception {
        super(id,categoryKey,fmName,ordered,isMultiselect,relatedChoicesModel, false, false);
            this.choiceModel=model;
            if (selectedMultiselect)
                choiceContainer = new ListMultipleChoice<AmpCategoryValue>(
                        "choice", model, choices, new TranslatedChoiceRenderer<AmpCategoryValue>());
            else
                choiceContainer = new DropDownChoice<AmpCategoryValue>(
                        "choice",
                        new AmpMultiValueDropDownChoiceModel<AmpCategoryValue>(model), choices, new TranslatedChoiceRenderer<AmpCategoryValue>())
                        .setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }

    public AmpCategorySelectFieldPanel(String id, String categoryKey,
            IModel<Set<AmpCategoryValue>> model, String fmName,
            boolean ordered, Boolean nullValid, Boolean isMultiselect,IModel<Set<AmpCategoryValue>> relatedChoicesModel, AmpFMTypes fmType) throws Exception {
        this(id, categoryKey, model, fmName, ordered, nullValid, isMultiselect, relatedChoicesModel);
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
     *
     * MERGE: shoudn't be merged
     */
    public AmpCategorySelectFieldPanel(String id, String categoryKey,
                                       IModel<AmpCategoryValue> model, String fmName,
                                       boolean ordered, Boolean nullValid, boolean hideLabel,
                                       IModel<Set<AmpCategoryValue>> relatedChoicesModel) throws Exception {
        super(id,categoryKey,fmName,ordered,false,relatedChoicesModel,hideLabel);
        choiceContainer = new DropDownChoice<AmpCategoryValue>(
                "choice",model, choices, new TranslatedChoiceRenderer<AmpCategoryValue>())
                .setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }
}
