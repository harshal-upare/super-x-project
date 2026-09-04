package com.desgin.view.operator;

import java.io.File;

import com.desgin.config.CloudinaryConfig;
import com.desgin.dao.AuthDAO;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class OperatorSettings {

    public static ScrollPane getSettingsSection() {
        // Section 1: Certified Profile & Machine Credentials (Editable)
        VBox profileCard = createProfileEditCard();

        // Section 2: Security & Password Management (Firestore Sync)
        VBox passwordCard = createPasswordCard();

        VBox content = new VBox(22, profileCard, passwordCard);
        content.setPadding(new Insets(20, 30, 35, 30));
        content.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    // =========================================================
    // Profile & Credentials Edit Section (Firestore Sync)
    // =========================================================
    private static VBox createProfileEditCard() {
        Text t = new Text("👨‍🌾 Operator Certified Profile & Credentials");
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Update your personal profile, contact number, operational zone, machinery qualifications and driving license in Firestore");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        // Photo Upload Row
        ImageView photoView = new ImageView();
        photoView.setFitWidth(52);
        photoView.setFitHeight(52);
        Circle photoClip = new Circle(26, 26, 26);
        photoView.setClip(photoClip);
        if (OperatorProfileStore.profilePic != null && !OperatorProfileStore.profilePic.isEmpty()) {
            try { photoView.setImage(new Image(OperatorProfileStore.profilePic, true)); } catch (Exception ignored) {}
        }

        Text pIcon = new Text("👨‍🌾");
        pIcon.setStyle("-fx-font-size: 24px;");
        javafx.scene.layout.StackPane photoAvatarBox = new javafx.scene.layout.StackPane(pIcon, photoView);
        photoAvatarBox.setPrefSize(52, 52);
        photoAvatarBox.setMaxSize(52, 52);
        photoAvatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 26; -fx-border-color: #A5D6A7; -fx-border-width: 1.5; -fx-border-radius: 26;");

        Button uploadPhotoBtn = new Button("📷 Change Photo");
        uploadPhotoBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12;");

        Label photoStatusLbl = new Label("");
        photoStatusLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #15803D;");

        final String[] uploadedPhotoHolder = new String[]{ OperatorProfileStore.profilePic };

        uploadPhotoBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Operator Photo");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.bmp"));
            Window win = uploadPhotoBtn.getScene() != null ? uploadPhotoBtn.getScene().getWindow() : null;
            File file = chooser.showOpenDialog(win);
            if (file != null) {
                photoStatusLbl.setText("Uploading photo...");
                uploadPhotoBtn.setDisable(true);
                Task<String> task = new Task<>() {
                    @Override
                    protected String call() {
                        return CloudinaryConfig.uploadImage(file);
                    }
                };
                task.setOnSucceeded(ev -> {
                    uploadPhotoBtn.setDisable(false);
                    String url = task.getValue();
                    if (url != null && !url.isEmpty()) {
                        uploadedPhotoHolder[0] = url;
                        OperatorProfileStore.setProfilePic(url);
                        try { photoView.setImage(new Image(url, true)); } catch (Exception ignored) {}
                        photoStatusLbl.setText("✓ Photo updated");
                        new Thread(() -> new AuthDAO().updateOperatorBusinessInfo(OperatorProfileStore.email, null, null, url, null, null, null)).start();
                    } else {
                        photoStatusLbl.setText("Upload failed");
                    }
                });
                task.setOnFailed(ev -> {
                    uploadPhotoBtn.setDisable(false);
                    photoStatusLbl.setText("Upload error");
                });
                new Thread(task).start();
            }
        });

        VBox photoBtnBox = new VBox(3, uploadPhotoBtn, photoStatusLbl);
        photoBtnBox.setAlignment(Pos.CENTER_LEFT);
        HBox photoRow = new HBox(14, photoAvatarBox, photoBtnBox);
        photoRow.setAlignment(Pos.CENTER_LEFT);

        // Fields
        TextField nameField = new TextField(OperatorProfileStore.name != null ? OperatorProfileStore.name : "");
        nameField.setPrefHeight(38);
        nameField.setPrefWidth(280);
        nameField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        TextField contactField = new TextField(OperatorProfileStore.phone != null ? OperatorProfileStore.phone : "");
        contactField.setPrefHeight(38);
        contactField.setPrefWidth(280);
        contactField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        TextField locationField = new TextField(OperatorProfileStore.zone != null ? OperatorProfileStore.zone : "");
        locationField.setPrefHeight(38);
        locationField.setPrefWidth(280);
        locationField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        TextField licenseField = new TextField(OperatorProfileStore.licenseNo != null ? OperatorProfileStore.licenseNo : "");
        licenseField.setPrefHeight(38);
        licenseField.setPrefWidth(280);
        licenseField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        ComboBox<String> expCombo = new ComboBox<>();
        expCombo.getItems().addAll(
            "1-2 Years (Junior Machinery Operator)",
            "3-5 Years (Certified Heavy Machinery Operator)",
            "5-8 Years (Senior Field Specialist)",
            "8+ Years (Master Agro-Equipment Expert)"
        );
        expCombo.setValue(OperatorProfileStore.drivingExperience != null ? OperatorProfileStore.drivingExperience : "3-5 Years (Certified Heavy Machinery Operator)");
        expCombo.setPrefWidth(280);
        expCombo.setPrefHeight(38);
        expCombo.setStyle("-fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        ComboBox<String> profCombo = new ComboBox<>();
        profCombo.getItems().addAll(
            "Tractors & Heavy Tillage",
            "Combined Harvesters & Threshers",
            "Rotavators, Cultivators & Seeders",
            "High-Capacity Sprayers & Agri Drones",
            "Multi-Machinery Operator (All Types)"
        );
        profCombo.setValue(OperatorProfileStore.equipmentProfession != null ? OperatorProfileStore.equipmentProfession : "Tractors & Heavy Tillage");
        profCombo.setPrefWidth(280);
        profCombo.setPrefHeight(38);
        profCombo.setStyle("-fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12px;");

        // DL Upload
        ImageView dlThumbView = new ImageView();
        dlThumbView.setFitWidth(55);
        dlThumbView.setFitHeight(36);
        dlThumbView.setPreserveRatio(true);
        if (OperatorProfileStore.licenseImage != null && !OperatorProfileStore.licenseImage.isEmpty()) {
            try { dlThumbView.setImage(new Image(OperatorProfileStore.licenseImage, true)); } catch (Exception ignored) {}
        }
        Text dlThumbIcon = new Text("📄");
        dlThumbIcon.setStyle("-fx-font-size: 16px;");
        javafx.scene.layout.StackPane dlThumbBox = new javafx.scene.layout.StackPane(dlThumbIcon, dlThumbView);
        dlThumbBox.setPrefSize(55, 36);
        dlThumbBox.setStyle("-fx-background-color: #F4F9F4; -fx-background-radius: 6; -fx-border-color: #C2E0CE; -fx-border-radius: 6; -fx-border-width: 1;");

        Button uploadDlBtn = new Button("📄 Upload / Replace DL");
        uploadDlBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12;");
        Label dlStatusLbl = new Label("");
        dlStatusLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #15803D;");

        final String[] uploadedDlHolder = new String[]{ OperatorProfileStore.licenseImage };

        uploadDlBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Driving License Image");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.bmp"));
            Window win = uploadDlBtn.getScene() != null ? uploadDlBtn.getScene().getWindow() : null;
            File file = chooser.showOpenDialog(win);
            if (file != null) {
                dlStatusLbl.setText("Uploading...");
                uploadDlBtn.setDisable(true);
                Task<String> task = new Task<>() {
                    @Override
                    protected String call() {
                        return CloudinaryConfig.uploadImage(file);
                    }
                };
                task.setOnSucceeded(ev -> {
                    uploadDlBtn.setDisable(false);
                    String url = task.getValue();
                    if (url != null && !url.isEmpty()) {
                        uploadedDlHolder[0] = url;
                        OperatorProfileStore.licenseImage = url;
                        try { dlThumbView.setImage(new Image(url, true)); } catch (Exception ignored) {}
                        dlStatusLbl.setText("✓ DL attached");
                        new Thread(() -> new AuthDAO().updateOperatorBusinessInfo(OperatorProfileStore.email, null, null, null, null, null, url)).start();
                    } else {
                        dlStatusLbl.setText("Upload failed");
                    }
                });
                task.setOnFailed(ev -> {
                    uploadDlBtn.setDisable(false);
                    dlStatusLbl.setText("Upload error");
                });
                new Thread(task).start();
            }
        });

        HBox dlRow = new HBox(10, dlThumbBox, uploadDlBtn, dlStatusLbl);
        dlRow.setAlignment(Pos.CENTER_LEFT);

        GridPane g = new GridPane();
        g.setHgap(20);
        g.setVgap(12);

        g.add(createLabel("Full Name:"), 0, 0);
        g.add(nameField, 0, 1);

        g.add(createLabel("Contact Mobile:"), 1, 0);
        g.add(contactField, 1, 1);

        g.add(createLabel("Operational Zone:"), 0, 2);
        g.add(locationField, 0, 3);

        g.add(createLabel("Operator License No.:"), 1, 2);
        g.add(licenseField, 1, 3);

        g.add(createLabel("Driving Experience:"), 0, 4);
        g.add(expCombo, 0, 5);

        g.add(createLabel("Equipment Profession:"), 1, 4);
        g.add(profCombo, 1, 5);

        g.add(createLabel("Driving License (DL Image):"), 0, 6, 2, 1);
        g.add(dlRow, 0, 7, 2, 1);

        Label feedbackLbl = new Label();
        feedbackLbl.setVisible(false);
        feedbackLbl.setManaged(false);

        Button saveProfileBtn = new Button("💾  Save Profile Changes to Firestore");
        saveProfileBtn.setPrefHeight(40);
        saveProfileBtn.setStyle("-fx-background-color: linear-gradient(to right, #15803D, #22C55E); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 24; -fx-effect: dropshadow(gaussian, rgba(21,128,61,0.25), 6, 0, 0, 2);");

        saveProfileBtn.setOnAction(e -> {
            String n = nameField.getText().trim();
            String c = contactField.getText().trim();
            String l = locationField.getText().trim();
            String lic = licenseField.getText().trim();
            String exp = expCombo.getValue();
            String prof = profCombo.getValue();

            OperatorProfileStore.setProfile(n, c, l, lic);
            if (exp != null) OperatorProfileStore.drivingExperience = exp;
            if (prof != null) OperatorProfileStore.equipmentProfession = prof;

            saveProfileBtn.setDisable(true);
            saveProfileBtn.setText("Saving to Firestore...");

            new Thread(() -> {
                String email = OperatorProfileStore.email;
                new AuthDAO().updateOperatorBusinessInfo(
                    email, n, c, uploadedPhotoHolder[0], exp, prof, uploadedDlHolder[0], l, lic
                );
                Platform.runLater(() -> {
                    saveProfileBtn.setDisable(false);
                    saveProfileBtn.setText("💾  Save Profile Changes to Firestore");
                    feedbackLbl.setText("✓ Operator profile updated and saved to Firestore!");
                    feedbackLbl.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8 14; -fx-background-radius: 6; -fx-border-color: #86EFAC; -fx-border-radius: 6;");
                    feedbackLbl.setVisible(true);
                    feedbackLbl.setManaged(true);
                });
            }).start();
        });

        HBox btnRow = new HBox(12, saveProfileBtn, feedbackLbl);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        VBox b = new VBox(14, new VBox(3, t, sub), photoRow, g, btnRow);
        b.setPadding(new Insets(20));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 4, 0, 0, 1);");
        return b;
    }

    // =========================================================
    // Password Change Field via Firestore
    // =========================================================
    private static VBox createPasswordCard() {
        Text t = new Text("🔒 Security & Change Password (Firestore Sync)");
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Update your operator account login password securely stored in Firestore");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        GridPane g = new GridPane();
        g.setHgap(15);
        g.setVgap(12);

        PasswordField currentPassField = new PasswordField();
        currentPassField.setPromptText("Enter current account password");
        currentPassField.setPrefHeight(38);
        currentPassField.setPrefWidth(280);
        currentPassField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Enter new password");
        newPassField.setPrefHeight(38);
        newPassField.setPrefWidth(280);
        newPassField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Re-enter new password to confirm");
        confirmPassField.setPrefHeight(38);
        confirmPassField.setPrefWidth(280);
        confirmPassField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        g.add(createLabel("Current Password:"), 0, 0);
        g.add(currentPassField, 1, 0);

        g.add(createLabel("New Password:"), 0, 1);
        g.add(newPassField, 1, 1);

        g.add(createLabel("Confirm New Password:"), 0, 2);
        g.add(confirmPassField, 1, 2);

        Label feedbackLbl = new Label();
        feedbackLbl.setVisible(false);
        feedbackLbl.setManaged(false);

        Button updatePassBtn = new Button("🔑  Update Password in Firestore");
        updatePassBtn.setPrefHeight(38);
        updatePassBtn.setStyle("-fx-background-color: linear-gradient(to right, #15803D, #22C55E); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20; -fx-effect: dropshadow(gaussian, rgba(21,128,61,0.25), 6, 0, 0, 2);");

        updatePassBtn.setOnAction(e -> {
            String curr = currentPassField.getText().trim();
            String newP = newPassField.getText().trim();
            String confP = confirmPassField.getText().trim();

            if (curr.isEmpty()) {
                showPassFeedback(feedbackLbl, "✕ Please enter your current password.", false);
                return;
            }
            if (newP.isEmpty()) {
                showPassFeedback(feedbackLbl, "✕ Please enter your new password.", false);
                return;
            }
            if (!newP.equals(confP)) {
                showPassFeedback(feedbackLbl, "✕ New password and confirmation password do not match.", false);
                return;
            }

            updatePassBtn.setDisable(true);
            updatePassBtn.setText("Updating Firestore...");

            new Thread(() -> {
                try {
                    String email = OperatorProfileStore.email;
                    String phone = OperatorProfileStore.phone;
                    com.desgin.dao.AuthDAO dao = new com.desgin.dao.AuthDAO();
                    com.desgin.model.AuthenticateModel user = null;
                    if (email != null && !email.trim().isEmpty()) {
                        user = dao.getUser(email, "Operator");
                    }
                    if (user == null && phone != null && !phone.trim().isEmpty()) {
                        user = dao.getUser(phone, "Operator");
                    }

                    boolean currentOk = false;
                    if (user != null && user.getPassword() != null && !user.getPassword().isEmpty()) {
                        currentOk = user.getPassword().equals(curr);
                    } else if (OperatorProfileStore.currentPassword != null && !OperatorProfileStore.currentPassword.isEmpty()) {
                        currentOk = OperatorProfileStore.currentPassword.equals(curr);
                    } else {
                        // If no password recorded yet in Firestore, permit update
                        currentOk = true;
                    }

                    if (!currentOk) {
                        Platform.runLater(() -> {
                            updatePassBtn.setDisable(false);
                            updatePassBtn.setText("🔑  Update Password in Firestore");
                            showPassFeedback(feedbackLbl, "✕ Current password is incorrect.", false);
                        });
                        return;
                    }

                    String identifier = (user != null && user.getMail() != null && !user.getMail().isEmpty()) 
                            ? user.getMail() 
                            : ((email != null && !email.isEmpty()) ? email : phone);

                    boolean success = dao.updatePassword(identifier, "Operator", newP);
                    if (phone != null && !phone.isEmpty() && !phone.equals(identifier)) {
                        dao.updatePassword(phone, "Operator", newP);
                    }

                    Platform.runLater(() -> {
                        updatePassBtn.setDisable(false);
                        updatePassBtn.setText("🔑  Update Password in Firestore");
                        if (success) {
                            OperatorProfileStore.currentPassword = newP;
                            currentPassField.clear();
                            newPassField.clear();
                            confirmPassField.clear();
                            showPassFeedback(feedbackLbl, "✓ Password updated in Firestore! You can now log in with your new password.", true);
                        } else {
                            showPassFeedback(feedbackLbl, "✕ Failed to update password in Firestore. Please try again.", false);
                        }
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        updatePassBtn.setDisable(false);
                        updatePassBtn.setText("🔑  Update Password in Firestore");
                        showPassFeedback(feedbackLbl, "✕ Error updating password: " + ex.getMessage(), false);
                    });
                }
            }).start();
        });

        HBox btnRow = new HBox(12, updatePassBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        VBox b = new VBox(12, new VBox(3, t, sub), g, btnRow, feedbackLbl);
        b.setPadding(new Insets(18));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 4, 0, 0, 1);");
        return b;
    }

    private static void showPassFeedback(Label lbl, String msg, boolean isSuccess) {
        lbl.setText(msg);
        if (isSuccess) {
            lbl.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8 14; -fx-background-radius: 6; -fx-border-color: #86EFAC; -fx-border-radius: 6;");
        } else {
            lbl.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8 14; -fx-background-radius: 6; -fx-border-color: #FCA5A5; -fx-border-radius: 6;");
        }
        lbl.setVisible(true);
        lbl.setManaged(true);
    }

    private static Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        return l;
    }
}
