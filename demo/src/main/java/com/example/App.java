package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Scene primaryScene;
    public static Stage stage;
    private static Parent mainRoot;
    // font
    public static final Font title_Font = Font.font("Arial", FontWeight.BOLD, 18);
    public static final Font header_Font = Font.font("Arial", FontWeight.SEMI_BOLD, 16);
    public static final Font normal_Font = Font.font("Arial", FontWeight.NORMAL, 12);

    @Override
    public void start(Stage stage) throws IOException {
        BusinessApp businessDashboard = new BusinessApp();
        mainRoot = businessDashboard.create_BusinessDashboard();
        primaryScene = new Scene(mainRoot, 900, 600);
        stage.setScene(primaryScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}