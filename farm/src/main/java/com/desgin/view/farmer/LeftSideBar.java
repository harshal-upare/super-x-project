package com.desgin.view.farmer;

import java.util.ArrayList;
import java.util.List;

import com.desgin.view.farmer.Swapnil.FarmerDashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LeftSideBar {

    private static Button activeButton;

    public static List<Button> navigationButtons = new ArrayList<>();

    public static Button dashboardBtn1;
    public static Button equipmentBtn1;
    public static Button bookingBtn1;
    public static Button wishlistBtn1;
    public static Button reviewBtn1;
    public static Button settingsBtn1;
    public static Button supportBtn1;

    /*
     * Runnable is used for Logout / Back action
     */
    public VBox getSideBar(Runnable ref) {

        // --------------------------------------------------
        // LOGO
        // --------------------------------------------------

        Image logoImage = new Image(
                "file:farm/src/main/resources/assets/Images/logo.png"
        );

        ImageView logoImageView = new ImageView(logoImage);

        logoImageView.setFitWidth(48);
        logoImageView.setFitHeight(48);
        logoImageView.setPreserveRatio(false);
        logoImageView.setSmooth(true);


        // --------------------------------------------------
        // FARM EQUIP TEXT
        // --------------------------------------------------

        Text textName = new Text("FarmEquip");

        textName.setStyle(
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-fill: #4A2C20;"
        );


        // --------------------------------------------------
        // DASHBOARD BUTTON
        // --------------------------------------------------

        dashboardBtn1 = new Button("⌂  Dashboard");

        styleMenuButton(dashboardBtn1);

        HBox btnBox1 = new HBox(
                5,
                dashboardBtn1
        );


        // --------------------------------------------------
        // EQUIPMENT BUTTON
        // --------------------------------------------------

        equipmentBtn1 = new Button("⚒  Browse Equipment");

        styleMenuButton(equipmentBtn1);

        HBox btnBox2 = new HBox(
                5,
                equipmentBtn1
        );


        // --------------------------------------------------
        // BOOKING BUTTON
        // --------------------------------------------------

        bookingBtn1 = new Button("📅  My Bookings");

        styleMenuButton(bookingBtn1);

        HBox btnBox3 = new HBox(
                5,
                bookingBtn1
        );


        // --------------------------------------------------
        // WISHLIST BUTTON
        // --------------------------------------------------

        wishlistBtn1 = new Button("♥  My Wishlist");

        styleMenuButton(wishlistBtn1);

        HBox btnBox4 = new HBox(
                5,
                wishlistBtn1
        );


        // --------------------------------------------------
        // REVIEW BUTTON
        // --------------------------------------------------

        reviewBtn1 = new Button("⭐  Reviews");

        styleMenuButton(reviewBtn1);

        HBox btnBox6 = new HBox(
                5,
                reviewBtn1
        );


        // --------------------------------------------------
        // MAIN NAVIGATION BUTTONS
        // --------------------------------------------------

        VBox vBoxBtn1 = new VBox(
                10,
                btnBox1,
                btnBox2,
                btnBox3,
                btnBox4,
                btnBox6
        );


        // --------------------------------------------------
        // SETTINGS BUTTON
        // --------------------------------------------------

        settingsBtn1 = new Button("⚙  Settings");

        styleMenuButton(settingsBtn1);

        HBox btnBox7 = new HBox(
                5,
                settingsBtn1
        );


        // --------------------------------------------------
        // HELP & SUPPORT BUTTON
        // --------------------------------------------------

        supportBtn1 = new Button("❓ Help & Support");

        styleMenuButton(supportBtn1);

        HBox btnBox8 = new HBox(
                5,
                supportBtn1
        );


        // --------------------------------------------------
        // LOGOUT BUTTON
        // --------------------------------------------------

        Button logoutBtn1 = new Button("↪  Logout");

        styleLogoutButton(logoutBtn1);

        logoutBtn1.setOnAction(e -> {

            if (ref != null) {
                ref.run();
            }

        });

        HBox btnBox9 = new HBox(
                5,
                logoutBtn1
        );


        // --------------------------------------------------
        // BOTTOM BUTTONS
        // --------------------------------------------------

        VBox vBoxBtn2 = new VBox(
                10,
                btnBox7,
                btnBox8,
                btnBox9
        );


        // --------------------------------------------------
        // SPACER
        // --------------------------------------------------

        Region spacer = new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );


        VBox btnBox = new VBox(
                10,
                vBoxBtn1,
                spacer,
                vBoxBtn2
        );


        // --------------------------------------------------
        // LOGO + NAME
        // --------------------------------------------------

        HBox logoTextHBox = new HBox(
                10,
                logoImageView,
                textName
        );

        logoTextHBox.setAlignment(
                Pos.CENTER_LEFT
        );

        logoTextHBox.setPadding(
                new Insets(0, 5, 10, 5)
        );


        // --------------------------------------------------
        // COMPLETE SIDEBAR
        // --------------------------------------------------

        VBox leftVB = new VBox(
                logoTextHBox,
                btnBox
        );

        leftVB.setPrefWidth(260);
        leftVB.setMinWidth(260);
        leftVB.setMaxWidth(260);

        leftVB.setMaxHeight(
                Double.MAX_VALUE
        );

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


        // --------------------------------------------------
        // NAVIGATION LIST
        // --------------------------------------------------

        navigationButtons.clear();

        navigationButtons.add(dashboardBtn1);
        navigationButtons.add(equipmentBtn1);
        navigationButtons.add(bookingBtn1);
        navigationButtons.add(wishlistBtn1);
        navigationButtons.add(reviewBtn1);
        navigationButtons.add(settingsBtn1);
        navigationButtons.add(supportBtn1);


        // --------------------------------------------------
        // DEFAULT ACTIVE BUTTON
        // --------------------------------------------------

        setActiveButton(
                dashboardBtn1,
                navigationButtons
        );


        // --------------------------------------------------
        // DASHBOARD ACTION
        // --------------------------------------------------

        dashboardBtn1.setOnAction(event -> {

            setActiveButton(
                    dashboardBtn1,
                    navigationButtons
            );

            FarmerDashboard obj = new FarmerDashboard();

            /*
             * Change scene back to Farmer Dashboard
             */
            if (ref != null) {

                // You can replace this with your own
                // dashboard navigation logic.

                System.out.println(
                        "Dashboard clicked"
                );
            }

        });


        // --------------------------------------------------
        // EQUIPMENT ACTION
        // --------------------------------------------------

        equipmentBtn1.setOnAction(event -> {

            setActiveButton(
                    equipmentBtn1,
                    navigationButtons
            );

            FarmerDashboard obj =new FarmerDashboard();
            obj.browse();

        });


        // --------------------------------------------------
        // BOOKING ACTION
        // --------------------------------------------------

        bookingBtn1.setOnAction(event -> {

            setActiveButton(
                    bookingBtn1,
                    navigationButtons
            );

            System.out.println(
                    "My Bookings clicked"
            );

        });


        // --------------------------------------------------
        // WISHLIST ACTION
        // --------------------------------------------------

        wishlistBtn1.setOnAction(event -> {

            setActiveButton(
                    wishlistBtn1,
                    navigationButtons
            );

            System.out.println(
                    "Wishlist clicked"
            );

        });


        // --------------------------------------------------
        // REVIEW ACTION
        // --------------------------------------------------

        reviewBtn1.setOnAction(event -> {

            setActiveButton(
                    reviewBtn1,
                    navigationButtons
            );

            System.out.println(
                    "Reviews clicked"
            );

        });


        // --------------------------------------------------
        // SETTINGS ACTION
        // --------------------------------------------------

        settingsBtn1.setOnAction(event -> {

            setActiveButton(
                    settingsBtn1,
                    navigationButtons
            );

            System.out.println(
                    "Settings clicked"
            );

        });


        // --------------------------------------------------
        // SUPPORT ACTION
        // --------------------------------------------------

        supportBtn1.setOnAction(event -> {

            setActiveButton(
                    supportBtn1,
                    navigationButtons
            );

            System.out.println(
                    "Help & Support clicked"
            );

        });


        return leftVB;
    }


    // ======================================================
    // ACTIVE BUTTON
    // ======================================================

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
                    "-fx-cursor: hand;"
            );
        }


        selected.setStyle(
                "-fx-background-color: #E4D3C2;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );
    }


    // ======================================================
    // NORMAL MENU BUTTON STYLE
    // ======================================================

    private void styleMenuButton(Button button) {

        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(48);
        button.setMinHeight(48);
        button.setMaxHeight(48);

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setPadding(
                new Insets(0, 15, 0, 15)
        );


        // --------------------------------------------------
        // NORMAL
        // --------------------------------------------------

        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: normal;" +
                "-fx-cursor: hand;"
        );


        // --------------------------------------------------
        // HOVER
        // --------------------------------------------------

        button.setOnMouseEntered(e -> {

            if (button != activeButton) {

                button.setStyle(
                        "-fx-background-color: #E4D3C2;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                );
            }
        });


        // --------------------------------------------------
        // MOUSE EXIT
        // --------------------------------------------------

        button.setOnMouseExited(e -> {

            if (button != activeButton) {

                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #5C4033;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: normal;" +
                        "-fx-cursor: hand;"
                );
            }
        });
    }


    // ======================================================
    // LOGOUT BUTTON STYLE
    // ======================================================

    private void styleLogoutButton(Button button) {

        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(48);
        button.setMinHeight(48);
        button.setMaxHeight(48);

        button.setAlignment(
                Pos.CENTER_LEFT
        );

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
                javafx.scene.paint.Color.web(
                        "#FFFFFF"
                )
        );

        button.setStyle(
                "-fx-background-color: #8B3A3A;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
        );


        // --------------------------------------------------
        // HOVER
        // --------------------------------------------------

        button.setOnMouseEntered(e -> {

            button.setStyle(
                    "-fx-background-color: #A94442;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
            );

        });


        // --------------------------------------------------
        // EXIT
        // --------------------------------------------------

        button.setOnMouseExited(e -> {

            button.setStyle(
                    "-fx-background-color: #8B3A3A;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
            );

        });
    }
}