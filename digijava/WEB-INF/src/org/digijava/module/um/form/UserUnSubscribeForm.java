/*
 *   UserUnSubscribeForm.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: UserUnSubscribeForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.um.form;

import java.util.Collection;

//
import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.ValidatorForm;

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

     private  Collection months;
     private  String selectedMonth;
     private  String day;
     private  String year;

     private  boolean  active;


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
    public void setActive(boolean active) {
        this.active = active;
    }

}