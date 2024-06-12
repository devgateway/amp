package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.exception.DgException;

import java.util.List;

/**
 *
 * @author acartaleanu
 *
 * abstract class as a scaffold for custom classes that would
 * employ special procedures in obtaining possible values and
 * specific activity values in for a full project information
 */
public interface PossibleValuesProvider {

    List<PossibleValue> getPossibleValues(TranslatorService translatorService) throws DgException;

    boolean isAllowed(Long id);
}
