/**
 * 
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.markup.html.border.Border;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * @author mihai
 *
 */
public class AmpFundingDetailComponent extends
		AmpComponentPanel<AmpFundingDetail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2622158760394707843L;

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpFundingDetailComponent(String id,
			String fmName) {
		super(id, fmName);
		Border boxBorder = new RoundedBox("roundedboxborder");
		add(boxBorder);
		boxBorder.add(new AmpFundingAmountComponent("amountComp", "amountComp"));
	}
	
}
