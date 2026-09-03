package com.desgin.view.provider;

import java.io.File;
import java.util.List;

import com.desgin.config.CloudinaryConfig;
import com.desgin.dao.AuthDAO;
import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.RentalRequestModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

public class ProviderProfileManagement {

    private static VBox profilePopupRef;
    private static Text profilePillNameText;
    private static StackPane profilePillAvatarBox;
    private static Text nameValText;
    private static Text emailValText;
    private static Text phoneValText;
    private static TextField modalTownField;
    private static TextField modalDistrictField;
    private static TextField modalStateField;
    private static TextField modalPincodeField;
    private static ImageView modalAvatarView;
    private static Text modalAvatarIcon;

    public static String getFormattedFirstName() {
        String raw = ProviderProfileStore.name;
        if (raw == null || raw.trim().isEmpty()) {
            return "Provider";
        }
        raw = raw.trim();
        if (raw.contains("@")) {
            raw = raw.split("@")[0];
        }
        String first = raw.split("[ ._]")[0];
        if (first.isEmpty()) {
            return "Provider";
        }
        return Character.toUpperCase(first.charAt(0)) + (first.length() > 1 ? first.substring(1) : "");
    }

    public static Text headerTitleText = new Text("Welcome back, " + getFormattedFirstName() + " 👋");
    public static Text headerSubtitleText = new Text("Provider Fleet Hub • Manage machinery inventory & farmer requests");
    public static VBox headerTitleBox;
    private static Label notifBadge;
    private static VBox notifListContainer;

    static {
        headerTitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        headerSubtitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");
        headerTitleBox = new VBox(2, headerTitleText, headerSubtitleText);
        headerTitleBox.setAlignment(Pos.CENTER_LEFT);

        ProviderProfileStore.addProfileListener(() -> {
            Platform.runLater(ProviderProfileManagement::refreshDynamicProfileUI);
        });
        ProviderProfileStore.addLocationListener(() -> {
            Platform.runLater(ProviderProfileManagement::refreshDynamicLocationUI);
        });
    }

    public static void updateHeaderGreeting() {
        String fName = getFormattedFirstName();
        setHeaderTitle("Welcome back, " + fName + " 👋", "Provider Fleet Hub • Manage machinery inventory & farmer requests");
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
            profilePillNameText.setText(ProviderProfileStore.name != null ? ProviderProfileStore.name : "Provider");
        }
        if (nameValText != null) {
            nameValText.setText(ProviderProfileStore.name != null ? ProviderProfileStore.name : "Not Set");
        }
        if (emailValText != null) {
            emailValText.setText(ProviderProfileStore.email != null ? ProviderProfileStore.email : "Not Set");
        }
        if (phoneValText != null) {
            phoneValText.setText(ProviderProfileStore.phone != null ? ProviderProfileStore.phone : "Not Set");
        }
        updateAvatarDisplay();
    }

    public static void refreshDynamicLocationUI() {
        if (modalTownField != null && !modalTownField.isFocused()) {
            modalTownField.setText(ProviderProfileStore.town != null ? ProviderProfileStore.town : "");
        }
        if (modalDistrictField != null && !modalDistrictField.isFocused()) {
            modalDistrictField.setText(ProviderProfileStore.district != null ? ProviderProfileStore.district : "");
        }
        if (modalStateField != null && !modalStateField.isFocused()) {
            modalStateField.setText(ProviderProfileStore.state != null ? ProviderProfileStore.state : "");
        }
        if (modalPincodeField != null && !modalPincodeField.isFocused()) {
            modalPincodeField.setText(ProviderProfileStore.pincode != null ? ProviderProfileStore.pincode : "");
        }
    }

    private static void updateAvatarDisplay() {
        String pic = ProviderProfileStore.profilePic;
        if (pic != null && !pic.trim().isEmpty()) {
            try {
                Image img = new Image(pic, true);
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
                    Circle clip = new Circle(11, 11, 11);
                    pillIv.setClip(clip);
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
                Text icon = new Text("🚜");
                icon.setStyle("-fx-font-size: 14px;");
                profilePillAvatarBox.getChildren().add(icon);
            }
        }
    }

    public HBox getProfile(StackPane root) {
        updateHeaderGreeting();

        HBox profileBox = createProfilePill();

        profilePopupRef = createProfileModal(root);
        root.getChildren().add(profilePopupRef);

        profileBox.setOnMouseClicked(event -> {
            boolean isVis = profilePopupRef.isVisible();
            if (!isVis) {
                refreshDynamicProfileUI();
                refreshDynamicLocationUI();
            }
            profilePopupRef.setVisible(!isVis);
        });

        notifBadge = new Label("");
        notifBadge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2px 6px; -fx-background-radius: 10px;");
        notifBadge.setVisible(false);
        notifBadge.setManaged(false);

        Button notificationBtn = createPillButton("🔔", "Notifications", notifBadge);

        VBox notifPopup = createNotificationModal();
        root.getChildren().add(notifPopup);

        notificationBtn.setOnAction(e -> {
            boolean isVis = notifPopup.isVisible();
            if (profilePopupRef.isVisible()) profilePopupRef.setVisible(false);
            notifPopup.setVisible(!isVis);
            if (!isVis) refreshNotifications();
        });

        refreshNotifications();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox rightHBox = new HBox(10, notificationBtn, profileBox);
        rightHBox.setAlignment(Pos.CENTER_RIGHT);

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
        Text defaultIcon = new Text("🚜");
        defaultIcon.setStyle("-fx-font-size: 14px;");
        profilePillAvatarBox.getChildren().add(defaultIcon);

        profilePillNameText = new Text(ProviderProfileStore.name != null ? ProviderProfileStore.name : "Provider");
        profilePillNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedDot = new Label("✓ Verified");
        verifiedDot.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 1px 6px; -fx-background-radius: 8px;");

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
                + "-fx-padding: 0 14px 0 12px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.18), 8, 0, 0, 2);";

        profileBox.setStyle(normalStyle);
        profileBox.setOnMouseEntered(e -> profileBox.setStyle(hoverStyle));
        profileBox.setOnMouseExited(e -> profileBox.setStyle(normalStyle));

        updateAvatarDisplay();
        return profileBox;
    }

    private VBox createProfileModal(StackPane root) {
        VBox modal = new VBox(14);
        modal.setPrefWidth(440);
        modal.setMaxWidth(440);
        modal.setPadding(new Insets(20));
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 16px;" +
                "-fx-effect: dropshadow(gaussian, rgba(27,67,50,0.22), 24, 0.2, 0, 6);");
        modal.setVisible(false);
        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 24, 0, 0));

        // Top Row: Title + Close Button
        Text title = new Text("Provider Hub Profile");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 15; -fx-text-fill: #6B7280; -fx-font-weight: bold; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> modal.setVisible(false));

        HBox topHBox = new HBox(10, title, topSpacer, closeBtn);
        topHBox.setAlignment(Pos.CENTER_LEFT);

        // Avatar + Cloudinary Image Upload Section
        modalAvatarIcon = new Text("🚜");
        modalAvatarIcon.setStyle("-fx-font-size: 32px;");

        modalAvatarView = new ImageView();
        modalAvatarView.setFitWidth(60);
        modalAvatarView.setFitHeight(60);
        Circle clip = new Circle(30, 30, 30);
        modalAvatarView.setClip(clip);
        modalAvatarView.setVisible(false);
        modalAvatarView.setManaged(false);

        StackPane avatarBox = new StackPane(modalAvatarIcon, modalAvatarView);
        avatarBox.setPrefSize(60, 60);
        avatarBox.setMaxSize(60, 60);
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 30; -fx-border-color: #A5D6A7; -fx-border-width: 1.5; -fx-border-radius: 30;");

        Button changePicBtn = new Button("📷 Change Photo");
        changePicBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 15; -fx-cursor: hand; -fx-padding: 5 12;");

        Label uploadStatusLabel = new Label("");
        uploadStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #2E7D32;");

        changePicBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Provider Profile Picture");
            chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.bmp")
            );
            Window win = root.getScene() != null ? root.getScene().getWindow() : null;
            File file = chooser.showOpenDialog(win);
            if (file != null) {
                uploadStatusLabel.setText("Uploading photo...");
                changePicBtn.setDisable(true);
                Task<String> uploadTask = new Task<>() {
                    @Override
                    protected String call() {
                        return CloudinaryConfig.uploadImage(file);
                    }
                };
                uploadTask.setOnSucceeded(ev -> {
                    changePicBtn.setDisable(false);
                    String url = uploadTask.getValue();
                    if (url != null) {
                        uploadStatusLabel.setText("✓ Profile photo updated!");
                        ProviderProfileStore.setProfilePic(url);
                        new Thread(() -> {
                            new AuthDAO().updateProfilePic(ProviderProfileStore.email, "Provider", url);
                        }).start();
                    } else {
                        uploadStatusLabel.setText("Upload failed. Try again.");
                    }
                });
                uploadTask.setOnFailed(ev -> {
                    changePicBtn.setDisable(false);
                    uploadStatusLabel.setText("Error during photo upload.");
                });
                new Thread(uploadTask).start();
            }
        });

        VBox avatarActions = new VBox(4, changePicBtn, uploadStatusLabel);
        avatarActions.setAlignment(Pos.CENTER_LEFT);

        HBox avatarRow = new HBox(14, avatarBox, avatarActions);
        avatarRow.setAlignment(Pos.CENTER_LEFT);
        avatarRow.setPadding(new Insets(6, 0, 6, 0));

        // Profile Info Details
        nameValText = new Text(ProviderProfileStore.name != null ? ProviderProfileStore.name : "Provider");
        nameValText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        emailValText = new Text(ProviderProfileStore.email != null ? ProviderProfileStore.email : "Not Set");
        emailValText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        phoneValText = new Text(ProviderProfileStore.phone != null ? ProviderProfileStore.phone : "Not Set");
        phoneValText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox infoBox = new VBox(3, nameValText, emailValText, phoneValText);

        // Location Section
        Text locHeader = new Text("Operating Hub Location");
        locHeader.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        GridPane locGrid = new GridPane();
        locGrid.setHgap(8);
        locGrid.setVgap(8);

        modalTownField = new TextField(ProviderProfileStore.town != null ? ProviderProfileStore.town : "");
        modalTownField.setPromptText("City / Town (e.g. Pune)");
        modalTownField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        modalDistrictField = new TextField(ProviderProfileStore.district != null ? ProviderProfileStore.district : "");
        modalDistrictField.setPromptText("District");
        modalDistrictField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        modalStateField = new TextField(ProviderProfileStore.state != null ? ProviderProfileStore.state : "");
        modalStateField.setPromptText("State");
        modalStateField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        modalPincodeField = new TextField(ProviderProfileStore.pincode != null ? ProviderProfileStore.pincode : "");
        modalPincodeField.setPromptText("Pincode");
        modalPincodeField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        locGrid.add(createFieldLabel("Town / City:"), 0, 0);
        locGrid.add(modalTownField, 1, 0);
        locGrid.add(createFieldLabel("District:"), 0, 1);
        locGrid.add(modalDistrictField, 1, 1);
        locGrid.add(createFieldLabel("State:"), 0, 2);
        locGrid.add(modalStateField, 1, 2);
        locGrid.add(createFieldLabel("Pincode:"), 0, 3);
        locGrid.add(modalPincodeField, 1, 3);

        Label saveMsg = new Label("");
        saveMsg.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #15803D;");

        Button saveLocationBtn = new Button("💾 Save Location");
        saveLocationBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 7 16;");
        saveLocationBtn.setOnAction(e -> {
            String t = modalTownField.getText().trim();
            String d = modalDistrictField.getText().trim();
            String s = modalStateField.getText().trim();
            String p = modalPincodeField.getText().trim();
            ProviderProfileStore.setLocation(t, d, s, p);
            saveMsg.setText("✓ Location updated!");
            Thread bg = new Thread(() -> {
                new AuthDAO().updateLocation(ProviderProfileStore.email, "Provider", t, d, s, p);
            });
            bg.setDaemon(true);
            bg.start();
        });

        HBox btnBox = new HBox(10, saveLocationBtn, saveMsg);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        modal.getChildren().addAll(topHBox, avatarRow, infoBox, locHeader, locGrid, btnBox);
        return modal;
    }

    public static void refreshNotifications() {
        Thread t = new Thread(() -> {
            try {
                String email = ProviderProfileStore.email;
                List<RentalRequestModel> list = new RentalRequestDAO().getRequestsByProvider(email);
                if (list.isEmpty()) {
                    list = new RentalRequestDAO().getAllRequests();
                }

                long pending = list.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
                List<RentalRequestModel> finalRequests = list;

                Platform.runLater(() -> {
                    if (notifBadge != null) {
                        if (pending > 0) {
                            notifBadge.setText(String.valueOf(pending));
                            notifBadge.setVisible(true);
                            notifBadge.setManaged(true);
                        } else {
                            notifBadge.setVisible(false);
                            notifBadge.setManaged(false);
                        }
                    }

                    if (notifListContainer != null) {
                        notifListContainer.getChildren().clear();
                        if (finalRequests.isEmpty()) {
                            notifListContainer.getChildren().add(createNotificationItem("🔔 Notifications", "No active requests or alerts at this time.", "Just now"));
                        } else {
                            for (RentalRequestModel r : finalRequests.stream().limit(5).toList()) {
                                String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "PENDING";
                                String icon = "PENDING".equals(st) ? "📥 " : ("APPROVED".equals(st) ? "🚜 " : "✔ ");
                                String title = icon + ("PENDING".equals(st) ? "New Request from " + r.getFarmerName() : ("APPROVED".equals(st) ? "Active Job: " + r.getMachineryName() : "Completed Job"));
                                String desc = "📍 " + (r.getFarmerLocation() != null ? r.getFarmerLocation() : "Local") + " • " + r.getMachineryName() + " (" + r.getDays() + " days)";
                                String time = r.getStartDate() != null ? r.getStartDate() : "Recent";
                                notifListContainer.getChildren().add(createNotificationItem(title, desc, time));
                            }
                        }
                    }
                });
            } catch (Exception ignored) {}
        });
        t.setDaemon(true);
        t.start();
    }

    private VBox createNotificationModal() {
        VBox modal = new VBox(10);
        modal.setPrefWidth(360);
        modal.setMaxWidth(360);
        modal.setPadding(new Insets(16));
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 18, 0, 0, 4);");
        modal.setVisible(false);
        StackPane.setAlignment(modal, Pos.TOP_RIGHT);
        StackPane.setMargin(modal, new Insets(60, 80, 0, 0));

        Text title = new Text("Hub Notifications 🔔");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button close = new Button("✕");
        close.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 12; -fx-text-fill: #6B7280; -fx-font-weight: bold; -fx-cursor: hand;");
        close.setOnAction(e -> modal.setVisible(false));

        HBox topH = new HBox(10, title, sp, close);
        topH.setAlignment(Pos.CENTER_LEFT);

        notifListContainer = new VBox(8);
        ScrollPane scroll = new ScrollPane(notifListContainer);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(280);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        modal.getChildren().addAll(topH, scroll);
        return modal;
    }

    private static VBox createNotificationItem(String title, String desc, String time) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        Text d = new Text(desc);
        d.setWrappingWidth(300);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");
        Text tm = new Text(time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #9CA3AF;");

        VBox box = new VBox(2, t, d, tm);
        box.setPadding(new Insets(6, 8, 6, 8));
        box.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 6;");
        return box;
    }

    private static Label createFieldLabel(String txt) {
        Label l = new Label(txt);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        return l;
    }
}
