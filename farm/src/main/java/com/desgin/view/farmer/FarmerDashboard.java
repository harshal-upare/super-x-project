package com.desgin.view.farmer;

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
import javafx.scene.text.Text;

public class FarmerDashboard {
    
    private Scene farmerDashboardScene;

    public Scene getfarmerDashboardScene() {

        
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(15));
        
        Image logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(48);
        logoImageView.setFitHeight(48);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        
        Text textName = new Text("FarmEquip");
        textName.setStyle(
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Poppins';" +
            "-fx-fill: #4A2C20;"
        );

        Button dashboardBtn1 = new Button("⌂  Dashboard");
        HBox btnBox1 = new HBox(5,dashboardBtn1);
        styleMenuButton(dashboardBtn1);
        
        Button equipmentBtn1 = new Button("⚒  Browse Equipment");
        HBox btnBox2 = new HBox(5,equipmentBtn1);
        styleMenuButton(equipmentBtn1);
        
        Button bookingBtn1 = new Button("📅  My Bookings");
        HBox btnBox3 = new HBox(5,bookingBtn1);
        styleMenuButton(bookingBtn1);
        
        Button wishlistBtn1 = new Button("♥  My Wishlist");
        HBox btnBox4 = new HBox(5,wishlistBtn1);
        styleMenuButton(wishlistBtn1);
        
        Button notificationBtn1 = new Button("🔔  Notifications");
        HBox btnBox5 = new HBox(5,notificationBtn1);
        styleMenuButton(notificationBtn1);

        
        Button reviewBtn1 = new Button("⭐  Reviews");
        HBox btnBox6 = new HBox(5,reviewBtn1);
        styleMenuButton(reviewBtn1);


        VBox vBoxBtn1 = new VBox(10,btnBox1,btnBox2,btnBox3,btnBox4,btnBox5,btnBox6);
        
        Button settingsBtn1 = new Button("⚙  Settings");
        HBox btnBox7 = new HBox(5,settingsBtn1);
        styleMenuButton(settingsBtn1);

        
        Button supportBtn1 = new Button("❓ Help & Support");
        HBox btnBox8 = new HBox(5,supportBtn1);
        styleMenuButton(supportBtn1);


        Button logoutBtn1 = new Button("↪  Logout");
        HBox btnBox9 = new HBox(5,logoutBtn1);
        styleMenuButton(logoutBtn1);


        VBox vBoxBtn2 = new VBox(10,btnBox7,btnBox8,btnBox9);
        
        VBox btnBox = new VBox(90,vBoxBtn1,vBoxBtn2);
        

        HBox logoTextHBox = new HBox(10, logoImageView, textName);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        logoTextHBox.setPadding(new Insets(0, 5, 10, 5));

        VBox leftVB = new VBox(logoTextHBox,btnBox);
        leftVB.setPrefWidth(260);
        leftVB.setMinWidth(260);
        leftVB.setMaxWidth(260);

        leftVB.setSpacing(25);

        leftVB.setPadding(
            new Insets(25, 18, 25, 18)
        );

       leftVB.setStyle(
            "-fx-background-color: #F5EFE6;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: #D8C7B5;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 15;"
        );

        
        borderPane.setLeft(leftVB);

        StackPane root = new StackPane();
        root.getChildren().addAll(borderPane);
    
        root.setStyle(
            "-fx-background-color: #EDE3D5;"
        );
        borderPane.prefWidthProperty().bind(root.widthProperty());
        borderPane.prefHeightProperty().bind(root.heightProperty());
        farmerDashboardScene = new Scene(root);
        
        return farmerDashboardScene;
    }
    private void styleMenuButton(Button button) {

        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(48);
        button.setMinHeight(48);
        button.setMaxHeight(48);

        button.setAlignment(Pos.CENTER_LEFT);

        button.setPadding(
            new Insets(0, 15, 0, 15)
        );

        // Normal style
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: #5C4033;" +
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: normal;" +
            "-fx-cursor: hand;"
        );

        // Hover effect
        button.setOnMouseEntered(e -> {

            button.setStyle(
                "-fx-background-color: #E4D3C2;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #3E2723;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
            );
        });

        // Back to normal
        button.setOnMouseExited(e -> {

            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: normal;" +
                "-fx-cursor: hand;"
            );
        });
    }
    private void styleLogoutButton(Button button) {

        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(48);
        button.setMinHeight(48);
        button.setMaxHeight(48);

        button.setAlignment(Pos.CENTER_LEFT);

        button.setPadding(
            new Insets(0, 15, 0, 15)
        );

        button.setFont(
            javafx.scene.text.Font.font(
                "Poppins",
                14
            )
        );

        button.setTextFill(
            javafx.scene.paint.Color.web("#FFFFFF")
        );

        button.setStyle(
            "-fx-background-color: #8B3A3A;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {

            button.setStyle(
                "-fx-background-color: #A94442;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
            );
        });

        button.setOnMouseExited(e -> {

            button.setStyle(
                "-fx-background-color: #8B3A3A;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
            );
        });
    }
}
