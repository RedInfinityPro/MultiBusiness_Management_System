package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BusinessApp {

    private static Tools tools = new Tools();
    private Label goldValueLabel;
    private Label moneyValueLabel;
    private Label astronomicalBucksValueLabel;
    private Timeline timeline;
    private static Integer startYear = 1970;
    private static Integer currentYear = startYear;
    private static Integer endYear = currentYear + 5;
    private Label yearLabel;

    BusinessApp() {
        YearUpdater();
        updateMetricsLabel_timeLine();
    }

    public static Integer get_startYear() {
        return startYear;
    }

    public static Integer get_endYear() {
        return endYear;
    }

    public static Integer get_currentYear() {
        return currentYear;
    }

    private void updateMetricsLabel_timeLine() {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(1), event -> {
                    updateMetricsLabel();
                })
        );
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();
    }

    private void updateMetricsLabel() {
        goldValueLabel.setText("$" + tools.Format_Number(BusinessDashboard.get_currentGold()));
        goldValueLabel.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        goldValueLabel.setFont(App.header_Font);
        moneyValueLabel.setText("$" + tools.Format_Number(BusinessDashboard.get_currentMoney()));
        moneyValueLabel.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        moneyValueLabel.setFont(App.header_Font);
        astronomicalBucksValueLabel.setText("$" + tools.Format_Number(BusinessDashboard.get_currentAstronomicalBucks()));
        astronomicalBucksValueLabel.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        astronomicalBucksValueLabel.setFont(App.header_Font);
    }

    private void YearUpdater() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    currentYear++;
                    endYear = currentYear + 5;
                    try {
                        yearLabel.setText("Year: " + currentYear);
                    } catch (Exception e) {
                        System.out.print(e.toString());
                    }
                    BusinessDashboard.updateAllCharts();
                })
        );
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();
    }

    public Parent create_App() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        VBox headerVBox = create_Header();
        borderPane.setTop(headerVBox);
        TabPane tabPane = create_TabPane();
        borderPane.setCenter(tabPane);
        return borderPane;
    }

    private VBox create_Header() {
        VBox headerVBox = new VBox(10);
        headerVBox.setStyle("-fx-background-color: #334a5fff; -fx-padding: 15;");
        // title
        HBox titleHBox = new HBox(10);
        Label businessNameLabel = new Label("Business Name: ");
        businessNameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        businessNameLabel.setFont(App.title_Font);
        TextField businessNameField = new TextField();
        businessNameField.setStyle("-fx-text-fill: black;");
        businessNameField.setFont(App.title_Font);
        businessNameField.setPromptText("Business Name...");
        yearLabel = new Label();
        yearLabel.setStyle("-fx-text-fill: white;");
        yearLabel.setFont(App.title_Font);
        titleHBox.getChildren().addAll(businessNameLabel, businessNameField, yearLabel);
        // metrics
        HBox metricsHBox = new HBox(10);
        metricsHBox.setAlignment(Pos.CENTER);
        VBox GoldVBox = new VBox();
        Label GoldLabel = new Label("Gold:");
        GoldLabel.setStyle("-fx-text-fill: white;");
        GoldLabel.setFont(App.header_Font);
        goldValueLabel = new Label("");
        GoldVBox.getChildren().addAll(GoldLabel, goldValueLabel);
        VBox MoneyVBox = new VBox();
        Label MoneyLabel = new Label("Money:");
        MoneyLabel.setStyle("-fx-text-fill: white;");
        MoneyLabel.setFont(App.header_Font);
        moneyValueLabel = new Label("");
        MoneyVBox.getChildren().addAll(MoneyLabel, moneyValueLabel);
        VBox AstronomicalBucksVBox = new VBox();
        Label AstronomicalBucksLabel = new Label("Astronomical Bucks:");
        AstronomicalBucksLabel.setStyle("-fx-text-fill: white;");
        AstronomicalBucksLabel.setFont(App.header_Font);
        astronomicalBucksValueLabel = new Label("");
        AstronomicalBucksVBox.getChildren().addAll(AstronomicalBucksLabel, astronomicalBucksValueLabel);
        Button convertButton = new Button("Convert");
        convertButton.setFont(App.normal_Font);
        metricsHBox.getChildren().addAll(GoldVBox, MoneyVBox, AstronomicalBucksVBox, convertButton);
        headerVBox.getChildren().addAll(titleHBox, metricsHBox);
        return headerVBox;
    }

    private TabPane create_TabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                BusinessDashboard.create_DashboardTab(),
                BusinessesTab.create_BusinessesTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }
}