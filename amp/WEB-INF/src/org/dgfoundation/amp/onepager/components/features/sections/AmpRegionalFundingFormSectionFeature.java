/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.items.AmpRegionalFundingItemFeaturePanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * The regionalfunding section of the activity form.
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpRegionalFundingFormSectionFeature extends
        AmpFormSectionFeaturePanel {

    protected ListView<AmpCategoryValueLocations> list;

    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpRegionalFundingFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        final IModel<Set<AmpRegionalFunding>> setModel = new PropertyModel<Set<AmpRegionalFunding>>(
                am, "regionalFundings");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet());

        final IModel<Set<AmpActivityLocation>> locationModel = new PropertyModel<Set<AmpActivityLocation>>(
                am, "locations");
        if (locationModel.getObject() == null)
            locationModel.setObject(new HashSet());

        /**
         * The following readonly model creates a set of the
         * AmpCategoryValueLocations
         */
        AbstractReadOnlyModel<List<AmpCategoryValueLocations>> listModel = new AbstractReadOnlyModel<List<AmpCategoryValueLocations>>() {
            @Override
            public List<AmpCategoryValueLocations> getObject() {
                Set<AmpCategoryValueLocations> s = new HashSet<AmpCategoryValueLocations>();
                if (locationModel.getObject() == null)
                    locationModel.setObject(new HashSet());
                for (AmpActivityLocation location : locationModel.getObject())
                    if (CategoryConstants.IMPLEMENTATION_LOCATION_REGION.equalsCategoryValue(location
                            .getLocation().getLocation()
                            .getParentCategoryValue()))
                        s.add(location.getLocation().getLocation());
                return new ArrayList<AmpCategoryValueLocations>(s);
            }
        };

        list = new ListView<AmpCategoryValueLocations>("listRegions", listModel) {
            @Override
            protected void populateItem(
                    final ListItem<AmpCategoryValueLocations> item) {
                AmpRegionalFundingItemFeaturePanel fundingItemFeature;
                try {
                    fundingItemFeature = new AmpRegionalFundingItemFeaturePanel(
                            "regionItem", "Region Item", am, setModel,
                                    item.getModel());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                item.add(fundingItemFeature);
            }
        };

        list.setReuseItems(true);
        add(list);

    }

    public ListView<AmpCategoryValueLocations> getList() {
        return list;
    }

}
