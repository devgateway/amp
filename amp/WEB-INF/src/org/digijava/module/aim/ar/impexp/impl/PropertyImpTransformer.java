package org.digijava.module.aim.ar.impexp.impl;

import java.util.Collection;

import org.digijava.module.aim.ar.impexp.ImpTransformer;
import org.digijava.module.aim.ar.impexp.data.PropertiesType.Property;

public class PropertyImpTransformer implements ImpTransformer<String>{
	
	protected Collection<Property> properties;
	protected String propertyName;

	protected PropertyImpTransformer( Collection<Property> properties, String propertyName ) {
		this.properties			= properties;
		this.propertyName		= propertyName;
	}
	
	@Override
	public String transform() {
		try {
			for (Property property: properties ) {
				if ( propertyName.equals( property.getName() ) ) {
					return property.getValue();
				}
			}
		}
		catch (NullPointerException e) {
			return null;
		}
		return null;
	}

}
