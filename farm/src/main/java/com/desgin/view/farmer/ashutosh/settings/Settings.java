package com.desgin.view.farmer.ashutosh.settings;

import com.desgin.view.components.PasswordEyeField;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Settings {

    // Controls
    private static TextField usernameField;
    private static TextField emailField;
    private static TextField phoneField;
    private static CheckBox notifSmsCheckBox;
    private static CheckBox notifEmailCheckBox;
    private static CheckBox notifBookingCheckBox;
    private static Label feedbackLabel;

    public static ScrollPane getSetting() {

        // Feedback Banner
        feedbackLabel = new Label();
        feedbackLabel.setVisible(false);
        feedbackLabel.setManaged(false);

        // ================= 1. ACCOUNT & SECURITY CARD =================
        VBox accountCard = createCard();
        Text accountTitle = createSectionTitle("👤  Farmer Account Credentials");

        usernameField = new TextField(FarmerProfileStore.name);
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(42);
        styleTextField(usernameField);

        emailField = new TextField(FarmerProfileStore.email);
        emailField.setPromptText("Enter email address");
        emailField.setPrefHeight(42);
        styleTextField(emailField);

        phoneField = new TextField(FarmerProfileStore.phone);
        phoneField.setPromptText("Enter registered mobile number");
        phoneField.setPrefHeight(42);
        styleTextField(phoneField);

        VBox usernameBox = createInputGroup("Full Name / Username", usernameField);
        VBox emailBox = createInputGroup("Email Address", emailField);
        VBox phoneBox = createInputGroup("Mobile Number", phoneField);

        GridPane accountGrid = new GridPane();
        accountGrid.setHgap(18);
        accountGrid.setVgap(14);
        accountGrid.add(usernameBox, 0, 0);
        accountGrid.add(emailBox, 1, 0);
        accountGrid.add(phoneBox, 0, 1);

        Button changePasswordButton = new Button("🔒  Change Password");
        changePasswordButton.setPrefHeight(42);
        changePasswordButton.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-text-fill: #2D6A4F;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 0 20px;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-radius: 10px;" +
                "-fx-cursor: hand;"
        );
        changePasswordButton.setOnAction(e -> showChangePasswordDialog());

        VBox passBox = createInputGroup("Security", changePasswordButton);
        accountGrid.add(passBox, 1, 1);

        accountCard.getChildren().addAll(accountTitle, accountGrid);

        // ================= 2. NOTIFICATION PREFERENCES CARD =================
        VBox notifCard = createCard();
        Text notifTitle = createSectionTitle("🔔  Notification Alerts & Reminders");

        notifBookingCheckBox = new CheckBox("Rental Booking Status Alerts (Instant updates when machinery is approved / dispatched)");
        notifBookingCheckBox.setSelected(true);
        styleCheckBox(notifBookingCheckBox);

        notifSmsCheckBox = new CheckBox("SMS Alerts (Receive booking OTP & operator contact directly on mobile)");
        notifSmsCheckBox.setSelected(true);
        styleCheckBox(notifSmsCheckBox);

        notifEmailCheckBox = new CheckBox("Weekly Agri Advisory, Crop Weather & Monsoon Insights via Email");
        notifEmailCheckBox.setSelected(false);
        styleCheckBox(notifEmailCheckBox);

        VBox notifList = new VBox(12, notifBookingCheckBox, notifSmsCheckBox, notifEmailCheckBox);
        notifCard.getChildren().addAll(notifTitle, notifList);

        // ================= 3. ACTION BUTTONS BAR =================
        Button saveButton = new Button("✓  Save Settings");
        saveButton.setPrefHeight(44);
        saveButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8px 26px;" +
                "-fx-background-radius: 12px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"
        );
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-padding: 8px 26px; -fx-background-radius: 12px; -fx-cursor: hand;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-padding: 8px 26px; -fx-background-radius: 12px; -fx-cursor: hand;"));
        saveButton.setOnAction(e -> saveSettings());

        Button resetButton = new Button("Reset to Default");
        resetButton.setPrefHeight(44);
        resetButton.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-text-fill: #2D6A4F;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8px 20px;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-radius: 12px;" +
                "-fx-cursor: hand;"
        );
        resetButton.setOnAction(e -> resetSettings());

        Region actionSpacer = new Region();
        HBox.setHgrow(actionSpacer, Priority.ALWAYS);

        HBox actionToolbar = new HBox(14, saveButton, resetButton, actionSpacer);
        actionToolbar.setAlignment(Pos.CENTER_LEFT);

        // ================= MAIN CONTAINER =================
        VBox mainContainer = new VBox(20, feedbackLabel, accountCard, notifCard, actionToolbar);
        mainContainer.setPadding(new Insets(20, 30, 35, 30));
        mainContainer.setMaxWidth(Double.MAX_VALUE);
        mainContainer.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static VBox createCard() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(18, 22, 20, 22));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );
        return card;
    }

    private static Text createSectionTitle(String title) {
        Text t = new Text(title);
        t.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #1B4332;"
        );
        return t;
    }

    private static VBox createInputGroup(String labelText, javafx.scene.Node control) {
        Label label = new Label(labelText);
        label.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1B4332;"
        );
        VBox box = new VBox(6, label, control);
        box.setPrefWidth(360);
        return box;
    }

    private static void styleTextField(TextField field) {
        field.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1F2937;" +
                "-fx-padding: 0 14px;"
        );
    }

    private static void styleCheckBox(CheckBox cb) {
        cb.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #374151;" +
                "-fx-cursor: hand;"
        );
    }

    private static void saveSettings() {
        if (usernameField != null && !usernameField.getText().trim().isEmpty()) {
            FarmerProfileStore.name = usernameField.getText().trim();
        }
        if (emailField != null && !emailField.getText().trim().isEmpty()) {
            FarmerProfileStore.email = emailField.getText().trim();
        }
        if (phoneField != null && !phoneField.getText().trim().isEmpty()) {
            FarmerProfileStore.phone = phoneField.getText().trim();
        }

        if (feedbackLabel != null) {
            feedbackLabel.setText("✓ Settings and credentials saved successfully!");
            feedbackLabel.setStyle(
                    "-fx-background-color: #DCFCE7;" +
                    "-fx-text-fill: #15803D;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10px 16px;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-border-color: #86EFAC;" +
                    "-fx-border-radius: 10px;"
            );
            feedbackLabel.setVisible(true);
            feedbackLabel.setManaged(true);
        }
    }

    private static void resetSettings() {
        if (usernameField != null) usernameField.setText(FarmerProfileStore.name);
        if (emailField != null) emailField.setText(FarmerProfileStore.email);
        if (phoneField != null) phoneField.setText(FarmerProfileStore.phone);
        if (notifBookingCheckBox != null) notifBookingCheckBox.setSelected(true);
        if (notifSmsCheckBox != null) notifSmsCheckBox.setSelected(true);
        if (notifEmailCheckBox != null) notifEmailCheckBox.setSelected(false);

        if (feedbackLabel != null) {
            feedbackLabel.setText("↺ Settings restored to saved credentials.");
            feedbackLabel.setStyle(
                    "-fx-background-color: #E8F5E9;" +
                    "-fx-text-fill: #2D6A4F;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10px 16px;" +
                    "-fx-background-radius: 10px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                    "-fx-border-radius: 10px;"
            );
            feedbackLabel.setVisible(true);
            feedbackLabel.setManaged(true);
        }
    }

    private static void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Security Password");

        // Custom styled header
        Text dlgTitle = new Text("🔒  Update Password");
        dlgTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text dlgSub = new Text("Create a strong password (min 8 chars, 1 uppercase, 1 digit, 1 symbol)");
        dlgSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(3, dlgTitle, dlgSub);

        PasswordEyeField oldPassword = new PasswordEyeField("Current password");
        oldPassword.setCustomPrefSize(340, 40);

        PasswordEyeField newPassword = new PasswordEyeField("New password");
        newPassword.setCustomPrefSize(340, 40);

        PasswordEyeField confirmPassword = new PasswordEyeField("Confirm new password");
        confirmPassword.setCustomPrefSize(340, 40);

        Label errLbl = new Label();
        errLbl.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 11.5px; -fx-font-weight: bold;");
        errLbl.setWrapText(true);
        errLbl.setMaxWidth(340);
        errLbl.setVisible(false);
        errLbl.setManaged(false);

        VBox content = new VBox(12);
        content.setPadding(new Insets(20, 24, 20, 24));
        content.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px;");

        content.getChildren().addAll(
                titleBox,
                new Label("Current Password:"), oldPassword,
                new Label("New Password:"), newPassword,
                new Label("Confirm New Password:"), confirmPassword,
                errLbl
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px;");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        if (okBtn != null) {
            okBtn.setText("Update Password");
            okBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 8px 18px;");
        }

        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        if (cancelBtn != null) {
            cancelBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 8px 18px;");
        }

        dialog.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                String newP = newPassword.getText();
                String confP = confirmPassword.getText();

                if (newP == null || newP.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password cannot be empty.", ButtonType.OK);
                    alert.showAndWait();
                } else if (!newP.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password must be at least 8 characters, with 1 uppercase, 1 digit & 1 symbol.", ButtonType.OK);
                    alert.showAndWait();
                } else if (!newP.equals(confP)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match.", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password updated successfully!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }
}
