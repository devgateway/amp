package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Class used for exporting an activity as a JSON
 *
 * @author Viorel Chihai
 */
public class ActivityExporter extends ObjectExporter<AmpActivityVersion> {

    private Map<String, Object> filter;
    private List<String> filteredFields = new ArrayList<>();

    public ActivityExporter(Map<String, Object> filter) {
        this(new DefaultTranslatedFieldReader(), AmpFieldsEnumerator.getAllEnumerators(), filter);
    }

    public ActivityExporter(TranslatedFieldReader translatedFieldReader, Map<Long, CachingFieldsEnumerator> enumerators,
                            Map<String, Object> filter) {
        super(translatedFieldReader, enumerators);
        this.filter = filter;
    }

    @Override
    public Map<String, Object> export(AmpActivityVersion object, Long fmId) {
        ApiErrorResponse error = ActivityInterchangeUtils.validateFilterActivityFields(filter, getApiFields(fmId));

        if (error != null) {
            return (Map) error.getErrors();
        }

        if (filter != null) {
            this.filteredFields = (List<String>) filter.get(ActivityEPConstants.FILTER_FIELDS);
        }

        return super.export(object, fmId);
    }

    /**
     * @param filteredFieldPath the underscorified path to the field
     * @return boolean, if the field should be exported in the result Json
     */
    @Override
    protected boolean isFiltered(String filteredFieldPath) {
        if (filteredFields.isEmpty()) {
            return true;
        }

        for (String s : filteredFields) {
            if (s.startsWith(filteredFieldPath) || filteredFieldPath.startsWith(s)) {
                return true;
            }
        }

        return false;
    }
}
