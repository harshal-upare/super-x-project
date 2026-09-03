package com.desgin.view.operator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorProfileManagement {

    private static VBox profilePopupRef;
    private static Text profilePillNameText;
    private static TextField modalNameField;
    private static TextField modalContactField;
    private static TextField modalLocationField;
    private static TextField modalLicenseField;

    public static String getFormattedFirstName() {
        String raw = OperatorProfileStore.name;
        if (raw == null || raw.trim().isEmpty()) {
            return "Operator";
        }
        raw = raw.trim();
        if (raw.contains("@")) {
            raw = raw.split("@")[0];
        }
        String first = raw.split("[ ._]")[0];
        if (first.isEmpty()) {
            return "Operator";
        }
        return Character.toUpperCase(first.charAt(0)) + (first.length() > 1 ? first.substring(1) : "");
    }

    public static Text headerTitleText = new Text("Welcome back, " + getFormattedFirstName() + " 👨‍🌾");
    public static Text headerSubtitleText = new Text("Machinery Operator Dashboard • Telematics & Daily Wages");
    public static VBox headerTitleBox;

    static {
        headerTitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        headerSubtitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");
        headerTitleBox = new VBox(2, headerTitleText, headerSubtitleText);
        headerTitleBox.setAlignment(Pos.CENTER_LEFT);

        OperatorProfileStore.addProfileListener(() -> {
            javafx.application.Platform.runLater(OperatorProfileManagement::refreshDynamicProfileUI);
        });
    }

    public static void updateHeaderGreeting() {
        String fName = getFormattedFirstName();
        setHeaderTitle("Welcome back, " + fName + " 👨‍🌾", "Machinery Operator Dashboard • Telematics & Daily Wages");
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
            profilePillNameText.setText(OperatorProfileStore.name != null ? OperatorProfileStore.name : "Operator");
        }
        if (modalNameField != null && !modalNameField.isFocused()) {
            modalNameField.setText(OperatorProfileStore.name != null ? OperatorProfileStore.name : "");
        }
        if (modalContactField != null && !modalContactField.isFocused()) {
            modalContactField.setText(OperatorProfileStore.phone != null ? OperatorProfileStore.phone : "");
        }
        if (modalLocationField != null && !modalLocationField.isFocused()) {
            modalLocationField.setText(OperatorProfileStore.zone != null ? OperatorProfileStore.zone : "");
        }
        if (modalLicenseField != null && !modalLicenseField.isFocused()) {
            modalLicenseField.setText(OperatorProfileStore.licenseNo != null ? OperatorProfileStore.licenseNo : "");
        }
    }

    public HBox getProfile(StackPane root) {
        updateHeaderGreeting();

        Button notificationBtn1 = createPillButton("🔔", "Notifications", "2");

        HBox profileBox = createProfilePill("👷", OperatorProfileStore.name, OperatorProfileStore.badge);

        // Profile Popup
        profilePopupRef = createProfileModal();
        root.getChildren().add(profilePopupRef);

        profileBox.setOnMouseClicked(event -> {
            boolean isVis = profilePopupRef.isVisible();
            if (!isVis) {
                refreshDynamicProfileUI();
            }
            profilePopupRef.setVisible(!isVis);
        });

        // Notifications Popup
        VBox notifPopup = createNotificationModal();
        root.getChildren().add(notifPopup);

        notificationBtn1.setOnAction(e -> {
            boolean isVis = notifPopup.isVisible();
            if (profilePopupRef.isVisible()) profilePopupRef.setVisible(false);
            notifPopup.setVisible(!isVis);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(16, headerTitleBox, spacer, notificationBtn1, profileBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 24, 10, 24));
        topBar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        return topBar;
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
            badgeLabel.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");
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

        profilePillNameText = new Text(name != null ? name : "Operator");
        profilePillNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedDot = new Label(tag != null ? tag : "Certified");
        verifiedDot.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 1px 6px; -fx-background-radius: 8px;");

        Text dropdownArrow = new Text("▾");
        dropdownArrow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #2D6A4F; -fx-font-weight: bold;");

        HBox profileBox = new HBox(6, profileIcon, profilePillNameText, verifiedDot, dropdownArrow);
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

    private VBox createProfileModal() {
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
        Text avatar = new Text("👷");
        avatar.setStyle("-fx-font-size: 18px;");
        StackPane avatarBox = new StackPane(avatar);
        avatarBox.setPrefSize(38, 38);
        avatarBox.setMinSize(38, 38);
        avatarBox.setMaxSize(38, 38);
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 10px;");

        Text title = new Text("Operator Certified Profile");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("License verification, machinery ratings & operational shift zone");
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

        // Form content
        VBox content = new VBox(14);
        content.setPadding(new Insets(0, 6, 0, 0));

        modalNameField = new TextField(OperatorProfileStore.name);
        modalContactField = new TextField(OperatorProfileStore.phone);
        modalLocationField = new TextField(OperatorProfileStore.zone);
        modalLicenseField = new TextField(OperatorProfileStore.licenseNo);

        styleField(modalNameField);
        styleField(modalContactField);
        styleField(modalLocationField);
        styleField(modalLicenseField);

        VBox card = new VBox(10);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-radius: 12px; -fx-border-width: 1px;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.add(createFieldGroup("Full Name", modalNameField), 0, 0);
        grid.add(createFieldGroup("Contact Mobile", modalContactField), 1, 0);
        grid.add(createFieldGroup("Operational Zone", modalLocationField), 0, 1);
        grid.add(createFieldGroup("Operator License No.", modalLicenseField), 1, 1);

        Label msg = new Label();
        msg.setVisible(false);
        msg.setManaged(false);

        Button saveBtn = new Button("Save Profile Changes");
        saveBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 7px 20px; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            String n = modalNameField.getText();
            String c = modalContactField.getText();
            String l = modalLocationField.getText();
            String lic = modalLicenseField.getText();
            OperatorProfileStore.setProfile(n, c, l, lic);

            msg.setText("✓ Operator profile saved successfully!");
            msg.setStyle("-fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-color: #DCFCE7; -fx-padding: 6px 10px; -fx-background-radius: 6px;");
            msg.setVisible(true);
            msg.setManaged(true);
        });

        HBox act = new HBox(12, saveBtn, msg);
        act.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(grid, act);
        content.getChildren().add(card);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setPrefHeight(420);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        return modal;
    }

    private VBox createNotificationModal() {
        VBox modal = new VBox(14);
        modal.setPadding(new Insets(18, 20, 18, 20));
        modal.setPrefSize(500, 440);
        modal.setMaxSize(500, 440);
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

        Text title = new Text("Operator Shift Alerts");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Field task assignments, daily wage credits & schedule updates");
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
        VBox n1 = createNotifCard("🚜 New Field Assignment", "Assigned to Tractor Plowing Job at Baramati Farm (6:00 AM shift tomorrow).", "25 mins ago", "ASSIGNMENT", "#DCFCE7", "#15803D");
        VBox n2 = createNotifCard("💵 Daily Wage Settled", "₹1,200 shift payout credited to linked UPI wallet for Rotavator operation.", "3 hours ago", "PAYOUT", "#FEF3C7", "#B45309");

        VBox list = new VBox(10, n1, n2);
        list.setPadding(new Insets(0, 6, 0, 0));
        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setPrefHeight(340);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        return modal;
    }

    private static VBox createNotifCard(String title, String desc, String time, String tag, String tagBg, String tagColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label tagLabel = new Label(tag);
        tagLabel.setStyle("-fx-background-color: " + tagBg + "; -fx-text-fill: " + tagColor + "; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 4px;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox topRow = new HBox(8, t, sp, tagLabel);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Text d = new Text(desc);
        d.setWrappingWidth(410);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-line-spacing: 2px;");

        Text tm = new Text(time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #9CA3AF;");

        VBox card = new VBox(5, topRow, d, tm);
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

    private static void styleField(TextField f) {
        f.setPrefHeight(36);
        f.setPrefWidth(190);
        f.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D1E7DD; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 10px; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");
    }

    private static VBox createFieldGroup(String label, TextField f) {
        Label l = new Label(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        return new VBox(3, l, f);
    }
}
