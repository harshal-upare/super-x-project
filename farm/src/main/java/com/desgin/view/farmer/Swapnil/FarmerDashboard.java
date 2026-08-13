package com.desgin.view.farmer.Swapnil;

import com.desgin.view.farmer.LeftSideBar;
import com.desgin.view.farmer.ashutosh.profile.ProfileManagement;
import com.desgin.view.farmer.om.BrowseEquip;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FarmerDashboard {

        private Scene farmerDashboardScene;
        public static BorderPane borderPane;

        public Scene getfarmerDashboardScene(Runnable ref) {

                StackPane root = new StackPane();

                // Side Bar
                LeftSideBar objLeftSideBar = new LeftSideBar();
                VBox leftVB = objLeftSideBar.getSideBar(ref);

                borderPane = new BorderPane();
                borderPane.setPadding(Insets.EMPTY);

                BorderPane subroot = new BorderPane();
                subroot.setLeft(leftVB);
                subroot.setCenter(borderPane);

                root.getChildren().addAll(subroot);

                ProfileManagement objProfileManagement = new ProfileManagement();
                borderPane.setTop(objProfileManagement.getProfile(root));

                String userName = "FarmerName";
                Text welcomeText = new Text("Welcome back, " + userName + " 👋");

                welcomeText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");

                Text dashboardText = new Text("Farmer Dashboard");

                dashboardText.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #5C4033;");

                Text descriptionText = new Text("Find the right equipment for your farm");

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

                TextField searchField = new TextField();

                searchField.setPromptText("Search equipment...");

                searchField.setPrefHeight(42);
                searchField.setPrefWidth(500);

                searchField.setStyle(
                                "-fx-background-color: #FFFFFF;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-border-width: 1;" +
                                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #4A2C20;" +
                                                "-fx-prompt-text-fill: #A18C7A;");
                Text searchIcon = new Text("🔍");

                searchIcon.setStyle(
                                "-fx-font-size: 18px;");
                HBox searchBox = new HBox(
                                10,
                                searchIcon,
                                searchField);

                searchBox.setAlignment(
                                Pos.CENTER_LEFT);

                VBox equipmentCard = createDashboardCard(
                                "🚜",
                                "Available Equipment",
                                "24");
                VBox bookingCard = createDashboardCard(
                                "📅",
                                "My Bookings",
                                "5");

                VBox ratingCard = createDashboardCard(
                                "⭐",
                                "My Rating",
                                "4.8");
                HBox cards = new HBox(
                                20,
                                equipmentCard,
                                bookingCard,
                                ratingCard);

                cards.setAlignment(Pos.CENTER_LEFT);

                VBox header = new VBox(headerText);
                header.setAlignment(Pos.TOP_LEFT);
                header.setPadding(
                                new Insets(25, 30, 20, 30));

                Text recommendedTitle = new Text("Recommended Equipment");

                recommendedTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");
                VBox tractorCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/tractor.png",
                                "Tractor",
                                "₹1200 / day");

                VBox rotavatorCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/rotavator.png",
                                "Rotavator",
                                "₹800 / day");

                VBox cultivatorCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/cultivator.png",
                                "Cultivator",
                                "₹600 / day");

                VBox harvesterCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/",
                                "Harvester",
                                "₹900 / day");

                VBox seederCard = createEquipmentCard(
                                "file:farm/src/main/resources/assets/Images/",
                                "Seeder",
                                "₹400 / day");

                HBox equipmentSection = new HBox(
                                20,
                                tractorCard,
                                rotavatorCard,
                                cultivatorCard,
                                harvesterCard,
                                seederCard);
                Text viewMore = new Text("View More →");
                viewMore.setOnMouseClicked(event -> {

                        LeftSideBar.setActiveButton(LeftSideBar.equipmentBtn1, LeftSideBar.navigationButtons);
                        borderPane.setCenter(browse());
                });

                viewMore.setStyle(
                                "-fx-fill: #2e7d32;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-cursor: hand;");

                equipmentSection.getChildren().add(viewMore);

                equipmentSection.setAlignment(
                                Pos.CENTER_LEFT);
                equipmentSection.setPadding(new Insets(10, 0, 10, 0));

                ScrollPane equipmentScroll = new ScrollPane();

                equipmentScroll.setContent(equipmentSection);

                equipmentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                equipmentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                equipmentScroll.setFitToHeight(true);

                equipmentScroll.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-border-color: transparent;");

                Text activeBookingTitle = new Text("Active Booking");

                activeBookingTitle.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #4A2C20;");
                Text viewMore2 = new Text("View More →");

                viewMore2.setStyle(
                                "-fx-font-family: 'Poppins';" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-fill: #2E7D32;" +
                                                "-fx-cursor: hand;");

                
                HBox activeBookingHeader = new HBox();
                activeBookingHeader.setAlignment(Pos.CENTER_LEFT);

                Region spacer = new Region();

                HBox.setHgrow(spacer, Priority.ALWAYS);

                activeBookingHeader.getChildren().addAll(
                        activeBookingTitle,
                        spacer,
                        viewMore2
                );

                

                VBox activeBookingCard = createActiveBookingCard(
                                "Tractor",
                                "15 Aug 2026 → 17 Aug 2026",
                                "Confirmed");

                VBox activeBookingSection = new VBox(12);

                activeBookingSection.getChildren().addAll(
                        activeBookingHeader,
                        activeBookingCard
                );

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
                                searchBox,
                                cards,
                                recommendedTitle,
                                equipmentScroll,
                                activeBookingSection,
                                recentBookingTitle,
                                bookingRow);
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
                farmerDashboardScene = new Scene(root);

                return farmerDashboardScene;
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

        public ScrollPane browse() {

                BrowseEquip objBrowseEquip = new BrowseEquip();
                return objBrowseEquip.getBrowseEquip();
        }
}
