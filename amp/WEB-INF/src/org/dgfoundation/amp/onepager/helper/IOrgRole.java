/**
 * 
 */
package org.dgfoundation.amp.onepager.helper;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * @author Nadejda Mandrescu
 *
 */
public interface IOrgRole extends Serializable {
	public AmpOrganisation getOrganisation();
	public AmpRole getRole();
}
