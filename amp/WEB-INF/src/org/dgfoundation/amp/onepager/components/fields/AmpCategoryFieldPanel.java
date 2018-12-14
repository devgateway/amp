/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * This class is used by all Category manager controls on the page, by
 * subclassing and implementing constructors that populate the
 * {@link AmpCategoryFieldPanel#choiceContainer} based on what type of control
 * we have (group or select)
 * 
 * @author mpostelnicu@dgateway.org since Sep 27, 2010
 */
public abstract class AmpCategoryFieldPanel extends
        AmpFieldPanel<AmpCategoryValue> {

    private static final long serialVersionUID = 8917920670614629713L;
    protected IModel<List<? extends AmpCategoryValue>> choices;
    protected Boolean selectedMultiselect;
    protected String categoryKey;
    protected boolean ordered;
    protected AbstractChoice<?, AmpCategoryValue> choiceContainer;
    protected final IModel<Set<AmpCategoryValue>> relatedChoicesModel;

    public AbstractChoice<?, AmpCategoryValue> getChoiceContainer() {
        return choiceContainer;
    }

    /**
     * 
     * @see AmpCategoryFieldPanel#AmpCategoryFieldPanel(String, String, String,
     *      boolean, Boolean, IModel)
     */
    public AmpCategoryFieldPanel(String id, String categoryKey, String fmName,
            boolean ordered, Boolean isMultiselect) throws Exception {
        this(id, categoryKey, fmName, ordered, isMultiselect, null);
    }

    /**
     * Constructs a category field panel used by all types of category manager
     * controls.
     * 
     * @param id
     *            the id of the control on the page
     * @param categoryKey
     *            the category key of the category displayed
     * @param fmName
     *            the feature manager name from FM
     * @param ordered
     *            if this category list is ordered
     * @param isMultiselect
     *            if this category is multiselect. Use null here to read the
     *            database state of this category, or true/false to override
     *            that behavior
     * @param relatedChoicesModel
     *            if this parameter is not null, it will be used to iterate all
     *            {@link AmpCategoryValue}S and retrieve the
     *            {@link AmpCategoryValue#getUsedByValues()} for each, making
     *            sure this control only displays {@link AmpCategoryValue}S that
     *            are used by the related {@link AmpCategoryValue}S This is used
     *            to filter linked categories
     * @see CategoryConstants#IMPLEMENTATION_LEVEL_NAME
     * @see CategoryConstants#IMPLEMENTATION_LOCATION_NAME
     * @throws Exception
     */
    public AmpCategoryFieldPanel(String id, String categKey, String fmName,
            boolean ordered, Boolean isMultiselect,
            final IModel<Set<AmpCategoryValue>> relatedChoicesModel,boolean hideLabel, boolean hideNewLine) throws Exception {
        super(id, fmName,hideLabel, hideNewLine);

        //set the categoryKey accordingly
        this.categoryKey = getAlternateKey(getSession(), categKey);

        selectedMultiselect = isMultiselect;
        this.relatedChoicesModel = relatedChoicesModel;
        if (selectedMultiselect == null) {
            AmpCategoryClass categoryClass = CategoryManagerUtil
                    .loadAmpCategoryClassByKey(categoryKey);
            selectedMultiselect = categoryClass.isMultiselect();
        }
        
        choices = new AbstractReadOnlyModel<List<? extends AmpCategoryValue>>() {
            @Override
            public List<AmpCategoryValue> getObject() {
                try
                {
                    List<AmpCategoryValue> collectionByKey = CategoryManagerUtil.getAllAcceptableValuesForACVClass(categoryKey, relatedChoicesModel == null ? null : relatedChoicesModel.getObject());
                    return collectionByKey;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        };
        
    }

    /**
     * looks up for an alternate category based on the workspace prefix
     * @param s
     * @param categKey
     * @return
     */
    public static String getAlternateKey(Session s, final String categKey) {
        AmpAuthWebSession session = (AmpAuthWebSession) s;
        if (session.getCurrentMember().getWorkspacePrefix() != null &&( !categKey.equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LEVEL_KEY) &&
                !categKey.equalsIgnoreCase(CategoryConstants.IMPLEMENTATION_LOCATION_KEY) ) ){
            String tmpKey = session.getCurrentMember().getWorkspacePrefix().getValue() + categKey;
            if (CategoryManagerUtil.loadAmpCategoryClassByKey(tmpKey) != null)
            {
                return tmpKey; // a prefixed category exists -> return its key
            }
        }
        return categKey;
    }

    public AmpCategoryFieldPanel(String id, final String categoryKey, String fmName,
                                 boolean ordered, Boolean isMultiselect,
                                 final IModel<Set<AmpCategoryValue>> relatedChoicesModel,boolean hideLabel) throws Exception {
        this(id,categoryKey,fmName,ordered,isMultiselect,relatedChoicesModel, hideLabel, true);
    }
    
    public AmpCategoryFieldPanel(String id, String categoryKey, String fmName,
            boolean ordered, Boolean isMultiselect,
            IModel<Set<AmpCategoryValue>> relatedChoicesModel) throws Exception  {
        this(id,categoryKey,fmName,ordered,isMultiselect,relatedChoicesModel,false);
    }
}
