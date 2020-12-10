package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.util.AmpThemeSkeleton;
import org.digijava.module.aim.util.ProgramUtil;
import org.hibernate.Session;

import com.google.common.collect.ImmutableMap;

/**
 * This class generates the filter list (tree) object for programs
 * 
 * @author Viorel Chihai
 *
 */
public class ProgramFilterListManager implements FilterListManager {
    
    public static final String NATIONAL_PLANNING_OBJECTIVES_ITEMS_NAME = "nationalPlanningObjectives";
    public static final String INDIRECT_PRIMARY_PROGRAM_ITEMS_NAME = "indirectPrimaryPrograms";
    public static final String PRIMARY_PROGRAM_ITEMS_NAME = "primaryPrograms";
    public static final String SECONDARY_PROGRAM_COLUMNS_BY_LEVEL_ITEMS_NAME = "secondaryPrograms";
    public static final String TERTIARY_PROGRAM_COLUMNS_BY_LEVEL_ITEMS_NAME = "tertiaryPrograms";

    public static final Map<String, String> PROGRAM_NAME_TO_ITEMS_NAME =
            new ImmutableMap.Builder<String, String>()
                    .put(ProgramUtil.NATIONAL_PLAN_OBJECTIVE, NATIONAL_PLANNING_OBJECTIVES_ITEMS_NAME)
                    .put(ProgramUtil.PRIMARY_PROGRAM, PRIMARY_PROGRAM_ITEMS_NAME)
                    .put(ProgramUtil.SECONDARY_PROGRAM, SECONDARY_PROGRAM_COLUMNS_BY_LEVEL_ITEMS_NAME)
                    .put(ProgramUtil.TERTIARY_PROGRAM, TERTIARY_PROGRAM_COLUMNS_BY_LEVEL_ITEMS_NAME)
                    .put(ProgramUtil.INDIRECT_PRIMARY_PROGRAM, INDIRECT_PRIMARY_PROGRAM_ITEMS_NAME)
                    .build();
    
    private static ProgramFilterListManager programFilterListManager;

    public static ProgramFilterListManager getInstance() {
        if (programFilterListManager == null) {
            programFilterListManager = new ProgramFilterListManager();
        }

        return programFilterListManager;
    }
    
    protected ProgramFilterListManager() { }

    @Override
    public FilterList getFilterList() {
        List<FilterListDefinition> programListDefinitions = getProgramListDefinitions();
        Map<String, List<FilterListTreeNode>> programListItems = getProgramListItems();
        
        return new FilterList(programListDefinitions, programListItems);
    }
    
    protected List<FilterListDefinition> getProgramListDefinitions() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        List<AmpActivityProgramSettings> progarmSettings = getProgramSettings();
       
        for (AmpActivityProgramSettings setting : progarmSettings) {
            String definitionName = getFilterDefinitionName(setting.getName());
            FilterListDefinition listDefinition = new FilterListDefinition();
            listDefinition.setId(setting.getAmpProgramSettingsId());
            listDefinition.setName(definitionName);
            listDefinition.setDisplayName(TranslatorWorker.translateText(definitionName));
            listDefinition.setFiltered(true);
            listDefinition.setFilterIds(getProgramFilterIds(setting));
            listDefinition.setItems(PROGRAM_NAME_TO_ITEMS_NAME.get(setting.getName()));
            listDefinitions.add(listDefinition);
        }
        
        return listDefinitions;
    }

    protected List<String> getProgramFilterIds(AmpActivityProgramSettings setting) {
        
        List<String> filterIds = AmpActivityProgramSettings.NAME_TO_COLUMN_AND_LEVEL.get(setting.getName())
            .values().stream()
            .map(col -> FilterUtils.INSTANCE.idFromColumnName(col))
            .collect(Collectors.toList());

        return filterIds;
    }

    private Map<String, List<FilterListTreeNode>> getProgramListItems() {
        List<AmpActivityProgramSettings> programSettings = getProgramSettings();
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        for (AmpActivityProgramSettings setting : programSettings) {
            List<FilterListTreeNode> programItems = new ArrayList<>();
            
            String programSettingName = setting.getName();
            Map<Long, AmpThemeSkeleton> programs = AmpThemeSkeleton.populateThemesTree(setting.getDefaultHierarchyId());
            
            programItems.add(getPrograms(programs.get(setting.getDefaultHierarchyId())));
            programItems.add(getUndefinedOption());
            items.put(PROGRAM_NAME_TO_ITEMS_NAME.get(programSettingName), programItems);
        }

        return items;
    }
    
    protected List<AmpActivityProgramSettings> getProgramSettings() {
        Set<String> visibleCols = ColumnsVisibility.getVisibleColumns();
        Session session = PersistenceManager.getSession();
        List<AmpActivityProgramSettings> allSettings = ProgramUtil.getAmpActivityProgramSettingsList(false);
        List<AmpActivityProgramSettings> programSettings = allSettings.stream()
                .filter(setting -> visibleCols.contains(getColumnName(setting.getName())))
                .filter(setting -> setting.getDefaultHierarchy() != null)
                .collect(Collectors.toList());

        return programSettings;
    }
    
    private FilterListTreeNode getPrograms(AmpThemeSkeleton program) {
        FilterListTreeNode node = new FilterListTreeNode();
        node.setId(program.getId());
        node.setCode(program.getCode());
        node.setName(program.getName());
        
        
        List<AmpThemeSkeleton> orderedChildPrograms = program.getChildLocations().stream()
                .sorted(Comparator.comparing(AmpThemeSkeleton::getName))
                .collect(Collectors.toList());
        
        for (AmpThemeSkeleton ampProgramChild : orderedChildPrograms) {
            node.addChild(getPrograms(ampProgramChild));
        }
        
        return node;
    }

    /**
     * @param programConfigurationName
     * @return
     */
    protected String getFilterDefinitionName(String programConfigurationName) {
        return programConfigurationName.equals(ProgramUtil.NATIONAL_PLAN_OBJECTIVE)
                ? ColumnConstants.NATIONAL_PLANNING_OBJECTIVES : programConfigurationName;
    }

    /**
     * @param programConfigurationName
     * @return
     */
    protected String getColumnName(String programConfigurationName) {
        return ProgramUtil.NAME_TO_COLUMN_MAP.get(programConfigurationName);
    }

}
