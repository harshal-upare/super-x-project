package com.desgin.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminProfileManagement {

    private static VBox profilePopupRef;
    private static Text profilePillNameText;
    private static Label verifiedDotLabel;

    public static String getFormattedFirstName() {
        String raw = AdminProfileStore.adminName;
        if (raw == null || raw.trim().isEmpty()) {
            return "Administrator";
        }
        raw = raw.trim();
        if (raw.contains("@")) {
            raw = raw.split("@")[0];
        }
        String first = raw.split("[ ._]")[0];
        if (first.isEmpty()) {
            return "Administrator";
        }
        return Character.toUpperCase(first.charAt(0)) + (first.length() > 1 ? first.substring(1) : "");
    }

    public static Text headerTitleText = new Text("Welcome back, " + getFormattedFirstName() + " 🛡️");
    public static Text headerSubtitleText = new Text("FarmEquip Admin Console • Platform Controls & Security (Max 5 Admins)");
    public static VBox headerTitleBox;

    static {
        headerTitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        headerSubtitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");
        headerTitleBox = new VBox(2, headerTitleText, headerSubtitleText);
        headerTitleBox.setAlignment(Pos.CENTER_LEFT);

        AdminProfileStore.addListener(() -> {
            javafx.application.Platform.runLater(AdminProfileManagement::refreshDynamicProfileUI);
        });
    }

    public static void updateHeaderGreeting() {
        String fName = getFormattedFirstName();
        setHeaderTitle("Welcome back, " + fName + " 🛡️", "FarmEquip Admin Console • Platform Controls & Security (Max 5 Admins)");
    }

    public static void setHeaderTitle(String title, String subtitle) {
        if (headerTitleText != null) headerTitleText.setText(title);
        if (headerSubtitleText != null) {
            if (subtitle != null && !subtitle.isEmpty()) {
                headerSubtitleText.setText(subtitle);
                headerSubtitleText.setVisible(true);
                headerSubtitleText.setManaged(true);
            } else {
                headerSubtitleText.setVisible(false);
                headerSubtitleText.setManaged(false);
            }
        }
    }

    public static void refreshDynamicProfileUI() {
        updateHeaderGreeting();
        if (profilePillNameText != null) {
            profilePillNameText.setText(AdminProfileStore.adminName != null ? AdminProfileStore.adminName : "Administrator");
        }
        if (verifiedDotLabel != null) {
            verifiedDotLabel.setText(AdminProfileStore.adminRole != null ? AdminProfileStore.adminRole : "HQ Root");
        }
    }

    public HBox getProfile(StackPane root) {
        updateHeaderGreeting();

        Button alertBtn = createPillButton("🔔", "System Alerts", "4 Flagged");

        HBox profileBox = createProfilePill("🛡️", AdminProfileStore.adminName, AdminProfileStore.adminRole);

        // Profile Popup
        profilePopupRef = createAdminProfileModal();
        root.getChildren().add(profilePopupRef);

        profileBox.setOnMouseClicked(event -> {
            profilePopupRef.setVisible(!profilePopupRef.isVisible());
        });

        // Alerts Popup
        VBox alertPopup = createAdminAlertsModal();
        root.getChildren().add(alertPopup);

        alertBtn.setOnAction(e -> {
            boolean isVis = alertPopup.isVisible();
            if (profilePopupRef.isVisible()) profilePopupRef.setVisible(false);
            alertPopup.setVisible(!isVis);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topHeader = new HBox(16, headerTitleBox, spacer, alertBtn, profileBox);
        topHeader.setAlignment(Pos.CENTER_LEFT);
        topHeader.setPadding(new Insets(10, 24, 10, 24));
        topHeader.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        return topHeader;
    }

    private Button createPillButton(String icon, String title, String badge) {
        Button btn = new Button();
        btn.setPrefHeight(38);
        btn.setMinHeight(38);
        btn.setMaxHeight(38);

        HBox content = new HBox(6);
        content.setAlignment(Pos.CENTER);

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 13.5px;");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        content.getChildren().addAll(iconText, titleText);

        if (badge != null && !badge.isEmpty()) {
            Label badgeLabel = new Label(badge);
            badgeLabel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");
            content.getChildren().add(badgeLabel);
        }

        btn.setGraphic(content);

        String normalStyle = "-fx-background-color: #FFFFFF;"
                + "-fx-background-radius: 20px;"
                + "-fx-border-color: #C2E0CE;"
                + "-fx-border-radius: 20px;"
                + "-fx-border-width: 1.2px;"
                + "-fx-padding: 0 14px 0 12px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 4, 0, 0, 1);";

        String hoverStyle = "-fx-background-color: #F0FDF4;"
                + "-fx-background-radius: 20px;"
                + "-fx-border-color: #2D6A4F;"
                + "-fx-border-radius: 20px;"
                + "-fx-border-width: 1.2px;"
                + "-fx-padding: 0 14px 0 12px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.18), 8, 0, 0, 2);";

        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
        return btn;
    }

    private HBox createProfilePill(String icon, String name, String tag) {
        Text profileIcon = new Text(icon);
        profileIcon.setStyle("-fx-font-size: 14px;");

        profilePillNameText = new Text(name != null ? name : "Administrator");
        profilePillNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        verifiedDotLabel = new Label(tag != null ? tag : "HQ Root");
        verifiedDotLabel.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 1px 6px; -fx-background-radius: 8px;");

        Text dropdownArrow = new Text("▾");
        dropdownArrow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #2D6A4F; -fx-font-weight: bold;");

        HBox profileBox = new HBox(6, profileIcon, profilePillNameText, verifiedDotLabel, dropdownArrow);
        profileBox.setAlignment(Pos.CENTER);
        profileBox.setPrefHeight(38);
        profileBox.setMinHeight(38);
        profileBox.setMaxHeight(38);
        profileBox.setPadding(new Insets(0, 14, 0, 12));

        String normalStyle = "-fx-background-color: #FFFFFF;"
                + "-fx-background-radius: 20px;"
                + "-fx-border-color: #C2E0CE;"
                + "-fx-border-radius: 20px;"
                + "-fx-border-width: 1.2px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 4, 0, 0, 1);";

        String hoverStyle = "-fx-background-color: #F0FDF4;"
                + "-fx-background-radius: 20px;"
                + "-fx-border-color: #2D6A4F;"
                + "-fx-border-radius: 20px;"
                + "-fx-border-width: 1.2px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.18), 8, 0, 0, 2);";

        profileBox.setStyle(normalStyle);
        profileBox.setOnMouseEntered(e -> profileBox.setStyle(hoverStyle));
        profileBox.setOnMouseExited(e -> profileBox.setStyle(normalStyle));
        return profileBox;
    }

    private static Button createCloseButton(Runnable onClose) {
        Button close = new Button("✕");
        close.setPrefSize(30, 30);
        close.setMinSize(30, 30);
        close.setMaxSize(30, 30);
        close.setStyle(
                "-fx-background-color: #F3F4F6;" +
                "-fx-background-radius: 15px;" +
                "-fx-text-fill: #6B7280;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;"
        );
        close.setOnMouseEntered(e -> close.setStyle(
                "-fx-background-color: #FEE2E2;" +
                "-fx-background-radius: 15px;" +
                "-fx-text-fill: #DC2626;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;"
        ));
        close.setOnMouseExited(e -> close.setStyle(
                "-fx-background-color: #F3F4F6;" +
                "-fx-background-radius: 15px;" +
                "-fx-text-fill: #6B7280;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;"
        ));
        close.setOnAction(e -> {
            if (onClose != null) onClose.run();
        });
        return close;
    }

    private VBox createAdminProfileModal() {
        VBox modal = new VBox(14);
        modal.setPadding(new Insets(18, 20, 18, 20));
        modal.setPrefSize(500, 520);
        modal.setMaxSize(500, 520);
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #D1E7DD;" +
                "-fx-border-radius: 18px;" +
                "-fx-border-width: 1.5px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 24, 0, 0, 8);"
        );

        // Header
        Text avatar = new Text("🛡️");
        avatar.setStyle("-fx-font-size: 18px;");
        StackPane avatarBox = new StackPane(avatar);
        avatarBox.setPrefSize(38, 38);
        avatarBox.setMinSize(38, 38);
        avatarBox.setMaxSize(38, 38);
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 10px;");

        Text title = new Text("Root Administrator Profile");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Platform master controls, escrow parameters & security compliance");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(2, title, sub);
        HBox titleGroup = new HBox(10, avatarBox, titleBox);
        titleGroup.setAlignment(Pos.CENTER_LEFT);

        Button close = createCloseButton(() -> modal.setVisible(false));

        HBox topBar = new HBox(titleGroup, close);
        topBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleGroup, Priority.ALWAYS);
        topBar.setPadding(new Insets(0, 0, 12, 0));
        topBar.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        // Content
        VBox card = new VBox(10);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-radius: 12px; -fx-border-width: 1px;");

        HBox r1 = createRow("Admin Authority:", "Super Root Controller (All Hubs)");
        HBox r2 = createRow("Operational Office:", "FarmEquip Central HQ, Pune 411001");
        HBox r3 = createRow("Escrow Vault Status:", "Encrypted & Active (₹48.5L GMV)");
        HBox r4 = createRow("KYC Audit Level:", "Tier-3 Full Regulatory Compliance");
        HBox r5 = createRow("Dispute Redressal SLA:", "Average Resolution: 18 Minutes");

        card.getChildren().addAll(r1, r2, r3, r4, r5);

        ScrollPane sp = new ScrollPane(card);
        sp.setFitToWidth(true);
        sp.setPrefHeight(420);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        return modal;
    }

    private VBox createAdminAlertsModal() {
        VBox modal = new VBox(14);
        modal.setPadding(new Insets(18, 20, 18, 20));
        modal.setPrefSize(500, 480);
        modal.setMaxSize(500, 480);
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #D1E7DD;" +
                "-fx-border-radius: 18px;" +
                "-fx-border-width: 1.5px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 24, 0, 0, 8);"
        );

        // Header
        Text bell = new Text("🔔");
        bell.setStyle("-fx-font-size: 18px;");
        StackPane bellBox = new StackPane(bell);
        bellBox.setPrefSize(38, 38);
        bellBox.setMinSize(38, 38);
        bellBox.setMaxSize(38, 38);
        bellBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 10px;");

        Text title = new Text("Platform Security & Moderation Alerts");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Pending machinery verifications, KYC audits & escrow logs");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(2, title, sub);
        HBox titleGroup = new HBox(10, bellBox, titleBox);
        titleGroup.setAlignment(Pos.CENTER_LEFT);

        Button close = createCloseButton(() -> modal.setVisible(false));

        HBox topBar = new HBox(titleGroup, close);
        topBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleGroup, Priority.ALWAYS);
        topBar.setPadding(new Insets(0, 0, 12, 0));
        topBar.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        // Items
        VBox n1 = createAlertCard("🚜 Machinery Verification Queue", "Swaraj 855 FE submitted by Rajesh Agro awaits RTO RC document audit.", "15 mins ago", "VERIFY", "#DCFCE7", "#15803D");
        VBox n2 = createAlertCard("👥 Operator License Queue", "3 new operator heavy machinery licenses submitted from Baramati district.", "1 hour ago", "KYC", "#E0E7FF", "#4338CA");
        VBox n3 = createAlertCard("💰 Escrow Daily Audit Passed", "Automated platform ledger reconciliation passed with 100% matched entries.", "4 hours ago", "AUDIT", "#FEF3C7", "#B45309");
        VBox n4 = createAlertCard("⚖️ Dispute Claim Resolved", "Booking #BK-4912 fuel settlement completed with full mutual agreement.", "Yesterday", "RESOLVED", "#F3F4F6", "#374151");

        VBox list = new VBox(10, n1, n2, n3, n4);
        list.setPadding(new Insets(0, 6, 0, 0));
        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setPrefHeight(380);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        return modal;
    }

    private static HBox createRow(String label, String value) {
        Label l = new Label(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        l.setPrefWidth(150);

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151;");

        HBox row = new HBox(8, l, v);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static VBox createAlertCard(String title, String desc, String time, String tag, String tagBg, String tagColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label tagLabel = new Label(tag);
        tagLabel.setStyle("-fx-background-color: " + tagBg + "; -fx-text-fill: " + tagColor + "; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 4px;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox topRow = new HBox(8, t, sp, tagLabel);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Text d = new Text(desc);
        d.setWrappingWidth(390);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-line-spacing: 2px;");

        Text tm = new Text(time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #9CA3AF;");

        VBox card = new VBox(5, topRow, d, tm);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPadding(new Insets(12, 14, 12, 14));
        card.setStyle(
                "-fx-background-color: #F8FAF8;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-radius: 12px;" +
                "-fx-border-width: 1px;"
        );
        return card;
    }
}
