package org.digijava.module.aim.ar.impexp.impl;

import java.util.Collection;
import java.util.HashMap;

import org.digijava.module.aim.ar.impexp.data.PropertiesType.Property;

public class TypePropertyImpTransformer extends PropertyImpTransformer {
	protected HashMap<String, String> map;
	protected String defaultString;
	
	public TypePropertyImpTransformer( Collection<Property> properties, String propertyName, HashMap<String, String> map, String defaultString ) {
		super(properties, propertyName);
		this.map				= map;
		this.defaultString	= defaultString;
	}
	
	@Override
	public String transform() {
		String result	= super.transform();
		if ( result != null ) {
			String translatedResult	= this.map.get(result);
			if ( translatedResult != null )
				return translatedResult;
			else 
				return this.defaultString;
		}
		return null;
	}
}
