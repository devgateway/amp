package org.dgfoundation.amp.reports.saiku.export;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.amp.AmpFiltersConverter;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema.NamedElemType;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.digijava.kernel.ampapi.endpoints.filters.FiltersConstants;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** 
 * Class used for generating the summary sheet in excel. See {@link SaikuReportXlsxExporter}
 * It take the entity ids from the filters and get the name associated with this entities.
 * 
 * @author Viorel Chihai
 *
 */
public class ExportFilterUtils {
    protected static Logger logger = Logger.getLogger(ExportFilterUtils.class);
    
    
    /**
     * @param filters
     * @return Map<String, List<String>> containing the filter names and values (no ids)
     */
    public static Map<String, List<String>> getFilterValuesForIds(ReportFilters filters) {
        Map<String, List<String>> extractedFilters = new HashMap<String, List<String>>();
        if (filters != null) {
            Map<ReportElement, FilterRule> filterRules = filters.getAllFilterRules();
            
            for (Map.Entry<ReportElement, FilterRule> filter : filterRules.entrySet()) {
                if (filter.getValue() != null) {
                    switch(filter.getKey().type) {
                        case ENTITY: {
                            ReportColumn col = (ReportColumn) filter.getKey().entity;
                            String columnName = col.getColumnName();
                            
                            if (ColumnConstants.DONOR_ID.equals(columnName))
                                columnName = ColumnConstants.DONOR_AGENCY;
                            
                            columnName = AmpFiltersConverter.removeIdSuffixIfNeeded(AmpReportsSchema.getInstance(), columnName);
                            
                            extractedFilters.put(TranslatorWorker.translateText(columnName), getEntityValuesNames(filter, columnName));
                        } 
                        break;
                        case DATE: {
                            extractedFilters.put(TranslatorWorker.translateText("Date"), getDateValues(filter.getValue()));
                        }
                        break;
                        default: {
                            
                        }
                    }
                }
            }
        }
        
        return extractedFilters;
    }
    
    private static List<String> getEntityValuesNames(Entry<ReportElement, FilterRule> filter, String columnName) {
        NamedElemType elemType = AmpReportsSchema.getInstance().getNamedElemType(columnName);
        
        if (elemType != NamedElemType.UNKNOWN) {
            if (elemType == NamedElemType.DATE) {
                return getDateValues(filter.getValue());
            } else {
                Set<Long> allIds = filter.getValue().addIds(null).stream().collect(Collectors.toSet());
                return new ArrayList<String>(fetchEntities(elemType, columnName, allIds).values());
            }
        } 
        
        if (ColumnConstants.APPROVAL_STATUS.equals(columnName)) {
            List<String> vals = filter.getValue().values.stream().collect(Collectors.toList());
            vals.replaceAll(status -> {
                if (Long.toString(ColumnReportData.UNALLOCATED_ID).equals(status)) {
                    return TranslatorWorker.translateText(FiltersConstants.UNDEFINED_NAME);
                } else {
                    return TranslatorWorker.translateText(FilterUtils.getApprovalStatusByNumber(new Integer(status)));
                }
            });

            return vals;
        }

        if (ColumnConstants.TEAM.equals(columnName)) {
            Set<Long> allIds = filter.getValue().addIds(null).stream().collect(Collectors.toSet());
            Map<Long, String> entities = new HashMap<Long, String>();
            TeamUtil.getAllTeams().stream().filter(team -> allIds.contains(team.getAmpTeamId())).forEach(team -> {
                entities.put(team.getAmpTeamId(), team.getName());
            });
            return new ArrayList<String>(entities.values());
        } 
        
        if (AmpReportsSchema.getInstance().isBooleanColumn(columnName)) {
            return getBooleanValues(filter.getValue());
        }
        
        if (filter.getValue().filterType == FilterType.SINGLE_VALUE) {
            return Stream.of(filter.getValue().value).collect(Collectors.toList());
        }
        
        return filter.getValue().values.stream().collect(Collectors.toList());
    }

    /**
     * Get the values having the entity ids (sectors, programs, category values, etc.)
     * @param elemType
     * @param elemName
     * @param allIds
     * @return
     */
    private static Map<Long, String> fetchEntities(NamedElemType elemType, String elemName, Set<Long> allIds) {
        Map<Long, String> entities = new HashMap<Long, String>();
        if (allIds.contains(ColumnReportData.UNALLOCATED_ID)) {
            entities.put(ColumnReportData.UNALLOCATED_ID,
                    TranslatorWorker.translateText(FiltersConstants.UNDEFINED_NAME));
        }

        switch(elemType) {
            case SECTOR: 
                SectorUtil.getAllSectors().stream().filter(sector -> allIds.contains(sector.getAmpSectorId())).forEach(sector -> {
                    entities.put(sector.getAmpSectorId(), sector.getName());
                });
                break;
            case PROGRAM: 
                ProgramUtil.getAllPrograms().stream().filter(program -> allIds.contains(program.getAmpThemeId())).forEach(program -> {
                    entities.put(program.getAmpThemeId(), program.getName());
                });
                break;
            case LOCATION: 
                LocationUtil.getAllCountriesAndRegions().stream().filter(location -> allIds.contains(location.getId())).forEach(location -> {
                    entities.put(location.getId(), location.getName());
                });
                
                break;
            case ORGANISATION: 
                OrganisationUtil.getAllOrganisations().stream().filter(org -> allIds.contains(org.getAmpOrgId())).forEach(org -> {
                    entities.put(org.getAmpOrgId(), org.getName());
                });
                break;
            case ORG_GROUP: 
                OrganisationUtil.getAllOrgGroups().stream().filter(orgGroup -> allIds.contains(orgGroup.getAmpOrgGrpId())).forEach(orgGroup -> {
                    entities.put(orgGroup.getAmpOrgGrpId(), orgGroup.getName());
                });
                break;
            case ORG_TYPE: 
                OrganisationUtil.getAllOrgTypes().stream().filter(orgType -> allIds.contains(orgType.getAmpOrgTypeId())).forEach(orgType -> {
                    entities.put(orgType.getAmpOrgTypeId(), orgType.getName());
                });
                break;
            case ACV: 
                CategoryManagerUtil.getAllCategoryValues().stream().filter(cv -> allIds.contains(cv.getId())).forEach(cv -> {
                    entities.put(cv.getId(), TranslatorWorker.translateText(cv.getValue()));
                });
            default:
            break;
        }
        
        return entities;
    }
    
    private static List<String> getDateValues(FilterRule rule) {
        List<String> values = new ArrayList<String>();

        values.add(DateTimeUtil.formatDate(DateTimeUtil.fromJulianNumberToDate(rule.min)));
        values.add(DateTimeUtil.formatDate(DateTimeUtil.fromJulianNumberToDate(rule.max)));

        return values;
    }
    
    private static List<String> getBooleanValues(FilterRule rule) {
        List<String> values = new ArrayList<String>();

        if (rule.values != null) {
            rule.values.forEach(value -> {
                if (FilterRule.TRUE_VALUE.equals(value)) {
                    values.add(TranslatorWorker.translateText("Yes"));
                } else if (FilterRule.FALSE_VALUE.equals(value)) {
                    values.add(TranslatorWorker.translateText("No"));
                } else if (Long.toString(ColumnReportData.UNALLOCATED_ID).equals(value)) {
                    values.add(TranslatorWorker.translateText(FiltersConstants.UNDEFINED_NAME));
                }
            });
        }

        return values;
    }
}
