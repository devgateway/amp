

package org.digijava.module.widget.helper;

import java.math.BigDecimal;

public class TopDonorGroupHelper {
    private String name;
    private BigDecimal value;

    public TopDonorGroupHelper(String name, BigDecimal value) {
        this.name = name;
        value = value.divide(BigDecimal.valueOf(1000000), 2, BigDecimal.ROUND_FLOOR);
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
