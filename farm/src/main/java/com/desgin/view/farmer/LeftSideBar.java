package com.desgin.view.farmer;

import java.util.ArrayList;
import java.util.List;

import com.desgin.view.farmer.Swapnil.Dashboard;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;
import com.desgin.view.farmer.ashutosh.helpandsupport.Help;
import com.desgin.view.farmer.ashutosh.profile.ProfileManagement;
import com.desgin.view.farmer.ashutosh.settings.Settings;
import com.desgin.view.farmer.harshal.MyBookings;
import com.desgin.view.farmer.om.BrowseEquip;
import com.desgin.view.farmer.pratik.WishList;
// import com.desgin.view.farmer.review.ReviewRating;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        public static Button payoutBtn1;
        public static Button paymentBtn1;
        public static Button reviewBtn1;
        public static Button settingsBtn1;
        public static Button supportBtn1;

        public static StackPane root;

        public LeftSideBar(FarmerDashboard obj) {
                LeftSideBar.root = FarmerDashboard.root;
        }

        public VBox getSideBar(Runnable ref) {

                navigationButtons.clear();

                Image logoImage;
                try {
                    logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
                    if (logoImage.isError()) {
                        logoImage = new Image(getClass().getResourceAsStream("/assets/Images/logo.png"));
                    }
                } catch (Exception e) {
                    logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.jpeg");
                }

                ImageView logoImageView = new ImageView(logoImage);
                logoImageView.setFitWidth(30);
                logoImageView.setFitHeight(30);
                logoImageView.setPreserveRatio(true);
                logoImageView.setSmooth(true);

                StackPane logoContainer = new StackPane(logoImageView);
                logoContainer.setPrefSize(42, 42);
                logoContainer.setMinSize(42, 42);
                logoContainer.setMaxSize(42, 42);
                logoContainer.setStyle(
                                "-fx-background-color: linear-gradient(to bottom right, rgba(45, 106, 79, 0.7), rgba(82, 183, 136, 0.4));" +
                                "-fx-background-radius: 12px;" +
                                "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                                "-fx-border-radius: 12px;" +
                                "-fx-border-width: 1px;" +
                                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.4), 8, 0, 0, 2);");

                Text textName = new Text("FarmEquip");
                textName.setStyle(
                                "-fx-font-size: 19px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-family: 'Poppins';" +
                                "-fx-fill: #FFFFFF;");

                Text subText = new Text("🌱 Farmer Portal");
                subText.setStyle(
                                "-fx-font-size: 11px;" +
                                "-fx-font-weight: 600;" +
                                "-fx-font-family: 'Poppins';" +
                                "-fx-fill: #74C69D;");

                VBox brandBox = new VBox(1, textName, subText);
                brandBox.setAlignment(Pos.CENTER_LEFT);

                HBox logoTextHBox = new HBox(10, logoContainer, brandBox);
                logoTextHBox.setAlignment(Pos.CENTER_LEFT);
                logoTextHBox.setPadding(new Insets(4, 6, 16, 6));
                logoTextHBox.setStyle("-fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-width: 0 0 1px 0;");

                // Main navigation buttons
                dashboardBtn1 = new Button("⌂  Dashboard");
                styleMenuButton(dashboardBtn1);

                equipmentBtn1 = new Button("⚒  Browse Equipment");
                styleMenuButton(equipmentBtn1);

                bookingBtn1 = new Button("📅  My Bookings");
                styleMenuButton(bookingBtn1);

                wishlistBtn1 = new Button("❤️  Saved Wishlist");
                styleMenuButton(wishlistBtn1);

                payoutBtn1 = new Button("💳  Payments & Spending");
                styleMenuButton(payoutBtn1);

                paymentBtn1 = new Button("👷  Search Operators");
                styleMenuButton(paymentBtn1);

                reviewBtn1 = new Button("⭐  Reviews & Ratings");
                styleMenuButton(reviewBtn1);

                settingsBtn1 = new Button("⚙  Settings");
                styleMenuButton(settingsBtn1);

                supportBtn1 = new Button("🛟  Help & Support");
                styleMenuButton(supportBtn1);

                VBox vBoxBtn1 = new VBox(6, dashboardBtn1, equipmentBtn1, bookingBtn1, wishlistBtn1, payoutBtn1, paymentBtn1, reviewBtn1);

                Button logoutBtn1 = new Button("↪  Logout");
                logoutBtn1.setOnAction(e -> {
                        if (ref != null) ref.run();
                });
                styleLogoutButton(logoutBtn1);

                VBox vBoxBtn2 = new VBox(6, settingsBtn1, supportBtn1, logoutBtn1);

                Region spacer = new Region();
                VBox.setVgrow(spacer, Priority.ALWAYS);

                VBox btnBox = new VBox(10, vBoxBtn1, spacer, vBoxBtn2);
                VBox.setVgrow(btnBox, Priority.ALWAYS);

                VBox leftVB = new VBox(logoTextHBox, btnBox);
                VBox.setVgrow(leftVB, Priority.ALWAYS);
                leftVB.setPrefWidth(250);
                leftVB.setMinWidth(250);
                leftVB.setMaxWidth(250);
                leftVB.setMaxHeight(Double.MAX_VALUE);
                leftVB.setSpacing(16);
                leftVB.setPadding(new Insets(20, 14, 20, 14));
                leftVB.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, #11281E 0%, #163628 50%, #0F231B 100%);" +
                                "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                                "-fx-border-width: 0 1px 0 0;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 12, 0, 3, 0);");

                navigationButtons.add(dashboardBtn1);
                navigationButtons.add(equipmentBtn1);
                navigationButtons.add(bookingBtn1);
                navigationButtons.add(wishlistBtn1);
                navigationButtons.add(payoutBtn1);
                navigationButtons.add(paymentBtn1);
                navigationButtons.add(reviewBtn1);
                navigationButtons.add(settingsBtn1);
                navigationButtons.add(supportBtn1);

                setActiveButton(dashboardBtn1, navigationButtons);

                dashboardBtn1.setOnAction(event -> {
                        setActiveButton(dashboardBtn1, navigationButtons);
                        ProfileManagement.updateHeaderGreeting();
                        FarmerDashboard.borderPane.setCenter(Dashboard.getPage());
                });

                equipmentBtn1.setOnAction(event -> {
                        setActiveButton(equipmentBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("Browse Equipment ⚒", "Find and rent the right machinery for your farm");
                        FarmerDashboard.borderPane.setCenter(BrowseEquip.getBrowseEquip());
                });

                bookingBtn1.setOnAction(event -> {
                        setActiveButton(bookingBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("My Bookings 📅", "Track and manage your equipment rentals & schedules");
                        MyBookings obj = new MyBookings();
                        FarmerDashboard.borderPane.setCenter(obj.getBooking(root));
                });

                wishlistBtn1.setOnAction(event -> {
                        setActiveButton(wishlistBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("My Saved Wishlist ❤️", "Saved machinery and certified operators for quick booking");
                        FarmerDashboard.borderPane.setCenter(WishList.getWishList());
                });

                payoutBtn1.setOnAction(event -> {
                        setActiveButton(payoutBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("Payments & Spending 💳", "Review transaction receipts, monthly spending curves, and invoices");
                        FarmerDashboard.borderPane.setCenter(com.desgin.view.farmer.pratik.Payment.getPaymentSection());
                });

                paymentBtn1.setOnAction(event -> {
                        setActiveButton(paymentBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("Search Machinery Operators 👷", "Find and hire certified drivers and machine operators");
                        FarmerDashboard.borderPane.setCenter(com.desgin.view.farmer.pratik.SearchOperator.getSearchOperatorSection(root));
                });

                reviewBtn1.setOnAction(event -> {
                        setActiveButton(reviewBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("Reviews & Field Ratings ⭐", "Rate your completed rentals and machine operators");
                        FarmerDashboard.borderPane.setCenter(com.desgin.view.farmer.review.ReviewRating.getReviewRatingPage(root));
                });

                settingsBtn1.setOnAction(event -> {
                        setActiveButton(settingsBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("Settings & Preferences ⚙", "Manage your account credentials and security");
                        FarmerDashboard.borderPane.setCenter(Settings.getSetting());
                });

                supportBtn1.setOnAction(event -> {
                        setActiveButton(supportBtn1, navigationButtons);
                        ProfileManagement.setHeaderTitle("Help & Support Desk 🛟", "Get 24x7 farmer assistance and AI advisory");
                        FarmerDashboard.borderPane.setCenter(Help.getHelp());
                });

                return leftVB;
        }

        public static void setActiveButton(Button selected, List<Button> buttons) {
                activeButton = selected;
                for (Button button : buttons) {
                        if (button == null) continue;
                        if (button == selected) {
                                button.setStyle(
                                                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C, #52B788);" +
                                                "-fx-background-radius: 10px;" +
                                                "-fx-text-fill: #FFFFFF;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13.5px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(82, 183, 136, 0.4), 10, 0, 0, 3);");
                        } else {
                                button.setStyle(
                                                "-fx-background-color: transparent;" +
                                                "-fx-background-radius: 10px;" +
                                                "-fx-text-fill: #D1E7DD;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13.5px;" +
                                                "-fx-font-weight: 500;" +
                                                "-fx-cursor: hand;");
                        }
                }
        }

        private void styleMenuButton(Button button) {
                button.setPrefWidth(222);
                button.setMinWidth(222);
                button.setMaxWidth(222);

                button.setPrefHeight(44);
                button.setMinHeight(44);
                button.setMaxHeight(44);

                button.setAlignment(Pos.CENTER_LEFT);
                button.setPadding(new Insets(0, 16, 0, 16));

                // Normal style
                button.setStyle(
                                "-fx-background-color: transparent;" +
                                "-fx-background-radius: 10px;" +
                                "-fx-text-fill: #D1E7DD;" +
                                "-fx-font-family: 'Poppins';" +
                                "-fx-font-size: 13.5px;" +
                                "-fx-font-weight: 500;" +
                                "-fx-cursor: hand;");

                // Hover
                button.setOnMouseEntered(e -> {
                        if (button != activeButton) {
                                button.setStyle(
                                                "-fx-background-color: rgba(255, 255, 255, 0.08);" +
                                                "-fx-background-radius: 10px;" +
                                                "-fx-text-fill: #FFFFFF;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13.5px;" +
                                                "-fx-font-weight: 600;" +
                                                "-fx-cursor: hand;");
                        }
                });

                // Mouse leaves
                button.setOnMouseExited(e -> {
                        if (button != activeButton) {
                                button.setStyle(
                                                "-fx-background-color: transparent;" +
                                                "-fx-background-radius: 10px;" +
                                                "-fx-text-fill: #D1E7DD;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13.5px;" +
                                                "-fx-font-weight: 500;" +
                                                "-fx-cursor: hand;");
                        }
                });
        }

        private void styleLogoutButton(Button button) {
                button.setPrefWidth(222);
                button.setMinWidth(222);
                button.setMaxWidth(222);

                button.setPrefHeight(42);
                button.setMinHeight(42);
                button.setMaxHeight(42);

                button.setAlignment(Pos.CENTER_LEFT);
                button.setPadding(new Insets(0, 16, 0, 16));

                button.setStyle(
                                "-fx-background-color: rgba(239, 68, 68, 0.12);" +
                                "-fx-background-radius: 10px;" +
                                "-fx-text-fill: #FCA5A5;" +
                                "-fx-font-family: 'Poppins';" +
                                "-fx-font-size: 13px;" +
                                "-fx-font-weight: 600;" +
                                "-fx-cursor: hand;" +
                                "-fx-border-color: rgba(239, 68, 68, 0.3);" +
                                "-fx-border-radius: 10px;" +
                                "-fx-border-width: 1px;");

                button.setOnMouseEntered(e -> {
                        button.setStyle(
                                        "-fx-background-color: rgba(239, 68, 68, 0.25);" +
                                        "-fx-background-radius: 10px;" +
                                        "-fx-text-fill: #FFFFFF;" +
                                        "-fx-font-family: 'Poppins';" +
                                        "-fx-font-size: 13px;" +
                                        "-fx-font-weight: 600;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-border-color: rgba(239, 68, 68, 0.6);" +
                                        "-fx-border-radius: 10px;" +
                                        "-fx-border-width: 1px;" +
                                        "-fx-effect: dropshadow(gaussian, rgba(239, 68, 68, 0.25), 8, 0, 0, 2);");
                });

                button.setOnMouseExited(e -> {
                        button.setStyle(
                                        "-fx-background-color: rgba(239, 68, 68, 0.12);" +
                                        "-fx-background-radius: 10px;" +
                                        "-fx-text-fill: #FCA5A5;" +
                                        "-fx-font-family: 'Poppins';" +
                                        "-fx-font-size: 13px;" +
                                        "-fx-font-weight: 600;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-border-color: rgba(239, 68, 68, 0.3);" +
                                        "-fx-border-radius: 10px;" +
                                        "-fx-border-width: 1px;");
                });
        }
}