

package org.digijava.module.aim.helper;

import java.util.Calendar;
import java.util.Comparator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

/**
 *
 * @author medea
 */
public class IndicatorValuesComparator implements Comparator<AmpIndicatorValue> {

    public int compare(AmpIndicatorValue o1, AmpIndicatorValue o2) {
        int compare;
        Calendar date = Calendar.getInstance();
        /*if(o1.getValueDate()==null||o2.getValueDate()==null){
            if(o1.getValueType() == o2.getValueType()){
               compare=0;
            }
            else{
                if(o1.getValueType() > o2.getValueType()){
                    compare=1;
                }
                else{
                    compare=-1;
                }
            }
           return compare;
            
        }*/
        date.setTime(o1.getValueDate());
        int o1year = date.get(Calendar.YEAR);
        date.setTime(o2.getValueDate());
        int o2year = date.get(Calendar.YEAR);
        if (o1year == o2year && o1.getValueType() == o2.getValueType()) {
            compare = 0;
        } else {
            if (o1year > o2year || (o1year == o2year && o1.getValueType() > o2.getValueType())) {
                compare = 1;
            } else {
                compare = -1;
            }

        }
        return compare;
    }

  
}
