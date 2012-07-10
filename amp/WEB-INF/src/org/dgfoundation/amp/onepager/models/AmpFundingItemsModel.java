package org.dgfoundation.amp.onepager.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class AmpFundingItemsModel implements IModel<Set<AmpFunding>> {
	private static final long serialVersionUID = 1L;
	private IModel<Set<AmpFunding>> model;
	private AmpOrganisation org;

	public AmpFundingItemsModel(IModel<Set<AmpFunding>> model, AmpOrganisation org) {
		this.model = model;
		this.org = org;
	}
	
	
	@Override
	public void detach() {
		model.detach();
	}

	@Override
	public Set<AmpFunding> getObject() {
		HashSet<AmpFunding> fundsSet = new HashSet<AmpFunding>();
		Set<AmpFunding> fSet = model.getObject();
		Iterator<AmpFunding> fSetIt = fSet.iterator();
		while (fSetIt.hasNext()) {
			AmpFunding funding = (AmpFunding) fSetIt.next();
			if (funding.getAmpDonorOrgId().getAmpOrgId().compareTo(org.getAmpOrgId()) == 0)
				fundsSet.add(funding);
		}

		return fundsSet;
	}

	@Override
	public void setObject(Set<AmpFunding> fi) {
		Set<AmpFunding> fSet = model.getObject();

		Iterator<AmpFunding> fSetIt = fSet.iterator();
		while (fSetIt.hasNext()) {
			AmpFunding funding = (AmpFunding) fSetIt.next();
			if (funding.getAmpDonorOrgId().getAmpOrgId().compareTo(org.getAmpOrgId()) == 0)
				fSetIt.remove();
		}
		fSet.addAll(fi);
		model.setObject(fSet);
	}
	
}