package org.digijava.kernel.ampapi.endpoints.common.values.providers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesDAO;
import org.digijava.kernel.ampapi.endpoints.activity.ProgramExtraInfo;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.dbentity.AmpThemeMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

/**
 * Possible values provider for indirect programs. Returns all AmpTheme entries regardless
 * of program type (config type). This is needed because indirect programs are auto-mapped
 * via NDD and may belong to a different program type than the parent activity program.
 *
 * Unlike {@link ThemePossibleValuesProvider} this class does NOT implement
 * {@link DiscriminatedPossibleValuesProvider}, so the PossibleValuesEnumerator instantiates
 * it with a no-arg constructor and does not pass the parent discriminator value.
 */
public class AllThemesPossibleValuesProvider extends AbstractPossibleValuesBaseProvider {

    private Map<Long, Set<Long>> mappedPrograms;

    @Override
    public boolean isAllowed(Long id) {
        // Indirect programs can be of any type; all existing themes are valid references
        return id != null;
    }

    @Override
    @SuppressWarnings("null")  // ArrayListMultimap supports null keys (used for root nodes); same pattern as AbstractPossibleValuesDAOProvider
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<Object[]> items = possibleValuesDAO.getAllThemes();

        if (mappedPrograms == null) {
            mappedPrograms = getMappedPrograms();
        }

        ListMultimap<Long, PossibleValue> groupedValues = ArrayListMultimap.create();
        for (Object[] item : items) {
            Long id = ((Number) item[0]).longValue();
            String value = (String) item[1];
            Long parentId = item.length > 2 ? (Long) item[PossibleValuesDAO.THEME_PARENT_ID_POS] : null;
            ProgramExtraInfo extraInfo = new ProgramExtraInfo(parentId, mappedPrograms.get(id));
            Map<String, String> translatedValues = translatorService.translateLabel(value);
            // groupedValues keyed by parentId (null for root nodes), same pattern as AbstractPossibleValuesDAOProvider
            groupedValues.put((Long) parentId, new PossibleValue(id, value, translatedValues, extraInfo));
        }

        return convertToHierarchical(groupedValues);
    }

    private Map<Long, Set<Long>> getMappedPrograms() {
        List<AmpThemeMapping> list = possibleValuesDAO.getMappedThemes();
        if (list != null) {
            return list.stream().collect(groupingBy(atm -> atm.getSrcTheme().getAmpThemeId(),
                    mapping(atm -> atm.getDstTheme().getAmpThemeId(), toSet())));
        }
        return new HashMap<>();
    }
}
