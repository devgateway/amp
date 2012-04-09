/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;

/**
 * Model that wraps a {@link Set<AmpRegionalFunding>} and returns a {@link Set
 * <AmpRegionalFunding>} filter by the given {@link #transactionType}
 * 
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpTrTypeLocationRegionalFundingDetailModel implements IModel<Set<AmpRegionalFunding>> {

	private static final long serialVersionUID = -544903343417479057L;
	private IModel<Set<AmpRegionalFunding>> model;
	private int transactionType;
	private IModel<AmpCategoryValueLocations> cvLocation;

	public AmpTrTypeLocationRegionalFundingDetailModel(
			IModel<Set<AmpRegionalFunding>> model, int transactionType,
			IModel<AmpCategoryValueLocations> cvLocationModel) {
		this.transactionType = transactionType;
		this.model = model;
		this.cvLocation = cvLocationModel;
	}

	@Override
	public Set<AmpRegionalFunding> getObject() {
		Set<AmpRegionalFunding> s = new HashSet<AmpRegionalFunding>();
		for (AmpRegionalFunding ampRegionalFunding : model.getObject())
			if (ampRegionalFunding.getTransactionType().equals(transactionType)
					&& ampRegionalFunding.getRegionLocation()
							.equals(cvLocation.getObject()))
				s.add(ampRegionalFunding);
		return s;
	}

	@Override
	public void detach() {
	}

	@Override
	public void setObject(Set<AmpRegionalFunding> object) {
		Set<AmpRegionalFunding> s = model.getObject();
		Iterator<AmpRegionalFunding> it = s.iterator();
		while (it.hasNext()) {
			AmpRegionalFunding rf = (AmpRegionalFunding) it
					.next();
			if (rf.getTransactionType().equals(transactionType)
					&& rf.getRegionLocation()
							.equals(cvLocation.getObject()))
				it.remove();
		}
		s.addAll(object);
		model.setObject(s);
	}
}
