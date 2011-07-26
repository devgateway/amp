/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * Model that wraps a {@link AmpFunding#getFundingDetails()} but returns a
 * {@link Set<AmpFundingDetail>} filter by the given {@link #transactionType}
 * 
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpTransactionTypeDonorFundingDetailModel implements
		IModel<Set<org.digijava.module.aim.dbentity.AmpFundingDetail>> {

	private static final long serialVersionUID = -544903343417479057L;
	private IModel<Set<AmpFundingDetail>> model;
	private int transactionType;

	public AmpTransactionTypeDonorFundingDetailModel(IModel<Set<AmpFundingDetail>> model, int transactionType) {
		this.transactionType = transactionType;
		this.model=model;
	}

	@Override
	public void detach() {
		model.detach();
	}

	@Override
	public Set<AmpFundingDetail> getObject() {
		Set<AmpFundingDetail> s = new HashSet<AmpFundingDetail>();
		if (model.getObject() != null){
			for (AmpFundingDetail ampFundingDetail : model.getObject())
				if (ampFundingDetail.getTransactionType().equals(transactionType))
					s.add(ampFundingDetail);
		}
		return s;
	}

	@Override
	public void setObject(Set<AmpFundingDetail> object) {
		for (AmpFundingDetail ampFundingDetail : model.getObject())
			if (ampFundingDetail.getTransactionType().equals(transactionType))
				model.getObject().remove(ampFundingDetail);
		model.getObject().addAll(object);
	}

}
