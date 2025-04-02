/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author aartimon@developmentgateway.org
 * @since 09 Jan 2013
 */
public class DateToYearModel implements IModel<String> {
    private static final long serialVersionUID = 1L;
    private IModel<Date> sourceModel;

    public DateToYearModel(IModel<Date> sourceModel) {
        this.sourceModel = sourceModel;
    }
    
    @Override
    public void setObject(String y) {
        int year = Integer.parseInt(y);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        Date newDate = c.getTime();
        sourceModel.setObject(newDate);
    }
    
    @Override
    public String getObject() {
        Date date = sourceModel.getObject();
        if (date == null)
            return null;
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



