package org.digijava.kernel.ampapi.endpoints.filters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class generates the filter list (tree) object for pledges programs
 * 
 * @author Viorel Chihai
 *
 */
public final class PledgesProgramFilterListManager extends ProgramFilterListManager {
    
    private static PledgesProgramFilterListManager pledgesProgramFilterListManager;

    private static final List<String> PLEDGES_NATIONAL_PLANNING_OBJECTIVES_COLUMNS = 
            new ImmutableList.Builder<String>()
                .add(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_0)
                .add(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_1)
                .add(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_2)
                .add(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_3)
            .build();

    private static final List<String> PLEDGES_PROGRAMS_COLUMNS = 
            new ImmutableList.Builder<String>()
                .add(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_0)
                .add(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_1)
                .add(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_2)
                .add(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_3)
            .build();
    
    private static final List<String> PLEDGES_SECONDARY_PROGRAMS_COLUMNS = 
            new ImmutableList.Builder<String>()
                .add(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_0)
                .add(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_1)
                .add(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_2)
                .add(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_3)
            .build();
    
    private static final List<String> PLEDGES_TERTIARY_PROGRAMS_COLUMNS = 
            new ImmutableList.Builder<String>()
                .add(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_0)
                .add(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_1)
                .add(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_2)
                .add(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_3)
            .build();

    public static final Map<String, List<String>> NAME_TO_COLUMNS =
            new ImmutableMap.Builder<String, List<String>>()
                    .put(ProgramUtil.NATIONAL_PLAN_OBJECTIVE, PLEDGES_NATIONAL_PLANNING_OBJECTIVES_COLUMNS)
                    .put(ProgramUtil.PRIMARY_PROGRAM, PLEDGES_PROGRAMS_COLUMNS)
                    .put(ProgramUtil.SECONDARY_PROGRAM, PLEDGES_SECONDARY_PROGRAMS_COLUMNS)
                    .put(ProgramUtil.TERTIARY_PROGRAM, PLEDGES_TERTIARY_PROGRAMS_COLUMNS)
                    .build();

    public static PledgesProgramFilterListManager getInstance() {
        if (pledgesProgramFilterListManager == null) {
            pledgesProgramFilterListManager = new PledgesProgramFilterListManager();
        }

        return pledgesProgramFilterListManager;
    }
    
    private PledgesProgramFilterListManager() { 
        super();
    }

    @Override
    protected List<String> getProgramFilterIds(AmpActivityProgramSettings setting) {
        List<String> filterIds = NAME_TO_COLUMNS.get(setting.getName()).stream()
            .map(col -> FilterUtils.INSTANCE.idFromColumnName(col))
            .sorted()
            .collect(Collectors.toList());

        return filterIds;
    }

    @Override
    protected String getFilterDefinitionName(String programConfigurationName) {
        return getColumnName(programConfigurationName);
    }

    @Override
    protected String getColumnName(String programConfigurationName) {
        switch(programConfigurationName) {
            case ProgramUtil.PRIMARY_PROGRAM:
                return ColumnConstants.PLEDGES_PROGRAMS;
            case ProgramUtil.SECONDARY_PROGRAM:
                return ColumnConstants.PLEDGES_SECONDARY_PROGRAMS;
            case ProgramUtil.TERTIARY_PROGRAM:
                return ColumnConstants.PLEDGES_TERTIARY_PROGRAMS;
            case ProgramUtil.NATIONAL_PLAN_OBJECTIVE:
            default:
                return ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES;
        }
    }

}
