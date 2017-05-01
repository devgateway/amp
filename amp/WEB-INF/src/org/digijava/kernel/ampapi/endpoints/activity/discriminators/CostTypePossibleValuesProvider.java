package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import static org.digijava.module.aim.dbentity.AmpFundingAmount.FundingType.PROPOSED;
import static org.digijava.module.aim.dbentity.AmpFundingAmount.FundingType.REVISED;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.module.aim.dbentity.AmpFundingAmount;

/**
 * Project Cost Type discriminator
 * @author Nadejda Mandrescu
 */
public class CostTypePossibleValuesProvider extends PossibleValuesProvider {

	private static final List<PossibleValue> OPTIONS = new ImmutableList.Builder<PossibleValue>()
			.add(new PossibleValue(PROPOSED.name(), PROPOSED.name(),
					TranslationUtil.translateLabel(PROPOSED.title + " Project Cost")))
			.add(new PossibleValue(REVISED.name(), REVISED.name(),
					TranslationUtil.translateLabel(REVISED.title + " Project Cost")))
			.build();

	@Override
	public List<PossibleValue> getPossibleValues() {
		return OPTIONS;
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
