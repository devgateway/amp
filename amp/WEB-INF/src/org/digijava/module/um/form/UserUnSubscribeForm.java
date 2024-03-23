/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.um.form;

//

import org.apache.struts.validator.ValidatorForm;

import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserUnSubscribeForm
     extends ValidatorForm {

     public static SimpleDateFormat fmt = new SimpleDateFormat(" d MMM yyyy");

     private  Collection months;
     private  String selectedMonth;
     private  String day;
     private  String year;

     private  boolean  active;

     private boolean onHold;
     private String formatedToDate;

     public void setMonths(Collection months) {
         this.months = months;
     }
     public Collection getMonths() {
         return months;
     }
     public void setSelectedMonth(String selectedMonth) {
         this.selectedMonth = selectedMonth;
     }
     public String getSelectedMonth() {
         return selectedMonth;
     }
     public void setDay(String day) {
         this.day = day;
     }
     public String getDay() {
         return day;
     }
     public void setYear(String year){
         this.year = year;
     }
     public String getYear() {
         return year;
     }
    public boolean isActive() {
        return active;
    }

    public String getFormatedToDate() {
        return formatedToDate;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFormatedToDate(String formatedToDate) {
        this.formatedToDate = formatedToDate;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

}
