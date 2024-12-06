/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpRegionalFundingItemFeaturePanel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    protected AmpMEFormSectionFeature meFormSection;
    public AmpRegionalFundingFormSectionFeature(String id, String fmName,
                                                final IModel<AmpActivityVersion> am, AmpComponentPanel meFormSectionFeature) throws Exception {
        super(id, fmName, am);
        this.meFormSection = (AmpMEFormSectionFeature) meFormSectionFeature;
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
                for (AmpActivityLocation location : locationModel.getObject()) {
                    if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1.equalsCategoryValue(
                            location.getLocation().getParentCategoryValue())) {
                        s.add(location.getLocation());
                    }
                }
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
        list.setOutputMarkupId(true);
        list.setReuseItems(true);
        add(list);

    }

    public ListView<AmpCategoryValueLocations> getList() {
        return list;
    }

    public AmpMEFormSectionFeature getMeFormSection() {
        return meFormSection;
    }

    public void setMeFormSection(AmpMEFormSectionFeature meFormSection) {
        this.meFormSection = meFormSection;
    }
}
