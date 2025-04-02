package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.diffcaching.ActivityInvalidationDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.nireports.schema.DimensionSnapshot;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.TabularSourcedNiDimension;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * a NiDimension which fetches its data from an SQL SELECT statement
 * @author Dolghier Constantin
 *
 */
public class SqlSourcedNiDimension extends TabularSourcedNiDimension {

    /**
     * the timeout, in seconds, of a dimension
     */
    public int DIMENSION_INVALIDATOR_TIMEOUT = 30 * 60;
    
    public final String sourceViewName;
    public final List<String> idColumnsNames;
    public final ExpiringCacher<Boolean, Boolean, DimensionSnapshot> cacher; 
    
    /**
     * @param name
     * @param sourceViewName
     * @param idColumnsNames must be enumerated from top to bottom
     */
    public SqlSourcedNiDimension(String name, String sourceViewName, List<String> idColumnsNames) {
        super(name, idColumnsNames.size());
        this.sourceViewName = sourceViewName;
        this.idColumnsNames = Collections.unmodifiableList(new ArrayList<>(idColumnsNames));
        this.cacher = new ExpiringCacher<>("ds_cache: " + name, (bool, engine) -> super.getDimensionData(), new ActivityInvalidationDetector(), DIMENSION_INVALIDATOR_TIMEOUT * 1000);
        check();
    }
    
    /**
     * builds an SQL-sourced degenerate dimension
     * @param name
     * @param sourceViewName
     * @param idColumnName
     * @return
     */
    public static NiDimension buildDegenerateDimension(String name, String sourceViewName, String idColumnName) {
        return new SqlSourcedNiDimension(name, sourceViewName, Arrays.asList(idColumnName));
    }

    @Override
    public DimensionSnapshot getDimensionData() {
        return cacher.buildOrGetValue(true, true);
    }
    
    @Override
    protected List<List<Long>> getTabularData() {
        return PersistenceManager.getSession().doReturningWork(connection -> {
            String query = String.format("SELECT %s FROM %s", SQLUtils.generateCSV(idColumnsNames), sourceViewName);
            return SQLUtils.collect(connection, query, this::fetchLine);
        });
    }   
    
    protected List<Long> fetchLine(ResultSet row) {
        return idColumnsNames.stream().map(z -> coalesce(SQLUtils.getLong(row, z), 999999999l)).collect(Collectors.toList());
    }
    
    private static Long coalesce(Long a, Long b) {
        if (a != null)
            return a;
        return b;
    }
    /**
     * dies if the view does not exist or does not contain the said columns;
     */
    private void check() {
        if (!SQLUtils.tableExists(sourceViewName))
            throw new RuntimeException(String.format("SqlSourcedNiDimension %s: view <%s> does not exist", name, sourceViewName));
        Set<String> columns = new TreeSet<>(idColumnsNames);
        columns.removeAll(SQLUtils.getTableColumns(sourceViewName));
        if (!columns.isEmpty())
            throw new RuntimeException(String.format("SqlSourcedNiDimension %s: column(s) <%s> do not exist in the view <%s>", name, columns.toString(), sourceViewName));
    }
    
    /** override in children if this dimension has unsure percentages */
    protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg, boolean pledgeColumn) {
        return null;
    }
    
    protected Map<NiDimensionUsage, PercentagesCorrector> getAllPercentagesCorrectors(boolean pledgeColumn) {
        Map<NiDimensionUsage, PercentagesCorrector> res = new HashMap<>();
        for(NiDimensionUsage dimUsg:this.getDUs()) {
            PercentagesCorrector cor = buildPercentagesCorrector(dimUsg, pledgeColumn);
            if (cor != null)
                res.put(dimUsg, cor);
        }
        return res;
    }
        
    @Override
    public String toString() {
        return String.format("SqlSourcedNiDimension %s based on columns <%s> of view <%s>", name, sourceViewName, idColumnsNames.toString());
    }
}
