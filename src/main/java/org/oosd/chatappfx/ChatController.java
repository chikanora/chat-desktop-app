/**
 * "chat-app-view.fxml"コントローラ・クラスのサンプル・スケルトン
 */

package org.oosd.chatappfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatController
{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="apMain"
    private AnchorPane apMain; // Value injected by FXMLLoader

    @FXML // fx:id="btnSend"
    private Button btnSend; // Value injected by FXMLLoader

    @FXML // fx:id="spMain"
    private ScrollPane spMain; // Value injected by FXMLLoader

    @FXML // fx:id="tfMessage"
    private TextField tfMessage; // Value injected by FXMLLoader

    @FXML // fx:id="vbMessage"
    private VBox vbMessage; // Value injected by FXMLLoader

    private Server server;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
        assert apMain != null : "fx:id=\"apMain\" was not injected: check your FXML file 'chat-app-view.fxml'.";
        assert btnSend != null : "fx:id=\"btnSend\" was not injected: check your FXML file 'chat-app-view.fxml'.";
        assert spMain != null : "fx:id=\"spMain\" was not injected: check your FXML file 'chat-app-view.fxml'.";
        assert tfMessage != null : "fx:id=\"tfMessage\" was not injected: check your FXML file 'chat-app-view.fxml'.";
        assert vbMessage != null : "fx:id=\"vbMessage\" was not injected: check your FXML file 'chat-app-view.fxml'.";

        try
        {
            server = new Server(new ServerSocket(1234));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error creating server");
        }

        vbMessage.heightProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                spMain.setVvalue((Double) newValue);
            }
        });

        server.receiveMessageFromClient(vbMessage);
        btnSend.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                String messageToSend = tfMessage.getText();

                if (!messageToSend.isEmpty())
                {
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5, 5, 5, 10));

                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239, 242, 255); " +
                            "-fx-background-color: rgb(15, 125, 242); " +
                            "-fx-background-radius: 20px;");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    hbox.getChildren().add(textFlow);
                    vbMessage.getChildren().add(hbox);

                    server.sendMessageToClient(messageToSend);
                    tfMessage.clear();
                }
            }
        });
    }

    public static void addLabel(String messageFromClient, VBox vbox)
    {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233, 233, 235); " +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hbox.getChildren().add(textFlow);

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                vbox.getChildren().add(hbox);
            }
        });
    }
}