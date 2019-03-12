package com.server;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.messages.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    /* Setting up variables */
    private static final int PORT = 9001;
    private static final HashMap<String, User> names = new HashMap<>();
    private static HashMap<String, ObjectOutputStream> writers = new HashMap<>();
    private static ArrayList<User> users = new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(PORT);
        logger.info("The chat server center is running.");
        logger.info("\t IP address:"+InetAddress.getLocalHost().getHostAddress());
        logger.info("\t Port: "+ listener.getLocalPort());
       
        
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }
    }


    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        
        private Logger logger = LoggerFactory.getLogger(Handler.class);
        private User user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;

        public Handler(Socket socket) throws IOException {
            this.socket = socket;
        }

        public void run() {
            logger.info("Attempting to connect a user...");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                checkDuplicateUsername(firstMessage);
                writers.put(firstMessage.getName(),output);
                addToList();

                while (socket.isConnected()) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        logger.info(inputmsg.getType() + " - " + inputmsg.getName() + ": " + inputmsg.getMsg());
                        switch (inputmsg.getType()) {
                            case CONNECTED:
                                addToList();
                                break;
                            case STATUS:
                                changeStatus(inputmsg);
                                break;
                            case INVITE:
                                sendRequestToFriend(inputmsg);
                                break;
                        }
                    }
                }
            } catch (SocketException socketException) {
                logger.error("Socket Exception for user " + name);
            } catch (Exception e){
                logger.error("Exception in run() method for user: " + name, e);
            } finally {
                closeConnections();
            }
        }

        private Message changeStatus(Message inputmsg) throws IOException {
            logger.debug(inputmsg.getName() + " has changed status to  " + inputmsg.getStatus());
            Message msg = new Message();
            msg.setName(user.getName());
            msg.setType(MessageType.STATUS);
            msg.setMsg("");
            User userObj = names.get(name);
            userObj.setStatus(inputmsg.getStatus());
            write(msg);
            return msg;
        }

        private synchronized void checkDuplicateUsername(Message firstMessage) {
            logger.info(firstMessage.getName() + " is trying to connect");
            if (!names.containsKey(firstMessage.getName())) {
                this.name = firstMessage.getName();
                user = new User();
                user.setName(firstMessage.getName());
                user.setStatus(Status.ONLINE);
                user.setPicture(firstMessage.getPicture());
                user.setIp(firstMessage.getIp());
                user.setPort(firstMessage.getPort());
                users.add(user);
                names.put(name, user);
                logger.info(name + ": " + user.getIp() + " and "+user.getPort() + " has been added to the list");
            } else {
                logger.error(firstMessage.getName() + " is already connected");
            }
        }



        private Message removeFromList() throws IOException {
            logger.debug("removeFromList() method Enter");
            Message msg = new Message();
            msg.setMsg("has left the chat.");
            msg.setType(MessageType.DISCONNECTED);
            msg.setName("SERVER CENTER");
            //msg.setUserlist(names);
            write(msg);
            logger.debug("removeFromList() method Exit");
            return msg;
        }

        /*
         * For displaying that a user has joined the server
         */
        private Message addToList() throws IOException {
            Message msg = new Message();
            msg.setMsg("Welcome, You have now joined the server!");
            msg.setType(MessageType.CONNECTED);
            msg.setName("SERVER CENTER");
            write(msg);
            return msg;
        }
        
        //Ham gui yeu cau ket noi cua user A den user B thong qua server center
        private Message sendRequestToFriend(Message inputmsg) throws IOException {
            String userNeedSendConnect = inputmsg.getMsg();// User name B ma user A muon ket noi den
            String userSendConnect = inputmsg.getName();// User name A da gui yeu cau ket noi den server center
            Message msg = new Message();
            msg.setName("SERVER CENTER");            
            msg.setMsg(userSendConnect);
            msg.setType(MessageType.INVITE);
            msg.setUserlist(names);
            msg.setUsers(users);
            msg.setOnlineCount(names.size());            
            // Tim ip and port cua user A de gui cho user B
            msg.setIp(inputmsg.getIp());
            msg.setPort(inputmsg.getPort());
            
            //Tim ObjectOutputStream cua user B
            if (writers.containsKey(userNeedSendConnect)) {//Kiem tra co hay khong user B
                ObjectOutputStream writer = writers.get(userNeedSendConnect);
                writer.writeObject(msg);
                writer.reset();
            }
            else{
                logger.info("No user: "+ userNeedSendConnect);
            }
            return msg;
        }

        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers.values()) {
                msg.setUserlist(names);
                msg.setUsers(users);
                msg.setOnlineCount(names.size());
                msg.setIp("");
                msg.setPort("");
                writer.writeObject(msg);
                writer.reset();
            }
        }

        /*
         * Once a user has been disconnected, we close the open connections and remove the writers
         */
        private synchronized void closeConnections()  {
            logger.debug("closeConnections() method Enter");
            logger.info("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            if (name != null) {
                names.remove(name);
                logger.info("User: " + name + " has been removed!");
            }
            if (user != null){
                users.remove(user);
                logger.info("User object: " + user + " has been removed!");
            }
            if (output != null){
                writers.remove(name);
                logger.info("Writer object: " + user + " has been removed!");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            logger.debug("closeConnections() method Exit");
        }
    }
}
