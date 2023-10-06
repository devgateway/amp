/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.DbUtil;

import java.util.HashSet;
import java.util.List;

/**
 * M&E section
 * @author aartimon@dginternational.org 
 * @since Feb 10, 2011
 */
public class AmpMEFormSectionFeature extends AmpFormSectionFeaturePanel {
    private final ListView<IndicatorActivity> list;

    public AmpMEFormSectionFeature(String id, String fmName,
                                   final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        if (am.getObject().getIndicators() == null) {
            am.getObject().setIndicators(new HashSet<>());
        }

        final IModel<List<IndicatorActivity>> listModel = OnePagerUtil
                .getReadOnlyListModelFromSetModel(new PropertyModel(am, "indicators"));

        final AmpUniqueCollectionValidatorField<IndicatorActivity> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<IndicatorActivity>(
                "uniqueMEValidator", listModel, "Unique MEs Validator") {

            @Override
            public Object getIdentifier(IndicatorActivity t) {
                return t.getIndicator().getName();
            }
        };
        add(uniqueCollectionValidationField);


        list = new ListView<IndicatorActivity>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<IndicatorActivity> item) {
                AmpMEItemFeaturePanel indicator = null;
                try {
                    indicator = new AmpMEItemFeaturePanel("item", "ME Item", item.getModel(), PersistentObjectModel.getModel(item.getModelObject().getIndicator()), new PropertyModel(item.getModel(), "values"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                item.add(indicator);

                String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this indicator?");
                AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
                        "delete", "Delete ME Item", new Model<String>(translatedMessage)) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        am.getObject().getIndicators().remove(item.getModelObject());
                        uniqueCollectionValidationField.reloadValidationField(target);
                        //setModel.getObject().remove(item.getModelObject());
                        list.removeAll();
                        target.add(AmpMEFormSectionFeature.this);
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
                    }
                };
                item.add(deleteLinkField);
            }
        };
        list.setReuseItems(true);
        add(list);


        final AmpAutocompleteFieldPanel<AmpIndicator> searchIndicators =
                new AmpAutocompleteFieldPanel<AmpIndicator>("search", "Search Indicators",
                        AmpMEIndicatorSearchModel.class) {

                    private static final long serialVersionUID = 1227775244079125152L;

                    @Override
                    protected String getChoiceValue(AmpIndicator choice) {
                        return DbUtil.filter(choice.getName());
                    }

                    @Override
                    public void onSelect(AjaxRequestTarget target, AmpIndicator choice) {
                        IndicatorActivity ia = new IndicatorActivity();
                        ia.setActivity(am.getObject());
                        ia.setIndicator(choice);
                        am.getObject().getIndicators().add(ia);
                        uniqueCollectionValidationField.reloadValidationField(target);

                        //setModel.getObject().add(ia);
                        list.removeAll();
                        target.add(list.getParent());
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
                    }

                    @Override
                    public Integer getChoiceLevel(AmpIndicator choice) {
                        return 0;
                    }

                };

        add(searchIndicators);
    }
}
