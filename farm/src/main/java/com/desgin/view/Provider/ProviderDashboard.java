package com.desgin.view.Provider;

import com.desgin.view.farmer.LeftSideBar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
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

public class ProviderDashboard {
    
    private Scene providerDashboardScene;

    public Scene getProviderDashboardScene() {

        StackPane root = new StackPane();

        // ---------------- LEFT SIDEBAR ----------------

        LeftSideBar sideBar = new LeftSideBar();
        VBox leftVB = sideBar.getSideBar();

        BorderPane borderPane = new BorderPane();

        BorderPane subRoot = new BorderPane();
        subRoot.setLeft(leftVB);
        subRoot.setCenter(borderPane);

        root.getChildren().add(subRoot);

        // ---------------- TOP PROFILE ----------------

        ProfileManagement objProfileManagement = new ProfileManagement();
        borderPane.setTop(objProfileManagement.getProfile(root));

        // ---------------- HEADER ----------------

        String providerName = "Pratik";

        Text welcomeText =
                new Text("Welcome back, " + providerName + " 👋");

        Text dashboardTitle =
                new Text("Provider Dashboard");

        Text description =
                new Text("Manage your equipment and bookings efficiently.");

        VBox headerText = new VBox(
                5,
                welcomeText,
                dashboardTitle,
                description
        );

        // ---------------- SEARCH BAR ----------------

        TextField searchField = new TextField();
        searchField.setPromptText("Search Equipment...");

        Text searchIcon = new Text("🔍");

        HBox searchBox = new HBox(
                10,
                searchIcon,
                searchField
        );

        // ---------------- DASHBOARD CARDS ----------------

        VBox equipmentCard =
                createDashboardCard(
                        "🚜",
                        "Total Equipment",
                        "24"
                );

        VBox rentalCard =
                createDashboardCard(
                        "📅",
                        "Active Rentals",
                        "8"
                );

        VBox earningCard =
                createDashboardCard(
                        "💰",
                        "Today's Earnings",
                        "₹6,500"
                );

        VBox ratingCard =
                createDashboardCard(
                        "⭐",
                        "Customer Rating",
                        "4.9"
                );

        HBox dashboardCards =
                new HBox(
                        20,
                        equipmentCard,
                        rentalCard,
                        earningCard,
                        ratingCard
                );

        // ---------------- MY EQUIPMENT ----------------

        Text equipmentTitle =
                new Text("My Equipment");
                VBox tractor = 
       createEquipmentCard(
                "file:assets/Images/tractor.png",
                "Tractor",
                "Available",
                "₹1500 / Day"
);

        VBox rotavator =
        createEquipmentCard(
                "file:assets/Images/rotavator.png",
                "Rotavator",
                "Currently Rented",
                "₹900 / Day"
        );

        VBox cultivator =
        createEquipmentCard(
                "file:assets/Images/cultivator.png",
                "Cultivator",
                "Maintenance",
                "₹700 / Day"
        );

        HBox equipmentSection =
                new HBox(
                        20,
                        tractor,
                        rotavator,
                        cultivator
                );

        // ---------------- BOOKING REQUESTS ----------------

        Text bookingTitle =
                new Text("Booking Requests");

        VBox bookingRequests =
                new VBox(
                        10,
                        createBookingRequestRow(
                                "Rahul",
                                "Tractor",
                                "15 Aug",
                                "Pending"
                        ),
                        createBookingRequestRow(
                                "Amit",
                                "Cultivator",
                                "16 Aug",
                                "Pending"
                        )
                );

        // ---------------- CURRENT RENTALS ----------------

        Text rentalTitle =
                new Text("Current Rentals");

       VBox currentRentals = new VBox(
        10,

        createCurrentRentalCard(
                "Tractor",
                "Rahul",
                "15 Aug - 17 Aug",
                "Running"
        ),

        createCurrentRentalCard(
                "Rotavator",
                "Suresh",
                "16 Aug - 18 Aug",
                "Running"
        )
);

        // ---------------- REVENUE ----------------

        Text revenueTitle =
                new Text("Revenue");

        VBox revenueCardView =
                createRevenueCard(
                        "Today",
                        "₹6,500",
                        "This Month",
                        "₹48,300"
                );

        // ---------------- REVIEWS ----------------

        Text reviewTitle =
                new Text("Recent Reviews");
VBox reviews = new VBox(
        10,

        createReviewRow(
                "Rahul",
                "⭐⭐⭐⭐⭐",
                "Excellent service. Equipment was delivered on time."
        ),

        createReviewRow(
                "Amit",
                "⭐⭐⭐⭐",
                "Good equipment and smooth booking experience."
        )
);

        // ---------------- NOTIFICATIONS ----------------

        Text notificationTitle =
                new Text("Notifications");

       VBox notifications = new VBox(
        10,

        createNotificationRow(
                "🔔",
                "New Booking",
                "Rahul has requested your Tractor.",
                "2 min ago"
        ),

        createNotificationRow(
                "💰",
                "Payment Received",
                "₹6500 credited successfully.",
                "15 min ago"
        ),

        createNotificationRow(
                "⭐",
                "New Review",
                "Amit rated your Rotavator 5 stars.",
                "1 hour ago"
        )
);

        // ---------------- MAIN CONTENT ----------------

        VBox centerContent =
                new VBox(
                        20,
                        headerText,
                        searchBox,
                        dashboardCards,
                        equipmentTitle,
                        equipmentSection,
                        bookingTitle,
                        bookingRequests,
                        rentalTitle,
                        currentRentals,
                        revenueTitle,
                        revenueCardView,
                        reviewTitle,
                        reviews,
                        notificationTitle,
                        notifications
                );

        centerContent.setPadding(
                new Insets(20,30,30,30)
        );

        ScrollPane scrollPane = new ScrollPane(centerContent);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        borderPane.setCenter(scrollPane);

        root.setStyle("-fx-background-color:#EDE3D5;");

        subRoot.prefWidthProperty().bind(root.widthProperty());
        subRoot.prefHeightProperty().bind(root.heightProperty());

        providerDashboardScene = new Scene(root);

        return providerDashboardScene;
    }


    private VBox createDashboardCard(String icon, String title, String value) {

    Text iconText = new Text(icon);
    iconText.setStyle("-fx-font-size: 28px;");

    Text titleText = new Text(title);
    titleText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#806A5B;"
    );

    Text valueText = new Text(value);
    valueText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:24px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    VBox card = new VBox(
            8,
            iconText,
            titleText,
            valueText
    );

    card.setPadding(new Insets(18));

    card.setPrefWidth(220);
    card.setPrefHeight(135);

    card.setAlignment(Pos.TOP_LEFT);

    card.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:15;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-width:1;" +
            "-fx-border-radius:15;"
    );

    card.setOnMouseEntered(e ->
            card.setStyle(
                    "-fx-background-color:#F0E4D6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#8B6F47;" +
                    "-fx-border-width:1.5;" +
                    "-fx-border-radius:15;"
            )
    );

    card.setOnMouseExited(e ->
            card.setStyle(
                    "-fx-background-color:#F5EFE6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#D8C7B5;" +
                    "-fx-border-width:1;" +
                    "-fx-border-radius:15;"
            )
    );

    return card;
}private HBox createBookingRequestRow(String farmerName,
                                     String equipmentName,
                                     String bookingDate,
                                     String status) {

    Text farmerText = new Text(farmerName);
    farmerText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text equipmentText = new Text(equipmentName);
    equipmentText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#806A5B;"
    );

    Text dateText = new Text(bookingDate);
    dateText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#806A5B;"
    );

    Text statusText = new Text(status);
    statusText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#E69500;"
    );

    Button acceptButton = new Button("Accept");

    acceptButton.setPrefWidth(90);
    acceptButton.setPrefHeight(35);

    acceptButton.setStyle(
            "-fx-background-color:#4CAF50;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:8;" +
            "-fx-cursor:hand;"
    );

    acceptButton.setOnMouseEntered(e ->
            acceptButton.setStyle(
                    "-fx-background-color:#388E3C;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:8;"
            )
    );

    acceptButton.setOnMouseExited(e ->
            acceptButton.setStyle(
                    "-fx-background-color:#4CAF50;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:8;"
            )
    );

    Button rejectButton = new Button("Reject");

    rejectButton.setPrefWidth(90);
    rejectButton.setPrefHeight(35);

    rejectButton.setStyle(
            "-fx-background-color:#D9534F;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:8;" +
            "-fx-cursor:hand;"
    );

    rejectButton.setOnMouseEntered(e ->
            rejectButton.setStyle(
                    "-fx-background-color:#C9302C;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:8;"
            )
    );

    rejectButton.setOnMouseExited(e ->
            rejectButton.setStyle(
                    "-fx-background-color:#D9534F;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:8;"
            )
    );

    HBox buttons = new HBox(10, acceptButton, rejectButton);
    buttons.setAlignment(Pos.CENTER);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    VBox details = new VBox(
            5,
            farmerText,
            equipmentText,
            dateText,
            statusText
    );

    HBox row = new HBox(
            20,
            details,
            spacer,
            buttons
    );

    row.setAlignment(Pos.CENTER_LEFT);

    row.setPadding(new Insets(15));

    row.setPrefHeight(90);

    row.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:12;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-width:1;" +
            "-fx-border-radius:12;"
    );

    row.setOnMouseEntered(e ->
            row.setStyle(
                    "-fx-background-color:#F0E4D6;" +
                    "-fx-background-radius:12;" +
                    "-fx-border-color:#8B6F47;" +
                    "-fx-border-width:1.5;" +
                    "-fx-border-radius:12;"
            )
    );

    row.setOnMouseExited(e ->
            row.setStyle(
                    "-fx-background-color:#F5EFE6;" +
                    "-fx-background-radius:12;" +
                    "-fx-border-color:#D8C7B5;" +
                    "-fx-border-width:1;" +
                    "-fx-border-radius:12;"
            )
    );

    acceptButton.setOnAction(e -> {
        System.out.println("Accepted booking of " + farmerName);
    });

    rejectButton.setOnAction(e -> {
        System.out.println("Rejected booking of " + farmerName);
    });

    return row;
}
    private VBox createCurrentRentalCard(String equipmentName,
                                     String farmerName,
                                     String rentalPeriod,
                                     String status) {

    Text equipmentText = new Text("🚜  " + equipmentName);
    equipmentText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:18px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text farmerText = new Text("Farmer : " + farmerName);
    farmerText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-fill:#806A5B;"
    );

    Text periodText = new Text("Rental : " + rentalPeriod);
    periodText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-fill:#806A5B;"
    );

    Text statusText = new Text("Status : " + status);
    statusText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4CAF50;"
    );

    Button viewButton = new Button("View Booking");

    viewButton.setPrefWidth(140);
    viewButton.setPrefHeight(35);

    viewButton.setStyle(
            "-fx-background-color:#8B6F47;" +
            "-fx-text-fill:white;" +
            "-fx-background-radius:8;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    Button completeButton = new Button("Complete");

    completeButton.setPrefWidth(120);
    completeButton.setPrefHeight(35);

    completeButton.setStyle(
            "-fx-background-color:#4CAF50;" +
            "-fx-text-fill:white;" +
            "-fx-background-radius:8;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    viewButton.setOnMouseEntered(e ->
            viewButton.setStyle(
                    "-fx-background-color:#6F5638;" +
                    "-fx-text-fill:white;" +
                    "-fx-background-radius:8;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;"
            )
    );

    viewButton.setOnMouseExited(e ->
            viewButton.setStyle(
                    "-fx-background-color:#8B6F47;" +
                    "-fx-text-fill:white;" +
                    "-fx-background-radius:8;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;"
            )
    );

    completeButton.setOnMouseEntered(e ->
            completeButton.setStyle(
                    "-fx-background-color:#388E3C;" +
                    "-fx-text-fill:white;" +
                    "-fx-background-radius:8;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;"
            )
    );

    completeButton.setOnMouseExited(e ->
            completeButton.setStyle(
                    "-fx-background-color:#4CAF50;" +
                    "-fx-text-fill:white;" +
                    "-fx-background-radius:8;" +
                    "-fx-font-family:'Poppins';" +
                    "-fx-font-weight:bold;"
            )
    );

    HBox buttonBox = new HBox(
            10,
            viewButton,
            completeButton
    );

    buttonBox.setAlignment(Pos.CENTER_LEFT);

    VBox card = new VBox(
            10,
            equipmentText,
            farmerText,
            periodText,
            statusText,
            buttonBox
    );

    card.setPadding(new Insets(18));

    card.setSpacing(10);

    card.setPrefWidth(500);
    card.setPrefHeight(170);

    card.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:15;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-width:1;" +
            "-fx-border-radius:15;"
    );

    card.setOnMouseEntered(e ->
            card.setStyle(
                    "-fx-background-color:#F0E4D6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#8B6F47;" +
                    "-fx-border-width:1.5;" +
                    "-fx-border-radius:15;"
            )
    );

    card.setOnMouseExited(e ->
            card.setStyle(
                    "-fx-background-color:#F5EFE6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#D8C7B5;" +
                    "-fx-border-width:1;" +
                    "-fx-border-radius:15;"
            )
    );

    viewButton.setOnAction(e ->
            System.out.println("View Booking : " + equipmentName));

    completeButton.setOnAction(e ->
            System.out.println("Rental Completed : " + equipmentName));

    return card;
}
    private VBox createRevenueCard(String today,
                               String todayAmount,
                               String month,
                               String monthAmount) {

    Text title = new Text("💰 Revenue Summary");

    title.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:18px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text todayTitle = new Text(today);
    todayTitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-fill:#806A5B;"
    );

    Text todayValue = new Text(todayAmount);
    todayValue.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:24px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#2E7D32;"
    );

    Separator separator = new Separator();

    Text monthTitle = new Text(month);
    monthTitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-fill:#806A5B;"
    );

    Text monthValue = new Text(monthAmount);
    monthValue.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:24px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#2E7D32;"
    );

    VBox card = new VBox(
            10,
            title,
            todayTitle,
            todayValue,
            separator,
            monthTitle,
            monthValue
    );

    card.setPadding(new Insets(20));

    card.setPrefWidth(350);
    card.setPrefHeight(220);

    card.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:15;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-width:1;" +
            "-fx-border-radius:15;"
    );

    card.setOnMouseEntered(e ->
            card.setStyle(
                    "-fx-background-color:#F0E4D6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#8B6F47;" +
                    "-fx-border-width:1.5;" +
                    "-fx-border-radius:15;"
            )
    );

    card.setOnMouseExited(e ->
            card.setStyle(
                    "-fx-background-color:#F5EFE6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#D8C7B5;" +
                    "-fx-border-width:1;" +
                    "-fx-border-radius:15;"
            )
    );

    return card;
}
private HBox createReviewRow(String customerName,
                             String rating,
                             String review) {

    Text customerText = new Text(customerName);

    customerText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:15px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text ratingText = new Text(rating);

    ratingText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-fill:#F4B400;"
    );

    Text reviewText = new Text(review);

    reviewText.setWrappingWidth(450);

    reviewText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#806A5B;"
    );

    VBox reviewBox = new VBox(
            5,
            customerText,
            ratingText,
            reviewText
    );

    HBox row = new HBox(reviewBox);

    row.setPadding(new Insets(15));

    row.setAlignment(Pos.CENTER_LEFT);

    row.setPrefHeight(90);

    row.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:12;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-width:1;" +
            "-fx-border-radius:12;"
    );

    row.setOnMouseEntered(e ->
            row.setStyle(
                    "-fx-background-color:#F0E4D6;" +
                    "-fx-background-radius:12;" +
                    "-fx-border-color:#8B6F47;" +
                    "-fx-border-width:1.5;" +
                    "-fx-border-radius:12;"
            )
    );

    row.setOnMouseExited(e ->
            row.setStyle(
                    "-fx-background-color:#F5EFE6;" +
                    "-fx-background-radius:12;" +
                    "-fx-border-color:#D8C7B5;" +
                    "-fx-border-width:1;" +
                    "-fx-border-radius:12;"
            )
    );

    return row;
}
    private HBox createNotificationRow(String icon,
                                   String title,
                                   String message,
                                   String time) {

    Text iconText = new Text(icon);
    iconText.setStyle(
            "-fx-font-size:24px;"
    );

    Text titleText = new Text(title);
    titleText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text messageText = new Text(message);
    messageText.setWrappingWidth(350);

    messageText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#806A5B;"
    );

    Text timeText = new Text(time);
    timeText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:11px;" +
            "-fx-fill:#A18C7A;"
    );

    VBox details = new VBox(
            4,
            titleText,
            messageText,
            timeText
    );

    HBox row = new HBox(
            15,
            iconText,
            details
    );

    row.setAlignment(Pos.CENTER_LEFT);

    row.setPadding(new Insets(15));

    row.setPrefHeight(90);

    row.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:12;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-width:1;" +
            "-fx-border-radius:12;"
    );

    row.setOnMouseEntered(e ->
            row.setStyle(
                    "-fx-background-color:#F0E4D6;" +
                    "-fx-background-radius:12;" +
                    "-fx-border-color:#8B6F47;" +
                    "-fx-border-width:1.5;" +
                    "-fx-border-radius:12;"
            )
    );

    row.setOnMouseExited(e ->
            row.setStyle(
                    "-fx-background-color:#F5EFE6;" +
                    "-fx-background-radius:12;" +
                    "-fx-border-color:#D8C7B5;" +
                    "-fx-border-width:1;" +
                    "-fx-border-radius:12;"
            )
    );

    return row;
    }
    private VBox createEquipmentCard(String imagePath,
                                 String equipmentName,
                                 String status,
                                 String price) {

    Image image = new Image(imagePath);

    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(190);
    imageView.setFitHeight(120);
    imageView.setPreserveRatio(true);

    StackPane imagePane = new StackPane(imageView);
    imagePane.setPrefHeight(130);

    imagePane.setStyle(
            "-fx-background-color:#E4D3C2;" +
            "-fx-background-radius:10;"
    );

    Text nameText = new Text(equipmentName);

    nameText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:17px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text statusText = new Text(status);

    String statusColor = "#4CAF50";

    if(status.equalsIgnoreCase("Currently Rented")){
        statusColor = "#F39C12";
    }
    else if(status.equalsIgnoreCase("Maintenance")){
        statusColor = "#E74C3C";
    }

    statusText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:" + statusColor + ";"
    );

    Text priceText = new Text(price);

    priceText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#806A5B;"
    );

    Button editButton = new Button("Edit");
    Button viewButton = new Button("View");
    Button deleteButton = new Button("Delete");

    editButton.setPrefWidth(65);
    viewButton.setPrefWidth(65);
    deleteButton.setPrefWidth(65);

    editButton.setStyle(
            "-fx-background-color:#8B6F47;" +
            "-fx-text-fill:white;" +
            "-fx-background-radius:8;" +
            "-fx-cursor:hand;"
    );

    viewButton.setStyle(
            "-fx-background-color:#4A7C59;" +
            "-fx-text-fill:white;" +
            "-fx-background-radius:8;" +
            "-fx-cursor:hand;"
    );

    deleteButton.setStyle(
            "-fx-background-color:#D9534F;" +
            "-fx-text-fill:white;" +
            "-fx-background-radius:8;" +
            "-fx-cursor:hand;"
    );

    editButton.setOnAction(e ->
            System.out.println("Edit " + equipmentName));

    viewButton.setOnAction(e ->
            System.out.println("View " + equipmentName));

    deleteButton.setOnAction(e ->
            System.out.println("Delete " + equipmentName));

    HBox buttons = new HBox(
            8,
            editButton,
            viewButton,
            deleteButton
    );

    buttons.setAlignment(Pos.CENTER);

    VBox card = new VBox(
            10,
            imagePane,
            nameText,
            statusText,
            priceText,
            buttons
    );

    card.setPadding(new Insets(12));

    card.setPrefWidth(220);
    card.setPrefHeight(280);

    card.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:15;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-width:1;" +
            "-fx-border-radius:15;"
    );

    card.setOnMouseEntered(e ->
            card.setStyle(
                    "-fx-background-color:#F0E4D6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#8B6F47;" +
                    "-fx-border-width:1.5;" +
                    "-fx-border-radius:15;"
            )
    );

    card.setOnMouseExited(e ->
            card.setStyle(
                    "-fx-background-color:#F5EFE6;" +
                    "-fx-background-radius:15;" +
                    "-fx-border-color:#D8C7B5;" +
                    "-fx-border-width:1;" +
                    "-fx-border-radius:15;"
            )
    );

    return card;
    }
}
