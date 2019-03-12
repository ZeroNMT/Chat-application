package com.messages;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Message implements Serializable {

    private String name;
    private MessageType type;
    private String msg;
    private int count;
    private ArrayList<User> list;
    private ArrayList<User> users;
    private Status status;
    private String picture = "default";
    String ipAddress;
    String portListen;

    //send file
    private byte [] byteArray;
    

    // byteArray
    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
    public byte[] getByteArray() {
        return byteArray;
    }
    
    
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
    
    // Picture message
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getPicture() {
        return picture;
    }

    // Name message
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Message
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    //Type message
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }

    //list
    public ArrayList<User> getUserlist() {
        return list;
    }
    public void setUserlist(HashMap<String, User> userList) {
        this.list = new ArrayList<>(userList.values());
    }

    // Count Online
    public void setOnlineCount(int count){
        this.count = count;
    }
    public int getOnlineCount(){
        return this.count;
    }

    //users
    public ArrayList<User> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    //Status
    public void setStatus(Status status) {
        this.status = status;
    }
    public Status getStatus() {
        return status;
    }
    
}
