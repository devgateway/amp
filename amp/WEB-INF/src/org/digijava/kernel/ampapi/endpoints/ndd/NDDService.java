package org.digijava.kernel.ampapi.endpoints.ndd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeMapping;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.INDIRECT_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.PRIMARY_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.SDG_DESTINATION_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.SDG_SOURCE_PROGRAM;

/**
 * @author Octavian Ciubotaru
 */
public class NDDService {

    /**
     * Level at which we have explicit indirect program mappings defined.
     */
    public static final int INDIRECT_MAPPING_LEVEL = 3;
    public static final int SDG_MAPPING_LEVEL = 3;

    static class SingleProgramData implements Serializable {
        private Long id;
        private String value;
        private boolean isIndirect;

        SingleProgramData(Long id, String value, boolean isIndirect) {
            this.id = id;
            this.value = value;
            this.isIndirect = isIndirect;
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
    }

    public IndirectProgramMappingConfiguration getIndirectProgramMappingConfiguration() {
        AmpTheme src = getSrcIndirectProgramRoot();
        AmpTheme dst = getDstIndirectProgramRoot();
        PossibleValue srcPV = src != null ? convert(src, INDIRECT_MAPPING_LEVEL) : null;
        PossibleValue dstPV = dst != null ? convert(dst, INDIRECT_MAPPING_LEVEL) : null;

        List<PossibleValue> allPrograms = new ArrayList<>();
        getAvailablePrograms(false).forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, INDIRECT_MAPPING_LEVEL);
            allPrograms.add(pv);
        });
        getAvailablePrograms(true).forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, INDIRECT_MAPPING_LEVEL);
            allPrograms.add(pv);
        });

        List<AmpIndirectTheme> mapping = loadIndirectMapping();

        SingleProgramData srcSPD = srcPV != null ? new SingleProgramData(srcPV.getId(), srcPV.getValue(), false) : null;
        SingleProgramData dstSPD = dstPV != null ? new SingleProgramData(dstPV.getId(), dstPV.getValue(), true) : null;
        return new IndirectProgramMappingConfiguration(mapping, srcSPD, dstSPD, allPrograms);
    }

    public ProgramMappingConfiguration getSDGProgramMappingConfiguration() {
        AmpTheme src = getSrcSDGProgramRoot();
        AmpTheme dst = getDstSDGProgramRoot();
        PossibleValue srcPV = src != null ? convert(src, SDG_MAPPING_LEVEL) : null;
        PossibleValue dstPV = dst != null ? convert(dst, SDG_MAPPING_LEVEL) : null;

        List<PossibleValue> allPrograms = new ArrayList<>();
        getAvailablePrograms(false).forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, SDG_MAPPING_LEVEL);
            allPrograms.add(pv);
        });
        getAvailablePrograms(false).forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, SDG_MAPPING_LEVEL);
            allPrograms.add(pv);
        });

        List<AmpThemeMapping> mapping = loadMapping();

        SingleProgramData srcSPD = srcPV != null ? new SingleProgramData(srcPV.getId(), srcPV.getValue(), false) : null;
        SingleProgramData dstSPD = dstPV != null ? new SingleProgramData(dstPV.getId(), dstPV.getValue(), false) : null;
        return new ProgramMappingConfiguration(mapping, srcSPD, dstSPD, allPrograms);
    }

    /**
     * Returns a list of first level programs.
     */
    public List<AmpTheme> getAvailablePrograms(boolean indirect) {
        List<AmpTheme> programs = ProgramUtil.getAllPrograms()
                .stream().filter(p -> p.getIndlevel().equals(0)
                        && (indirect ? p.getProgramSettings().size() == 0 : p.getProgramSettings().size() > 0))
                .collect(Collectors.toList());
        return programs;
    }

    /**
     * Returns a list of programs available for mapping classified by direct/indirect (src/dst).
     * @return
     */
    public List<SingleProgramData> getSinglePrograms(final boolean includeIndirectPrograms) {
        List<SingleProgramData> availablePrograms = new ArrayList<>();
        List<AmpTheme> src = getAvailablePrograms(false);
        List<AmpTheme> dst = getAvailablePrograms(false);
        availablePrograms.addAll(src.stream().map(p -> new SingleProgramData(p.getAmpThemeId(), p.getName(), false))
                .collect(Collectors.toList()));
        if (includeIndirectPrograms) {
            availablePrograms.addAll(dst.stream().map(p -> new SingleProgramData(p.getAmpThemeId(), p.getName(), true))
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

        mapping.forEach(PersistenceManager.getSession()::save);
    }

    public void updateSDGProgramsMapping(List<AmpThemeMapping> mapping) {
        validateSDGProgramMapping(mapping);

        PersistenceManager.getSession().createCriteria(AmpThemeMapping.class).setCacheable(true).list()
                .forEach(PersistenceManager.getSession()::delete);

        mapping.forEach(PersistenceManager.getSession()::save);
    }

    /**
     * Update the GS for Primary Program and Indirect Program.
     *
     * @param mapping
     */
    public void updateMainIndirectProgramsMapping(AmpIndirectTheme mapping) {
        if (mapping.getNewTheme() != null && mapping.getOldTheme() != null
                && !mapping.getNewTheme().getAmpThemeId().equals(mapping.getOldTheme().getAmpThemeId())) {
            AmpGlobalSettings srcGS = FeaturesUtil.getGlobalSetting(PRIMARY_PROGRAM);
            srcGS.setGlobalSettingsValue(mapping.getOldTheme().getAmpThemeId().toString());
            FeaturesUtil.updateGlobalSetting(srcGS);
            AmpGlobalSettings dstGS = FeaturesUtil.getGlobalSetting(INDIRECT_PROGRAM);
            dstGS.setGlobalSettingsValue(mapping.getNewTheme().getAmpThemeId().toString());
            FeaturesUtil.updateGlobalSetting(dstGS);
        }
    }

    /**
     * Update the GS for Primary Program and Indirect Program.
     *
     * @param mapping
     */
    public void updateMainSDGProgramsMapping(final AmpThemeMapping mapping) {
        if (mapping.getSrcTheme() != null && mapping.getDstTheme() != null
                && !mapping.getSrcTheme().getAmpThemeId().equals(mapping.getDstTheme().getAmpThemeId())) {
            AmpGlobalSettings srcGS = FeaturesUtil.getGlobalSetting(SDG_SOURCE_PROGRAM);
            srcGS.setGlobalSettingsValue(mapping.getSrcTheme().getAmpThemeId().toString());
            FeaturesUtil.updateGlobalSetting(srcGS);
            AmpGlobalSettings dstGS = FeaturesUtil.getGlobalSetting(SDG_DESTINATION_PROGRAM);
            dstGS.setGlobalSettingsValue(mapping.getDstTheme().getAmpThemeId().toString());
            FeaturesUtil.updateGlobalSetting(dstGS);
        }
    }

    /**
     * Validate the mapping.
     * <p>Mapping is considered valid when:</p>
     * <ul><li>all source and destination programs are specified</li>
     * <li>source and destination programs are for level {@link #INDIRECT_MAPPING_LEVEL}</li>
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
                        || !m.getOldTheme().getIndlevel().equals(INDIRECT_MAPPING_LEVEL)
                        || !m.getNewTheme().getIndlevel().equals(INDIRECT_MAPPING_LEVEL)
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
     * <li>source and destination programs are for level {@link #SDG_MAPPING_LEVEL}</li>
     * <li>source program root is the one returned by {@link #getSrcSDGProgramRoot()}</li>
     * <li>destination program root is the one returned by {@link #getDstSDGProgramRoot()}</li>
     * <li>the same source and destination program appear only once in the mapping</li></ul>
     *
     * @throws ValidationException when the mapping is invalid
     */
    private void validateSDGProgramMapping(List<AmpThemeMapping> mapping) {
        AmpTheme srcProgramRoot = getSrcSDGProgramRoot();
        AmpTheme dstProgramRoot = getDstSDGProgramRoot();

        boolean hasInvalidMappings = mapping.stream().anyMatch(
                m -> m.getSrcTheme() == null
                        || m.getDstTheme() == null
                        || !m.getDstTheme().getIndlevel().equals(SDG_MAPPING_LEVEL)
                        || !m.getDstTheme().getIndlevel().equals(SDG_MAPPING_LEVEL)
                        || !getRoot(m.getDstTheme()).equals(srcProgramRoot)
                        || !getRoot(m.getSrcTheme()).equals(dstProgramRoot));

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
        if (theme.getIndlevel() < level) {
            children = theme.getSiblings().stream()
                    .map(p -> convert(p, level))
                    .collect(Collectors.toList());
        } else {
            children = ImmutableList.of();
        }
        return new PossibleValue(theme.getAmpThemeId(), theme.getName())
                .withChildren(children);
    }

    /**
     * Returns the root of the indirect program. Configured by {@link GlobalSettingsConstants#INDIRECT_PROGRAM}
     * Global Setting.
     */
    public static AmpTheme getDstIndirectProgramRoot() {
        return getProgramRoot(INDIRECT_PROGRAM);
    }

    public static AmpTheme getSrcIndirectProgramRoot() {
        return getProgramRoot(PRIMARY_PROGRAM);
    }

    /**
     * Returns the root of the sdg program. Configured by {@link GlobalSettingsConstants#SDG_DESTINATION_PROGRAM}
     * Global Setting.
     */
    public static AmpTheme getDstSDGProgramRoot() {
        return getProgramRoot(SDG_DESTINATION_PROGRAM);
    }

    public static AmpTheme getSrcSDGProgramRoot() {
        return getProgramRoot(SDG_SOURCE_PROGRAM);
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
