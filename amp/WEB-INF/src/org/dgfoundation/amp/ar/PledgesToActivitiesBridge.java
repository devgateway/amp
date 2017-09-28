package org.dgfoundation.amp.ar;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedViewsRepository;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.dbentity.AmpColumns;

public class PledgesToActivitiesBridge {
    
    /**
     * map<activity_extractor_view, pledge_extractor_view_name> for (meta)text columns
     */
     public final static Map<String, BridgeItem> activityViewToPledgeView = Collections.unmodifiableMap(new HashMap<String, BridgeItem>() {{
                                                                                add("v_titles", "v_pledges_titles");
                                                                            
                                                                                add("v_sectors", "v_pledges_sectors");
                                                                                add("v_secondary_sectors", "v_pledges_secondary_sectors");
                                                                                add("v_tertiary_sectors", "v_pledges_tertiary_sectors");
    
                                                                                add("v_primaryprogram_level_1", "v_pledges_programs");
                                                                                add("v_secondaryprogram_level_1", "v_pledges_secondary_programs");
                                                                                add("v_tertiaryprogram_level_1", "v_pledges_tertiary_programs");
                                                                                add("v_nationalobjectives_level_1", "v_pledges_npd_objectives");
                                                                            
                                                                                add("v_regions", "v_pledges_regions");
                                                                                add("v_zones", "v_pledges_zones");
                                                                                add("v_districts", "v_pledges_districts");
                                                                            
                                                                                add("v_donor_groups", "v_pledges_donor_group");
                                                                                add("v_donor_type", "v_pledges_donor_type");
                                                                            }
     
                                                                            public void add(String activitiesView, String pledgesView) {
                                                                                if (this.containsKey(activitiesView))
                                                                                    throw new RuntimeException("doubly-defined activity->pledge view name: " + activitiesView);
                                                                                
                                                                                if (!SQLUtils.tableExists(activitiesView))
                                                                                    throw new RuntimeException("trying to define a pledges twin for the nonexistant activities view " + activitiesView);
                                                                                
                                                                                if (!SQLUtils.tableExists(pledgesView))
                                                                                    throw new RuntimeException("trying to define a nonexistant view as a pledges twin view: " + activitiesView);

                                                                                boolean i18nXor = InternationalizedViewsRepository.i18Models.containsKey(activitiesView) ^ InternationalizedViewsRepository.i18Models.containsKey(pledgesView);
                                                                                /*if (i18nXor)
                                                                                    throw new RuntimeException("activity-view and pledges view should either be both i18n or none i18n: " + activitiesView + " vs. " + pledgesView);*/
                                                                                put(activitiesView, new BridgeItem(pledgesView, ARUtil.getColumnForView(activitiesView).getColumnName()));
                                                                            }
                                                                        });
     
     public static boolean hasCorrespondingPledgeView(AmpColumns col) {
         return activityViewToPledgeView.containsKey(col.getExtractorView());
     }
     
     public static BridgeItem getCorrespondingPledgeView(AmpColumns col) {
         return activityViewToPledgeView.get(col.getExtractorView());
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
