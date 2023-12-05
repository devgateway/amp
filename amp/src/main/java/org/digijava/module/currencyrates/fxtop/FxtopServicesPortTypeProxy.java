package org.digijava.module.currencyrates.fxtop;

public class FxtopServicesPortTypeProxy implements org.digijava.module.currencyrates.fxtop.FxtopServicesPortType {
  private String _endpoint = null;
  private org.digijava.module.currencyrates.fxtop.FxtopServicesPortType fxtopServicesPortType = null;
  
  public FxtopServicesPortTypeProxy() {
    _initFxtopServicesPortTypeProxy();
  }
  
  public FxtopServicesPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initFxtopServicesPortTypeProxy();
  }
  
  private void _initFxtopServicesPortTypeProxy() {
    try {
      fxtopServicesPortType = (new org.digijava.module.currencyrates.fxtop.FxtopServicesLocator()).getFxtopServicesPort();
      if (fxtopServicesPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)fxtopServicesPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)fxtopServicesPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (fxtopServicesPortType != null)
      ((javax.xml.rpc.Stub)fxtopServicesPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.digijava.module.currencyrates.fxtop.FxtopServicesPortType getFxtopServicesPortType() {
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType;
  }
  
  public org.digijava.module.currencyrates.fxtop.ConvertResult convert(java.lang.String originalAmount, java.lang.String c1, java.lang.String c2, java.lang.String date, java.lang.String user, java.lang.String password) throws java.rmi.RemoteException{
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType.convert(originalAmount, c1, c2, date, user, password);
  }
  
  public org.digijava.module.currencyrates.fxtop.CurrencyDescription descCurrency(java.lang.String lang, java.lang.String isocode) throws java.rmi.RemoteException{
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType.descCurrency(lang, isocode);
  }
  
  public java.lang.String listCurrencies(java.lang.String user, java.lang.String password) throws java.rmi.RemoteException{
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType.listCurrencies(user, password);
  }
  
  
}
