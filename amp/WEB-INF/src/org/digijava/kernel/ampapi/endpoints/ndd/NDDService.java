package org.digijava.kernel.ampapi.endpoints.ndd;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.common.values.ValueConverter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.services.sync.model.SyncConstants;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpOfflineChangelog;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeMapping;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;

import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_INDIRECT_LEVEL;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_PROGRAM_LEVEL;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_SOURCE_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.PRIMARY_PROGRAM;
import static org.digijava.module.aim.util.ProgramUtil.INDIRECT_PRIMARY_PROGRAM;

/**
 * @author Octavian Ciubotaru
 */
public class NDDService {

    private static final Logger LOGGER = Logger.getLogger(ValueConverter.class);
    /**
     * Level at which we have explicit indirect program mappings defined.
     */
    public static final int MAX_MAPPING_LEVEL = 3;

    public static class SingleProgramData implements Serializable {
        private Long id;
        private String value;
        private boolean isIndirect;
        private int levels;

        SingleProgramData(Long id, String value, boolean isIndirect, int levels) {
            this.id = id;
            this.value = value;
            this.isIndirect = isIndirect;
            this.levels = levels;
        }

        public Long getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public boolean isIndirect() {
            return isIndirect;
        }

        public int getLevels() {
            return levels;
        }
    }

    public static int getMappingLevel(String gs) {
        return Integer.parseInt(FeaturesUtil.getGlobalSetting(gs).getGlobalSettingsValue());
    }

    public IndirectProgramMappingConfiguration getIndirectProgramMappingConfiguration() {
        AmpTheme src = getSrcIndirectProgramRoot();
        AmpTheme dst = getDstIndirectProgramRoot();
        int level = getMappingLevel(MAPPING_INDIRECT_LEVEL);
        PossibleValue srcPV = src != null ? convert(src, level) : null;
        PossibleValue dstPV = dst != null ? convert(dst, level) : null;

        List<PossibleValue> allPrograms = new ArrayList<>();
        getAvailablePrograms(false).forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, MAX_MAPPING_LEVEL);
            allPrograms.add(pv);
        });
        getAvailablePrograms(true).forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, MAX_MAPPING_LEVEL);
            allPrograms.add(pv);
        });

        List<AmpIndirectTheme> mapping = loadIndirectMapping();

        SingleProgramData srcSPD = srcPV != null ? new SingleProgramData(srcPV.getId(), srcPV.getValue(), false,
                level) : null;
        SingleProgramData dstSPD = dstPV != null ? new SingleProgramData(dstPV.getId(), dstPV.getValue(), true,
                level) : null;
        return new IndirectProgramMappingConfiguration(mapping, srcSPD, dstSPD, allPrograms);
    }

    public ProgramMappingConfiguration getProgramMappingConfiguration() {
        AmpTheme src = getSrcProgramRoot();
        AmpTheme dst = getDstProgramRoot();
        int level = getMappingLevel(MAPPING_PROGRAM_LEVEL);
        PossibleValue srcPV = src != null ? convert(src, level) : null;
        PossibleValue dstPV = dst != null ? convert(dst, level) : null;

        List<PossibleValue> allPrograms = new ArrayList<>();
        getAvailablePrograms(false).forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, MAX_MAPPING_LEVEL);
            allPrograms.add(pv);
        });

        List<AmpThemeMapping> mapping = loadMapping();

        SingleProgramData srcSPD = srcPV != null ? new SingleProgramData(srcPV.getId(), srcPV.getValue(), false,
                level) : null;
        SingleProgramData dstSPD = dstPV != null ? new SingleProgramData(dstPV.getId(), dstPV.getValue(), false,
                level) : null;
        return new ProgramMappingConfiguration(mapping, srcSPD, dstSPD, allPrograms);
    }

    /**
     * Returns a list of first level programs.
     */
    public List<AmpTheme> getAvailablePrograms(boolean indirect) {
        AmpActivityProgramSettings indirectProgramSetting = null;
        try {
            indirectProgramSetting = ProgramUtil.getAmpActivityProgramSettings(INDIRECT_PRIMARY_PROGRAM);
        } catch (DgException e) {
            e.printStackTrace();
        }
        AmpActivityProgramSettings finalIndirectProgramSetting = indirectProgramSetting;
        List<AmpTheme> programs = ProgramUtil.getAllPrograms()
                .stream().filter(p -> p.getIndlevel().equals(0) && !p.isSoftDeleted())
                .filter(p -> {
                    if (indirect) {
                        return p.getProgramSettings().size() == 0
                                || (p.getProgramSettings().size() == 1
                                && p.getProgramSettings().contains(finalIndirectProgramSetting));
                    } else {
                        return p.getProgramSettings().size() > 0
                                && !p.getProgramSettings().contains(finalIndirectProgramSetting);
                    }
                })
                .collect(Collectors.toList());
        return programs;
    }

    /**
     * Returns a list of programs available for mapping classified by direct/indirect (src/dst).
     *
     * @return
     */
    public List<SingleProgramData> getSinglePrograms(final boolean includeIndirectPrograms) {
        List<SingleProgramData> availablePrograms = new ArrayList<>();
        List<AmpTheme> src = getAvailablePrograms(false);
        List<AmpTheme> dst = getAvailablePrograms(true);
        availablePrograms.addAll(src.stream().map(p -> new SingleProgramData(p.getAmpThemeId(), p.getName(), false,
                ProgramUtil.getMaxDepth(p, null)))
                .collect(Collectors.toList()));
        if (includeIndirectPrograms) {
            availablePrograms.addAll(dst.stream().map(p -> new SingleProgramData(p.getAmpThemeId(), p.getName(), true,
                    ProgramUtil.getMaxDepth(p, null)))
                    .collect(Collectors.toList()));
        }
        return availablePrograms;
    }

    @SuppressWarnings("unchecked")
    private List<AmpIndirectTheme> loadIndirectMapping() {
        return PersistenceManager.getSession()
                .createCriteria(AmpIndirectTheme.class)
                .setCacheable(true)
                .list();
    }

    @SuppressWarnings("unchecked")
    private List<AmpThemeMapping> loadMapping() {
        return PersistenceManager.getSession()
                .createCriteria(AmpThemeMapping.class)
                .setCacheable(true)
                .list();
    }

    @SuppressWarnings("unchecked")
    public void updateIndirectProgramsMapping(List<AmpIndirectTheme> mapping) {
        validateIndirectProgramMapping(mapping);

        PersistenceManager.getSession().createCriteria(AmpIndirectTheme.class).setCacheable(true).list()
                .forEach(PersistenceManager.getSession()::delete);

        // TODO: Add param in UI to decide if we want to clear current activity mappings or not.
        /* PersistenceManager.getSession().createCriteria(AmpActivityIndirectProgram.class).setCacheable(true).list()
                .forEach(PersistenceManager.getSession()::delete); */

        mapping.forEach(PersistenceManager.getSession()::save);
    }

    public void updateProgramsMapping(List<AmpThemeMapping> mapping) {
        validateProgramMapping(mapping);

        PersistenceManager.getSession().createCriteria(AmpThemeMapping.class).setCacheable(true).list()
                .forEach(PersistenceManager.getSession()::delete);

        mapping.forEach(PersistenceManager.getSession()::save);

        // Update changelog to force a sync in offline.
        try {
            ProgramUtil.getAllThemes(true).forEach(p -> {
                AmpOfflineChangelog changelog = new AmpOfflineChangelog();
                changelog.setEntityId(p.getAmpThemeId().toString());
                changelog.setEntityName(SyncConstants.Entities.THEME);
                changelog.setOperationName(SyncConstants.Ops.UPDATED);
                changelog.setOperationTime(new Date());
                PersistenceManager.getSession().save(changelog);
            });
        } catch (DgException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the GS for Primary Program and Indirect Program.
     *
     * @param mapping
     */
    public void updateMainIndirectProgramsMapping(AmpIndirectTheme mapping) {
        try {
            AmpGlobalSettings srcGS = FeaturesUtil.getGlobalSetting(PRIMARY_PROGRAM);
            AmpActivityProgramSettings indirectProgramSetting =
                    ProgramUtil.getAmpActivityProgramSettings(INDIRECT_PRIMARY_PROGRAM);
            AmpGlobalSettings level = FeaturesUtil.getGlobalSetting(MAPPING_INDIRECT_LEVEL);
            if (mapping.getNewTheme() != null && mapping.getOldTheme() != null) {
                if (!mapping.getNewTheme().getAmpThemeId().equals(mapping.getOldTheme().getAmpThemeId())) {
                    srcGS.setGlobalSettingsValue(mapping.getOldTheme().getAmpThemeId().toString());
                    FeaturesUtil.updateGlobalSetting(srcGS);

                    if (indirectProgramSetting == null) {
                        indirectProgramSetting = new AmpActivityProgramSettings(INDIRECT_PRIMARY_PROGRAM);

                    }
                    indirectProgramSetting.setDefaultHierarchy(mapping.getNewTheme());
                    PersistenceManager.getSession().saveOrUpdate(indirectProgramSetting);

                    level.setGlobalSettingsValue(mapping.getLevel().toString());
                    FeaturesUtil.updateGlobalSetting(level);
                }
            } else {
                srcGS.setGlobalSettingsValue(null);
                FeaturesUtil.updateGlobalSetting(srcGS);
                if (indirectProgramSetting != null) {
                    PersistenceManager.getSession().delete(indirectProgramSetting);
                }
                level.setGlobalSettingsValue("0");
                FeaturesUtil.updateGlobalSetting(level);
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot save mapping", e);
        }
    }

    /**
     * Update the GS for Primary Program and Indirect Program.
     *
     * @param mapping
     */
    public void updateMainProgramsMapping(final AmpThemeMapping mapping) {
        AmpGlobalSettings srcGS = FeaturesUtil.getGlobalSetting(MAPPING_SOURCE_PROGRAM);
        AmpGlobalSettings dstGS = FeaturesUtil.getGlobalSetting(MAPPING_DESTINATION_PROGRAM);
        AmpGlobalSettings level = FeaturesUtil.getGlobalSetting(MAPPING_PROGRAM_LEVEL);

        if (mapping.getSrcTheme() != null && mapping.getDstTheme() != null) {
            if (!mapping.getSrcTheme().getAmpThemeId().equals(mapping.getDstTheme().getAmpThemeId())) {
                srcGS.setGlobalSettingsValue(mapping.getSrcTheme().getAmpThemeId().toString());
                FeaturesUtil.updateGlobalSetting(srcGS);
                dstGS.setGlobalSettingsValue(mapping.getDstTheme().getAmpThemeId().toString());
                FeaturesUtil.updateGlobalSetting(dstGS);

                level.setGlobalSettingsValue(mapping.getLevel().toString());
                FeaturesUtil.updateGlobalSetting(level);
            }
        } else {
            srcGS.setGlobalSettingsValue(null);
            FeaturesUtil.updateGlobalSetting(srcGS);
            dstGS.setGlobalSettingsValue(null);
            FeaturesUtil.updateGlobalSetting(dstGS);

            level.setGlobalSettingsValue("0");
            FeaturesUtil.updateGlobalSetting(level);
        }
    }

    /**
     * Validate the mapping.
     * <p>Mapping is considered valid when:</p>
     * <ul><li>all source and destination programs are specified</li>
     * <li>source and destination programs are for level 3 or less</li>
     * <li>source program root is the one returned by {@link #getSrcIndirectProgramRoot()}</li>
     * <li>destination program root is the one returned by {@link #getDstIndirectProgramRoot()}</li>
     * <li>the same source and destination program appear only once in the mapping</li></ul>
     *
     * @throws ValidationException when the mapping is invalid
     */
    private void validateIndirectProgramMapping(List<AmpIndirectTheme> mapping) {
        AmpTheme srcProgramRoot = getSrcIndirectProgramRoot();
        AmpTheme dstProgramRoot = getDstIndirectProgramRoot();

        boolean hasInvalidMappings = mapping.stream().anyMatch(
                m -> m.getNewTheme() == null
                        || m.getOldTheme() == null
                        || !m.getOldTheme().getIndlevel().equals(m.getLevel())
                        || !m.getNewTheme().getIndlevel().equals(m.getLevel())
                        || !getRoot(m.getOldTheme()).equals(srcProgramRoot)
                        || !getRoot(m.getNewTheme()).equals(dstProgramRoot));

        if (!hasInvalidMappings) {
            long distinctCount = mapping.stream().map(m -> Pair.of(m.getOldTheme(), m.getNewTheme()))
                    .distinct()
                    .count();
            hasInvalidMappings = mapping.size() > distinctCount;
        }

        if (hasInvalidMappings) {
            throw new ValidationException("Invalid mapping");
        }
    }

    /**
     * Validate the mapping.
     * <p>Consider mapping valid when:</p>
     * <ul><li>all source and destination programs are specified</li>
     * <li>source and destination programs are for configured level </li>
     * <li>source program root is the one returned by {@link #getSrcProgramRoot()}</li>
     * <li>destination program root is the one returned by {@link #getDstProgramRoot()}</li>
     * <li>the same source and destination program appear only once in the mapping</li></ul>
     *
     * @throws ValidationException when the mapping is invalid
     */
    private void validateProgramMapping(List<AmpThemeMapping> mapping) {
        AmpTheme srcProgramRoot = getSrcProgramRoot();
        AmpTheme dstProgramRoot = getDstProgramRoot();

        boolean hasInvalidMappings = mapping.stream().anyMatch(
                m -> m.getSrcTheme() == null
                        || m.getDstTheme() == null
                        || !m.getSrcTheme().getIndlevel().equals(m.getLevel())
                        || !m.getDstTheme().getIndlevel().equals(m.getLevel())
                        || !getRoot(m.getSrcTheme()).equals(srcProgramRoot)
                        || !getRoot(m.getDstTheme()).equals(dstProgramRoot));

        if (!hasInvalidMappings) {
            long distinctCount = mapping.stream().map(m -> Pair.of(m.getDstTheme(), m.getSrcTheme()))
                    .distinct()
                    .count();
            hasInvalidMappings = mapping.size() > distinctCount;
        }

        if (hasInvalidMappings) {
            throw new ValidationException("Invalid mapping");
        }
    }

    /**
     * Convert AmpTheme to PossibleValue object up to the specified level.
     */
    private PossibleValue convert(AmpTheme theme, int level) {
        List<PossibleValue> children;
        if (theme.getIndlevel() < level && !theme.isSoftDeleted()) {
            children = theme.getSiblings().stream()
                    .filter(p -> !p.isSoftDeleted())
                    .map(p -> convert(p, level))
                    .collect(Collectors.toList());
        } else {
            children = ImmutableList.of();
        }
        return new PossibleValue(theme.getAmpThemeId(), theme.getName())
                .withChildren(children);
    }

    /**
     * Returns the root of the indirect program. Configured Indirect Primary Program program setting
     */
    public static AmpTheme getDstIndirectProgramRoot() {
        try {
            AmpActivityProgramSettings indirectProgramSetting =
                    ProgramUtil.getAmpActivityProgramSettings(INDIRECT_PRIMARY_PROGRAM);

            if (indirectProgramSetting != null) {
                return indirectProgramSetting.getDefaultHierarchy();
            } else {
                return null;
            }
        } catch (DgException e) {
            LOGGER.error("getDstProgramRoot", e);
            return null;
        }
    }

    public static AmpTheme getSrcIndirectProgramRoot() {
        return getProgramRoot(PRIMARY_PROGRAM);
    }

    /**
     * Returns the root of the destination program.
     * Configured by {@link GlobalSettingsConstants#MAPPING_DESTINATION_PROGRAM} Global Setting.
     */
    public static AmpTheme getDstProgramRoot() {
        return getProgramRoot(MAPPING_DESTINATION_PROGRAM);
    }

    public static AmpTheme getSrcProgramRoot() {
        return getProgramRoot(MAPPING_SOURCE_PROGRAM);
    }

    public static AmpTheme getProgramRoot(final String rootProgram) {
        String primaryProgram = FeaturesUtil.getGlobalSettingValue(rootProgram);
        if (primaryProgram == null) {
            return null;
        }
        return ProgramUtil.getTheme(Long.valueOf(primaryProgram));
    }

    public AmpTheme getRoot(AmpTheme theme) {
        while (theme.getParentThemeId() != null) {
            theme = theme.getParentThemeId();
        }
        return theme;
    }
}
