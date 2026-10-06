/**
 * FxtopServicesPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.digijava.module.currencyrates.fxtop;

public interface FxtopServicesPortType extends java.rmi.Remote {

    /**
     * Returns amount (OriginalAmount) converted from source currency
     * (C1 is a 3 letters Iso Code) to destination currency (C2 is a 3 letters
     * Iso Code) at a specific date (Date format DD/MM/YYYY) or today if
     * Date parameter is left blank.  If User and Password are not provided,
     * conversion will be performed with a date in 1999.
     */
    /**
     * Returns amount (OriginalAmount) converted from source currency
     (C1 is a 3 letters Iso Code) to destination currency (C2 is a 3 letters
     Iso Code) at a specific date (Date format DD/MM/YYYY) or today if
      Date parameter is left blank.  .
     * 
     * @param originalAmount
     * @param c1 source currency - 3 letters Iso Code
     * @param c2 destination currency - 3 letters Iso Code
     * @param date (Date format DD/MM/YYYY) or today if Date parameter is left blank
     * @param user user name that is suscribed to the service
     * @param password password of the subscription
     * @return
     * @throws java.rmi.RemoteException
     */
    public org.digijava.module.currencyrates.fxtop.ConvertResult convert(java.lang.String originalAmount, java.lang.String c1, java.lang.String c2, java.lang.String date, java.lang.String user, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Returns description of a currency (Isocode defined by a 3 letter)
     * in language Lang (supported languages : EN, FR, DE,ES, IT, PT, DK,
     * SE, FI, NO, NL)
     */
    public org.digijava.module.currencyrates.fxtop.CurrencyDescription descCurrency(java.lang.String lang, java.lang.String isocode) throws java.rmi.RemoteException;

    /**
     * Returns a string with list of ISO code of supported currencies
     * (3 letters Iso codes like USD) separated by a slash, User and Password
     * is optional and is not used yet
     */
    public java.lang.String listCurrencies(java.lang.String user, java.lang.String password) throws java.rmi.RemoteException;
}
