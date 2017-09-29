/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.reports.CustomMeasures;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMapping;
import org.digijava.kernel.ampapi.saiku.SaikuGeneratedReport;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DbUtil;

/**
 * Reports utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class MondrianReportUtils {
    protected static final Logger logger = Logger.getLogger(MondrianReportUtils.class);
    
    /**
     * flushes Mondrian Cache
     */
    public static void flushCache() {
        new mondrian.rolap.CacheControlImpl(null).flushSchemaCache();
//      try {
//          RolapConnection rolapConn = olapConnection.unwrap(mondrian.rolap.RolapConnection.class);
//          rolapConn.getCacheControl(null).flushSchema(rolapConn.getSchema());
//      }
//      catch (SQLException e) {
//          throw new RuntimeException(e);
//      }
    }
    
    /**
     * @return default configuration for the current user settings
     */
    public static ReportSettingsImpl getCurrentUserDefaultSettings() {
        ReportSettingsImpl settings = new ReportSettingsImpl();
        settings.setCurrencyFormat(FormatHelper.getDefaultFormat());
        AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();
        if (ampAppSettings == null) {
            settings.setCalendar(DbUtil.getAmpFiscalCalendar(DbUtil.getBaseFiscalCalendar()));
        } else { 
            settings.setCalendar(ampAppSettings.getFiscalCalendar());
        }
        return settings;
    }
    
    /**
     * Retrieves column index for the specified column from the given ReportSpecification
     * @param col
     * @param spec
     * @return
     */
    public static int getColumnId(ReportColumn col, ReportSpecification spec) {
        if (spec == null || spec.getColumns() == null) return -1;
        int colId = 0;
        for (Iterator<ReportColumn> iter = spec.getColumns().iterator(); iter.hasNext(); colId++)
            if (iter.next().equals(col))
                break;
        return colId == spec.getColumns().size() ? -1 : colId; 
    }
            
    /**
     * returns a list of all the ACVL IDs 
     * @param geoid
     * @return
     */
    public static List<String> geoIdToLocationIds(String geoid) {
        List<?> acvlIds = PersistenceManager.getSession().createSQLQuery("SELECT id FROM amp_category_value_location WHERE geo_code=" + SQLUtils.stringifyObject(geoid)).list();

        List<String> res = new ArrayList<>();
        for(Object geoId:acvlIds)
            if (geoId != null)
                res.add(PersistenceManager.getLong(geoId).toString());
        return res;
    }
    
    /**
     * switches geocodes for location IDs
     * @param in
     * @return
     */
    public static FilterRule postprocessGeocodeRule(FilterRule in) {
        if (in == null) return null;
        Set<String> locationIds = new HashSet<>();
        
        switch(in.filterType) {
            case RANGE:
                throw new RuntimeException("no range for geoids!");
                
            case SINGLE_VALUE:
                locationIds.addAll(geoIdToLocationIds(in.value));
                break;
                
            case VALUES:
                for(String value:in.values)
                    locationIds.addAll(geoIdToLocationIds(value));
                break;
        }
        return new FilterRule(new ArrayList<>(locationIds), in.valuesInclusive);
    }
}
