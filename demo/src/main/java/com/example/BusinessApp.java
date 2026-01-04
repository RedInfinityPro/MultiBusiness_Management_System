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
    private VBox totalMoneyLabel;
    private VBox totalGoldLabel;
    private VBox totalAstronomicalBucksLabel;
    private Timeline timeline;
    private Label yearLabel;
    public static Integer startYear = 1970;
    public static Integer currentYear = startYear;
    public static Integer endYear = currentYear + 5;

    BusinessApp() {
        YearUpdater();
        updateMetricsLabel_timeLine();
    }

    public Parent create_BusinessDashboard() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        VBox headerVBox = createHeader();
        borderPane.setTop(headerVBox);
        // Center - Tab pane
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                BusinessDashboard.createDashboardTab(),
                BusinessesTab.createBusinessesTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        borderPane.setCenter(tabPane);
        return borderPane;
    }

    private VBox createHeader() {
        VBox headerVBox = new VBox(10);
        headerVBox.setStyle("-fx-background-color: #6792bdff; -fx-padding: 15;");
        // title
        HBox titleHBox = new HBox(10);
        Label businessNameLabel = new Label("Business Name: ");
        businessNameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        businessNameLabel.setFont(App.title_Font);
        TextField businessNameField = new TextField();
        businessNameField.setStyle("-fx-text-fill: black;");
        businessNameField.setFont(App.title_Font);
        businessNameField.setPromptText("Business Name...");
        yearLabel = new Label("Year: " + currentYear);
        yearLabel.setStyle("-fx-text-fill: white;");
        yearLabel.setFont(App.title_Font);
        titleHBox.getChildren().addAll(businessNameLabel, businessNameField, yearLabel);
        // metrics
        HBox metricsHBox = new HBox(10);
        metricsHBox.setAlignment(Pos.CENTER);
        totalMoneyLabel = createMetricLabel("Total Money: ", "$0");
        totalGoldLabel = createMetricLabel("Total Gold: ", "$0");
        totalAstronomicalBucksLabel = createMetricLabel("Total Astronomical Bucks: ", "$0");
        Button convertButton = new Button("Convert");
        convertButton.setFont(App.normal_Font);
        metricsHBox.getChildren().addAll(totalMoneyLabel, totalGoldLabel, totalAstronomicalBucksLabel, convertButton);
        headerVBox.getChildren().addAll(titleHBox, metricsHBox);
        return headerVBox;
    }

    private void updateMetricsLabel_timeLine () {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(1), event -> {
                    updateMetricsLabel();
                })
        );
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();
    }

    private void updateMetricsLabel() {
        totalMoneyLabel.getChildren().clear();
        totalMoneyLabel.getChildren().addAll(
                new Label("Total Money: "),
                new Label("$" + String.format("%s", tools.Format_Number(BusinessDashboard.currentMoney)))
        );
        totalGoldLabel.getChildren().clear();
        totalGoldLabel.getChildren().addAll(
                new Label("Total Gold: "),
                new Label("$" + String.format("%s", tools.Format_Number(BusinessDashboard.currentGold)))
        );
        totalAstronomicalBucksLabel.getChildren().clear();
        totalAstronomicalBucksLabel.getChildren().addAll(
                new Label("Total Astronomical Bucks: "),
                new Label("$" + String.format("%s", tools.Format_Number(BusinessDashboard.currentAstronomicalBucks)))
        );
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

    private VBox createMetricLabel(String title, String value) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white;");
        titleLabel.setFont(App.header_Font);
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        valueLabel.setFont(App.header_Font);
        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }
}