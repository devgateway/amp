/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.sections;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.*;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEItemFeaturePanel;
import org.dgfoundation.amp.onepager.events.LocationChangedEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * M&E section
 *
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEFormSectionFeature extends AmpFormSectionFeaturePanel {
//    private final ListView<IndicatorActivity> list;

    protected ListView<AmpActivityLocation> tabsList;

    protected ListView<AmpActivityLocation> indicatorLocationList;

    private Map<AmpActivityLocation, AmpMEItemFeaturePanel> locationIndicatorItems = new TreeMap<>();

    private boolean isTabsView = true;

    final List<AmpActivityLocation> locations;

//    final WebMarkupContainer wmc;

    public AmpMEFormSectionFeature(String id, String fmName,
                                   final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        if (am.getObject().getIndicators() == null) {
            am.getObject().setIndicators(new HashSet<>());
        }
        locations = new ArrayList<>(am.getObject().getLocations());

        ListView<AmpActivityLocation> listView = new ListView<AmpActivityLocation>("listView", locations) {

            @Override
            protected void populateItem(org.apache.wicket.markup.html.list.ListItem<AmpActivityLocation> listItem) {
                listItem.add(new Label("item", listItem.getModelObject().getLocation().getName()));

            }
        };
        add(listView);
        add(UpdateEventBehavior.of(LocationChangedEvent.class));

        tabsList = new ListView<AmpActivityLocation>("locationItemsForTabs", locations) {
            private static final long serialVersionUID = -206108834217110807L;

            @Override
            protected void populateItem(org.apache.wicket.markup.html.list.ListItem<AmpActivityLocation> item) {
                AmpCategoryValueLocations location = item.getModel().getObject().getLocation();
                String locationName = location.getName();
                String locationIso = location.getIso3();

                if (location.getParentLocation() != null) {
                    locationIso = location.getParentLocation().getIso3();
                }

                if (location.getParentLocation() != null) {
                    locationName = location.getParentLocation().getName() + "[" + location.getName() + "]";
                }

                ExternalLink l = new ExternalLink("locationLinkForTabs", "#tab" + (item.getIndex() + 1));
                l.add(new AttributePrepender("title", new Model<String>(locationName), ""));

                Label label = new Label("locationTabsLabel", new Model<String>(locationIso));

                l.add(label);

                item.add(l);
            }

        };
        tabsList.setVisibilityAllowed(isTabsView);
        tabsList.setOutputMarkupId(true);
        add(tabsList);

        indicatorLocationList = new ListView<AmpActivityLocation>("listIndicatorLocation", locations) {


            @Override
            protected void populateItem(org.apache.wicket.markup.html.list.ListItem<AmpActivityLocation> item) {
                AmpMEItemFeaturePanel indicatorLoc = null;
                try {
                    indicatorLoc = new AmpMEItemFeaturePanel("indicatorLocation", "ME Item Location", item.getModel(), am, locations,
                            AmpMEFormSectionFeature.this);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                indicatorLoc.setTabIndex(item.getIndex());

                item.add(new AttributePrepender("data-is_location_tab", new Model<String>("true"), ""));
                locationIndicatorItems.put(item.getModelObject(), indicatorLoc);

                item.add(indicatorLoc);
            }

        };
        indicatorLocationList.setOutputMarkupId(true);
        add(indicatorLocationList);
        this.add(UpdateEventBehavior.of(AmpActivityLocation.class));
    }

    public void updateAmpLocationModel(AmpActivityLocation selectedLocation) {
        locations.add(selectedLocation);
    }

    /*public void reloadMeFormSection(AjaxRequestTarget target) {
        target.add(AmpMEFormSectionFeature.this);
        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
        target.appendJavaScript("indicatorTabs();");
    }*/
    public boolean isTabsView() {
        return isTabsView;
    }

    public void clearLocations(AmpCategoryValueLocations locationToClear) {
        if (locations != null) {
            if (locationToClear != null) {
                locations.removeIf(location -> location.getLocation().getName().equals(locationToClear.getName()) &&
                        location.getLocation().getIso().equals(locationToClear.getIso()));
            } else {
                locations.clear();
            }
        }

    }
}
