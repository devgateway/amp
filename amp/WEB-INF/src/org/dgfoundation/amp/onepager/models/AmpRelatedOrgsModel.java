/**
 * 
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * @author mihai
 * read the list of organizations from related organizations page, and create a unique set with the orgs chosen
 */
public class AmpRelatedOrgsModel extends AbstractReadOnlyModel<List<AmpOrganisation>> {
	
	private IModel<AmpActivityVersion> am;
	protected AbstractChoice<?, AmpRole> roleChoiceContainer;

	public AmpRelatedOrgsModel(IModel<AmpActivityVersion> am, AbstractChoice<?, AmpRole> roleChoiceContainer) {
		this.am=am;
		this.roleChoiceContainer=roleChoiceContainer;
	}
	
	
	@Override
	public List<AmpOrganisation> getObject() {
		Set<AmpOrganisation> set=new TreeSet<AmpOrganisation>();
		Set<AmpOrgRole> orgroles=am.getObject().getOrgrole();
		AmpRole role=(AmpRole) roleChoiceContainer.getModelObject();
		if(role==null) return new ArrayList<AmpOrganisation>(set);
		for (AmpOrgRole orgrole  : orgroles) 
			if(role.getAmpRoleId().equals(orgrole.getRole().getAmpRoleId())) set.add(orgrole.getOrganisation());
		return new ArrayList<AmpOrganisation>(set);
	}
}
