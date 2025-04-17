package org.digijava.module.budgetexport.util;

import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;

/**
 * User: flyer
 * Date: 2/6/12
 * Time: 6:50 PM
 */
public class AmpEntityMappedItem {
    private HierarchyListable ampEntity;
    private AmpBudgetExportMapItem mapItem;

    public HierarchyListable getAmpEntity() {
        return ampEntity;
    }

    public void setAmpEntity(HierarchyListable ampEntity) {
        this.ampEntity = ampEntity;
    }

    public AmpBudgetExportMapItem getMapItem() {
        return mapItem;
    }

    public void setMapItem(AmpBudgetExportMapItem mapItem) {
        this.mapItem = mapItem;
    }
}
