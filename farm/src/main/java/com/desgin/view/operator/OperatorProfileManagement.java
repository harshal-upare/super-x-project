package com.desgin.view.operator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.desgin.config.CloudinaryConfig;
import com.desgin.dao.AuthDAO;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class OperatorProfileManagement {

    private static VBox profilePopupRef;
    private static Text profilePillNameText;
    private static StackPane profilePillIconContainer;
    private static Label modalNameLabel;
    private static Label modalContactLabel;
    private static Label modalLocationLabel;
    private static Label modalLicenseLabel;
    private static Label modalExpLabel;
    private static Label modalProfLabel;
    private static ImageView modalPhotoView;
    private static ImageView modalDlView;
    private static Label modalDlStatusLabel;
    private static Text modalHeaderName;

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
        if (profilePillIconContainer != null) {
            updateProfilePillAvatar(profilePillIconContainer, "👷");
        }
        if (modalHeaderName != null) {
            modalHeaderName.setText(OperatorProfileStore.name != null ? OperatorProfileStore.name : "Operator");
        }
        if (modalNameLabel != null) {
            modalNameLabel.setText(OperatorProfileStore.name != null && !OperatorProfileStore.name.isEmpty() ? OperatorProfileStore.name : "-");
        }
        if (modalContactLabel != null) {
            modalContactLabel.setText(OperatorProfileStore.phone != null && !OperatorProfileStore.phone.isEmpty() ? OperatorProfileStore.phone : "-");
        }
        if (modalLocationLabel != null) {
            modalLocationLabel.setText(OperatorProfileStore.zone != null && !OperatorProfileStore.zone.isEmpty() ? OperatorProfileStore.zone : "-");
        }
        if (modalLicenseLabel != null) {
            modalLicenseLabel.setText(OperatorProfileStore.licenseNo != null && !OperatorProfileStore.licenseNo.isEmpty() ? OperatorProfileStore.licenseNo : "-");
        }
        if (modalExpLabel != null) {
            modalExpLabel.setText(OperatorProfileStore.drivingExperience != null && !OperatorProfileStore.drivingExperience.isEmpty() ? OperatorProfileStore.drivingExperience : "-");
        }
        if (modalProfLabel != null) {
            modalProfLabel.setText(OperatorProfileStore.equipmentProfession != null && !OperatorProfileStore.equipmentProfession.isEmpty() ? OperatorProfileStore.equipmentProfession : "-");
        }
        if (modalPhotoView != null && OperatorProfileStore.profilePic != null && !OperatorProfileStore.profilePic.isEmpty()) {
            try { modalPhotoView.setImage(new Image(OperatorProfileStore.profilePic, true)); } catch (Exception ignored) {}
        }
        if (modalDlView != null && OperatorProfileStore.licenseImage != null && !OperatorProfileStore.licenseImage.isEmpty()) {
            try { modalDlView.setImage(new Image(OperatorProfileStore.licenseImage, true)); } catch (Exception ignored) {}
            if (modalDlStatusLabel != null) {
                modalDlStatusLabel.setText("✓ Verified DL Attached");
                modalDlStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #15803D; -fx-font-weight: bold;");
            }
        }
    }

    private static Label operatorNotifBadge;
    private static VBox operatorNotifList;
    private static VBox notifPopupRef;
    private static final List<NotificationItemData> currentUnreadNotifs = new ArrayList<>();

    private static class NotificationItemData {
        String id;
        String title;
        String desc;
        String time;
        String tag;
        String tagBg;
        String tagColor;
        String relatedId;

        public NotificationItemData(String id, String title, String desc, String time, String tag, String tagBg, String tagColor, String relatedId) {
            this.id = id;
            this.title = title;
            this.desc = desc;
            this.time = time;
            this.tag = tag;
            this.tagBg = tagBg;
            this.tagColor = tagColor;
            this.relatedId = relatedId;
        }
    }

    public static void refreshOperatorNotifications() {
        Thread t = new Thread(() -> {
            try {
                String email = OperatorProfileStore.email;
                if (email == null || email.trim().isEmpty()) email = "operator@farmequip.com";

                java.util.List<com.desgin.model.NotificationModel> dbNotifs = new com.desgin.dao.NotificationDAO().getNotificationsByUser(email);
                java.util.List<com.desgin.model.RentalRequestModel> reqs = new com.desgin.dao.RentalRequestDAO().getRequestsByOperator(email);

                List<NotificationItemData> items = new ArrayList<>();
                java.util.Set<String> processedReqIds = new java.util.HashSet<>();

                // 1. Process notifications from DB
                for (com.desgin.model.NotificationModel n : dbNotifs) {
                    if (n.isRead() || OperatorProfileStore.isNotificationRead(n.getNotificationId())) continue;

                    String tag = "🔔 UPDATE";
                    String tagBg = "#DCFCE7";
                    String tagColor = "#15803D";

                    if ("PAYMENT".equalsIgnoreCase(n.getType())) {
                        tag = "💰 PAYMENT DONE";
                        tagBg = "#DCFCE7";
                        tagColor = "#15803D";
                    } else if ("REQUEST".equalsIgnoreCase(n.getType()) || "BOOKING".equalsIgnoreCase(n.getType())) {
                        tag = "🚜 NEW REQUEST";
                        tagBg = "#FEF3C7";
                        tagColor = "#B45309";
                    }

                    items.add(new NotificationItemData(
                        n.getNotificationId(),
                        n.getTitle(),
                        n.getMessage(),
                        n.getCreatedAt() != null ? n.getCreatedAt() : "Recently",
                        tag, tagBg, tagColor,
                        n.getRelatedId()
                    ));
                    if (n.getRelatedId() != null) processedReqIds.add(n.getRelatedId());
                }

                // 2. Real-time requests fallback
                for (com.desgin.model.RentalRequestModel r : reqs) {
                    if (r.getRequestId() == null) continue;
                    if (processedReqIds.contains(r.getRequestId())) continue;

                    String reqId = r.getRequestId();
                    if ("PENDING".equalsIgnoreCase(r.getOperatorStatus())) {
                        String notifId = "NOTIF_REQ_" + reqId;
                        if (!OperatorProfileStore.isNotificationRead(notifId)) {
                            items.add(new NotificationItemData(
                                notifId,
                                "🚜 New Work Request: " + (r.getMachineryName() != null ? r.getMachineryName() : "Field Duty"),
                                "Farmer " + (r.getFarmerName() != null ? r.getFarmerName() : "Farmer") + " sent a request for " + (r.getDays() > 0 ? r.getDays() + " Days" : "Standard Shift") + " at " + (r.getFarmerLocation() != null ? r.getFarmerLocation() : "Field Plot") + ". Total wage: ₹" + String.format("%,d", r.getTotalAmount()),
                                r.getCreatedAt() != null ? r.getCreatedAt() : "Recent",
                                "🚜 NEW REQUEST",
                                "#FEF3C7", "#B45309",
                                reqId
                            ));
                        }
                    } else if ("PAID".equalsIgnoreCase(r.getPaymentStatus()) && ("CONFIRMED".equalsIgnoreCase(r.getStatus()) || "ACCEPTED".equalsIgnoreCase(r.getStatus()))) {
                        String notifId = "NOTIF_PAY_" + reqId;
                        if (!OperatorProfileStore.isNotificationRead(notifId)) {
                            items.add(new NotificationItemData(
                                notifId,
                                "💰 Wage Paid: Job #" + reqId,
                                "Farmer " + (r.getFarmerName() != null ? r.getFarmerName() : "Farmer") + " paid ₹" + String.format("%,d", r.getTotalAmount()) + " via Razorpay. Shift is ready to start!",
                                r.getUpdatedAt() != null ? r.getUpdatedAt() : "Recent",
                                "💰 PAYMENT DONE",
                                "#DCFCE7", "#15803D",
                                reqId
                            ));
                        }
                    } else if ("IN_PROGRESS".equalsIgnoreCase(r.getStatus())) {
                        String notifId = "NOTIF_ACT_" + reqId;
                        if (!OperatorProfileStore.isNotificationRead(notifId)) {
                            items.add(new NotificationItemData(
                                notifId,
                                "⏱ Shift In Progress: " + (r.getMachineryName() != null ? r.getMachineryName() : "Task"),
                                "Field shift is running with active countdown. Complete button unlocks when timer finishes.",
                                "Active",
                                "⏱ SHIFT ACTIVE",
                                "#E0F2FE", "#0284C7",
                                reqId
                            ));
                        }
                    }
                }

                javafx.application.Platform.runLater(() -> {
                    currentUnreadNotifs.clear();
                    currentUnreadNotifs.addAll(items);
                    updateNotifUI();
                });
            } catch (Exception ignored) {}
        });
        t.setDaemon(true);
        t.start();
    }

    private static void updateNotifUI() {
        int count = currentUnreadNotifs.size();
        if (operatorNotifBadge != null) {
            if (count > 0) {
                operatorNotifBadge.setText(String.valueOf(count));
                operatorNotifBadge.setVisible(true);
                operatorNotifBadge.setManaged(true);
            } else {
                operatorNotifBadge.setVisible(false);
                operatorNotifBadge.setManaged(false);
            }
        }

        if (operatorNotifList != null) {
            operatorNotifList.getChildren().clear();
            if (currentUnreadNotifs.isEmpty()) {
                VBox empty = new VBox(10);
                empty.setAlignment(Pos.CENTER);
                empty.setPadding(new Insets(30, 20, 30, 20));

                Text bell = new Text("🔔");
                bell.setStyle("-fx-font-size: 32px;");

                Text t = new Text("No New Notifications");
                t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

                Text sub = new Text("You're all caught up! New field requests and wage payments will appear here.");
                sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280; -fx-text-alignment: center;");
                sub.setWrappingWidth(360);

                empty.getChildren().addAll(bell, t, sub);
                operatorNotifList.getChildren().add(empty);
            } else {
                for (NotificationItemData item : currentUnreadNotifs) {
                    operatorNotifList.getChildren().add(createInteractiveNotifCard(item));
                }
            }
        }
    }

    public HBox getProfile(StackPane root) {
        updateHeaderGreeting();

        operatorNotifBadge = new Label("");
        operatorNotifBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");
        operatorNotifBadge.setVisible(false);
        operatorNotifBadge.setManaged(false);

        Button notificationBtn1 = createPillButtonWithBadge("🔔", "Notifications", operatorNotifBadge);

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
            if (!isVis) refreshOperatorNotifications();
        });

        refreshOperatorNotifications();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(16, headerTitleBox, spacer, notificationBtn1, profileBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 24, 10, 24));
        topBar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        return topBar;
    }

    private Button createPillButtonWithBadge(String icon, String title, Label badgeLabel) {
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
        if (badgeLabel != null) content.getChildren().add(badgeLabel);
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
        profilePillIconContainer = new StackPane();
        profilePillIconContainer.setPrefSize(24, 24);
        profilePillIconContainer.setMinSize(24, 24);
        profilePillIconContainer.setMaxSize(24, 24);
        updateProfilePillAvatar(profilePillIconContainer, icon);

        profilePillNameText = new Text(name != null ? name : "Operator");
        profilePillNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedDot = new Label(tag != null ? tag : "Certified");
        verifiedDot.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 1px 6px; -fx-background-radius: 8px;");

        Text dropdownArrow = new Text("▾");
        dropdownArrow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #2D6A4F; -fx-font-weight: bold;");

        HBox profileBox = new HBox(6, profilePillIconContainer, profilePillNameText, verifiedDot, dropdownArrow);
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

    private static void updateProfilePillAvatar(StackPane container, String fallbackIcon) {
        if (container == null) return;
        container.getChildren().clear();
        if (OperatorProfileStore.profilePic != null && !OperatorProfileStore.profilePic.trim().isEmpty()) {
            try {
                ImageView iv = new ImageView(new Image(OperatorProfileStore.profilePic.trim(), true));
                iv.setFitWidth(24);
                iv.setFitHeight(24);
                Circle clip = new Circle(12, 12, 12);
                iv.setClip(clip);
                container.getChildren().add(iv);
                return;
            } catch (Exception ignored) {}
        }
        Text profileIcon = new Text(fallbackIcon != null ? fallbackIcon : "👷");
        profileIcon.setStyle("-fx-font-size: 14px;");
        container.getChildren().add(profileIcon);
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
        modal.setPrefSize(520, 560);
        modal.setMaxSize(520, 560);
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

        // Photo & Header Row (Display Only)
        modalPhotoView = new ImageView();
        modalPhotoView.setFitWidth(52);
        modalPhotoView.setFitHeight(52);
        Circle photoClip = new Circle(26, 26, 26);
        modalPhotoView.setClip(photoClip);
        if (OperatorProfileStore.profilePic != null && !OperatorProfileStore.profilePic.isEmpty()) {
            try { modalPhotoView.setImage(new Image(OperatorProfileStore.profilePic, true)); } catch (Exception ignored) {}
        }

        Text pIcon = new Text("👨‍🌾");
        pIcon.setStyle("-fx-font-size: 24px;");
        StackPane photoAvatarBox = new StackPane(pIcon, modalPhotoView);
        photoAvatarBox.setPrefSize(52, 52);
        photoAvatarBox.setMaxSize(52, 52);
        photoAvatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 26; -fx-border-color: #A5D6A7; -fx-border-width: 1.5; -fx-border-radius: 26;");

        modalHeaderName = new Text(OperatorProfileStore.name != null && !OperatorProfileStore.name.isEmpty() ? OperatorProfileStore.name : "Operator");
        modalHeaderName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label badgeLbl = new Label("🛡️ Certified Heavy Machinery Operator");
        badgeLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-padding: 3 8; -fx-background-radius: 6;");

        VBox nameAndBadge = new VBox(3, modalHeaderName, badgeLbl);
        nameAndBadge.setAlignment(Pos.CENTER_LEFT);

        HBox photoRow = new HBox(12, photoAvatarBox, nameAndBadge);
        photoRow.setAlignment(Pos.CENTER_LEFT);
        photoRow.setPadding(new Insets(2, 0, 6, 0));

        // Form content (Read Only Display)
        VBox content = new VBox(14);
        content.setPadding(new Insets(0, 6, 0, 0));

        modalNameLabel = new Label(OperatorProfileStore.name != null && !OperatorProfileStore.name.isEmpty() ? OperatorProfileStore.name : "-");
        modalContactLabel = new Label(OperatorProfileStore.phone != null && !OperatorProfileStore.phone.isEmpty() ? OperatorProfileStore.phone : "-");
        modalLocationLabel = new Label(OperatorProfileStore.zone != null && !OperatorProfileStore.zone.isEmpty() ? OperatorProfileStore.zone : "-");
        modalLicenseLabel = new Label(OperatorProfileStore.licenseNo != null && !OperatorProfileStore.licenseNo.isEmpty() ? OperatorProfileStore.licenseNo : "-");
        modalExpLabel = new Label(OperatorProfileStore.drivingExperience != null && !OperatorProfileStore.drivingExperience.isEmpty() ? OperatorProfileStore.drivingExperience : "-");
        modalProfLabel = new Label(OperatorProfileStore.equipmentProfession != null && !OperatorProfileStore.equipmentProfession.isEmpty() ? OperatorProfileStore.equipmentProfession : "-");

        // DL Preview Box (Display Only)
        modalDlView = new ImageView();
        modalDlView.setFitWidth(50);
        modalDlView.setFitHeight(34);
        modalDlView.setPreserveRatio(true);
        if (OperatorProfileStore.licenseImage != null && !OperatorProfileStore.licenseImage.isEmpty()) {
            try { modalDlView.setImage(new Image(OperatorProfileStore.licenseImage, true)); } catch (Exception ignored) {}
        }
        Text dlThumbIcon = new Text("📄");
        dlThumbIcon.setStyle("-fx-font-size: 16px;");
        StackPane dlThumbBox = new StackPane(dlThumbIcon, modalDlView);
        dlThumbBox.setPrefSize(50, 34);
        dlThumbBox.setStyle("-fx-background-color: #F4F9F4; -fx-background-radius: 6; -fx-border-color: #C2E0CE; -fx-border-radius: 6; -fx-border-width: 1;");

        modalDlStatusLabel = new Label(
            (OperatorProfileStore.licenseImage != null && !OperatorProfileStore.licenseImage.isEmpty()) 
            ? "✓ Official Driving License on File" 
            : "No DL document uploaded yet"
        );
        modalDlStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: #15803D;");

        HBox dlDisplayRow = new HBox(12, dlThumbBox, modalDlStatusLabel);
        dlDisplayRow.setAlignment(Pos.CENTER_LEFT);
        dlDisplayRow.setPadding(new Insets(6, 12, 6, 12));
        dlDisplayRow.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        VBox card = new VBox(12);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-radius: 12px; -fx-border-width: 1px;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.add(createDisplayField("Full Name", modalNameLabel), 0, 0);
        grid.add(createDisplayField("Contact Mobile", modalContactLabel), 1, 0);
        grid.add(createDisplayField("Operational Zone", modalLocationLabel), 0, 1);
        grid.add(createDisplayField("Operator License No.", modalLicenseLabel), 1, 1);
        grid.add(createDisplayField("Driving Experience", modalExpLabel), 0, 2);
        grid.add(createDisplayField("Equipment Profession", modalProfLabel), 1, 2);
        grid.add(createCustomFieldGroup("Driving License (DL Document)", dlDisplayRow), 0, 3, 2, 1);

        // Bottom Action Button: Redirects to Settings
        Button editBtn = new Button("✏  Edit Profile Details in Settings");
        editBtn.setMaxWidth(Double.MAX_VALUE);
        editBtn.setPrefHeight(40);
        editBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8px;" +
            "-fx-padding: 8 20;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 6, 0, 0, 2);"
        );
        HBox.setHgrow(editBtn, Priority.ALWAYS);
        editBtn.setOnAction(e -> {
            modal.setVisible(false);
            OperatorLeftSideBar.navigateToSettings();
        });

        HBox act = new HBox(editBtn);
        act.setAlignment(Pos.CENTER);
        act.setPadding(new Insets(6, 0, 0, 0));

        card.getChildren().addAll(photoRow, grid, act);
        content.getChildren().add(card);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setPrefHeight(450);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        return modal;
    }

    private static VBox createDisplayField(String title, Label valLbl) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563; -fx-font-weight: 600;");

        valLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #1B4332; -fx-font-weight: 600;");
        valLbl.setWrapText(true);

        HBox valBox = new HBox(valLbl);
        valBox.setAlignment(Pos.CENTER_LEFT);
        valBox.setPadding(new Insets(8, 12, 8, 12));
        valBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        valBox.setPrefWidth(205);
        valBox.setMinWidth(180);

        return new VBox(4, t, valBox);
    }

    private VBox createNotificationModal() {
        VBox modal = new VBox(14);
        modal.setPadding(new Insets(18, 20, 18, 20));
        modal.setPrefSize(500, 460);
        modal.setMaxSize(500, 460);
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

        Text title = new Text("Operator Shift Alerts & Requests");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Field task assignments, daily wage credits & schedule updates");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(2, title, sub);
        HBox titleGroup = new HBox(10, bellBox, titleBox);
        titleGroup.setAlignment(Pos.CENTER_LEFT);

        Button markAllBtn = new Button("✓ Mark All Read");
        markAllBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-cursor: hand; -fx-padding: 4 8;");
        markAllBtn.setOnAction(e -> {
            for (NotificationItemData itm : new ArrayList<>(currentUnreadNotifs)) {
                OperatorProfileStore.markNotificationRead(itm.id);
                new Thread(() -> {
                    try {
                        new com.desgin.dao.NotificationDAO().markAsRead(itm.id);
                    } catch (Exception ignored) {}
                }).start();
            }
            currentUnreadNotifs.clear();
            updateNotifUI();
        });

        Button close = createCloseButton(() -> modal.setVisible(false));

        HBox topActions = new HBox(8, markAllBtn, close);
        topActions.setAlignment(Pos.CENTER_RIGHT);

        HBox topBar = new HBox(titleGroup, topActions);
        topBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleGroup, Priority.ALWAYS);
        topBar.setPadding(new Insets(0, 0, 12, 0));
        topBar.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        // Items
        operatorNotifList = new VBox(10);
        operatorNotifList.setPadding(new Insets(0, 6, 0, 0));
        ScrollPane sp = new ScrollPane(operatorNotifList);
        sp.setFitToWidth(true);
        sp.setPrefHeight(360);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);
        notifPopupRef = modal;

        return modal;
    }

    private static VBox createInteractiveNotifCard(NotificationItemData item) {
        Text t = new Text(item.title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label tagLabel = new Label(item.tag);
        tagLabel.setStyle("-fx-background-color: " + item.tagBg + "; -fx-text-fill: " + item.tagColor + "; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 4px;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox topRow = new HBox(8, t, sp, tagLabel);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Text d = new Text(item.desc);
        d.setWrappingWidth(390);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-line-spacing: 2px;");

        Text tm = new Text("🕒 " + item.time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #9CA3AF;");

        Region sp2 = new Region();
        HBox.setHgrow(sp2, Priority.ALWAYS);

        Button openBtn = new Button("Open in Jobs ➔");
        openBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-cursor: hand; -fx-padding: 4 10;");

        Button markReadBtn = new Button("✓ Mark Read");
        markReadBtn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 500; -fx-background-radius: 6px; -fx-cursor: hand; -fx-padding: 4 8;");

        HBox actionRow = new HBox(8, tm, sp2, markReadBtn, openBtn);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, topRow, d, actionRow);
        card.setPadding(new Insets(12, 14, 12, 14));
        card.setStyle(
                "-fx-background-color: #F8FAF8;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: #D1E7DD;" +
                "-fx-border-radius: 12px;" +
                "-fx-border-width: 1px;" +
                "-fx-cursor: hand;"
        );

        Runnable markReadAction = () -> {
            OperatorProfileStore.markNotificationRead(item.id);
            new Thread(() -> {
                try {
                    new com.desgin.dao.NotificationDAO().markAsRead(item.id);
                } catch (Exception ignored) {}
            }).start();
            currentUnreadNotifs.remove(item);
            updateNotifUI();
        };

        markReadBtn.setOnAction(e -> {
            e.consume();
            markReadAction.run();
        });

        Runnable navigateAction = () -> {
            markReadAction.run();
            if (notifPopupRef != null) notifPopupRef.setVisible(false);
            if (item.tag != null && (item.tag.toUpperCase().contains("REQUEST") || item.tag.toUpperCase().contains("BOOKING") || (item.title != null && item.title.toUpperCase().contains("REQUEST")))) {
                OperatorLeftSideBar.navigateToJobRequests();
            } else {
                OperatorLeftSideBar.navigateToJobs();
            }
        };

        openBtn.setOnAction(e -> {
            e.consume();
            navigateAction.run();
        });

        card.setOnMouseClicked(e -> {
            navigateAction.run();
        });

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

    private static VBox createCustomFieldGroup(String label, javafx.scene.Node n) {
        Label l = new Label(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        return new VBox(3, l, n);
    }
}
