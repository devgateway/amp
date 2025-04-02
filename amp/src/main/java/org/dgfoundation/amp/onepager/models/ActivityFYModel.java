package org.dgfoundation.amp.onepager.models;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;

public class ActivityFYModel implements IModel<List<String>> {
    private static final long serialVersionUID = 1L;
    private IModel<String> sourceModel;
    private List<String> fyYears=new ArrayList<String>();
    
    public ActivityFYModel(IModel<String> sourceModel) {
        this.sourceModel = sourceModel;
    }
    
    @Override
    public void setObject(List<String> object) {
        if (object == null){
            sourceModel.setObject(new String());
        }
        else{
            List<String> selected=(List<String>)object;
            StringBuilder years=new StringBuilder();
            for(String year:selected){
                years.append(year);
                years.append(",");
            }
            int idx = years.length()-1;
            if (idx > 0)
                years.deleteCharAt(idx);
            String fy=years.toString();
            sourceModel.setObject(fy);
        }
            
    }
    
    @Override
    public List<String> getObject() {
        String fy = (String) sourceModel.getObject();
        fyYears.clear();
        if(fy!=null&&fy.length()>0){
            String[] years=fy.split(",");
            fyYears.addAll(Arrays.asList(years));
        }
        return fyYears;
    }

    @Override
    public void detach() {
        sourceModel.detach();
    }
}



