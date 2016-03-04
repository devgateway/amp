/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.FieldsDiscriminator;
import org.digijava.module.aim.dbentity.AmpFundingAmount;

/**
 * Project Cost Type discriminator
 * @author Nadejda Mandrescu
 */
public class CostTypeDiscriminator extends FieldsDiscriminator {
	private static final Map<String, String> options = new HashMap<String, String>() {{
		put(AmpFundingAmount.FundingType.PROPOSED.name(), AmpFundingAmount.FundingType.PROPOSED.name());
		put(AmpFundingAmount.FundingType.REVISED.name(), AmpFundingAmount.FundingType.REVISED.name());
	}};

	@Override
	public Map<String, ?> getPossibleValues() {
		return options;
	}

	@Override
	public Object toJsonOutput(Object value) {
		return ((AmpFundingAmount.FundingType) value).name();
	}

	@Override
	public Long getIdOf(Object value) {
		return Long.decode(value.toString());
	}

	@Override
	public Object toAmpFormat(Object obj) {
		return obj;
	}

}
