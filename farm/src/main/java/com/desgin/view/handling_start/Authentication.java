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

    public Scene getAuthenticationScene() {
        BorderPane borderPane = new BorderPane();

        // ------------------ MAIN CARD IN CENTER ------------------
        VBox userCard = createStandardUserLoginCard(borderPane);
        borderPane.setCenter(userCard);
        BorderPane.setAlignment(userCard, Pos.CENTER);
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
    // 1. STANDARD USER LOGIN CARD (FARMER, PROVIDER, OPERATOR)
    // ============================================================
    private VBox createStandardUserLoginCard(BorderPane borderPane) {
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

        emailErrorLabel.setPadding(new Insets(3, 0, 2, 0));

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
        passwordErrorLabel.setPadding(new Insets(3, 0, 2, 0));
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

        // ------------------ ROLES (4 ROLES: Farmer, Provider, Operator, Admin) ------------------
        Label roleLabel = new Label("Login As");
        roleLabel.setStyle("-fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins';");

        RadioButton farmerRadio = new RadioButton("Farmer");
        RadioButton providerRadio = new RadioButton("Provider");
        RadioButton operatorRadio = new RadioButton("Operator");
        //RadioButton adminRadio = new RadioButton("Admin");

        String radioStyle = "-fx-font-size: 12.5px; -fx-font-weight: 500; -fx-text-fill: #374151; -fx-cursor: hand; -fx-font-family: 'Poppins';";
        farmerRadio.setStyle(radioStyle);
        providerRadio.setStyle(radioStyle);
        operatorRadio.setStyle(radioStyle);
        //adminRadio.setStyle(radioStyle);

        farmerRadio.setFocusTraversable(false);
        providerRadio.setFocusTraversable(false);
        operatorRadio.setFocusTraversable(false);
        //adminRadio.setFocusTraversable(false);

        ToggleGroup loginRoleGroup = new ToggleGroup();
        farmerRadio.setToggleGroup(loginRoleGroup);
        providerRadio.setToggleGroup(loginRoleGroup);
        operatorRadio.setToggleGroup(loginRoleGroup);
        //adminRadio.setToggleGroup(loginRoleGroup);
        farmerRadio.setSelected(true);

        //HBox roleHBox = new HBox(12, farmerRadio, providerRadio, operatorRadio, adminRadio);
        HBox roleHBox = new HBox(22, farmerRadio, providerRadio, operatorRadio);
        roleHBox.setAlignment(Pos.CENTER_LEFT);
        roleHBox.setPadding(new Insets(2, 0, 0, 0));

        VBox emailBox = new VBox(4, label1, mailAndPhoneTextField, emailErrorLabel);
        VBox passwordBox = new VBox(4, label2, passwordTextField, passwordErrorLabel);
        VBox roleBox = new VBox(6, roleLabel, roleHBox);

        VBox secondaryVBox = new VBox(14, emailBox, passwordBox, roleBox);
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

            if ("Admin".equalsIgnoreCase(selectedRole)) {
                AuthenticateController.AdminAuthResult result = objController.authenticateAndAuthorizeAdmin(inputUser, inputPassword);
                if (result != null && result.isSuccess()) {
                    if (result.getUser() != null) {
                        com.desgin.view.admin.AdminProfileStore.setAdminProfile(
                            result.getUser().getName(),
                            result.getUser().getMail(),
                            result.getUser().getNum(),
                            "Master Admin",
                            result.getUser().getProfilePic()
                        );
                    }
                    com.desgin.view.admin.AdminProfileManagement.updateHeaderGreeting();
                    String adminKey = (result.getUser() != null && result.getUser().getMail() != null) ? result.getUser().getMail() : inputUser;
                    com.desgin.service.UserStatusWatcher.startWatching(adminKey, "Admin", backToLogin);
                    com.desgin.view.admin.AdminDashboard obj = new com.desgin.view.admin.AdminDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getAdminDashboardScene(backToLogin));
                } else {
                    String msg = result != null ? result.getMessage() : "Invalid Admin credentials.";
                    emailErrorLabel.setText(msg);
                    emailErrorLabel.setVisible(true);
                    emailErrorLabel.setManaged(true);
                    mailAndPhoneTextField.setStyle(INPUT_ERROR_STYLE);
                }
                return;
            }

            AuthenticateModel userDoc = objController.getUser(inputUser, selectedRole);
            if (userDoc == null) {
                emailErrorLabel.setText("No registered account found for this " + selectedRole + ".");
                emailErrorLabel.setVisible(true);
                emailErrorLabel.setManaged(true);
                mailAndPhoneTextField.setStyle(INPUT_ERROR_STYLE);
                return;
            }

            // Verify account is not suspended
            if ("SUSPENDED".equalsIgnoreCase(userDoc.getStatus())) {
                String errorMsg = "🚫 Your account has been suspended by the platform administrator. Access denied.";
                emailErrorLabel.setText(errorMsg);
                emailErrorLabel.setVisible(true);
                emailErrorLabel.setManaged(true);
                mailAndPhoneTextField.setStyle(INPUT_ERROR_STYLE);

                try {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Account Suspended");
                    alert.setHeaderText("Access Denied - Account Suspended");
                    alert.setContentText("Your " + selectedRole + " account (" + inputUser + ") has been suspended by the platform administrator.\n\nYou cannot access your portal while under suspension.");
                    alert.show();
                } catch (Exception ignored) {}
                return;
            }

            // Verify password strictly against database (Firestore updated password is source of truth)
            boolean pwdValid = false;
            String storedPwd = userDoc.getPassword();
            if (storedPwd != null && !storedPwd.trim().isEmpty()) {
                pwdValid = storedPwd.trim().equals(inputPassword);
            } else {
                String emailToAuth = (userDoc.getMail() != null && !userDoc.getMail().isEmpty()) ? userDoc.getMail() : inputUser;
                pwdValid = objController.signIn(emailToAuth, inputPassword);
            }

            if (!pwdValid) {
                passwordErrorLabel.setText("Incorrect password. Please enter your valid updated password.");
                passwordErrorLabel.setVisible(true);
                passwordErrorLabel.setManaged(true);
                passwordTextField.setStyle(INPUT_ERROR_STYLE);
                passwordTextField.clear();
                return;
            }

            String userKey = (userDoc.getMail() != null && !userDoc.getMail().isEmpty()) ? userDoc.getMail() : inputUser;
            com.desgin.service.UserStatusWatcher.startWatching(userKey, selectedRole, backToLogin);

            if ("Provider".equalsIgnoreCase(selectedRole)) {
                com.desgin.view.provider.ProviderProfileStore.setFullProfile(
                    userDoc.getName(), userDoc.getMail(), userDoc.getNum(),
                    userDoc.getTown(), userDoc.getDistrict(), userDoc.getState(), userDoc.getPincode(),
                    userDoc.getProfilePic()
                );
                com.desgin.view.provider.ProviderProfileStore.setBankDetails(
                    userDoc.getAccountHolder(), userDoc.getBankName(), userDoc.getAccountNumber(),
                    userDoc.getIfsc(), userDoc.getUpiId()
                );
                com.desgin.view.provider.ProviderProfileManagement.updateHeaderGreeting();
                ProviderDashboard obj = new ProviderDashboard();
                WelcomePage.welcomePageStage.setScene(obj.getProviderDashboardScene(backToLogin));

            } else if ("Operator".equalsIgnoreCase(selectedRole)) {
                com.desgin.view.operator.OperatorProfileStore.setProfile(userDoc.getName(), userDoc.getNum(), null, null);
                com.desgin.view.operator.OperatorProfileStore.email = (userDoc.getMail() != null && !userDoc.getMail().isEmpty()) ? userDoc.getMail() : inputUser;
                if (userDoc.getProfilePic() != null && !userDoc.getProfilePic().isEmpty()) {
                    com.desgin.view.operator.OperatorProfileStore.profilePic = userDoc.getProfilePic();
                }
                if (userDoc.getDrivingExperience() != null && !userDoc.getDrivingExperience().isEmpty()) {
                    com.desgin.view.operator.OperatorProfileStore.drivingExperience = userDoc.getDrivingExperience();
                }
                if (userDoc.getEquipmentProfession() != null && !userDoc.getEquipmentProfession().isEmpty()) {
                    com.desgin.view.operator.OperatorProfileStore.equipmentProfession = userDoc.getEquipmentProfession();
                }
                if (userDoc.getLicenseImage() != null && !userDoc.getLicenseImage().isEmpty()) {
                    com.desgin.view.operator.OperatorProfileStore.licenseImage = userDoc.getLicenseImage();
                }
                if (userDoc.getTown() != null && !userDoc.getTown().isEmpty()) {
                    com.desgin.view.operator.OperatorProfileStore.zone = userDoc.getTown() + (userDoc.getDistrict() != null ? (" / " + userDoc.getDistrict()) : "");
                }
                com.desgin.view.operator.OperatorProfileManagement.updateHeaderGreeting();
                OperatorDashboard obj = new OperatorDashboard();
                WelcomePage.welcomePageStage.setScene(obj.getOperatorDashboardScene(backToLogin));

            } else if ("Farmer".equalsIgnoreCase(selectedRole)) {
                FarmerProfileStore.setCredentials(userDoc.getName(), userDoc.getMail(), userDoc.getNum());
                if (userDoc.getTown() != null && !userDoc.getTown().isEmpty()) {
                    FarmerProfileStore.setLocation(userDoc.getTown(), userDoc.getDistrict(), userDoc.getState(), userDoc.getPincode());
                }
                if (userDoc.getProfilePic() != null) {
                    FarmerProfileStore.setProfilePic(userDoc.getProfilePic());
                }
                com.desgin.view.farmer.ashutosh.profile.ProfileManagement.updateHeaderGreeting();
                FarmerDashboard obj = new FarmerDashboard();
                WelcomePage.welcomePageStage.setScene(obj.getfarmerDashboardScene(backToLogin));
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
            String initialUser = mailAndPhoneTextField.getText() != null ? mailAndPhoneTextField.getText().trim() : "";
            String initialPass = passwordTextField.getText() != null ? passwordTextField.getText() : "";
            VBox adminCard = createAdminLoginCard(borderPane, initialUser, initialPass);
            borderPane.setCenter(adminCard);
            BorderPane.setAlignment(adminCard, Pos.CENTER);
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

        loginVBox.setPrefWidth(390);
        loginVBox.setMaxWidth(390);
        loginVBox.setMaxHeight(Region.USE_PREF_SIZE);

        return loginVBox;
    }

    // ============================================================
    // 2. ADMIN LOGIN CARD (IDENTICAL FROSTED GLASS UI DESIGN)
    // ============================================================
    private VBox createAdminLoginCard(BorderPane borderPane, String initialUser, String initialPass) {
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

        Text loginToFarmEquipText = new Text("Login to FarmEquip Admin Console");
        loginToFarmEquipText.setStyle("-fx-font-size: 13.5px; -fx-font-weight: 600; -fx-fill: #4B5563; -fx-font-family: 'Poppins';");

        VBox primaryVBox = new VBox(5, loginPageImageView, wlcBack, loginToFarmEquipText);
        primaryVBox.setAlignment(Pos.CENTER);

        // ------------------ INPUT FIELDS & LABELS ------------------
        Label label1 = new Label("Email or Mobile No.");
        label1.setStyle("-fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins';");

        TextField mailAndPhoneTextField = new TextField(initialUser != null ? initialUser : "");
        mailAndPhoneTextField.setPromptText("Enter admin email");
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

        PasswordEyeField passwordTextField = new PasswordEyeField("Enter admin password");
        passwordTextField.setCustomPrefSize(290, 40);
        if (initialPass != null && !initialPass.isEmpty()) {
            passwordTextField.setText(initialPass);
        }
        passwordTextField.setFocusTraversable(false);
        passwordTextField.getHiddenField().setStyle(PWD_NORMAL_STYLE);
        passwordTextField.getShownField().setStyle(PWD_NORMAL_STYLE);

        Label passwordErrorLabel = new Label();
        passwordErrorLabel.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Poppins';");
        passwordErrorLabel.setWrapText(true);
        passwordErrorLabel.setMaxWidth(290);
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

        // ------------------ ADMIN QUOTA BADGE ------------------
        AuthenticateController controller = new AuthenticateController();
        int adminCount = controller.getAdminCount();

        Label roleLabel = new Label("Portal Access");
        roleLabel.setStyle("-fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins';");

        Label quotaBadge = new Label("👑 Authorized Admin (" + adminCount + "/5 Seats Allocated)");
        quotaBadge.setStyle("-fx-background-color: " + (adminCount >= 5 ? "#FEF2F2" : "#E8F5E9") + "; -fx-text-fill: " + (adminCount >= 5 ? "#DC2626" : "#15803D") + "; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3px 8px; -fx-background-radius: 6px;");

        HBox adminRoleBox = new HBox(roleLabel, quotaBadge);
        adminRoleBox.setAlignment(Pos.CENTER_LEFT);
        adminRoleBox.setSpacing(8);

        VBox emailBox = new VBox(3, label1, mailAndPhoneTextField, emailErrorLabel);
        VBox passwordBox = new VBox(3, label2, passwordTextField, passwordErrorLabel);
        VBox roleBox = new VBox(3, adminRoleBox);

        VBox secondaryVBox = new VBox(10, emailBox, passwordBox, roleBox);
        secondaryVBox.setMaxWidth(290);
        secondaryVBox.setAlignment(Pos.CENTER_LEFT);

        // ------------------ LOGIN BUTTON ------------------
        Button loginButton = new Button("Login as Admin");
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

        loginButton.setOnAction(e -> {
            boolean isValid = validateCredentials(mailAndPhoneTextField, emailErrorLabel, passwordTextField, passwordErrorLabel);
            if (!isValid) {
                return;
            }

            String inputUser = mailAndPhoneTextField.getText().trim();
            String inputPassword = passwordTextField.getText();

            loginButton.setText("Verifying...");
            loginButton.setDisable(true);

            javafx.concurrent.Task<AuthenticateController.AdminAuthResult> authTask = new javafx.concurrent.Task<>() {
                @Override
                protected AuthenticateController.AdminAuthResult call() {
                    return controller.authenticateAndAuthorizeAdmin(inputUser, inputPassword);
                }
            };

            authTask.setOnSucceeded(ev -> {
                loginButton.setText("Login as Admin");
                loginButton.setDisable(false);
                AuthenticateController.AdminAuthResult result = authTask.getValue();

                if (result != null && result.isSuccess()) {
                    if (result.getUser() != null) {
                        com.desgin.view.admin.AdminProfileStore.setAdminProfile(
                            result.getUser().getName(),
                            result.getUser().getMail(),
                            result.getUser().getNum(),
                            "Master Admin",
                            result.getUser().getProfilePic()
                        );
                    }
                    com.desgin.view.admin.AdminProfileManagement.updateHeaderGreeting();

                    Runnable backToLogin = () -> backtologin();
                    String adminKey = (result.getUser() != null && result.getUser().getMail() != null) ? result.getUser().getMail() : inputUser;
                    com.desgin.service.UserStatusWatcher.startWatching(adminKey, "Admin", backToLogin);
                    com.desgin.view.admin.AdminDashboard obj = new com.desgin.view.admin.AdminDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getAdminDashboardScene(backToLogin));
                } else {
                    String msg = result != null ? result.getMessage() : "Authentication or authorization failed.";
                    emailErrorLabel.setText(msg);
                    emailErrorLabel.setVisible(true);
                    emailErrorLabel.setManaged(true);
                    mailAndPhoneTextField.setStyle(INPUT_ERROR_STYLE);
                }
            });

            authTask.setOnFailed(ev -> {
                loginButton.setText("Login as Admin");
                loginButton.setDisable(false);
                emailErrorLabel.setText("System error during admin verification. Please try again.");
                emailErrorLabel.setVisible(true);
                emailErrorLabel.setManaged(true);
                mailAndPhoneTextField.setStyle(INPUT_ERROR_STYLE);
            });

            new Thread(authTask).start();
        });

        // ------------------ REGISTER AS ADMIN LINK ------------------
        Text noAdminAccText = new Text("Need an admin account? ");
        noAdminAccText.setStyle("-fx-font-size: 13px; -fx-fill: #374151; -fx-font-weight: 500; -fx-font-family: 'Poppins';");

        Text registerAdminText = new Text("Register as Admin");
        registerAdminText.setStyle("-fx-font-size: 13.5px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';");

        HBox registerAdminHBox = new HBox(4, noAdminAccText, registerAdminText);
        registerAdminHBox.setAlignment(Pos.CENTER);
        registerAdminHBox.setStyle("-fx-cursor: hand; -fx-padding: 2px 0 0 0;");

        registerAdminHBox.setOnMouseEntered(e -> registerAdminText.setStyle("-fx-font-size: 13.5px; -fx-fill: #1B4332; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';"));
        registerAdminHBox.setOnMouseExited(e -> registerAdminText.setStyle("-fx-font-size: 13.5px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';"));

        registerAdminHBox.setOnMouseClicked(event -> {
            VBox regCard = createAdminRegisterCard(borderPane, mailAndPhoneTextField.getText());
            borderPane.setCenter(regCard);
            BorderPane.setAlignment(regCard, Pos.CENTER);
        });

        // ------------------ SWITCH TO USER LOGIN LINK ------------------
        Text notAdminText = new Text("Not an administrator? ");
        notAdminText.setStyle("-fx-font-size: 13px; -fx-fill: #374151; -fx-font-weight: 500; -fx-font-family: 'Poppins';");

        Text backToUserText = new Text("User Login");
        backToUserText.setStyle("-fx-font-size: 13.5px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';");

        HBox backToUserHBox = new HBox(4, notAdminText, backToUserText);
        backToUserHBox.setAlignment(Pos.CENTER);
        backToUserHBox.setStyle("-fx-cursor: hand; -fx-padding: 2px 0 0 0;");

        backToUserHBox.setOnMouseEntered(e -> backToUserText.setStyle("-fx-font-size: 13.5px; -fx-fill: #1B4332; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';"));
        backToUserHBox.setOnMouseExited(e -> backToUserText.setStyle("-fx-font-size: 13.5px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand; -fx-font-family: 'Poppins';"));

        backToUserHBox.setOnMouseClicked(event -> {
            VBox userCard = createStandardUserLoginCard(borderPane);
            borderPane.setCenter(userCard);
            BorderPane.setAlignment(userCard, Pos.CENTER);
        });

        // ------------------ USER LOGIN FOOTER PILL ------------------
        HBox divider = new HBox();
        divider.setPrefHeight(1);
        divider.setMaxHeight(1);
        divider.setPrefWidth(290);
        divider.setMaxWidth(290);
        divider.setStyle("-fx-background-color: rgba(45, 106, 79, 0.2);");

        Label userPortalPill = new Label("👨‍🌾 Farmer / Provider Portal");
        String adminNormalStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2D6A4F; -fx-background-color: #E8F5E9; -fx-background-radius: 16px; -fx-padding: 5px 14px; -fx-cursor: hand; -fx-border-color: rgba(45, 106, 79, 0.3); -fx-border-radius: 16px; -fx-border-width: 1px; -fx-font-family: 'Poppins';";
        String adminHoverStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #2D6A4F; -fx-background-radius: 16px; -fx-padding: 5px 14px; -fx-cursor: hand; -fx-border-color: #2D6A4F; -fx-border-radius: 16px; -fx-border-width: 1px; -fx-font-family: 'Poppins';";

        userPortalPill.setStyle(adminNormalStyle);
        userPortalPill.setOnMouseEntered(e -> userPortalPill.setStyle(adminHoverStyle));
        userPortalPill.setOnMouseExited(e -> userPortalPill.setStyle(adminNormalStyle));
        userPortalPill.setOnMouseClicked(e -> {
            VBox userCard = createStandardUserLoginCard(borderPane);
            borderPane.setCenter(userCard);
            BorderPane.setAlignment(userCard, Pos.CENTER);
        });

        VBox userPortalBox = new VBox(8, divider, userPortalPill);
        userPortalBox.setAlignment(Pos.CENTER);

        // ------------------ MAIN ADMIN LOGIN CARD (TRANSLUCENT) ------------------
        VBox adminVBox = new VBox(13, primaryVBox, secondaryVBox, loginButton, registerAdminHBox, backToUserHBox, userPortalBox);
        adminVBox.setAlignment(Pos.CENTER);
        adminVBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.58);" +
            "-fx-background-radius: 22px;" +
            "-fx-border-color: rgba(255, 255, 255, 0.60);" +
            "-fx-border-radius: 22px;" +
            "-fx-border-width: 1.5px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.20), 24, 0.12, 0, 8);" +
            "-fx-padding: 22px 34px 20px 34px;"
        );

        adminVBox.setPrefWidth(390);
        adminVBox.setMaxWidth(390);
        adminVBox.setMaxHeight(Region.USE_PREF_SIZE);

        return adminVBox;
    }

    // ============================================================
    // 3. ADMIN REGISTRATION CARD (IDENTICAL FROSTED GLASS UI DESIGN)
    // ============================================================
    private VBox createAdminRegisterCard(BorderPane borderPane, String initialEmail) {
        // Logo & Header
        Image img;
        try {
            img = new Image("file:farm/src/main/resources/assets/Images/logo.png");
            if (img.isError()) {
                img = new Image(getClass().getResourceAsStream("/assets/Images/logo.png"));
            }
        } catch (Exception e) {
            img = new Image("file:farm/src/main/resources/assets/Images/logo.jpeg");
        }

        ImageView regLogo = new ImageView(img);
        regLogo.setFitWidth(46);
        regLogo.setFitHeight(46);
        regLogo.setPreserveRatio(true);
        regLogo.setSmooth(true);

        Text title = new Text("Register Administrator");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Create Platform Administrator Account");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 500; -fx-fill: #4B5563;");

        VBox headerVBox = new VBox(3, regLogo, title, subtitle);
        headerVBox.setAlignment(Pos.CENTER);

        // 1. Quota Pill
        AuthenticateController controller = new AuthenticateController();
        int adminCount = controller.getAdminCount();
        Label quotaPill = new Label(adminCount >= 5 
            ? "🚫 Quota Full (5/5 Admin Seats Reached)" 
            : "👑 Admin Quota: " + adminCount + " / 5 Seats Allocated (" + (5 - adminCount) + " Remaining)");
        quotaPill.setStyle("-fx-background-color: " + (adminCount >= 5 ? "#FEF2F2" : "#E8F5E9") + "; -fx-text-fill: " + (adminCount >= 5 ? "#DC2626" : "#15803D") + "; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3px 8px; -fx-background-radius: 6px;");

        // 2. Full Name
        Label nameLbl = new Label("Full Name");
        nameLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextField nameF = new TextField();
        nameF.setPromptText("Enter admin full name");
        nameF.setPrefSize(300, 35);
        nameF.setMaxSize(300, 35);
        nameF.setFocusTraversable(false);
        nameF.setStyle(INPUT_NORMAL_STYLE);

        Label nameErr = new Label();
        nameErr.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-font-weight: bold;");
        nameErr.setVisible(false);
        nameErr.setManaged(false);

        nameF.textProperty().addListener((obs, oldV, newV) -> {
            if (nameErr.isVisible()) {
                nameErr.setText("");
                nameErr.setVisible(false);
                nameErr.setManaged(false);
                nameF.setStyle(INPUT_NORMAL_STYLE);
            }
        });

        // 3. Email
        Label emailLbl = new Label("Admin Email");
        emailLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextField emailF = new TextField(initialEmail != null ? initialEmail : "");
        emailF.setPromptText("admin@farmequip.com");
        emailF.setPrefSize(300, 35);
        emailF.setMaxSize(300, 35);
        emailF.setFocusTraversable(false);
        emailF.setStyle(INPUT_NORMAL_STYLE);

        Label emailErr = new Label();
        emailErr.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-font-weight: bold;");
        emailErr.setVisible(false);
        emailErr.setManaged(false);

        emailF.textProperty().addListener((obs, oldV, newV) -> {
            if (emailErr.isVisible()) {
                emailErr.setText("");
                emailErr.setVisible(false);
                emailErr.setManaged(false);
                emailF.setStyle(INPUT_NORMAL_STYLE);
            }
        });

        // 4. Mobile Number
        Label mobLbl = new Label("Mobile Number (10 Digits)");
        mobLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        TextField mobF = new TextField();
        mobF.setPromptText("10-digit mobile number");
        mobF.setPrefSize(300, 35);
        mobF.setMaxSize(300, 35);
        mobF.setFocusTraversable(false);
        mobF.setStyle(INPUT_NORMAL_STYLE);

        Label mobErr = new Label();
        mobErr.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-font-weight: bold;");
        mobErr.setVisible(false);
        mobErr.setManaged(false);

        mobF.textProperty().addListener((obs, oldV, newV) -> {
            if (mobErr.isVisible()) {
                mobErr.setText("");
                mobErr.setVisible(false);
                mobErr.setManaged(false);
                mobF.setStyle(INPUT_NORMAL_STYLE);
            }
        });

        // 5. Password
        Label passLbl = new Label("Password");
        passLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        PasswordEyeField passF = new PasswordEyeField("Create password");
        passF.setCustomPrefSize(300, 35);
        passF.setFocusTraversable(false);
        passF.getHiddenField().setStyle(PWD_NORMAL_STYLE);
        passF.getShownField().setStyle(PWD_NORMAL_STYLE);

        Label passErr = new Label();
        passErr.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-font-weight: bold;");
        passErr.setVisible(false);
        passErr.setManaged(false);

        passF.textProperty().addListener((obs, oldV, newV) -> {
            if (passErr.isVisible()) {
                passErr.setText("");
                passErr.setVisible(false);
                passErr.setManaged(false);
                passF.getHiddenField().setStyle(PWD_NORMAL_STYLE);
                passF.getShownField().setStyle(PWD_NORMAL_STYLE);
            }
        });

        // 6. Confirm Password
        Label confirmLbl = new Label("Confirm Password");
        confirmLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        PasswordEyeField confirmF = new PasswordEyeField("Confirm password");
        confirmF.setCustomPrefSize(300, 35);
        confirmF.setFocusTraversable(false);
        confirmF.getHiddenField().setStyle(PWD_NORMAL_STYLE);
        confirmF.getShownField().setStyle(PWD_NORMAL_STYLE);

        Label confirmErr = new Label();
        confirmErr.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #DC2626; -fx-font-size: 10px; -fx-font-weight: bold;");
        confirmErr.setVisible(false);
        confirmErr.setManaged(false);

        confirmF.textProperty().addListener((obs, oldV, newV) -> {
            if (confirmErr.isVisible()) {
                confirmErr.setText("");
                confirmErr.setVisible(false);
                confirmErr.setManaged(false);
                confirmF.getHiddenField().setStyle(PWD_NORMAL_STYLE);
                confirmF.getShownField().setStyle(PWD_NORMAL_STYLE);
            }
        });

        VBox formVBox = new VBox(5, 
            quotaPill,
            new VBox(1, nameLbl, nameF, nameErr),
            new VBox(1, emailLbl, emailF, emailErr),
            new VBox(1, mobLbl, mobF, mobErr),
            new VBox(1, passLbl, passF, passErr),
            new VBox(1, confirmLbl, confirmF, confirmErr)
        );
        formVBox.setMaxWidth(300);
        formVBox.setAlignment(Pos.CENTER_LEFT);

        // Submit Button
        Button regBtn = new Button("Register as Admin");
        regBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-background-radius: 10px; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.35), 8, 0, 0, 3);");
        regBtn.setPrefHeight(38);
        regBtn.setPrefWidth(300);
        regBtn.setMaxWidth(300);

        regBtn.setOnAction(e -> {
            String name = nameF.getText() != null ? nameF.getText().trim() : "";
            String email = emailF.getText() != null ? emailF.getText().trim() : "";
            String mobile = mobF.getText() != null ? mobF.getText().trim() : "";
            String pwd = passF.getText() != null ? passF.getText() : "";
            String cpwd = confirmF.getText() != null ? confirmF.getText() : "";

            boolean valid = true;

            // Name
            if (name.isEmpty() || name.length() < 2) {
                nameErr.setText("Name must be at least 2 characters.");
                nameErr.setVisible(true);
                nameErr.setManaged(true);
                nameF.setStyle(INPUT_ERROR_STYLE);
                valid = false;
            }

            // Email
            if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                emailErr.setText("Enter a valid admin email (e.g. name@domain.com).");
                emailErr.setVisible(true);
                emailErr.setManaged(true);
                emailF.setStyle(INPUT_ERROR_STYLE);
                valid = false;
            }

            // Mobile
            if (mobile.isEmpty() || !mobile.matches("^(?:\\+91|0)?[6-9]\\d{9}$")) {
                mobErr.setText("Enter a valid 10-digit Indian Mobile Number.");
                mobErr.setVisible(true);
                mobErr.setManaged(true);
                mobF.setStyle(INPUT_ERROR_STYLE);
                valid = false;
            }

            // Password
            if (pwd.isEmpty() || !pwd.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
                passErr.setText("Password must be 8+ chars (1 uppercase, 1 digit, 1 symbol).");
                passErr.setVisible(true);
                passErr.setManaged(true);
                passF.getHiddenField().setStyle(PWD_ERROR_STYLE);
                passF.getShownField().setStyle(PWD_ERROR_STYLE);
                valid = false;
            }

            // Confirm Password
            if (cpwd.isEmpty() || !cpwd.equals(pwd)) {
                confirmErr.setText("Passwords do not match.");
                confirmErr.setVisible(true);
                confirmErr.setManaged(true);
                confirmF.getHiddenField().setStyle(PWD_ERROR_STYLE);
                confirmF.getShownField().setStyle(PWD_ERROR_STYLE);
                valid = false;
            }

            if (!valid) return;

            // Quota check
            if (controller.getAdminCount() >= 5) {
                emailErr.setText("Registration Closed: Maximum limit of 5 Admins reached.");
                emailErr.setVisible(true);
                emailErr.setManaged(true);
                return;
            }

            regBtn.setText("Registering...");
            regBtn.setDisable(true);

            javafx.concurrent.Task<AuthenticateController.AdminAuthResult> regTask = new javafx.concurrent.Task<>() {
                @Override
                protected AuthenticateController.AdminAuthResult call() {
                    return controller.registerAdmin(name, email, mobile, pwd);
                }
            };

            regTask.setOnSucceeded(ev -> {
                regBtn.setText("Register as Admin");
                regBtn.setDisable(false);
                AuthenticateController.AdminAuthResult result = regTask.getValue();

                if (result != null && result.isSuccess()) {
                    if (result.getUser() != null) {
                        com.desgin.view.admin.AdminProfileStore.setAdminProfile(
                            result.getUser().getName(),
                            result.getUser().getMail(),
                            result.getUser().getNum(),
                            "Master Admin",
                            result.getUser().getProfilePic()
                        );
                    }
                    com.desgin.view.admin.AdminProfileManagement.updateHeaderGreeting();

                    Runnable backToLogin = () -> backtologin();
                    com.desgin.view.admin.AdminDashboard obj = new com.desgin.view.admin.AdminDashboard();
                    WelcomePage.welcomePageStage.setScene(obj.getAdminDashboardScene(backToLogin));
                } else {
                    String msg = result != null ? result.getMessage() : "Admin registration failed.";
                    emailErr.setText(msg);
                    emailErr.setVisible(true);
                    emailErr.setManaged(true);
                    emailF.setStyle(INPUT_ERROR_STYLE);
                }
            });

            regTask.setOnFailed(ev -> {
                regBtn.setText("Register as Admin");
                regBtn.setDisable(false);
                emailErr.setText("System error during admin registration.");
                emailErr.setVisible(true);
                emailErr.setManaged(true);
                emailF.setStyle(INPUT_ERROR_STYLE);
            });

            new Thread(regTask).start();
        });

        // Switch to Admin Login Link
        Text haveAcc = new Text("Already have an admin account? ");
        haveAcc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151; -fx-font-weight: 500;");

        Text adminLoginLink = new Text("Admin Login");
        adminLoginLink.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");

        HBox loginHBox = new HBox(4, haveAcc, adminLoginLink);
        loginHBox.setAlignment(Pos.CENTER);
        loginHBox.setStyle("-fx-cursor: hand;");

        loginHBox.setOnMouseEntered(e -> adminLoginLink.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #1B4332; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;"));
        loginHBox.setOnMouseExited(e -> adminLoginLink.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #2D6A4F; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;"));
        loginHBox.setOnMouseClicked(event -> {
            VBox adminCard = createAdminLoginCard(borderPane, emailF.getText(), "");
            borderPane.setCenter(adminCard);
            BorderPane.setAlignment(adminCard, Pos.CENTER);
        });

        // Bottom User Portal Pill
        HBox div = new HBox();
        div.setPrefHeight(1);
        div.setMaxHeight(1);
        div.setPrefWidth(290);
        div.setMaxWidth(290);
        div.setStyle("-fx-background-color: rgba(45, 106, 79, 0.2);");

        Label userPill = new Label("👨‍🌾 Farmer / Provider Portal");
        userPill.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2D6A4F; -fx-background-color: #E8F5E9; -fx-background-radius: 16px; -fx-padding: 5px 14px; -fx-cursor: hand; -fx-border-color: rgba(45, 106, 79, 0.3); -fx-border-radius: 16px; -fx-border-width: 1px; -fx-font-family: 'Poppins';");
        userPill.setOnMouseClicked(e -> {
            VBox userCard = createStandardUserLoginCard(borderPane);
            borderPane.setCenter(userCard);
            BorderPane.setAlignment(userCard, Pos.CENTER);
        });

        VBox bottomBox = new VBox(6, div, userPill);
        bottomBox.setAlignment(Pos.CENTER);

        VBox registerAdminCard = new VBox(10, headerVBox, formVBox, regBtn, loginHBox, bottomBox);
        registerAdminCard.setAlignment(Pos.CENTER);
        registerAdminCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.58);" +
            "-fx-background-radius: 22px;" +
            "-fx-border-color: rgba(255, 255, 255, 0.60);" +
            "-fx-border-radius: 22px;" +
            "-fx-border-width: 1.5px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.20), 24, 0.12, 0, 8);" +
            "-fx-padding: 18px 30px 18px 30px;"
        );

        registerAdminCard.setPrefWidth(390);
        registerAdminCard.setMaxWidth(390);
        registerAdminCard.setMaxHeight(Region.USE_PREF_SIZE);

        return registerAdminCard;
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
