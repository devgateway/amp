package org.digijava.module.translation.importexport;

/**
 * Defines type of import.
 * Used fore each language importing separately.
 * TODO improve by adding conversion to JSP request strings. 
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public enum ImportType {
    UPDATE,         //update existing if its time stamp is older then one from import file
    OVERWRITE,      //overwrite existing records without checking time stamp.
    ONLY_NEW        //insert only if same record not exists in database.
}
