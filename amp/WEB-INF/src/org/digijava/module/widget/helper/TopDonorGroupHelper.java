

package org.digijava.module.widget.helper;

import java.math.BigDecimal;

public class TopDonorGroupHelper {
    private String name;
    private BigDecimal value;

    public TopDonorGroupHelper(String name, Double value) {
        this.name = name;
        BigDecimal val=new BigDecimal(value);
        val = val.divide(BigDecimal.valueOf(1000000), 2, BigDecimal.ROUND_FLOOR);
        this.value = val;
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
