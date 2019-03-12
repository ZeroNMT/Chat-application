package com.client.login;

import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginController implements Initializable {
    @FXML private ImageView Defaultview;
    @FXML public  TextField hostnameTextfield;
    @FXML private TextField portTextfield;
    @FXML private TextField usernameTextfield;
    @FXML private ChoiceBox imagePicker;
    @FXML private Label selectedPicture;
    @FXML private TextField portListenTextfield;
    public static ChatController con;
    @FXML private BorderPane borderPane;
    private double xOffset;
    private double yOffset;
    private Scene scene;
    private static LoginController instance;

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController() {
        instance = this;
    }

    public static LoginController getInstance() {
        return instance;
    }
    public void loginButtonAction() throws IOException {
        String hostname = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        String username = usernameTextfield.getText();
        String picture = selectedPicture.getText();
        if (username.isEmpty() || hostname.isEmpty()   || portListenTextfield.getText().isEmpty() || portTextfield.getText().isEmpty()  ){
            showWarning("No enough infomation !!!");
        }
        else{
            String portListen = portListenTextfield.getText();
            String ipAddress =InetAddress.getLocalHost().getHostAddress();
            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));
            Parent window = (Pane) fmxlLoader.load();
            con = fmxlLoader.<ChatController>getController();
            Listener listener = new Listener(hostname, port, username, picture, portListen, ipAddress, con);
            con.setListener(listener); // Them boi Thoai ngay 13/10/18 9:44pm
            Thread x = new Thread(listener);
            x.start();
            this.scene = new Scene(window);
        }
    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {
            Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
            stage.setResizable(true);
            stage.setWidth(700);
            stage.setHeight(620);

            stage.setOnCloseRequest((WindowEvent e) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setScene(this.scene);
            stage.setMinWidth(700);
            stage.setMinHeight(300);
            stage.centerOnScreen();
            con.setUsernameLabel(usernameTextfield.getText());
            con.setImageLabel(selectedPicture.getText());
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imagePicker.getSelectionModel().selectFirst();
        selectedPicture.setText("default");
        selectedPicture.setVisible(false);

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

        imagePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldPicture, String newPicture) {
                if (newPicture != null) {
                    usernameTextfield.clear();
                    switch (newPicture) {
                        case "Default":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
                            break;
                        case "Minh Tham":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/MinhTham.png").toString())); 
                            selectedPicture.setText("MinhTham");
                            usernameTextfield.setText(newPicture);
                            break;
                        case "An Khang":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/AnKhang.png").toString())); 
                            selectedPicture.setText("AnKhang");
                            usernameTextfield.setText(newPicture);
                            break;
                        case "Huynh Thoai":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/HuynhThoai.png").toString())); 
                            selectedPicture.setText("HuynhThoai");
                            usernameTextfield.setText(newPicture);
                            break;
                        case "Ngoc Thach":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/NgocThach.png").toString())); 
                            selectedPicture.setText("NgocThach");
                            usernameTextfield.setText(newPicture);
                            break;
                        case "Cao Luong":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/CaoLuong.png").toString())); 
                            selectedPicture.setText("CaoLuong");
                            usernameTextfield.setText(newPicture);
                            break;
                        case "Huynh Thuc":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/HuynhThuc.png").toString())); 
                            selectedPicture.setText("HuynhThuc");
                            usernameTextfield.setText(newPicture);
                            break;
                        case "Chi Luong":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/ChiLuong.png").toString())); 
                            selectedPicture.setText("ChiLuong");
                            usernameTextfield.setText(newPicture);
                            break;
                        case "Tuan Vu":
                            Defaultview.setImage(new Image(getClass().getClassLoader().getResource("images/TuanVu.png").toString())); 
                            selectedPicture.setText("TuanVu");
                            usernameTextfield.setText(newPicture);
                            break;                            
                    }
                }
            }
        });
        int numberOfSquares = 50;
        while (numberOfSquares > 0){
            generateAnimation();
            numberOfSquares--;
        }
    }


    /* This method is used to generate the animation on the login window, It will generate random ints to determine
     * the size, speed, starting points and direction of each square.
     */
    public void generateAnimation(){
        Random rand = new Random();
        int sizeOfSqaure = rand.nextInt(70) + 1;
        int speedOfSqaure = rand.nextInt(10) + 5;
        int startXPoint = rand.nextInt(550);
        int startYPoint = rand.nextInt(364);
        int direction = rand.nextInt(5) + 1;

        KeyValue moveXAxis = null;
        KeyValue moveYAxis = null;
        Rectangle r1 = null;

        switch (direction){
            case 1 :
                // MOVE LEFT TO RIGHT
                r1 = new Rectangle(0,startYPoint,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 364 -  sizeOfSqaure);
                break;
            case 2 :
                // MOVE TOP TO BOTTOM
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 550 - sizeOfSqaure);
                break;
            case 3 :
                // MOVE LEFT TO RIGHT, TOP TO BOTTOM
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 364 -  sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 550 - sizeOfSqaure);
                break;
            case 4 :
                // MOVE BOTTOM TO TOP
                r1 = new Rectangle(startXPoint,550-sizeOfSqaure ,sizeOfSqaure,sizeOfSqaure);
                moveYAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 5 :
                // MOVE RIGHT TO LEFT
                r1 = new Rectangle(364-sizeOfSqaure,startYPoint,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 6 :
                //MOVE RIGHT TO LEFT, BOTTOM TO TOP
                r1 = new Rectangle(startXPoint,0,sizeOfSqaure,sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 364 -  sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 550 - sizeOfSqaure);
                break;

            default:
                System.out.println("default");
        }

        r1.setFill(Color.web("#007bff"));
        r1.setOpacity(0.1);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speedOfSqaure * 1000), moveXAxis, moveYAxis);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        borderPane.getChildren().add(borderPane.getChildren().size()-1,r1);
    }

    /* Terminates Application */
    public void closeSystem(){
        Platform.exit();
        System.exit(0);
    }

    public void minimizeWindow(){
        MainLauncher.getPrimaryStage().setIconified(true);
    }

    public void showErrorDialog(String message) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(message);
            alert.setContentText("Please check for firewall issues and check if the server is running.");
            alert.showAndWait();
        });
    }

    public static void showWarning(String message) {
    Platform.runLater(()-> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(message);
        alert.setContentText("Please enter infomation ");
        alert.showAndWait();
    });
    }
}
