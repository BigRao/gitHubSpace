package com.inspur.bjzx.scenesecurityserve.Interface;

public interface PublicInterfaceService extends javax.xml.rpc.Service {
    String getInterfaceAddress();

    PublicInterface getInterface() throws javax.xml.rpc.ServiceException;

    PublicInterface getInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
