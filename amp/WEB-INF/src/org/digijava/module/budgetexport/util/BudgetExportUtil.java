package org.digijava.module.budgetexport.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.DummyAmpEntity;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportCSVItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
import org.digijava.module.budgetexport.serviceimport.ObjectRetriever;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/1/12
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class BudgetExportUtil {
    private static Logger logger = Logger.getLogger(BudgetExportUtil.class);

    private static final String CSV_ROW_DELIMITER = "\n";
    private static final String CSV_COL_DELIMITER_COMA = ",";
    private static final String CSV_COL_DELIMITER_TAB = "\t";

    private static final String RETRIEVER_PACKAGE = "org.digijava.module.budgetexport.serviceimport.impl";
    private static final String RETRIEVER_INTERFACE = "org.digijava.module.budgetexport.serviceimport.ObjectRetriever";

    private static Set<Map.Entry<String, String>> serviceObjectRetrievers;

    public static Map<String, String> parseCSV (String csvContent, int delimiter, boolean hasHeader) {
        Map<String, String> retVal = new HashMap<String, String>();
        String delimiterStr = null;

        if (delimiter == AmpBudgetExportMapRule.CSV_COL_DELIMITER_COMA) {
            delimiterStr = CSV_COL_DELIMITER_COMA;
        } else if (delimiter == AmpBudgetExportMapRule.CSV_COL_DELIMITER_TAB) {
            delimiterStr = CSV_COL_DELIMITER_TAB;
        }

        
        StringTokenizer rows = new StringTokenizer(csvContent, CSV_ROW_DELIMITER, false);

        
        //Skip header row
        if (rows.hasMoreElements() && hasHeader) {
            rows.nextToken();
        }

        while (rows.hasMoreElements()) {
            String row = rows.nextToken();

            //if (row.indexOf("\"") < 0) { //skip for now
            if (row.indexOf("\"") > -1) {
                row.replaceAll("\"","");
            }
                StringTokenizer cols = new StringTokenizer(row, delimiterStr, false);
                String code = cols.nextToken().trim();
                String label = cols.nextToken().trim();
                retVal.put(code, label);
            //}
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
                    newItem.setAdditionalLabel(ampObj.getAdditionalSearchString());
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
    
    public static void matchMapItems (List<AmpEntityMappedItem> ampEntityMappedItems, AmpBudgetExportMapRule rule) {
        List<AmpBudgetExportCSVItem> csvItems = rule.getCsvItems();
        int matchFragmentLength = 5;
        int matchedFlag = AmpBudgetExportMapItem.MAP_MATCH_LEVEL_NONE;
        for  (AmpEntityMappedItem mappedItem : ampEntityMappedItems) {
           // if (mappedItem.getMapItem() == null ||
           //         (mappedItem.getMapItem()!=null && !mappedItem.getMapItem().isApproved())){

                AmpBudgetExportMapItem mapItem = null;
                if (mappedItem.getMapItem() != null) {
                    mapItem = mappedItem.getMapItem();
                } else {
                    mapItem = new AmpBudgetExportMapItem();
                    mapItem.setRule(rule);
                    mapItem.setAmpObjectID(Long.parseLong(mappedItem.getAmpEntity().getUniqueId()));
                    mapItem.setAmpLabel(mappedItem.getAmpEntity().getLabel());
                    mapItem.setAdditionalLabel(mappedItem.getAmpEntity().getAdditionalSearchString());
                }

                for (AmpBudgetExportCSVItem csvItem : csvItems) {

                        matchedFlag = AmpBudgetExportMapItem.MAP_MATCH_LEVEL_NONE;;
                        //check exact match
                        if (csvItem.getLabel().trim().equalsIgnoreCase(mappedItem.getAmpEntity().getLabel().trim()) ||
                                (mappedItem.getAmpEntity().getAdditionalSearchString() != null &&
                                        (csvItem.getLabel().trim().equalsIgnoreCase(mappedItem.getAmpEntity().getAdditionalSearchString().trim()) ||
                                                csvItem.getCode().trim().equalsIgnoreCase(mappedItem.getAmpEntity().getAdditionalSearchString().trim())))) {
                            mapItem.setWarning(false);

                                mapItem.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_EXACT);
                            if (!mapItem.isApproved()) {
                                mapItem.setImportedLabel(csvItem.getLabel());
                                mapItem.setImportedCode(csvItem.getCode());
                                mapItem.setApproved(true);//Set approved to exactly matched items
                            } else {
                                if (mapItem.getImportedLabel()==csvItem.getLabel() && mapItem.getImportedCode() != csvItem.getCode()){
                                    mapItem.setWarning(true);
                                }
                                break;
                            }

                            matchedFlag = AmpBudgetExportMapItem.MAP_MATCH_LEVEL_EXACT;;
                        } else if (!mapItem.isApproved()) {

                            if (matchedFlag == AmpBudgetExportMapItem.MAP_MATCH_LEVEL_NONE && (csvItem.getLabel().trim().contains(mappedItem.getAmpEntity().getLabel().trim()) ||
                                                            mappedItem.getAmpEntity().getLabel().trim().contains(csvItem.getLabel().trim()))) {
                                mapItem.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_SOME);
                                mapItem.setImportedLabel(csvItem.getLabel());
                                mapItem.setImportedCode(csvItem.getCode());
                            } else if (csvItem.getLabel().length() > matchFragmentLength) {
                                for (int offset = 0; offset < csvItem.getLabel().length() - matchFragmentLength; offset ++) {
                                    if (mappedItem.getAmpEntity().getLabel().toLowerCase().indexOf(csvItem.getLabel().toLowerCase().substring(offset, offset + matchFragmentLength)) > -1 &&
                                            matchedFlag == AmpBudgetExportMapItem.MAP_MATCH_LEVEL_NONE) { //Take first particular match.
                                        mapItem.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_SOME);
                                        mapItem.setImportedLabel(csvItem.getLabel());
                                        mapItem.setImportedCode(csvItem.getCode());
                                        matchedFlag = AmpBudgetExportMapItem.MAP_MATCH_LEVEL_SOME;
                                    }
                                }
                            }
                        } else {
                            if ((mapItem.getImportedLabel()==csvItem.getLabel() && mapItem.getImportedCode() != csvItem.getCode()) ||
                                    (mapItem.getImportedCode() == csvItem.getCode() && mapItem.getImportedLabel()!=csvItem.getLabel())){

                                mapItem.setWarning(true);
                                break;
                            }

                        }

                        //Do not break after paricular match. Exactly matchin item can be "lower" in the collection
                        if (matchedFlag == AmpBudgetExportMapItem.MAP_MATCH_LEVEL_EXACT ||
                                matchedFlag == AmpBudgetExportMapItem.MAP_MATCH_LEVEL_SOME) {
                            mappedItem.setMapItem(mapItem);
                            if (matchedFlag == AmpBudgetExportMapItem.MAP_MATCH_LEVEL_EXACT) {
                                break;
                            }
                        }

                }
           // }
        }
    }
    
    public static List<HierarchyListable> searchAmpEntity (List<HierarchyListable> searchIn, String searchStr) {
        List<HierarchyListable> retVal = new ArrayList<HierarchyListable>();
        if (searchStr.length()>0) {
            for (HierarchyListable obj : searchIn) {
                if (obj.getLabel().toLowerCase().startsWith(searchStr.toLowerCase())) {
                    retVal.add(obj);
                }
            }
        } else {
            retVal = searchIn;
        }
        Collections.sort(retVal, new Comparator<HierarchyListable>() {
            @Override
            public int compare(HierarchyListable o1, HierarchyListable o2) {
                return o1.getLabel().toLowerCase().compareTo(o2.getLabel().toLowerCase());
            }
        });
        return  retVal;
    }

    public static List<AmpBudgetExportCSVItem> searchCsvItems (List<AmpBudgetExportCSVItem> searchIn, String searchStr) {
        return searchCsvItems (searchIn, searchStr, false);
    }

    public static List<AmpBudgetExportCSVItem> searchCsvItems (List<AmpBudgetExportCSVItem> searchIn, String searchStr, boolean usingCodes) {
        List<AmpBudgetExportCSVItem> retVal = null;

        if (searchStr != null && searchStr.length() > 0) {
            retVal = new ArrayList<AmpBudgetExportCSVItem>();
            for (AmpBudgetExportCSVItem obj : searchIn) {
                if (!usingCodes) {
                    if (obj.getLabel().toLowerCase().startsWith(searchStr.toLowerCase())) {
                        retVal.add(obj);
                    }
                } else {
                    if (obj.getCode().startsWith(searchStr.toLowerCase())) {
                        retVal.add(obj);
                    }
                }
            }
        } else {
            retVal = searchIn;
        }
        if (!usingCodes) {
            Collections.sort(retVal, new Comparator<AmpBudgetExportCSVItem>() {
                @Override
                public int compare(AmpBudgetExportCSVItem o1, AmpBudgetExportCSVItem o2) {
                    return o1.getLabel().toLowerCase().compareTo(o2.getLabel().toLowerCase());
                }
            });
        } else {
            Collections.sort(retVal, new Comparator<AmpBudgetExportCSVItem>() {
                @Override
                public int compare(AmpBudgetExportCSVItem o1, AmpBudgetExportCSVItem o2) {
                    return o1.getCode().compareTo(o2.getCode());
                }
            });
        }
        return  retVal;
    }
    
    public static List<AmpEntityMappedItem> getAmpEntityMappedItems (AmpBudgetExportMapRule rule) throws DgException {
        List<AmpEntityMappedItem> retVal = new ArrayList<AmpEntityMappedItem>();

        /*
        if (rule.isAllowAllItem()) {
            AmpEntityMappedItem allOpt = new AmpEntityMappedItem();
            allOpt.setAmpEntity(new DummyAmpEntity(new Long(-1), "All"));
            retVal.add(allOpt);
        }
        if (rule.isAllowNoneItem()) {
            AmpEntityMappedItem allOpt = new AmpEntityMappedItem();
            allOpt.setAmpEntity(new DummyAmpEntity(new Long(-2), "None"));
            retVal.add(allOpt);
        } */

        MappingEntityAdapter adapter = MappingEntityAdapterUtil.getEntityAdapter(rule.getAmpColumn().getExtractorView());

        List<HierarchyListable> tmpList = adapter.getAllObjects();


        Collections.sort(tmpList, new Comparator<HierarchyListable>() {
            public int compare(HierarchyListable o1, HierarchyListable o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });

        List<HierarchyListable> ampEntityList = new ArrayList<HierarchyListable>();
        if (rule.isAllowAllItem()) {
            ampEntityList.add(new DummyAmpEntity(new Long(-1), "All"));
        }
        if (rule.isAllowNoneItem()) {
            ampEntityList.add(new DummyAmpEntity(new Long(-2), "None"));
        }
        ampEntityList.addAll(tmpList);

        List<AmpBudgetExportMapItem> mapItems = rule.getItems();

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
    
    public static AmpBudgetExportMapRule getRuleByProjectIdAndView (Long projectID, String viewName) throws DgException{
        AmpBudgetExportMapRule retVal = null;
        AmpBudgetExportProject prj = DbUtil.getProjectById(projectID);

        for (AmpBudgetExportMapRule rule : prj.getRules()) {
            if (rule.getAmpColumn().getExtractorView().equals(viewName)) {
                retVal = rule;
                break;
            }
        }
        return retVal;
    }
    
    public static Set<Map.Entry<String, String>> getAvailRetrievers() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Set<Map.Entry<String, String>> retVal = null;

        if (serviceObjectRetrievers != null) {
            retVal = serviceObjectRetrievers;
        } else {

            retVal = new HashSet<Map.Entry<String, String>>();

            /*
            Reflections reflections = new Reflections("org.digijava.module.budgetexport.serviceimport.impl");
            Set<Class<? extends ObjectRetriever>> classes = reflections.getSubTypesOf(ObjectRetriever.class);
            */
            Set<Class> classes = getClassesInPackage(RETRIEVER_PACKAGE, RETRIEVER_INTERFACE);

            for (Class clazz : classes) {
                String className = clazz.getName();
                ObjectRetriever or =
                        (ObjectRetriever)Class.forName(className).newInstance();
                String displayName = or.getName();

                retVal.add(new HashMap.SimpleEntry<String, String>(className, displayName));
            }

            serviceObjectRetrievers = retVal;
        }


        return retVal;
    }

    /**
     * Given a package name, attempts to reflect to find all classes within the package
     * on the local file system.
     *
     * @param packageName
     * @return
     */
    private static Set<Class> getClassesInPackage(String packageName, String implInterface) {
        Set<Class> classes = new HashSet<Class>();
        String packageNameSlashed = "/" + packageName.replace(".", "/");
        // Get a File object for the package
        URL directoryURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlashed);
        if (directoryURL == null) {
            return classes;
        }

        String directoryString = directoryURL.getFile();
        if (directoryString == null) {
            return classes;
        }

        File directory = new File(directoryString);
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (String fileName : files) {
                // We are only interested in .class files
                if (fileName.endsWith(".class")) {
                    // Remove the .class extension
                    fileName = fileName.substring(0, fileName.length() - 6);
                    try {
                        Class clazz = Class.forName(packageName + "." + fileName);
                        //Check if class implements ObjectRetriever interface
                        java.lang.Class[] interfaces = clazz.getInterfaces();
                        if (interfaces != null && interfaces.length > 0) {
                            for (int intIdx = 0; intIdx < interfaces.length; intIdx ++) {
                                if (interfaces[intIdx].getName().equals(implInterface)) {
                                    classes.add(clazz);
                                    break;
                                }
                            }
                        }

                    } catch (ClassNotFoundException e) {

                    }
                }
            }
        } else {
            logger.warn(packageName + " does not appear to exist as a valid package on the file system.");
        }
        return classes;
    }
}
