package org.digijava.kernel.ampapi.endpoints.serializers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.models.properties.UntypedProperty;
import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.dbentity.AmpTeamFilterData;
import org.dgfoundation.amp.reports.converters.AmpARFilterConverter;
import org.digijava.kernel.ampapi.swagger.converters.ModelDescriber;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.Identifiable;


/**
 * {@link AmpTeam} class JSON serializer
 * 
 * @author Nadejda Mandrescu
 */
public class AmpTeamSerializer extends AmpJsonSerializer<AmpTeam> implements ModelDescriber {
    private ObjectMapper mapper = new ObjectMapper();
    private final ThreadLocal<SimpleDateFormat> sdfIn = new ThreadLocal<>();
    private final ThreadLocal<SimpleDateFormat> sdfApiOut = new ThreadLocal<>();
    
    private void init() {
        if (sdfIn.get() == null) {
            sdfIn.set(new SimpleDateFormat(AmpARFilter.SDF_IN_FORMAT_STRING));
        }
        if (sdfApiOut.get() == null) {
            sdfApiOut.set(new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT));
        }
    }
    
    @Override
    protected void serialize(AmpTeam ampTeam) throws IOException {
        init();
        writeField("id", ampTeam.getAmpTeamId());
        writeField("name", getTranslations("name"));
        writeField("description", getTranslations("description"));
        writeField("workspace-group", ampTeam.getWorkspaceGroup() == null ? null : ampTeam.getWorkspaceGroup().getValue());
        
        writeField("workspace-lead-id", ampTeam.getTeamLead() == null ? null : ampTeam.getTeamLead().getAmpTeamMemId());
        writeField("add-activity", ampTeam.getAddActivity());
        writeField("is-computed", ampTeam.getComputation());
        writeField("hide-draft", ampTeam.getHideDraftActivities());
        writeField("is-cross-team-validation", ampTeam.getCrossteamvalidation());
        writeField("use-filter", ampTeam.getUseFilter());
        writeField("parent-workspace-id", ampTeam.getParentTeamId() == null ? null : ampTeam.getParentTeamId().getAmpTeamId());
        writeField("access-type", ampTeam.getAccessType());
        writeField("is-private", ampTeam.getIsolated());
        writeField("permission-strategy", ampTeam.getPermissionStrategy());
        writeField("fm-template-id", ampTeam.getFmTemplate() == null ? null : ampTeam.getFmTemplate().getId());
        writeField("workspace-prefix", ampTeam.getWorkspacePrefix() == null ? null : ampTeam.getWorkspacePrefix().getValue());
        
        if (Boolean.TRUE.equals(ampTeam.getComputation()) && ampTeam.getOrganizations() != null 
                && !ampTeam.getOrganizations().isEmpty()) {
            List<Long> computedOrgs = ((Collection<AmpOrganisation>) ampTeam.getOrganizations()).stream()
                    .map(org -> org.getAmpOrgId()).collect(Collectors.toList());
            writeField("organizations", computedOrgs);
        }
        
        if (ampTeam.getFilterDataSet() != null && !ampTeam.getFilterDataSet().isEmpty()) {
            AmpARFilter arFilter = FilterUtil.buildFilterFromSource(ampTeam);
            Map<String, Object> filters = new TreeMap<String, Object>();
            for (AmpTeamFilterData filter : ampTeam.getFilterDataSet()) {
                if (!AmpARFilter.SETTINGS_PROPERTIES.contains(filter.getPropertyName())
                        && StringUtils.isNotBlank(filter.getValue())) {
                    Object filterValue = getFilterValue(filter, filters.get(filter.getPropertyName()), arFilter);
                    filters.put(filter.getPropertyName(), filterValue);
                }
            }
            writeField("workspace-filters", filters);
            AmpARFilterConverter arFilterTranslator = new AmpARFilterConverter(arFilter);
            writeField("workspace-filters-widget-format", arFilterTranslator.buildFilters());
        }
    }
    
    private Object getFilterValue(AmpTeamFilterData filter, Object existing, AmpARFilter arFilter) throws IOException {
        try {
            Class<?> clazz = Class.forName(filter.getPropertyClassName());            
            if (Collection.class.isAssignableFrom(clazz)) {
                if (AmpARFilter.UNDEFINED_OPTIONS.equals(filter.getPropertyName())) {                    
                    Class<? extends Collection<String>> collectionClass = (Class<Collection<String>>) clazz;
                    Collection<String> set = existing == null ? collectionClass.newInstance() : (Collection) existing;
                    set.add(filter.getValue());
                    return set;                     
                } else {
                    Class<? extends Collection<Long>> collectionClass = (Class<Collection<Long>>) clazz;
                    Collection<Long> set = existing == null ? collectionClass.newInstance() : (Collection) existing;
                    set.add(Long.valueOf(filter.getValue()));
                    return set; 
                }              
            }
            if (AmpARFilter.DATE_PROPERTIES.contains(filter.getPropertyName())) {
                // no dynamic dates filter conversion, just passing that config further as it is 
                Field dateField = AmpARFilter.class.getDeclaredField(filter.getPropertyName());
                dateField.setAccessible(true);
                return sdfApiOut.get().format(sdfIn.get().parse((String) dateField.get(arFilter)));
            }
            if (String.class.equals(clazz)) {
                return filter.getValue();
            }
            if (Identifiable.class.isAssignableFrom(clazz)) {
                return Long.parseLong(filter.getValue());
            }
            return mapper.readValue(filter.getValue(), clazz);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public Model describe() {
        ModelImpl model = new ModelImpl();
        model.name("AmpTeam");

        model.addProperty("id", new LongProperty());
        model.addProperty("name", new UntypedProperty());
        model.addProperty("description", new UntypedProperty());
        model.addProperty("workspace-group", new StringProperty());
        model.addProperty("workspace-lead-id", new LongProperty());
        model.addProperty("add-activity", new BooleanProperty());
        model.addProperty("is-computed", new BooleanProperty());
        model.addProperty("hide-draft", new BooleanProperty());
        model.addProperty("is-cross-team-validation", new BooleanProperty());
        model.addProperty("use-filter", new BooleanProperty());
        model.addProperty("parent-workspace-id", new LongProperty());
        model.addProperty("access-type", new StringProperty());
        model.addProperty("is-private", new BooleanProperty());
        model.addProperty("permission-strategy", new StringProperty());
        model.addProperty("fm-template-id", new LongProperty());
        model.addProperty("workspace-prefix", new StringProperty());
        model.addProperty("organizations", new ArrayProperty(new LongProperty()));
        model.addProperty("workspace-filters", new MapProperty());
        model.addProperty("workspace-filters-widget-format", new MapProperty());

        return model;
    }
}
