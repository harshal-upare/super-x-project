package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminLeftSideBar {

    private static Button activeButton;

    public static List<Button> navigationButtons = new ArrayList<>();
    public static Button dashboardBtn;
    public static Button approvalsBtn;
    public static Button usersBtn;
    public static Button escrowBtn;
    public static Button disputesBtn;
    public static Button settingsBtn;

    public static StackPane root;

    public AdminLeftSideBar(AdminDashboard obj) {
        root = AdminDashboard.root;
    }

    public VBox getSideBar(Runnable ref) {
        navigationButtons.clear();

        Image logoImage;
        try {
            logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
            if (logoImage.isError()) {
                logoImage = new Image(getClass().getResourceAsStream("/assets/Images/logo.png"));
            }
        } catch (Exception e) {
            logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.jpeg");
        }

        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(30);
        logoImageView.setFitHeight(30);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        StackPane logoContainer = new StackPane(logoImageView);
        logoContainer.setPrefSize(42, 42);
        logoContainer.setMinSize(42, 42);
        logoContainer.setMaxSize(42, 42);
        logoContainer.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 10px;" +
                "-fx-border-width: 1px;");

        Text textName = new Text("FarmEquip");
        textName.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-fill: #1B4332;");

        Text roleBadge = new Text("🛡️ Admin Console");
        roleBadge.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-fill: #2D6A4F;");

        VBox brandBox = new VBox(1, textName, roleBadge);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        HBox logoTextHBox = new HBox(10, logoContainer, brandBox);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        logoTextHBox.setPadding(new Insets(4, 6, 16, 6));
        logoTextHBox.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        dashboardBtn = new Button("⌂   Master Overview");
        styleMenuButton(dashboardBtn);

        approvalsBtn = new Button("🚜  Machinery Approvals");
        styleMenuButton(approvalsBtn);

        usersBtn = new Button("👥  User & KYC Directory");
        styleMenuButton(usersBtn);

        escrowBtn = new Button("💰  Escrow & Financials");
        styleMenuButton(escrowBtn);

        disputesBtn = new Button("⚖   Dispute Resolution");
        styleMenuButton(disputesBtn);

        settingsBtn = new Button("⚙   Platform Settings");
        styleMenuButton(settingsBtn);

        VBox vBoxBtn1 = new VBox(6, dashboardBtn, approvalsBtn, usersBtn, escrowBtn, disputesBtn, settingsBtn);

        Button logoutBtn = new Button("↪   Logout");
        logoutBtn.setOnAction(e -> {
            if (ref != null) {
                ref.run();
            }
        });
        styleLogoutButton(logoutBtn);

        VBox vBoxBtn2 = new VBox(6, logoutBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox btnBox = new VBox(10, vBoxBtn1, spacer, vBoxBtn2);
        VBox.setVgrow(btnBox, Priority.ALWAYS);

        VBox leftVB = new VBox(logoTextHBox, btnBox);
        VBox.setVgrow(leftVB, Priority.ALWAYS);
        leftVB.setPrefWidth(250);
        leftVB.setMinWidth(250);
        leftVB.setMaxWidth(250);
        leftVB.setMaxHeight(Double.MAX_VALUE);
        leftVB.setSpacing(16);
        leftVB.setPadding(new Insets(20, 14, 20, 14));
        leftVB.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 0 1px 0 0;");

        navigationButtons.add(dashboardBtn);
        navigationButtons.add(approvalsBtn);
        navigationButtons.add(usersBtn);
        navigationButtons.add(escrowBtn);
        navigationButtons.add(disputesBtn);
        navigationButtons.add(settingsBtn);

        setActiveButton(dashboardBtn, navigationButtons);

        dashboardBtn.setOnAction(event -> {
            setActiveButton(dashboardBtn, navigationButtons);
            AdminProfileManagement.updateHeaderGreeting();
            AdminDashboard.borderPane.setCenter(AdminHome.getPage(root));
        });

        approvalsBtn.setOnAction(event -> {
            setActiveButton(approvalsBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("Machinery Approvals & Verification 🚜", "Audit provider machine RTO, insurance, and equipment health certificates");
            AdminDashboard.borderPane.setCenter(MachineryApprovals.getPage(root));
        });

        usersBtn.setOnAction(event -> {
            setActiveButton(usersBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("User & KYC Compliance Desk 👥", "Platform user registry, role auditing, and administrator quota management");
            AdminDashboard.borderPane.setCenter(UserManagement.getPage(root));
        });

        escrowBtn.setOnAction(event -> {
            setActiveButton(escrowBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("Escrow Vault & Settlement 💰", "Track automated booking holds, operator wage credits, and banking payout status");
            AdminDashboard.borderPane.setCenter(EscrowFinancials.getPage(root));
        });

        disputesBtn.setOnAction(event -> {
            setActiveButton(disputesBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("Dispute Resolution & Claims ⚖", "Arbitrate farm work order disputes, fuel adjustment claims, and refund cases");
            AdminDashboard.borderPane.setCenter(DisputeResolution.getPage(root));
        });

        settingsBtn.setOnAction(event -> {
            setActiveButton(settingsBtn, navigationButtons);
            AdminProfileManagement.setHeaderTitle("Platform System Settings ⚙", "Configure system security policies, commission fees, and admin quota controls");
            AdminDashboard.borderPane.setCenter(AdminSettings.getPage(root));
        });

        return leftVB;
    }

    public static void setActiveButton(Button selected, List<Button> buttons) {
        activeButton = selected;
        for (Button button : buttons) {
            if (button == null) continue;
            if (button == selected) {
                button.setStyle(
                        "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);");
            } else {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: 500;" +
                        "-fx-cursor: hand;");
            }
        }
    }

    private void styleMenuButton(Button button) {
        button.setPrefWidth(222);
        button.setMinWidth(222);
        button.setMaxWidth(222);
        button.setPrefHeight(44);
        button.setMinHeight(44);
        button.setMaxHeight(44);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 16, 0, 16));

        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10px;" +
                "-fx-text-fill: #374151;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: 500;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: #F0FDF4;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #1B4332;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #D1E7DD;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-width: 1px;");
            }
        });

        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: 500;" +
                        "-fx-cursor: hand;");
            }
        });
    }

    private void styleLogoutButton(Button button) {
        button.setPrefWidth(222);
        button.setMinWidth(222);
        button.setMaxWidth(222);
        button.setPrefHeight(42);
        button.setMinHeight(42);
        button.setMaxHeight(42);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 16, 0, 16));

        button.setStyle(
                "-fx-background-color: #FEF2F2;" +
                "-fx-background-radius: 10px;" +
                "-fx-text-fill: #DC2626;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #FEE2E2;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #B91C1C;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #FEF2F2;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #DC2626;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13.5px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;");
        });
    }
}
