package org.digijava.module.aim.ar.impexp.impl;

import java.util.Collection;

import org.digijava.module.aim.ar.impexp.AbstractExpTransformer;
import org.digijava.module.aim.ar.impexp.data.ColumnLikeType;
import org.digijava.module.aim.ar.impexp.data.ColumnsType;
import org.digijava.module.aim.ar.impexp.data.HierarchiesType;
import org.digijava.module.aim.ar.impexp.data.MeasuresType;
import org.digijava.module.aim.ar.impexp.data.PropertiesType;
import org.digijava.module.aim.ar.impexp.data.ReportsType;
import org.digijava.module.aim.ar.impexp.data.PropertiesType.Property;
import org.digijava.module.aim.dbentity.AmpReports;

public class AmpReportsExpTransformer extends AbstractExpTransformer<AmpReports, ReportsType>{

	private ReportsType reportsType	= null;
	
	@Override
	public void processJaxb(String fieldName, Object result) {
		if ( result instanceof Property ) {
			reportsType.getProperties().getProperty().add( (Property)result  );
		}
		
		if ( fieldName.equals("columns") ) {
			reportsType.getColumns().getColumn().addAll( (Collection<ColumnLikeType>)result );
		}
		if ( fieldName.equals("hierarchies") ) {
			reportsType.getHierarchies().getHierarchy().addAll( (Collection<ColumnLikeType>)result );
		}
		if ( fieldName.equals("measures") ) {
			reportsType.getMeasures().getMeasure().addAll( (Collection<ColumnLikeType>)result );
		}
		
	}

	@Override
	public ReportsType transform(AmpReports e, String propertyName) {
		reportsType	= new ReportsType();
		reportsType.setName( e.getName() );
		reportsType.setMode( (e.getDrilldownTab()!=null&&e.getDrilldownTab()==true)?"tab":"report" );
		if ( e.getDescription() !=null && e.getDescription().length() > 0 )
			reportsType.setDescription( e.getDescription() );
		reportsType.setProperties( new PropertiesType() );
		reportsType.setColumns( new ColumnsType() );
		reportsType.setHierarchies( new HierarchiesType() );
		reportsType.setMeasures( new MeasuresType() );
		
		try {
			this.iterateProperties(e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if ( reportsType.getHierarchies().getHierarchy().size() == 0 ) 
			reportsType.setHierarchies(null);
		
		return reportsType;
	}

}
