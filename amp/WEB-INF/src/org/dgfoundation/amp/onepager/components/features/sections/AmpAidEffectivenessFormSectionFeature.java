package org.dgfoundation.amp.onepager.components.features.sections;

import java.io.Serializable;
import java.util.*;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.AidEffectivenessIndicatorUtil;


public class AmpAidEffectivenessFormSectionFeature extends
		AmpFormSectionFeaturePanel 
		implements AmpRequiredComponentContainer {
	
	private List<org.apache.wicket.markup.html.form.FormComponent<?>> requiredFormComponents = new ArrayList<org.apache.wicket.markup.html.form.FormComponent<?>>();


    private Map<Long, AmpAidEffectivenessIndicatorOption> allOptions = null;

    private class OptionDecorator implements Serializable {

        OptionDecorator(Long id) {
            this.id = id;
        }

        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

	public AmpAidEffectivenessFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		AmpAuthWebSession session = (AmpAuthWebSession) getSession();


        allOptions = AidEffectivenessIndicatorUtil.getAllOptions();

        final IChoiceRenderer<Long> renderer = new IChoiceRenderer<Long>() {
            @Override
            public Object getDisplayValue(Long object) {
                return allOptions.get(object).getAmpIndicatorOptionName();
            }

            @Override
            public String getIdValue(Long object, int index) {
                return object.toString();
            }
        };

        Set<AmpAidEffectivenessIndicatorOption> optionList = AidEffectivenessIndicatorUtil.populateSelectedOptions(am.getObject());

        final IModel<Set<AmpAidEffectivenessIndicatorOption>> listModel = new Model((Serializable)optionList);

        final ListEditor<AmpAidEffectivenessIndicatorOption> indicatorsList = new ListEditor<AmpAidEffectivenessIndicatorOption>(
                "selectedEffectivenessIndicatorOptions", listModel) {


            @Override
            public void updateModel() {
                am.getObject().getSelectedEffectivenessIndicatorOptions().clear();
                am.getObject().getSelectedEffectivenessIndicatorOptions().addAll(getModel().getObject());
                // OVERRDING OF THIS METHOD IS VERY IMPORTANT!!!
                // we update the model "on the fly". This method overrides these efforts in the end (before save)
                //super.updateModel();
            }

            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpAidEffectivenessIndicatorOption> componentOuter) {

                AmpAidEffectivenessIndicator indicator = componentOuter.getModelObject().getIndicator();
                AmpFieldPanel<Long> indicatorChoices = null;
                AbstractChoice choiceContainer = null;

                        List<Long> options = new ArrayList<Long>();
                for (AmpAidEffectivenessIndicatorOption o : indicator.getOptions()) {
                    options.add(o.getAmpIndicatorOptionId());
                }


                OptionDecorator decorator = new OptionDecorator(componentOuter.getModelObject().getAmpIndicatorOptionId());
                PropertyModel<Long> decoratorModel = new PropertyModel<Long>(decorator, "id") {
                    @Override
                    public void setObject(Long object) {
                        AmpAidEffectivenessIndicatorOption option = new AmpAidEffectivenessIndicatorOption();
                        option.setAmpIndicatorOptionId(getObject());
                        listModel.getObject().remove(option);
                        listModel.getObject().add(AmpAidEffectivenessFormSectionFeature.this.allOptions.get(object));

                        super.setObject(object);
                    }
                };

                if (indicator.getIndicatorType() == 0) {
                    indicatorChoices = new AmpGroupFieldPanel<Long>(
                            "ampIndicatorOptionId", decoratorModel, options,
                            indicator.getAmpIndicatorName(), false, false, renderer, indicator.getTooltipText());
                    choiceContainer = ((AmpGroupFieldPanel)indicatorChoices).getChoiceContainer();
                } else {
                    indicatorChoices = new AmpSelectFieldPanel <Long>(
                            "ampIndicatorOptionId", decoratorModel, options,
                            indicator.getAmpIndicatorName(), false, false, renderer, false);

                    choiceContainer = ((AmpSelectFieldPanel)indicatorChoices).getChoiceContainer();
                }

                if (indicator.getMandatory()) {
                    choiceContainer.setRequired(true);
                    requiredFormComponents.add(choiceContainer);
                }



                indicatorChoices.setTitleTooltip(new Label("tooltip", indicator.getTooltipText()));
                componentOuter.add(indicatorChoices);
            }

        };

        add(indicatorsList);

	}

    public List<org.apache.wicket.markup.html.form.FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}

}