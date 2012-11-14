package org.dgfoundation.amp.onepager.components.fields;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * Regular date picker that uses the formatting in AMP, but is not part of FM
 * 
 * @author aartimon@developmentgateway.org
 * @since Nov 13, 2012
 * 
 */
public class AmpDatePickerRegular extends DateTextField {
	private static final long serialVersionUID = 1L;

	public static AmpDatePickerRegular newDatePicker(String id, IModel<Date> model){
		String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
		pattern = pattern.replace('m', 'M');
		return new AmpDatePickerRegular(id, model, pattern);
	}
	
	private AmpDatePickerRegular(String id, IModel<Date> model, String pattern) {
		super(id, model, pattern);
		this.setOutputMarkupId(true);
		DatePicker dp = new DatePicker() {
			private static final long serialVersionUID = 1L;
	
			
			@Override
			protected boolean enableMonthYearSelection() {
				return true;
			}
		};
		dp.setShowOnFieldClick(true);
		dp.setAutoHide(true);
		this.add(dp);
	}
}
