package org.digijava.kernel.ampapi.endpoints.interchange;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.dbentity.AmpActivityFields;

public class InterchangeUtils {

	public static final Logger LOGGER = Logger.getLogger(InterchangeUtils.class);

	static final Map<String, String> COLUMN_MAPPING = new HashMap<String, String>() {
		{
			put(ColumnConstants.ACTIVITY_ID, "amp_activity_id");
			put(ColumnConstants.PROJECT_TITLE, "title");
			put(ColumnConstants.ACTIVITY_CREATED_ON, "created_date");
			put(ColumnConstants.AMP_ID, "amp_id");
			put(ColumnConstants.PROJECT_CODE, "project_code");

		}
	};

	@SuppressWarnings("serial")
	protected static final Map<Class<?>, String> classToCustomType = new HashMap<Class<?>, String>() {
		{
			put(java.lang.String.class, "string");
			put(java.util.Date.class, "date");
			put(java.lang.Double.class, "float");

		}
	};

	public static String getCustomFieldType(Field field) {
		if (classToCustomType.containsKey(field.getClass())) {
			return classToCustomType.get(field.getClass());
		}
		return "bred";

	}

	public static List<JsonBean> getActivityList() {
		String name = "ActivityList";
		ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);
		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
		spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_CREATED_ON));
		spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_CODE));

		// spec.addColumn(new ReportColumn(ColumnConstants.TEAM));
		// spec.addColumn(new ReportColumn(ColumnConstants.TEAM_ID));

		spec.addMeasure(new ReportMeasure(MeasureConstants.ALWAYS_PRESENT));

		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class,
				ReportEnvironment.buildFor(TLSUtils.getRequest()), false);
		GeneratedReport report = null;
		List<JsonBean> activityList = new ArrayList<JsonBean>();
		try {
			report = generator.executeReport(spec);
			List<ReportArea> ll = report.reportContents.getChildren();
			for (ReportArea reportArea : ll) {
				JsonBean activity = new JsonBean();
				Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
				Set<ReportOutputColumn> col = row.keySet();
				for (ReportOutputColumn reportOutputColumn : col) {
					String columnName = COLUMN_MAPPING.get(reportOutputColumn.originalColumnName);
					if (columnName != null) {
						activity.set(columnName, row.get(reportOutputColumn).value);
					}
				}
				activityList.add(activity);

			}

		} catch (AMPException e) {
			LOGGER.error("Cannot execute report", e);
		}
		return activityList;
	}

	public static JsonBean getAllAvailableFields() {
		JsonBean result = new JsonBean();

		Set<String> visibleColumnNames = ColumnsVisibility.getVisibleColumns();
		Field[] fields = AmpActivityFields.class.getDeclaredFields();

		List<Field> exportableFields = new ArrayList<Field>();
		for (Field field : fields) {
			if (field.getAnnotation(Interchangeable.class) != null) {
				exportableFields.add(field);
			}
		}

		for (Field field : exportableFields) {
			result.set(field.getName(), field.getType().toString());
		}

		// for (String col : visibleColumnNames) {
		// result.set(, value);
		// // fieldSet.contains(arg0)
		// // result.set(col, );
		// }
		return result;
	}

}
