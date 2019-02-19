package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Class used for exporting an activity as a JSON
 * 
 * @author Viorel Chihai
 */
public class ActivityExporter extends ObjectExporter<AmpActivityVersion> {

    private JsonBean filter;
    private List<String> filteredFields = new ArrayList<>();

    public ActivityExporter(JsonBean filter) {
        super(AmpFieldsEnumerator.getEnumerator().getActivityFields());
        this.filter = filter;
    }

    @Override
    public JsonBean export(AmpActivityVersion object) {
        JsonBean resultJson = new JsonBean();

        if (!InterchangeUtils.validateFilterActivityFields(filter, resultJson, getApiFields())) {
            return resultJson;
        }

        if (filter != null) {
            this.filteredFields = (List<String>) filter.get(ActivityEPConstants.FILTER_FIELDS);
        }

        return super.export(object);
    }

    /**
     * @param filteredFieldPath the underscorified path to the field
     * @return boolean, if the field should be exported in the result Json
     */
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
