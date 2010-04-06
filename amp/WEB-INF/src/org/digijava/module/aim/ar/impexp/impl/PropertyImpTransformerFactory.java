/**
 * 
 */
package org.digijava.module.aim.ar.impexp.impl;

import org.digijava.module.aim.ar.impexp.ImpTransformer;
import org.digijava.module.aim.ar.impexp.ImpTransformerFactory;
import org.digijava.module.aim.ar.impexp.data.ReportsType;

/**
 * @author Alex Gartner
 *
 */
public class PropertyImpTransformerFactory implements
		ImpTransformerFactory<String, ReportsType> {

	@Override
	public ImpTransformer<String> generateImpTransformer(ReportsType rt, String propertyName) {
			return  new PropertyImpTransformer( rt.getProperties().getProperty(), propertyName );
	}

}
