package org.dgfoundation.amp.onepager.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ActivityFYModel extends Model {

	private IModel sourceModel;
	private List<String> ddvalues;
	ArrayList<String> fyYears=new ArrayList<String>();
	
	public ActivityFYModel(IModel sourceModel) {
		this.sourceModel = sourceModel;
	}
	
	@Override
	public void setObject(Serializable object) {
		if (object == null){
			sourceModel.setObject(new ArrayList<String>());
		}
		else{
			List<String> selected=(List<String>)object;
			StringBuilder years=new StringBuilder();
			for(String year:selected){
				years.append(year);
				years.append(",");
			}
			years.deleteCharAt(years.length()-1);
			String fy=years.toString();
			sourceModel.setObject(fy);
		}
			
	}
	
	@Override
	public Serializable getObject() {
		String fy = (String) sourceModel.getObject();
		fyYears.clear();
		if(fy!=null&&fy.length()>0){
			String[] years=fy.split(",");
			fyYears.addAll(Arrays.asList(years));
		}
		return fyYears;
	}

}



