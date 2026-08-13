package com.desgin.view.farmer.ashutosh.profile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProfileManagement {

        public HBox getProfile(StackPane root) {

                Button notificationBtn1 = new Button("🔔  Notifications");
                notificationBtn1.setPadding(new Insets(0, 15, 0, 15));
                notificationBtn1.setStyle("-fx-background-color: transparent;" + "-fx-background-radius: 10;"
                        + "-fx-border-width: 0;"
                        + "-fx-border-color: transparent;" + "-fx-text-fill: #5C4033;"
                        + "-fx-font-family: 'Poppins';"
                        + "-fx-font-size: 20px;" + "-fx-font-weight: normal;"
                        + "-fx-cursor: hand;");
                hoverElement(notificationBtn1);
                
                Text profileIcon = new Text("👤");
                profileIcon.setStyle("-fx-font-size: 28px;");

                Text profileName = new Text("Harshal");
                profileName.setStyle("-fx-font-family: 'Poppins';" 
                                + "-fx-font-size: 14px;" 
                                + "-fx-font-weight: bold;"
                                + "-fx-fill: #4A2C20;");

                Text profileRole = new Text("Farmer");
                profileRole.setStyle("-fx-font-family: 'Poppins';" 
                                + "-fx-font-size: 11px;"
                                + "-fx-fill: #806A5B;");

                VBox profileText = new VBox(2, profileName, profileRole);
                profileText.setAlignment(Pos.CENTER_LEFT);

                HBox profileBox = new HBox(10, profileIcon, profileText);

                Button closeButton = new Button("✕");

                // closeButton.setAlignment(Pos.TOP_RIGHT);
                VBox profilePopup = new VBox(closeButton, profilePopup());
                profilePopup.setAlignment(Pos.TOP_RIGHT);
                profilePopup.setPadding(new Insets(10));
                profilePopup.setPrefSize(500, 700);
                profilePopup.setMaxSize(500, 700);

                profilePopup.setStyle(
                                "-fx-background-color: #FFFDF9;" 
                                + "-fx-background-radius: 15;" 
                                + "-fx-border-color: #D8C7B5;" 
                                + "-fx-border-radius: 15;" 
                                + "-fx-border-width: 1;");

                StackPane.setAlignment(
                                profilePopup,
                                Pos.TOP_RIGHT);
                StackPane.setMargin(
                                profilePopup,
                                new Insets(80, 25, 0, 0));

                profilePopup.setVisible(false);

                root.getChildren().addAll(profilePopup);

                hoverElement(profileBox);
                profileBox.setAlignment(Pos.CENTER_LEFT);
                profileBox.setPadding(new Insets(10));
                profileBox.setStyle("-fx-background-color: #EDE3D5;"
                                + "-fx-background-radius: 10;" 
                                + "-fx-padding: 20px 20px;");

                profileBox.setOnMouseClicked(event -> {

                        closeButton.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;");

                        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
                                        "-fx-background-color: #c0392b;" 
                                        + "-fx-text-fill: white;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;"
                                        + "-fx-cursor: hand;"));

                        closeButton.setOnMouseExited(e -> closeButton.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;"));

                        profilePopup.setVisible(true);
                });
                closeButton.setOnMouseClicked(event -> {

                        closeButton.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;");

                        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
                                        "-fx-background-color: #c0392b;" 
                                        + "-fx-text-fill: white;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;"
                                        + "-fx-cursor: hand;"));

                        closeButton.setOnMouseExited(e -> closeButton.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;"));
                                        
                        profilePopup.setVisible(false);
                });

                HBox rightHBox = new HBox(notificationBtn1, profileBox);
                rightHBox.setAlignment(Pos.CENTER_RIGHT);

                Button closeButton2 = new Button("✕");

                // closeButton.setAlignment(Pos.TOP_RIGHT);
                VBox notificationPopUp = new VBox(closeButton2, getNotification());
                notificationPopUp.setAlignment(Pos.TOP_RIGHT);
                notificationPopUp.setPadding(new Insets(10));

                notificationPopUp.setPrefSize(500, 450);
                // profilePopup.setMinSize(350, 250);
                notificationPopUp.setMaxSize(500, 450);

                notificationPopUp.setStyle(
                                "-fx-background-color: #FFFDF9;" 
                                + "-fx-background-radius: 15;" 
                                + "-fx-border-color: #D8C7B5;" 
                                + "-fx-border-radius: 15;" 
                                + "-fx-border-width: 1;");

                StackPane.setAlignment(
                                notificationPopUp,
                                Pos.TOP_RIGHT);
                StackPane.setMargin(
                                notificationPopUp,
                                new Insets(80, 25, 0, 0));
                notificationPopUp.setVisible(false);

                root.getChildren().addAll(notificationPopUp);

                hoverElement(notificationBtn1);
                notificationBtn1.setAlignment(Pos.CENTER_LEFT);
                notificationBtn1.setPadding(new Insets(10));
                notificationBtn1.setStyle("-fx-background-color: #EDE3D5;"
                                + "-fx-background-radius: 10;"
                                + "-fx-padding: 20px 20px;");

                notificationBtn1.setOnMouseClicked(event -> {
                        closeButton2.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;");

                        closeButton2.setOnMouseEntered(e -> closeButton.setStyle(
                                        "-fx-background-color: #c0392b;" 
                                        + "-fx-text-fill: white;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;"
                                        + "-fx-cursor: hand;"));

                        closeButton2.setOnMouseExited(e -> closeButton.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;"));
                                        
                        notificationPopUp.setVisible(true);
                });
                closeButton2.setOnMouseClicked(event -> {
                        closeButton2.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;");

                        closeButton2.setOnMouseEntered(e -> closeButton.setStyle(
                                        "-fx-background-color: #c0392b;" 
                                        + "-fx-text-fill: white;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;"
                                        + "-fx-cursor: hand;"));

                        closeButton2.setOnMouseExited(e -> closeButton.setStyle(
                                        "-fx-background-color: transparent;" 
                                        + "-fx-text-fill: red;" 
                                        + "-fx-font-size: 18px;" 
                                        + "-fx-font-weight: bold;" 
                                        + "-fx-cursor: hand;"));
                                        
                        notificationPopUp.setVisible(false);
                });

                return rightHBox;
        }

        static void hoverElement(Node node) {

                node.setOnMouseEntered(e -> {

                        node.setTranslateY(-2);
                        node.setOpacity(0.75);

                });

                node.setOnMouseExited(e -> {

                        node.setTranslateY(0);
                        node.setOpacity(1.0);

                });
        }

        public static ScrollPane profilePopup() {

                VBox profileContent = new VBox(20);
                profileContent.setPadding(new Insets(30));
                profileContent.setStyle(
                                "-fx-background-color: #f5f7f5;");

                HBox profileHeader = new HBox(15);
                profileHeader.setAlignment(Pos.CENTER_LEFT);

                Text profileIcon = new Text("👤");
                profileIcon.setStyle(
                                "-fx-font-size: 35px;");

                Text profileTitle = new Text("My Profile");
                profileTitle.setStyle(
                                "-fx-font-size: 28px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-fill: #263238;");

                VBox titleBox = new VBox(3);

                Text subtitle = new Text("Manage your personal and farm information");
                subtitle.setStyle(
                                "-fx-font-size: 13px;" 
                                + "-fx-fill: #78909c;");

                titleBox.getChildren().addAll(profileTitle, subtitle);

                profileHeader.getChildren().addAll(profileIcon, titleBox);

                Text personalTitle = new Text("Personal Information");
                personalTitle.setStyle(
                                "-fx-font-size: 18px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-fill: #37474f;");

                TextField nameField = new TextField("Harshal");
                nameField.setPromptText("Full Name");

                TextField emailField = new TextField("harshal@gmail.com");
                emailField.setPromptText("Email");

                TextField phoneField = new TextField();
                phoneField.setPromptText("Mobile Number");

                styleField(nameField);
                styleField(emailField);
                styleField(phoneField);

                GridPane personalGrid = new GridPane();
                personalGrid.setHgap(20);
                personalGrid.setVgap(15);

                personalGrid.add(createFieldBox("Full Name", nameField), 0, 0);
                personalGrid.add(createFieldBox("Email", emailField), 1, 0);
                personalGrid.add(createFieldBox("Mobile Number", phoneField), 0, 1);

                Text locationTitle = new Text("Location");
                locationTitle.setStyle(
                                "-fx-font-size: 18px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-fill: #37474f;");

                TextField villageField = new TextField();
                villageField.setPromptText("Village");

                TextField districtField = new TextField();
                districtField.setPromptText("District");

                TextField stateField = new TextField("Maharashtra");
                stateField.setPromptText("State");

                TextField pincodeField = new TextField();
                pincodeField.setPromptText("Pincode");

                styleField(villageField);
                styleField(districtField);
                styleField(stateField);
                styleField(pincodeField);

                GridPane locationGrid = new GridPane();
                locationGrid.setHgap(20);
                locationGrid.setVgap(15);

                locationGrid.add(createFieldBox("Village", villageField), 0, 0);
                locationGrid.add(createFieldBox("District", districtField), 1, 0);
                locationGrid.add(createFieldBox("State", stateField), 0, 1);
                locationGrid.add(createFieldBox("Pincode", pincodeField), 1, 1);

                Text farmTitle = new Text("Farm Information");
                farmTitle.setStyle(
                                "-fx-font-size: 18px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-fill: #37474f;");

                TextField farmSizeField = new TextField();
                farmSizeField.setPromptText("Farm Size (acres)");

                TextField cropsField = new TextField();
                cropsField.setPromptText("Primary Crops");

                TextField irrigationField = new TextField();
                irrigationField.setPromptText("Irrigation Type");

                TextField soilField = new TextField();
                soilField.setPromptText("Soil Type");

                TextField equipmentField = new TextField();
                equipmentField.setPromptText("Preferred Equipment");

                styleField(farmSizeField);
                styleField(cropsField);
                styleField(irrigationField);
                styleField(soilField);
                styleField(equipmentField);

                GridPane farmGrid = new GridPane();
                farmGrid.setHgap(20);
                farmGrid.setVgap(15);

                farmGrid.add(createFieldBox("Farm Size", farmSizeField), 0, 0);
                farmGrid.add(createFieldBox("Primary Crops", cropsField), 1, 0);
                farmGrid.add(createFieldBox("Irrigation Type", irrigationField), 0, 1);
                farmGrid.add(createFieldBox("Soil Type", soilField), 1, 1);
                farmGrid.add(createFieldBox("Preferred Equipment", equipmentField), 0, 2);

                Text accountTitle = new Text("Account Settings");
                accountTitle.setStyle(
                                "-fx-font-size: 18px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-fill: #37474f;");

                PasswordField passwordField = new PasswordField();
                passwordField.setPromptText("New Password");

                styleField(passwordField);

                Button saveButton = new Button("Save Changes");
                saveButton.setPrefWidth(150);
                saveButton.setPrefHeight(40);

                saveButton.setStyle(
                                "-fx-background-color: #2e7d32;" 
                                + "-fx-text-fill: white;" 
                                + "-fx-font-size: 14px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-background-radius: 8px;" 
                                + "-fx-cursor: hand;");

                Button changePasswordButton = new Button("Change Password");
                changePasswordButton.setPrefWidth(150);
                changePasswordButton.setPrefHeight(40);

                changePasswordButton.setStyle(
                                "-fx-background-color: #ffffff;" 
                                + "-fx-text-fill: #2e7d32;" 
                                + "-fx-font-size: 14px;" 
                                + "-fx-font-weight: bold;"
                                + "-fx-border-color: #2e7d32;" 
                                + "-fx-border-radius: 8px;" 
                                + "-fx-background-radius: 8px;" 
                                + "-fx-cursor: hand;");

                Button logoutButton = new Button("Logout");
                logoutButton.setPrefWidth(100);
                logoutButton.setPrefHeight(40);

                logoutButton.setStyle(
                                "-fx-background-color: #ffffff;" 
                                + "-fx-text-fill: #d32f2f;" 
                                + "-fx-font-size: 14px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-border-color: #d32f2f;" 
                                + "-fx-border-radius: 8px;" 
                                + "-fx-background-radius: 8px;" 
                                + "-fx-cursor: hand;");

                HBox buttonBox = new HBox(15);
                buttonBox.setAlignment(Pos.CENTER_LEFT);

                buttonBox.getChildren().addAll(
                                saveButton,
                                changePasswordButton,
                                logoutButton);

                profileContent.getChildren().addAll(
                                profileHeader,

                                personalTitle,
                                personalGrid,

                                locationTitle,
                                locationGrid,

                                farmTitle,
                                farmGrid,

                                accountTitle,
                                passwordField,

                                buttonBox);

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(profileContent);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: #f5f7f5;");

                return scrollPane;
        }

        private static void styleField(TextField field) {

                field.setPrefHeight(42);
                field.setPrefWidth(280);

                field.setStyle(
                                "-fx-background-color: white;" 
                                + "-fx-border-color: #d5ddd5;" 
                                + "-fx-border-radius: 7px;" 
                                + "-fx-background-radius: 7px;" 
                                + "-fx-padding: 0 12px;" 
                                + "-fx-font-size: 14px;");
        }

        private static VBox createFieldBox(String labelText, TextField field) {

                Label label = new Label(labelText);

                label.setStyle(
                                "-fx-font-size: 13px;" 
                                + "-fx-font-weight: bold;" 
                                + "-fx-text-fill: #546e7a;");

                VBox box = new VBox(6);
                box.getChildren().addAll(label, field);

                return box;
        }

        public static VBox getNotification() {

        HBox header = new HBox();
        
        Button notificationBtn1 = new Button("🔔  Notifications");

        notificationBtn1.setPadding(
                new Insets(0, 15, 0, 15)
        );

        notificationBtn1.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-border-width: 0;" +
                "-fx-border-color: transparent;" +
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: normal;" +
                "-fx-cursor: hand;"
        );
        

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button markAllRead = new Button("Mark all as read");

        markAllRead.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #2e7d32;" +
                "-fx-font-size: 12px;" +
                "-fx-cursor: hand;");

        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(
                notificationBtn1,
                spacer,
                markAllRead);

        VBox notificationList = new VBox(10);

        notificationList.setPadding(new Insets(5));

        notificationList.getChildren().addAll(

                createNotification(
                        "Booking Confirmed",
                        "Your tractor booking has been confirmed.",
                        "10 min ago"),

                createNotification(
                        "New Equipment Available",
                        "A new tractor is available near your location.",
                        "1 hour ago"),

                createNotification(
                        "Booking Reminder",
                        "Your equipment rental starts tomorrow.",
                        "2 hours ago"),

                createNotification(
                        "Booking Accepted",
                        "The equipment provider accepted your request.",
                        "Yesterday"));

        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(notificationList);
        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(450);
        scrollPane.setPrefWidth(500);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;");

        VBox notificationContentVBox = new VBox(15);
        // notificationContentVBox.setPadding(new Insets(20));
        notificationContentVBox.setPrefWidth(500);

        notificationContentVBox.getChildren().addAll(
                header,
                scrollPane);

        return notificationContentVBox;
    }
     private static HBox createNotification(
            String title,
            String message,
            String time) {

        Text notificationTitle = new Text(title);

        notificationTitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #263238;");

        Text notificationMessage = new Text(message);

        notificationMessage.setWrappingWidth(300);

        notificationMessage.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-fill: #607d8b;");

        Text notificationTime = new Text(time);

        notificationTime.setStyle(
                "-fx-font-size: 10px;" +
                        "-fx-fill: #9e9e9e;");

        VBox textBox = new VBox(4);

        textBox.getChildren().addAll(
                notificationTitle,
                notificationMessage,
                notificationTime);

        HBox notification = new HBox(textBox);

        notification.setPadding(new Insets(12));

        notification.setPrefWidth(380);

        notification.setStyle(
                "-fx-background-color: #f7faf7;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: #e0e8e0;" +
                "-fx-border-radius: 10px;");


        return notification;
    }

}