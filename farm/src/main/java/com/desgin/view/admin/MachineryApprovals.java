package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MachineryApprovals {

    public static class ApprovalItem {
        public String id;
        public String machineTitle;
        public String category;
        public String providerName;
        public String providerPhone;
        public String rtoRegNo;
        public int dailyRate;
        public String specs;
        public String imagePath;
        public String status; // "PENDING", "APPROVED", "REJECTED"
        public String submissionDate;

        public ApprovalItem(String id, String machineTitle, String category, String providerName, String providerPhone, String rtoRegNo, int dailyRate, String specs, String imagePath, String status, String submissionDate) {
            this.id = id;
            this.machineTitle = machineTitle;
            this.category = category;
            this.providerName = providerName;
            this.providerPhone = providerPhone;
            this.rtoRegNo = rtoRegNo;
            this.dailyRate = dailyRate;
            this.specs = specs;
            this.imagePath = imagePath;
            this.status = status;
            this.submissionDate = submissionDate;
        }
    }

    private static List<ApprovalItem> approvalsList = new ArrayList<>();
    private static VBox listContainer;
    private static String activeTab = "PENDING";
    private static String searchFilter = "";

    static {
        initData();
    }

    private static void initData() {
        approvalsList.clear();
        try {
            java.util.List<com.desgin.model.MachineryModel> machs = new com.desgin.dao.MachineryDAO().getAllMachinery();
            for (com.desgin.model.MachineryModel m : machs) {
                String st = m.getStatus() != null ? m.getStatus().toUpperCase() : "APPROVED";
                String appStatus = "PENDING".equals(st) ? "PENDING" : ("REJECTED".equals(st) ? "REJECTED" : "APPROVED");
                String img = (m.getImagePath() != null && !m.getImagePath().isEmpty()) ? m.getImagePath() : "file:farm/src/main/resources/assets/Images/tractor.png";
                approvalsList.add(new ApprovalItem(
                        m.getId() != null ? m.getId() : "MAC-" + System.currentTimeMillis(),
                        m.getName() != null ? m.getName() : "Machinery",
                        m.getCategory() != null ? m.getCategory() : "Equipment",
                        m.getProviderName() != null ? m.getProviderName() : "Fleet Provider",
                        m.getProviderPhone() != null ? m.getProviderPhone() : "+91 98000 00000",
                        m.getRegistrationNumber() != null ? m.getRegistrationNumber() : "MH-12-REG",
                        m.getPricePerDay(),
                        m.getSpecifications() != null ? m.getSpecifications() : (m.getModel() != null ? m.getModel() : "Farm Ready"),
                        img,
                        appStatus,
                        "Registered"
                ));
            }
        } catch (Exception ignored) {}
        if (approvalsList.isEmpty()) {
            approvalsList.add(new ApprovalItem("APP-094", "John Deere 5310 PowerTech", "Tractors", "Rajesh Patil", "+91 98220 12345", "MH-12-JD-5310", 1500, "55 HP • Turbocharged • 4WD", "file:farm/src/main/resources/assets/Images/tractor.png", "APPROVED", "Active"));
        }
    }

    public static ScrollPane getPage(StackPane root) {
        Text title = new Text("Machinery Listing Verification & Quality Moderation");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Review newly registered machinery submitted by fleet providers, inspect RTO registration, and approve listings for public rental.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(3, title, subtitle);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by Application ID, Machine Model, Provider or RTO No...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(380);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            searchFilter = newV.trim().toLowerCase();
            renderList(root);
        });

        // Filter Tabs
        HBox tabBox = createFilterTabs(root);

        listContainer = new VBox(14);
        listContainer.setMinWidth(0);
        renderList(root);

        VBox content = new VBox(18, titleBox, searchField, tabBox, listContainer);
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

    private static HBox createFilterTabs(StackPane root) {
        long pendingCount = approvalsList.stream().filter(a -> "PENDING".equals(a.status)).count();
        long approvedCount = approvalsList.stream().filter(a -> "APPROVED".equals(a.status)).count();
        long rejectedCount = approvalsList.stream().filter(a -> "REJECTED".equals(a.status)).count();

        Button tPending = new Button("Pending Review (" + pendingCount + ")");
        Button tApproved = new Button("Approved Fleet (" + approvedCount + ")");
        Button tRejected = new Button("Rejected (" + rejectedCount + ")");

        styleTab(tPending, "PENDING".equals(activeTab));
        styleTab(tApproved, "APPROVED".equals(activeTab));
        styleTab(tRejected, "REJECTED".equals(activeTab));

        tPending.setOnAction(e -> { activeTab = "PENDING"; updateTabs(tPending, tApproved, tRejected); renderList(root); });
        tApproved.setOnAction(e -> { activeTab = "APPROVED"; updateTabs(tPending, tApproved, tRejected); renderList(root); });
        tRejected.setOnAction(e -> { activeTab = "REJECTED"; updateTabs(tPending, tApproved, tRejected); renderList(root); });

        HBox bar = new HBox(10, tPending, tApproved, tRejected);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setMinWidth(0);
        return bar;
    }

    private static void updateTabs(Button b1, Button b2, Button b3) {
        styleTab(b1, "PENDING".equals(activeTab));
        styleTab(b2, "APPROVED".equals(activeTab));
        styleTab(b3, "REJECTED".equals(activeTab));
    }

    private static void styleTab(Button b, boolean active) {
        if (active) {
            b.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
        } else {
            b.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
        }
    }

    private static void renderList(StackPane root) {
        listContainer.getChildren().clear();

        for (ApprovalItem item : approvalsList) {
            if (!item.status.equals(activeTab)) continue;

            if (!searchFilter.isEmpty()) {
                boolean match = item.id.toLowerCase().contains(searchFilter)
                        || item.machineTitle.toLowerCase().contains(searchFilter)
                        || item.providerName.toLowerCase().contains(searchFilter)
                        || item.rtoRegNo.toLowerCase().contains(searchFilter);
                if (!match) continue;
            }

            listContainer.getChildren().add(createCard(item, root));
        }

        if (listContainer.getChildren().isEmpty()) {
            VBox empty = new VBox(10);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(40));
            Text t = new Text("No machinery applications under this filter.");
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-fill: #4B5563;");
            empty.getChildren().add(t);
            listContainer.getChildren().add(empty);
        }
    }

    private static VBox createCard(ApprovalItem item, StackPane root) {
        // Thumbnail Image
        ImageView iv = new ImageView();
        try {
            Image img = new Image(item.imagePath);
            iv.setImage(img);
            iv.setFitWidth(110);
            iv.setFitHeight(75);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
        } catch (Exception ignored) {}

        StackPane imgBox = new StackPane(iv);
        imgBox.setPrefSize(120, 80);
        imgBox.setMaxSize(120, 80);
        imgBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8;");

        Text title = new Text(item.machineTitle + " (" + item.category + ")");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text rto = new Text("🆔 Reg / RTO: " + item.rtoRegNo + "  •  📅 " + item.submissionDate);
        rto.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Text provider = new Text("👤 Provider: " + item.providerName + " (📞 " + item.providerPhone + ")");
        provider.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #374151;");

        Text specs = new Text("⚙ Specs: " + item.specs);
        specs.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #374151;");

        Text rate = new Text("₹" + item.dailyRate + " / day tariff");
        rate.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        VBox middleInfo = new VBox(2, title, rto, provider, specs, rate);
        middleInfo.setMinWidth(0);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Action buttons
        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_RIGHT);

        if ("PENDING".equals(item.status)) {
            Button approveBtn = new Button("✔ Approve");
            approveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14 6 14;");
            approveBtn.setOnAction(e -> {
                item.status = "APPROVED";
                renderList(root);
                new Thread(() -> {
                    try {
                        new com.desgin.dao.MachineryDAO().updateMachineryStatus(item.id, "AVAILABLE");
                    } catch (Exception ignored) {}
                }).start();
            });

            Button rejectBtn = new Button("✖ Reject");
            rejectBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
            rejectBtn.setOnAction(e -> {
                item.status = "REJECTED";
                renderList(root);
                new Thread(() -> {
                    try {
                        new com.desgin.dao.MachineryDAO().updateMachineryStatus(item.id, "REJECTED");
                    } catch (Exception ignored) {}
                }).start();
            });

            actions.getChildren().addAll(rejectBtn, approveBtn);
        } else {
            Label stLabel = new Label("● " + item.status);
            stLabel.setStyle("-fx-background-color: " + ("APPROVED".equals(item.status) ? "#E8F5E9" : "#FFEBEE") + "; -fx-text-fill: " + ("APPROVED".equals(item.status) ? "#2E7D32" : "#C62828") + "; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 6;");
            actions.getChildren().add(stLabel);
        }

        HBox cardRow = new HBox(14, imgBox, middleInfo, spacer, actions);
        cardRow.setAlignment(Pos.CENTER_LEFT);
        cardRow.setMinWidth(0);

        VBox card = new VBox(cardRow);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return card;
    }
}
