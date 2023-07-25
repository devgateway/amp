/**
 * 
 */
package org.dgfoundation.amp.ar.dyn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.hibernate.query.Query;
import org.hibernate.Session;

/**
 * @author Alex Gartner
 *
 */
public class DynamicColumnsUtil {
    private static Logger logger = Logger.getLogger(DynamicColumnsUtil.class);
    
    public final static Set<String> mtefAliases = Collections.unmodifiableSet(new HashSet<String>() 
            {{add("mtef"); add("realmtef"); add("pipelinemtef"); add("projectionmtef");}}
    );
    
    private static List<AmpColumns> cachedMtefColumnList = null;
    private static List<AmpColumns> cachedAllMtefColumnList = null;
    private static List<AmpMeasures> cachedMtefMeasureList = null;
    
    
    /**
     * replaced by AmpReportsSchema.synchronizeAmpColumnsBackport
     * @param sCtx
     */
    @Deprecated
    public static void createInexistentMtefColumns (ServletContext sCtx) {
        List<Integer> mtefFundingYears = getMtefYears();
        
        Set<Integer> newMtefCols = buildInexistentMtefColumnYears(mtefFundingYears, "mtef");
        buildMtefColumns(sCtx, "MTEF", "mtef", newMtefCols);
         
        Set<Integer> newRealMtefCols = buildInexistentMtefColumnYears(mtefFundingYears, "realmtef");
        buildMtefColumns(sCtx, "Real MTEF", "realmtef", newRealMtefCols);
        
        Set<Integer> newPipelineMtefCols = buildInexistentMtefColumnYears(mtefFundingYears, "pipelinemtef");
        buildMtefColumns(sCtx, "Pipeline MTEF Projections", "pipelinemtef", newPipelineMtefCols);
        
        Set<Integer> newProjectionMtefCols = buildInexistentMtefColumnYears(mtefFundingYears, "projectionmtef");
        buildMtefColumns(sCtx, "Projection MTEF Projections", "projectionmtef", newProjectionMtefCols);
        
        
        if (!newMtefCols.isEmpty() || !newRealMtefCols.isEmpty() || !newPipelineMtefCols.isEmpty() || !newProjectionMtefCols.isEmpty()) {
            DynamicColumnsUtil.cachedMtefColumnList = null;
            DynamicColumnsUtil.cachedAllMtefColumnList = null;
            MathExpressionRepository.buildMtefColumn();
        }
    }

    @Deprecated
    private static void buildMtefColumns(ServletContext sCtx, String namePrefix, String aliasPrefix, Set<Integer> mtefYears) {
        for (Integer year: mtefYears) {
            AmpColumns  col     = new AmpColumns();
            col.setColumnName(namePrefix + " " + year);
            col.setAliasName(aliasPrefix + year);
            col.setExtractorView("v_mtef_funding");
            col.setTokenExpression(col.getColumnName());
            col.setCellType("org.dgfoundation.amp.ar.cell.ComputedAmountCell");
            
            logger.info("Adding " + namePrefix + " column for year " + year);
            dynamicallyCreateNewColumn(col, "Funding Information", sCtx);
        }

    }
    
    /**
     * 
     * @param newCol create a new AmpColumn object and give it to this function as parameter
     * @param featureName name of the parent feature of this field   
     */
    public static void dynamicallyCreateNewColumn(AmpColumns newCol, String featureName, ServletContext sCtx) {
        ColumnSavingEngine cse  = new ColumnSavingEngine(newCol, featureName, sCtx);
        cse.startSavingProcess();
    }

    public static List<AmpColumns> getAllMtefColumns(Set<String> prefixes) {
        List<AmpColumns> z = new ArrayList<AmpColumns>();
        Collection<AmpColumns> allCols = AdvancedReportUtil.getColumnList();
        for (AmpColumns col: allCols) {
            for(String prefix:prefixes) {
                if (col.getAliasName() != null && col.getAliasName().startsWith(prefix))
                    z.add(col);
            }
        }
        return z;
    }
    
    public static List<AmpColumns> getAllMtefColumns() {
        if (DynamicColumnsUtil.cachedAllMtefColumnList == null) {
            DynamicColumnsUtil.cachedAllMtefColumnList  = getAllMtefColumns(mtefAliases);
        }
        return Collections.unmodifiableList(DynamicColumnsUtil.cachedAllMtefColumnList);
    }

    
    public static List<AmpColumns> getMtefColumns() {
        if (DynamicColumnsUtil.cachedMtefColumnList == null) {          
            DynamicColumnsUtil.cachedMtefColumnList = getAllMtefColumns(new HashSet<String>(Arrays.asList("mtef")));
        }
        return Collections.unmodifiableList(DynamicColumnsUtil.cachedMtefColumnList);
    }
    
    public static List<AmpMeasures> getMtefMeasures()
    {
        if (DynamicColumnsUtil.cachedMtefMeasureList == null)
        {
            List<AmpMeasures> z = new ArrayList<AmpMeasures>();
            List<AmpMeasures> allCols = AdvancedReportUtil.getMeasureList();
            for (AmpMeasures mes: allCols) 
            {
                if (mes.getMeasureName().contains("MTEF"))
                    z.add(mes);
            }
            DynamicColumnsUtil.cachedMtefMeasureList = new ArrayList<AmpMeasures>(z);
        }
        return Collections.unmodifiableList(DynamicColumnsUtil.cachedMtefMeasureList);
    }
    
    public static Set<Integer> buildInexistentMtefColumnYears(List<Integer> mtefFundingYears, String prefix) {
        try {
            Set<Integer> res = new TreeSet<>(mtefFundingYears);
            String queryString = "select c.aliasName from " + AmpColumns.class.getName() + " c where c.extractorView = 'v_mtef_funding' AND c.aliasName like '" + prefix + "%' ";
            
            List<String> mtefColNames = PersistenceManager.getSession().createQuery(queryString).list();
            for (String colName: mtefColNames) {
                String yearStr = colName.substring(prefix.length());
                Integer year = Integer.parseInt(yearStr);
                res.remove(year);
            }
            return res;
        } catch (Exception ex) {
            throw new RuntimeException(ex); 
        }
    }
    
    public static List<Integer> getMtefYears() {
        try {
            Session session = PersistenceManager.getSession();
            String queryString = "select distinct(year(p.projectionDate)) FROM "
                + AmpFundingMTEFProjection.class.getName() + " p WHERE p.projectionDate IS NOT NULL "
                + " ORDER BY year(p.projectionDate)";
            
            List<Integer> rawList = session.createQuery(queryString).list();
            List<Integer> retList = new ArrayList<>();
            
            if (rawList.isEmpty())
                return retList;
            
            int minYear = rawList.get(0) - 3;
            int maxYear = rawList.get(rawList.size() - 1) + 3;
            
            for(int i = minYear; i <= maxYear; i++)
                retList.add(i);
                
            return retList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
