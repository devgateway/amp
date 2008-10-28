

package org.digijava.module.aim.helper;

/**
 *
 * @author medea
 */
public class PossibleValue {
   private String value;
   
   /* 
    *  shows if category value is country, 
    *  region or none of them
    */
   private long fieldType; 
   private boolean disable;

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public long getFieldType() {
        return fieldType;
    }

    public void setFieldType(long fieldType) {
        this.fieldType = fieldType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
   

}
