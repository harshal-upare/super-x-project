package com.desgin.view.handling_start;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Authentication {
    
    private Scene authenticationScene;

    Scene getAuthenticationScene() {

        // Image img = new Image("file:agriculture/src/main/resources/assets/Images/loginPageImage2.jpg");
        // ImageView loginPageImageView = new ImageView(img);
        // loginPageImageView.setFitWidth(900);
        // loginPageImageView.setFitHeight(760);
        // loginPageImageView.setPreserveRatio(false);
        // loginPageImageView.setSmooth(true);

        BorderPane borderPane = new BorderPane();

        Image img = new Image("file:farm/src/main/resources/assets/Images/logo.png");
        ImageView loginPageImageView = new ImageView(img);
        loginPageImageView.setFitWidth(60);
        loginPageImageView.setFitHeight(60);
        loginPageImageView.setPreserveRatio(false);
        loginPageImageView.setSmooth(true);
        //loginPageImageView.setStyle("-fx-setPadding:5px");
        

        Text wlcBack = new Text("Welcome Back");
        wlcBack.setStyle("-fx-font-size:35px; -fx-font-weight:bold;");
        Text loginToFarmEquipText = new Text("Login To FarmEquip");
        loginToFarmEquipText.setStyle("-fx-font-size:20px; -fx-font-weight:bold; ");

        VBox primaryVBox = new VBox(10,loginPageImageView,wlcBack,loginToFarmEquipText);
        primaryVBox.setStyle("-fx-alignment:center");


        Label label1 = new Label("Email/Mobile NO.");
        TextField mailAndPhoneTextField = new TextField();
        mailAndPhoneTextField.setPrefSize(250, 42);
        mailAndPhoneTextField.setMaxSize(250, 42);
        mailAndPhoneTextField.setFocusTraversable(false);
        mailAndPhoneTextField.setStyle("-fx-background-color : #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px;");
        
        
        Label label2 = new Label("Password");
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPrefSize(250, 42);
        passwordTextField.setMaxSize(250, 42);
        passwordTextField.setFocusTraversable(false);
        passwordTextField.setStyle("-fx-background-color : #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px;");
        
        
        
        Label roleLabel = new Label("Login As");
        RadioButton farmerRadio = new RadioButton("Farmer");
        RadioButton providerRadio = new RadioButton("Provider");
        RadioButton operatorRadio = new RadioButton("Operator");
        farmerRadio.setSelected(true);
        farmerRadio.setFocusTraversable(false);
        providerRadio.setFocusTraversable(false);
        operatorRadio.setFocusTraversable(false);

        ToggleGroup loginRoleGroup = new ToggleGroup();
        farmerRadio.setToggleGroup(loginRoleGroup);
        providerRadio.setToggleGroup(loginRoleGroup);
        operatorRadio.setToggleGroup(loginRoleGroup);

        HBox roleHBox = new HBox(12, farmerRadio, providerRadio, operatorRadio);
        roleHBox.setAlignment(Pos.CENTER_LEFT);

        VBox secondaryVBox = new VBox(5, label1, mailAndPhoneTextField, label2, passwordTextField, roleLabel, roleHBox);
        secondaryVBox.setMaxWidth(250);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand;");
        loginButton.setPrefHeight(42);
        loginButton.setMaxWidth(250);
        loginButton.setFocusTraversable(false);

        loginButton.setOnAction(e -> {
            RadioButton valRadio = (RadioButton) loginRoleGroup.getSelectedToggle();
            String role = valRadio != null ? valRadio.getText() : "Farmer";

            Runnable backToLogin = new Runnable() {
                public void run() {
                    backtologin();
                }
            };

            if ("Provider".equals(role)) {
                com.desgin.view.provider.ProviderDashboard obj = new com.desgin.view.provider.ProviderDashboard();
                WelcomePage.welcomePageStage.setScene(obj.getProviderDashboardScene(backToLogin));
            } else if ("Operator".equals(role)) {
                com.desgin.view.operator.OperatorDashboard obj = new com.desgin.view.operator.OperatorDashboard();
                WelcomePage.welcomePageStage.setScene(obj.getOperatorDashboardScene(backToLogin));
            } else {
                com.desgin.view.farmer.Swapnil.FarmerDashboard obj = new com.desgin.view.farmer.Swapnil.FarmerDashboard();
                WelcomePage.welcomePageStage.setScene(obj.getfarmerDashboardScene(backToLogin));
            }
        });
        
        Text noAccText = new Text("Don't have an account?");
        noAccText.setStyle("-fx-underline: true; -fx-fill : #000;");
        Text registerText = new Text("Register");
        registerText.setStyle("-fx-underline: true; -fx-fill : #000");
        registerText.setOnMouseClicked(null);

        registerText.setOnMouseClicked(event ->{
          RegisterPage registerPage = new RegisterPage();

            Runnable backToLogin = new Runnable() {
                public void run(){

                    backtologin();

                }
            };

            WelcomePage.welcomePageStage.setScene(registerPage.getRegisterPageScene(backToLogin));
        });
        

        VBox ternaryVBox = new VBox(5,noAccText,registerText);
        ternaryVBox.setStyle("-fx-alignment:center");


        VBox loginVBox = new VBox(25,primaryVBox,secondaryVBox,loginButton,ternaryVBox);
       
        //loginVBox.setStyle("-fx-border-color : #D6DDD2; -fx-padding:30px; -fx-border-radius : 16px; -fx-background-color : #FFFFFF; -fx-background-radius : 16px;");// -fx-alignment:center");
        //loginVBox.setStyle("-fx-padding:30px; -fx-background-color:transparent;");
        
        loginVBox.setStyle("-fx-background-color: rgba(255,255,255,0.58);" +"-fx-background-radius: 22;");

        loginVBox.setAlignment(Pos.TOP_CENTER);
        loginVBox.setPrefSize(450, 500);
        loginVBox.setMaxSize(450, 500);

        borderPane.setCenter(loginVBox);
        borderPane.setStyle("-fx-background-color : transparent;");

        Image backgroundImage = new Image("/assets/Images/background.jpeg");
        ImageView backgroundImageView = new ImageView(backgroundImage);

        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true); 
        backgroundImageView.setMouseTransparent(true);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView,borderPane);

        backgroundImageView.fitWidthProperty().bind(root.widthProperty());
        backgroundImageView.fitHeightProperty().bind(root.heightProperty());

        authenticationScene = new Scene(root);

        return authenticationScene;
    }

    public void backtologin(){
        WelcomePage.welcomePageStage.setScene(authenticationScene);
    }
}
