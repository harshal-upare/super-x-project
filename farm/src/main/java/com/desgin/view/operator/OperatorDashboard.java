package com.desgin.view.operator;

import com.desgin.view.operator.profile.ProfileManagement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorDashboard {

        private Scene operatorDashboardScene;

        public Scene getOperatorDashboardScene() {

                StackPane root = new StackPane();

                // Side Bar
                LeftSideBar objLeftSideBar = new LeftSideBar();
                VBox leftVB = objLeftSideBar.getSideBar();

                BorderPane borderPane = new BorderPane();
                borderPane.setPadding(Insets.EMPTY);

                BorderPane subroot = new BorderPane();
                subroot.setLeft(leftVB);
                subroot.setCenter(borderPane);

                root.getChildren().addAll(subroot);

                ProfileManagement objProfileManagement = new ProfileManagement();
                borderPane.setTop(objProfileManagement.getProfile(root));

                String userName = "OperatorName";
                Text welcomeText = new Text("Welcome back, " + userName + " 👋");

                welcomeText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text dashboardText = new Text("Operator Dashboard");

                dashboardText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #5C4033;");

                Text descriptionText = new Text("Manage your assigned jobs and keep operations running smoothly");

                descriptionText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                VBox headerText = new VBox(
                                5,
                                welcomeText,
                                dashboardText,
                                descriptionText);

                headerText.setAlignment(Pos.CENTER_LEFT);

                VBox assignedCard = createDashboardCard(
                                "📝",
                                "Assigned Jobs",
                                "24");
                VBox pendingCard = createDashboardCard(
                                "🕐",
                                "Pending Jobs",
                                "5");

                VBox activeCard = createDashboardCard(
                                "🚜",
                                "Active Jobs",
                                "2");

                VBox completedCard = createDashboardCard(
                                "✅",
                                "Completed Jobs",
                                "2");

                HBox cards = new HBox(
                                20,
                                assignedCard,
                                pendingCard,
                                activeCard,
                                completedCard);

                cards.setAlignment(Pos.CENTER_LEFT);

                VBox header = new VBox(headerText);
                header.setAlignment(Pos.TOP_LEFT);
                header.setPadding(
                                new Insets(25, 30, 20, 30));

                Text recommendedTitle = new Text("Today's Jobs");

                recommendedTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                // ================= JOB 1 =================

                VBox job1 = new VBox(10);
                job1.setPadding(new Insets(18));
                job1.setPrefWidth(250);
                job1.setPrefHeight(190);

                job1.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                HBox job1Top = new HBox();

                Text job1Equipment = new Text("🚜  Tractor");

                job1Equipment.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Region job1Spacer = new Region();

                HBox.setHgrow(
                                job1Spacer,
                                Priority.ALWAYS);

                Label job1Status = new Label("Pending");

                job1Status.setStyle(
                                "-fx-background-color: #F4D9A6;" +
                                                "-fx-text-fill: #7A5418;" +
                                                "-fx-background-radius: 15;" +
                                                "-fx-padding: 5 10;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;");

                job1Top.getChildren().addAll(
                                job1Equipment,
                                job1Spacer,
                                job1Status);

                Text job1Farmer = new Text(
                                "Farmer: Rahul Patil");

                job1Farmer.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Text job1Location = new Text(
                                "Location: Pune, Maharashtra");

                job1Location.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Text job1Date = new Text(
                                "Date: 12 Aug 2026");

                job1Date.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                HBox job1Buttons = new HBox(10);

                Button acceptButton = new Button("✓ Accept");
                Button rejectButton = new Button("✕ Reject");

                acceptButton.setStyle(
                                "-fx-background-color: #04ba10;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                rejectButton.setStyle(
                                "-fx-background-color: #ce2909;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                job1Buttons.getChildren().addAll(
                                acceptButton,
                                rejectButton);

                job1.getChildren().addAll(
                                job1Top,
                                job1Farmer,
                                job1Location,
                                job1Date,
                                job1Buttons);

                // ================= JOB 2 =================

                VBox job2 = new VBox(10);

                job2.setPadding(new Insets(18));
                job2.setPrefWidth(250);
                job2.setPrefHeight(190);

                job2.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                HBox job2Top = new HBox();

                Text job2Equipment = new Text("🚜  Rotavator");

                job2Equipment.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Region job2Spacer = new Region();

                HBox.setHgrow(
                                job2Spacer,
                                Priority.ALWAYS);

                Label job2Status = new Label("Accepted");

                job2Status.setStyle(
                                "-fx-background-color: #D8E6C5;" +
                                                "-fx-text-fill: #4F6832;" +
                                                "-fx-background-radius: 15;" +
                                                "-fx-padding: 5 10;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;");

                job2Top.getChildren().addAll(
                                job2Equipment,
                                job2Spacer,
                                job2Status);

                Text job2Farmer = new Text(
                                "Farmer: Amit Shinde");

                job2Farmer.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Text job2Location = new Text(
                                "Location: Baramati, Pune");

                job2Location.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Text job2Date = new Text(
                                "Date: 13 Aug 2026");

                job2Date.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Button startButton = new Button("▶ Start Job");

                startButton.setStyle(
                                "-fx-background-color: #04ba10;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                HBox job2Buttons = new HBox(
                                startButton);

                job2.getChildren().addAll(
                                job2Top,
                                job2Farmer,
                                job2Location,
                                job2Date,
                                job2Buttons);

                // ================= JOB 3 =================

                VBox job3 = new VBox(10);

                job3.setPadding(new Insets(18));
                job3.setPrefWidth(250);
                job3.setPrefHeight(190);

                job3.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                HBox job3Top = new HBox();

                Text job3Equipment = new Text("🚜  Cultivator");

                job3Equipment.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Region job3Spacer = new Region();

                HBox.setHgrow(
                                job3Spacer,
                                Priority.ALWAYS);

                Label job3Status = new Label("Active");

                job3Status.setStyle(
                                "-fx-background-color: #C9DDE8;" +
                                                "-fx-text-fill: #315B70;" +
                                                "-fx-background-radius: 15;" +
                                                "-fx-padding: 5 10;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;");

                job3Top.getChildren().addAll(
                                job3Equipment,
                                job3Spacer,
                                job3Status);

                Text job3Farmer = new Text(
                                "Farmer: Suresh Jadhav");

                job3Farmer.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Text job3Location = new Text(
                                "Location: Nashik, Maharashtra");

                job3Location.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Text job3Date = new Text(
                                "Date: 14 Aug 2026");

                job3Date.setStyle(
                                "-fx-font-size: 12px;" +
                                                "-fx-fill: #806B5D;");

                Button completeButton = new Button("✓ Complete");

                completeButton.setStyle(
                                "-fx-background-color: #04ba10;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                HBox job3Buttons = new HBox(
                                completeButton);

                job3.getChildren().addAll(
                                job3Top,
                                job3Farmer,
                                job3Location,
                                job3Date,
                                job3Buttons);

                // ================= ALL JOBS =================

                HBox equipmentSection = new HBox(
                                20,
                                job1,
                                job2,
                                job3);

                equipmentSection.setAlignment(
                                Pos.CENTER_LEFT);

                Text activeBookingTitle = new Text("Active Booking");

                activeBookingTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text notificationTitle = new Text("Recent Notifications");

                notificationTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");
                HBox notification1 = createNotificationRow(
                                "🔔",
                                "Your tractor booking has been confirmed");

                HBox notification2 = createNotificationRow(
                                "⭐",
                                "You received a new review");
                VBox notifications = new VBox(
                                10,
                                notification1,
                                notification2);

                VBox activeBookingCard = createActiveBookingCard(
                                "Tractor",
                                "15 Aug 2026 → 17 Aug 2026",
                                "Confirmed");

                Text recentBookingTitle = new Text("Recent Bookings");

                recentBookingTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");
                HBox bookingRow = createBookingRow(
                                "Tractor",
                                "12 Aug 2026",
                                "Confirmed");

                VBox centerContent = new VBox(
                                20,
                                header,
                                cards,
                                recommendedTitle,
                                equipmentSection,
                                activeBookingTitle,
                                activeBookingCard,
                                recentBookingTitle,
                                bookingRow,
                                notifications);
                centerContent.setPadding(new Insets(20, 30, 30, 30));
                ScrollPane scrollPane = new ScrollPane();

                scrollPane.setContent(centerContent);

                scrollPane.setFitToWidth(true);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle("-fx-background-color: transparent;" + "-fx-background: transparent;");
                borderPane.setCenter(scrollPane);

                root.setStyle("-fx-background-color: #EDE3D5;");

                subroot.prefWidthProperty().bind(root.widthProperty());
                subroot.prefHeightProperty().bind(root.heightProperty());
                operatorDashboardScene = new Scene(root);

                return operatorDashboardScene;
        }

        private VBox createDashboardCard(String icon, String title, String value) {

                Text iconText = new Text(icon);
                iconText.setStyle("-fx-font-size: 25px;");

                Text titleText = new Text(title);
                titleText.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                Text valueText = new Text(value);
                valueText.setStyle(
                                "-fx-font-size: 24px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                VBox card = new VBox(
                                8,
                                iconText,
                                titleText,
                                valueText);

                card.setPrefWidth(220);
                card.setPrefHeight(135);

                card.setPadding(new Insets(18));

                card.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        private VBox createEquipmentCard(
                        String imagePath,
                        String equipmentName,
                        String price) {

                Image equipmentImage = new Image(imagePath);

                ImageView imageView = new ImageView(equipmentImage);

                imageView.setFitWidth(196);
                imageView.setFitHeight(110);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                StackPane imageBox = new StackPane(imageView);

                imageBox.setPrefHeight(110);
                imageBox.setMaxWidth(Double.MAX_VALUE);

                imageBox.setStyle(
                                "-fx-background-color: #E4D3C2;" +
                                                "-fx-background-radius: 10;");

                Text nameText = new Text(equipmentName);

                nameText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text priceText = new Text(price);

                priceText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");
                Button viewButton = new Button("View Details");

                viewButton.setPrefWidth(196);
                viewButton.setPrefHeight(35);

                viewButton.setStyle(
                                "-fx-background-color: #8B6F47;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                viewButton.setOnMouseEntered(e -> {

                        viewButton.setStyle(
                                        "-fx-background-color: #6F5638;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-cursor: hand;");
                });

                viewButton.setOnMouseExited(e -> {

                        viewButton.setStyle(
                                        "-fx-background-color: #8B6F47;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-family: 'Poppins';" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-cursor: hand;");
                });

                VBox card = new VBox(
                                10,
                                imageBox,
                                nameText,
                                priceText,
                                viewButton);

                card.setPrefWidth(220);
                card.setPrefHeight(210);

                card.setPadding(
                                new Insets(12));

                card.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        private HBox createBookingRow(
                        String equipmentName,
                        String date,
                        String status) {

                Text equipmentText = new Text(equipmentName);

                equipmentText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text dateText = new Text(date);

                dateText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                Text statusText = new Text(status);

                statusText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #6B7D45;");

                VBox equipmentBox = new VBox(equipmentText);

                VBox dateBox = new VBox(dateText);

                VBox statusBox = new VBox(statusText);

                HBox row = new HBox(
                                100,
                                equipmentBox,
                                dateBox,
                                statusBox);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                row.setPadding(
                                new Insets(15));

                row.setPrefHeight(55);

                row.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 10;");

                return row;
        }

        private VBox createActiveBookingCard(
                        String equipmentName,
                        String bookingDate,
                        String status) {

                Text equipmentText = new Text("🚜  " + equipmentName);

                equipmentText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text dateText = new Text(bookingDate);

                dateText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #806A5B;");

                Text statusText = new Text("Status: " + status);

                statusText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #6B7D45;");

                Button viewButton = new Button("View Booking");

                viewButton.setPrefWidth(130);
                viewButton.setPrefHeight(35);

                viewButton.setStyle(
                                "-fx-background-color: #8B6F47;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                viewButton.setOnAction(e -> {

                        System.out.println("View Booking clicked");

                });

                VBox card = new VBox(
                                10,
                                equipmentText,
                                dateText,
                                statusText,
                                viewButton);

                card.setPadding(
                                new Insets(18));

                card.setPrefHeight(150);

                card.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 14;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 14;");

                return card;
        }

        private HBox createNotificationRow(
                        String icon,
                        String message) {

                Text iconText = new Text(icon);

                iconText.setStyle(
                                "-fx-font-size: 18px;");

                Text messageText = new Text(message);

                messageText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-fill: #5C4033;");

                HBox row = new HBox(
                                12,
                                iconText,
                                messageText);

                row.setAlignment(
                                Pos.CENTER_LEFT);

                row.setPadding(
                                new Insets(12, 15, 12, 15));

                row.setPrefHeight(45);

                row.setStyle(
                                "-fx-background-color: #F5EFE6;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-border-radius: 10;");

                return row;
        }

}
