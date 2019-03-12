package com.messages;

import java.io.Serializable;


public class User implements Serializable {
    String picture;
    Status status;
    String name;
    String ipAddress;
    String portListen;
    
    
    //IP Address
    public String getIp() {
        return ipAddress;
    }
    public void setIp(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    //port to Listen
    public String getPort() {
        return portListen;
    }
    public void setPort(String port) {
        this.portListen = port;
    }   
    
    //name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Picture
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    //Status
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
