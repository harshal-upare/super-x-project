package com.desgin.view;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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

        Image img = new Image("file:agriculture/src/main/resources/assets/Icons/finalLogo.png");
        ImageView loginPageImageView = new ImageView(img);
        loginPageImageView.setFitWidth(60);
        loginPageImageView.setFitHeight(60);
        loginPageImageView.setPreserveRatio(false);
        loginPageImageView.setSmooth(true);
        

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
        
        
        
        VBox secondaryVBox = new VBox(5,label1,mailAndPhoneTextField,label2,passwordTextField);
        //secondaryVBox.setStyle("-fx-alignment:center");
        secondaryVBox.setMaxWidth(250);
        
        
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 15px; -fx-font-weight: bold;");
        loginButton.setPrefHeight(42);
        loginButton.setMaxWidth(250);
        loginButton.setFocusTraversable(false);
        
        Text noAccText = new Text("Don't have an account?");
        noAccText.setStyle("-fx-underline: true;");
        Text registerText = new Text("Register");
        registerText.setStyle("-fx-underline: true; -fx-fill : #2E7D32");
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
       
        loginVBox.setStyle("-fx-border-color : #D6DDD2; -fx-padding:30px; -fx-border-radius : 16px; -fx-background-color : #FFFFFF; -fx-background-radius : 16px;");// -fx-alignment:center");
        loginVBox.setAlignment(Pos.TOP_CENTER);
        loginVBox.setPrefSize(450, 500);
        loginVBox.setMaxSize(450, 500);

        borderPane.setCenter(loginVBox);
        borderPane.setStyle("-fx-background-color : #F8F7F2;");

        authenticationScene = new Scene(borderPane,1500,750);

        return authenticationScene;
    }

    public void backtologin(){
        WelcomePage.welcomePageStage.setScene(authenticationScene);
    }
}
