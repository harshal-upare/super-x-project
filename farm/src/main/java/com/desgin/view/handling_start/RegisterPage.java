package com.desgin.view.handling_start;
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

public class RegisterPage {

    private Scene registerScene;

    public Scene getRegisterPageScene(Runnable backToLogin){

        BorderPane borderPane = new BorderPane();

        // ================= LOGO =================

        Image img = new Image("file:agriculture/src/main/resources/assets/Icons/finalLogo.png");

        ImageView registrationPageImageView = new ImageView(img);

        registrationPageImageView.setFitWidth(60);
        registrationPageImageView.setFitHeight(60);
        registrationPageImageView.setPreserveRatio(false);
        registrationPageImageView.setSmooth(true);


        // ================= HEADING =================

        Text createAccount = new Text("Create Account");

        createAccount.setStyle("-fx-font-size:35px; -fx-font-weight:bold;");


        Text joinFarmEquip = new Text("Register To FarmEquip");

        joinFarmEquip.setStyle("-fx-font-size:20px; -fx-font-weight:bold;");


        VBox primaryVBox = new VBox(10,registrationPageImageView, createAccount,joinFarmEquip );

        primaryVBox.setStyle( "-fx-alignment:center");


        // ================= NAME =================

        Label label1 = new Label("Full Name");

        TextField nameTextField = new TextField();

        nameTextField.setPrefSize(250, 42);
        nameTextField.setMaxSize(250, 42);
        nameTextField.setFocusTraversable(false);

        nameTextField.setStyle("-fx-background-color : #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px;");


        // ================= EMAIL =================

        Label label2 = new Label("Email");

        TextField emailTextField = new TextField();

        emailTextField.setPrefSize(250, 42);
        emailTextField.setMaxSize(250, 42);
        emailTextField.setFocusTraversable(false);

        emailTextField.setStyle("-fx-background-color : #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px;");


        // ================= MOBILE =================

        Label label3 = new Label("Mobile No.");

        TextField mobileTextField = new TextField();

        mobileTextField.setPrefSize(250, 42);
        mobileTextField.setMaxSize(250, 42);
        mobileTextField.setFocusTraversable(false);

        mobileTextField.setStyle("-fx-background-color : #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px;");


        // ================= PASSWORD =================

        Label label4 = new Label("Password");

        PasswordField passwordTextField = new PasswordField();

        passwordTextField.setPrefSize(250, 42);
        passwordTextField.setMaxSize(250, 42);
        passwordTextField.setFocusTraversable(false);

        passwordTextField.setStyle("-fx-background-color : #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px;");


        // ================= CONFIRM PASSWORD =================

        Label label5 = new Label("Confirm Password");

        PasswordField confirmPasswordTextField = new PasswordField();

        confirmPasswordTextField.setPrefSize(250, 42);
        confirmPasswordTextField.setMaxSize(250, 42);
        confirmPasswordTextField.setFocusTraversable(false);

        confirmPasswordTextField.setStyle( "-fx-background-color : #F8FAF7; -fx-border-color : #D6DDD2; -fx-border-radius: 8px;");


        // ================= INPUT SECTION =================

        VBox secondaryVBox = new VBox(5,label1,nameTextField,label2,emailTextField,label3,
            mobileTextField,label4,passwordTextField,label5,confirmPasswordTextField);

        secondaryVBox.setMaxWidth(250);


        // ================= REGISTER BUTTON =================

        Button registerButton = new Button("Register");

        registerButton.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 15px;  -fx-font-weight: bold;");

        registerButton.setPrefHeight(42);
        registerButton.setMaxWidth(250);
        registerButton.setFocusTraversable(false);


        // ================= LOGIN TEXT =================

        Text alreadyAccountText = new Text("Already have an account?");

        alreadyAccountText.setStyle("-fx-underline: true;");


        Text loginText = new Text("Login");

        loginText.setStyle("-fx-underline: true; -fx-fill : #2E7D32");
        loginText.setOnMouseClicked(event ->{

            backToLogin.run();

        });


        VBox ternaryVBox = new VBox(5,alreadyAccountText,loginText);

        ternaryVBox.setStyle("-fx-alignment:center");


        // ================= MAIN REGISTER BOX =================

        VBox registrationVBox = new VBox(20,primaryVBox,secondaryVBox,registerButton,ternaryVBox);

        registrationVBox.setStyle("-fx-border-color : #D6DDD2; -fx-padding:30px; -fx-border-radius : 16px; -fx-background-color : #FFFFFF; -fx-background-radius : 16px;");

        registrationVBox.setAlignment(Pos.TOP_CENTER);

        registrationVBox.setPrefSize(450,650);

        registrationVBox.setMaxSize(450,650);


        // ================= BORDER PANE =================

        borderPane.setCenter(registrationVBox);

        borderPane.setStyle("-fx-background-color : #F8F7F2;");


        Scene sc = new Scene(borderPane,1500,750);
        registerScene = sc;
        
        return registerScene;

        

    }
    
}