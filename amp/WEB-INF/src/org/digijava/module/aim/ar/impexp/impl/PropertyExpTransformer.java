package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ExpTransformer;
import org.digijava.module.aim.ar.impexp.data.PropertiesType.Property;

public class PropertyExpTransformer implements
		ExpTransformer<Object, Property> {

	@Override
	public Property transform(Object e, String propertyName) {
		Property p		= new Property();
		p.setName( propertyName );
		if ( e != null )
			p.setValue( e.toString() );
		else 
			p.setValue(null);
		return p;
	}

}
