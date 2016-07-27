/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.GisConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * GIS Gap Analysis service
 * 
 * @author Nadejda Mandrescu
 */
public class GapAnalysis {
    protected static Logger LOGGER = Logger.getLogger(GapAnalysis.class);
    
    private Map<String, AmpIndicatorLayer> implLocPopulationLayer = new HashMap<>();
    
    public GapAnalysis() {
    }
    
    /**
     * Get unique Population Layer designated for 'admLevel' and remember for reuse
     * @param admLevel the generic ADM level ("adm0", ...)
     * @return the population layer or null if no unique layer found
     */
    public AmpIndicatorLayer getPopulationLayer(String admLevel) {
        HardCodedCategoryValue hardcodedCatValue = GisConstants.ADM_TO_IMPL_CATEGORY_VALUE.get(admLevel);
        return getPopulationLayer(hardcodedCatValue == null ? null : hardcodedCatValue.getAmpCategoryValueFromDB());
    }
    
    /**
     * Get unique Population Layer designated for the given implementation location and remember for reuse
     * @param implLoc the implementation location (Region, etc)
     * @return  the population layer or null if no unique layer found
     */
    public AmpIndicatorLayer getPopulationLayer(AmpCategoryValue implLoc) {
        String implLocation = implLoc == null ? null : implLoc.getValue();
        AmpIndicatorLayer ail = implLocPopulationLayer.putIfAbsent(implLocation, getPopulationLayer2(implLoc));
        if (ail == null) {
            ail = implLocPopulationLayer.get(implLocation);
        }
        return ail;
    }
    
    public boolean canDoGapAnalysis(AmpIndicatorLayer ail) {
        if (ail != null && ail.getIndicatorType() != null) {
            String acvValue = ail.getIndicatorType().getValue();
            if (acvValue.equals(CategoryConstants.INDICATOR_LAYER_TYPE_PER_CAPITA.getValueKey())
                    || acvValue.equals(CategoryConstants.INDICATOR_LAYER_TYPE_COUNT.getValueKey())
                    || (acvValue.equals(CategoryConstants.INDICATOR_LAYER_TYPE_POPULATION_RATIO.getValueKey())
                            && getPopulationLayer(ail.getAdmLevel()) != null)) {
                return true;
            }
        }
        return false;
    }
    
    // TODO: move under IndicatorUtils
    public static AmpIndicatorLayer getPopulationLayer2(HardCodedCategoryValue hardcodedCatValue) {
        return getPopulationLayer2(hardcodedCatValue == null ? null : hardcodedCatValue.getAmpCategoryValueFromDB());
    }
    
    /**
     * Get unique Population Layer designated for the given implementation location
     * @param implLoc the implementation location (Region, etc)
     * @return  the population layer or null if no unique layer found
     */
    public static AmpIndicatorLayer getPopulationLayer2(AmpCategoryValue implementationLocation) {
        AmpIndicatorLayer ail = null;
        if (implementationLocation != null && implementationLocation.isVisible()) {
            List<AmpIndicatorLayer> ailList = PersistenceManager.getSession()
                    .createQuery("select o from " + AmpIndicatorLayer.class.getName() + " o "
                    + "where o.population is true and o.admLevel is not null and o.admLevel.id=:admLevelId")
                    .setLong("admLevelId", implementationLocation.getId()).list();
            if (ailList != null && ailList.size() == 1) {
                ail = ailList.iterator().next();
            }
        }
        if (ail == null) {
            LOGGER.error("Could not uniquely locate population layer for admLevel = " + implementationLocation.getValue());
        }
        return ail;
    }

}
