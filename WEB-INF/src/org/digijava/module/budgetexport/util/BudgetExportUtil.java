package org.digijava.module.budgetexport.util;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/1/12
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class BudgetExportUtil {
    private static final String CSV_ROW_DELIMITER = "\n";
    private static final String CSV_COL_DELIMITER = ",";

    public static Map<String, String> parseCSV (String csvContent, boolean hasHeader) {
        Map<String, String> retVal = new HashMap<String, String> ();
        StringTokenizer rows = new StringTokenizer(csvContent, CSV_ROW_DELIMITER, false);

        //Skip header row
        if (rows.hasMoreElements() && hasHeader) {
            rows.nextToken();
        }

        while (rows.hasMoreElements()) {
            String row = rows.nextToken();

            if (row.indexOf("\"") < 0) { //skip for now
                StringTokenizer cols = new StringTokenizer(row, CSV_COL_DELIMITER, false);
                retVal.put(cols.nextToken(), cols.nextToken());
            }
        }
        return  retVal;
    }
    
    public static List<AmpBudgetExportMapItem> matchAndGetMapItems (Map<String, String> csvMap, List<HierarchyListable> ampObjects, AmpBudgetExportMapRule rule) {
        List<AmpBudgetExportMapItem> retVal = new ArrayList<AmpBudgetExportMapItem>();
        int matchFragmentLength = 5;
        Set keySet = csvMap.keySet();
        
        Iterator<String> keySetIt = keySet.iterator();
        Set<String> matchedOnes = new HashSet<String>();

        boolean matchedFlag = false;
        while (keySetIt.hasNext()) {
            String key = keySetIt.next();
            String val = csvMap.get(key);

            AmpBudgetExportMapItem newItem = new AmpBudgetExportMapItem();
            newItem.setImportedCode(key);
            newItem.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_NONE);
            newItem.setImportedLabel(val);
            newItem.setRule(rule);

            for (HierarchyListable ampObj : ampObjects) {
                matchedFlag = false;
                //check exact match
                if (val.trim().equalsIgnoreCase(ampObj.getLabel().trim()) ||
                        (ampObj.getAdditionalSearchString() != null && val.trim().equalsIgnoreCase(ampObj.getAdditionalSearchString().trim()))) {
                    newItem.setAmpObjectID(Long.parseLong(ampObj.getUniqueId()));
                    newItem.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_EXACT);
                    newItem.setAmpLabel(ampObj.getLabel());
                    matchedFlag = true;
                } else {
                    if (val.length() > matchFragmentLength) {

                        for (int offset = 0; offset < val.length() - matchFragmentLength; offset ++) {
                            if (ampObj.getLabel().toLowerCase().indexOf(val.toLowerCase().substring(offset, offset + matchFragmentLength)) > -1) {
                                newItem.setAmpObjectID(Long.parseLong(ampObj.getUniqueId()));
                                newItem.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_SOME);
                                newItem.setAmpLabel(ampObj.getLabel());
                                matchedFlag = true;
                            }
                        }
                    }
                }
                if (matchedFlag) break;
            }
            retVal.add(newItem);
        }

        return retVal;
    }
    
    public static List<HierarchyListable> searchAmpEntity (List<HierarchyListable> searchIn, String searchStr) {
        List<HierarchyListable> retVal = new ArrayList<HierarchyListable>();
        for (HierarchyListable obj : searchIn) {
            if (obj.getLabel().toLowerCase().startsWith(searchStr.toLowerCase())) {
                retVal.add(obj);
            }
        }
        Collections.sort(retVal, new Comparator<HierarchyListable>() {
            @Override
            public int compare(HierarchyListable o1, HierarchyListable o2) {
                return o1.getLabel().toLowerCase().compareTo(o2.getLabel().toLowerCase());
            }
        });
        return  retVal;
    }
    
    public static List<AmpEntityMappedItem> getAmpEntityMappedItems (AmpBudgetExportMapRule rule) throws DgException {
        List<AmpEntityMappedItem> retVal = new ArrayList<AmpEntityMappedItem>();
        MappingEntityAdapter adapter = MappingEntityAdapterUtil.getEntityAdapter(rule.getAmpColumn().getExtractorView());
        List<HierarchyListable> ampEntityList = adapter.getAllObjects();

        List<AmpBudgetExportMapItem> mapItems = rule.getItems();
        Collections.sort(ampEntityList, new Comparator<HierarchyListable>() {
            public int compare(HierarchyListable o1, HierarchyListable o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });

        for (HierarchyListable ampEntity : ampEntityList) {
            AmpEntityMappedItem ampEntityMappedItem = new AmpEntityMappedItem();
            ampEntityMappedItem.setAmpEntity(ampEntity);

            for(AmpBudgetExportMapItem mapItem : mapItems) {
                Long ampEntityId = Long.parseLong(ampEntity.getUniqueId());
                if (ampEntityId.equals(mapItem.getAmpObjectID())) {
                    ampEntityMappedItem.setMapItem(mapItem);
                    //break;
                }
            }

            retVal.add(ampEntityMappedItem);
        }
        return retVal;
    }
}
