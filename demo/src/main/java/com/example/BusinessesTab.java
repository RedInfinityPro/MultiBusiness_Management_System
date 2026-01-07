package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BusinessesTab {
    private static Tools tools = new Tools();
    private static String[] filterList = {"Businesses name: [A-Z]", "Businesses Name: [Z-A]", "Manager Name: [A-Z]", "Manager Name: [Z-A]", "Revenue Output: ^", "Revenue Output: v", "Revenue Output Time: ^", "Revenue Output Time: V", "Locked State: On", "Locked State: Off", "Cost To By: ^", "Cost To By: v", "Expenses Output: ^", "Expenses Output: v", "Upgrade Cost: ^", "Upgrade Cost: v", "Quality Cost: ^", "Quality Cost: v"};

    public static Tab create_BusinessesTab() {
        Tab tab = new Tab("Businesses");
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(15));
        // sear bar
        HBox searchHbox = new HBox(10);
        TextField searchBarField = new TextField();
        searchBarField.setPromptText("Search...");
        ComboBox filterComboBox = new ComboBox(observableArrayList(filterList));
        filterComboBox.setPromptText("Filter...");
        searchHbox.getChildren().addAll(searchBarField, filterComboBox);
        // content
        VBox content = new VBox(15);
        content.setPadding(new Insets(0, 0, 15, 0));
        int max = 100;
        int min = 1;
        int random = (int) ((Math.random() * (max - min)) + min);
        for (int i = 0; i < random; i++) {
            VBox businessBox = new BusinessBox().getView();
            content.getChildren().add(businessBox);
        }
        // Scroll pane
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        mainContainer.getChildren().addAll(searchHbox, scroll);
        tab.setContent(mainContainer);
        return tab;
    }

    // classes
    private static class BusinessBox {

        private final VBox root = new VBox(8);
        private final ImageView businessImage;
        private Timeline progressTimeline;
        private final Organization organization;
        private ProgressBar progressBar;

        BusinessBox() {
            this.organization = new Organization(tools.GenerateName(), null, true, tools.LargeNumber_RandomGenerator(), tools.Random_InRange(60, 100), 0.0, tools.LargeNumber_RandomGenerator(), tools.LargeNumber_RandomGenerator(), tools.LargeNumber_RandomGenerator(), tools.LargeNumber_RandomGenerator());
            businessImage = new ImageView(new Image("https://picsum.photos/800/600", true));
            businessImage.setFitWidth(100);
            businessImage.setFitHeight(100);
            businessImage.setPreserveRatio(true);
            configureBox();
            updateDisplay();
        }

        VBox getView() {
            return root;
        }

        private void configureBox() {
            root.setPadding(new Insets(10));
            root.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: white;");
            root.setOnMouseClicked(e -> {
                if (organization.isLocked()) {
                    attemptUnlock();
                } else if (!organization.isInProgress()) {
                    startProductionCycle();
                }
            });
            root.setOnMouseEntered(e -> {
                root.setStyle("-fx-border-color: #3498db; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: #ecf0f1; -fx-cursor: hand;");
            });
            root.setOnMouseExited(e -> {
                root.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: white;");
            });
        }

        private void attemptUnlock() {
            double unlockCost = organization.getOrganizationValue();
            if (BusinessDashboard.get_currentMoney() >= unlockCost) {
                BusinessDashboard.subtract_currentMoney(unlockCost);
                organization.setLocked(false);
                updateDisplay();
            }
        }

        private void startProductionCycle() {
            organization.setInProgress(true);
            organization.setProgress(0);
            progressTimeline = new Timeline(
                    new KeyFrame(Duration.millis(0), e -> progressBar.setProgress(0)),
                    new KeyFrame(Duration.millis(organization.getOrganizationOutputTime()), e -> {
                        progressBar.setProgress(1);
                        organization.setInProgress(false);
                        updateDisplay();
                    })
            );
            progressTimeline.currentTimeProperty().addListener((obs, old, now) -> {
                progressBar.setProgress(
                        now.toMillis() / organization.getOrganizationOutputTime()
                );
            });
            progressTimeline.play();
            BusinessDashboard.add_currentMoney(organization.getRevenueOutput());
            updateDisplay(progressBar);
        }

        private void updateDisplay() {
            updateDisplay(null);
        }

        private void updateDisplay(ProgressBar activeProgress) {
            root.getChildren().clear();
            HBox header = new HBox(20,
                    new Label("Business: " + organization.getName()),
                    new Label("Manager: " + organization.getManager())
            );
            VBox infoBox = new VBox(8);
            HBox timeBox = new HBox(10,
                    new Label("Revenue Value: $" + tools.Format_Number(organization.getRevenueOutput())),
                    new Label("Per: " + tools.Format_Number(organization.getOrganizationOutputTime()) + " ms")
            );

            HBox statusBox = new HBox(10);
            if (organization.isLocked()) {
                statusBox.getChildren().add(new Label("Locked | Cost to buy: $" + tools.Format_Number(organization.getOrganizationValue())));
            } else {
                ProgressBar displayProgress = (organization.isInProgress() && activeProgress != null) ? activeProgress : new ProgressBar(0);
                displayProgress.setOnMouseClicked(e -> {
                    if (organization.isLocked()) {
                        attemptUnlock();
                    } else if (!organization.isInProgress()) {
                        startProductionCycle();
                    }
                });
                if (!organization.isInProgress()) {
                    progressBar = displayProgress;
                }
                displayProgress.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(displayProgress, Priority.ALWAYS);
                // Button container
                VBox buttonBox = new VBox(5);
                Button upgradeButton = new Button("Upgrade ($" + tools.Format_Number(organization.getUpgradeCost()) + ")");
                upgradeButton.setMaxWidth(Double.MAX_VALUE);
                upgradeButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                upgradeButton.setOnAction(e -> {
                    if (BusinessDashboard.get_currentMoney() >= organization.getUpgradeCost()) {
                        BusinessDashboard.subtract_currentMoney(organization.getUpgradeCost());
                        // Add upgrade logic here
                        System.out.println("Upgraded: " + organization.getName());
                    }
                });
                Button qualityButton = new Button("Quality ($" + tools.Format_Number(organization.getQualityCost()) + ")");
                qualityButton.setMaxWidth(Double.MAX_VALUE);
                qualityButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
                qualityButton.setOnAction(e -> {
                    if (BusinessDashboard.get_currentMoney() >= organization.getQualityCost()) {
                        BusinessDashboard.subtract_currentMoney(organization.getQualityCost());
                        // Add quality improvement logic here
                        System.out.println("Quality improved: " + organization.getName());
                    }
                });
                buttonBox.getChildren().addAll(upgradeButton, qualityButton);
                // Bind progress bar height to button box height
                displayProgress.prefHeightProperty().bind(buttonBox.heightProperty());
                statusBox.getChildren().addAll(displayProgress, buttonBox);
            }
            infoBox.getChildren().addAll(timeBox, statusBox);
            HBox.setHgrow(infoBox, Priority.ALWAYS);
            HBox main = new HBox(10, businessImage, infoBox);
            root.getChildren().addAll(header, main);
        }
    }

    public static class Organization {
        private StringProperty name;
        private StringProperty manager;
        private SimpleBooleanProperty locked;
        private SimpleDoubleProperty organizationValue;
        private SimpleDoubleProperty organizationOutputTime;
        private SimpleDoubleProperty organizationCurrentTime;
        private SimpleDoubleProperty revenueOutput;
        private SimpleDoubleProperty expensesOutput;
        private SimpleDoubleProperty upgradeCost;
        private SimpleDoubleProperty qualityCost;
        private boolean inProgress;
        private double progress;

        Organization(String name, String manager, Boolean locked, double organizationValue, double organizationOutputTime, double organizationCurrentTime, double revenueOutput, double expensesOutput, double upgradeCost, double qualityCost) {
            this.name = new SimpleStringProperty(name);
            this.manager = new SimpleStringProperty(manager);
            this.locked = new SimpleBooleanProperty(locked);
            this.organizationValue = new SimpleDoubleProperty(organizationValue);
            this.organizationOutputTime = new SimpleDoubleProperty(organizationOutputTime);
            this.organizationCurrentTime = new SimpleDoubleProperty(organizationCurrentTime);
            this.revenueOutput = new SimpleDoubleProperty(revenueOutput);
            this.expensesOutput = new SimpleDoubleProperty(expensesOutput);
            this.upgradeCost = new SimpleDoubleProperty(upgradeCost);
            this.qualityCost = new SimpleDoubleProperty(qualityCost);
        }

        public String getName() {
            return name.get();
        }

        public String getManager() {
            return manager.get();
        }

        public boolean isLocked() {
            return locked.get();
        }

        public void setLocked(boolean lockState) {
            this.locked.set(lockState);
        }

        public double getOrganizationValue() {
            return organizationValue.get();
        }

        public double getOrganizationOutputTime() {
            return organizationOutputTime.get();
        }

        public double getOrganizationCurrentTime() {
            return organizationCurrentTime.get();
        }

        public double getRevenueOutput() {
            return revenueOutput.get();
        }

        public double getExpensesOutput() {
            return expensesOutput.get();
        }

        public double getUpgradeCost() {
            return upgradeCost.get();
        }

        public double getQualityCost() {
            return qualityCost.get();
        }

        public boolean isInProgress() {
            return inProgress;
        }

        public void setInProgress(boolean inProgress) {
            this.inProgress = inProgress;
        }

        public void setProgress(double progress) {
            this.progress = progress;
        }

        public double getProgress() {
            return progress;
        }

        @Override
        public String toString() {
            return name.get();
        }
    }
}
