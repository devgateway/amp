package org.dgfoundation.amp.reports.converters;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;

public class AmpReportFiltersConverter {

	protected static final Logger logger = Logger.getLogger(AmpReportFiltersConverter.class);
	private AmpReportFilters filters;
	private AmpARFilter ampARFilter;

	public AmpReportFiltersConverter(AmpReportFilters filters) {
		if (filters == null) {
		    filters = new AmpReportFilters();
		}
		this.filters = filters;
	}

	public AmpReportFiltersConverter(AmpReportFilters filters, AmpARFilter ampARFilter) {
		this.filters = filters;
		this.ampARFilter = ampARFilter;
	}

	public void setReportFilters(AmpReportFilters filters) {
		this.filters = filters;
	}

	public AmpARFilter buildFilters() {
		if (this.ampARFilter == null) {
			this.ampARFilter = new AmpARFilter();
		}

		// Donors section.
		addFilter(ColumnConstants.DONOR_ID, AmpOrganisation.class, "donnorgAgency", true);

		// Related organizations section.
		addFilter(ColumnConstants.BENEFICIARY_AGENCY, AmpOrganisation.class, "beneficiaryAgency", true);
		addFilter(ColumnConstants.EXECUTING_AGENCY, AmpOrganisation.class, "executingAgency", true);
		addFilter(ColumnConstants.CONTRACTING_AGENCY, AmpOrganisation.class, "contractingAgency", true);
		addFilter(ColumnConstants.IMPLEMENTING_AGENCY, AmpOrganisation.class, "implementingAgency", true);

		// Sector´s section.
		addFilter(ColumnConstants.PRIMARY_SECTOR, AmpSector.class, "selectedSectors", true);
		addFilter(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, AmpSector.class, "selectedSectors", false);
		addFilter(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, AmpSector.class, "selectedSectors", false);
		addFilter(ColumnConstants.SECONDARY_SECTOR_ID, AmpSector.class, "selectedSecondarySectors", true);
		addFilter(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, AmpSector.class, "selectedSecondarySectors", false);
		addFilter(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, AmpSector.class, "selectedSecondarySectors", false);
		addFilter(ColumnConstants.TERTIARY_SECTOR, AmpSector.class, "selectedTertiarySectors", true);
		addFilter(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, AmpSector.class, "selectedTertiarySectors", false);
		addFilter(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, AmpSector.class, "selectedTertiarySectors", false);

		// Programs and national objectives section.
		addFilter(ColumnConstants.PRIMARY_PROGRAM, AmpTheme.class, "selectedPrimaryPrograms", true);
		addFilter(ColumnConstants.SECONDARY_PROGRAM, AmpTheme.class, "selectedSecondaryPrograms", true);
		addFilter(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, AmpTheme.class, "selectedNatPlanObj", true);

		// Other fields.
		addFilter(ColumnConstants.FINANCING_INSTRUMENT, AmpCategoryValue.class, "financingInstruments", true);
		addFilter(ColumnConstants.TYPE_OF_ASSISTANCE, AmpCategoryValue.class, "typeOfAssistance", true);
		addFilter(ColumnConstants.ON_OFF_TREASURY_BUDGET, AmpCategoryValue.class, "budget", true);
		addFilter(ColumnConstants.WORKSPACES, AmpTeam.class, "workspaces", true);
		addFilter(ColumnConstants.STATUS, AmpCategoryValue.class, "statuses", true);
		addFilter(ColumnConstants.APPROVAL_STATUS, String.class, "approvalStatusSelected", true);
		
		this.ampARFilter.setComputedYear(this.filters.getComputedYear());
		
		// System.out.println(this.ampARFilter.toString());
		return this.ampARFilter;
	}

	/**
	 * Convert one of the Mondrian Filters to one of the fields in AmpARFilters and add it to the new filter.
	 * 
	 * @param mondrianFilterColumnName
	 *          is the constant name in the new filters.
	 * @param ampARFilterFieldClass
	 *          is the Class of the field in AmpARFilters.
	 * @param ampARFilterFieldName
	 *          is the field name in AmpARFilters.
	 * @param cleanup
	 *          if true then old values will be replaced.
	 */
	private void addFilter(String mondrianFilterColumnName, Class ampARFilterFieldClass, String ampARFilterFieldName, boolean cleanup) {
		try {
			Session session = PersistenceManager.getSession();
			// Use reflection to dynamically call the setter method on AmpARFilter class, that includes generating the setter
			// name and inspecting the type of data it uses (Set, Collection, Wrapper, etc).
			Method getterMethod = AmpARFilter.class.getDeclaredMethod(getGetterName(ampARFilterFieldName));
			Class paramClass = getterMethod.getReturnType();
			Class[] param = new Class[1];
			param[0] = paramClass;
			Method setterMethod = AmpARFilter.class.getDeclaredMethod(getSetterName(ampARFilterFieldName), param);

			// Get values from mondrian filters.
			List<FilterRule> filterRules = this.filters.getAllFilterRules().get(new ReportColumn(mondrianFilterColumnName));

			if (filterRules != null) {
				if (paramClass.getName().equals("java.util.Set") || paramClass.getName().equals("java.util.Collection")) {
					Set<Object> values = null;
					values = new HashSet();
					Iterator<FilterRule> iFilterRules = filterRules.iterator();
					while (iFilterRules.hasNext()) {
						FilterRule auxFilterRule = iFilterRules.next();
						if (auxFilterRule.values != null) {
							Iterator<String> iValues = auxFilterRule.values.iterator();
							while (iValues.hasNext()) {
								String auxValue = iValues.next();
								if (!ampARFilterFieldClass.toString().equals("class java.lang.String")) {
									Object auxEntity = session.load(ampARFilterFieldClass, new Long(auxValue));
									values.add(auxEntity);
								} else {
									values.add(auxValue);
								}
							}
						}
					}

					if (cleanup == false) {
						// Preserve old values.
						Set<Object> previousValues = (Set) getterMethod.invoke(this.ampARFilter);
						if (previousValues != null) {
							values.addAll(previousValues);
						}
					}
					// Use reflection to call the setter.
					setterMethod.invoke(this.ampARFilter, values);
					logger.info("Found filter: " + mondrianFilterColumnName + " with values: " + values.toString());
				} else if (paramClass.getName().equals("java.lang.String")) {
					setterMethod.invoke(this.ampARFilter, filterRules.toString());
					logger.info("Found filter: " + mondrianFilterColumnName + " with values: " + filterRules.toString());
				} else if (paramClass.getName().equals("java.lang.Integer")) {
					setterMethod.invoke(this.ampARFilter, Integer.valueOf(filterRules.toString()));
					logger.info("Found filter: " + mondrianFilterColumnName + " with values: " + filterRules.toString());
				} else if (paramClass.getName().equals("java.lang.Double")) {
					setterMethod.invoke(this.ampARFilter, Double.valueOf(filterRules.toString()));
					logger.info("Found filter: " + mondrianFilterColumnName + " with values: " + filterRules.toString());
				} else {
					throw new RuntimeException(paramClass.getName());
				}
			} else {
				// logger.info("Not found filter: " + mondrianFilterColumnName);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * Merge all fields that have not been populated in the build process.
	 * 
	 * @param oldFilters
	 */
	public void mergeWithOldFilters(AmpARFilter oldFilters) {
		Field[] declaredFields = AmpARFilter.class.getDeclaredFields();
		for (Field auxField : declaredFields) {
			try {
				// Obtain getter and setter for each field.
				Method getter = AmpARFilter.class.getDeclaredMethod(getGetterName(auxField.getName()), null);
				Class paramClass = getter.getReturnType();
				Class[] param = new Class[1];
				param[0] = paramClass;
				Method setter = AmpARFilter.class.getDeclaredMethod(getSetterName(auxField.getName()), param);
				Object oldFieldValue = getter.invoke(oldFilters, null);
				Object newFieldValue = getter.invoke(this.ampARFilter, null);
				if (oldFieldValue != null && newFieldValue == null) {
					logger.info(auxField.toGenericString() + ": " + oldFieldValue);

					if (oldFieldValue instanceof Collection) {
						if (((Collection) oldFieldValue).size() > 0) {
							setter.invoke(this.ampARFilter, oldFieldValue);
						}
					} else if (oldFieldValue instanceof Map) {
						if (((Map) oldFieldValue).size() > 0) {
							setter.invoke(this.ampARFilter, oldFieldValue);
						}
					} else {
						setter.invoke(this.ampARFilter, oldFieldValue);
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
	}

	private String getSetterName(String field) {
		String get = null;
		get = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
		return get;
	}

	private String getGetterName(String field) {
		String get = null;
		get = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
		return get;
	}

}