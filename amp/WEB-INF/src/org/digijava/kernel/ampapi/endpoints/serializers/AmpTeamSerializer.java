/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.serializers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.dbentity.AmpTeamFilterData;
import org.digijava.module.aim.dbentity.AmpTeam;


/**
 * {@link AmpTeam} class JSON serializer
 * 
 * @author Nadejda Mandrescu
 */
public class AmpTeamSerializer extends AmpJsonSerializer<AmpTeam> {
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void serialize(AmpTeam ampTeam) throws IOException {
        writeField("id", ampTeam.getAmpTeamId());
        writeField("name", getTranslations("name"));
        writeField("description", getTranslations("description"));
        writeField("workspace-group", ampTeam.getWorkspaceGroup() == null ? null : ampTeam.getWorkspaceGroup().getValue());
        
        writeField("workspace-lead-id", ampTeam.getTeamLead() == null ? null : ampTeam.getTeamLead().getAmpTeamMemId());
        writeField("child-workspaces", ampTeam.getChildrenWorkspaces());
        writeField("add-activity", ampTeam.getAddActivity());
        writeField("is-computed", ampTeam.getComputation());
        writeField("hide-draft", ampTeam.getHideDraftActivities());
        writeField("is-cross-team-validation", ampTeam.getCrossteamvalidation());
        writeField("use-filter", ampTeam.getUseFilter());
        writeField("parent-workspace-id", ampTeam.getParentTeamId() == null ? null : ampTeam.getParentTeamId().getAmpTeamId());
        writeField("access-type", ampTeam.getAccessType());
        writeField("permission-strategy", ampTeam.getPermissionStrategy());
        writeField("fm-template-id", ampTeam.getFmTemplate() == null ? null : ampTeam.getFmTemplate().getId());
        writeField("workspace-prefix", ampTeam.getWorkspacePrefix() == null ? null : ampTeam.getWorkspacePrefix().getValue());
        
        if (ampTeam.getFilterDataSet() != null && !ampTeam.getFilterDataSet().isEmpty()) {
            Map<String, Object> filters = new TreeMap<String, Object>();
            for (AmpTeamFilterData filter : ampTeam.getFilterDataSet()) {
                if (!AmpARFilter.SETTINGS_PROPERTIES.contains(filter.getPropertyName()) 
                        && StringUtils.isNotBlank(filter.getValue())) {
                    filters.put(filter.getPropertyName(), getFilterValue(filter, filters.get(filter.getPropertyName())));
                }
            }
            writeField("workspace-filters", filters);
        }
    }
    
    private Object getFilterValue(AmpTeamFilterData filter, Object existing) throws IOException {
        if ("java.util.HashSet".equals(filter.getPropertyClassName())) {
            Set<Long> set = existing == null ? new TreeSet<>() : (Set) existing;
            set.add(Long.valueOf(filter.getValue()));
            return set;
        }
        try {
            Object value = mapper.readValue(filter.getValue(), Class.forName(filter.getPropertyClassName()));
            return value;
        } catch (ClassNotFoundException | IOException e) {
            throw new IOException(e);
        }
    }
}
