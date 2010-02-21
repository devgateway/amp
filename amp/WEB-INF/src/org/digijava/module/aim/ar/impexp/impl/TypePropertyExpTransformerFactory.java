package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ExpTransformer;
import org.digijava.module.aim.ar.impexp.ExpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.PropertiesType.Property;

public class TypePropertyExpTransformerFactory implements ExpTransformerFactory<Long, Property>{
private TypePropertyExpTransformer transformer	= null;
	
	@Override
	public ExpTransformer<Long, Property> generateExpTransformer() {
		if ( this.transformer == null ) {
			this.transformer		= new TypePropertyExpTransformer();
		}
		return this.transformer;
	}

}
