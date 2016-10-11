package org.dgfoundation.amp.reports.saiku.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.amp.AmpFiltersConverter;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema.NamedElemType;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;

/** 
 * Class used for generating the summary sheet in excel. See {@link SaikuReportXlsxExporter}
 * It take the entity ids from the filters and get the name associated with this entities.
 * @author Viorel Chihai
 *
 */
public class SaikuExportFilterUtils {
	protected static Logger logger = Logger.getLogger(SaikuExportFilterUtils.class);
	
	
	/**
	 * @param filters
	 * @return Map<String, List<String>> containing the filter names and values (no ids)
	 */
	public static Map<String, List<String>> getFilterValuesForIds(ReportFilters filters) {
		Map<String, List<String>> extractedFilters = new HashMap<String, List<String>>();
		if (filters != null) {
			Map<ReportElement, List<FilterRule>> filterRules = filters.getAllFilterRules();
			
			for (Map.Entry<ReportElement, List<FilterRule>> filter : filterRules.entrySet()) {
				if (filter.getValue() != null) {
					switch(filter.getKey().type) {
						case ENTITY: {
							ReportColumn col = (ReportColumn) filter.getKey().entity;
							String columnName = col.getColumnName();
							
							if (ColumnConstants.DONOR_ID.equals(columnName))
								columnName = ColumnConstants.DONOR_AGENCY;
							
							if (ColumnConstants.ARCHIVED.equals(columnName)) 
								break; //TODO: the filter is not yet implemented.
							
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
	
	private static List<String> getEntityValuesNames(Entry<ReportElement, List<FilterRule>> filter, String columnName) {
		NamedElemType elemType = AmpReportsSchema.getInstance().getNamedElemType(columnName);
		
		if (elemType != NamedElemType.UNKNOWN) {
			if (elemType == NamedElemType.DATE) {
				return getDateValues(filter.getValue());
			} else {
				Set<Long> allIds = filter.getValue().stream().flatMap(fr -> fr.addIds(null).stream()).collect(Collectors.toSet());
				return new ArrayList<String>(fetchEntities(elemType, columnName, allIds).values());
			}
		} 
		
		if (ColumnConstants.APPROVAL_STATUS.equals(columnName)) {
			List<String> vals = filter.getValue().stream().flatMap(fr -> fr.values.stream()).collect(Collectors.toList());
			vals.replaceAll(status -> TranslatorWorker.translateText(FilterUtils.getApprovalStatusByNumber(new Integer(status))));
			return vals;
		} 
		
		if (ColumnConstants.TEAM.equals(columnName)) {
			Set<Long> allIds = filter.getValue().stream().flatMap(fr -> fr.addIds(null).stream()).collect(Collectors.toSet());
			Map<Long, String> entities = new HashMap<Long, String>();
			TeamUtil.getAllTeams().stream().filter(team -> allIds.contains(team.getAmpTeamId())).forEach(team -> {
				entities.put(team.getAmpTeamId(), team.getName());
			});
			return new ArrayList<String>(entities.values());
		} 
		
		if (AmpReportsSchema.getInstance().isBooleanColumn(columnName)) {
			return getBooleanValues(filter.getValue());
		}
		
		return filter.getValue().stream().flatMap(fr -> fr.values.stream()).collect(Collectors.toList());
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
	
	private static List<String> getDateValues(List<FilterRule> rules) {
		List<String> values = new ArrayList<String>();
		
		for(FilterRule rule : rules) {
			values.add(DateTimeUtil.formatDate(DateTimeUtil.fromJulianNumberToDate(rule.min)));
			values.add(DateTimeUtil.formatDate(DateTimeUtil.fromJulianNumberToDate(rule.max)));
		}
		
		return values;
	}
	
	private static List<String> getBooleanValues(List<FilterRule> rules) {
		List<String> values = new ArrayList<String>();

		for(FilterRule rule : rules) {
			 if (rule.values != null) {
				if (rule.values.size() > 1) {
					values.add(TranslatorWorker.translateText("All") + ": " + TranslatorWorker.translateText("Yes") + "/" + TranslatorWorker.translateText("No"));
				} else {
					values.add(FilterRule.TRUE_VALUE.equals(rule.values.get(0)) ? TranslatorWorker.translateText("Yes") : TranslatorWorker.translateText("No"));
				}
			}
		}
		
		return values;
	}
}
