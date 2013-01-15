package org.dgfoundation.amp.onepager.models;
import java.util.Calendar;
import java.util.Date;
import org.apache.wicket.model.IModel;

/**
 *
 * @author aartimon@developmentgateway.org
 */
public class MTEFYearsModel implements IModel<String> {
	private static final long serialVersionUID = 1L;
	private IModel<Date> sourceModel;
	
	public MTEFYearsModel(IModel<Date> sourceModel) {
		this.sourceModel = sourceModel;
	}
	
	@Override
	public void setObject(String object) {
		//value is not modifiable 
	}
	
	@Override
	public String getObject() {
		Date date = sourceModel.getObject();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);	
		return Integer.toString(year)+"/"+Integer.toString(year+1);
	}

	@Override
	public void detach() {
		sourceModel.detach();
	}
}



