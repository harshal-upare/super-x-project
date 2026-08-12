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
        logoImageView.setFitWidth(60);
        logoImageView.setFitHeight(60);
        logoImageView.setPreserveRatio(false);
        logoImageView.setSmooth(true);
        
        Text textName = new Text("FarmEquip");
        textName.setStyle("-fx-font-size: 34px;" +"-fx-font-weight: bold;" +"-fx-font-family: Poppins;" +"-fx-fill: #FFFFFF;");
        
        Button dashboardBtn1 = new Button("🏠");
        Button dashboardBtn2 = new Button("Dashboard");
        HBox btnBox1 = new HBox(5,dashboardBtn1,dashboardBtn2);
        
        Button equipmentBtn1 = new Button("🚜");
        Button equipmentBtn2 = new Button("Browse Equipment");
        HBox btnBox2 = new HBox(5,equipmentBtn1,equipmentBtn2);
        
        Button bookingBtn1 = new Button("📅");
        Button bookingBtn2 = new Button("My Bookings");
        HBox btnBox3 = new HBox(5,bookingBtn1,bookingBtn2);
        
        Button wishlistBtn1 = new Button("♥");
        Button wishlistBtn2 = new Button("My Wishlist");
        HBox btnBox4 = new HBox(5,wishlistBtn1,wishlistBtn2);
        
        Button notificationBtn1 = new Button("🔔");
        Button notificationBtn2 = new Button("Notifications");
        HBox btnBox5 = new HBox(5,notificationBtn1,notificationBtn2);
        
        Button reviewBtn1 = new Button("🔔");
        Button reviewBtn2 = new Button("Notifications");
        HBox btnBox6 = new HBox(5,reviewBtn1,reviewBtn2);
       

        

        
        // Button settingsBtn =
        //         createMenuButton("⚙", "Settings");

        // Button helpBtn =
        //         createMenuButton("❓", "Help & Support");

        VBox vb = new VBox();
        

        HBox logoTextHBox = new HBox(5,logoImageView,textName);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        borderPane.setLeft(logoTextHBox);

        StackPane root = new StackPane();
        root.getChildren().addAll(borderPane);
    
        root.setStyle("-fx-background-color: #870404");
        borderPane.prefWidthProperty().bind(root.widthProperty());
        borderPane.prefHeightProperty().bind(root.heightProperty());
        farmerDashboardScene = new Scene(root);
        
        return farmerDashboardScene;
    }
}
