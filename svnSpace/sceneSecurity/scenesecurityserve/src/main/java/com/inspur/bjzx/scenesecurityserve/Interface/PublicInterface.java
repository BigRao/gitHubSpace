package com.inspur.bjzx.scenesecurityserve.Interface;

public interface PublicInterface extends java.rmi.Remote {
    String sendSMS(String xml) throws java.rmi.RemoteException;
}
