package com.desgin.view.farmer.ashutosh.settings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.scene.text.Text;

public class Settings {

    // Main colors
    private final String BROWN = "#4A2C20";
    private final String LIGHT_BROWN = "#F5EDE7";
    private final String CREAM = "#FFF9F5";
    private final String WHITE = "#FFFFFF";
    private final String ACCENT = "#B8795B";
    private final String BORDER = "#E5D5CC";

    // Settings controls
    private CheckBox notificationCheckBox;
    

    
    private TextField usernameField;
    private TextField emailField;

    /**
     * Creates and returns the complete Settings page.
     */
    public ScrollPane getSetting() {

        // ==============================
        // Main container
        // ==============================

        VBox mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(30, 40, 40, 40));
        mainContainer.setStyle(
                "-fx-background-color: " + CREAM + ";"
        );

        // ==============================
        // Header
        // ==============================

        Text settingTitle = new Text("Settings");
        settingTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + BROWN + ";"
        );

        Text subtitle = new Text(
                "Manage your account, preferences and application settings."
        );

        subtitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-fill: #8A6A5B;"
        );

        VBox header = new VBox(6);
        header.getChildren().addAll(settingTitle, subtitle);

        // ==============================
        // Account Section
        // ==============================

        VBox accountCard = createCard();

        Text accountTitle = createSectionTitle("👤  Account");

        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefHeight(40);
        styleTextField(usernameField);

        emailField = new TextField();
        emailField.setPromptText("Enter email address");
        emailField.setPrefHeight(40);
        styleTextField(emailField);

        VBox usernameBox = createInputGroup(
                "Username",
                usernameField
        );

        VBox emailBox = createInputGroup(
                "Email Address",
                emailField
        );

        Button changePasswordButton = createButton(
                "Change Password"
        );

        changePasswordButton.setOnAction(e -> showChangePasswordDialog());

        accountCard.getChildren().addAll(
                accountTitle,
                usernameBox,
                emailBox,
                changePasswordButton
        );

       
       

        // ==============================
        // Notification Section
        // ==============================

        VBox notificationCard = createCard();

        Text notificationTitle =
                createSectionTitle("🔔  Notifications");

        notificationCheckBox =
                new CheckBox("Enable notifications");

        notificationCheckBox.setSelected(true);

        notificationCheckBox.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + BROWN + ";"
        );

        Text notificationDescription =
                createDescription(
                        "Receive notifications about important updates."
                );

        notificationCard.getChildren().addAll(
                notificationTitle,
                notificationCheckBox,
                notificationDescription
        );

        // ==============================
        // Sound Section
        // ==============================

       

        

        // ==============================
        // Security Section
        // ==============================

        VBox securityCard = createCard();

        Text securityTitle =
                createSectionTitle("🔐  Security");

        CheckBox rememberMe =
                new CheckBox("Remember me on this device");

        rememberMe.setSelected(true);

        rememberMe.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + BROWN + ";"
        );

        Button logoutButton =
                createDangerButton("Logout");

        logoutButton.setOnAction(
                e -> showLogoutConfirmation()
        );

        securityCard.getChildren().addAll(
                securityTitle,
                rememberMe,
                logoutButton
        );

        // ==============================
        // Action Buttons
        // ==============================

        Button saveButton =
                createButton("Save Settings");

        saveButton.setOnAction(
                e -> saveSettings()
        );

        Button resetButton =
                createSecondaryButton("Reset to Defaults");

        resetButton.setOnAction(
                e -> resetSettings()
        );

        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        actionButtons.getChildren().addAll(
                resetButton,
                saveButton
        );

        // ==============================
        // Add everything
        // ==============================

        mainContainer.getChildren().addAll(
                header,
                accountCard,
            
                notificationCard,
                securityCard,
                actionButtons
        );

        // ==============================
        // ScrollPane
        // ==============================

        ScrollPane scrollPane =
                new ScrollPane(mainContainer);

        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;"
        );

        return scrollPane;
    }

    // =========================================================
    // CARD CREATION
    // =========================================================

    private VBox createCard() {

        VBox card = new VBox(15);

        card.setPadding(new Insets(22));

        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 1px;"
        );

        return card;
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private Text createSectionTitle(String text) {

        Text title = new Text(text);

        title.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + BROWN + ";"
        );

        return title;
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    private Text createDescription(String text) {

        Text description = new Text(text);

        description.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #947D70;"
        );

        return description;
    }

    // =========================================================
    // INPUT GROUP
    // =========================================================

    private VBox createInputGroup(
            String label,
            Control control
    ) {

        Label labelText = new Label(label);

        labelText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + BROWN + ";"
        );

        VBox box = new VBox(7);

        box.getChildren().addAll(
                labelText,
                control
        );

        return box;
    }

    // =========================================================
    // TEXT FIELD STYLE
    // =========================================================

    private void styleTextField(TextField field) {

        field.setStyle(
                "-fx-background-color: #FFFDFC;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 8px 12px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;"
        );
    }

    // =========================================================
    // COMBOBOX STYLE
    // =========================================================

    private void styleComboBox(
            ComboBox<String> comboBox) {
     

        comboBox.setStyle(
                "-fx-background-color: #FFFDFC;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;"
        );
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefHeight(42);
        button.setPadding(
                new Insets(0, 22, 0, 22)
        );

        button.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 9px;" +
                "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: #9E6045;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 9px;" +
                        "-fx-cursor: hand;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: " + ACCENT + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 9px;" +
                        "-fx-cursor: hand;"
                )
        );

        return button;
    }

    // =========================================================
    // SECONDARY BUTTON
    // =========================================================

    private Button createSecondaryButton(
            String text
    ) {

        Button button = new Button(text);

        button.setPrefHeight(42);
        button.setPadding(
                new Insets(0, 22, 0, 22)
        );

        button.setStyle(
                "-fx-background-color: " + LIGHT_BROWN + ";" +
                "-fx-text-fill: " + BROWN + ";" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 9px;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 9px;" +
                "-fx-cursor: hand;"
        );

        return button;
    }

    // =========================================================
    // DANGER BUTTON
    // =========================================================

    private Button createDangerButton(
            String text
    ) {

        Button button = new Button(text);

        button.setPrefHeight(40);
        button.setPadding(
                new Insets(0, 20, 0, 20)
        );

        button.setStyle(
                "-fx-background-color: #F8E4E1;" +
                "-fx-text-fill: #B7473A;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;"
        );

        return button;
    }

    // =========================================================
    // SAVE SETTINGS
    // =========================================================

    private void saveSettings() {

        String username =
                usernameField.getText();

        String email =
                emailField.getText();

        

        

        boolean notifications =
                notificationCheckBox.isSelected();

        
        

        System.out.println(
                "===== SETTINGS SAVED ====="
        );

        System.out.println(
                "Username: " + username
        );

        System.out.println(
                "Email: " + email
        );

       
       
        System.out.println(
                "Notifications: " + notifications
        );

       

        
        showInformation(
                "Settings Saved",
                "Your settings have been saved successfully."
        );
    }

    // =========================================================
    // RESET SETTINGS
    // =========================================================

    private void resetSettings() {

        usernameField.clear();
        emailField.clear();

        
        

        notificationCheckBox.setSelected(true);

        

      

        showInformation(
                "Settings Reset",
                "All settings have been restored to their defaults."
        );
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    private void showChangePasswordDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle("Change Password");

        PasswordField oldPassword =
                new PasswordField();

        oldPassword.setPromptText(
                "Current password"
        );

        PasswordField newPassword =
                new PasswordField();

        newPassword.setPromptText(
                "New password"
        );

        PasswordField confirmPassword =
                new PasswordField();

        confirmPassword.setPromptText(
                "Confirm new password"
        );

        VBox content = new VBox(12);

        content.setPadding(
                new Insets(20)
        );

        content.getChildren().addAll(
                new Label("Current Password"),
                oldPassword,
                new Label("New Password"),
                newPassword,
                new Label("Confirm Password"),
                confirmPassword
        );

        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.OK,
                        ButtonType.CANCEL
                );

        dialog.showAndWait();

        if (newPassword.getText().isEmpty()) {
            return;
        }

        if (!newPassword.getText()
                .equals(confirmPassword.getText())) {

            showError(
                    "Password Error",
                    "New passwords do not match."
            );

            return;
        }

        showInformation(
                "Password Changed",
                "Your password has been changed successfully."
        );
    }

    // =========================================================
    // LOGOUT CONFIRMATION
    // =========================================================

    private void showLogoutConfirmation() {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure?");
        alert.setContentText(
                "Do you want to logout from your account?"
        );

        alert.showAndWait().ifPresent(
                response -> {

                    if (response ==
                            ButtonType.OK) {

                        System.out.println(
                                "User logged out."
                        );
                    }
                }
        );
    }

    // =========================================================
    // INFORMATION DIALOG
    // =========================================================

    private void showInformation(
            String title,
            String message
    ) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // ERROR DIALOG
    // =========================================================

    private void showError(
            String title,
            String message
    ) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
