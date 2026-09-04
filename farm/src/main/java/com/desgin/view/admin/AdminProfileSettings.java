package com.desgin.view.admin;

import java.io.File;
import com.desgin.dao.AuthDAO;
import com.desgin.view.components.PasswordEyeField;
import javafx.application.Platform;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class AdminProfileSettings {

    private static ImageView avatarImageView;
    private static StackPane avatarContainer;
    private static Text defaultAvatarIcon;
    private static Label feedbackLabel;
    private static Label pwFeedbackLabel;

    private static final String PWD_NORMAL_STYLE = "-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 36px 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;";
    private static final String PWD_ERROR_STYLE  = "-fx-background-color: #FEF2F2; -fx-border-color: #EF4444; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 36px 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;";
    private static final String PWD_FOCUS_STYLE  = "-fx-background-color: #FFFFFF; -fx-border-color: #2D6A4F; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 36px 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;";

    public static ScrollPane getPage(StackPane root) {
        feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8 16 8 16; -fx-background-radius: 8;");
        feedbackLabel.setVisible(false);
        feedbackLabel.setManaged(false);

        VBox avatarCard = createAvatarManagementCard(root);
        VBox detailsCard = createDetailsCard();
        VBox passwordCard = createChangePasswordCard();

        VBox content = new VBox(20, feedbackLabel, avatarCard, detailsCard, passwordCard);
        content.setPadding(new Insets(22, 26, 40, 26));
        content.setMinWidth(0);
        content.setMaxWidth(Double.MAX_VALUE);

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Sync with Firestore in background for the logged-in admin email
        new Thread(() -> {
            try {
                String mail = AdminProfileStore.adminEmail;
                if (mail != null && !mail.trim().isEmpty()) {
                    com.desgin.model.AuthenticateModel u = new AuthDAO().getUser(mail, "Admin");
                    if (u != null) {
                        String pic = u.getProfilePic() != null ? u.getProfilePic().trim() : "";
                        Platform.runLater(() -> {
                            AdminProfileStore.adminProfilePic = pic;
                            updateAvatarGraphic(pic);
                            AdminProfileStore.notifyListeners();
                        });
                    }
                }
            } catch (Exception ignored) {}
        }).start();

        return sp;
    }

    // ─────────────────────────────────────────────
    // CARD 1 – Profile Picture
    // ─────────────────────────────────────────────
    private static VBox createAvatarManagementCard(StackPane root) {
        Label cardTitle = buildCardTitle("PHOTO", "Profile Picture");
        Text cardSub = new Text("Upload a professional photo. It will appear across the admin console.");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        avatarImageView = new ImageView();
        avatarImageView.setFitWidth(96);
        avatarImageView.setFitHeight(96);
        avatarImageView.setPreserveRatio(false);
        avatarImageView.setSmooth(true);
        Circle clip = new Circle(48, 48, 48);
        avatarImageView.setClip(clip);

        defaultAvatarIcon = new Text("\uD83D\uDEE1\uFE0F");
        defaultAvatarIcon.setStyle("-fx-font-size: 42px;");

        avatarContainer = new StackPane(defaultAvatarIcon, avatarImageView);
        avatarContainer.setPrefSize(96, 96);
        avatarContainer.setMinSize(96, 96);
        avatarContainer.setMaxSize(96, 96);
        avatarContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #D8F3DC, #B7E4C7); -fx-background-radius: 50%; -fx-border-color: #2D6A4F; -fx-border-radius: 50%; -fx-border-width: 2.5px; -fx-effect: dropshadow(gaussian, rgba(45,106,79,0.25), 10, 0, 0, 3);");

        updateAvatarGraphic(AdminProfileStore.adminProfilePic);

        Button changePicBtn = new Button("\uD83D\uDCF7  Change Profile Image");
        changePicBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 8px 18px; -fx-cursor: hand;");

        changePicBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Administrator Profile Picture");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp"));
            File selectedFile = fc.showOpenDialog(root.getScene().getWindow());
            if (selectedFile != null) {
                // Immediately display the chosen image locally so the user sees the direct update!
                try {
                    Image localImg = new Image(selectedFile.toURI().toString(), false);
                    avatarImageView.setImage(localImg);
                    avatarImageView.setVisible(true);
                    avatarImageView.setManaged(true);
                    defaultAvatarIcon.setVisible(false);
                    defaultAvatarIcon.setManaged(false);
                } catch (Exception ignored) {}

                changePicBtn.setDisable(true);
                changePicBtn.setText("\u23F3 Uploading...");
                showFeedback("\u23F3 Uploading profile photo to cloud storage...", "#E0F2FE", "#0369A1");

                new Thread(() -> {
                    String url = com.desgin.config.CloudinaryConfig.uploadImage(selectedFile);
                    Platform.runLater(() -> {
                        changePicBtn.setDisable(false);
                        changePicBtn.setText("\uD83D\uDCF7  Change Profile Image");
                        if (url != null && !url.trim().isEmpty()) {
                            AdminProfileStore.adminProfilePic = url;
                            updateAvatarGraphic(url);
                            AdminProfileStore.notifyListeners();
                            new Thread(() -> new AuthDAO().updateProfilePic(AdminProfileStore.adminEmail, "Admin", url)).start();
                            showFeedback("\u2705 Profile picture updated and saved successfully!", "#DCFCE7", "#15803D");
                        } else {
                            showFeedback("\u274C Upload failed. Please check your internet connection.", "#FEE2E2", "#DC2626");
                        }
                    });
                }).start();
            }
        });

        Button removePicBtn = new Button("\uD83D\uDDD1\uFE0F  Remove Picture");
        removePicBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: 600; -fx-background-radius: 8px; -fx-padding: 8px 14px; -fx-cursor: hand; -fx-border-color: #FCA5A5; -fx-border-radius: 8px; -fx-border-width: 1px;");
        removePicBtn.setOnAction(e -> {
            AdminProfileStore.adminProfilePic = "";
            updateAvatarGraphic("");
            AdminProfileStore.notifyListeners();
            new Thread(() -> {
                new AuthDAO().updateProfilePic(AdminProfileStore.adminEmail, "Admin", "");
                Platform.runLater(() -> showFeedback("\u2705 Profile picture removed.", "#F3F4F6", "#374151"));
            }).start();
        });

        HBox btnRow = new HBox(12, changePicBtn, removePicBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        VBox descBox = new VBox(8, cardSub, btnRow);
        HBox mainRow = new HBox(20, avatarContainer, descBox);
        mainRow.setAlignment(Pos.CENTER_LEFT);

        return buildCard(cardTitle, mainRow);
    }

    // ─────────────────────────────────────────────
    // CARD 2 – Personal & Contact Credentials
    // ─────────────────────────────────────────────
    private static VBox createDetailsCard() {
        Label cardTitle = buildCardTitle("PERSON", "Personal & Contact Credentials");

        String tfStyle = "-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-padding: 8 12 8 12;";
        TextField nameField  = new TextField(AdminProfileStore.adminName  != null ? AdminProfileStore.adminName  : "Administrator");
        TextField emailField = new TextField(AdminProfileStore.adminEmail != null ? AdminProfileStore.adminEmail : "admin@farmequip.com");
        TextField phoneField = new TextField(AdminProfileStore.adminPhone != null ? AdminProfileStore.adminPhone : "+91 98000 00001");
        TextField roleField  = new TextField(AdminProfileStore.adminRole  != null ? AdminProfileStore.adminRole  : "Master Admin");

        nameField.setStyle(tfStyle);  nameField.setPrefWidth(290);
        emailField.setStyle(tfStyle + " -fx-background-color: #F8FAFC; -fx-text-fill: #64748B;");
        emailField.setPrefWidth(290); emailField.setEditable(false);
        phoneField.setStyle(tfStyle); phoneField.setPrefWidth(290);
        roleField.setStyle(tfStyle + " -fx-background-color: #F8FAFC; -fx-text-fill: #15803D; -fx-font-weight: bold;");
        roleField.setPrefWidth(290);  roleField.setEditable(false);

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);
        grid.add(mkLabel("Administrator Full Name:"),   0, 0); grid.add(nameField,  1, 0);
        grid.add(mkLabel("Email Address (Login Key):"), 0, 1); grid.add(emailField, 1, 1);
        grid.add(mkLabel("Registered Contact Number:"),0, 2); grid.add(phoneField, 1, 2);
        grid.add(mkLabel("Assigned Authority Role:"),  0, 3); grid.add(roleField,  1, 3);

        Button saveBtn = new Button("\uD83D\uDCBE  Save Changes");
        saveBtn.setStyle("-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 10px 22px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.2), 8, 0, 0, 2);");
        saveBtn.setOnAction(e -> {
            String n = nameField.getText().trim(), p = phoneField.getText().trim();
            if (n.isEmpty()) {
                showFeedback("\u274C Name cannot be empty.", "#FEE2E2", "#DC2626");
                return;
            }
            saveBtn.setDisable(true);
            saveBtn.setText("\u23F3 Saving...");
            new Thread(() -> {
                boolean ok = new AuthDAO().updateProfile(AdminProfileStore.adminEmail, "Admin", n, p);
                Platform.runLater(() -> {
                    saveBtn.setDisable(false);
                    saveBtn.setText("\uD83D\uDCBE  Save Changes");
                    if (ok) {
                        AdminProfileStore.setAdminProfile(n, AdminProfileStore.adminEmail, p, AdminProfileStore.adminRole, AdminProfileStore.adminProfilePic);
                        AdminProfileManagement.updateHeaderGreeting();
                        showFeedback("\u2705 Profile updated successfully!", "#DCFCE7", "#15803D");
                    } else {
                        showFeedback("\u26A0\uFE0F Failed to update profile. Please check your connection.", "#FEF3C7", "#B45309");
                    }
                });
            }).start();
        });

        return buildCard(cardTitle, grid, saveBtn);
    }

    // ─────────────────────────────────────────────
    // CARD 3 – Change Password with Eye Toggle & Login Validation
    // ─────────────────────────────────────────────
    private static VBox createChangePasswordCard() {
        Label cardTitle = buildCardTitle("LOCK", "Change Password");
        Text info = new Text("Enter your current password to verify identity, then set a new password.");
        info.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        PasswordEyeField curPf = new PasswordEyeField("Current password");
        curPf.setCustomPrefSize(290, 40);
        applyEyeFieldStyle(curPf, PWD_NORMAL_STYLE);

        PasswordEyeField newPf = new PasswordEyeField("New password");
        newPf.setCustomPrefSize(290, 40);
        applyEyeFieldStyle(newPf, PWD_NORMAL_STYLE);

        PasswordEyeField confPf = new PasswordEyeField("Confirm new password");
        confPf.setCustomPrefSize(290, 40);
        applyEyeFieldStyle(confPf, PWD_NORMAL_STYLE);

        // Strength label
        Label strength = new Label("Password requirement: 8+ chars (1 uppercase, 1 digit, 1 special symbol)");
        strength.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #6B7280;");

        newPf.textProperty().addListener((obs, o, nv) -> {
            if (nv == null || nv.isEmpty()) {
                strength.setText("Password requirement: 8+ chars (1 uppercase, 1 digit, 1 special symbol)");
                strength.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #6B7280;");
                return;
            }
            if (!nv.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
                strength.setText("\uD83D\uDD34  Must have 8+ chars, 1 uppercase, 1 digit & 1 symbol");
                strength.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #DC2626; -fx-font-weight: bold;");
            } else {
                strength.setText("\uD83D\uDFE2  Strong & Compliant Password");
                strength.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #15803D; -fx-font-weight: bold;");
            }
        });

        pwFeedbackLabel = new Label();
        pwFeedbackLabel.setVisible(false);
        pwFeedbackLabel.setManaged(false);

        Button btn = new Button("\uD83D\uDD12  Update Password");
        btn.setStyle("-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 10px 22px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.2), 8, 0, 0, 2);");

        btn.setOnAction(e -> {
            String cur = curPf.getText() != null ? curPf.getText().trim() : "";
            String nw = newPf.getText() != null ? newPf.getText().trim() : "";
            String conf = confPf.getText() != null ? confPf.getText().trim() : "";

            // Reset field styles
            applyEyeFieldStyle(curPf, PWD_NORMAL_STYLE);
            applyEyeFieldStyle(newPf, PWD_NORMAL_STYLE);
            applyEyeFieldStyle(confPf, PWD_NORMAL_STYLE);

            if (cur.isEmpty()) {
                applyEyeFieldStyle(curPf, PWD_ERROR_STYLE);
                showPwFeedback("\u274C Please enter your current password.", "#FEE2E2", "#DC2626");
                return;
            }

            // Same validation as Login / Signup: 8 chars, 1 uppercase, 1 digit, 1 symbol
            if (nw.isEmpty()) {
                applyEyeFieldStyle(newPf, PWD_ERROR_STYLE);
                showPwFeedback("\u274C New password is required.", "#FEE2E2", "#DC2626");
                return;
            }

            if (!nw.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
                applyEyeFieldStyle(newPf, PWD_ERROR_STYLE);
                showPwFeedback("\u274C Password must be at least 8 characters, with 1 uppercase, 1 digit, and 1 special symbol.", "#FEE2E2", "#DC2626");
                return;
            }

            if (!nw.equals(conf)) {
                applyEyeFieldStyle(confPf, PWD_ERROR_STYLE);
                showPwFeedback("\u274C New passwords do not match.", "#FEE2E2", "#DC2626");
                return;
            }

            if (nw.equals(cur)) {
                applyEyeFieldStyle(newPf, PWD_ERROR_STYLE);
                showPwFeedback("\u26A0\uFE0F New password must be different from current password.", "#FEF3C7", "#B45309");
                return;
            }

            btn.setDisable(true);
            btn.setText("\u23F3 Verifying...");
            showPwFeedback("\u23F3 Verifying current password...", "#E0F2FE", "#0369A1");

            new Thread(() -> {
                AuthDAO dao = new AuthDAO();
                boolean ok = dao.verifyCurrentPassword(AdminProfileStore.adminEmail, "Admin", cur);
                if (!ok) {
                    Platform.runLater(() -> {
                        btn.setDisable(false);
                        btn.setText("\uD83D\uDD12  Update Password");
                        applyEyeFieldStyle(curPf, PWD_ERROR_STYLE);
                        showPwFeedback("\u274C Current password is incorrect.", "#FEE2E2", "#DC2626");
                    });
                    return;
                }

                boolean done = dao.updatePassword(AdminProfileStore.adminEmail, "Admin", nw);
                Platform.runLater(() -> {
                    btn.setDisable(false);
                    btn.setText("\uD83D\uDD12  Update Password");
                    if (done) {
                        curPf.clear();
                        newPf.clear();
                        confPf.clear();
                        applyEyeFieldStyle(curPf, PWD_NORMAL_STYLE);
                        applyEyeFieldStyle(newPf, PWD_NORMAL_STYLE);
                        applyEyeFieldStyle(confPf, PWD_NORMAL_STYLE);
                        showPwFeedback("\u2705 Password updated successfully!", "#DCFCE7", "#15803D");
                    } else {
                        showPwFeedback("\u26A0\uFE0F Failed to update password. Please try again.", "#FEF3C7", "#B45309");
                    }
                });
            }).start();
        });

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);
        grid.add(mkLabel("Current Password:"), 0, 0); grid.add(curPf,  1, 0);
        grid.add(mkLabel("New Password:"),     0, 1); grid.add(newPf,  1, 1);
        grid.add(new Label(""),                0, 2); grid.add(strength,1, 2);
        grid.add(mkLabel("Confirm Password:"), 0, 3); grid.add(confPf, 1, 3);

        return buildCard(cardTitle, info, grid, pwFeedbackLabel, btn);
    }

    private static void applyEyeFieldStyle(PasswordEyeField field, String style) {
        if (field == null) return;
        field.getHiddenField().setStyle(style);
        field.getShownField().setStyle(style);
        field.getHiddenField().focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (isFocused) {
                field.getHiddenField().setStyle(PWD_FOCUS_STYLE);
                field.getShownField().setStyle(PWD_FOCUS_STYLE);
            } else {
                field.getHiddenField().setStyle(PWD_NORMAL_STYLE);
                field.getShownField().setStyle(PWD_NORMAL_STYLE);
            }
        });
        field.getShownField().focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (isFocused) {
                field.getHiddenField().setStyle(PWD_FOCUS_STYLE);
                field.getShownField().setStyle(PWD_FOCUS_STYLE);
            } else {
                field.getHiddenField().setStyle(PWD_NORMAL_STYLE);
                field.getShownField().setStyle(PWD_NORMAL_STYLE);
            }
        });
    }

    private static Label buildCardTitle(String tag, String text) {
        String emoji = "PHOTO".equals(tag) ? "\uD83D\uDCF7" : ("PERSON".equals(tag) ? "\uD83D\uDC64" : "\uD83D\uDD12");
        Label l = new Label(emoji + "  " + text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        return l;
    }

    private static VBox buildCard(javafx.scene.Node... children) {
        VBox card = new VBox(14);
        card.getChildren().addAll(children);
        card.setPadding(new Insets(18));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static void updateAvatarGraphic(String picUri) {
        if (avatarImageView == null || defaultAvatarIcon == null) return;
        if (picUri == null || picUri.trim().isEmpty()) {
            avatarImageView.setImage(null);
            avatarImageView.setVisible(false);
            avatarImageView.setManaged(false);
            defaultAvatarIcon.setVisible(true);
            defaultAvatarIcon.setManaged(true);
            return;
        }
        final String url = picUri.trim();
        new Thread(() -> {
            try {
                Image img = new Image(url, false);
                if (img.isError()) {
                    Platform.runLater(() -> {
                        avatarImageView.setVisible(false);
                        avatarImageView.setManaged(false);
                        defaultAvatarIcon.setVisible(true);
                        defaultAvatarIcon.setManaged(true);
                    });
                    return;
                }
                Platform.runLater(() -> {
                    try {
                        avatarImageView.setImage(img);
                        avatarImageView.setVisible(true);
                        avatarImageView.setManaged(true);
                        defaultAvatarIcon.setVisible(false);
                        defaultAvatarIcon.setManaged(false);
                    } catch (Exception ignored) {}
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    avatarImageView.setVisible(false);
                    avatarImageView.setManaged(false);
                    defaultAvatarIcon.setVisible(true);
                    defaultAvatarIcon.setManaged(true);
                });
            }
        }, "admin-settings-avatar-loader").start();
    }

    private static void showFeedback(String msg, String bg, String fg) {
        if (feedbackLabel == null) return;
        feedbackLabel.setText(msg);
        feedbackLabel.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-padding: 10 16 10 16; -fx-background-radius: 8; -fx-border-color: " + fg + "; -fx-border-radius: 8; -fx-border-width: 1;");
        feedbackLabel.setVisible(true);
        feedbackLabel.setManaged(true);
    }

    private static void showPwFeedback(String msg, String bg, String fg) {
        if (pwFeedbackLabel == null) return;
        pwFeedbackLabel.setText(msg);
        pwFeedbackLabel.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-padding: 10 16 10 16; -fx-background-radius: 8; -fx-border-color: " + fg + "; -fx-border-radius: 8; -fx-border-width: 1;");
        pwFeedbackLabel.setVisible(true);
        pwFeedbackLabel.setManaged(true);
    }

    private static Label mkLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        return l;
    }
}