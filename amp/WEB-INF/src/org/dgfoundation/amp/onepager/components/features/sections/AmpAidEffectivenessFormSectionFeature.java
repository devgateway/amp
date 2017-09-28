package org.dgfoundation.amp.onepager.components.features.sections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.digijava.module.aim.util.AidEffectivenessIndicatorUtil;


public class AmpAidEffectivenessFormSectionFeature extends
        AmpFormSectionFeaturePanel 
        implements AmpRequiredComponentContainer {
    
    private List<org.apache.wicket.markup.html.form.FormComponent<?>> requiredFormComponents = new ArrayList<org.apache.wicket.markup.html.form.FormComponent<?>>();


    private Map<Long, AmpAidEffectivenessIndicatorOption> allOptions = null;

    public AmpAidEffectivenessFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);

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
                Set<AmpAidEffectivenessIndicatorOption> allOptionsOnFM = getModel().getObject();
                for (AmpAidEffectivenessIndicatorOption o : allOptionsOnFM) {
                    if (o.getAmpIndicatorOptionId() != null) {
                        am.getObject().getSelectedEffectivenessIndicatorOptions().add(o);
                    }
                }

                // overriding of this method is very important !!!
                // we update the model "on the fly". This method overrides these efforts in the end (before save)
                //super.updateModel();
            }

            @Override
            protected void onPopulateItem(ListItem<AmpAidEffectivenessIndicatorOption> componentOuter) {

                AmpAidEffectivenessIndicator indicator = componentOuter.getModelObject().getIndicator();
                AmpFieldPanel<Long> indicatorChoices = null;
                AbstractSingleSelectChoice choiceContainer = null;

                        List<Long> options = new ArrayList<Long>();
                for (AmpAidEffectivenessIndicatorOption o : indicator.getOptions()) {
                    options.add(o.getAmpIndicatorOptionId());
                }

                AmpAidEffectivenessIndicatorOption option = componentOuter.getModelObject();
                OptionDecorator decorator = new OptionDecorator(option.getAmpIndicatorOptionId());

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

                final String indicatorName = indicator.getAmpIndicatorName();
                if (AmpAidEffectivenessIndicator.IndicatorType.SELECT_LIST.ordinal() == indicator.getIndicatorType()) {
                    indicatorChoices = new AmpGroupFieldPanel<Long>(
                            "ampIndicatorOptionId", decoratorModel, options,
                            indicator.getFmName(), false, false, renderer, indicator.getTooltipText()) {
                        
                            @Override
                            protected void configureTranslatorLinks() {
                                this.setTitleTranslatorEnabled(false);
                                this.setTooltipTranslatorEnabled(false);
                            };
                            
                            @Override
                            protected void configureLabelText() {
                                this.setLabelText(indicatorName);
                            };
                        };
                    choiceContainer = (AbstractSingleSelectChoice)((AmpGroupFieldPanel)indicatorChoices).getChoiceContainer();
                } else {
                    indicatorChoices = new AmpSelectFieldPanel <Long>(
                            "ampIndicatorOptionId", decoratorModel, options,
                            indicator.getFmName(), false, false, renderer, false) {
                            
                            @Override
                            protected void configureTranslatorLinks() {
                                this.setTitleTranslatorEnabled(false);
                                this.setTooltipTranslatorEnabled(false);
                            };
                            
                            @Override
                            protected void configureLabelText() {
                                this.setLabelText(indicatorName);
                            };
                    };

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
}
