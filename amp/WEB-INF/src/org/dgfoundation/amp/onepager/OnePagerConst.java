/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.helper.Constants;

/**
 * One Pager Constants
 * @author mpostelnicu@dgateway.org
 * since Nov 11, 2010
 */
public final class OnePagerConst {
	public final static MetaInfo<Integer>[] adjustmentTypes=new MetaInfo[] { new MetaInfo<Integer>("Actual",Constants.ACTUAL), 
		new MetaInfo<Integer>("Planned" ,Constants.PLANNED),  new MetaInfo<Integer>("Pipeline",Constants.ADJUSTMENT_TYPE_PIPELINE )};
	public final static MetaInfo<Integer>[] adjustmentTypesShort=new MetaInfo[] { new MetaInfo<Integer>("Actual",Constants.ACTUAL), 
		new MetaInfo<Integer>("Planned" ,Constants.PLANNED)};
	
	//TODO: please load this as a JS resource, DO NOT PUT JS scripts in java unless they only invoke a function
	public final static String slideToggle = "$('a.slider').click(function(){$(this).siblings('div:first').slideToggle();return false;});";
}
