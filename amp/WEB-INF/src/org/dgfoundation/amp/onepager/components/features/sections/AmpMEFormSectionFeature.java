/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.*;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEItemFeaturePanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.*;

import java.util.HashSet;

/**
 * M&E section
 * @author aartimon@dginternational.org 
 * @since Feb 10, 2011
 */
public class AmpMEFormSectionFeature extends AmpFormSectionFeaturePanel {
//    private final ListView<IndicatorActivity> list;

    protected ListEditor<AmpActivityLocation> tabsList;

    protected ListEditor<AmpActivityLocation> indicatorLocationList;

    private Map<AmpActivityLocation, AmpMEItemFeaturePanel> locationIndicatorItems = new TreeMap<>();

    private boolean isTabsView = true;

    final IModel<Set<AmpActivityLocation>> locations;

//    final WebMarkupContainer wmc;

    public AmpMEFormSectionFeature(String id, String fmName,
                                   final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        if (am.getObject().getIndicators() == null) {
            am.getObject().setIndicators(new HashSet<>());
        }

//        wmc = new WebMarkupContainer("container");
//        wmc.setOutputMarkupId(true);
//        add(wmc);
//        locations = new PropertyModel(am, "locations");

        locations = new LoadableDetachableModel<Set<AmpActivityLocation>>() {
            @Override
            protected Set<AmpActivityLocation> load() {
                // Load or calculate the locations here
                return am.getObject().getLocations();
            }
        };

        tabsList = new ListEditor<AmpActivityLocation>("locationItemsForTabs", locations) {
            private static final long serialVersionUID = -206108834217110807L;

            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpActivityLocation> item) {
                AmpCategoryValueLocations location = item.getModel().getObject().getLocation();
                String locationName = location.getName();
                String locationIso = location.getIso3();

                if(location.getParentLocation() != null){
                    locationIso = location.getParentLocation().getIso3();
                }

                if(location.getParentLocation() != null){
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

        indicatorLocationList = new ListEditor<AmpActivityLocation>("listIndicatorLocation", locations) {

            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpActivityLocation> item) {
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

    }
    public void updateAmpLocationModel(AmpActivityLocation selectedLocation) {
        am.getObject().getLocations().add(selectedLocation);
        locations.getObject().add(selectedLocation);

    }
    public void reloadMeFormSection(AjaxRequestTarget target){
        target.add(AmpMEFormSectionFeature.this);
//        target.addChildren(tabsList.getParent(), AmpMEFormSectionFeature.this);
        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
        target.appendJavaScript("indicatorTabs();");
    }
    public void addLocationIndicator(AmpActivityVersion indicator){
        if (indicator == null) return;
        indicatorLocationList.updateModel();
    }

    public ListEditor<AmpActivityLocation> getList() {
        return indicatorLocationList;
    }

    public boolean isTabsView() {
        return isTabsView;
    }

    public void setTabsView(boolean tabsView) {
        isTabsView = tabsView;
    }

    public ListEditor<AmpActivityLocation> getTabsList() {
        return tabsList;
    }

    public void setTabsList(ListEditor<AmpActivityLocation> tabsList) {
        this.tabsList = tabsList;
    }

//    public WebMarkupContainer getWmc() {
//        return wmc;
//    }
}
