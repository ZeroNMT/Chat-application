package com.client.chatwindow;

import com.messages.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * A Class for Rendering users images / name on the userlist.
 */
class CellRenderer implements Callback<ListView<User>, ListCell<User>> {

    private ChatController ch;
    private HashMap<String,Text> hmap;
    public CellRenderer(ChatController ch) {
        this.ch = ch;
        hmap = new HashMap<>();
    }
    public void setTxt(String name, String message){
        Text tt = hmap.get(name);
        if(tt == null) return;
        if(message.length() < 10){
            tt.setText(message);
        }
        else{
            tt.setText(message.substring(0,10).concat("..."));
        }
        tt.setFill(Color.GREY);
    }

    @Override
    public ListCell<User> call(ListView<User> p) {

        ListCell<User> cell = new ListCell<User>() {

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();
                    VBox vBox = new VBox();

                    String userName = user.getName();
                    Text name = new Text(userName);
                    name.setFill(Color.BLUE);
                    name.setFont(Font.font("Verdana", 20));

                    Text newMgs = new Text("");
                    hmap.put(userName, newMgs);
                    vBox.getChildren().addAll(name, newMgs);

                    ImageView statusImageView = new ImageView();
                    Image statusImage = new Image(getClass().getClassLoader().getResource("images/" + user.getStatus().toString().toLowerCase() + ".png").toString(), 16, 16, true, true);
                    statusImageView.setImage(statusImage);
                    //ImageView pictureImageView = new ImageView();
                    Image image = new Image(getClass().getClassLoader().getResource("images/" + user.getPicture() + ".png").toString(), 50, 50, true, true);
                    ImageView picture = new ImageView(image);
                    picture.setFitHeight(40);
                    //button.setOnAction(this::handleButtonAction);
                    if (!userName.equals(ch.getListener().username)) {
                        hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                try {
                                    ch.setUserNow(user.getName());
                                    ch.setuserNowConnectName(user.getName());
                                    ch.setuserNowConnectImage(user.getPicture());
                                } catch (IOException ex) {
                                    Logger.getLogger(CellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                    //hBox.getChildren().addAll(button,Text_space1,statusImageView, pictureImageView,Text_space3,name);
                    //hBox.getChildren().addAll(statusImageView, pictureImageView,name);
                    hBox.getChildren().addAll(statusImageView, picture, vBox);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}
