package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BusinessDashboard {
    private static int year = 0;
    // money
    private final List<DataPoint> moneyHistory = new ArrayList<>();
    private static double currentMoney = 1e10;
    private static double currentGold = 0.0;
    private static double currentAstronomicalBucks = 0.0;
    private LineChart<Number, Number> moneyPriceChart;
    // gold
    private static final List<DataPoint> goldPriceHistory = new ArrayList<>();
    private static final Random goldRandom = new Random();
    private static double lastGoldPrice = 300.0;
    private static LineChart<Number, Number> goldPriceChart;
    // astronomical bucks
    private static final List<DataPoint> astronomicalBuckHistory = new ArrayList<>();
    private static final Random astronomicalBuckRandom = new Random();
    private static double lastAstronomicalBuckPrice = 420000000;
    private static LineChart<Number, Number> astronomicalBuckPriceChart;
    // businesses and transactions
    private static ObservableList<BusinessesTab.Organization> organization = observableArrayList();
    private static BarChart<String, Number> businessChart;
    private static PieChart expenseChart;

    public static void updateAllCharts() {
        updateGoldPriceChart(BusinessApp.get_startYear(), BusinessApp.get_endYear());
        updateAstronomicalBuckChart(BusinessApp.get_startYear(), BusinessApp.get_endYear());
    }

    private static void updateBusinessChart() {
        businessChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Profit");
        for (BusinessesTab.Organization o : organization) {
            series.getData().add(new XYChart.Data<>(o.getName(), o.getRevenueOutput() - o.getExpensesOutput()));
        }
        businessChart.getData().add(series);
    }

    private static void updateExpenseChart() {
        expenseChart.getData().clear();
        for (BusinessesTab.Organization o : organization) {
            if (o.getExpensesOutput() > 0) {
                PieChart.Data data = new PieChart.Data(o.getName(), o.getExpensesOutput());
                expenseChart.getData().add(data);
            }
        }
    }

    private static void updateGoldPriceChart(int startYear, int endYear) {
        if (startYear >= endYear) {
            showAlert("Invalid Range", "Start year must be less than end year.");
            return;
        }
        List<DataPoint> dataPoints = fetchHistoricalGoldData(startYear, endYear);
        goldPriceChart.getData().clear();
        // Separate historical and projected data
        List<DataPoint> historicalData = new ArrayList<>();
        List<DataPoint> projectedData = new ArrayList<>();
        for (DataPoint point : dataPoints) {
            if (point.year <= BusinessApp.get_currentYear()) {
                historicalData.add(point);
            } else {
                projectedData.add(point);
            }
        }
        goldPriceChart.setCreateSymbols(true);
        // Create historical series (solid line)
        if (!historicalData.isEmpty()) {
            XYChart.Series<Number, Number> historicalSeries = new XYChart.Series<>();
            historicalSeries.setName(String.format("Historical Price (>%s)", BusinessApp.get_currentYear()));
            for (DataPoint point : historicalData) {
                historicalSeries.getData().add(new XYChart.Data<>(point.year, point.price));
            }
            goldPriceChart.getData().add(historicalSeries);
        }
        // Create projected series (dashed line effect via styling)
        if (!projectedData.isEmpty()) {
            XYChart.Series<Number, Number> projectedSeries = new XYChart.Series<>();
            projectedSeries.setName(String.format("Projected Price (%s)", endYear));
            // Add last historical point to connect the lines
            if (!historicalData.isEmpty()) {
                DataPoint lastHistorical = historicalData.get(historicalData.size() - 1);
                projectedSeries.getData().add(new XYChart.Data<>(lastHistorical.year, lastHistorical.price));
            }
            for (DataPoint point : projectedData) {
                projectedSeries.getData().add(new XYChart.Data<>(point.year, point.price));
            }
            goldPriceChart.getData().add(projectedSeries);
        }
        // Update x-axis range
        NumberAxis xAxis = (NumberAxis) goldPriceChart.getXAxis();
        xAxis.setLowerBound(startYear);
        xAxis.setUpperBound(endYear);
        xAxis.setTickUnit(Math.max(1, (endYear - startYear) / 10));
    }

    public static double get_currentMoney() {
        return currentMoney;
    }

    public static void subtract_currentMoney(Double value) {
        currentMoney -= value;
    }

    public static void add_currentMoney(Double value) {
        currentMoney += value;
    }

    public static double get_currentGold() {
        return currentGold;
    }

    public static double get_currentAstronomicalBucks() {
        return currentAstronomicalBucks;
    }

    public static void add_implementData(String name, String manager, boolean locked, double organizationValue, double organizationOutputTime, double organizationCurrentTime,double revenueOutput, double expensesOutput, double upgradeCost, double qualityCost) {
        BusinessesTab.Organization b1 = new BusinessesTab.Organization(name, manager, locked, organizationValue, organizationOutputTime, organizationCurrentTime,revenueOutput, expensesOutput, upgradeCost, qualityCost);
        organization.add(b1);
    }

    public static Tab create_DashboardTab() {
        Tab tab = new Tab("Dashboard");
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        // Business Summary Section
        HBox BusinessSection = create_BusinessSection();
        // Gold Price Analysis Section
        HBox sectionBox = new HBox(15);
        VBox goldSection = create_GoldPriceSection();
        VBox astronomicalBuckSection = create_AstronomicalBuckSection();
        sectionBox.getChildren().addAll(goldSection, astronomicalBuckSection);
        // content
        content.getChildren().addAll(BusinessSection,sectionBox);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        tab.setContent(scroll);
        return tab;
    }

    private static HBox create_BusinessSection() {
        HBox section = new HBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: #ecf0f1; -fx-background-radius: 5;");
        // Business Performance Chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        businessChart = new BarChart<>(xAxis, yAxis);
        businessChart.setTitle("Business Profit Comparison");
        businessChart.setPrefHeight(300);
        // Expense Distribution Chart
        expenseChart = new PieChart();
        expenseChart.setTitle("Expense Distribution by Business");
        expenseChart.setPrefHeight(300);
        section.getChildren().addAll(businessChart, expenseChart);
        updateBusinessChart();
        updateExpenseChart();
        return section;
    }

    private static VBox create_GoldPriceSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: #ecf0f1; -fx-background-radius: 5;");
        Label sectionTitle = new Label("Gold Price Analysis");
        sectionTitle.setStyle("fx-font-weight: bold;");
        sectionTitle.setFont(App.header_Font);
        // Gold price chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Year");
        yAxis.setLabel("Price per Troy Ounce");
        yAxis.setAutoRanging(true);
        xAxis.setAutoRanging(false);
        goldPriceChart = new LineChart<>(xAxis, yAxis);
        goldPriceChart.setTitle("Historical & Projected Gold Prices");
        goldPriceChart.setCreateSymbols(false); // Smoother line without individual data point markers
        goldPriceChart.setLegendVisible(true);
        section.getChildren().addAll(sectionTitle, goldPriceChart);
        // Initial chart population
        updateGoldPriceChart(BusinessApp.get_startYear(), BusinessApp.get_endYear());
        return section;
    }

    private static List<DataPoint> fetchHistoricalGoldData(int startYear, int endYear) {
        List<DataPoint> result = new ArrayList<>();
        for (year = startYear; year <= endYear; year++) {
            DataPoint existing = goldPriceHistory.stream()
                    .filter(p -> p.year == year)
                    .findFirst()
                    .orElse(null);
            if (existing == null) {
                double price = calculateGoldPrice();
                existing = new DataPoint(year, price);
                goldPriceHistory.add(existing);
            }
            result.add(existing);
        }
        return result;
    }

    private static double calculateGoldPrice() {
        // Long-term drift (1–3%)
        double drift = 1.01 + goldRandom.nextDouble() * 0.02;
        // Volatility (market noise)
        double shock = goldRandom.nextGaussian() * 0.07;
        // Momentum (prevents zig-zag chaos)
        double momentum = (goldRandom.nextDouble() - 0.5) * 0.03;
        double nextPrice
                = lastGoldPrice * drift
                * (1 + shock + momentum);
        // Mean reversion (keeps it believable)
        double equilibrium = 1200;
        nextPrice += (equilibrium - nextPrice) * 0.05;
        // Safety floor
        nextPrice = Math.max(nextPrice, 25);
        lastGoldPrice = nextPrice;
        return Math.round(nextPrice * 100.0) / 100.0;
    }

    private static VBox create_AstronomicalBuckSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: #ecf0f1; -fx-background-radius: 5;");
        Label sectionTitle = new Label("Astronomical Bucks Analysis");
        sectionTitle.setStyle("fx-font-weight: bold;");
        sectionTitle.setFont(App.header_Font);
        // Gold price chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Year");
        yAxis.setLabel("Price per Astronomical Buck");
        yAxis.setAutoRanging(true);
        xAxis.setAutoRanging(false);
        astronomicalBuckPriceChart = new LineChart<>(xAxis, yAxis);
        astronomicalBuckPriceChart.setTitle("Historical & Projected per Astronomical Buck");
        astronomicalBuckPriceChart.setCreateSymbols(false); // Smoother line without individual data point markers
        astronomicalBuckPriceChart.setLegendVisible(true);
        section.getChildren().addAll(sectionTitle, astronomicalBuckPriceChart);
        // Initial chart population
        updateAstronomicalBuckChart(BusinessApp.get_startYear(), BusinessApp.get_endYear());
        return section;
    }

    private static void updateAstronomicalBuckChart(int startYear, int endYear) {
        if (startYear >= endYear) {
            showAlert("Invalid Range", "Start year must be less than end year.");
            return;
        }
        List<DataPoint> dataPoints = fetchHistoricalAstronomicalBuckData(startYear, endYear);
        astronomicalBuckPriceChart.getData().clear();
        // Separate historical and projected data
        List<DataPoint> historicalData = new ArrayList<>();
        List<DataPoint> projectedData = new ArrayList<>();
        for (DataPoint point : dataPoints) {
            if (point.year <= BusinessApp.get_currentYear()) {
                historicalData.add(point);
            } else {
                projectedData.add(point);
            }
        }
        astronomicalBuckPriceChart.setCreateSymbols(true);
        // Create historical series (solid line)
        if (!historicalData.isEmpty()) {
            XYChart.Series<Number, Number> historicalSeries = new XYChart.Series<>();
            historicalSeries.setName(String.format("Historical Price (>%s)", BusinessApp.get_currentYear()));
            for (DataPoint point : historicalData) {
                historicalSeries.getData().add(new XYChart.Data<>(point.year, point.price));
            }
            astronomicalBuckPriceChart.getData().add(historicalSeries);
        }
        // Create projected series (dashed line effect via styling)
        if (!projectedData.isEmpty()) {
            XYChart.Series<Number, Number> projectedSeries = new XYChart.Series<>();
            projectedSeries.setName(String.format("Projected Price (%s)", endYear));
            // Add last historical point to connect the lines
            if (!historicalData.isEmpty()) {
                DataPoint lastHistorical = historicalData.get(historicalData.size() - 1);
                projectedSeries.getData().add(new XYChart.Data<>(lastHistorical.year, lastHistorical.price));
            }
            for (DataPoint point : projectedData) {
                projectedSeries.getData().add(new XYChart.Data<>(point.year, point.price));
            }
            astronomicalBuckPriceChart.getData().add(projectedSeries);
        }
        // Update x-axis range
        NumberAxis xAxis = (NumberAxis) astronomicalBuckPriceChart.getXAxis();
        xAxis.setLowerBound(startYear);
        xAxis.setUpperBound(endYear);
        xAxis.setTickUnit(Math.max(1, (endYear - startYear) / 10));
    }

    private static List<DataPoint> fetchHistoricalAstronomicalBuckData(int startYear, int endYear) {
        List<DataPoint> result = new ArrayList<>();
        for (year = startYear; year <= endYear; year++) {
            DataPoint existing = astronomicalBuckHistory.stream()
                    .filter(p -> p.year == year)
                    .findFirst()
                    .orElse(null);
            if (existing == null) {
                double price = calculateAstronomicalBuckPrice();
                existing = new DataPoint(year, price);
                astronomicalBuckHistory.add(existing);
            }
            result.add(existing);
        }
        return result;
    }

    private static double calculateAstronomicalBuckPrice() {
        // Long-term drift (1–3%)
        double drift = 1.01 + astronomicalBuckRandom.nextDouble() * 0.02;
        // Volatility (market noise)
        double shock = astronomicalBuckRandom.nextGaussian() * 0.07;
        // Momentum (prevents zig-zag chaos)
        double momentum = (astronomicalBuckRandom.nextDouble() - 0.5) * 0.03;
        double nextPrice
                = lastAstronomicalBuckPrice * drift
                * (1 + shock + momentum);
        // Mean reversion (keeps it believable)
        double equilibrium = 1200 * 20;
        nextPrice += (equilibrium - nextPrice) * 0.05;
        // Safety floor
        nextPrice = Math.max(nextPrice, 25);
        lastAstronomicalBuckPrice = nextPrice;
        return Math.round(nextPrice * 100.0) / 100.0;
    }

    private static class DataPoint {
        final int year;
        final double price;

        DataPoint(int year, double price) {
            this.year = year;
            this.price = price;
        }
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
