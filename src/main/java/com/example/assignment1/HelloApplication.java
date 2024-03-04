package com.example.assignment1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Taskbar.Feature;

public class HelloApplication extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());


        Rectangle clipRect = new Rectangle(900, 600);
        clipRect.setArcWidth(20);
        clipRect.setArcHeight(20);
        root.setClip(clipRect);

        scene.setFill(Color.TRANSPARENT);

        //set app icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/800px-Stack_Overflow_icon.svg.png")));
        stage.getIcons().add(icon);
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(getClass().getResource("/images/800px-Stack_Overflow_icon.svg.png"));
                taskbar.setIconImage(dockIcon);
            }

        }

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}