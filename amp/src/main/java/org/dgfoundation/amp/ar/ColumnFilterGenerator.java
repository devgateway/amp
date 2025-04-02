/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpColumnsFilters;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.Identifiable;

/**
 * @author mihai
 * 
 */
public class ColumnFilterGenerator {

    protected static Logger logger = Logger.getLogger(ColumnFilterGenerator.class);

    /**
     * ...when fetching view V, filter entries by AmpARFilter property
     * Map<extractor_view, Set<by_view_column_name>>
     */
    public final static Map<String, Set<ViewDonorFilteringInfo>> PLEDGES_VIEWS_FILTERED_COLUMNS = new HashMap<String, Set<ViewDonorFilteringInfo>>(){{
        put(ArConstants.VIEW_PLEDGES_FUNDING, new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("aid_modality_id", "aidModalities"));
            add(new ViewDonorFilteringInfo("financing_instrument_id", "financingInstruments"));
            add(new ViewDonorFilteringInfo("terms_assist_id", "typeOfAssistance"));
        }});
        
        put("v_pledges_aid_modality", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("amp_modality_id", "aidModalities"));
        }});
        
        put("v_pledges_type_of_assistance", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("id", "typeOfAssistance"));
        }});
        
        put("v_pledges_sectors", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("amp_sector_id", "sectorsAndAncestors"));
        }});
        
        put("v_pledges_secondary_sectors", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("amp_sector_id", "secondarySectorsAndAncestors"));
        }});
        
        put("v_pledges_tertiary_sectors", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("amp_sector_id", "tertiarySectorsAndAncestors"));
        }});
                
        put("v_pledges_adm_level_3", new HashSet<ViewDonorFilteringInfo>() {{
            add(new ViewDonorFilteringInfo("adm_level_3_id", "relatedLocations"));
        }});
    
        put("v_pledges_adm_level_1", new HashSet<ViewDonorFilteringInfo>() {{
            add(new ViewDonorFilteringInfo("adm_level_1_id", "relatedLocations"));
        }});
        
        put("v_pledges_adm_level_2", new HashSet<ViewDonorFilteringInfo>() {{
            add(new ViewDonorFilteringInfo("adm_level_2_id", "relatedLocations"));
        }});
        
        put("v_pledges_npd_objectives", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("amp_program_id", "relatedNatPlanObjs"));
        }});
        
        put("v_pledges_programs", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("amp_program_id", "relatedPrimaryProgs"));
        }});
        
        put("v_pledges_secondary_programs", new HashSet<ViewDonorFilteringInfo>(){{
            add(new ViewDonorFilteringInfo("amp_program_id", "relatedSecondaryProgs"));
        }});
        
//      put("v_pledges_tertiary_programs", new HashSet<ViewDonorFilteringInfo>(){{
//          add(new ViewDonorFilteringInfo("amp_program_id", "relatedTertiaryProgs"));
//      }});
    }};
    
    /**
     * Map<AmpARFilter.propertyName, Set<AmpColumns.name>> - columns to also fetch when the report uses a said filter
     */
    public final static Map<String, List<String>> PLEDGES_COLUMNS_FILTERS = new HashMap<String, List<String>>(){{
        
        put("sectorsAndAncestors", new ArrayList<String>(){{
            add("Pledges Sectors");
        }});
        
        put("secondarySectorsAndAncestors", new ArrayList<String>(){{
            add("Pledges Secondary Sectors");
        }});
        
        put("tertiarySectorsAndAncestors", new ArrayList<String>(){{
            add("Pledges Tertiary Sectors");
        }});
        
        put("relatedLocations", new ArrayList<String>(){{
            add("Pledges Administrative Level 1");
            add("Pledges Administrative Level 2");
            add("Pledges Administrative Level 3");
        }});
        
        put("relatedPrimaryProgs", new ArrayList<String>(){{
            add("Pledges Programs");
        }});
        
        put("relatedSecondaryProgs", new ArrayList<String>(){{
            add("Pledges Secondary Programs");
        }});
        
//      put("relatedTertiaryProgs", new ArrayList<String>(){{
//          add("Pledges Tertiary Programs");
//      }});        
                
        put("relatedNatPlanObjs", new ArrayList<String>(){{
            add("Pledges National Plan Objectives");
        }});
        
//      put("relatedNatPlanObjs", new ArrayList<String>(){{
//          for(int i = 1; i <= 8; i++)
//              add("National Planning Objectives Level " + i);
//          add("National Planning Objectives");
//      }});
//      put("relatedPrimaryProgs", new ArrayList<String>(){{
//          for(int i = 1; i <= 8; i++)
//              add("Primary Program Level " + i);
//          add("Primary Program");
//      }});
//      put("relatedSecondaryProgs", new ArrayList<String>(){{
//          for(int i = 1; i <= 8; i++)
//              add("Secondary Program Level " + i);
//          add("Secondary Program");
//      }});        
        
    }};
    
    /**
     * Attaches hard coded filters for hard coded columns. The FUNDING columns
     * are hard coded, because they do not appear in amp_columns table,
     * therefore they cannot have persisted AmpColumnsFilters, thus the objects
     * will be created manually
     * 
     * @param c
     *            the object to which the hard coded filters will be attached
     */
    public static void attachHardcodedFilters(AmpColumns c) {
        c.setFilters(new HashSet<AmpColumnsFilters>());
        if (ArConstants.VIEW_DONOR_FUNDING.equals(c.getExtractorView()) || c.getColumnName().toLowerCase().contains("mtef")) {
            // TODO: example here of how to add hardcoded filters for hardcoded
            // FUNDING columns
            // AmpColumnsFilters acf=new
            // AmpColumnsFilters(c,"donorGroups","donor_group_id");
            // c.getFilters().add(acf);
            AmpColumnsFilters acf = new AmpColumnsFilters(c,"donorGroups","org_grp_id");
            c.getFilters().add(acf);
            AmpColumnsFilters acf2 = new AmpColumnsFilters(c,"donorTypes","org_type_id");
            c.getFilters().add(acf2);
            AmpColumnsFilters acf3= new AmpColumnsFilters(c,"financingInstruments","financing_instrument_id");
            c.getFilters().add(acf3);
            AmpColumnsFilters acf4= new AmpColumnsFilters(c,"typeOfAssistance","terms_assist_id");
            c.getFilters().add(acf4);
            AmpColumnsFilters acf5= new AmpColumnsFilters(c,"donnorgAgency","org_id");
            c.getFilters().add(acf5);
            AmpColumnsFilters acf6= new AmpColumnsFilters(c,"activityPledgesTitle","activity_pledges_title_id");
            c.getFilters().add(acf6);
            c.getFilters().add(new AmpColumnsFilters(c, "disasterResponseCodes", "disaster_response_code"));
            //c.getFilters().add(new AmpColumnsFilters(c, "expenditureClass", "expenditure_class_id"));
        }
            
        if (ArConstants.VIEW_COMPONENT_FUNDING.equals(c.getExtractorView())) {
            //TODO: add filters here    
            //AmpColumnsFilters acf = new AmpColumnsFilters(c,"regions","amp_component_id");
            //c.getFilters().add(acf);
        }
        if (ArConstants.VIEW_REGIONAL_FUNDING.equals(c.getExtractorView())) {
            //TODO: add filters here
            AmpColumnsFilters acf = new AmpColumnsFilters(c,"relatedLocations","region_id");
            c.getFilters().add(acf);
        }
    }

    /**
     * Helper method to create the sql clause for only one property of the
     * filter bean.
     * 
     * @see ColumnFilterGenerator#generateColumnFilterSQLClause(AmpARFilter,
     *      AmpColumns, boolean)
     * @param property
     *            the property for which the clause is generated
     * @param viewFieldName
     *            the sql view field name (column name is sql view)
     * @return the logical clause for this property
     */
    private static String generatePropertyFilterSQLClause(Object property,
            String viewFieldName) {
        if (property instanceof Collection)
            return viewFieldName
                    + " IN ("
                    + org.dgfoundation.amp.Util
                            .toCSStringForIN((Collection) property) + ")";
        if (property instanceof String)
            return viewFieldName + "='" + property + "'";
        if (property instanceof Identifiable)
            return generatePropertyFilterSQLClause(((Identifiable) property)
                    .getIdentifier(), viewFieldName);
        return viewFieldName + "=" + property;
    }

    /**
     * Generates the column filter sql query for a given column, by reading the
     * mapped properties that can affect this column, related with the filter
     * bean. Then the bean is queried to get the values for those properties and
     * the SQL clause is generated
     * 
     * @param f
     *            the filter bean as it is generated by the filter form
     * @param c
     *            the column for which the filtering SQL clause is created
     * @param exclusive
     *            if true, it will generate an SQL clause linked with AND
     *            operators , otherwise it will use OR
     * @return the complete SQL logical clause
     */
    public static String generateColumnFilterSQLClause(AmpARFilter f, Set<? extends ColumnFilteringInfo> filters) {
        // get all bindings between this column and possible filter properties:
        StringBuffer sb = new StringBuffer("");
        if (filters != null) {
            for(ColumnFilteringInfo cf:filters) {
                try {
                    Object property = PropertyUtils.getSimpleProperty(f, cf.getBeanFieldName());
                    if (property == null)
                        continue;
                    sb.append(" AND " + generatePropertyFilterSQLClause(property, cf.getViewFieldName()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return sb.toString();

    }

    public static boolean propertyIsDefined(Object obj, String propName){
        try{
            Object property = PropertyUtils.getSimpleProperty(obj, propName);
            return property != null;
        }
        catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     * iterates a list of (AmpARFilter.property -> Column) assocs and adds the missing ones
     * @param extractable
     * @param filter
     * @param colFilters
     * @return
     */
    protected static List<String> appendFilterRetrievableColumns(List<AmpReportColumn> extractable, Set<String> filterRetrievableCols)
    {
        // check which columns are selected in the filter and have attached
        // columns that are filter retrievable
        List<String> addedColumnNames=new ArrayList<String>();
        TreeSet<String> colNames = new TreeSet<String>();
        for(AmpReportColumn col:extractable){
            colNames.add(col.getColumn().getColumnName());
        }
        
        for(String retrievableColumnName:filterRetrievableCols){
            if (colNames.contains(retrievableColumnName))
                continue; //column already added to the to-be-fetched list
            AmpColumns col = AdvancedReportUtil.getColumnByName(retrievableColumnName);
        
            AmpReportColumn arc = new AmpReportColumn();
            arc.setColumn(col);
            arc.setOrderId(1L);
            //logger.info("Adding additional column " + cf.getColumn().getColumnName() + " because selected filter "+cf.getBeanFieldName()+" is filterRetrievable");
            extractable.add(arc);
            addedColumnNames.add(arc.getColumn().getColumnName());
        } 
        return addedColumnNames;
    }
    
    /**
     * during refactoring this should become an AmpARFilter instance method
     * @return
     */
    public static Map<String, List<AmpColumns>> getPledgesColumnFilters(){
        Map<String, List<AmpColumns>> res = new HashMap<String, List<AmpColumns>>();
        for(String propName: PLEDGES_COLUMNS_FILTERS.keySet()){
            res.put(propName, new ArrayList<AmpColumns>());
            for(String colName: PLEDGES_COLUMNS_FILTERS.get(propName))
                res.get(propName).add(AdvancedReportUtil.getColumnByName(colName));
        }
        return res;
    }
    
    /**
     * during refactoring this should become an AmpARFilter instance method
     * @return
     */
    public static Map<String, List<AmpColumns>> getNormalColumnFilters(){
        Map<String, List<AmpColumns>> res = new HashMap<String, List<AmpColumns>>();
        Session session = PersistenceManager.getSession();
        Query query = session.createQuery("from "   + AmpColumnsFilters.class.getName());
        List<AmpColumnsFilters> allFilters = query.list();
        // scan the in-db AmpColumnsFilters and distribute the entries by beanFieldName
        for(AmpColumnsFilters acf:allFilters){
            if (acf.getColumn() != null && acf.getColumn().getFilterRetrievable() != null && acf.getColumn().getFilterRetrievable()){
                if (!res.containsKey(acf.getBeanFieldName()))
                    res.put(acf.getBeanFieldName(), new ArrayList<AmpColumns>());
                res.get(acf.getBeanFieldName()).add(acf.getColumn());
            }
                
        }
        return res;
    }
    
    /**
     * Map<AmpARFilter.beanName, List<AmpColumns.columnName> to fetch if the given property is not null>
     * @param forPledges
     * @return
     */
    public static Map<String, List<AmpColumns>> getColumnFilters(boolean forPledges){
        return forPledges ? getPledgesColumnFilters() : getNormalColumnFilters();
    }
    
    /**
     * returns set of names of all columns which should be checked / retrieved according to this filter
     * TODO: during refactoring, move to AmpARFilter
     * @param filter
     * @return
     */
    public static Set<String> getFilterRetrievableColumns(AmpARFilter filter){
        Map<String, List<AmpColumns>> cols = getColumnFilters(filter.isPledgeFilter());
        Set<String> res = new HashSet<String>();
        // iterate all of the non-null properties of AmpARFilter which are configured in AmpColumnsFilters and fetch the corresponding column
        for(String propName:cols.keySet()){
            if (propertyIsDefined(filter, propName)){
                for(AmpColumns col:cols.get(propName))
                    res.add(col.getColumnName());
            }
        }
        return res;
    }
    
    /**
     * Append filter retrievable columns to list. those are columns that appear
     * in the current filter as selections and for filtering to be correctly
     * applied the content of the columns has to be present (it holds extra
     * metadata like percentages, etc...)
     * 
     * TODO: during refactoring this would become a simple method within AmpARFilter (see the {@link #getNormalColumnFilters()} and {@link #getPledgesColumnFilters()} methods above)
     * @see http://bugs.digijava.org/jira/browse/AMP-3454?focusedCommentId=39811#action_39811
     * @param extractable
     *            the list of already extractable columns
     * @param filter
     * @return the no of cols added
     */
    public static List<String> appendFilterRetrievableColumns(List<AmpReportColumn> extractable, AmpARFilter filter) {
        // check which columns are selected in the filter and have attached
        // columns that are filter retrievable
        try {           
            return appendFilterRetrievableColumns(extractable, getFilterRetrievableColumns(filter));
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }  
    }
}
