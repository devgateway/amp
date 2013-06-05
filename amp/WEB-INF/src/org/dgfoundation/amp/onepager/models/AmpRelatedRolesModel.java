/**
 * 
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * @author mihai
 *read the list of roles from Related Organizations page, and create a unique Set with the roles chosen
 */
public class AmpRelatedRolesModel extends AbstractReadOnlyModel<List<AmpRole>> {
	
	private IModel<AmpActivityVersion> am;

	public AmpRelatedRolesModel(IModel<AmpActivityVersion> am) {
		this.am=am;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
		public List<AmpRole> getObject() {
			Set<AmpRole> set=new TreeSet<AmpRole>();
			Set<AmpOrgRole> orgroles=am.getObject().getOrgrole();
			if(orgroles==null || orgroles.size()==0) new ArrayList<AmpRole>(set);
			for (AmpOrgRole orgrole  : orgroles) set.add(orgrole.getRole());
			return new ArrayList<AmpRole>(set);
		}
}
