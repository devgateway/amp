package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.dbentity.AmpColumns;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PledgesToActivitiesBridge {
    
    /**
     * map<activity_extractor_view, pledge_extractor_view_name> for (meta)text columns
     */
     public static final Map<String, BridgeItem> ACTIVITY_VIEW_TO_PLEDGE_VIEW =
        Collections.unmodifiableMap(new HashMap<String, BridgeItem>() {{
            add("v_titles", "v_pledges_titles");
        
            add("v_sectors", "v_pledges_sectors");
            add("v_secondary_sectors", "v_pledges_secondary_sectors");
            add("v_tertiary_sectors", "v_pledges_tertiary_sectors");

            add("v_primaryprogram_level_1", "v_pledges_programs");
            add("v_secondaryprogram_level_1", "v_pledges_secondary_programs");
            add("v_tertiaryprogram_level_1", "v_pledges_tertiary_programs");
            add("v_nationalobjectives_level_1", "v_pledges_npd_objectives");
        
            add("v_adm_level_1", "v_pledges_adm_level_1");
            add("v_adm_level_2", "v_pledges_adm_level_2");
            add("v_adm_level_3", "v_pledges_adm_level_3");
        
            add("v_donor_groups", "v_pledges_donor_group");
            add("v_donor_type", "v_pledges_donor_type");
        }

        public void add(String activitiesView, String pledgesView) {
            if (this.containsKey(activitiesView)) {
                throw new RuntimeException("doubly-defined activity->pledge view name: " + activitiesView);
            }
            
            if (!SQLUtils.tableExists(activitiesView)) {
                throw new RuntimeException("trying to define a pledges twin for the nonexistant activities view "
                        + activitiesView);
            }
            
            if (!SQLUtils.tableExists(pledgesView)) {
                throw new RuntimeException("trying to define a nonexistant view as a pledges twin view: "
                        + activitiesView);
            }

            put(activitiesView, new BridgeItem(pledgesView, ARUtil.getColumnForView(activitiesView).getColumnName()));
        }
    });
     
     public static boolean hasCorrespondingPledgeView(AmpColumns col) {
         return ACTIVITY_VIEW_TO_PLEDGE_VIEW.containsKey(col.getExtractorView());
     }
     
     public static BridgeItem getCorrespondingPledgeView(AmpColumns col) {
         return ACTIVITY_VIEW_TO_PLEDGE_VIEW.get(col.getExtractorView());
     }
     
     
     public static class BridgeItem {
         public final String pledgeView;
         public final String columnName;
         
         public BridgeItem(String pledgeView, String columnName) {
             this.pledgeView = pledgeView;
             this.columnName = columnName;
         }
     }
}
