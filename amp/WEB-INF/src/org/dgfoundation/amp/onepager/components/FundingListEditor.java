package org.dgfoundation.amp.onepager.components;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.joda.time.DateTime;

/**
 * Class that extends ListEditor to be able to block edit in a funding item
 * 
 * @author JulianEduardo
 *
 * @param <T>
 */
public class FundingListEditor<T> extends ListEditor<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FundingListEditor(String id, IModel<Set<T>> model) {
		super(id, model);
	}

	public FundingListEditor(String id, IModel<Set<T>> model, Comparator<T> comparator) {
		super(id, model, comparator);
	}

	@Override
	protected void onPopulateItem(ListItem<T> item) {
		Boolean enabled = item.isEnabled();

		FundingInformationItem fundingDetailItem = (FundingInformationItem) item.getModel().getObject();

		if (org.apache.wicket.Session.get().getMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION) != null
				&& org.apache.wicket.Session.get().getMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION)
						.get(fundingDetailItem.getTransactionDate()) != null) {
			enabled = org.apache.wicket.Session.get().getMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION)
					.get(fundingDetailItem.getTransactionDate())  && enabled;
		}
		item.setEnabled(enabled);
		fundingDetailItem.setCheckSum(ActivityUtil.calculateFundingDetailCheckSum(fundingDetailItem));
	}

}
