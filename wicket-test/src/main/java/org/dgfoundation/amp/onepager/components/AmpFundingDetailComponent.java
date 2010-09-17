/**
 * 
 */
package org.dgfoundation.amp.onepager.components;

import java.util.Date;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * @author mihai
 *
 */
public class AmpFundingDetailComponent extends
		AmpFormComponentPanel<AmpFundingDetail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2622158760394707843L;

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpFundingDetailComponent(String id, IModel<AmpFundingDetail> model,
			String fmName) {
		super(id, model, fmName);
		add(new AmpFundingAmountComponent("amountComp", new CompoundPropertyModel<AmpFundingDetail>(model), "amountComp"));
	}
	
}
