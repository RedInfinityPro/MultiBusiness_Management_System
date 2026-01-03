package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Scene primaryScene;
    public static Stage stage;
    private static Parent mainRoot;

    @Override
    public void start(Stage stage) throws IOException {
        BusinessDashboard businessDashboard = new BusinessDashboard();
        mainRoot = businessDashboard.create_BusinessDashboard();
        primaryScene = new Scene(mainRoot, 900, 600);
        stage.setScene(primaryScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}