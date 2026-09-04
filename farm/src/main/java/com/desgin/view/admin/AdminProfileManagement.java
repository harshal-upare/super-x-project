package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.NotificationDAO;
import com.desgin.model.NotificationModel;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class AdminProfileManagement {

    private static VBox profilePopupRef;
    private static Text profilePillNameText;
    private static Label verifiedDotLabel;

    private static Text modalNameText;
    private static Text modalEmailText;
    private static Text modalPhoneText;
    private static Text modalRoleText;

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
        if (modalNameText != null) {
            modalNameText.setText(AdminProfileStore.adminName != null ? AdminProfileStore.adminName : "Administrator");
        }
        if (modalEmailText != null) {
            modalEmailText.setText(AdminProfileStore.adminEmail != null ? AdminProfileStore.adminEmail : "admin@farmequip.com");
        }
        if (modalPhoneText != null) {
            modalPhoneText.setText(AdminProfileStore.adminPhone != null ? AdminProfileStore.adminPhone : "+91 98000 00001");
        }
        if (modalRoleText != null) {
            modalRoleText.setText(AdminProfileStore.adminRole != null ? AdminProfileStore.adminRole : "Master Admin");
        }
        updateAllAvatarGraphics();
    }

    private static Label alertBadgeLabel;
    private static VBox alertsListContainer;

    public HBox getProfile(StackPane root) {
        updateHeaderGreeting();

        alertBadgeLabel = new Label("...");
        alertBadgeLabel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");

        Button alertBtn = createPillButtonWithLabel("🔔", "System Alerts", alertBadgeLabel);

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
            if (!isVis) {
                refreshAdminAlerts();
            }
        });

        // Load initial alerts count
        refreshAdminAlerts();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topHeader = new HBox(16, headerTitleBox, spacer, alertBtn, profileBox);
        topHeader.setAlignment(Pos.CENTER_LEFT);
        topHeader.setPadding(new Insets(10, 24, 10, 24));
        topHeader.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        return topHeader;
    }

    private Button createPillButtonWithLabel(String icon, String title, Label badgeLabel) {
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

    private static StackPane profilePillAvatarBox;
    private static StackPane modalAvatarBox;
    private static ImageView modalAvatarImageView;
    private static Text modalDefaultAvatarIcon;

    private static void updateAllAvatarGraphics() {
        String pic = AdminProfileStore.adminProfilePic;
        boolean hasPic = pic != null && !pic.trim().isEmpty();

        if (!hasPic) {
            // Show default icon in pill
            if (profilePillAvatarBox != null) {
                profilePillAvatarBox.getChildren().clear();
                Text ico = new Text("🛡️"); ico.setStyle("-fx-font-size: 13px;");
                profilePillAvatarBox.getChildren().add(ico);
            }
            // Show default icon in modal
            if (modalAvatarImageView != null && modalDefaultAvatarIcon != null) {
                modalAvatarImageView.setImage(null);
                modalAvatarImageView.setVisible(false);
                modalAvatarImageView.setManaged(false);
                modalDefaultAvatarIcon.setVisible(true);
                modalDefaultAvatarIcon.setManaged(true);
            }
            return;
        }

        // Load image on background thread, set on FX thread when ready
        final String url = pic.trim();
        new Thread(() -> {
            try {
                // Load synchronously on background thread (no async flag)
                Image img = new Image(url, false);
                if (img.isError()) {
                    javafx.application.Platform.runLater(() -> {
                        // Fallback to default icon
                        if (profilePillAvatarBox != null) {
                            profilePillAvatarBox.getChildren().clear();
                            Text ico = new Text("🛡️"); ico.setStyle("-fx-font-size: 13px;");
                            profilePillAvatarBox.getChildren().add(ico);
                        }
                        if (modalAvatarImageView != null && modalDefaultAvatarIcon != null) {
                            modalAvatarImageView.setVisible(false);
                            modalAvatarImageView.setManaged(false);
                            modalDefaultAvatarIcon.setVisible(true);
                            modalDefaultAvatarIcon.setManaged(true);
                        }
                    });
                    return;
                }
                javafx.application.Platform.runLater(() -> {
                    try {
                        // Update pill avatar
                        if (profilePillAvatarBox != null) {
                            profilePillAvatarBox.getChildren().clear();
                            ImageView iv = new ImageView(img);
                            iv.setFitWidth(24); iv.setFitHeight(24);
                            iv.setPreserveRatio(false); iv.setSmooth(true);
                            Circle clip = new Circle(12, 12, 12);
                            iv.setClip(clip);
                            profilePillAvatarBox.getChildren().add(iv);
                        }
                        // Update modal avatar
                        if (modalAvatarImageView != null && modalDefaultAvatarIcon != null) {
                            modalAvatarImageView.setImage(img);
                            modalAvatarImageView.setVisible(true);
                            modalAvatarImageView.setManaged(true);
                            modalDefaultAvatarIcon.setVisible(false);
                            modalDefaultAvatarIcon.setManaged(false);
                        }
                    } catch (Exception ex) {
                        // ignore UI error
                    }
                });
            } catch (Exception ex) {
                javafx.application.Platform.runLater(() -> {
                    if (profilePillAvatarBox != null) {
                        profilePillAvatarBox.getChildren().clear();
                        Text ico = new Text("🛡️"); ico.setStyle("-fx-font-size: 13px;");
                        profilePillAvatarBox.getChildren().add(ico);
                    }
                    if (modalAvatarImageView != null && modalDefaultAvatarIcon != null) {
                        modalAvatarImageView.setVisible(false);
                        modalAvatarImageView.setManaged(false);
                        modalDefaultAvatarIcon.setVisible(true);
                        modalDefaultAvatarIcon.setManaged(true);
                    }
                });
            }
        }, "admin-avatar-loader").start();
    }

    private HBox createProfilePill(String icon, String name, String tag) {
        profilePillAvatarBox = new StackPane();
        profilePillAvatarBox.setPrefSize(26, 26);
        profilePillAvatarBox.setMinSize(26, 26);
        profilePillAvatarBox.setMaxSize(26, 26);
        profilePillAvatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 50%; -fx-border-color: #2D6A4F; -fx-border-radius: 50%; -fx-border-width: 1px;");

        profilePillNameText = new Text(name != null ? name : "Administrator");
        profilePillNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        verifiedDotLabel = new Label(tag != null ? tag : "HQ Root");
        verifiedDotLabel.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 1px 6px; -fx-background-radius: 8px;");

        Text dropdownArrow = new Text("▾");
        dropdownArrow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #2D6A4F; -fx-font-weight: bold;");

        HBox profileBox = new HBox(6, profilePillAvatarBox, profilePillNameText, verifiedDotLabel, dropdownArrow);
        profileBox.setAlignment(Pos.CENTER);
        profileBox.setPrefHeight(38);
        profileBox.setMinHeight(38);
        profileBox.setMaxHeight(38);
        profileBox.setPadding(new Insets(0, 14, 0, 10));

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

        updateAllAvatarGraphics();

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
        modal.setPrefSize(470, 410);
        modal.setMaxSize(470, 410);
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #D1E7DD;" +
                "-fx-border-radius: 18px;" +
                "-fx-border-width: 1.5px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 24, 0, 0, 8);"
        );

        // Circular Avatar in Modal Header
        modalAvatarImageView = new ImageView();
        modalAvatarImageView.setFitWidth(42);
        modalAvatarImageView.setFitHeight(42);
        modalAvatarImageView.setPreserveRatio(false);
        modalAvatarImageView.setSmooth(true);
        Circle clip = new Circle(21, 21, 21);
        modalAvatarImageView.setClip(clip);

        modalDefaultAvatarIcon = new Text("🛡️");
        modalDefaultAvatarIcon.setStyle("-fx-font-size: 20px;");

        modalAvatarBox = new StackPane(modalDefaultAvatarIcon, modalAvatarImageView);
        modalAvatarBox.setPrefSize(42, 42);
        modalAvatarBox.setMinSize(42, 42);
        modalAvatarBox.setMaxSize(42, 42);
        modalAvatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 50%; -fx-border-color: #2D6A4F; -fx-border-radius: 50%; -fx-border-width: 1.5px;");

        Text title = new Text("Administrator Profile");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Verified administrator credentials and platform access details");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(2, title, sub);
        HBox titleGroup = new HBox(10, modalAvatarBox, titleBox);
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

        modalNameText = new Text(AdminProfileStore.adminName != null ? AdminProfileStore.adminName : "Administrator");
        modalNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        modalEmailText = new Text(AdminProfileStore.adminEmail != null ? AdminProfileStore.adminEmail : "admin@farmequip.com");
        modalEmailText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151;");

        modalPhoneText = new Text(AdminProfileStore.adminPhone != null ? AdminProfileStore.adminPhone : "+91 98000 00001");
        modalPhoneText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151;");

        modalRoleText = new Text(AdminProfileStore.adminRole != null ? AdminProfileStore.adminRole : "Master Admin");
        modalRoleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

        HBox r1 = createRowWithNode("Admin Name:", modalNameText);
        HBox r2 = createRowWithNode("Email Address:", modalEmailText);
        HBox r3 = createRowWithNode("Phone Number:", modalPhoneText);
        HBox r4 = createRowWithNode("Assigned Role:", modalRoleText);

        // Change Profile Image / Settings Button
        Button editProfileBtn = new Button("📷  Change Profile Image & Edit Details");
        editProfileBtn.setPrefWidth(Double.MAX_VALUE);
        editProfileBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 8px 16px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"
        );

        editProfileBtn.setOnAction(e -> {
            modal.setVisible(false);
            AdminLeftSideBar.navigateToProfileSettings();
        });

        card.getChildren().addAll(r1, r2, r3, r4, editProfileBtn);

        modal.getChildren().addAll(topBar, card);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        updateAllAvatarGraphics();

        return modal;
    }

    private static HBox createRowWithNode(String label, javafx.scene.Node valueNode) {
        Label l = new Label(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        l.setPrefWidth(140);

        HBox row = new HBox(8, l, valueNode);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static final List<NotificationModel> currentAdminAlerts = new ArrayList<>();

    public static void refreshAdminAlerts() {
        new Thread(() -> {
            List<NotificationModel> list = new NotificationDAO().getAdminNotifications();
            Platform.runLater(() -> {
                currentAdminAlerts.clear();
                currentAdminAlerts.addAll(list);
                updateAdminBadgeCount(list.size());
                renderAdminAlertsList();
            });
        }).start();
    }

    private static void updateAdminBadgeCount(int count) {
        if (alertBadgeLabel != null) {
            if (count <= 0) {
                alertBadgeLabel.setText("0");
                alertBadgeLabel.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #64748B; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");
            } else {
                alertBadgeLabel.setText(count + " New");
                alertBadgeLabel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");
            }
        }
    }

    private static void renderAdminAlertsList() {
        if (alertsListContainer == null) return;
        alertsListContainer.getChildren().clear();

        if (currentAdminAlerts.isEmpty()) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(30, 20, 30, 20));
            Text empIcon = new Text("🔔");
            empIcon.setStyle("-fx-font-size: 28px;");
            Text empTitle = new Text("No New Notifications");
            empTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");
            Text empSub = new Text("All new user registrations, equipment listings, and customer reviews have been marked as read.");
            empSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280; -fx-text-alignment: center;");
            empSub.setWrappingWidth(380);
            emptyBox.getChildren().addAll(empIcon, empTitle, empSub);
            alertsListContainer.getChildren().add(emptyBox);
            return;
        }

        for (NotificationModel n : new ArrayList<>(currentAdminAlerts)) {
            String type = n.getType() != null ? n.getType().toUpperCase() : "ALERT";
            String tag = type;
            String tagBg = "#F3F4F6";
            String tagColor = "#374151";

            if ("EQUIPMENT".equals(type)) {
                tag = "EQUIPMENT";
                tagBg = "#DCFCE7";
                tagColor = "#15803D";
            } else if ("USER".equals(type)) {
                tag = "NEW USER";
                tagBg = "#E0E7FF";
                tagColor = "#4338CA";
            } else if ("REVIEW".equals(type)) {
                tag = "NEW REVIEW";
                tagBg = "#FEF3C7";
                tagColor = "#B45309";
            }

            String title = n.getTitle() != null ? n.getTitle() : "System Alert";
            String desc = n.getMessage() != null ? n.getMessage() : "";
            String time = n.getCreatedAt() != null ? n.getCreatedAt() : "Recent";

            VBox card = createAlertCard(n, title, desc, time, tag, tagBg, tagColor);
            alertsListContainer.getChildren().add(card);
        }
    }

    private VBox createAdminAlertsModal() {
        VBox modal = new VBox(14);
        modal.setPadding(new Insets(18, 20, 18, 20));
        modal.setPrefSize(560, 500);
        modal.setMaxSize(560, 500);
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

        Text title = new Text("Platform Activity & Moderation Alerts");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Live registrations, equipment listings & customer reviews");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(2, title, sub);
        HBox titleGroup = new HBox(10, bellBox, titleBox);
        titleGroup.setAlignment(Pos.CENTER_LEFT);

        Button markAllBtn = new Button("Mark as Read");
        markAllBtn.setMinWidth(Region.USE_PREF_SIZE);
        markAllBtn.setStyle("-fx-background-color: #DCFCE7; -fx-background-radius: 8px; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 5 12; -fx-border-color: #86EFAC; -fx-border-radius: 8px; -fx-border-width: 1px;");
        markAllBtn.setOnMouseEntered(e -> markAllBtn.setStyle("-fx-background-color: #BBF7D0; -fx-background-radius: 8px; -fx-text-fill: #14532D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 5 12; -fx-border-color: #4ADE80; -fx-border-radius: 8px; -fx-border-width: 1px;"));
        markAllBtn.setOnMouseExited(e -> markAllBtn.setStyle("-fx-background-color: #DCFCE7; -fx-background-radius: 8px; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 5 12; -fx-border-color: #86EFAC; -fx-border-radius: 8px; -fx-border-width: 1px;"));
        markAllBtn.setOnAction(e -> {
            if (!currentAdminAlerts.isEmpty()) {
                List<String> ids = new ArrayList<>();
                for (NotificationModel m : currentAdminAlerts) {
                    if (m.getNotificationId() != null) ids.add(m.getNotificationId());
                }
                currentAdminAlerts.clear();
                updateAdminBadgeCount(0);
                renderAdminAlertsList();
                new Thread(() -> new NotificationDAO().markAllAdminAlertsAsRead(ids)).start();
            }
        });

        Button close = createCloseButton(() -> modal.setVisible(false));

        HBox topActions = new HBox(8, markAllBtn, close);
        topActions.setAlignment(Pos.CENTER_RIGHT);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(8, titleGroup, topSpacer, topActions);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 12, 0));
        topBar.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        // Dynamic Items Container
        alertsListContainer = new VBox(10);
        alertsListContainer.setPadding(new Insets(0, 6, 0, 0));

        ScrollPane sp = new ScrollPane(alertsListContainer);
        sp.setFitToWidth(true);
        sp.setPrefHeight(400);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topBar, sp);

        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 22, 0, 0));
        modal.setVisible(false);

        // Pre-render list
        renderAdminAlertsList();

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

    private static VBox createAlertCard(NotificationModel notif, String title, String desc, String time, String tag, String tagBg, String tagColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label tagLabel = new Label(tag);
        tagLabel.setStyle("-fx-background-color: " + tagBg + "; -fx-text-fill: " + tagColor + "; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 4px;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox topRow = new HBox(8, t, sp, tagLabel);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Text d = new Text(desc);
        d.setWrappingWidth(420);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-line-spacing: 2px;");

        Text tm = new Text(time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #9CA3AF;");

        Button markReadBtn = new Button("✓ Mark as Read");
        markReadBtn.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 600; -fx-padding: 4px 10px; -fx-background-radius: 6px; -fx-cursor: hand; -fx-border-color: #CBD5E1; -fx-border-radius: 6px; -fx-border-width: 1px;");
        markReadBtn.setOnMouseEntered(e -> markReadBtn.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4px 10px; -fx-background-radius: 6px; -fx-cursor: hand; -fx-border-color: #86EFAC; -fx-border-radius: 6px; -fx-border-width: 1px;"));
        markReadBtn.setOnMouseExited(e -> markReadBtn.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #475569; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 600; -fx-padding: 4px 10px; -fx-background-radius: 6px; -fx-cursor: hand; -fx-border-color: #CBD5E1; -fx-border-radius: 6px; -fx-border-width: 1px;"));

        VBox card = new VBox(6);

        markReadBtn.setOnAction(e -> {
            currentAdminAlerts.remove(notif);
            if (alertsListContainer != null) {
                alertsListContainer.getChildren().remove(card);
            }
            updateAdminBadgeCount(currentAdminAlerts.size());
            if (currentAdminAlerts.isEmpty()) {
                renderAdminAlertsList();
            }
            new Thread(() -> {
                if (notif.getNotificationId() != null) {
                    new NotificationDAO().markAdminAlertAsRead(notif.getNotificationId());
                }
            }).start();
        });

        Region bottomSp = new Region();
        HBox.setHgrow(bottomSp, Priority.ALWAYS);
        HBox bottomRow = new HBox(8, tm, bottomSp, markReadBtn);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(topRow, d, bottomRow);
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

