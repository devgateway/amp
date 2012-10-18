package org.dgfoundation.amp.onepager.models;
import java.util.Calendar;
import java.util.Date;
import org.apache.wicket.model.IModel;

public class YearAsDateModel implements IModel<String> {
	private static final long serialVersionUID = 1L;
	private IModel<Date> sourceModel;
	
	public YearAsDateModel(IModel<Date> sourceModel) {
		this.sourceModel = sourceModel;
	}
	
	@Override
	public void setObject(String object) {
		if (object == null){
			sourceModel.setObject(new Date());
		}
		else{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, Integer.parseInt(object));
			c.set(Calendar.DAY_OF_YEAR,1);
			sourceModel.setObject(c.getTime());
		}
			
	}
	
	@Override
	public String getObject() {
		Date date = sourceModel.getObject();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);	
		return Integer.toString(year);
	}

	@Override
	public void detach() {
		sourceModel.detach();
	}
}



