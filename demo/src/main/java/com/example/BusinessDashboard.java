package com.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BusinessDashboard {
    private ObservableList<Business> businesses = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private TableView<Business> businessTable;
    private TableView<Transaction> transactionTable;
    private Label totalProfitLabel;
    private Label totalRevenueLabel;
    private Label totalExpensesLabel;
    private BarChart<String, Number> businessChart;
    private PieChart expenseChart;

    public Parent create_BusinessDashboard() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        VBox headerVBox = createHeader();
        borderPane.setTop(headerVBox);
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createDashboardTab(),
            createBusinessesTab(),
            createTransactionsTab(),
            createReportsTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        borderPane.setCenter(tabPane);
        return borderPane;
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 15;");

        Label title = new Label("Multi-Business Management System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        HBox metrics = new HBox(30);
        metrics.setAlignment(Pos.CENTER);
        
        totalRevenueLabel = createMetricLabel("Total Revenue", "$0");
        totalExpensesLabel = createMetricLabel("Total Expenses", "$0");
        totalProfitLabel = createMetricLabel("Total Profit", "$0");
        
        metrics.getChildren().addAll(totalRevenueLabel, totalExpensesLabel, totalProfitLabel);
        header.getChildren().addAll(title, metrics);
        
        updateMetrics();
        return header;
    }

    private Label createMetricLabel(String title, String value) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 12px;");
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #3498db; -fx-font-size: 20px; -fx-font-weight: bold;");
        box.getChildren().addAll(titleLabel, valueLabel);
        return valueLabel;
    }

    private Tab createDashboardTab() {
        Tab tab = new Tab("Dashboard");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        
        // Business Performance Chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        businessChart = new BarChart<>(xAxis, yAxis);
        businessChart.setTitle("Business Profit Comparison");
        businessChart.setPrefHeight(300);
        updateBusinessChart();
        
        // Expense Distribution Chart
        expenseChart = new PieChart();
        expenseChart.setTitle("Expense Distribution by Business");
        expenseChart.setPrefHeight(300);
        updateExpenseChart();
        
        content.getChildren().addAll(businessChart, expenseChart);
        
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        tab.setContent(scroll);
        return tab;
    }

    private Tab createBusinessesTab() {
        Tab tab = new Tab("Businesses");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        // Toolbar
        HBox toolbar = new HBox(10);
        Button addBtn = new Button("Add Business");
        addBtn.setOnAction(e -> showAddBusinessDialog());
        Button editBtn = new Button("Edit Business");
        editBtn.setOnAction(e -> editSelectedBusiness());
        Button deleteBtn = new Button("Delete Business");
        deleteBtn.setOnAction(e -> deleteSelectedBusiness());
        toolbar.getChildren().addAll(addBtn, editBtn, deleteBtn);
        
        // Business Table
        businessTable = new TableView<>();
        businessTable.setItems(businesses);
        
        TableColumn<Business, String> nameCol = new TableColumn<>("Business Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        
        TableColumn<Business, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(150);
        
        TableColumn<Business, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(150);
        
        TableColumn<Business, Double> revenueCol = new TableColumn<>("Revenue");
        revenueCol.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        revenueCol.setPrefWidth(120);
        
        TableColumn<Business, Double> expensesCol = new TableColumn<>("Expenses");
        expensesCol.setCellValueFactory(new PropertyValueFactory<>("expenses"));
        expensesCol.setPrefWidth(120);
        
        TableColumn<Business, Double> profitCol = new TableColumn<>("Profit");
        profitCol.setCellValueFactory(new PropertyValueFactory<>("profit"));
        profitCol.setPrefWidth(120);
        
        businessTable.getColumns().addAll(nameCol, typeCol, locationCol, revenueCol, expensesCol, profitCol);
        
        content.getChildren().addAll(toolbar, businessTable);
        tab.setContent(content);
        return tab;
    }

    private Tab createTransactionsTab() {
        Tab tab = new Tab("Transactions");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        // Toolbar
        HBox toolbar = new HBox(10);
        Button addBtn = new Button("Add Transaction");
        addBtn.setOnAction(e -> showAddTransactionDialog());
        Button deleteBtn = new Button("Delete Transaction");
        deleteBtn.setOnAction(e -> deleteSelectedTransaction());
        toolbar.getChildren().addAll(addBtn, deleteBtn);
        
        // Transaction Table
        transactionTable = new TableView<>();
        transactionTable.setItems(transactions);
        
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(120);
        
        TableColumn<Transaction, String> businessCol = new TableColumn<>("Business");
        businessCol.setCellValueFactory(new PropertyValueFactory<>("businessName"));
        businessCol.setPrefWidth(200);
        
        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(100);
        
        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(120);
        
        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);
        
        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(250);
        
        transactionTable.getColumns().addAll(dateCol, businessCol, typeCol, amountCol, categoryCol, descCol);
        
        content.getChildren().addAll(toolbar, transactionTable);
        tab.setContent(content);
        return tab;
    }

    private Tab createReportsTab() {
        Tab tab = new Tab("Reports");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        
        Label title = new Label("Business Performance Summary");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefRowCount(20);
        
        Button generateBtn = new Button("Generate Report");
        generateBtn.setOnAction(e -> {
            reportArea.setText(generateReport());
        });
        
        content.getChildren().addAll(title, generateBtn, reportArea);
        tab.setContent(content);
        return tab;
    }

    private void showAddBusinessDialog() {
        Dialog<Business> dialog = new Dialog<>();
        dialog.setTitle("Add New Business");
        dialog.setHeaderText("Enter business details");
        
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextField typeField = new TextField();
        TextField locationField = new TextField();
        TextField revenueField = new TextField("0");
        TextField expensesField = new TextField("0");
        
        grid.add(new Label("Business Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Location:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Revenue:"), 0, 3);
        grid.add(revenueField, 1, 3);
        grid.add(new Label("Expenses:"), 0, 4);
        grid.add(expensesField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    return new Business(
                        nameField.getText(),
                        typeField.getText(),
                        locationField.getText(),
                        Double.parseDouble(revenueField.getText()),
                        Double.parseDouble(expensesField.getText())
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
        
        Optional<Business> result = dialog.showAndWait();
        result.ifPresent(business -> {
            businesses.add(business);
            updateMetrics();
            updateBusinessChart();
            updateExpenseChart();
        });
    }

    private void editSelectedBusiness() {
        Business selected = businessTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a business to edit.");
            return;
        }
        
        Dialog<Business> dialog = new Dialog<>();
        dialog.setTitle("Edit Business");
        dialog.setHeaderText("Edit business details");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(selected.getName());
        TextField typeField = new TextField(selected.getType());
        TextField locationField = new TextField(selected.getLocation());
        TextField revenueField = new TextField(String.valueOf(selected.getRevenue()));
        TextField expensesField = new TextField(String.valueOf(selected.getExpenses()));
        
        grid.add(new Label("Business Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Location:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Revenue:"), 0, 3);
        grid.add(revenueField, 1, 3);
        grid.add(new Label("Expenses:"), 0, 4);
        grid.add(expensesField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    selected.setName(nameField.getText());
                    selected.setType(typeField.getText());
                    selected.setLocation(locationField.getText());
                    selected.setRevenue(Double.parseDouble(revenueField.getText()));
                    selected.setExpenses(Double.parseDouble(expensesField.getText()));
                    return selected;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
        
        Optional<Business> result = dialog.showAndWait();
        result.ifPresent(business -> {
            businessTable.refresh();
            updateMetrics();
            updateBusinessChart();
            updateExpenseChart();
        });
    }

    private void deleteSelectedBusiness() {
        Business selected = businessTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a business to delete.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Business");
        alert.setContentText("Are you sure you want to delete " + selected.getName() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            businesses.remove(selected);
            updateMetrics();
            updateBusinessChart();
            updateExpenseChart();
        }
    }

    private void showAddTransactionDialog() {
        if (businesses.isEmpty()) {
            showAlert("No Businesses", "Please add at least one business first.");
            return;
        }
        
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText("Enter transaction details");
        
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<Business> businessCombo = new ComboBox<>(businesses);
        businessCombo.setValue(businesses.get(0));
        ComboBox<String> typeCombo = new ComboBox<>(FXCollections.observableArrayList("Revenue", "Expense"));
        typeCombo.setValue("Revenue");
        TextField amountField = new TextField();
        TextField categoryField = new TextField();
        TextField descField = new TextField();
        
        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Business:"), 0, 1);
        grid.add(businessCombo, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeCombo, 1, 2);
        grid.add(new Label("Amount:"), 0, 3);
        grid.add(amountField, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(categoryField, 1, 4);
        grid.add(new Label("Description:"), 0, 5);
        grid.add(descField, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    Business business = businessCombo.getValue();
                    double amount = Double.parseDouble(amountField.getText());
                    String type = typeCombo.getValue();
                    
                    if (type.equals("Revenue")) {
                        business.setRevenue(business.getRevenue() + amount);
                    } else {
                        business.setExpenses(business.getExpenses() + amount);
                    }
                    
                    return new Transaction(
                        datePicker.getValue().toString(),
                        business.getName(),
                        type,
                        amount,
                        categoryField.getText(),
                        descField.getText()
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
        
        Optional<Transaction> result = dialog.showAndWait();
        result.ifPresent(transaction -> {
            transactions.add(transaction);
            businessTable.refresh();
            updateMetrics();
            updateBusinessChart();
            updateExpenseChart();
        });
    }

    private void deleteSelectedTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a transaction to delete.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Transaction");
        alert.setContentText("Are you sure you want to delete this transaction?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            transactions.remove(selected);
            updateMetrics();
        }
    }

    private void updateMetrics() {
        double totalRevenue = businesses.stream().mapToDouble(Business::getRevenue).sum();
        double totalExpenses = businesses.stream().mapToDouble(Business::getExpenses).sum();
        double totalProfit = totalRevenue - totalExpenses;
        
        totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));
        totalExpensesLabel.setText(String.format("$%.2f", totalExpenses));
        totalProfitLabel.setText(String.format("$%.2f", totalProfit));
        
        if (totalProfit >= 0) {
            totalProfitLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 20px; -fx-font-weight: bold;");
        } else {
            totalProfitLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 20px; -fx-font-weight: bold;");
        }
    }

    private void updateBusinessChart() {
        businessChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Profit");
        
        for (Business b : businesses) {
            series.getData().add(new XYChart.Data<>(b.getName(), b.getProfit()));
        }
        
        businessChart.getData().add(series);
    }

    private void updateExpenseChart() {
        expenseChart.getData().clear();
        
        for (Business b : businesses) {
            if (b.getExpenses() > 0) {
                PieChart.Data data = new PieChart.Data(b.getName(), b.getExpenses());
                expenseChart.getData().add(data);
            }
        }
    }

    private String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("BUSINESS PERFORMANCE REPORT\n");
        sb.append("Generated: ").append(LocalDate.now().format(DateTimeFormatter.ISO_DATE)).append("\n");
        sb.append("=".repeat(60)).append("\n\n");
        
        double totalRevenue = 0, totalExpenses = 0;
        
        for (Business b : businesses) {
            sb.append("Business: ").append(b.getName()).append("\n");
            sb.append("Type: ").append(b.getType()).append("\n");
            sb.append("Location: ").append(b.getLocation()).append("\n");
            sb.append(String.format("Revenue: $%.2f\n", b.getRevenue()));
            sb.append(String.format("Expenses: $%.2f\n", b.getExpenses()));
            sb.append(String.format("Profit: $%.2f\n", b.getProfit()));
            sb.append("-".repeat(60)).append("\n\n");
            
            totalRevenue += b.getRevenue();
            totalExpenses += b.getExpenses();
        }
        
        sb.append("OVERALL SUMMARY\n");
        sb.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
        sb.append(String.format("Total Expenses: $%.2f\n", totalExpenses));
        sb.append(String.format("Total Profit: $%.2f\n", totalRevenue - totalExpenses));
        sb.append(String.format("Profit Margin: %.2f%%\n", 
            totalRevenue > 0 ? ((totalRevenue - totalExpenses) / totalRevenue * 100) : 0));
        
        return sb.toString();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void initializeSampleData() {
        businesses.add(new Business("Tech Solutions Inc", "Technology", "San Francisco", 250000, 120000));
        businesses.add(new Business("Green Grocers", "Retail", "Portland", 180000, 95000));
        businesses.add(new Business("Cafe Delight", "Food & Beverage", "Seattle", 95000, 68000));
        
        transactions.add(new Transaction("2025-01-01", "Tech Solutions Inc", "Revenue", 50000, "Sales", "Q4 Software Licenses"));
        transactions.add(new Transaction("2025-01-02", "Green Grocers", "Expense", 15000, "Inventory", "Fresh Produce"));
        transactions.add(new Transaction("2025-01-03", "Cafe Delight", "Revenue", 12000, "Sales", "Weekly Revenue"));
    }

    private String getStyles() {
        return ".table-view { -fx-background-color: white; }" +
               ".button { -fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15; }" +
               ".button:hover { -fx-background-color: #2980b9; }";
    }

    public static class Business {
        private final StringProperty name;
        private final StringProperty type;
        private final StringProperty location;
        private final DoubleProperty revenue;
        private final DoubleProperty expenses;
        
        public Business(String name, String type, String location, double revenue, double expenses) {
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.location = new SimpleStringProperty(location);
            this.revenue = new SimpleDoubleProperty(revenue);
            this.expenses = new SimpleDoubleProperty(expenses);
        }
        
        public String getName() { return name.get(); }
        public void setName(String value) { name.set(value); }
        public StringProperty nameProperty() { return name; }
        
        public String getType() { return type.get(); }
        public void setType(String value) { type.set(value); }
        public StringProperty typeProperty() { return type; }
        
        public String getLocation() { return location.get(); }
        public void setLocation(String value) { location.set(value); }
        public StringProperty locationProperty() { return location; }
        
        public double getRevenue() { return revenue.get(); }
        public void setRevenue(double value) { revenue.set(value); }
        public DoubleProperty revenueProperty() { return revenue; }
        
        public double getExpenses() { return expenses.get(); }
        public void setExpenses(double value) { expenses.set(value); }
        public DoubleProperty expensesProperty() { return expenses; }
        
        public double getProfit() { return getRevenue() - getExpenses(); }
        
        @Override
        public String toString() { return name.get(); }
    }

    public static class Transaction {
        private final StringProperty date;
        private final StringProperty businessName;
        private final StringProperty type;
        private final DoubleProperty amount;
        private final StringProperty category;
        private final StringProperty description;
        
        public Transaction(String date, String businessName, String type, double amount, String category, String description) {
            this.date = new SimpleStringProperty(date);
            this.businessName = new SimpleStringProperty(businessName);
            this.type = new SimpleStringProperty(type);
            this.amount = new SimpleDoubleProperty(amount);
            this.category = new SimpleStringProperty(category);
            this.description = new SimpleStringProperty(description);
        }
        
        public String getDate() { return date.get(); }
        public StringProperty dateProperty() { return date; }
        
        public String getBusinessName() { return businessName.get(); }
        public StringProperty businessNameProperty() { return businessName; }
        
        public String getType() { return type.get(); }
        public StringProperty typeProperty() { return type; }
        
        public double getAmount() { return amount.get(); }
        public DoubleProperty amountProperty() { return amount; }
        
        public String getCategory() { return category.get(); }
        public StringProperty categoryProperty() { return category; }
        
        public String getDescription() { return description.get(); }
        public StringProperty descriptionProperty() { return description; }
    }
}