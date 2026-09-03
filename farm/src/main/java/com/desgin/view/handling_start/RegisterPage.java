package com.desgin.view.handling_start;

import com.desgin.controller.AuthenticateController;
import com.desgin.view.components.PasswordEyeField;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;
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

public class RegisterPage {

    private Scene registerScene;

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

    public Scene getRegisterPageScene(Runnable backToLogin) {

        BorderPane borderPane = new BorderPane();

        // ================= LOGO & HEADER =================
        Image img;
        try {
            img = new Image("file:farm/src/main/resources/assets/Images/logo.png");
            if (img.isError()) {
                img = new Image(getClass().getResourceAsStream("/assets/Images/logo.png"));
            }
        } catch (Exception e) {
            img = new Image("file:farm/src/main/resources/assets/Images/logo.jpeg");
        }

        ImageView registrationPageImageView = new ImageView(img);
        registrationPageImageView.setFitWidth(48);
        registrationPageImageView.setFitHeight(48);
        registrationPageImageView.setPreserveRatio(true);
        registrationPageImageView.setSmooth(true);

        Text createAccount = new Text("Create Account");
        createAccount.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text joinFarmEquip = new Text("Join the FarmEquip Agriculture Network");
        joinFarmEquip.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: 500; -fx-fill: #4B5563;");

        VBox primaryVBox = new VBox(3, registrationPageImageView, createAccount, joinFarmEquip);
        primaryVBox.setAlignment(Pos.CENTER);

        // ================= INPUT FIELDS =================

        // 1. FULL NAME
        Label nameLabel = new Label("Full Name");
        nameLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter your full name");
        nameTextField.setPrefSize(310, 36);
        nameTextField.setMaxSize(310, 36);
        nameTextField.setFocusTraversable(false);
        nameTextField.setStyle(INPUT_NORMAL_STYLE);

        Label nameErrorLabel = new Label();
        nameErrorLabel.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10.5px; -fx-font-weight: bold;");
        nameErrorLabel.setWrapText(true);
        nameErrorLabel.setMaxWidth(310);
        nameErrorLabel.setVisible(false);
        nameErrorLabel.setManaged(false);

        nameTextField.focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (nameErrorLabel.isVisible()) return;
            nameTextField.setStyle(isFocused ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
        });
        nameTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (nameErrorLabel.isVisible()) {
                nameErrorLabel.setText("");
                nameErrorLabel.setVisible(false);
                nameErrorLabel.setManaged(false);
                nameTextField.setStyle(nameTextField.isFocused() ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
            }
        });

        // 2. EMAIL
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextField emailTextField = new TextField();
        emailTextField.setPromptText("Enter email (e.g., name@gmail.com)");
        emailTextField.setPrefSize(310, 36);
        emailTextField.setMaxSize(310, 36);
        emailTextField.setFocusTraversable(false);
        emailTextField.setStyle(INPUT_NORMAL_STYLE);

        Label emailErrorLabel = new Label();
        emailErrorLabel.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10.5px; -fx-font-weight: bold;");
        emailErrorLabel.setWrapText(true);
        emailErrorLabel.setMaxWidth(310);
        emailErrorLabel.setVisible(false);
        emailErrorLabel.setManaged(false);

        emailTextField.focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (emailErrorLabel.isVisible()) return;
            emailTextField.setStyle(isFocused ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
        });
        emailTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (emailErrorLabel.isVisible()) {
                emailErrorLabel.setText("");
                emailErrorLabel.setVisible(false);
                emailErrorLabel.setManaged(false);
                emailTextField.setStyle(emailTextField.isFocused() ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
            }
        });

        // 3. MOBILE NUMBER (10 DIGITS WITHOUT "STARTS WITH")
        Label mobileLabel = new Label("Mobile Number (10 Digits)");
        mobileLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextField mobileTextField = new TextField();
        mobileTextField.setPromptText("10-digit mobile number");
        mobileTextField.setPrefSize(310, 36);
        mobileTextField.setMaxSize(310, 36);
        mobileTextField.setFocusTraversable(false);
        mobileTextField.setStyle(INPUT_NORMAL_STYLE);

        Label mobileErrorLabel = new Label();
        mobileErrorLabel.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10.5px; -fx-font-weight: bold;");
        mobileErrorLabel.setWrapText(true);
        mobileErrorLabel.setMaxWidth(310);
        mobileErrorLabel.setVisible(false);
        mobileErrorLabel.setManaged(false);

        mobileTextField.focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (mobileErrorLabel.isVisible()) return;
            mobileTextField.setStyle(isFocused ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
        });
        mobileTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (mobileErrorLabel.isVisible()) {
                mobileErrorLabel.setText("");
                mobileErrorLabel.setVisible(false);
                mobileErrorLabel.setManaged(false);
                mobileTextField.setStyle(mobileTextField.isFocused() ? INPUT_FOCUS_STYLE : INPUT_NORMAL_STYLE);
            }
        });

        // 4. PASSWORD (8 CHARACTERS, 1 UPPERCASE, 1 DIGIT, 1 SYMBOL)
        Label passwordLabel = new Label("Create Password");
        passwordLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        PasswordEyeField passwordTextField = new PasswordEyeField("Create password");
        passwordTextField.setCustomPrefSize(310, 36);
        passwordTextField.setFocusTraversable(false);
        passwordTextField.getHiddenField().setStyle(PWD_NORMAL_STYLE);
        passwordTextField.getShownField().setStyle(PWD_NORMAL_STYLE);

        Label passwordErrorLabel = new Label();
        passwordErrorLabel.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10.5px; -fx-font-weight: bold;");
        passwordErrorLabel.setWrapText(true);
        passwordErrorLabel.setMaxWidth(310);
        passwordErrorLabel.setVisible(false);
        passwordErrorLabel.setManaged(false);

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

        // 5. CONFIRM PASSWORD
        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        PasswordEyeField confirmPasswordTextField = new PasswordEyeField("Confirm your password");
        confirmPasswordTextField.setCustomPrefSize(310, 36);
        confirmPasswordTextField.setFocusTraversable(false);
        confirmPasswordTextField.getHiddenField().setStyle(PWD_NORMAL_STYLE);
        confirmPasswordTextField.getShownField().setStyle(PWD_NORMAL_STYLE);

        Label confirmPasswordErrorLabel = new Label();
        confirmPasswordErrorLabel.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10.5px; -fx-font-weight: bold;");
        confirmPasswordErrorLabel.setWrapText(true);
        confirmPasswordErrorLabel.setMaxWidth(310);
        confirmPasswordErrorLabel.setVisible(false);
        confirmPasswordErrorLabel.setManaged(false);

        confirmPasswordTextField.getHiddenField().focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (confirmPasswordErrorLabel.isVisible()) return;
            confirmPasswordTextField.getHiddenField().setStyle(isFocused ? PWD_FOCUS_STYLE : PWD_NORMAL_STYLE);
        });
        confirmPasswordTextField.getShownField().focusedProperty().addListener((obs, oldV, isFocused) -> {
            if (confirmPasswordErrorLabel.isVisible()) return;
            confirmPasswordTextField.getShownField().setStyle(isFocused ? PWD_FOCUS_STYLE : PWD_NORMAL_STYLE);
        });
        confirmPasswordTextField.textProperty().addListener((obs, oldV, newV) -> {
            if (confirmPasswordErrorLabel.isVisible()) {
                confirmPasswordErrorLabel.setText("");
                confirmPasswordErrorLabel.setVisible(false);
                confirmPasswordErrorLabel.setManaged(false);
                boolean isFoc = confirmPasswordTextField.getHiddenField().isFocused() || confirmPasswordTextField.getShownField().isFocused();
                String st = isFoc ? PWD_FOCUS_STYLE : PWD_NORMAL_STYLE;
                confirmPasswordTextField.getHiddenField().setStyle(st);
                confirmPasswordTextField.getShownField().setStyle(st);
            }
        });

        // 6. ROLE SELECTION (3 ROLES: Farmer, Provider, Operator)
        Label roleLabel = new Label("Register Role");
        roleLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        RadioButton farmerRadio = new RadioButton("Farmer");
        RadioButton providerRadio = new RadioButton("Provider");
        RadioButton operatorRadio = new RadioButton("Operator");

        String radioStyle = "-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 500; -fx-text-fill: #374151; -fx-cursor: hand;";
        farmerRadio.setStyle(radioStyle);
        providerRadio.setStyle(radioStyle);
        operatorRadio.setStyle(radioStyle);

        farmerRadio.setFocusTraversable(false);
        providerRadio.setFocusTraversable(false);
        operatorRadio.setFocusTraversable(false);

        ToggleGroup roleGroup = new ToggleGroup();
        farmerRadio.setToggleGroup(roleGroup);
        providerRadio.setToggleGroup(roleGroup);
        operatorRadio.setToggleGroup(roleGroup);

        HBox roleHBox = new HBox(16, farmerRadio, providerRadio, operatorRadio);
        roleHBox.setAlignment(Pos.CENTER_LEFT);
        roleHBox.setPadding(new Insets(2, 0, 0, 0));

        // Group inputs
        VBox nameBox = new VBox(2, nameLabel, nameTextField, nameErrorLabel);
        VBox emailBox = new VBox(2, emailLabel, emailTextField, emailErrorLabel);
        VBox mobileBox = new VBox(2, mobileLabel, mobileTextField, mobileErrorLabel);
        VBox passBox = new VBox(2, passwordLabel, passwordTextField, passwordErrorLabel);
        VBox confirmPassBox = new VBox(2, confirmPasswordLabel, confirmPasswordTextField, confirmPasswordErrorLabel);
        VBox roleBox = new VBox(2, roleLabel, roleHBox);

        VBox secondaryVBox = new VBox(7, nameBox, emailBox, mobileBox, passBox, confirmPassBox, roleBox);
        secondaryVBox.setMaxWidth(310);
        secondaryVBox.setAlignment(Pos.CENTER_LEFT);

        // ================= REGISTER BUTTON =================
        Button registerButton = new Button("Create Account");
        String buttonNormalStyle = "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-background-radius: 10px; -fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.35), 8, 0, 0, 3);";
        String buttonHoverStyle = "-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-background-radius: 10px; -fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.45), 10, 0, 0, 4);";

        registerButton.setStyle(buttonNormalStyle);
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(buttonHoverStyle));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(buttonNormalStyle));

        registerButton.setPrefHeight(40);
        registerButton.setPrefWidth(310);
        registerButton.setMaxWidth(310);
        registerButton.setFocusTraversable(false);

        // ================= FULL REGISTRATION VALIDATION & SUBMISSION =================
        registerButton.setOnAction(event -> {
            String name = nameTextField.getText() != null ? nameTextField.getText().trim() : "";
            String email = emailTextField.getText() != null ? emailTextField.getText().trim() : "";
            String mobile = mobileTextField.getText() != null ? mobileTextField.getText().trim() : "";
            String password = passwordTextField.getText() != null ? passwordTextField.getText() : "";
            String confirmPassword = confirmPasswordTextField.getText() != null ? confirmPasswordTextField.getText() : "";

            boolean isValid = true;

            // 1. Validate Full Name
            if (name.isEmpty()) {
                nameErrorLabel.setText("Full Name is required.");
                nameErrorLabel.setVisible(true);
                nameErrorLabel.setManaged(true);
                nameTextField.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else if (name.length() < 2) {
                nameErrorLabel.setText("Name must be at least 2 characters.");
                nameErrorLabel.setVisible(true);
                nameErrorLabel.setManaged(true);
                nameTextField.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else {
                nameErrorLabel.setText("");
                nameErrorLabel.setVisible(false);
                nameErrorLabel.setManaged(false);
                nameTextField.setStyle(INPUT_NORMAL_STYLE);
            }

            // 2. Validate Email
            if (email.isEmpty()) {
                emailErrorLabel.setText("Email Address is required.");
                emailErrorLabel.setVisible(true);
                emailErrorLabel.setManaged(true);
                emailTextField.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                emailErrorLabel.setText("Enter a valid email address (e.g. name@domain.com).");
                emailErrorLabel.setVisible(true);
                emailErrorLabel.setManaged(true);
                emailTextField.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else {
                emailErrorLabel.setText("");
                emailErrorLabel.setVisible(false);
                emailErrorLabel.setManaged(false);
                emailTextField.setStyle(INPUT_NORMAL_STYLE);
            }

            // 3. Validate Mobile Number (10-digit Indian Mobile)
            if (mobile.isEmpty()) {
                mobileErrorLabel.setText("Mobile Number is required.");
                mobileErrorLabel.setVisible(true);
                mobileErrorLabel.setManaged(true);
                mobileTextField.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else if (!mobile.matches("^(?:\\+91|0)?[6-9]\\d{9}$")) {
                mobileErrorLabel.setText("Enter a valid 10-digit Indian mobile number.");
                mobileErrorLabel.setVisible(true);
                mobileErrorLabel.setManaged(true);
                mobileTextField.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else {
                mobileErrorLabel.setText("");
                mobileErrorLabel.setVisible(false);
                mobileErrorLabel.setManaged(false);
                mobileTextField.setStyle(INPUT_NORMAL_STYLE);
            }

            // 4. Validate Password (8 characters, 1 uppercase, 1 digit, 1 symbol)
            if (password.isEmpty()) {
                passwordErrorLabel.setText("Password is required.");
                passwordErrorLabel.setVisible(true);
                passwordErrorLabel.setManaged(true);
                passwordTextField.getHiddenField().setStyle(PWD_ERROR_STYLE);
                passwordTextField.getShownField().setStyle(PWD_ERROR_STYLE);
                isValid = false;
            } else if (!password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
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

            // 5. Validate Confirm Password
            if (confirmPassword.isEmpty()) {
                confirmPasswordErrorLabel.setText("Please confirm your password.");
                confirmPasswordErrorLabel.setVisible(true);
                confirmPasswordErrorLabel.setManaged(true);
                confirmPasswordTextField.getHiddenField().setStyle(PWD_ERROR_STYLE);
                confirmPasswordTextField.getShownField().setStyle(PWD_ERROR_STYLE);
                isValid = false;
            } else if (!confirmPassword.equals(password)) {
                confirmPasswordErrorLabel.setText("Passwords do not match.");
                confirmPasswordErrorLabel.setVisible(true);
                confirmPasswordErrorLabel.setManaged(true);
                confirmPasswordTextField.getHiddenField().setStyle(PWD_ERROR_STYLE);
                confirmPasswordTextField.getShownField().setStyle(PWD_ERROR_STYLE);
                isValid = false;
            } else {
                confirmPasswordErrorLabel.setText("");
                confirmPasswordErrorLabel.setVisible(false);
                confirmPasswordErrorLabel.setManaged(false);
                confirmPasswordTextField.getHiddenField().setStyle(PWD_NORMAL_STYLE);
                confirmPasswordTextField.getShownField().setStyle(PWD_NORMAL_STYLE);
            }

            if (!isValid) {
                return;
            }

            RadioButton valRadioButton = (RadioButton) roleGroup.getSelectedToggle();
            if (valRadioButton == null) {
                farmerRadio.setSelected(true);
                valRadioButton = farmerRadio;
            }
            String redirect = valRadioButton.getText();

            AuthenticateController objController = new AuthenticateController();
            if (objController.signUp(email, password)) {
                
                objController.addUser(name, email, mobile, password, redirect);
                if ("Provider".equalsIgnoreCase(redirect)) {
                    com.desgin.view.provider.ProviderDashboard obj = new com.desgin.view.provider.ProviderDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getProviderDashboardScene(backToLogin));
                } else if ("Operator".equalsIgnoreCase(redirect)) {
                    com.desgin.view.operator.OperatorProfileStore.setProfile(name, mobile, null, null);
                    com.desgin.view.operator.OperatorDashboard obj = new com.desgin.view.operator.OperatorDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getOperatorDashboardScene(backToLogin));
                } else if ("Farmer".equalsIgnoreCase(redirect)) {
                    FarmerProfileStore.setCredentials(name, email, mobile);
                    com.desgin.view.farmer.ashutosh.profile.ProfileManagement.updateHeaderGreeting();
                    FarmerDashboard obj = new FarmerDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getfarmerDashboardScene(backToLogin));
                }
            } else {
                emailErrorLabel.setText("Registration failed: Email may already exist or network error.");
                emailErrorLabel.setVisible(true);
                emailErrorLabel.setManaged(true);
                emailTextField.setStyle(INPUT_ERROR_STYLE);
            }
        });

        // ================= LOGIN TEXT (BRIGHT & CLICKABLE) =================
        Text alreadyAccount = new Text("Already have an account? ");
        alreadyAccount.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: 500; -fx-fill: #374151;");

        Text loginNow = new Text("Login here");
        loginNow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #2D6A4F; -fx-underline: true; -fx-cursor: hand;");

        HBox loginHBox = new HBox(4, alreadyAccount, loginNow);
        loginHBox.setAlignment(Pos.CENTER);
        loginHBox.setStyle("-fx-cursor: hand; -fx-padding: 3px 0 0 0;");

        loginHBox.setOnMouseEntered(e -> loginNow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332; -fx-underline: true; -fx-cursor: hand;"));
        loginHBox.setOnMouseExited(e -> loginNow.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #2D6A4F; -fx-underline: true; -fx-cursor: hand;"));

        loginHBox.setOnMouseClicked(event -> {
            if (backToLogin != null) {
                backToLogin.run();
            }
        });

        HBox div = new HBox();
        div.setPrefHeight(1);
        div.setMaxHeight(1);
        div.setPrefWidth(290);
        div.setMaxWidth(290);
        div.setStyle("-fx-background-color: rgba(45, 106, 79, 0.2);");

        Label adminRegPill = new Label("🔒 Admin Portal Registration (Max 5 Admins)");
        String adminNormalStyle = "-fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #2D6A4F; -fx-background-color: #E8F5E9; -fx-background-radius: 16px; -fx-padding: 4px 12px; -fx-cursor: hand; -fx-border-color: rgba(45, 106, 79, 0.3); -fx-border-radius: 16px; -fx-border-width: 1px; -fx-font-family: 'Poppins';";
        String adminHoverStyle = "-fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #2D6A4F; -fx-background-radius: 16px; -fx-padding: 4px 12px; -fx-cursor: hand; -fx-border-color: #2D6A4F; -fx-border-radius: 16px; -fx-border-width: 1px; -fx-font-family: 'Poppins';";

        adminRegPill.setStyle(adminNormalStyle);
        adminRegPill.setOnMouseEntered(e -> adminRegPill.setStyle(adminHoverStyle));
        adminRegPill.setOnMouseExited(e -> adminRegPill.setStyle(adminNormalStyle));
        adminRegPill.setOnMouseClicked(e -> {
            Authentication auth = new Authentication();
            WelcomePage.welcomePageStage.setScene(auth.getAuthenticationScene());
        });

        VBox adminBox = new VBox(6, div, adminRegPill);
        adminBox.setAlignment(Pos.CENTER);

        // ================= MAIN REGISTRATION CARD (TRANSLUCENT) =================
        VBox registerCard = new VBox(9, primaryVBox, secondaryVBox, registerButton, loginHBox, adminBox);
        registerCard.setAlignment(Pos.CENTER);
        registerCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.58);" +
            "-fx-background-radius: 22px;" +
            "-fx-border-color: rgba(255, 255, 255, 0.60);" +
            "-fx-border-radius: 22px;" +
            "-fx-border-width: 1.5px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.20), 24, 0.12, 0, 8);" +
            "-fx-padding: 16px 30px 16px 30px;"
        );

        registerCard.setPrefWidth(430);
        registerCard.setMaxWidth(430);
        registerCard.setMaxHeight(Region.USE_PREF_SIZE);

        borderPane.setCenter(registerCard);
        borderPane.setStyle("-fx-background-color: transparent;");

        // ================= BACKGROUND =================
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

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, borderPane);

        backgroundImageView.fitWidthProperty().bind(root.widthProperty());
        backgroundImageView.fitHeightProperty().bind(root.heightProperty());

        registerScene = new Scene(root);
        return registerScene;
    }
}