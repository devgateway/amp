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
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * @author Octavian Ciubotaru
 */
public class NDDService {

    class SingleProgramData implements Serializable {
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

        List<AmpIndirectTheme> mapping = loadMapping();

        return new MappingConfiguration(mapping, src, dst);
    }

    /**
     * Returns a list of first level programs that are part of the multi-program configuration.
     * Use a simplified object to reduce bandwidth.
     */
    public List<SingleProgramData> getAvailablePrograms() {
        List<SingleProgramData> singleProgramDataList = ProgramUtil.getAllPrograms()
                .stream().filter(p -> p.getIndlevel().equals(0) && p.getProgramSettings().size() > 0)
                .map(p -> new SingleProgramData(p.getAmpThemeId(), p.getName()))
                .collect(Collectors.toList());
        return singleProgramDataList;
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
     * Validate the mapping.
     * <p>Mapping is considered valid when:</p>
     * <ul><li>all source and destination programs are specified</li>
     * <li>source and destination programs are for level {@link IndirectProgramUpdater#INDIRECT_MAPPING_LEVEL}</li>
     * <li>source program root is the one returned by {@link #getSrcProgramRoot()}</li>
     * <li>destination program root is the one returned by {@link #getDstProgramRoot()}</li>
     * <li>the same source and destination program appear only once in the mapping</li></ul>
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

    /**
     * Returns the root of the program used compute the indirect programs. It is the program tree configured as
     * the Primary Program.
     */
    private AmpTheme getSrcProgramRoot() {
        try {
            AmpActivityProgramSettings setting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
            return setting.getDefaultHierarchy();
        } catch (DgException e) {
            throw new RuntimeException("Failed to load program config.", e);
        }
    }

    public AmpTheme getRoot(AmpTheme theme) {
        while (theme.getParentThemeId() != null) {
            theme = theme.getParentThemeId();
        }
        return theme;
    }
}
