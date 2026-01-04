package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BusinessesTab {
    private static Tools tools = new Tools();

    public static class Organization {

        private final StringProperty name;
        private final StringProperty manager;
        private final SimpleBooleanProperty locked;
        private final SimpleDoubleProperty organizationValue;
        private final SimpleDoubleProperty organizationOutputTime;
        private final SimpleDoubleProperty organizationCurrentTime;
        private final SimpleDoubleProperty revenueOutput;
        private final SimpleDoubleProperty expensesOutput;
        private final SimpleDoubleProperty upgradeCost;
        private final SimpleDoubleProperty qualityCost;
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

    public static Tab createBusinessesTab() {
        Tab tab = new Tab("Businesses");
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        // content
        int max = 100;
        int min = 1;
        int random = (int) ((Math.random() * (max - min)) + min);
        for (int i = 0; i < random; i++) {
            VBox businessBox = new BusinessBox().getView();
            content.getChildren().add(businessBox);
        }
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        tab.setContent(scroll);
        return tab;
    }

    private static class BusinessBox {
        private final VBox root = new VBox(8);
        private final ImageView businessImage;
        private Timeline progressTimeline;
        private final Organization organization;
        private ProgressBar progressBar;

        BusinessBox() {
            
            this.organization = new Organization(tools.GenerateName(), "Manager Name", true, tools.LargeNumber_RandomGenerator(), tools.LargeNumber_RandomGenerator(), 0.0, tools.LargeNumber_RandomGenerator(), tools.LargeNumber_RandomGenerator(), tools.LargeNumber_RandomGenerator(), tools.LargeNumber_RandomGenerator());
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
            root.setStyle("-fx-border-color: gray; -fx-border-width: 1;");

            root.setOnMouseClicked(e -> {
                if (organization.isLocked()) {
                    attemptUnlock();
                } else if (!organization.isInProgress()) {
                    startProductionCycle();
                }
            });
        }

        private void attemptUnlock() {
            double unlockCost = organization.getOrganizationValue();
            if (BusinessDashboard.currentMoney >= unlockCost) {
                BusinessDashboard.currentMoney -= unlockCost;
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
            BusinessDashboard.currentMoney += organization.getRevenueOutput();
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
                    new Label("Value: $" + tools.Format_Number(organization.getRevenueOutput())),
                    new Label("Per: " + tools.Format_Number(organization.getOrganizationOutputTime()) + " ms")
            );
            HBox statusBox = new HBox(10);
            if (organization.isLocked()) {
                statusBox.getChildren().add(new Label("Locked"));
            } else if (organization.isInProgress() && activeProgress != null) {
                activeProgress.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(activeProgress, Priority.ALWAYS);
                statusBox.getChildren().add(activeProgress);
            } else {
                progressBar = new ProgressBar(0);
                statusBox.getChildren().add(progressBar);
            }
            infoBox.getChildren().addAll(timeBox, statusBox);
            HBox main = new HBox(10, businessImage, infoBox);
            root.getChildren().addAll(header, main);
        }
    }
}
