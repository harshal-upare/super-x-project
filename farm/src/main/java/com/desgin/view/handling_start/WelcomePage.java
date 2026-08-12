package com.desgin.view.handling_start;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomePage extends Application {

    private Scene welcomePageScene;
    public static Stage welcomePageStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        welcomePageStage = primaryStage;

        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(Insets.EMPTY);
        borderPane.setStyle("-fx-background-color: transparent;");

        // Farm
        Text applicationName1 = new Text("Farm");
        applicationName1.setStyle("-fx-font-size: 34px;" +"-fx-font-weight: bold;" +"-fx-font-family: Poppins;" +"-fx-fill: #FFFFFF;");

        // Equip
        Text applicationName2 = new Text("Equip");
        applicationName2.setStyle("-fx-font-size: 34px;" +"-fx-font-weight: bold;" +"-fx-font-family: Poppins;" +"-fx-fill: #A5D65E;");

        HBox applicationName = new HBox(0,applicationName1,applicationName2);
        applicationName.setAlignment(Pos.CENTER_LEFT);

        Image logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.jpeg");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(80);
        logoImageView.setFitHeight(80);
        logoImageView.setPreserveRatio(false);

        // Rounded corners for logo
        Rectangle logoClip = new Rectangle(80,80);
        logoClip.setArcWidth(18);
        logoClip.setArcHeight(18);
        logoImageView.setClip(logoClip);

        Text describeText = new Text("Agricultural Equipment Rental Platform");
        describeText.setStyle("-fx-font-size: 17px;" +"-fx-font-weight: normal;" +"-fx-font-family: Poppins;" +"-fx-fill: #D8F3DC;");

        VBox topVBox = new VBox(3,applicationName,describeText);
        topVBox.setAlignment(Pos.CENTER_LEFT);

        HBox topHBox = new HBox(18,logoImageView,topVBox);
        topHBox.setPrefHeight(90);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(5,0,5,0));
        topHBox.setStyle("-fx-background-color: #176B24;");

        borderPane.setTop(topHBox);

        // Welcome text
        Text welcomeText = new Text("Welcome to");
        welcomeText.setStyle("-fx-font-size: 24px;" +"-fx-font-weight: bold;" +"-fx-font-family: Poppins;" +"-fx-fill: #16723A;");

        // FarmEquip
        Text farmEquipText = new Text("FarmEquip");
        farmEquipText.setStyle("-fx-font-size: 52px;" +"-fx-font-weight: bold;" +"-fx-font-family: Poppins;" + "-fx-fill: #064E3B;");

        // Subtitle
        Text subtitleText = new Text("Agricultural Equipment\nRental Platform");
        subtitleText.setStyle("-fx-font-size: 20px;" +"-fx-font-weight: bold;" +"-fx-font-family: Poppins;" +"-fx-fill: #263238;");

        // Description
        Text descriptionText = new Text("Connecting farmers with equipment providers\n" +"through a secure, reliable and modern platform.");
        descriptionText.setStyle("-fx-font-size: 16px;" +"-fx-font-weight: normal;" +"-fx-font-family: Poppins;" +"-fx-fill: #37474F;");

        Button getStarted = new Button("Get Started");
        getStarted.setPrefWidth(220);
        getStarted.setPrefHeight(52);
        getStarted.setStyle("-fx-background-color: #087F3F;" +"-fx-text-fill: white;" +"-fx-font-size: 18px;" +"-fx-font-weight: bold;" +"-fx-font-family: Poppins;" +"-fx-background-radius: 12;" +"-fx-cursor: hand;");

        getStarted.setOnAction(event -> {

            Authentication obj = new Authentication();
            welcomePageStage.setScene(obj.getAuthenticationScene());
        });

        VBox leftVBox = new VBox(18,welcomeText,farmEquipText,subtitleText,descriptionText,getStarted);
        leftVBox.setAlignment(Pos.CENTER_LEFT);

        /*
         * This transparent/white card makes the text readable
         * while still allowing the farm background to be visible.
         */

        StackPane contentCard = new StackPane();
        contentCard.setMaxWidth(560);
        contentCard.setMaxHeight(440);
        contentCard.setPadding(new Insets(35,40,35,40));
        contentCard.setStyle("-fx-background-color: rgba(255,255,255,0.58);" +"-fx-background-radius: 22;");
        contentCard.getChildren().add(leftVBox);

        HBox centerHBox = new HBox(contentCard);
        centerHBox.setAlignment(Pos.CENTER_LEFT);
        centerHBox.setPadding(new Insets(0,0,0,100));

        // VERY IMPORTANT:
        // Keep center transparent.
        centerHBox.setStyle("-fx-background-color: transparent;");
        borderPane.setCenter(centerHBox);

        Text footerText = new Text("© 2026 FarmEquip | Empowering Agriculture Through Technology");
        footerText.setStyle("-fx-font-size: 13px;" +"-fx-text-fill: #444444;" +"-fx-font-family: Poppins;");

        HBox footerHBox = new HBox(footerText);
        footerHBox.setPrefHeight(40);
        footerHBox.setAlignment(Pos.CENTER);
        footerHBox.setStyle("-fx-background-color: rgba(232,245,233,0.95);");

        borderPane.setBottom(footerHBox);

        //Image backgroundImage = new Image("/assets/Images/farmBackground.png");
        Image backgroundImage = new Image("file:farm/src/main/resources/assets/Images/background.jpeg");
        ImageView backgroundImageView = new ImageView(backgroundImage);

        
        StackPane root = new StackPane();
        root.setPadding(Insets.EMPTY);

        backgroundImageView.fitWidthProperty().bind(root.widthProperty());
        backgroundImageView.fitHeightProperty().bind(root.heightProperty());
        backgroundImageView.setPreserveRatio(false);

        // Better image quality while resizing
        backgroundImageView.setSmooth(true);

        // Background doesn't receive mouse events
        backgroundImageView.setMouseTransparent(true);

        root.getChildren().addAll(backgroundImageView,borderPane);
        
        welcomePageScene = new Scene(root);
        
        welcomePageStage.setScene(welcomePageScene);
        welcomePageStage.setTitle("Farm Equipment Rental Platform (FarmEquip)");
        welcomePageStage.setMaximized(true);
        welcomePageStage.show();
    }
}
