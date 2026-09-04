package com.desgin.view.operator;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorSettings {

    public static ScrollPane getSettingsSection() {
        // Section: Security & Password Management (Firestore Sync)
        VBox passwordCard = createPasswordCard();

        VBox content = new VBox(18, passwordCard);
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
