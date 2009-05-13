package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ExpTransformer;
import org.digijava.module.aim.ar.impexp.ExpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.PropertiesType.Property;

public class PropertyExpTransformerFactory implements
		ExpTransformerFactory<Object, Property> {

	private PropertyExpTransformer transformer	= null;
	
	@Override
	public ExpTransformer<Object, Property> generateExpTransformer() {
		if ( this.transformer == null ) {
			this.transformer		= new PropertyExpTransformer();
		}
		return this.transformer;
	}

}
