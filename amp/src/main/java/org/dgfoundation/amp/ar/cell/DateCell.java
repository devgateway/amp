/**
 * DateCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Date;

import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.DateColWorker;
import org.digijava.module.aim.helper.EthDateWorker;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianBasedWorker;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 19, 2006
 * Cell holding a Date object
 *
 */
public class DateCell extends Cell {

    //this is not thread safe
//  private static SimpleDateFormat sdt=new SimpleDateFormat("dd/mm/yy");
    
    protected boolean ethiopianDate;
    
    protected Date value;
    
    public String getEthDate()
    {
        EthiopianCalendar ec1=EthiopianCalendar.getEthiopianDate(value);
        return  ec1.ethDay+"/"+ec1.ethMonth+"/"+ec1.ethYear ;
    }
    
    
    /**
     * 
     */
    public DateCell() {
        super();
        ethiopianDate=false;
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param name
     */
    public DateCell(Long id) {
        super(id);
        ethiopianDate=false;
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Cell#getExtractor()
     */
    public Class getWorker() {
        return DateColWorker.class;
    }

    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Cell#getValue()
     */
    public Object getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Cell#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        this.value=(Date) value;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Viewable#getViewArray()
     */
    protected MetaInfo[] getViewArray() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.cell.Cell#add(org.dgfoundation.amp.ar.cell.Cell)
     */
    
    
    public Cell merge(Cell c) {
        throw new UnsupportedOperationException("DateCellS do not support merging");
    }

    public String toString() {
        if(ethiopianDate) return getEthDate();
        return value!=null?DateTimeUtil.formatDate(value):"";
    }

    public Cell newInstance() {
        return new DateCell();
    }

    /**
     * @return Returns the ethiopianDate.
     */
    public boolean isEthiopianDate() {
        return ethiopianDate;
    }

    /**
     * @param ethiopianDate The ethiopianDate to set.
     */
    public void setEthiopianDate(boolean ethiopianDate) {
        this.ethiopianDate = ethiopianDate;
    }


    public Comparable comparableToken() {
        return value;
    }


    @Override
    public void merge(Cell c, Cell dest) {
        throw new UnsupportedOperationException("DateCellS do not support merging");        
    }
    
}
