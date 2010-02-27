/**
 * 
 */
package org.digijava.module.aim.ar.impexp.impl;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.aim.ar.impexp.ImpTransformer;
import org.digijava.module.aim.ar.impexp.data.ColumnLikeType;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;


/**
 * @author Alex Gartner
 *
 */
public class ColumnLikeImpTransformer implements ImpTransformer<Set<Object>> {

	private List<ColumnLikeType> list;
	private Constructor constructor;
	private Collection<? extends Object> columns;
	private AmpCategoryValue cvLevel;
	
	protected ColumnLikeImpTransformer(List<ColumnLikeType> list, Class columnLikeClass, Collection<? extends Object> columns, AmpCategoryValue cvLevel ) 
											throws NoSuchMethodException {
		this.list							= list;
		this.constructor				= columnLikeClass.getConstructor(new Class [0]);
		this.columns					= columns;
		this.cvLevel					= cvLevel;
	}
	
	@Override
	public Set<Object> transform() {
		Set<Object> returnSet			= new HashSet<Object>();
			if ( list != null && list.size() > 0) {
				for ( ColumnLikeType clt: list ) {
					try {
						Object colLike		= this.constructor.newInstance(new Object[0]);
						if ( clt.getName() != null ) {
							if ( columns != null ) {
								for (Object col: columns) {
									if ( clt.getName().equals(col.toString()) ) {
										Object [] params		= new Object[1];
										params[0]					= col;
										ReportWizardAction.invokeSetterForBeanPropertyWithAnnotation(colLike, ColumnLike.class, params);
										params[0]					= clt.getIndex().longValue();
										ReportWizardAction.invokeSetterForBeanPropertyWithAnnotation(colLike, Order.class, params);
										params[0]					= cvLevel;
										ReportWizardAction.invokeSetterForBeanPropertyWithAnnotation(colLike, Level.class, params);
										returnSet.add(colLike);
										break;
									}
								}
							}
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		return returnSet;
	}
}
