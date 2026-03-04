package com.example.rtsgame;

import com.example.rtsgame.map.MapManager;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws ParserConfigurationException, IOException, SAXException {
        Group root = new Group();
        Scene scene = new Scene(root, 1000, 600);
        Game game = new Game(root, scene);
        root.getChildren().add(game);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}