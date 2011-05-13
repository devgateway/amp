/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.digijava.module.aim.util;

import java.util.Collection;

/**
 * Used to display tree like structures in selectors
 * @author mpostelnicu@dgateway.org
 * since Oct 22, 2010
 */
public interface AmpComboboxDisplayable {
	public AmpComboboxDisplayable getParent();
	public Collection getSiblings();
	public Collection getVisibleSiblings();
}
