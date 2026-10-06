package org.digijava.module.currencyrates;
import org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorLocator;
/**
 * 
 * @author Marcelo Sotero
 * 
 */
public class MyCurrencyConvertorLocator extends CurrencyConvertorLocator {
    private java.lang.String CurrencyConvertorSoap_address = "http://www.webservicex.net/CurrencyConvertor.asmx";
    public org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap getCurrencyConvertorSoap(int minutes) throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
         try {
             endpoint = new java.net.URL(CurrencyConvertorSoap_address);
             
         }
         catch (java.net.MalformedURLException e) {
             throw new javax.xml.rpc.ServiceException(e);
         }
         return getCurrencyConvertorSoap(endpoint, minutes);
     }
    public org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap getCurrencyConvertorSoap(java.net.URL portAddress, int minutes) throws javax.xml.rpc.ServiceException {
        try {
            org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoapStub _stub = new org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoapStub(portAddress, this);
            _stub.setPortName(getCurrencyConvertorSoapWSDDServiceName());
            _stub.setTimeout(minutes * 1000 * 60);
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

}
