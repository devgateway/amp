/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.aim.util;

/**
 *
 * 
 */
public class Step {
   
   private String stepNumber; // Base Number of the Step (refer editActivityMenu.jsp)
   private int stepActualNumber; // value depends on previous steps visibility
   
    /**
     * @return Returns the stepActualNumber.
     */
    public int getStepActualNumber() {
        return stepActualNumber;
    }

    public void setStepActualNumber(int stepActualNumber) {
        this.stepActualNumber = stepActualNumber;
    }

   /**
     * @return Returns the stepNumber
     */
    public String getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(String stepNumber) {
        this.stepNumber = stepNumber;
    }
    
    @Override
    public String toString() {
        return stepNumber + ":" + stepActualNumber;
    }

   
}
