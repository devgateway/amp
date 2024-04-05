package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

import java.util.Date;

public class DateRangeLogicalToken extends LogicalToken {

    private Date d1 = null;
    private Date d2 = null;
    private String type;

    public DateRangeLogicalToken(Date d1, Date d2, String type) {
        this.d1 = d1;
        this.d2 = d2;
        this.type = type;
    }

    @Override
    public boolean evaluate(CategAmountCell c) {
        MetaInfo m = c.getMetaData().getMetaInfo(ArConstants.TRANSACTION_DATE);
        if ( m != null) {
            Date date = (Date) m.getValue();
            if ((d1 != null) && (d2 != null)) {
                ret = (date.compareTo(d1) > 0 && date.compareTo(d2) < 0);
            }
            if ((d1 == null) && (d2 != null)) {
                ret = date.compareTo(d2) < 0;
            }
    
            if ((d1 != null) && (d2 == null)) {
                ret = date.compareTo(d1) > -1;
            }
        }
        else 
            ret     = false;
        return super.evaluate(c);
    }
}
