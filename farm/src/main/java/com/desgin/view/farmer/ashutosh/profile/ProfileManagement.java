package com.desgin.view.farmer.ashutosh.profile;

import java.util.List;

import com.desgin.dao.AuthDAO;
import com.desgin.view.farmer.LeftSideBar;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;
import com.desgin.view.farmer.ashutosh.settings.Settings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProfileManagement {

    private static VBox profilePopupRef;
    private static Text profilePillNameText;
    private static StackPane profilePillAvatarBox;
    private static ImageView modalAvatarView;
    private static Text modalAvatarIcon;
    private static Text nameValText;
    private static Text emailValText;
    private static Text phoneValText;
    private static TextField modalTownField;
    private static TextField modalDistrictField;
    private static TextField modalStateField;
    private static TextField modalPincodeField;

    public static String getFormattedFirstName() {
        String raw = FarmerProfileStore.name;
        if (raw == null || raw.trim().isEmpty()) {
            return "Farmer";
        }
        raw = raw.trim();
        if (raw.contains("@")) {
            raw = raw.split("@")[0];
        }
        String first = raw.split("[ ._]")[0];
        if (first.isEmpty()) {
            return "Farmer";
        }
        return Character.toUpperCase(first.charAt(0)) + (first.length() > 1 ? first.substring(1) : "");
    }

    public static Text headerTitleText = new Text("Welcome back, " + getFormattedFirstName() + " 👋");
    public static Text headerSubtitleText = new Text("Farmer Dashboard • Find the right equipment for your farm");
    public static VBox headerTitleBox;
    private static Label farmerNotifBadge;
    private static VBox farmerNotifList;

    static {
        headerTitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        headerSubtitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");
        headerTitleBox = new VBox(2, headerTitleText, headerSubtitleText);
        headerTitleBox.setAlignment(Pos.CENTER_LEFT);

        FarmerProfileStore.addProfileListener(() -> {
            javafx.application.Platform.runLater(ProfileManagement::refreshDynamicProfileUI);
        });
        FarmerProfileStore.addLocationListener(() -> {
            javafx.application.Platform.runLater(ProfileManagement::refreshDynamicLocationUI);
        });
    }

    public static void updateHeaderGreeting() {
        String fName = getFormattedFirstName();
        setHeaderTitle("Welcome back, " + fName + " 👋", "Farmer Dashboard • Find the right equipment for your farm");
    }

    public static void refreshDynamicProfileUI() {
        updateHeaderGreeting();
        if (profilePillNameText != null) {
            profilePillNameText.setText(FarmerProfileStore.name != null ? FarmerProfileStore.name : "Farmer");
        }
        if (nameValText != null) {
            nameValText.setText(FarmerProfileStore.name != null ? FarmerProfileStore.name : "Not Set");
        }
        if (emailValText != null) {
            emailValText.setText(FarmerProfileStore.email != null ? FarmerProfileStore.email : "Not Set");
        }
        if (phoneValText != null) {
            phoneValText.setText(FarmerProfileStore.phone != null ? FarmerProfileStore.phone : "Not Set");
        }
        updateAvatarDisplay();
    }

    private static void updateAvatarDisplay() {
        String pic = FarmerProfileStore.profilePic;
        if (pic != null && !pic.trim().isEmpty()) {
            try {
                javafx.scene.image.Image img = new javafx.scene.image.Image(pic, true);
                if (modalAvatarView != null) {
                    modalAvatarView.setImage(img);
                    modalAvatarView.setVisible(true);
                    modalAvatarView.setManaged(true);
                }
                if (modalAvatarIcon != null) {
                    modalAvatarIcon.setVisible(false);
                    modalAvatarIcon.setManaged(false);
                }
                if (profilePillAvatarBox != null) {
                    profilePillAvatarBox.getChildren().clear();
                    ImageView pillIv = new ImageView(img);
                    pillIv.setFitWidth(22);
                    pillIv.setFitHeight(22);
                    pillIv.setClip(new javafx.scene.shape.Circle(11, 11, 11));
                    profilePillAvatarBox.getChildren().add(pillIv);
                }
            } catch (Exception ignored) {}
        } else {
            if (modalAvatarView != null) {
                modalAvatarView.setVisible(false);
                modalAvatarView.setManaged(false);
            }
            if (modalAvatarIcon != null) {
                modalAvatarIcon.setVisible(true);
                modalAvatarIcon.setManaged(true);
            }
            if (profilePillAvatarBox != null) {
                profilePillAvatarBox.getChildren().clear();
                Text icon = new Text("👤");
                icon.setStyle("-fx-font-size: 14px;");
                profilePillAvatarBox.getChildren().add(icon);
            }
        }
    }

    public static void refreshDynamicLocationUI() {
        if (modalTownField != null && !modalTownField.isFocused()) {
            modalTownField.setText(FarmerProfileStore.town != null ? FarmerProfileStore.town : "");
        }
        if (modalDistrictField != null && !modalDistrictField.isFocused()) {
            modalDistrictField.setText(FarmerProfileStore.district != null ? FarmerProfileStore.district : "");
        }
        if (modalStateField != null && !modalStateField.isFocused()) {
            modalStateField.setText(FarmerProfileStore.state != null ? FarmerProfileStore.state : "");
        }
        if (modalPincodeField != null && !modalPincodeField.isFocused()) {
            modalPincodeField.setText(FarmerProfileStore.pincode != null ? FarmerProfileStore.pincode : "");
        }
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

    public HBox getProfile(StackPane root) {

        updateHeaderGreeting();

        // ================= TOP RIGHT: NOTIFICATIONS & PROFILE =================
        farmerNotifBadge = new Label("");
        farmerNotifBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");
        farmerNotifBadge.setVisible(false);
        farmerNotifBadge.setManaged(false);

        Button notificationBtn1 = createPillButton("🔔", "Notifications", farmerNotifBadge);

        // Profile Pill Button
        HBox profileBox = createProfilePill();

        // Profile Popup
        profilePopupRef = createProfileModal();
        root.getChildren().add(profilePopupRef);

        profileBox.setOnMouseClicked(event -> {
            boolean isVis = profilePopupRef.isVisible();
            if (!isVis) {
                refreshDynamicProfileUI();
                refreshDynamicLocationUI();
            }
            profilePopupRef.setVisible(!isVis);
        });

        // Notifications Popup
        VBox notificationPopUp = createNotificationModal();
        root.getChildren().add(notificationPopUp);

        notificationBtn1.setOnAction(e -> {
            boolean isVis = notificationPopUp.isVisible();
            if (profilePopupRef.isVisible()) profilePopupRef.setVisible(false);
            notificationPopUp.setVisible(!isVis);
            if (!isVis) refreshFarmerNotifications();
        });

        refreshFarmerNotifications();

        HBox rightHBox = new HBox(10, notificationBtn1, profileBox);
        rightHBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(16, headerTitleBox, spacer, rightHBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 24, 10, 24));
        topBar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        return topBar;
    }

    private Button createPillButton(String icon, String title, Label badgeLabel) {
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

        if (badgeLabel != null) {
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

        String hoverStyle = "-fx-background-color: #F4FBF7;"
                + "-fx-background-radius: 20px;"
                + "-fx-border-color: #2D6A4F;"
                + "-fx-border-radius: 20px;"
                + "-fx-border-width: 1.2px;"
                + "-fx-padding: 0 14px 0 12px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.12), 8, 0, 0, 2);";

        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));

        return btn;
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

    private HBox createProfilePill() {
        profilePillAvatarBox = new StackPane();
        profilePillAvatarBox.setPrefSize(24, 24);
        Text profileIcon = new Text("👤");
        profileIcon.setStyle("-fx-font-size: 14px;");
        profilePillAvatarBox.getChildren().add(profileIcon);

        profilePillNameText = new Text(FarmerProfileStore.name != null ? FarmerProfileStore.name : "Farmer");
        profilePillNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedDot = new Label("✓");
        verifiedDot.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 1px 5px; -fx-background-radius: 8px;");

        Text dropdownArrow = new Text("▾");
        dropdownArrow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #2D6A4F; -fx-font-weight: bold;");

        HBox profileBox = new HBox(6, profilePillAvatarBox, profilePillNameText, verifiedDot, dropdownArrow);
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

    private void togglePopup(VBox target, VBox... others) {
        boolean vis = target.isVisible();
        hideAllPopups(others);
        if (profilePopupRef != null) profilePopupRef.setVisible(false);
        target.setVisible(!vis);
    }

    private void hideAllPopups(VBox... popups) {
        for (VBox p : popups) {
            if (p != null) p.setVisible(false);
        }
    }

    // ============================================================
    // REUSABLE MODERN POPUP CONTAINER WITH DEEP VERTICAL SCROLL
    // ============================================================
    private VBox createInfoPopup(String icon, String title, String subtitle, Node content) {
        VBox box = new VBox(14);
        box.setPadding(new Insets(18, 20, 18, 20));
        box.setPrefSize(500, 520);
        box.setMaxSize(500, 520);
        box.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #D1E7DD;" +
                "-fx-border-radius: 18px;" +
                "-fx-border-width: 1.5px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 24, 0, 0, 8);"
        );

        // Header
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 18px;");
        StackPane iconBox = new StackPane(iconText);
        iconBox.setPrefSize(38, 38);
        iconBox.setMinSize(38, 38);
        iconBox.setMaxSize(38, 38);
        iconBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 10px;");

        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text s = new Text(subtitle);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(2, t, s);
        HBox titleGroup = new HBox(10, iconBox, titleBox);
        titleGroup.setAlignment(Pos.CENTER_LEFT);

        Button close = createCloseButton(() -> box.setVisible(false));

        HBox topBar = new HBox(titleGroup, close);
        topBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleGroup, Priority.ALWAYS);
        topBar.setPadding(new Insets(0, 0, 12, 0));
        topBar.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        box.getChildren().addAll(topBar, content);

        StackPane.setAlignment(box, Pos.TOP_LEFT);
        StackPane.setMargin(box, new Insets(60, 0, 0, 22));
        box.setVisible(false);

        return box;
    }

    // 1. RICH USER GUIDE CONTENT
    private ScrollPane createGuideContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(0, 6, 0, 0));
        box.getChildren().addAll(
                createRichCard("🚜 1. Browse & Reserve Machinery", 
                        "• Search implements by power rating (HP), farm size, and rental duration.\n" +
                        "• Select booking start & return dates with real-time slot availability.\n" +
                        "• Choose optional verified machine operator during checkout.",
                        "Step 1 • Search & Select", "#E8F5E9", "#1B4332"),

                createRichCard("💳 2. Secure Escrow Payment Protection", 
                        "• Your payment remains in 100% secure escrow vault.\n" +
                        "• Provider only receives payout after machine completes satisfactory field operation.\n" +
                        "• Full refund guarantee if machinery fails on-field inspection.",
                        "Step 2 • Escrow Security", "#FEF3C7", "#B45309"),

                createRichCard("👷 3. Hiring Certified Field Operators", 
                        "• Filter operators by machinery specialization (Tractor, Harvester, Drone).\n" +
                        "• Direct contact unlocks instantly once booking request is accepted.\n" +
                        "• Daily wage settlement handled safely through platform.",
                        "Step 3 • Operator Dispatch", "#E0E7FF", "#4338CA"),

                createRichCard("📋 4. Machinery Check-in & Check-out", 
                        "• Inspect engine hours, diesel tank level, and implements with provider.\n" +
                        "• Take quick mobile photos of machinery condition at start of rental.\n" +
                        "• Sign digital completion handover note upon job finish.",
                        "Step 4 • On-Field Checklist", "#DCFCE7", "#15803D"),

                createRichCard("⭐ 5. Rate & Review for Karma Badges", 
                        "• Rate equipment efficiency and operator punctuality (1 to 5 Stars).\n" +
                        "• Earn Kisan Karma rewards for priority rental bookings.",
                        "Step 5 • Feedback", "#F3F4F6", "#374151")
        );
        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setPrefHeight(420);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    // 2. RICH GOVT SCHEMES & SUBSIDIES CONTENT
    private ScrollPane createSchemesContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(0, 6, 0, 0));
        box.getChildren().addAll(
                createRichCard("🏛️ PM-Kisan Samman Nidhi Yojana", 
                        "• Financial Benefit: ₹6,000 per year in 3 installments of ₹2,000 directly via DBT.\n" +
                        "• Eligibility: Small and marginal landholding farmer families across India.\n" +
                        "• Required Docs: Aadhaar Card, 7/12 Land Extract, Active Bank Account.",
                        "100% Direct Cash Support", "#DCFCE7", "#15803D"),

                createRichCard("🚜 SMAM (Sub-Mission on Agricultural Mechanization)", 
                        "• Subsidy Benefit: 40% to 50% capital subsidy on Tractors, Power Tillers, Rotavators & Harvesters.\n" +
                        "• Custom Hiring Centers (CHC): Up to ₹10 Lakh subsidy to establish village machinery hubs.\n" +
                        "• Application Portal: agrimachinery.nic.in (Direct Bank Transfer).",
                        "Up to 50% Machinery Subsidy", "#FEF3C7", "#B45309"),

                createRichCard("⚡ PM Kusum Solar Water Pump Scheme", 
                        "• Solar Subsidy: 60% subsidy for standalone solar agriculture pumps (3HP to 7.5HP).\n" +
                        "• Additional Benefit: Grid-connected solar power generation with surplus power sale.",
                        "60% Solar Pump Subsidy", "#E0E7FF", "#4338CA"),

                createRichCard("🌾 Pradhan Mantri Fasal Bima Yojana (PMFBY)", 
                        "• Premium Rate: Only 1.5% to 2% subsidized premium for Kharif & Rabi crops.\n" +
                        "• Coverage: Comprehensive risk cover for unseasonal rains, drought & pest attacks.",
                        "Subsidized Crop Insurance", "#FCE7F3", "#BE185D")
        );
        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setPrefHeight(420);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    // 3. RICH AGRI ADVISORY & LIVE MANDI PRICES CONTENT
    private ScrollPane createAdvisoryContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(0, 6, 0, 0));
        box.getChildren().addAll(
                createRichCard("📈 Live APMC Mandi Rates (Pune & Western Hubs)", 
                        "• Wheat (Sharbati): ₹2,450 / qtl  [▲ +₹60]\n" +
                        "• Soybean (Yellow): ₹4,850 / qtl  [▲ +₹110]\n" +
                        "• Cotton (Medium Staple): ₹7,200 / qtl  [Stable]\n" +
                        "• Gram (Desi Chana): ₹5,150 / qtl  [▲ +₹40]\n" +
                        "• Sugarcane (FRP Rate): ₹3,150 / ton  [Govt Fixed]",
                        "Today's Live Commodity Ticker", "#E8F5E9", "#1B4332"),

                createRichCard("🌱 Sowing & Soil Preparation Advisory", 
                        "• Soil Moisture: 18% to 22% ideal moisture detected across Pune & Solapur belt.\n" +
                        "• Recommended Implement: 7ft Rotary Tiller with dual-speed gearbox for fine tilth.\n" +
                        "• Seed Treatment: Treat seeds with Trichoderma viride @ 4g/kg seed before drilling.",
                        "Kharif Pre-Sowing Protocol", "#FEF3C7", "#B45309"),

                createRichCard("🌧 Western Maharashtra Weather Forecast", 
                        "• Temperature: 28°C Day / 21°C Night • Humidity: 76%\n" +
                        "• Rain Alert: Light to moderate scattered showers expected over next 48 hours.\n" +
                        "• Drone Advisory: Spraying recommended before 10:00 AM to avoid wind drift.",
                        "48-Hour Microclimate Radar", "#E0E7FF", "#4338CA")
        );
        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setPrefHeight(420);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    // 4. RICH KISAN HELPLINES & EMERGENCY CONTENT
    private ScrollPane createHelplineContent() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(0, 6, 0, 0));
        box.getChildren().addAll(
                createRichCard("📞 National Kisan Call Center", 
                        "• Toll-Free Number: 1800-180-1551\n" +
                        "• Operational Hours: 6:00 AM – 10:00 PM (365 Days)\n" +
                        "• Languages: Marathi, Hindi, English, Kannada & 18 regional dialects.",
                        "Toll-Free National Helpline", "#DCFCE7", "#15803D"),

                createRichCard("💬 WhatsApp Farm Support Desk", 
                        "• WhatsApp Hotline: +91 98220 54321\n" +
                        "• Instant assistance: Share field geo-location, machinery photos or video clips.\n" +
                        "• Average response time: Under 4 minutes.",
                        "Instant Field WhatsApp", "#DCFCE7", "#15803D"),

                createRichCard("🚜 Machinery Breakdown Emergency Hub", 
                        "• Emergency Helpline: 1800-266-9911\n" +
                        "• On-Field Mobile Mechanic Van dispatched within 45 minutes across Pune region.\n" +
                        "• Genuine spare parts & mobile diesel refill assist.",
                        "Roadside Field Assistance", "#FEF3C7", "#B45309"),

                createRichCard("🌾 District Agriculture Officer (Pune HQ)", 
                        "• Direct Office: +91 20 2612 3456\n" +
                        "• Address: Central Building, Agriculture Commissionerate, Pune 411001.",
                        "District Govt Office", "#F3F4F6", "#374151")
        );
        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setPrefHeight(420);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private VBox createRichCard(String title, String details, String tag, String tagBg, String tagColor) {
        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332; -fx-font-family: 'Poppins';");

        Label tagLabel = new Label(tag);
        tagLabel.setStyle("-fx-background-color: " + tagBg + "; -fx-text-fill: " + tagColor + "; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3px 8px; -fx-background-radius: 6px;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox headerRow = new HBox(8, titleText, sp, tagLabel);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Text descText = new Text(details);
        descText.setWrappingWidth(420);
        descText.setStyle("-fx-font-size: 12px; -fx-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-line-spacing: 3px;");

        VBox card = new VBox(8, headerRow, descText);
        card.setPadding(new Insets(14, 16, 14, 16));
        card.setStyle(
            "-fx-background-color: #F8FAF8;" +
            "-fx-background-radius: 12px;" +
            "-fx-border-color: #E2EBE5;" +
            "-fx-border-radius: 12px;" +
            "-fx-border-width: 1px;"
        );
        return card;
    }

    // ============================================================
    // STUNNING PROFILE POPUP
    // ============================================================
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
        Text avatar = new Text("👤");
        avatar.setStyle("-fx-font-size: 18px;");
        StackPane avatarBox = new StackPane(avatar);
        avatarBox.setPrefSize(38, 38);
        avatarBox.setMinSize(38, 38);
        avatarBox.setMaxSize(38, 38);
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 10px;");

        Text title = new Text("Farmer Profile & Location");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Personal credentials & operational farm location");
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
        VBox content = new VBox(14);
        content.setPadding(new Insets(0, 6, 0, 0));

        // Section 1: Personal Credentials
        Text sec1 = new Text("Personal Credentials");
        sec1.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox credCard = new VBox(8);
        credCard.setPadding(new Insets(12, 14, 12, 14));
        credCard.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-radius: 12px; -fx-border-width: 1px;");

        modalAvatarIcon = new Text("👤");
        modalAvatarIcon.setStyle("-fx-font-size: 20px;");
        modalAvatarView = new ImageView();
        modalAvatarView.setFitWidth(44);
        modalAvatarView.setFitHeight(44);
        modalAvatarView.setClip(new javafx.scene.shape.Circle(22, 22, 22));
        modalAvatarView.setVisible(false);
        modalAvatarView.setManaged(false);

        StackPane photoBox = new StackPane(modalAvatarIcon, modalAvatarView);
        photoBox.setPrefSize(44, 44);
        photoBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 22; -fx-border-color: #A5D6A7; -fx-border-width: 1.2; -fx-border-radius: 22;");

        Button changePicBtn = new Button("📷 Change Photo");
        changePicBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 4 10;");

        Label picMsg = new Label();
        picMsg.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-text-fill: #15803D;");

        changePicBtn.setOnAction(e -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.setTitle("Select Profile Image");
            chooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.bmp")
            );
            javafx.stage.Window win = modal.getScene() != null ? modal.getScene().getWindow() : null;
            java.io.File file = chooser.showOpenDialog(win);
            if (file != null) {
                picMsg.setText("Uploading photo...");
                changePicBtn.setDisable(true);
                javafx.concurrent.Task<String> uploadTask = new javafx.concurrent.Task<>() {
                    @Override
                    protected String call() {
                        return com.desgin.config.CloudinaryConfig.uploadImage(file);
                    }
                };
                uploadTask.setOnSucceeded(ev -> {
                    changePicBtn.setDisable(false);
                    String url = uploadTask.getValue();
                    if (url != null) {
                        picMsg.setText("✓ Photo updated!");
                        FarmerProfileStore.setProfilePic(url);
                        new Thread(() -> new AuthDAO().updateProfilePic(FarmerProfileStore.email, "Farmer", url)).start();
                    } else {
                        picMsg.setText("Upload failed.");
                    }
                });
                uploadTask.setOnFailed(ev -> {
                    changePicBtn.setDisable(false);
                    picMsg.setText("Upload error.");
                });
                new Thread(uploadTask).start();
            }
        });
        HBox photoRow = new HBox(12, photoBox, new VBox(3, changePicBtn, picMsg));
        photoRow.setAlignment(Pos.CENTER_LEFT);

        nameValText = new Text(FarmerProfileStore.name != null ? FarmerProfileStore.name : "Not Set");
        emailValText = new Text(FarmerProfileStore.email != null ? FarmerProfileStore.email : "Not Set");
        phoneValText = new Text(FarmerProfileStore.phone != null ? FarmerProfileStore.phone : "Not Set");

        HBox nameRow = createInfoRowWithAction("Full Name:", nameValText, ProfileManagement::redirectToSettings);
        HBox emailRow = createInfoRowWithAction("Email Address:", emailValText, ProfileManagement::redirectToSettings);
        HBox phoneRow = createInfoRowWithAction("Mobile No.:", phoneValText, ProfileManagement::redirectToSettings);

        credCard.getChildren().addAll(photoRow, nameRow, emailRow, phoneRow);

        // Section 2: Location
        Text sec2 = new Text("📍 Operational Farm Location (For Machinery Matching)");
        sec2.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox locCard = new VBox(10);
        locCard.setPadding(new Insets(12, 14, 12, 14));
        locCard.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-radius: 12px; -fx-border-width: 1px;");

        modalTownField = new TextField(FarmerProfileStore.town != null ? FarmerProfileStore.town : "");
        modalTownField.setPromptText("Village / Town (e.g. Pune, Baramati)");
        styleField(modalTownField);

        modalDistrictField = new TextField(FarmerProfileStore.district != null ? FarmerProfileStore.district : "");
        modalDistrictField.setPromptText("District");
        styleField(modalDistrictField);

        modalStateField = new TextField(FarmerProfileStore.state != null ? FarmerProfileStore.state : "");
        modalStateField.setPromptText("State");
        styleField(modalStateField);

        modalPincodeField = new TextField(FarmerProfileStore.pincode != null ? FarmerProfileStore.pincode : "");
        modalPincodeField.setPromptText("Pincode");
        styleField(modalPincodeField);

        GridPane locGrid = new GridPane();
        locGrid.setHgap(12);
        locGrid.setVgap(8);
        locGrid.add(createFieldGroup("Village / Town", modalTownField), 0, 0);
        locGrid.add(createFieldGroup("District", modalDistrictField), 1, 0);
        locGrid.add(createFieldGroup("State", modalStateField), 0, 1);
        locGrid.add(createFieldGroup("Pincode", modalPincodeField), 1, 1);

        Label locMsg = new Label();
        locMsg.setVisible(false);
        locMsg.setManaged(false);

        Button saveLocBtn = new Button("Save Location 📍");
        saveLocBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 6px 18px; -fx-cursor: hand;");
        saveLocBtn.setOnAction(e -> {
            String t = modalTownField.getText() != null ? modalTownField.getText().trim() : "";
            String d = modalDistrictField.getText() != null ? modalDistrictField.getText().trim() : "";
            String s = modalStateField.getText() != null ? modalStateField.getText().trim() : "";
            String p = modalPincodeField.getText() != null ? modalPincodeField.getText().trim() : "";

            FarmerProfileStore.setLocation(t, d, s, p);

            locMsg.setText("⏳ Saving to Firebase...");
            locMsg.setStyle("-fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-color: #E8F5E9; -fx-padding: 6px 10px; -fx-background-radius: 6px;");
            locMsg.setVisible(true);
            locMsg.setManaged(true);

            // Asynchronously save to Firebase/Firestore in background thread
            new Thread(() -> {
                String farmerEmail = FarmerProfileStore.email;
                boolean savedToDb = false;
                if (farmerEmail != null && !farmerEmail.trim().isEmpty()) {
                    savedToDb = new com.desgin.controller.AuthenticateController().updateLocation(farmerEmail, "Farmer", t, d, s, p);
                }
                final boolean success = savedToDb;
                javafx.application.Platform.runLater(() -> {
                    if (success) {
                        locMsg.setText("✓ Location saved to Firebase & matching active (" + FarmerProfileStore.town + ")!");
                        locMsg.setStyle("-fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-color: #DCFCE7; -fx-padding: 6px 10px; -fx-background-radius: 6px;");
                    } else {
                        locMsg.setText("✓ Location set to " + FarmerProfileStore.town + "! Matching active.");
                        locMsg.setStyle("-fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-color: #DCFCE7; -fx-padding: 6px 10px; -fx-background-radius: 6px;");
                    }
                });
            }).start();
        });

        HBox locActionBox = new HBox(10, saveLocBtn, locMsg);
        locActionBox.setAlignment(Pos.CENTER_LEFT);

        locCard.getChildren().addAll(locGrid, locActionBox);

        content.getChildren().addAll(sec1, credCard, sec2, locCard);

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

    public static void redirectToSettings() {
        if (profilePopupRef != null) {
            profilePopupRef.setVisible(false);
        }
        ProfileManagement.setHeaderTitle("Settings & Preferences ⚙", "Manage your account credentials and security");
        if (FarmerDashboard.borderPane != null) {
            FarmerDashboard.borderPane.setCenter(Settings.getSetting());
        }
        if (LeftSideBar.settingsBtn1 != null && LeftSideBar.navigationButtons != null) {
            LeftSideBar.setActiveButton(LeftSideBar.settingsBtn1, LeftSideBar.navigationButtons);
        }
    }

    private static HBox createInfoRowWithAction(String label, Text valText, Runnable onEdit) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        lbl.setPrefWidth(110);

        valText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button editBtn = new Button("Edit ⚙");
        editBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 3px 12px; -fx-background-radius: 6px; -fx-cursor: hand; -fx-border-color: #D1E7DD; -fx-border-radius: 6px;");
        editBtn.setOnAction(e -> {
            if (onEdit != null) {
                onEdit.run();
            }
        });

        HBox row = new HBox(8, lbl, valText, sp, editBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }


    private static void styleField(TextField field) {
        field.setPrefHeight(36);
        field.setPrefWidth(190);
        field.setStyle(
                "-fx-background-color: #FFFFFF;" 
                + "-fx-border-color: #D1E7DD;" 
                + "-fx-border-radius: 8px;" 
                + "-fx-background-radius: 8px;" 
                + "-fx-padding: 0 10px;" 
                + "-fx-font-family: 'Poppins';"
                + "-fx-font-size: 12px;");
    }

    private static VBox createFieldGroup(String labelText, TextField field) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        VBox box = new VBox(3, label, field);
        return box;
    }

    // ============================================================
    // STUNNING NOTIFICATIONS MODAL
    // ============================================================
    private VBox createNotificationModal() {
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

        Text title = new Text("Recent Activity & Alerts");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Rental confirmations, operator dispatches & farm alerts");
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
        farmerNotifList = new VBox(10);
        farmerNotifList.setPadding(new Insets(0, 6, 0, 0));
        refreshFarmerNotifications();

        ScrollPane sp = new ScrollPane(farmerNotifList);
        sp.setFitToWidth(true);
        sp.setPrefHeight(380);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        return modal;
    }

    public static void refreshFarmerNotifications() {
        Thread t = new Thread(() -> {
            try {
                String mail = FarmerProfileStore.email;
                if (mail == null || mail.trim().isEmpty()) return;

                // 1. Fetch real notifications from NotificationDAO
                java.util.List<com.desgin.model.NotificationModel> dbNotifs = new com.desgin.dao.NotificationDAO().getNotificationsByUser(mail);
                int unreadCount = 0;
                for (com.desgin.model.NotificationModel n : dbNotifs) {
                    if (!n.isRead()) unreadCount++;
                }

                // 2. Fetch farmer's own requests
                List<com.desgin.model.RentalRequestModel> list = new com.desgin.dao.RentalRequestDAO().getRequestsByFarmer(mail);
                final int finalUnread = unreadCount;
                final java.util.List<com.desgin.model.NotificationModel> finalNotifs = dbNotifs;
                final List<com.desgin.model.RentalRequestModel> finalRequests = list;

                javafx.application.Platform.runLater(() -> {
                    if (farmerNotifBadge != null) {
                        if (finalUnread > 0) {
                            farmerNotifBadge.setText(String.valueOf(finalUnread));
                            farmerNotifBadge.setVisible(true);
                            farmerNotifBadge.setManaged(true);
                        } else {
                            farmerNotifBadge.setVisible(false);
                            farmerNotifBadge.setManaged(false);
                        }
                    }

                    if (farmerNotifList != null) {
                        farmerNotifList.getChildren().clear();

                        if (!finalNotifs.isEmpty()) {
                            for (com.desgin.model.NotificationModel n : finalNotifs.stream().limit(6).toList()) {
                                String bg = n.isRead() ? "#F3F4F6" : "#DCFCE7";
                                String fg = n.isRead() ? "#4B5563" : "#15803D";
                                farmerNotifList.getChildren().add(createNotifCard(n.getTitle(), n.getMessage(), n.getCreatedAt(), n.getType(), bg, fg));
                            }
                        } else if (!finalRequests.isEmpty()) {
                            for (com.desgin.model.RentalRequestModel r : finalRequests.stream().limit(6).toList()) {
                                String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "PENDING";
                                String title;
                                String desc;
                                String time = r.getStartDate() != null ? r.getStartDate() : "Recent";
                                String bg;
                                String fg;

                                if ("APPROVED".equals(st) || "ACCEPTED".equals(st)) {
                                    title = "✅ Booking Approved";
                                    desc = "Your request for " + r.getMachineryName() + " was approved by provider " + (r.getProviderName() != null ? r.getProviderName() : "") + "!";
                                    bg = "#DCFCE7";
                                    fg = "#15803D";
                                } else if ("DECLINED".equals(st) || "REJECTED".equals(st)) {
                                    title = "❌ Booking Declined";
                                    desc = "Provider was unable to fulfill request for " + r.getMachineryName() + ".";
                                    bg = "#FEE2E2";
                                    fg = "#DC2626";
                                } else if ("COMPLETED".equals(st)) {
                                    title = "✔ Rental Completed";
                                    desc = "Rental completed for " + r.getMachineryName() + ". Thank you!";
                                    bg = "#E0E7FF";
                                    fg = "#4338CA";
                                } else {
                                    title = "⏳ Request Pending";
                                    desc = "Booking inquiry for " + r.getMachineryName() + " (" + r.getDays() + " days) is awaiting provider confirmation.";
                                    bg = "#FFF3E0";
                                    fg = "#E65100";
                                }
                                farmerNotifList.getChildren().add(createNotifCard(title, desc, time, st, bg, fg));
                            }
                        } else {
                            farmerNotifList.getChildren().add(createNotifCard("🔔 No Notifications", "You are all caught up! Booking and payment alerts will appear here.", "Just now", "SYSTEM", "#F3F4F6", "#4B5563"));
                        }
                    }
                });
            } catch (Exception ignored) {}
        });
        t.setDaemon(true);
        t.start();
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
}