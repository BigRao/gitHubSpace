package com.inspur.plugins.kaoshi.Interface;

public class PublicInterfaceProxy implements PublicInterface {
  private String _endpoint = null;
  private PublicInterface publicInterface = null;
  
  public PublicInterfaceProxy() {
    _initPublicInterfaceProxy();
  }
  
  public PublicInterfaceProxy(String endpoint) {
    _endpoint = endpoint;
    _initPublicInterfaceProxy();
  }
  
  private void _initPublicInterfaceProxy() {
    try {
      publicInterface = (new PublicInterfaceServiceLocator()).getInterface();
      if (publicInterface != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)publicInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)publicInterface)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (publicInterface != null)
      ((javax.xml.rpc.Stub)publicInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public PublicInterface getPublicInterface() {
    if (publicInterface == null)
      _initPublicInterfaceProxy();
    return publicInterface;
  }
  
  public String sendSMS(String xml) throws java.rmi.RemoteException{
    if (publicInterface == null)
      _initPublicInterfaceProxy();
    return publicInterface.sendSMS(xml);
  }
  
  
}