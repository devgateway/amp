/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity ;

/**
 * @author aartimon@dginternational.org
 * @since Apr 27, 2011
 */
public class AmpActivity extends AmpActivityVersion implements Cloneable {

	/**
	 * 
	 * NOTE:
	 *    You shouldn't have the need to add new fields here.
	 *    This class should be identical with AmpActivityVersion
	 * 
	 */
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			return (AmpActivity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
}

