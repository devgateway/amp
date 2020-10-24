package org.digijava.kernel.ampapi.endpoints.ndd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.dgfoundation.amp.onepager.util.IndirectProgramUpdater;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;

import static org.digijava.module.aim.helper.GlobalSettingsConstants.INDIRECT_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.PRIMARY_PROGRAM;

/**
 * @author Octavian Ciubotaru
 */
public class NDDService {

    static class SingleProgramData implements Serializable {
        private Long id;
        private String value;

        SingleProgramData(Long id, String value) {
            this.id = id;
            this.value = value;
        }

        public Long getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }

    public MappingConfiguration getMappingConfiguration() {
        PossibleValue src = convert(getSrcProgramRoot(), IndirectProgramUpdater.INDIRECT_MAPPING_LEVEL);
        PossibleValue dst = convert(getDstProgramRoot(), IndirectProgramUpdater.INDIRECT_MAPPING_LEVEL);

        List<PossibleValue> allPrograms = new ArrayList<>();
        getAvailablePrograms().forEach(ampTheme -> {
            PossibleValue pv = convert(ampTheme, IndirectProgramUpdater.INDIRECT_MAPPING_LEVEL);
            allPrograms.add(pv);
        });

        List<AmpIndirectTheme> mapping = loadMapping();

        return new MappingConfiguration(mapping, new SingleProgramData(src.getId(), src.getValue()),
                new SingleProgramData(dst.getId(), dst.getValue()), allPrograms);
    }

    /**
     * Returns a list of first level programs that are part of the multi-program configuration.
     * Use a simplified object to reduce bandwidth.
     */
    public List<AmpTheme> getAvailablePrograms() {
        List<AmpTheme> programs = ProgramUtil.getAllPrograms()
                .stream().filter(p -> p.getIndlevel().equals(0) && p.getProgramSettings().size() > 0)
                .collect(Collectors.toList());
        return programs;
    }

    @SuppressWarnings("unchecked")
    private List<AmpIndirectTheme> loadMapping() {
        return PersistenceManager.getSession()
                .createCriteria(AmpIndirectTheme.class)
                .setCacheable(true)
                .list();
    }

    @SuppressWarnings("unchecked")
    public void updateMapping(List<AmpIndirectTheme> mapping) {
        validate(mapping);

        PersistenceManager.getSession().createCriteria(AmpIndirectTheme.class).setCacheable(true).list()
                .forEach(PersistenceManager.getSession()::delete);

        mapping.forEach(PersistenceManager.getSession()::save);
    }

    /**
     * Update the GS for Primary Program and Indirect Program.
     *
     * @param mapping
     */
    public void updateMainProgramsMapping(AmpIndirectTheme mapping) {
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
     * Validate the mapping.
     * <p>Mapping is considered valid when:</p>
     * <ul><li>all source and destination programs are specified</li>
     * <li>source and destination programs are for level {@link IndirectProgramUpdater#INDIRECT_MAPPING_LEVEL}</li>
     * <li>source program root is the one returned by {@link #getSrcProgramRoot()}</li>
     * <li>destination program root is the one returned by {@link #getDstProgramRoot()}</li>
     * <li>the same source and destination program appear only once in the mapping</li></ul>
     *
     * @throws ValidationException when the mapping is invalid
     */
    private void validate(List<AmpIndirectTheme> mapping) {
        AmpTheme srcProgramRoot = getSrcProgramRoot();
        AmpTheme dstProgramRoot = getDstProgramRoot();

        boolean hasInvalidMappings = mapping.stream().anyMatch(
                m -> m.getNewTheme() == null
                        || m.getOldTheme() == null
                        || !m.getOldTheme().getIndlevel().equals(IndirectProgramUpdater.INDIRECT_MAPPING_LEVEL)
                        || !m.getNewTheme().getIndlevel().equals(IndirectProgramUpdater.INDIRECT_MAPPING_LEVEL)
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
    private AmpTheme getDstProgramRoot() {
        String indirectProgram = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.INDIRECT_PROGRAM);
        if (indirectProgram == null) {
            throw new RuntimeException(GlobalSettingsConstants.INDIRECT_PROGRAM + " is not configured.");
        }
        return ProgramUtil.getTheme(Long.valueOf(indirectProgram));
    }

    private AmpTheme getSrcProgramRoot() {
        String primaryProgram = FeaturesUtil.getGlobalSettingValue(PRIMARY_PROGRAM);
        if (primaryProgram == null) {
            throw new RuntimeException(PRIMARY_PROGRAM + " is not configured.");
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
