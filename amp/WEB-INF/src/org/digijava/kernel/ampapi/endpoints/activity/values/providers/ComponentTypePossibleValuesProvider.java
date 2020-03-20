package org.digijava.kernel.ampapi.endpoints.activity.values.providers;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.util.ComponentsUtil;

/**
 * @author Nadejda Mandrescu
 */
public class ComponentTypePossibleValuesProvider implements PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        return ComponentsUtil.getAmpComponentTypes(true).stream()
                .map(type -> toPossibleValue(type, translatorService))
                .collect(toList());
    }

    private PossibleValue toPossibleValue(AmpComponentType type, TranslatorService translatorService) {
        return new PossibleValue(type.getType_id(), type.getName(), translatorService.translateLabel(type.getName()));
    }

    @Override
    public boolean isAllowed(Long id) {
        AmpComponentType ct = ComponentsUtil.getComponentTypeById(id);
        return ct != null && Boolean.TRUE.equals(ct.getEnable());
    }

}
