package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserManagement {

    public static class UserItem {
        public String userId;
        public String name;
        public String role; // "FARMER", "PROVIDER", "OPERATOR"
        public String phone;
        public String location;
        public String kycStatus; // "VERIFIED", "PENDING"
        public String accountStatus; // "ACTIVE", "SUSPENDED"
        public String stats;

        public UserItem(String userId, String name, String role, String phone, String location, String kycStatus, String accountStatus, String stats) {
            this.userId = userId;
            this.name = name;
            this.role = role;
            this.phone = phone;
            this.location = location;
            this.kycStatus = kycStatus;
            this.accountStatus = accountStatus;
            this.stats = stats;
        }
    }

    private static List<UserItem> userList = new ArrayList<>();
    private static VBox listContainer;
    private static String activeRoleFilter = "ALL";
    private static String searchFilter = "";

    static {
        initUsers();
    }

    private static void initUsers() {
        if (!userList.isEmpty()) return;
        userList.add(new UserItem("USR-F-1021", "Balasaheb Shirole", "FARMER", "+91 98229 11029", "Baramati, Pune", "VERIFIED", "ACTIVE", "14 Rentals • ₹1.68L Volume"));
        userList.add(new UserItem("USR-P-2041", "Rajesh Patil (Agro Services)", "PROVIDER", "+91 98220 12345", "Pune Central", "VERIFIED", "ACTIVE", "9 Machines • ₹4.85L Earned"));
        userList.add(new UserItem("USR-O-3012", "Ramesh Chavan", "OPERATOR", "+91 98901 44552", "Baramati", "VERIFIED", "ACTIVE", "4.9 ★ • 148 Engine Hrs"));
        userList.add(new UserItem("USR-F-1088", "Anand Kadam", "FARMER", "+91 94230 78119", "Indapur", "VERIFIED", "ACTIVE", "6 Rentals • ₹82,000 Vol"));
        userList.add(new UserItem("USR-P-2099", "Vikas More (AgriFleet)", "PROVIDER", "+91 98502 11234", "Indapur", "PENDING", "ACTIVE", "4 Machines • ₹1.20L Earned"));
        userList.add(new UserItem("USR-O-3045", "Dilip Shinde", "OPERATOR", "+91 97632 99401", "Saswad", "PENDING", "ACTIVE", "4.8 ★ • Heavy Harvester"));
    }

    public static ScrollPane getPage(StackPane root) {
        Text title = new Text("User Directory & KYC Compliance Desk");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Manage platform farmers, equipment providers, and certified field operators. Review KYC and moderate accounts.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(3, title, subtitle);

        // Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search user by Name, Mobile, Role or District...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(380);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            searchFilter = newV.trim().toLowerCase();
            renderUsers(root);
        });

        // Filter Pills
        HBox roleTabs = createRoleTabs(root);

        listContainer = new VBox(12);
        listContainer.setMinWidth(0);
        renderUsers(root);

        VBox content = new VBox(18, titleBox, searchField, roleTabs, listContainer);
        content.setPadding(new Insets(20, 25, 35, 25));
        content.setMinWidth(0);
        content.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createRoleTabs(StackPane root) {
        Button bAll = new Button("All (1,284)");
        Button bFarmers = new Button("Farmers (840)");
        Button bProviders = new Button("Providers (320)");
        Button bOperators = new Button("Operators (124)");

        styleTab(bAll, "ALL".equals(activeRoleFilter));
        styleTab(bFarmers, "FARMER".equals(activeRoleFilter));
        styleTab(bProviders, "PROVIDER".equals(activeRoleFilter));
        styleTab(bOperators, "OPERATOR".equals(activeRoleFilter));

        bAll.setOnAction(e -> { activeRoleFilter = "ALL"; update(bAll, bFarmers, bProviders, bOperators); renderUsers(root); });
        bFarmers.setOnAction(e -> { activeRoleFilter = "FARMER"; update(bAll, bFarmers, bProviders, bOperators); renderUsers(root); });
        bProviders.setOnAction(e -> { activeRoleFilter = "PROVIDER"; update(bAll, bFarmers, bProviders, bOperators); renderUsers(root); });
        bOperators.setOnAction(e -> { activeRoleFilter = "OPERATOR"; update(bAll, bFarmers, bProviders, bOperators); renderUsers(root); });

        HBox bar = new HBox(10, bAll, bFarmers, bProviders, bOperators);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setMinWidth(0);
        return bar;
    }

    private static void update(Button b1, Button b2, Button b3, Button b4) {
        styleTab(b1, "ALL".equals(activeRoleFilter));
        styleTab(b2, "FARMER".equals(activeRoleFilter));
        styleTab(b3, "PROVIDER".equals(activeRoleFilter));
        styleTab(b4, "OPERATOR".equals(activeRoleFilter));
    }

    private static void styleTab(Button b, boolean active) {
        if (active) {
            b.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
        } else {
            b.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
        }
    }

    private static void renderUsers(StackPane root) {
        listContainer.getChildren().clear();

        for (UserItem u : userList) {
            if (!"ALL".equals(activeRoleFilter) && !u.role.equals(activeRoleFilter)) continue;

            if (!searchFilter.isEmpty()) {
                boolean match = u.name.toLowerCase().contains(searchFilter)
                        || u.userId.toLowerCase().contains(searchFilter)
                        || u.phone.toLowerCase().contains(searchFilter)
                        || u.location.toLowerCase().contains(searchFilter);
                if (!match) continue;
            }

            listContainer.getChildren().add(createUserCard(u, root));
        }
    }

    private static VBox createUserCard(UserItem u, StackPane root) {
        String roleIcon = "FARMER".equals(u.role) ? "👨‍🌾" : ("PROVIDER".equals(u.role) ? "🚜" : "👨‍🔧");

        Text icon = new Text(roleIcon);
        icon.setStyle("-fx-font-size: 22px;");
        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(42, 42);
        iconBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8;");

        Text name = new Text(u.name);
        name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label roleBadge = new Label(u.role);
        roleBadge.setStyle("-fx-background-color: #F4F9F4; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");

        HBox nameRow = new HBox(6, name, roleBadge);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Text sub = new Text("🆔 " + u.userId + "  •  📞 " + u.phone + "  •  📍 " + u.location);
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Text stats = new Text("📊 " + u.stats);
        stats.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #2E7D32; -fx-font-weight: bold;");

        VBox info = new VBox(2, nameRow, sub, stats);
        info.setMinWidth(0);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // KYC Badge
        Label kyc = new Label("KYC: " + u.kycStatus);
        kyc.setStyle("-fx-background-color: " + ("VERIFIED".equals(u.kycStatus) ? "#E8F5E9" : "#FFF3E0") + "; -fx-text-fill: " + ("VERIFIED".equals(u.kycStatus) ? "#2E7D32" : "#E65100") + "; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

        Button verifyKycBtn = new Button("✔ Verify KYC");
        verifyKycBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 10 5 10;");
        verifyKycBtn.setVisible("PENDING".equals(u.kycStatus));
        verifyKycBtn.setOnAction(e -> {
            u.kycStatus = "VERIFIED";
            renderUsers(root);
        });

        Button statusToggle = new Button("ACTIVE".equals(u.accountStatus) ? "Suspend" : "Activate");
        statusToggle.setStyle("-fx-background-color: " + ("ACTIVE".equals(u.accountStatus) ? "#8B3A3A" : "#2E7D32") + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 10 5 10;");
        statusToggle.setOnAction(e -> {
            u.accountStatus = "ACTIVE".equals(u.accountStatus) ? "SUSPENDED" : "ACTIVE";
            renderUsers(root);
        });

        HBox actions = new HBox(8, kyc, verifyKycBtn, statusToggle);
        actions.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(12, iconBox, info, spacer, actions);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinWidth(0);

        VBox card = new VBox(row);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");

        return card;
    }
}
