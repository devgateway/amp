/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.events.ProgramSelectedEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;

import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEItemFeaturePanel extends AmpFeaturePanel<IndicatorActivity> {


    private static final Logger logger = Logger.getLogger(AmpMEItemFeaturePanel.class);
    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    private final ListView<IndicatorActivity> list;

    private Integer tabIndex;

    protected IModel<Set<IndicatorActivity>> parentModel;

    protected IModel<Set<IndicatorActivity>> setModel;

    protected IModel<AmpActivityVersion> am;

    protected List<AmpActivityLocation> parentLocations;

    public AmpMEItemFeaturePanel(String id, String fmName, IModel<AmpActivityLocation> location,
                                 final IModel<AmpActivityVersion> conn, List<AmpActivityLocation> locations) {
        super(id, fmName, true);
        am = conn;
        parentLocations = locations;
        String locationName = location.getObject().getLocation().getName();

        if (location.getObject().getLocation().getParentLocation() != null) {
            locationName = location.getObject().getLocation().getParentLocation().getName() + "[" + location.getObject().getLocation().getName() + "]";
        }

        add(new Label("countryLocation", locationName));

        final IModel<List<IndicatorActivity>> listModel = OnePagerUtil
                .getReadOnlyListModelFromSetModel(new PropertyModel(conn, "indicators"));

        final IModel<List<IndicatorActivity>> filteredListModel = new LoadableDetachableModel<List<IndicatorActivity>>() {
            @Override
            protected List<IndicatorActivity> load() {
                List<IndicatorActivity> allIndicators = listModel.getObject();
                List<IndicatorActivity> filteredIndicators = new ArrayList<>();

                for (IndicatorActivity indicatorActivity : allIndicators) {
                    if (indicatorActivity.getActivityLocation() != null && indicatorActivity.getActivityLocation() == location.getObject()) {
                        filteredIndicators.add(indicatorActivity);
                    }
                }

                return filteredIndicators;
            }
        };

        parentModel = new PropertyModel<>(conn, "indicators");

        setModel = new AbstractMixedSetModel<IndicatorActivity>(parentModel) {
            @Override
            public boolean condition(IndicatorActivity item) {
                return item.getActivityLocation() == location.getObject();
            }
        };

        final AmpUniqueCollectionValidatorField<IndicatorActivity> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<IndicatorActivity>(
                "uniqueMEValidator", filteredListModel, "Unique MEs Validator") {

            @Override
            public Object getIdentifier(IndicatorActivity t) {
                return t.getIndicator().getName();
            }
        };
        add(uniqueCollectionValidationField);
        list = new ListView<IndicatorActivity>("list", filteredListModel) {
            @Override
            protected void populateItem(org.apache.wicket.markup.html.list.ListItem<IndicatorActivity> item) {
                AmpMEIndicatorFeaturePanel indicatorItem = null;
                try {
                    indicatorItem = new AmpMEIndicatorFeaturePanel("item", "ME Item", item.getModel(), PersistentObjectModel.getModel(item.getModelObject().getIndicator()), new PropertyModel(item.getModel(), "values"), location);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                item.add(indicatorItem);

                String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this indicator?");
                AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
                        "delete", "Delete ME Item", new Model<>(translatedMessage)) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        conn.getObject().getIndicators().remove(item.getModelObject());
                        filteredListModel.detach();

                        uniqueCollectionValidationField.reloadValidationField(target);

                        target.add(list.getParent());
                        list.removeAll();
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEItemFeaturePanel.this));
                    }
                };
                item.add(deleteLinkField);
            }
        };
        list.setReuseItems(true);
        add(list);


        final AmpAutocompleteFieldPanel<AmpIndicator> searchIndicators =
                new AmpAutocompleteFieldPanel<AmpIndicator>("searchIndicators", "Search Indicators",
                        AmpMEIndicatorSearchModel.class) {

                    private static final long serialVersionUID = 1227775244079125152L;

                    @Override
                    protected String getChoiceValue(AmpIndicator choice) {
                        return DbUtil.filter(choice.getName());
                    }

                    @Override
                    public void onSelect(AjaxRequestTarget target, AmpIndicator choice) {

                        IndicatorActivity ia = new IndicatorActivity();
                        ia.setActivity(conn.getObject());
                        ia.setIndicator(choice);
                        ia.setActivityLocation(location.getObject());
                        conn.getObject().getIndicators().add(ia);
                        setModel.getObject().add(ia);
                        uniqueCollectionValidationField.reloadValidationField(target);

                        target.add(list.getParent());
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEItemFeaturePanel.this));
                    }

                    @Override
                    public Integer getChoiceLevel(AmpIndicator choice) {
                        return 0;
                    }

                };
        searchIndicators.getModelParams().put(AmpMEIndicatorSearchModel.PARAM.ACTIVITY_PROGRAM, am.getObject().getActPrograms());
        searchIndicators.getModelParams().put(AmpMEIndicatorSearchModel.PARAM.ACTIVITY_LOCATION, am.getObject().getLocations());
        add(UpdateEventBehavior.of(ProgramSelectedEvent.class));
        add(searchIndicators);
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }
}