package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorLeftSideBar {

    private static Button activeButton;

    public static List<Button> navigationButtons = new ArrayList<>();
    public static Button dashboardBtn;
    public static Button machineryBtn;
    public static Button jobsBtn;
    public static Button fieldLogsBtn;
    public static Button earningsBtn;
    public static Button maintenanceBtn;
    public static Button analyticsBtn;
    public static Button settingsBtn;
    public static Button supportBtn;

    public static StackPane root;

    public OperatorLeftSideBar(OperatorDashboard obj) {
        root = OperatorDashboard.root;
    }

    public VBox getSideBar(Runnable ref) {
        navigationButtons.clear();

        Image logoImage = null;
        try {
            logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
        } catch (Exception e) {
            // fallback
        }

        ImageView logoImageView;
        if (logoImage != null && !logoImage.isError()) {
            logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(44);
            logoImageView.setFitHeight(44);
            logoImageView.setPreserveRatio(false);
            logoImageView.setSmooth(true);
        } else {
            logoImageView = new ImageView();
            logoImageView.setFitWidth(44);
            logoImageView.setFitHeight(44);
        }

        Text textName = new Text("FarmEquip");
        textName.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-font-family: 'Poppins';"
                + "-fx-fill: #4A2C20;");

        Text roleBadge = new Text("OPERATOR PORTAL");
        roleBadge.setStyle("-fx-font-size: 9px;" + "-fx-font-weight: bold;" + "-fx-font-family: 'Poppins';"
                + "-fx-fill: #8B6F47;" + "-fx-letter-spacing: 1px;");

        VBox brandBox = new VBox(1, textName, roleBadge);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        dashboardBtn = new Button("⌂   Dashboard");
        styleMenuButton(dashboardBtn);

        machineryBtn = new Button("🚜  Assigned Machinery");
        styleMenuButton(machineryBtn);

        jobsBtn = new Button("📋  Field Jobs & Schedule");
        styleMenuButton(jobsBtn);

        fieldLogsBtn = new Button("⏱  Field Logs & Hours");
        styleMenuButton(fieldLogsBtn);

        earningsBtn = new Button("💰  Wages & Earnings");
        styleMenuButton(earningsBtn);

        maintenanceBtn = new Button("🛠  Maintenance & SOS");
        styleMenuButton(maintenanceBtn);

        analyticsBtn = new Button("📊  Performance & Safety");
        styleMenuButton(analyticsBtn);

        VBox vBoxBtn1 = new VBox(8,
            new HBox(dashboardBtn),
            new HBox(machineryBtn),
            new HBox(jobsBtn),
            new HBox(fieldLogsBtn),
            new HBox(earningsBtn),
            new HBox(maintenanceBtn),
            new HBox(analyticsBtn)
        );

        settingsBtn = new Button("⚙   Settings");
        styleMenuButton(settingsBtn);

        supportBtn = new Button("❓  Help & Field SOS");
        styleMenuButton(supportBtn);

        Button logoutBtn = new Button("↪   Logout");
        logoutBtn.setOnAction(e -> {
            if (ref != null) {
                ref.run();
            }
        });
        styleLogoutButton(logoutBtn);

        VBox vBoxBtn2 = new VBox(8, new HBox(settingsBtn), new HBox(supportBtn), new HBox(logoutBtn));

        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        VBox btnBox = new VBox(10, vBoxBtn1, spacer, vBoxBtn2);

        HBox logoTextHBox = new HBox(10, logoImageView, brandBox);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        logoTextHBox.setPadding(new Insets(0, 5, 10, 5));

        VBox leftVB = new VBox(logoTextHBox, btnBox);
        leftVB.setPrefWidth(260);
        leftVB.setMinWidth(260);
        leftVB.setMaxWidth(260);
        leftVB.setMaxHeight(Double.MAX_VALUE);
        leftVB.setSpacing(20);
        leftVB.setPadding(new Insets(25, 18, 25, 18));
        leftVB.setStyle("-fx-background-color: #F5EFE6;" + "-fx-background-radius: 15;"
                + "-fx-border-color: #D8C7B5;" + "-fx-border-width: 1;" + "-fx-border-radius: 15;");

        navigationButtons.add(dashboardBtn);
        navigationButtons.add(machineryBtn);
        navigationButtons.add(jobsBtn);
        navigationButtons.add(fieldLogsBtn);
        navigationButtons.add(earningsBtn);
        navigationButtons.add(maintenanceBtn);
        navigationButtons.add(analyticsBtn);
        navigationButtons.add(settingsBtn);
        navigationButtons.add(supportBtn);

        setActiveButton(dashboardBtn, navigationButtons);

        // Sidebar Navigation Actions
        dashboardBtn.setOnAction(event -> {
            setActiveButton(dashboardBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorHome.getPage());
        });

        machineryBtn.setOnAction(event -> {
            setActiveButton(machineryBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorMachinery.getMachinerySection(root));
        });

        jobsBtn.setOnAction(event -> {
            setActiveButton(jobsBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorJobs.getJobsSection(root));
        });

        fieldLogsBtn.setOnAction(event -> {
            setActiveButton(fieldLogsBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorFieldLogs.getLogsSection(root));
        });

        earningsBtn.setOnAction(event -> {
            setActiveButton(earningsBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorEarnings.getEarningsSection(root));
        });

        maintenanceBtn.setOnAction(event -> {
            setActiveButton(maintenanceBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorMaintenance.getMaintenanceSection(root));
        });

        analyticsBtn.setOnAction(event -> {
            setActiveButton(analyticsBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorAnalytics.getAnalyticsSection());
        });

        settingsBtn.setOnAction(event -> {
            setActiveButton(settingsBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorSettings.getSettingsSection());
        });

        supportBtn.setOnAction(event -> {
            setActiveButton(supportBtn, navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorHelp.getHelpSection());
        });

        return leftVB;
    }

    public static void setActiveButton(Button selected, List<Button> buttons) {
        activeButton = selected;
        for (Button button : buttons) {
            button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-text-fill: #5C4033;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: normal;" +
                    "-fx-cursor: hand;");
        }

        if (selected != null) {
            selected.setStyle(
                    "-fx-background-color: #E4D3C2;" +
                    "-fx-background-radius: 10;" +
                    "-fx-text-fill: #4A2C20;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;");
        }
    }

    private void styleMenuButton(Button button) {
        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(44);
        button.setMinHeight(44);
        button.setMaxHeight(44);

        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 15, 0, 15));

        // Normal style
        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: normal;" +
                "-fx-cursor: hand;");

        // Hover
        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: #E4D3C2;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;");
            }
        });

        // Mouse leaves
        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #5C4033;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: normal;" +
                        "-fx-cursor: hand;");
            }
        });
    }

    private void styleLogoutButton(Button button) {
        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(44);
        button.setMinHeight(44);
        button.setMaxHeight(44);

        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 15, 0, 15));

        button.setFont(javafx.scene.text.Font.font("Poppins", 13.5));
        button.setTextFill(javafx.scene.paint.Color.web("#FFFFFF"));

        button.setStyle(
                "-fx-background-color: #8B3A3A;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #A94442;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #8B3A3A;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;");
        });
    }
}
