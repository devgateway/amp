package org.dgfoundation.amp.onepager.components;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
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
		AmpDataFreezeSettings settings;
		// we first check if the feature is enabled
		if (TLSUtils.getThreadLocalInstance().request.getAttribute("AmpDataFreezeSettings") == null) {
			List<AmpDataFreezeSettings> ampDataFreezeSettingsList = (List<AmpDataFreezeSettings>) DbUtil
					.getAll(AmpDataFreezeSettings.class);

			if (!ampDataFreezeSettingsList.isEmpty()) {
				settings = ampDataFreezeSettingsList.get(0);
			} else {
				settings = new AmpDataFreezeSettings();
			}
		} else {
			settings = (AmpDataFreezeSettings) TLSUtils.getThreadLocalInstance().request
					.getAttribute("AmpDataFreezeSettings");
		}
		TLSUtils.getThreadLocalInstance().request.setAttribute("AmpDataFreezeSettings",settings); 
		if (settings.getEnabled()) {
			Boolean enabled = true;
			AmpFiscalCalendar fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(
					FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR));

			Quarter currentQuarter = new Quarter(fiscalCalendar, new Date());
			Quarter previousQuarter = currentQuarter.getPreviousQuarter();
			FundingInformationItem fundingDetailItem = (FundingInformationItem) item.getModel().getObject();
			DateTime itemUpdateDate = new DateTime(fundingDetailItem.getUpdatedDate());

			DateTime today = new DateTime();
			
			if (currentQuarter.getQuarterNumber() > 1) {
				// we only check after 1 quarter since in first quarter we don't
				// need to check
				DateTime previousQuarterStart = new DateTime(previousQuarter.getQuarterStartDate());
				DateTime previousQuarterEnd = new DateTime(previousQuarter.getQuarterEndDate());
				if (itemUpdateDate.isAfter(previousQuarterStart) && itemUpdateDate.isBefore(previousQuarterEnd)) {
					// date is within the last quarter
					if (today.isAfter(previousQuarterEnd.plusDays(settings.getGracePeriod()))) {
						// only if the grace period has been reached we block
						// the
						// edit of the item
						enabled = false;
					}
					if (currentQuarter.getQuarterNumber() > 2) {
						// we need to see if the item is between first quarter
						// start and second to last quarter end
						DateTime firstDayOfFy = new DateTime(currentQuarter.getFirstQuarter().getQuarterStartDate());
						DateTime secondToLastQuarterEndDate = new DateTime(
								currentQuarter.getFirstQuarter().getQuarterStartDate());

						// if the activity was edited between the first day of
						// the fy and secondToLastQuarterEndDate and we are
						// still in the same fy we block the edit
						if (itemUpdateDate.isAfter(firstDayOfFy)
								&& itemUpdateDate.isBefore(secondToLastQuarterEndDate)) {
							enabled = false;
						}
					}
				}
			} else {
				// if we are in quarter one we need to check if the activity was
				// edited during the last fy and block it until the grace period
				// has been reached

				DateTime firstDayOfLastFy = new DateTime(previousQuarter.getFirstQuarter().getQuarterStartDate());
				DateTime lastDayOfLastFy = new DateTime(previousQuarter.getQuarterEndDate());
				if (itemUpdateDate.isAfter(firstDayOfLastFy) && itemUpdateDate.isBefore(lastDayOfLastFy)) {
					enabled = false;
				}
			}
			fundingDetailItem.setCheckSum(ActivityUtil.calculateFundingDetailCheckSum(fundingDetailItem));

			item.setEnabled(enabled);
		}

	}
}
