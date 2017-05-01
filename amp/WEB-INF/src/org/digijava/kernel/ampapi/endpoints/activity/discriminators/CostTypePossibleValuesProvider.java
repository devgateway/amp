package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import static org.digijava.module.aim.dbentity.AmpFundingAmount.FundingType.PROPOSED;
import static org.digijava.module.aim.dbentity.AmpFundingAmount.FundingType.REVISED;

import java.util.List;
import java.util.Map;

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

	@Override
	public List<PossibleValue> getPossibleValues() {
		Map<String, String> ppcTranslations = TranslationUtil.translateLabel(PROPOSED.title + " Project Cost");
		Map<String, String> rpcTranslations = TranslationUtil.translateLabel(REVISED.title + " Project Cost");
		return new ImmutableList.Builder<PossibleValue>()
				.add(new PossibleValue(PROPOSED.name(), PROPOSED.name(), ppcTranslations))
				.add(new PossibleValue(REVISED.name(), REVISED.name(), rpcTranslations))
				.build();
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
