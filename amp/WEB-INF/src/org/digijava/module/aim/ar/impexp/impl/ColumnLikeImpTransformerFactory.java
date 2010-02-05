package org.digijava.module.aim.ar.impexp.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.ar.impexp.ImpTransformer;
import org.digijava.module.aim.ar.impexp.ImpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.ColumnLikeType;
import org.digijava.module.aim.ar.impexp.data.ReportsType;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class ColumnLikeImpTransformerFactory implements
		ImpTransformerFactory<Set<Object>, ReportsType> {

	@Override
	public ImpTransformer<Set<Object>> generateImpTransformer(ReportsType r,
			String propertyName) {
		Collection<AmpColumns> columns;
		Class columnLikeClass;
		List<ColumnLikeType> jaxbList;
		
		AmpCategoryValue level1		= CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);
		
		if (propertyName.equals("measures")) {
			columnLikeClass	= AmpReportMeasures.class;
			columns				= AdvancedReportUtil.getMeasureList();
			if ( r.getMeasures() != null ) {
				jaxbList				= r.getMeasures().getMeasure();
			}
			else 
				return null;
		}
		else if ( propertyName.equals("hierarchies") ) {
			columnLikeClass	= AmpReportHierarchy.class;
			columns				= AdvancedReportUtil.getColumnList();
			if ( r.getHierarchies() != null ) {
				jaxbList				= r.getHierarchies().getHierarchy();
			}
			else 
				return null;
		}
		else {
			columnLikeClass	= AmpReportColumn.class;
			columns				= AdvancedReportUtil.getColumnList();
			if ( r.getColumns() != null ) {
				jaxbList				= r.getColumns().getColumn();
			}
			else 
				return null;
		}
		
		
		try {
			return new ColumnLikeImpTransformer( jaxbList, columnLikeClass, columns, level1 );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

}
