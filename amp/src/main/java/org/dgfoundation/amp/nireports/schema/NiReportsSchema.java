package org.dgfoundation.amp.nireports.schema;

import java.util.*;

import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.SubDimensions;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;

/**
 * an interface describing the Schema of an API's reporting needs.
 * For a detailed reading about defining and using a schema, please refer to https://wiki.dgfoundation.org/display/AMPDOC/2.+NiReports+Configuration%3A+the+schema
 * @author Constantin Dolghier
 *
 */
public interface NiReportsSchema {
    /**
     * returns the list of columns which exist in the schema
     * @return
     */
    public Map<String, NiReportColumn<? extends Cell>> getColumns();
    
    /**
     * returns the list of measures which exist in the schema
     * @return
     */
    public Map<String, NiReportMeasure<?>> getMeasures();
    
    /**
     * returns the funding column definition
     */
    public NiReportColumn<CategAmountCell> getFundingFetcher(NiReportsEngine engine);

    /**
     * returns a function which exposes the spec's {@link ReportFilters} instance to Ni's NiFilters
     * @return
     */
    public NiFilters convertFilters(NiReportsEngine engine);
    
    /**
     * builds the schema-specific scratchpad, which will be stored in {@link NiReportsEngine#schemaSpecificScratchpad} at start time
     * @return
     */
    public SchemaSpecificScratchpad generateScratchpad(NiReportsEngine engine);
    
    public default Map<String, List<ReportRenderWarning>> performColumnChecks(Optional<Set<String>> columns) {
        Set<String> checkedColumns = columns.isPresent() ? columns.get() : getColumns().keySet();
        Map<String, List<ReportRenderWarning>> res = new HashMap<>();
        for(String colName:checkedColumns) {
            res.put(colName, getColumns().get(colName).performCheck());
        }
        return res;
    }
    
    /**
     * fetches an entity. By default it just forwards to the entity
     * @param engine
     * @param entity
     * @return
     * @throws Exception
     */
    public default<K extends Cell> List<K> fetchEntity(NiReportsEngine engine, NiReportedEntity<K> entity) throws Exception {
        return entity.fetch(engine);
    }

    /**
     * returns true if an entity is a transaction-level hierarchy
     * @param col
     * @param engine
     * @return
     */
    public default boolean isTransactionLevelHierarchy(NiReportColumn<?> col, NiReportsEngine engine) {
        return col.isTransactionLevelHierarchy();
    }
    
    /**
     * returns the list of post-measure V-split strategies for a given measure during the runtime of a report
     * @param engine
     * @param cc
     * @return
     */
    public default List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine engine, CellColumn cc) {
        List<VSplitStrategy> res = cc.behaviour.getSubMeasureHierarchies(engine);
        return res!=null?res:new ArrayList<>();
    }

    SubDimensions getSubDimensions();
}
