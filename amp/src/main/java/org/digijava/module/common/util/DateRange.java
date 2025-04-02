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

package org.digijava.module.common.util;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author irakli
 *
 */
public class DateRange implements Serializable {
    
    private Date  startDate = null;
    private Date  endDate = null;
    
    public DateRange ( Date startDate, Date endDate ) {
            
        try {
            
            checkLegalRange( startDate, endDate);
            
            this.startDate =  startDate;
            this.endDate = endDate;
            
        } catch ( IllegalArgumentException ex ) {
            throw new IllegalArgumentException ( ex.getMessage() + "\n" 
                                                    + ex.getStackTrace() );
        }
        
            
    }
    
    /**
     * 
     * @author irakli
     *
     * Checks if endDate >= startDate. Created to avoid code duplication in
     * places where this check is needed.
     * 
     */
    private void checkLegalRange ( Date startDate, Date endDate ) {
        
        boolean problem = false;
        if  ( startDate == null || endDate == null ) {
            problem = true;
        } else {
            if ( startDate.after( endDate ) ) {
                problem = true;
            }
        }
        
        if ( problem ) {
            throw new IllegalArgumentException(" Start date may not exceed the " +
            "end date and neither of them may be null");
        }
    }
    
    
    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }
        
    public void setEndDate(Date endDate) {
        try {
            checkLegalRange(this.startDate, endDate);
            
            this.endDate = endDate;
        } catch ( IllegalArgumentException ex ) {
            throw new IllegalArgumentException ( ex.getMessage() + "\n" 
                                                    + ex.getStackTrace() );
        }
    }
    
    public void setStartDate(Date startDate) {
        try {
            checkLegalRange( startDate, this.endDate);
            
            this.startDate = startDate;
            
        } catch ( IllegalArgumentException ex ) {
            throw new IllegalArgumentException ( ex.getMessage() + "\n" 
                                                    + ex.getStackTrace() );
        }
    }
    
    public boolean equals ( Object o ) {
        
        if ( ! (o instanceof DateRange) ) {
            return false;
        } 
        
        DateRange dr = (DateRange) o;
        
        if ( dr.getStartDate().equals( this.startDate )  && 
             dr.getEndDate().equals ( this.endDate ) ) {
            
            return true;
        }
        
        return false;
    }
    
    public int hashCode() {
       
        int hashCode = new HashCodeBuilder()
                .append( startDate.toString() )
                .append( endDate.toString() )
                .toHashCode();
        
        return hashCode;
    }
    
    public String toString() {
        return "Start Date: " + startDate + " " +
                   "  End Date: " + endDate; 
    }
    
}
