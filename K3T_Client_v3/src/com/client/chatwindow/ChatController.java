package com.client.chatwindow;

import com.client.login.MainLauncher;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.messages.User;
import com.messages.bubble.BubbleSpec;
import com.messages.bubble.BubbledLabel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ChatController implements Initializable {

    @FXML
    private TextArea messageBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label onlineCountLabel;
    @FXML
    private ListView userList;
    @FXML
    private ImageView userImageView;
    @FXML
    ListView chatPane;
    @FXML
    ListView statusList;
    @FXML
    BorderPane borderPane;
    @FXML
    ComboBox statusComboBox;

    @FXML
    private ImageView userNowConnectImage;
    @FXML
    private Label userNowConnectName;
    
    private double xOffset;
    private double yOffset;
    Logger logger = LoggerFactory.getLogger(ChatController.class);
    private Listener listener;
    String userNow="";
    public CellRenderer cellRender;


    public void setListener(Listener ls) {
        this.listener = ls;
    }

    public Listener getListener() {
        return this.listener;
    }
    public void setuserNowConnectImage(String picture){
        switch (picture) {
            case "Default":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
                break;
            case "MinhTham":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/MinhTham.png").toString()));
                break;
            case "AnKhang":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/AnKhang.png").toString()));
                break;   
            case "HuynhThoai":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/HuynhThoai.png").toString())); 
                break;
            case "NgocThach":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/NgocThach.png").toString())); 
                break;
            case "CaoLuong":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/CaoLuong.png").toString())); 
                break;
            case "HuynhThuc":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/HuynhThuc.png").toString())); 
                break;
            case "ChiLuong":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/ChiLuong.png").toString())); 
                break;
            case "TuanVu":
                this.userNowConnectImage.setImage(new Image(getClass().getClassLoader().getResource("images/TuanVu.png").toString())); 
                break;                  
        }       
    }
    public void setuserNowConnectName(String name){
        userNowConnectName.setFont(Font.font("Verdana", 16));
        userNowConnectName.setText(name);
    }
    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
    }

    public void setImageLabel() throws IOException {
        this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void setUserList(Message msg) {
        logger.info("setUserList() method Enter");
        Platform.runLater(() -> {
            ObservableList<User> users = FXCollections.observableList(msg.getUsers());
            userList.setItems(users);
            cellRender = new CellRenderer(this);
            userList.setCellFactory(cellRender);
            setOnlineLabel(String.valueOf(msg.getUserlist().size()));
        });
        logger.info("setUserList() method Exit");
    }

    public void sendMethod(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
        }
    }

    @FXML
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    public void minimizeApplication(){
        MainLauncher.getPrimaryStage().setIconified(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setImageLabel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Drag and Drop */
        borderPane.setOnMousePressed(event -> {
            xOffset = MainLauncher.getPrimaryStage().getX() - event.getScreenX();
            yOffset = MainLauncher.getPrimaryStage().getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            MainLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            MainLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });

        statusComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    listener.sendStatusUpdate(Status.valueOf(newValue.toUpperCase()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /* Added to prevent the enter from adding a new line to inputMessageBox */
        messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    sendButtonAction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ke.consume();
            }
        });

    }

    public void setImageLabel(String selectedPicture) {
        switch (selectedPicture) {
            case "Default":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
                break;
            case "MinhTham":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/MinhTham.png").toString()));
                break;
            case "AnKhang":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/AnKhang.png").toString()));
                break;   
            case "HuynhThoai":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/HuynhThoai.png").toString())); 
                break;
            case "NgocThach":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/NgocThach.png").toString())); 
                break;
            case "CaoLuong":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/CaoLuong.png").toString())); 
                break;
            case "HuynhThuc":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/HuynhThuc.png").toString())); 
                break;
            case "ChiLuong":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/ChiLuong.png").toString())); 
                break;
            case "TuanVu":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/TuanVu.png").toString())); 
                break;                  
        }
    }

    public void logoutScene() {
        Platform.runLater(() -> {
            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent window = null;
            try {
                window = (Pane) fmxlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = MainLauncher.getPrimaryStage();
            Scene scene = new Scene(window);
            stage.setMaxWidth(364);
            stage.setMaxHeight(550);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }


    
    public static void showInfo(String message1, String message2) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(message1);
            alert.setHeaderText(null);
            alert.setContentText(message2);
            alert.showAndWait();
        });
    }

    public void setUserNow(String userN) throws IOException {

        if (!listener.isConnected(userN)) {//neu chua co ket noi tu truoc
            listener.createConnect(userN);        //gui message toi server
        }
        if (!userN.equals(this.userNow)) {
            this.userNow = userN;

            chatPane.getItems().clear();
            ArrayList<Message> list = listener.listview.get(userNow);
            if (list == null) {
                return;
            }
            for (Message mgs : list) {
                addToChat(mgs);
            }
        }

    }

    public synchronized void updateMgs(Message mgs) {
        this.addToChat(mgs);
        this.cellRender.setTxt(mgs.getName(), mgs.getMsg());
    }



    public synchronized void addToChat(Message msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture() + ".png").toString());
                //mage image = userImageView.getImage();
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                
                BubbledLabel bl6 = new BubbledLabel();
                if(msg.getType()==MessageType.USER){
                    bl6.setText(msg.getMsg());
                }
                else if(msg.getType()==MessageType.FILE){
                    String fileName = msg.getMsg();
                    bl6.setText(fileName+" recieved !");   
                    bl6.setUnderline(true);
                    bl6.setOnMouseClicked(new EventHandler<Event>() {                
                        @Override
                        public void handle(Event event) {
                            FileChooser fileSaver = new FileChooser();
                            fileSaver.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("Text File",
                                            "*.txt"),
                                    new FileChooser.ExtensionFilter("Document",
                                            "*.pdf", "*.docx"),
                                    new FileChooser.ExtensionFilter("Image Files",
                                            "*.jpg", "*.png", "*.bmp", "*.gif"),
                                    new FileChooser.ExtensionFilter("Video Files",
                                            "*.mkv", "*.mp4"),
                                    new FileChooser.ExtensionFilter("Audio Files",
                                            "*.mp3", "*.m4p")
                            );
                            File file = fileSaver.showSaveDialog(MainLauncher.getPrimaryStage());
                            

                            if(file!=null)
                            {
                                logger.info(file.getAbsolutePath());
                                try {
                                    saveFile(file.getAbsolutePath(), msg.getByteArray());
                                } catch (IOException ex) {
                                    logger.info("No file !!!");
                                }
 
                           
                            }
                        }
                    });
                }
                bl6.setBackground(new Background(new BackgroundFill(Color.web("#F1F0F0"), null, null)));
                
                
                HBox x = new HBox();
                x.setMaxWidth(chatPane.getWidth() - 20);
                x.setAlignment(Pos.TOP_LEFT);
                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                x.getChildren().addAll(profileImage, bl6);
                
                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };
        othersMessages.setOnSucceeded(event -> chatPane.getItems().add(othersMessages.getValue()));
        
        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = userImageView.getImage();
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                
                BubbledLabel bl6 = new BubbledLabel();
                if(msg.getType()==MessageType.USER){
                    bl6.setText(msg.getMsg());
                }
                else if(msg.getType()==MessageType.FILE){
                    String fileName = msg.getMsg();
                    bl6.setText(fileName+" is sent !");
                }
                bl6.setBackground(new Background(new BackgroundFill(Color.web("#007bff80"), null, null)));
                
                HBox x = new HBox();
                x.setMaxWidth(chatPane.getWidth() - 20);
                x.setAlignment(Pos.TOP_RIGHT);
                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                x.getChildren().addAll(bl6, profileImage);
                
                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };
        yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));
        
        if (msg.getName().equals(this.usernameLabel.getText())) {
            yourMessages.run();
        } else {
            othersMessages.run();
        }
    }
    private FileChooser fileChooser;
    private File file;
    private byte [] byteArray;

    @FXML
    void btnSendFileOnAction(ActionEvent event) {
        fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(MainLauncher.getPrimaryStage());
        if(file!=null)
        {
            byteArray  = new byte [(int)file.length()];
            messageBox.setText(file.getName()+" is sent !");
            this.showInfo(userNow, "Choose file successly !!!");
        }
    }
    
    public void sendButtonAction() throws IOException {
        String msg = messageBox.getText();
        if(file!=null&&userNow!=""){
            messageBox.clear();                        
            listener.sendFile(byteArray,file,userNow);
            file=null;
            byteArray=null;
        }
        else if (!messageBox.getText().isEmpty()&&userNow!="") {
            if (msg.length()>50) {
                int len = msg.length();
                String partA = "", partB = "";
                for(int i=0;i<len;i+=20)
                {
                    if(i>0)
                    {
                        partA = msg.substring(0, i);
                        partB = msg.substring(i+1, len);
                        msg = partA + "\n" + partB;
                    }
                }
            }
            messageBox.clear();            
            listener.sendTo(msg,userNow);// send message to user
        }
        else if (!messageBox.getText().isEmpty()&&userNow=="") {
            showInfo("Notice to "+listener.username, "Please connect with anyone !!!");
            messageBox.clear();
        }
    }
        
    public void saveFile(String filePath,byte[] byteArray) throws IOException
    {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(byteArray, 0, byteArray.length);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        showInfo(userNow,"Success !!!");
    }
}
