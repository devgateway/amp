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

        OptionDecorator(Long id, String indicatorName) {
            this.id = id;
        }

        private Long id;

        private String indicatorName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getIndicatorName() {
            return indicatorName;
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
                return /*allOptions.get(object) == null ? "Select One" : */allOptions.get(object).getAmpIndicatorOptionName();
            }

            @Override
            public String getIdValue(Long object, int index) {
                return object.toString();
            }
        };

        Set<AmpAidEffectivenessIndicatorOption> optionList = AidEffectivenessIndicatorUtil.populateSelectedOptions(am.getObject());
        /*
        List<OptionDecorator> decoratorList = new ArrayList<>();
        for (AmpAidEffectivenessIndicatorOption option : optionList) {
            OptionDecorator decorator = null;
            if (option.getAmpIndicatorOptionId() != null) {
                decorator = new OptionDecorator(option.getAmpIndicatorOptionId(),
                        option.getIndicator().getAmpIndicatorName());
            } else {
                decorator = new OptionDecorator(-1l,
                        option.getIndicator().getAmpIndicatorName());
            }

            decoratorList.add(decorator);
        }*/



        final IModel<Set<AmpAidEffectivenessIndicatorOption>> listModel = new Model((Serializable)optionList);

        final ListEditor<AmpAidEffectivenessIndicatorOption> indicatorsList = new ListEditor<AmpAidEffectivenessIndicatorOption>(
                "selectedEffectivenessIndicatorOptions", listModel) {


            @Override
            public void updateModel() {
                am.getObject().getSelectedEffectivenessIndicatorOptions().clear();
                Set<AmpAidEffectivenessIndicatorOption> allOptionsOnFM = getModel().getObject();
                for (AmpAidEffectivenessIndicatorOption o : allOptionsOnFM) {
                    if (o.getAmpIndicatorOptionId() != null) {
                        am.getObject().getSelectedEffectivenessIndicatorOptions().add(o);
                    }
                }

                // OVERRDING OF THIS METHOD IS VERY IMPORTANT!!!
                // we update the model "on the fly". This method overrides these efforts in the end (before save)
                //super.updateModel();
            }

            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpAidEffectivenessIndicatorOption> componentOuter) {

                AmpAidEffectivenessIndicator indicator = componentOuter.getModelObject().getIndicator();
                AmpFieldPanel<Long> indicatorChoices = null;
                AbstractSingleSelectChoice choiceContainer = null;

                        List<Long> options = new ArrayList<Long>();
                for (AmpAidEffectivenessIndicatorOption o : indicator.getOptions()) {
                    options.add(o.getAmpIndicatorOptionId());
                }

                AmpAidEffectivenessIndicatorOption option = componentOuter.getModelObject();
                OptionDecorator decorator = new OptionDecorator(option.getAmpIndicatorOptionId(),
                        option.getIndicator().getAmpIndicatorName());

                PropertyModel<Long> decoratorModel = new PropertyModel<Long>(decorator, "id") {
                    @Override
                    public void setObject(Long object) {
                        // it could be null because no value has been selected yet
                        if (getObject() != null) {
                            AmpAidEffectivenessIndicatorOption option = new AmpAidEffectivenessIndicatorOption();
                            option.setAmpIndicatorOptionId(getObject());
                            listModel.getObject().remove(option);
                        }
                        listModel.getObject().add(AmpAidEffectivenessFormSectionFeature.this.allOptions.get(object));

                        super.setObject(object);
                    }
                };

                if (indicator.getIndicatorType() == 0) {
                    indicatorChoices = new AmpGroupFieldPanel<Long>(
                            "ampIndicatorOptionId", decoratorModel, options,
                            indicator.getAmpIndicatorName(), false, false, renderer, indicator.getTooltipText());
                    choiceContainer = (AbstractSingleSelectChoice)((AmpGroupFieldPanel)indicatorChoices).getChoiceContainer();
                } else {
                    indicatorChoices = new AmpSelectFieldPanel <Long>(
                            "ampIndicatorOptionId", decoratorModel, options,
                            indicator.getAmpIndicatorName(), false, false, renderer, false);

                    choiceContainer = (AbstractSingleSelectChoice)((AmpSelectFieldPanel)indicatorChoices).getChoiceContainer();
                }

                if (indicator.getMandatory()) {
                    choiceContainer.setRequired(true);
                    requiredFormComponents.add(choiceContainer);
                } else {
                    choiceContainer.setNullValid(true);
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