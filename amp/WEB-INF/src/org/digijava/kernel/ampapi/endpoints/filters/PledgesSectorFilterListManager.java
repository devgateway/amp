package org.digijava.kernel.ampapi.endpoints.filters;

import com.google.common.collect.ImmutableMap;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;

import java.util.Map;

/**
 * This class generates the filter list (tree) object for pledges sectors
 * 
 * @author Viorel Chihai
 *
 */
public final class PledgesSectorFilterListManager extends SectorFilterListManager {
    
    private static final String PLEDGES_PREFIX = "Pledges";
    
    private static PledgesSectorFilterListManager pledgesSectorFilterListManager;
    
    public static final Map<String, String> SECTOR_FILTER_ID_TO_PLEDGE_FILTER_ID =
            new ImmutableMap.Builder<String, String>()
                    .put(FiltersConstants.PRIMARY_SECTOR, FiltersConstants.PLEDGES_SECTORS)
                    .put(FiltersConstants.PRIMARY_SECTOR_SUB_SECTOR, FiltersConstants.PLEDGES_SECTORS_SUB_SECTORS)
                    .put(FiltersConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_SECTORS_SUB_SUB_SECTORS)
                    .put(FiltersConstants.SECONDARY_SECTOR, FiltersConstants.PLEDGES_SECONDARY_SECTORS)
                    .put(FiltersConstants.SECONDARY_SECTOR_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_SECONDARY_SECTORS_SUB_SECTORS)
                    .put(FiltersConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_SECONDARY_SECTORS_SUB_SUB_SECTORS)
                    .put(FiltersConstants.TERTIARY_SECTOR, FiltersConstants.PLEDGES_TERTIARY_SECTORS)
                    .put(FiltersConstants.TERTIARY_SECTOR_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_TERTIARY_SECTORS_SUB_SECTORS)
                    .put(FiltersConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_TERTIARY_SECTORS_SUB_SUB_SECTORS)
                    .put(FiltersConstants.QUATERNARY_SECTOR, FiltersConstants.PLEDGES_QUATERNARY_SECTORS)
                    .put(FiltersConstants.QUATERNARY_SECTOR_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_QUATERNARY_SECTORS_SUB_SECTORS)
                    .put(FiltersConstants.QUATERNARY_SECTOR_SUB_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_QUATERNARY_SECTORS_SUB_SUB_SECTORS)
                    .put(FiltersConstants.QUINARY_SECTOR, FiltersConstants.PLEDGES_QUINARY_SECTORS)
                    .put(FiltersConstants.QUINARY_SECTOR_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_QUINARY_SECTORS_SUB_SECTORS)
                    .put(FiltersConstants.QUINARY_SECTOR_SUB_SUB_SECTOR, 
                            FiltersConstants.PLEDGES_QUINARY_SECTORS_SUB_SUB_SECTORS)
                    .build();

    public static PledgesSectorFilterListManager getInstance() {
        if (pledgesSectorFilterListManager == null) {
            pledgesSectorFilterListManager = new PledgesSectorFilterListManager();
        }

        return pledgesSectorFilterListManager;
    }
    
    private PledgesSectorFilterListManager() { 
        super();
    }

    @Override
    protected String getFilterId(String filterId) {
        return SECTOR_FILTER_ID_TO_PLEDGE_FILTER_ID.get(filterId);
    }
    
    @Override
    protected String getFilterDefinitionName(String sectorConfigurationName) {
        switch(sectorConfigurationName) {
            case AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME:
            case AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME:
            case AmpClassificationConfiguration.QUATERNARY_CLASSIFICATION_CONFIGURATION_NAME:
            case AmpClassificationConfiguration.QUINARY_CLASSIFICATION_CONFIGURATION_NAME:
                return String.format("%s %s %s", PLEDGES_PREFIX, sectorConfigurationName, SECTORS_SUFFIX);
            case AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME:
            default:
                return String.format("%s %s", PLEDGES_PREFIX, SECTORS_SUFFIX);
        }
    }

}
