package org.digijava.module.aim.ar.impexp.impl;

import java.math.BigInteger;

import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.aim.ar.impexp.ExpTransformer;
import org.digijava.module.aim.ar.impexp.data.ColumnLikeType;

public class ColumnLikeExpTransformer implements ExpTransformer<Object, ColumnLikeType> {

	@Override
	public ColumnLikeType transform(Object e, String propertyName) {
		try{
			ColumnLikeType columnLikeType	= new ColumnLikeType();
			columnLikeType.setName( 
					ReportWizardAction.invokeGetterForBeanPropertyWithAnnotation(e, ColumnLike.class, new Object[0]).toString() );
			columnLikeType.setIndex( 
					new BigInteger( ReportWizardAction.invokeGetterForBeanPropertyWithAnnotation(e, Order.class, new Object[0]).toString() ) 
			);
			return columnLikeType;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
