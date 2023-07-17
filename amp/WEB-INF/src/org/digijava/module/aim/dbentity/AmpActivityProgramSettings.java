package org.digijava.module.aim.dbentity;

import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.ProgramUtil;

import java.io.Serializable;
import java.util.Map;
import javax.persistence.*;

@Entity
@Table(name = "AMP_PROGRAM_SETTINGS")
public class AmpActivityProgramSettings implements Serializable, Identifiable {

    /**
     *
     */
    private static final long serialVersionUID = -2140430282705711013L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_program_settings_seq_generator")
    @SequenceGenerator(name = "amp_program_settings_seq_generator", sequenceName = "AMP_PROGRAM_SETTINGS_seq", allocationSize = 1)
    @Column(name = "amp_program_settings_id")
    private Long ampProgramSettingsId;

    @Column(name = "allow_multiple")
    private boolean allowMultiple;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "default_hierarchy")
    private AmpTheme defaultHierarchy;
    @Transient
    private static final int LEVEL_0 = 0;
    @Transient

    private static final int LEVEL_1 = 1;
    @Transient

    private static final int LEVEL_2 = 2;
    @Transient

    private static final int LEVEL_3 = 3;
    @Transient

    private static final int LEVEL_4 = 4;
    @Transient

    private static final int LEVEL_5 = 5;
    @Transient

    private static final int LEVEL_6 = 6;
    @Transient

    private static final int LEVEL_7 = 7;
    @Transient

    private static final int LEVEL_8 = 8;
    @Transient


    private static final Map<Integer, String> NATIONAL_PLANNING_OBJECTIVES_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_0, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_0)
                    .put(LEVEL_1, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1)
                    .put(LEVEL_2, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2)
                    .put(LEVEL_3, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3)
                    .put(LEVEL_4, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_4)
                    .put(LEVEL_5, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_5)
                    .put(LEVEL_6, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_6)
                    .put(LEVEL_7, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_7)
                    .put(LEVEL_8, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_8)
                    .build();
    private static final Map<Integer, String> INDIRECT_PRIMARY_PROGRAM_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_0, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_0)
                    .put(LEVEL_1, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_1)
                    .put(LEVEL_2, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_2)
                    .put(LEVEL_3, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_3)
                    .put(LEVEL_4, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_4)
                    .put(LEVEL_5, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_5)
                    .put(LEVEL_6, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_6)
                    .put(LEVEL_7, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_7)
                    .put(LEVEL_8, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_8)
                    .build();

    private static final Map<Integer, String> PRIMARY_PROGRAM_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_0, ColumnConstants.PRIMARY_PROGRAM_LEVEL_0)
                    .put(LEVEL_1, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1)
                    .put(LEVEL_2, ColumnConstants.PRIMARY_PROGRAM_LEVEL_2)
                    .put(LEVEL_3, ColumnConstants.PRIMARY_PROGRAM_LEVEL_3)
                    .put(LEVEL_4, ColumnConstants.PRIMARY_PROGRAM_LEVEL_4)
                    .put(LEVEL_5, ColumnConstants.PRIMARY_PROGRAM_LEVEL_5)
                    .put(LEVEL_6, ColumnConstants.PRIMARY_PROGRAM_LEVEL_6)
                    .put(LEVEL_7, ColumnConstants.PRIMARY_PROGRAM_LEVEL_7)
                    .put(LEVEL_8, ColumnConstants.PRIMARY_PROGRAM_LEVEL_8)
                    .build();

    private static final Map<Integer, String> SECONDARY_PROGRAM_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_0, ColumnConstants.SECONDARY_PROGRAM_LEVEL_0)
                    .put(LEVEL_1, ColumnConstants.SECONDARY_PROGRAM_LEVEL_1)
                    .put(LEVEL_2, ColumnConstants.SECONDARY_PROGRAM_LEVEL_2)
                    .put(LEVEL_3, ColumnConstants.SECONDARY_PROGRAM_LEVEL_3)
                    .put(LEVEL_4, ColumnConstants.SECONDARY_PROGRAM_LEVEL_4)
                    .put(LEVEL_5, ColumnConstants.SECONDARY_PROGRAM_LEVEL_5)
                    .put(LEVEL_6, ColumnConstants.SECONDARY_PROGRAM_LEVEL_6)
                    .put(LEVEL_7, ColumnConstants.SECONDARY_PROGRAM_LEVEL_7)
                    .put(LEVEL_8, ColumnConstants.SECONDARY_PROGRAM_LEVEL_8)
                    .build();

    private static final Map<Integer, String> TERTIARY_PROGRAM_COLUMNS_BY_LEVEL =
            new ImmutableMap.Builder<Integer, String>()
                    .put(LEVEL_0, ColumnConstants.TERTIARY_PROGRAM_LEVEL_0)
                    .put(LEVEL_1, ColumnConstants.TERTIARY_PROGRAM_LEVEL_1)
                    .put(LEVEL_2, ColumnConstants.TERTIARY_PROGRAM_LEVEL_2)
                    .put(LEVEL_3, ColumnConstants.TERTIARY_PROGRAM_LEVEL_3)
                    .put(LEVEL_4, ColumnConstants.TERTIARY_PROGRAM_LEVEL_4)
                    .put(LEVEL_5, ColumnConstants.TERTIARY_PROGRAM_LEVEL_5)
                    .put(LEVEL_6, ColumnConstants.TERTIARY_PROGRAM_LEVEL_6)
                    .put(LEVEL_7, ColumnConstants.TERTIARY_PROGRAM_LEVEL_7)
                    .put(LEVEL_8, ColumnConstants.TERTIARY_PROGRAM_LEVEL_8)
                    .build();

    public static final Map<String, String> NAME_TO_COLUMN =
            new ImmutableMap.Builder<String, String>().put(ProgramUtil.NATIONAL_PLAN_OBJECTIVE,
                    ProgramUtil.NATIONAL_PLANNING_OBJECTIVES)
                    .put(ProgramUtil.PRIMARY_PROGRAM,
                            ProgramUtil.PRIMARY_PROGRAM)
                    .put(ProgramUtil.SECONDARY_PROGRAM,
                            ProgramUtil.SECONDARY_PROGRAM)
                    .put(ProgramUtil.TERTIARY_PROGRAM,
                            ProgramUtil.TERTIARY_PROGRAM)
                    .put(ProgramUtil.INDIRECT_PRIMARY_PROGRAM,
                            ProgramUtil.INDIRECT_PRIMARY_PROGRAM)
                    .build();

    public static final Map<String, Map<Integer, String>> NAME_TO_COLUMN_AND_LEVEL =
            new ImmutableMap.Builder<String, Map<Integer, String>>()
                    .put(ProgramUtil.NATIONAL_PLAN_OBJECTIVE, NATIONAL_PLANNING_OBJECTIVES_COLUMNS_BY_LEVEL)
                    .put(ProgramUtil.PRIMARY_PROGRAM, PRIMARY_PROGRAM_COLUMNS_BY_LEVEL)
                    .put(ProgramUtil.SECONDARY_PROGRAM, SECONDARY_PROGRAM_COLUMNS_BY_LEVEL)
                    .put(ProgramUtil.TERTIARY_PROGRAM, TERTIARY_PROGRAM_COLUMNS_BY_LEVEL)
                    .put(ProgramUtil.INDIRECT_PRIMARY_PROGRAM, INDIRECT_PRIMARY_PROGRAM_COLUMNS_BY_LEVEL)
                    .build();


        public AmpTheme getDefaultHierarchy() {
                return defaultHierarchy;
        }

        public boolean isAllowMultiple() {
                return allowMultiple;
        }

        public Long getAmpProgramSettingsId() {
                return ampProgramSettingsId;
        }

        public String getName() {
                return name;
        }

        public void setDefaultHierarchy(AmpTheme defaultHierarchy) {
                this.defaultHierarchy = defaultHierarchy;
        }

        public void setAllowMultiple(boolean allowMultiple) {
                this.allowMultiple = allowMultiple;
        }

        public void setAmpProgramSettingsId(Long ampProgramSettingsId) {
                this.ampProgramSettingsId = ampProgramSettingsId;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Long getDefaultHierarchyId() {
                Long id = null;
                if (defaultHierarchy != null) {
                        id = defaultHierarchy.getAmpThemeId();
                }
                return id;
        }

        public void setDefaultHierarchyId(Long id) {
                if (id != null && id != new Long( -1)) {
                        this.defaultHierarchy = ProgramUtil.getThemeById(id);
                }
                else {
                        this.defaultHierarchy = null;
                }
        }

        public AmpActivityProgramSettings() {}

        public AmpActivityProgramSettings(String name) {
                this.name = name;
                this.allowMultiple = false;
                this.defaultHierarchy = null;
        }

        @Override
        public Object getIdentifier() {
            return ampProgramSettingsId;
//          return null;
        }

    @Override
    public String toString() {
        return name;
    }
}
