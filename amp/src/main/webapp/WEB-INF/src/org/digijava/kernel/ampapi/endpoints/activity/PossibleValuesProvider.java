package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;

/**
 * 
 * @author acartaleanu
 *
 * abstract class as a scaffold for custom classes that would 
 * employ special procedures in obtaining possible values and 
 * specific activity values in for a full project information
 */
public interface PossibleValuesProvider {

    List<PossibleValue> getPossibleValues(TranslatorService translatorService);

    boolean isAllowed(Long id);
}
