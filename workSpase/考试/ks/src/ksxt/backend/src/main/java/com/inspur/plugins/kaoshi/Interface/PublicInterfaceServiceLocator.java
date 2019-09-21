/**
 * PublicInterfaceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.inspur.plugins.kaoshi.Interface;

public class PublicInterfaceServiceLocator extends org.apache.axis.client.Service implements PublicInterfaceService {

    public PublicInterfaceServiceLocator() {
    }


    public PublicInterfaceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PublicInterfaceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Interface
    private String Interface_address = "http://10.4.149.18:7300/SMS4A/Proxy_Services/SMS4A_Proxy";

    public String getInterfaceAddress() {
        return Interface_address;
    }

    // The WSDD service name defaults to the port name.
    private String InterfaceWSDDServiceName = "Interface";

    public String getInterfaceWSDDServiceName() {
        return InterfaceWSDDServiceName;
    }

    public void setInterfaceWSDDServiceName(String name) {
        InterfaceWSDDServiceName = name;
    }

    public PublicInterface getInterface() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Interface_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInterface(endpoint);
    }

    public PublicInterface getInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            InterfaceSoapBindingStub _stub = new InterfaceSoapBindingStub(portAddress, this);
            _stub.setPortName(getInterfaceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setInterfaceEndpointAddress(String address) {
        Interface_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PublicInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                InterfaceSoapBindingStub _stub = new InterfaceSoapBindingStub(new java.net.URL(Interface_address), this);
                _stub.setPortName(getInterfaceWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("Interface".equals(inputPortName)) {
            return getInterface();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://10.224.240.73:7010/SmsWebRoot/services/Interface", "PublicInterfaceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://10.224.240.73:7010/SmsWebRoot/services/Interface", "Interface"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("Interface".equals(portName)) {
            setInterfaceEndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
