package org.digijava.module.aim.ar.impexp.impl;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.ar.impexp.ExpTransformer;
import org.digijava.module.aim.ar.impexp.data.PropertiesType.Property;

public class TypePropertyExpTransformer implements ExpTransformer<Long, Property> {

	@Override
	public Property transform(Long e, String propertyName) {
		Property p		= new Property();
		String value	= null;
		switch ( e.intValue() ) {
			case ArConstants.DONOR_TYPE:
					value		= "donor"; break;
			case ArConstants.REGIONAL_TYPE:
				value		= "regional"; break;
			case ArConstants.COMPONENT_TYPE:
				value		= "component"; break;
			case ArConstants.CONTRIBUTION_TYPE:
				value		= "contribution"; break;
			default: 
				value		= "donor";
		
		}
		p.setName(propertyName);
		p.setValue(value);
		return p;
	}


}
