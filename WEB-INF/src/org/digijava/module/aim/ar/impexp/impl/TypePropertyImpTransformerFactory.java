/**
 * 
 */
package org.digijava.module.aim.ar.impexp.impl;

import java.util.HashMap;

import org.dgfoundation.amp.ar.ArConstants;
import org.digijava.module.aim.ar.impexp.ImpTransformer;
import org.digijava.module.aim.ar.impexp.ImpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.ReportsType;

/**
 * @author Alex Gartner
 *
 */
public class TypePropertyImpTransformerFactory implements
		ImpTransformerFactory<String, ReportsType> {

	TypePropertyImpTransformer transformer	= null;
	@Override
	public ImpTransformer<String> generateImpTransformer(ReportsType rt, String propertyName) {
		HashMap<String,String> map					= new HashMap<String, String>();
		map.put("donor", ArConstants.DONOR_TYPE + "");
		map.put("regional", ArConstants.REGIONAL_TYPE + "");
		map.put("component", ArConstants.COMPONENT_TYPE + "");
		map.put("contribution", ArConstants.CONTRIBUTION_TYPE + "");
		if ( transformer == null ) {
			transformer = new TypePropertyImpTransformer( rt.getProperties().getProperty(), propertyName, map, "donor" );
		}
		return transformer;
	}

}
