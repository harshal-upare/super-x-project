package com.desgin.view.farmer;

import java.util.ArrayList;
import java.util.List;

import com.desgin.view.farmer.Swapnil.Dashboard;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.ashutosh.helpandsupport.Help;
import com.desgin.view.farmer.ashutosh.settings.Settings;
import com.desgin.view.farmer.harshal.MyBookings;
import com.desgin.view.farmer.om.BrowseEquip;
import com.desgin.view.farmer.pratik.Payment;
import com.desgin.view.farmer.pratik.WishList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LeftSideBar {

        private static Button activeButton;

        public static List<Button> navigationButtons = new ArrayList<>();
        public static Button dashboardBtn1;
        public static Button equipmentBtn1;
        public static Button bookingBtn1;
        public static Button wishlistBtn1;
        public static Button paymentBtn1;
        public static Button settingsBtn1;
        public static Button supportBtn1;

        public static StackPane root;
        public LeftSideBar(FarmerDashboard obj) {

                this.root = obj.root;
        }
        public VBox getSideBar(Runnable ref) {

                Image logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
                ImageView logoImageView = new ImageView(logoImage);
                logoImageView.setFitWidth(48);
                logoImageView.setFitHeight(48);
                logoImageView.setPreserveRatio(false);
                logoImageView.setSmooth(true);

                Text textName = new Text("FarmEquip");
                textName.setStyle("-fx-font-size: 30px;" + "-fx-font-weight: bold;" + "-fx-font-family: 'Poppins';"
                                + "-fx-fill: #4A2C20;");

                dashboardBtn1 = new Button("⌂  Dashboard");
                HBox btnBox1 = new HBox(5, dashboardBtn1);
                styleMenuButton(dashboardBtn1);

                equipmentBtn1 = new Button("⚒  Browse Equipment");
                HBox btnBox2 = new HBox(5, equipmentBtn1);
                styleMenuButton(equipmentBtn1);

                bookingBtn1 = new Button("📅  My Bookings");
                HBox btnBox3 = new HBox(5, bookingBtn1);
                styleMenuButton(bookingBtn1);

                wishlistBtn1 = new Button("♥  My Wishlist");
                HBox btnBox4 = new HBox(5, wishlistBtn1);
                styleMenuButton(wishlistBtn1);

                // styleMenuButton(notificationBtn1);

                paymentBtn1 = new Button(" 💸 Payment");
                HBox btnBox6 = new HBox(5, paymentBtn1);
                styleMenuButton(paymentBtn1);
                

                VBox vBoxBtn1 = new VBox(10, btnBox1, btnBox2, btnBox3, btnBox4, btnBox6);

                settingsBtn1 = new Button("⚙  Settings");
                HBox btnBox7 = new HBox(5, settingsBtn1);
                styleMenuButton(settingsBtn1);

                Button supportBtn1 = new Button("❓ Help & Support");
                HBox btnBox8 = new HBox(5, supportBtn1);
                styleMenuButton(supportBtn1);

                Button logoutBtn1 = new Button("↪  Logout");
                logoutBtn1.setOnAction(e ->{
                        ref.run();
                });
                HBox btnBox9 = new HBox(5, logoutBtn1);
                styleLogoutButton(logoutBtn1);

                VBox vBoxBtn2 = new VBox(10, btnBox7, btnBox8, btnBox9);
                
                Region spacer = new Region();
                VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                VBox btnBox = new VBox(10, vBoxBtn1, spacer, vBoxBtn2);

                HBox logoTextHBox = new HBox(10, logoImageView, textName);
                logoTextHBox.setAlignment(Pos.CENTER_LEFT);
                logoTextHBox.setPadding(new Insets(0, 5, 10, 5));

                VBox leftVB = new VBox(logoTextHBox, btnBox);
                leftVB.setPrefWidth(260);
                leftVB.setMinWidth(260);
                leftVB.setMaxWidth(260);
                leftVB.setMaxHeight(Double.MAX_VALUE);
                leftVB.setSpacing(25);
                leftVB.setPadding(new Insets(25, 18, 25, 18));
                leftVB.setStyle("-fx-background-color: #F5EFE6;" + "-fx-background-radius: 15;"
                                + "-fx-border-color: #D8C7B5;" + "-fx-border-width: 1;" + "-fx-border-radius: 15;");

                navigationButtons.add(dashboardBtn1);
                navigationButtons.add(equipmentBtn1);
                navigationButtons.add(bookingBtn1);
                navigationButtons.add(wishlistBtn1);
                navigationButtons.add(paymentBtn1);
                navigationButtons.add(settingsBtn1);
                navigationButtons.add(supportBtn1);

                setActiveButton(dashboardBtn1, navigationButtons );

                dashboardBtn1.setOnAction(event -> {

                        setActiveButton(dashboardBtn1, navigationButtons);
                        FarmerDashboard.borderPane.setCenter(Dashboard.getPage());      
                });

                equipmentBtn1.setOnAction(event -> {
                        setActiveButton(equipmentBtn1, navigationButtons);      

                        BrowseEquip obj = new BrowseEquip();
                     
                        FarmerDashboard.borderPane.setCenter(BrowseEquip.getBrowseEquip());


                        
                });
                bookingBtn1.setOnAction(event -> {

                        setActiveButton(bookingBtn1, navigationButtons);
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(root));


                });
                paymentBtn1.setOnAction(event -> {

                        setActiveButton(paymentBtn1, navigationButtons);

                        FarmerDashboard.borderPane.setCenter(Payment.getPaymentSection()); 
                });

                wishlistBtn1.setOnAction(event -> {

                        setActiveButton(wishlistBtn1, navigationButtons);
                        FarmerDashboard.borderPane.setCenter( WishList.getWishList());
                });
        

                settingsBtn1.setOnAction(event -> {
                        setActiveButton(settingsBtn1, navigationButtons);

                        FarmerDashboard.borderPane.setCenter(Settings.getSetting());
                });

                supportBtn1.setOnAction(event -> {
                        setActiveButton(supportBtn1, navigationButtons);

                        FarmerDashboard.borderPane.setCenter(Help.getHelp());
                });


        
                return leftVB;

        }

        public static void setActiveButton(
                        Button selected,
                        List<Button> buttons) {

                activeButton = selected;
                for (Button button : buttons) {

                        button.setStyle(
                                        "-fx-background-color: transparent;" +
                                                        "-fx-background-radius: 10;" +
                                                        "-fx-text-fill: #5C4033;" +
                                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 14px;" +
                                                        "-fx-font-weight: normal;" +
                                                        "-fx-cursor: hand;");
                }

                selected.setStyle(
                                "-fx-background-color: #E4D3C2;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-text-fill: #4A2C20;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");
        }

        private void styleMenuButton(Button button) {

                button.setPrefWidth(224);
                button.setMinWidth(224);
                button.setMaxWidth(224);

                button.setPrefHeight(48);
                button.setMinHeight(48);
                button.setMaxHeight(48);

                button.setAlignment(Pos.CENTER_LEFT);

                button.setPadding(new Insets(0, 15, 0, 15));

                // Normal style
                button.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-text-fill: #5C4033;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: normal;" +
                                                "-fx-cursor: hand;");

                // Hover
                button.setOnMouseEntered(e -> {

                        if (button != activeButton) {

                                button.setStyle(
                                                "-fx-background-color: #E4D3C2;" +
                                                                "-fx-background-radius: 10;" +
                                                                "-fx-text-fill: #3E2723;" +
                                                                "-fx-font-family: 'Poppins';" +
                                                                "-fx-font-size: 14px;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-cursor: hand;");
                        }
                });

                // Mouse leaves
                button.setOnMouseExited(e -> {

                        if (button != activeButton) {

                                button.setStyle(
                                                "-fx-background-color: transparent;" +
                                                                "-fx-background-radius: 10;" +
                                                                "-fx-text-fill: #5C4033;" +
                                                                "-fx-font-family: 'Poppins';" +
                                                                "-fx-font-size: 14px;" +
                                                                "-fx-font-weight: normal;" +
                                                                "-fx-cursor: hand;");
                        }
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
                                new Insets(0, 15, 0, 15));

                button.setFont(
                                javafx.scene.text.Font.font(
                                                "Poppins",
                                                14));

                button.setTextFill(
                                javafx.scene.paint.Color.web("#FFFFFF"));

                button.setStyle(
                                "-fx-background-color: #8B3A3A;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-cursor: hand;");

                button.setOnMouseEntered(e -> {

                        button.setStyle(
                                        "-fx-background-color: #A94442;" +
                                                        "-fx-background-radius: 10;" +
                                                        "-fx-cursor: hand;");
                });

                button.setOnMouseExited(e -> {

                        button.setStyle(
                                        "-fx-background-color: #8B3A3A;" +
                                                        "-fx-background-radius: 10;" +
                                                        "-fx-cursor: hand;");
                });
        }

}