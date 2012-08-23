package org.dgfoundation.amp.onepager.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class AmpFundingGroupModel implements IModel<Set<AmpOrganisation>> {
	private static final long serialVersionUID = 1L;
	private IModel<Set<AmpFunding>> model;

	public AmpFundingGroupModel(IModel<Set<AmpFunding>> model) {
		this.model = model;
	}
	
	@Override
	public void detach() {
		model.detach();
	}

	@Override
	public Set<AmpOrganisation> getObject() {
		HashSet<AmpOrganisation> orgsSet = new HashSet<AmpOrganisation>();
		Set<AmpFunding> fSet = model.getObject();
		if(fSet == null)
			return orgsSet;
		Iterator<AmpFunding> fSetIt = fSet.iterator();		
		while (fSetIt.hasNext()) {
			AmpFunding funding = (AmpFunding) fSetIt.next();
			orgsSet.add(funding.getAmpDonorOrgId());
		}
		return orgsSet;
	}

	@Override
	public void setObject(Set<AmpOrganisation> object) {
		//do nothing
	}
	
}