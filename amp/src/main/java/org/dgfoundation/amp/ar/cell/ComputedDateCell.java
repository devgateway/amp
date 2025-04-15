package org.dgfoundation.amp.ar.cell;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.workers.ComputedDateColWorker;
import org.digijava.module.aim.helper.FormatHelper;

public class ComputedDateCell extends TextCell {

    private static Logger log = Logger.getLogger(ComputedDateCell.class);
    
    
    public Class getWorker() {
        return ComputedDateColWorker.class;
    }

    @Override
    public String toString() {
        String result =super.toString();
        if ("".equalsIgnoreCase(result) || result==null){
            result="0";
        }
        Double value=Double.parseDouble(result);
        if (value > 0d) {
            return FormatHelper.formatNumber(value);
        }else{
            return "";
        }
    }

    @Override
    public Object getValue() {

        return super.getValue();
    }

    public ComputedDateCell(Long id) {
        super(id);

    }

    public ComputedDateCell() {
        super();
    }
    
    @Override
    public Comparable comparableToken() {
        Double retValue = 0d;
        try {
            if (this.getValue() != null){
                String value = (String) this.getValue();
                if (!value.equalsIgnoreCase("")) {
                    retValue = Double.parseDouble(value);
                }       
            }
        } catch (NumberFormatException ex){
            log.debug(ex.getMessage(), ex);
        }
        return retValue;
    }   
}
