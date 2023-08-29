/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.validators.AmpUniqueComponentTitleValidator;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.ComponentsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.*;

/**
 * @author aartimon@dginternational.org
 * since Oct 28, 2010
 */
public class AmpComponentIdentificationFormTableFeature extends AmpSubsectionFeaturePanel {
    private boolean titleSelected;
    private boolean typeSelected;
    private WebMarkupContainer feedbackContainer;
    private Label feedbackLabel;

    /**
     * @param id
     * @param componentsFundingsSetModel 
     * @param componentFundingSection 
     * @param fmName
     * @param contractNameLabel 
     * @param am
     * @throws Exception
     */
    public AmpComponentIdentificationFormTableFeature(String id, IModel<AmpActivityVersion> activityModel, 
            final IModel<AmpComponent> componentModel, final PropertyModel<Set<AmpComponentFunding>> componentsFundingsSetModel, String fmName, final Label componentNameLabel) throws Exception{
        super(id, fmName, activityModel, false, true);
        
        final IModel<Set<AmpComponent>> setModel = new PropertyModel<Set<AmpComponent>>(activityModel, "components");


        AmpSelectFieldPanel<AmpComponentType> compTypes = new AmpSelectFieldPanel<AmpComponentType>("type", new PropertyModel<AmpComponentType>(componentModel, "type"),
                    new LoadableDetachableModel<List<AmpComponentType>>() {
                        @Override
                        protected List<AmpComponentType> load() {
                            return new ArrayList(ComponentsUtil.getAmpComponentTypes(true));
                        }
                    }, COMPONENT_TYPE, false, false, new ChoiceRenderer<AmpComponentType>("name")){
            /**
             * 
             */
            private static final long serialVersionUID = 1L;
            @Override
            public boolean dropDownChoiceIsDisabled(
                    AmpComponentType object,
                    int index,
                    String selected) {
                if (object.getSelectable())
                    return false;
                else
                    return true;
            }
        };
        compTypes.setOutputMarkupId(true);
        compTypes.getChoiceContainer().setRequired(true);
        compTypes.getChoiceContainer().add(new AttributeAppender("style", true, new Model("max-width: 300px"), ";"));
        add(compTypes);
        
        final AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("name", new PropertyModel<String>(componentModel, "title"), COMPONENT_TITLE);
        name.setTextContainerDefaultMaxSize();
        name.setOutputMarkupId(true);
        name.getTextContainer().setRequired(true);
        name.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur"){
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(componentNameLabel);
            }
        });
        name.getTextContainer().add(new AmpUniqueComponentTitleValidator(new PropertyModel<AmpActivityGroup>(activityModel, "ampActivityGroup")));
        add(name);

        AmpTextAreaFieldPanel description = new AmpTextAreaFieldPanel("description", new PropertyModel(componentModel, "description"), COMPONENT_DESCRIPTION, false, false, false);
        add(description);

    }

}
