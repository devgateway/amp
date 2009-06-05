

package org.digijava.module.widget.helper;

import java.math.BigDecimal;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class TopDonorGroupHelper {
    private String name;
    private BigDecimal value;

    public TopDonorGroupHelper(String name, BigDecimal value) {
        this.name = name;
       if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))){
			value=value.divide(BigDecimal.valueOf(1000),2,BigDecimal.ROUND_FLOOR);
        }
		else{
           value=value.divide(BigDecimal.valueOf(1000000),2,BigDecimal.ROUND_FLOOR);
        }
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }



}
