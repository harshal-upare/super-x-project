package com.desgin.view.handling_start;

import java.util.concurrent.CompletableFuture;

import com.desgin.controller.AuthenticateController;
import com.desgin.model.AuthenticateModel;
import com.desgin.view.components.PasswordEyeField;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;
import com.desgin.view.operator.OperatorDashboard;
import com.desgin.view.provider.ProviderDashboard;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Authentication {
    
    private Scene authenticationScene;
    private StackPane rootStackPane;

    private static final String INPUT_NORMAL_STYLE = 
        "-fx-background-color: #FFFFFF; -fx-border-color: #D6DDD2; -fx-border-width: 1.2px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1F2937;";
    
    private static final String INPUT_FOCUS_STYLE = 
        "-fx-background-color: #FFFFFF; -fx-border-color: #2D6A4F; -fx-border-width: 1.5px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1F2937;";
    
    private static final String INPUT_ERROR_STYLE = 
        "-fx-background-color: #FFF8F8; -fx-border-color: #DC2626; -fx-border-width: 1.5px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1F2937;";

    private static final String PWD_NORMAL_STYLE = 
        "-fx-background-color: #FFFFFF; -fx-border-color: #D6DDD2; -fx-border-width: 1.2px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 36px 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1F2937;";
    
    private static final String PWD_FOCUS_STYLE = 
        "-fx-background-color: #FFFFFF; -fx-border-color: #2D6A4F; -fx-border-width: 1.5px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 36px 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1F2937;";
    
    private static final String PWD_ERROR_STYLE = 
        "-fx-background-color: #FFF8F8; -fx-border-color: #DC2626; -fx-border-width: 1.5px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 0 36px 0 12px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1F2937;";

    Scene getAuthenticationScene() {

        BorderPane borderPane = new BorderPane();

        // ------------------ LOGO & HEADER ------------------
        Image img;
        try {
            img = new Image("file:farm/src/main/resources/assets/Images/logo.png");
            if (img.isError()) {
                img = new Image(getClass().getResourceAsStream("/assets/Images/logo.png"));
            }
        } catch (Exception e) {
            img = new Image("file:farm/src/main/resources/assets/Images/logo.jpeg");
        }

        ImageView loginPageImageView = new ImageView(img);
        loginPageImageView.setFitWidth(54);
        loginPageImageView.setFitHeight(54);
        loginPageImageView.setPreserveRatio(true);
        loginPageImageView.setSmooth(true);

        Text wlcBack = new Text("Welcome Back");
        wlcBack.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332; -fx-font-family: 'Poppins';");

        Text loginToFarmEquipText = new Text("Login to FarmEquip");
        loginToFarmEquipText.setStyle("-fx-font-size: 13.5px; -fx-font-weight: 600; -fx-fill: #4B5563; -fx-font-family: 'Poppins';");

        VBox primaryVBox = new VBox(5, loginPageImageView, wlcBack, loginToFarmEquipText);
        primaryVBox.setAlignment(Pos.CENTER);

        // ------------------ INPUT FIELDS & LABELS ------------------
        Label label1 = new Label("Email or Mobile No.");
        label1.setStyle("-fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins';");

        TextField mailAndPhoneTextField = new TextField();
        mailAndPhoneTextField.setPromptText("Enter email");
        mailAndPhoneTextField.setPrefSize(290, 40);
        mailAndPhoneTextField.setMaxSize(290, 40);
        mailAndPhoneTextField.setFocusTraversable(false);
        mailAndPhoneTextField.setStyle(INPUT_NORMAL_STYLE);

        Label emailErrorLabel = new Label();
        emailErrorLabel.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Poppins';");
        emailErrorLabel.setWrapText(true);
        emailErrorLabel.setMaxWidth(290);
        emailErrorLabel.setVisible(false);
        emailErrorLabel.setManaged(false);

        mailAndPhoneTextField.focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (emailErrorLabel.isVisible()) return;
            mailAndPhoneTextField.setStyle(isFocused ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
        });

        mailAndPhoneTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (emailErrorLabel.isVisible()) {
                emailErrorLabel.setText("");
                emailErrorLabel.setVisible(false);
                emailErrorLabel.setManaged(false);
                mailAndPhoneTextField.setStyle(mailAndPhoneTextField.isFocused() ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
            }
        });

        Label label2 = new Label("Password");
        label2.setStyle("-fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins';");

        PasswordEyeField passwordTextField = new PasswordEyeField("Enter your password");
        passwordTextField.setCustomPrefSize(290, 40);
        passwordTextField.setFocusTraversable(false);
        passwordTextField.getHiddenField().setStyle(PWD_NORMAL_STYLE);
        passwordTextField.getShownField().setStyle(PWD_NORMAL_STYLE);

        Label passwordErrorLabel = new Label();
        passwordErrorLabel.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Poppins';");
        passwordErrorLabel.setWrapText(true);
        passwordErrorLabel.setMaxWidth(290);
        passwordErrorLabel.setVisible(false);
        passwordErrorLabel.setManaged(false);

        // Password focus handling
        passwordTextField.getHiddenField().focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (passwordErrorLabel.isVisible()) return;
            passwordTextField.getHiddenField().setStyle(isFocused ? PWD_FOCUS_STYLE : PWD_NORMAL_STYLE);
        });
        passwordTextField.getShownField().focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (passwordErrorLabel.isVisible()) return;
            passwordTextField.getShownField().setStyle(isFocused ? PWD_FOCUS_STYLE : PWD_NORMAL_STYLE);
        });

        passwordTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (passwordErrorLabel.isVisible()) {
                passwordErrorLabel.setText("");
                passwordErrorLabel.setVisible(false);
                passwordErrorLabel.setManaged(false);
                boolean isFoc = passwordTextField.getHiddenField().isFocused() || passwordTextField.getShownField().isFocused();
                String st = isFoc ? PWD_FOCUS_STYLE : PWD_NORMAL_STYLE;
                passwordTextField.getHiddenField().setStyle(st);
                passwordTextField.getShownField().setStyle(st);
            }
        });

        // ------------------ ROLES (3 ROLES: Farmer, Provider, Operator) ------------------
        Label roleLabel = new Label("Login As");
        roleLabel.setStyle("-fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins';");

        RadioButton farmerRadio = new RadioButton("Farmer");
        RadioButton providerRadio = new RadioButton("Provider");
        RadioButton operatorRadio = new RadioButton("Operator");

        String radioStyle = "-fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: #374151; -fx-cursor: hand; -fx-font-family: 'Poppins';";
        farmerRadio.setStyle(radioStyle);
        providerRadio.setStyle(radioStyle);
        operatorRadio.setStyle(radioStyle);

        farmerRadio.setFocusTraversable(false);
        providerRadio.setFocusTraversable(false);
        operatorRadio.setFocusTraversable(false);

        ToggleGroup loginRoleGroup = new ToggleGroup();
        farmerRadio.setToggleGroup(loginRoleGroup);
        providerRadio.setToggleGroup(loginRoleGroup);
        operatorRadio.setToggleGroup(loginRoleGroup);

        HBox roleHBox = new HBox(16, farmerRadio, providerRadio, operatorRadio);
        roleHBox.setAlignment(Pos.CENTER_LEFT);
        roleHBox.setPadding(new Insets(2, 0, 0, 0));

        VBox emailBox = new VBox(3, label1, mailAndPhoneTextField, emailErrorLabel);
        VBox passwordBox = new VBox(3, label2, passwordTextField, passwordErrorLabel);
        VBox roleBox = new VBox(3, roleLabel, roleHBox);

        VBox secondaryVBox = new VBox(10, emailBox, passwordBox, roleBox);
        secondaryVBox.setMaxWidth(290);
        secondaryVBox.setAlignment(Pos.CENTER_LEFT);

        // ------------------ LOGIN BUTTON ------------------
        Button loginButton = new Button("Login");
        String buttonNormalStyle = "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-background-radius: 10px; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-family: 'Poppins'; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.35), 8, 0, 0, 3);";
        String buttonHoverStyle = "-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-background-radius: 10px; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-family: 'Poppins'; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.45), 10, 0, 0, 4);";

        loginButton.setStyle(buttonNormalStyle);
        loginButton.setOnMouseEntered(e -> {
            if (!loginButton.isDisable()) loginButton.setStyle(buttonHoverStyle);
        });
        loginButton.setOnMouseExited(e -> {
            if (!loginButton.isDisable()) loginButton.setStyle(buttonNormalStyle);
        });

        loginButton.setPrefHeight(42);
        loginButton.setPrefWidth(290);
        loginButton.setMaxWidth(290);
        loginButton.setFocusTraversable(false);

        // Fast Asynchronous Action on Login (Zero UI Delay)
        loginButton.setOnAction(e -> {
            boolean isValid = validateCredentials(mailAndPhoneTextField, emailErrorLabel, passwordTextField, passwordErrorLabel);
            if (!isValid) {
                return;
            }

            String inputUser = mailAndPhoneTextField.getText().trim();
            String inputPassword = passwordTextField.getText();

            RadioButton valRadio = (RadioButton) loginRoleGroup.getSelectedToggle();
            String selectedRole = valRadio.getText();

            Runnable backToLogin = () -> backtologin();

            AuthenticateController objController = new AuthenticateController();

            if ("Provider".equalsIgnoreCase(selectedRole)) {
                
                boolean validUser = objController.isUser(inputUser,selectedRole);

                if(validUser) {

                    ProviderDashboard obj = new ProviderDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getProviderDashboardScene(backToLogin));
                } else {

                }
            } else if ("Operator".equalsIgnoreCase(selectedRole)) {

                boolean validUser = objController.isUser(inputUser,selectedRole);

                if(validUser) {

                    OperatorDashboard obj = new OperatorDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getOperatorDashboardScene(backToLogin));
                } else {

                }
                
            } else if ("Farmer".equalsIgnoreCase(selectedRole)) {

                boolean validUser = objController.isUser(inputUser,selectedRole);

                if(validUser) {

                    FarmerDashboard obj = new FarmerDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getfarmerDashboardScene(backToLogin));
                } else {

                }
            }
        });

        // ------------------ REGISTER LINK ------------------
        Text noAccText = new Text("Don't have an account? ");
        noAccText.setStyle("-fx-font-size: 13px; -fx-fill: #374151; -fx-font-weight: 500; -fx-font-family: 'Poppins';");

        Text registerText = new Text("Register here");
        registerText.setStyle("-fx-font-size: 13.5px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';");

        HBox registerHBox = new HBox(4, noAccText, registerText);
        registerHBox.setAlignment(Pos.CENTER);
        registerHBox.setStyle("-fx-cursor: hand; -fx-padding: 2px 0 0 0;");

        registerHBox.setOnMouseEntered(e -> registerText.setStyle("-fx-font-size: 13.5px; -fx-fill: #1B4332; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';"));
        registerHBox.setOnMouseExited(e -> registerText.setStyle("-fx-font-size: 13.5px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';"));

        registerHBox.setOnMouseClicked(event -> {
            RegisterPage registerPage = new RegisterPage();
            Runnable backToLogin = () -> backtologin();
            WelcomePage.welcomePageStage.setScene(registerPage.getRegisterPageScene(backToLogin));
        });

        // ------------------ ADMIN LOGIN FOOTER ------------------
        HBox divider = new HBox();
        divider.setPrefHeight(1);
        divider.setMaxHeight(1);
        divider.setPrefWidth(290);
        divider.setMaxWidth(290);
        divider.setStyle("-fx-background-color: rgba(45, 106, 79, 0.2);");

        Label adminLoginLabel = new Label("🔒 Admin Portal Login");
        String adminNormalStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2D6A4F; -fx-background-color: #E8F5E9; -fx-background-radius: 16px; -fx-padding: 5px 14px; -fx-cursor: hand; -fx-border-color: rgba(45, 106, 79, 0.3); -fx-border-radius: 16px; -fx-border-width: 1px; -fx-font-family: 'Poppins';";
        String adminHoverStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #2D6A4F; -fx-background-radius: 16px; -fx-padding: 5px 14px; -fx-cursor: hand; -fx-border-color: #2D6A4F; -fx-border-radius: 16px; -fx-border-width: 1px; -fx-font-family: 'Poppins';";

        adminLoginLabel.setStyle(adminNormalStyle);
        adminLoginLabel.setOnMouseEntered(e -> adminLoginLabel.setStyle(adminHoverStyle));
        adminLoginLabel.setOnMouseExited(e -> adminLoginLabel.setStyle(adminNormalStyle));

        adminLoginLabel.setOnMouseClicked(event -> {
            boolean isValid = validateCredentials(mailAndPhoneTextField, emailErrorLabel, passwordTextField, passwordErrorLabel);
            if (!isValid) {
                return;
            }

            Runnable backToLogin = () -> backtologin();
            com.desgin.view.admin.AdminDashboard obj = new com.desgin.view.admin.AdminDashboard();
            WelcomePage.welcomePageStage.setScene(obj.getAdminDashboardScene(backToLogin));
        });

        VBox adminBox = new VBox(8, divider, adminLoginLabel);
        adminBox.setAlignment(Pos.CENTER);

        // ------------------ MAIN LOGIN CARD (TRANSLUCENT) ------------------
        VBox loginVBox = new VBox(15, primaryVBox, secondaryVBox, loginButton, registerHBox, adminBox);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.58);" +
            "-fx-background-radius: 22px;" +
            "-fx-border-color: rgba(255, 255, 255, 0.60);" +
            "-fx-border-radius: 22px;" +
            "-fx-border-width: 1.5px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.20), 24, 0.12, 0, 8);" +
            "-fx-padding: 24px 34px 22px 34px;"
        );

        loginVBox.setPrefWidth(420);
        loginVBox.setMaxWidth(420);
        loginVBox.setMaxHeight(Region.USE_PREF_SIZE);

        borderPane.setCenter(loginVBox);
        borderPane.setStyle("-fx-background-color: transparent;");

        // ------------------ BACKGROUND ------------------
        Image backgroundImage;
        try {
            backgroundImage = new Image("/assets/Images/background.jpeg");
            if (backgroundImage.isError()) {
                backgroundImage = new Image("file:farm/src/main/resources/assets/Images/background.jpeg");
            }
        } catch (Exception e) {
            backgroundImage = new Image("file:farm/src/main/resources/assets/Images/background.jpeg");
        }

        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);
        backgroundImageView.setMouseTransparent(true);

        rootStackPane = new StackPane();
        rootStackPane.getChildren().addAll(backgroundImageView, borderPane);

        backgroundImageView.fitWidthProperty().bind(rootStackPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootStackPane.heightProperty());

        authenticationScene = new Scene(rootStackPane);

        return authenticationScene;
    }

    // ============================================================
    // VALIDATION: MOBILE (10 DIGITS WITHOUT STARTS WITH) &
    // PASSWORD (8 CHARS, 1 UPPERCASE, 1 DIGIT, 1 SYMBOL, NO ERROR SYMBOLS)
    // ============================================================
    private boolean validateCredentials(TextField mailAndPhoneTextField, Label emailErrorLabel, 
                                        PasswordEyeField passwordTextField, Label passwordErrorLabel) {
        
        String inputUser = mailAndPhoneTextField.getText() != null ? mailAndPhoneTextField.getText().trim() : "";
        String inputPassword = passwordTextField.getText() != null ? passwordTextField.getText() : "";

        boolean isValid = true;

        // Validate Email or 10-Digit Indian Mobile Number
        if (inputUser.isEmpty()) {
            emailErrorLabel.setText("Email or Mobile Number is required.");
            emailErrorLabel.setVisible(true);
            emailErrorLabel.setManaged(true);
            mailAndPhoneTextField.setStyle(INPUT_ERROR_STYLE);
            isValid = false;
        } else {
            boolean isIndianPhone = inputUser.matches("^(?:\\+91|0)?[6-9]\\d{9}$");
            boolean isEmail = inputUser.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

            if (!isIndianPhone && !isEmail) {
                emailErrorLabel.setText("Enter a valid Email or 10-digit Indian Mobile Number.");
                emailErrorLabel.setVisible(true);
                emailErrorLabel.setManaged(true);
                mailAndPhoneTextField.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else {
                emailErrorLabel.setText("");
                emailErrorLabel.setVisible(false);
                emailErrorLabel.setManaged(false);
                mailAndPhoneTextField.setStyle(INPUT_NORMAL_STYLE);
            }
        }

        // Validate Password (8 chars, 1 uppercase, 1 digit, 1 symbol)
        if (inputPassword.isEmpty()) {
            passwordErrorLabel.setText("Password is required.");
            passwordErrorLabel.setVisible(true);
            passwordErrorLabel.setManaged(true);
            passwordTextField.getHiddenField().setStyle(PWD_ERROR_STYLE);
            passwordTextField.getShownField().setStyle(PWD_ERROR_STYLE);
            isValid = false;
        } else if (!inputPassword.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
            passwordErrorLabel.setText("Password must be at least 8 characters, with 1 uppercase, 1 digit & 1 symbol.");
            passwordErrorLabel.setVisible(true);
            passwordErrorLabel.setManaged(true);
            passwordTextField.getHiddenField().setStyle(PWD_ERROR_STYLE);
            passwordTextField.getShownField().setStyle(PWD_ERROR_STYLE);
            isValid = false;
        } else {
            passwordErrorLabel.setText("");
            passwordErrorLabel.setVisible(false);
            passwordErrorLabel.setManaged(false);
            passwordTextField.getHiddenField().setStyle(PWD_NORMAL_STYLE);
            passwordTextField.getShownField().setStyle(PWD_NORMAL_STYLE);
        }

        return isValid;
    }

    public void backtologin() {
        WelcomePage.welcomePageStage.setScene(authenticationScene);
    }
}
