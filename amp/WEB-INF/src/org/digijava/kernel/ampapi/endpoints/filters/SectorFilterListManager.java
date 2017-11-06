package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.SectorUtil;

/**
 * This class generates the filter list (tree) object for sectors
 * 
 * @author Viorel Chihai
 *
 */
public final class SectorFilterListManager implements FilterListManager {
    
    private static final String SECTORS_SUFFIX = " Sectors";
    
    private static SectorFilterListManager sectorFilterListManager;

    public static SectorFilterListManager getInstance() {
        if (sectorFilterListManager == null) {
            sectorFilterListManager = new SectorFilterListManager();
        }

        return sectorFilterListManager;
    }
    
    private SectorFilterListManager() { }

    @Override
    public FilterList getFilterList() {
        List<FilterTreeDefinition> sectorTreeDefinitions = getSectorTreeDefinitions();
        List<FilterTreeNode> sectorTreeItems = getSectorTreeItems();
        
        return new FilterList(sectorTreeDefinitions, sectorTreeItems);
    }
    
    private List<FilterTreeDefinition> getSectorTreeDefinitions() {
        List<FilterTreeDefinition> treeDefinitions = new ArrayList<>();
        List<AmpClassificationConfiguration> sectorConfigs = getSectorConfigs();
       
        for (AmpClassificationConfiguration sc : sectorConfigs) {
            List<String> filterIds = AmpClassificationConfiguration.NAME_TO_COLUMN_AND_LEVEL.get(sc.getName())
                    .values().stream()
                    .map(col -> FilterUtils.INSTANCE.idFromColumnName(col))
                    .collect(Collectors.toList());
            
            FilterTreeDefinition treeDefinition = new FilterTreeDefinition();
            treeDefinition.setId(sc.getId());
            treeDefinition.setName(sc.getName() + SECTORS_SUFFIX);
            treeDefinition.setDisplayName(TranslatorWorker.translateText(sc.getName() + SECTORS_SUFFIX));
            treeDefinition.setFilterIds(filterIds);
            treeDefinitions.add(treeDefinition);
        }
        
        return treeDefinitions;
    }
    
    private List<FilterTreeNode> getSectorTreeItems() {
        List<FilterTreeNode> items = new ArrayList<>();
        List<AmpClassificationConfiguration> sectorConfigs = getSectorConfigs();
        
        for (AmpClassificationConfiguration sc : sectorConfigs) {
            String sectorConfigName = sc.getName();
            List<AmpSector> sectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(sectorConfigName);
            List<Long> treeIds = new ArrayList<>();
            treeIds.add(sc.getId());
            for (AmpSector as : sectors) {
                items.add(getSectors(as, treeIds));
            }
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
    
    private FilterTreeNode getSectors(AmpSector as, List<Long> treeIds) {
        FilterTreeNode node = new FilterTreeNode();
        node.setId(as.getAmpSectorId());
        node.setCode(as.getSectorCodeOfficial());
        node.setName(as.getName());
        
        if (as.getSectors().isEmpty()) {
            node.setTreeIds(treeIds);
        }
        
        for (AmpSector ampSectorChild : as.getSectors()) {
            node.addChild(getSectors(ampSectorChild, treeIds));
        }
        
        return node;
    }

}
