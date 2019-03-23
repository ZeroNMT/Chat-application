package com.client.chatwindow;

import com.client.login.LoginController;
import com.cryptography.Cryptography;
import com.cryptography.DSA;
import com.cryptography.RSA;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.messages.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import static jdk.nashorn.internal.objects.NativeString.substring;



public class Listener implements Runnable {

    private static final String HASCONNECTED = "has connected";

    private  String picture;
    private Socket socket;
    public String hostname;
    public int port;
    public  String username;
    public ChatController controller;
    public  String portListen;
    public  String ipAddress;
    public Cryptography crypt;
    
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    Logger logger = LoggerFactory.getLogger(Listener.class);

    public ServerSocket subserver; // Them boi Thoai ngay 13/10/18 9:17pm
    public HashMap<String, ObjectOutputStream> senders;// Them boi Thoai ngay 13/10/18 9:17pm
    public HashMap<String, ArrayList<Message>> listview;// Them boi Thoai ngay 13/10/18 9:17pm
 
    

    public Listener(String hostname, int port, String username, String picture,String portListen,String ipAddress, ChatController controller)  {
        try{
            this.hostname = hostname;
            this.port = port;
            this.username = username;
            this.picture = picture;
            this.controller = controller;
            this.portListen=portListen;
            this.ipAddress=ipAddress;
            Cryptography crypt = new Cryptography();
            ArrayList<byte[]>  listKey = new ArrayList<byte[]> ();
            KeyPair keyRSA = RSA.createKeyRSA(2048);
            KeyPair KeyDSA = DSA.createKeyDSA();
            listKey.add(0,keyRSA.getPublic().getEncoded());
            listKey.add(1,KeyDSA.getPublic().getEncoded());
            listKey.add(2,keyRSA.getPrivate().getEncoded());
            listKey.add(3,KeyDSA.getPrivate().getEncoded());
            crypt.setListKey(listKey);
        
            this.crypt = crypt;
        } catch (Exception ex) {
            ex.printStackTrace();
        }        

        
    }

    public void run() {
        try {
            subserver = new ServerSocket(Integer.parseInt(this.portListen));
            logger.info("Server "+ username+" (Port: "+portListen+" IpAddress: "+ipAddress + ") is running !!!");
            senders = new HashMap<>();
            listview = new HashMap<>();

            socket = new Socket(hostname, port);
            LoginController.getInstance().showScene();
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {
            LoginController.getInstance().showErrorDialog("Could not connect to server");
            logger.error("Could not Connect");
        }
        logger.info("IP " + hostname + ":" +port);
        logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            connect();//message first
            logger.info("Sockets in and out ready!");
            while (socket.isConnected()) {
                Message message = null;
                message = (Message) input.readObject();

                if (message != null) {
                    logger.debug("Message recieved:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
                    switch (message.getType()) {
                        case CONNECTED:
                            controller.setUserList(message);
                            break;
                        case DISCONNECTED:
                            controller.setUserList(message);
                            break;
                        case STATUS:
                            controller.setUserList(message);
                            break;
                        case INVITE:
                            acceptRequestConnect(message);
                            break;

                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            controller.logoutScene();
        }
    }





    /* This method is used for sending a normal Message
 * @param msg - The message which the user generates
 */
    public  void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(this.username);
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        createMessage.setPicture(this.picture);
        createMessage.setIp(this.ipAddress);
        createMessage.setPort(this.portListen);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* Message first*/
    public  void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(this.username);
        createMessage.setType(MessageType.CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setPicture(this.picture);
        createMessage.setIp(this.ipAddress);
        createMessage.setPort(this.portListen);
        ArrayList<byte[]> list = this.crypt.getListKey();
        
        Cryptography crypt = new Cryptography();
        ArrayList<byte[]>  listKey = new ArrayList<byte[]> ();
        listKey.add(0,list.get(0));
        listKey.add(1,list.get(1));
        crypt.setListKey(listKey);
        createMessage.setCrypt(crypt);
        oos.writeObject(createMessage);
    }
    // Send connect request
    
    public  void sendConnectFriend(String userNameFriend) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(this.username);
        createMessage.setType(MessageType.INVITE);
        createMessage.setMsg(userNameFriend);//user name muon ket noi
        createMessage.setPicture(this.picture);
        createMessage.setIp(this.ipAddress);
        createMessage.setPort(this.portListen);
        oos.writeObject(createMessage);
    }
    


    public boolean isConnected(String userName) {
        return senders.containsKey(userName);
    }
    
    public void createConnect(String name) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Socket sock = subserver.accept();
                        ObjectInputStream obj = new ObjectInputStream(sock.getInputStream());
                        Message message = (Message) obj.readObject();
                        
                        if (message.getType() == MessageType.INVITE
                                && message.getName().equals(name)) {
                            senders.put(name, new ObjectOutputStream(sock.getOutputStream()));
                            listview.put(name, new ArrayList<Message>());
                            handleConnection(sock, obj);
                            break;
                        }
                    }
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

        sendConnectFriend(name);
    }      
    
    

    public void sendTo(String msg , String name) {
        
        logger.info("sendTo: "+name+" message: "+msg);

        Message createMessage = new Message();
        createMessage.setName(this.username);
        createMessage.setType(MessageType.USER);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMsg(msg);
        createMessage.setPicture(this.picture);
        
        listview.get(name).add(createMessage);
        this.controller.cellRender.setTxt(name, createMessage.getMsg());
        if(name.equals(controller.userNow))
            controller.addToChat(createMessage);  
        
        ObjectOutputStream ossTo = senders.get(name);
        try {
            ossTo.writeObject(createMessage);
            ossTo.flush();
        } catch (IOException ex) {
            logger.error("Can not write to : " + name, ex);
        }     
    }

   
    public void handleConnection(Socket socket, ObjectInputStream input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {
                        Message message = null;
                        message = (Message) input.readObject();
                        if (message != null) {
                            logger.info("Message recieved: " + message.getMsg() + " MessageType: " + message.getType() + " Name: " + message.getName());
                            switch (message.getType()) {
                                case USER :
                                    listview.get(message.getName()).add(message);
                                    if(message.getName().equals(controller.userNow))
                                        controller.updateMgs(message);
                                    break;
                                case FILE:
                                     listview.get(message.getName()).add(message);
                                    if(message.getName().equals(controller.userNow))
                                        controller.updateMgs(message);
                                    break;       
                                case EXCHANGE_KEY:
                                    exchangeKeyMessage(message);
                                    break;                                    
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    controller.logoutScene();
                }
            }

        }).start();
    }


    public void acceptRequestConnect(Message message) throws IOException{
        String userSendConnect = message.getMsg();

        Socket sock = new Socket(message.getIp(), Integer.parseInt(message.getPort()));
        ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
        senders.put(userSendConnect, out);
        listview.put(userSendConnect, new ArrayList<Message>());

        message.setType(MessageType.INVITE);
        message.setName(this.username);//Ten cua client gui
        out.writeObject(message);
        handleConnection(sock,new ObjectInputStream(sock.getInputStream()));
        logger.info("User name: "+userSendConnect+" IP: "+message.getIp()+" Port: "+message.getPort());
        ChatController.showInfo("Notice to "+this.username,"User: "+userSendConnect +" want to connect to you !");

    }
    
    public void sendFile(byte[] byteArray,File file, String username) throws IOException
    {
        //this.messageToSend = message;
        logger.info("sendFIle: "+username+" message:"+file.getName() + " is sent to " +username);
        
        Message createMessage = new Message();
        createMessage.setName(this.username);
        createMessage.setType(MessageType.FILE);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMsg(file.getName());
        createMessage.setPicture(this.picture);
        
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(byteArray,0,byteArray.length);
        createMessage.setByteArray(byteArray);
        
        listview.get(username).add(createMessage);
        this.controller.cellRender.setTxt(username, "Send File");
        if(username.equals(controller.userNow))
            controller.addToChat(createMessage);
        
        ObjectOutputStream ossTo = senders.get(username);
        try {
            ossTo.writeObject(createMessage);
            ossTo.flush();
        } catch (IOException ex) {
            logger.error("Can not send message to : " + username, ex);
        }
    }

    public void sendToMessEXCHANGE_KEY_Lan1 (){
        Message createMessage = new Message();
        createMessage.setName(this.username);
        createMessage.setType(MessageType.EXCHANGE_KEY);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMsg(crypt.getNameAlgorithm());
        createMessage.setPicture(this.picture);
        ArrayList<User> listUser = controller.listUser;
        try {
            
            //Ma hoa key
            ArrayList<byte[]> listKey = crypt.getListKey();
            byte[] exchangeKey = listKey.get(4);//"234".getBytes();//
            byte[] privateKeyByteDSA = listKey.get(3);
            byte [] exChangeKeySign = DSA.EncryptionDSA(exchangeKey, privateKeyByteDSA);                                           
            for (int i = 0; i < listUser.size(); i++) {
                User user= listUser.get(i);
                ObjectOutputStream ossTo = senders.get(user.getName());
                if (ossTo == null){
                    continue;
                }  
                byte[] publicKeyByteRSA= user.getCrypt().getListKey().get(0);
                logger.info("BYTE:-----------------   " + String.valueOf(exchangeKey.length) +"   "+ String.valueOf(exChangeKeySign.length));
                
                byte[] exChangeKeySign_EnRSA = RSA.EncryptionRSA(exChangeKeySign, publicKeyByteRSA);              
                byte[] exChangeKey_EnRSA = RSA.EncryptionRSA(exchangeKey, publicKeyByteRSA);                
                ArrayList<byte[]> listAlgorithm = new ArrayList<byte[]>();
                listAlgorithm.add(exChangeKey_EnRSA);
                listAlgorithm.add(exChangeKeySign_EnRSA);
                createMessage.setAlgorithm(listAlgorithm);
//                logger.info(Base64.getEncoder().encodeToString(exchangeKey));                            
//                logger.info(Base64.getEncoder().encodeToString(exChangeKeySign));                            
//                logger.info(Base64.getEncoder().encodeToString(listKey.get(1)));                

                ossTo.writeObject(createMessage);
                ossTo.flush();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public  void exchangeKeyMessage(Message msg){
        String nameAlgo = msg.getMsg();
        String nameUserSend = msg.getName();
        ArrayList<byte[]> listAlgo = msg.getAlgorithm();
        byte[] privateKeyByteRSA= crypt.getListKey().get(2);
        ArrayList<User> listUser = controller.listUser;
        byte[] publicKeyByteDSA = null;
        int i;
        ArrayList<byte[]> listKey = new ArrayList<byte[]>();
        for ( i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getName().equals(nameUserSend) ){
                listKey = listUser.get(i).getCrypt().getListKey();
                publicKeyByteDSA = listKey.get(1);
                break;
            }
        }
        logger.info("exchange key ..............");
        byte[] exChangeKey = RSA.DecryptionRSA(listAlgo.get(0), privateKeyByteRSA);
        byte[] exChangeKeySign = RSA.DecryptionRSA(listAlgo.get(1), privateKeyByteRSA);
//            logger.info(Base64.getEncoder().encodeToString(exChangeKey));
//            logger.info(Base64.getEncoder().encodeToString(exChangeKeySign));
//            logger.info(Base64.getEncoder().encodeToString(publicKeyByteDSA));

        boolean result = DSA.DecryptionDSA_verifySign(exChangeKey, exChangeKeySign, publicKeyByteDSA);
        if(!result){
            controller.showInfo(username,"HACKKKKKKKKKK");
        }
        else{
            listKey.add(2,exChangeKey);
            controller.listUser.get(i).setCrypt(new Cryptography(nameAlgo,listKey));

        }


    }
    
    
}
