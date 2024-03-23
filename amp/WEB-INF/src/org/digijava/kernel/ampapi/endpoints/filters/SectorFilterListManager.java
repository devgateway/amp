package org.digijava.kernel.ampapi.endpoints.filters;

import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.SectorUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class generates the filter list (tree) object for sectors
 * 
 * @author Viorel Chihai
 *
 */
public class SectorFilterListManager implements FilterListManager {
    
    protected static final String SECTORS_SUFFIX = "Sectors";
    
    private static SectorFilterListManager sectorFilterListManager;

    public static SectorFilterListManager getInstance() {
        if (sectorFilterListManager == null) {
            sectorFilterListManager = new SectorFilterListManager();
        }

        return sectorFilterListManager;
    }
    
    protected SectorFilterListManager() { }

    @Override
    public FilterList getFilterList() {
        List<FilterListDefinition> sectorListDefinitions = getSectorListDefinitions();
        Map<String, List<FilterListTreeNode>> sectorListItems = getSectorListItems();
        
        return new FilterList(sectorListDefinitions, sectorListItems);
    }
    
    protected List<FilterListDefinition> getSectorListDefinitions() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        List<AmpClassificationConfiguration> sectorConfigs = getSectorConfigs();
       
        for (AmpClassificationConfiguration sc : sectorConfigs) {
            List<String> filterIds = AmpClassificationConfiguration.NAME_TO_COLUMN_AND_LEVEL.get(sc.getName())
                    .values().stream()
                    .map(col -> getFilterId(FilterUtils.INSTANCE.idFromColumnName(col)))
                    .collect(Collectors.toList());
            String filterDefinitionName = getFilterDefinitionName(sc.getName());
            
            FilterListDefinition listDefinition = new FilterListDefinition();
            listDefinition.setId(sc.getId());
            listDefinition.setName(filterDefinitionName);
            listDefinition.setDisplayName(TranslatorWorker.translateText(filterDefinitionName));
            listDefinition.setFiltered(true);
            listDefinition.setFilterIds(filterIds);
            listDefinition.setItems(sc.getName().toLowerCase());
            listDefinitions.add(listDefinition);
        }
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getSectorListItems() {
        List<AmpClassificationConfiguration> sectorConfigs = getSectorConfigs();
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        for (AmpClassificationConfiguration sc : sectorConfigs) {
            List<FilterListTreeNode> sectorItems = new ArrayList<>();
            String sectorConfigName = sc.getName();
            List<AmpSector> sectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(sectorConfigName);
            for (AmpSector as : sectors) {
                sectorItems.add(getSectors(as));
            }
            sectorItems.add(getUndefinedOption());
            
            items.put(sectorConfigName.toLowerCase(), sectorItems);
        }

        return items;
    }
    
    private List<AmpClassificationConfiguration> getSectorConfigs() {
        Set<String> visibleCols = ColumnsVisibility.getVisibleColumns();
        List<AmpClassificationConfiguration> sectorConfigs = SectorUtil.getAllClassificationConfigs().stream()
                .filter(sc -> visibleCols.contains(AmpClassificationConfiguration.NAME_TO_COLUMN_MAP.get(sc.getName())))
                .collect(Collectors.toList());
        
        return sectorConfigs;
    }
    
    private FilterListTreeNode getSectors(AmpSector as) {
        FilterListTreeNode node = new FilterListTreeNode();
        node.setId(as.getAmpSectorId());
        node.setCode(as.getSectorCodeOfficial());
        node.setName(as.getName());
        
        List<AmpSector> orderedSectors = as.getSectors().stream()
                .sorted(Comparator.comparing(AmpSector::getName))
                .collect(Collectors.toList());
        
        for (AmpSector ampSectorChild : orderedSectors) {
            node.addChild(getSectors(ampSectorChild));
        }
        
        return node;
    }

    protected String getFilterId(String filterId) {
        return filterId;
    }

    protected String getFilterDefinitionName(String sectorConfigurationName) {
        return String.format("%s %s", sectorConfigurationName, SECTORS_SUFFIX);
    }

}
