package org.digijava.module.aim.ar.impexp.impl;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.module.aim.ar.impexp.AbstractImpTransformer;
import org.digijava.module.aim.ar.impexp.data.ReportsType;
import org.digijava.module.aim.dbentity.AmpReports;

public class AmpReportsImpTransformer extends AbstractImpTransformer<AmpReports, ReportsType>{

	private static Logger logger 		= Logger.getLogger(AmpReportsImpTransformer.class);
	
	private AmpReports ampReport;
	
	private ReportsType root;
	
	private String propertyName;
	
	public AmpReportsImpTransformer(ReportsType r, String propertyName) {
		this.ampReport				= new AmpReports();
		this.root						= r;
		this.propertyName			= propertyName;
	}
	
	@Override
	protected Object processResponse(Object response, String beanPropertyName,
			Class beanPropertyClass) {
		if ( response instanceof Collection ) 
			return response;
		try {
			if ( !response.getClass().equals(beanPropertyClass) ) {
				Class [] contructorParams	= new Class[1];
				contructorParams[0]			= response.getClass();
				Constructor constr				= beanPropertyClass.getConstructor(contructorParams);
				Object [] params				= new Object[1];
				params[0]							= response;
				return constr.newInstance(params);
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return response;
	}

	@Override
	public AmpReports transform() {
		this.ampReport.setName( root.getName() );
		this.ampReport.setDescription( root.getDescription() );
		this.ampReport.setUpdatedDate( new Date(System.currentTimeMillis()) );
		this.ampReport.setDrilldownTab( "tab".equals( root.getMode() )?true:false );
		this.ampReport.setPublicReport(false);
		
		try {
			this.iterateProperties(ampReport, root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ampReport;
	}

}
