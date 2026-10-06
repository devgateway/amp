package org.digijava.module.currencyrates.NET.webserviceX.www;

public class CurrencyConvertorSoapProxy implements org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap {
  private String _endpoint = null;
  private org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap currencyConvertorSoap = null;
  
  public CurrencyConvertorSoapProxy() {
    _initCurrencyConvertorSoapProxy();
  }
  
  public CurrencyConvertorSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initCurrencyConvertorSoapProxy();
  }
  
  private void _initCurrencyConvertorSoapProxy() {
    try {
      currencyConvertorSoap = (new org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorLocator()).getCurrencyConvertorSoap();
      if (currencyConvertorSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)currencyConvertorSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)currencyConvertorSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (currencyConvertorSoap != null)
      ((javax.xml.rpc.Stub)currencyConvertorSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.digijava.module.currencyrates.NET.webserviceX.www.CurrencyConvertorSoap getCurrencyConvertorSoap() {
    if (currencyConvertorSoap == null)
      _initCurrencyConvertorSoapProxy();
    return currencyConvertorSoap;
  }
  
  public double conversionRate(org.digijava.module.currencyrates.NET.webserviceX.www.Currency fromCurrency, org.digijava.module.currencyrates.NET.webserviceX.www.Currency toCurrency) throws java.rmi.RemoteException{
    if (currencyConvertorSoap == null)
      _initCurrencyConvertorSoapProxy();
    return currencyConvertorSoap.conversionRate(fromCurrency, toCurrency);
  }
  
  
}
