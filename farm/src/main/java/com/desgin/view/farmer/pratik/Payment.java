package com.desgin.view.farmer.pratik;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;



public class Payment {

    private static ArrayList<VBox> paymentCardList = new ArrayList<>();
    private static ArrayList<String> paymentSearchData = new ArrayList<>();
    private static ArrayList<String> paymentStatusList = new ArrayList<>();
    private static ArrayList<String> paymentMethodList = new ArrayList<>();

    private static TextField searchField;

private static CheckBox paid;
private static CheckBox pending;
private static CheckBox failed;

private static CheckBox upi;
private static CheckBox card;
private static CheckBox net;

//private VBox paymentCards;
   public static ScrollPane getPaymentSection() {

    Text paymentTitle = new Text("Payment Dashboard");
    paymentTitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:26px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text subtitle = new Text(
            "Manage all your equipment payments"
    );

    subtitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-fill:#7A6658;"
    );

    VBox heading = new VBox(
            5,
            paymentTitle,
            subtitle
    );

    searchField = new TextField();
    searchField.setPromptText(
            "Search Transaction ID or Equipment..."
    );

    stylePaymentTextField(searchField);

    Button searchButton = new Button("Search");
    styleGreenButton(searchButton);
    searchButton.setOnAction(e -> {

    String keyword = searchField.getText().trim().toLowerCase();
    for(int i = 0; i < paymentCardList.size(); i++){
        VBox card = paymentCardList.get(i);
        String data = paymentSearchData.get(i);
        boolean found = keyword.isEmpty() || data.contains(keyword);
        card.setVisible(found);
        card.setManaged(found);
        }

    });
    searchField.textProperty().addListener((obs, oldValue, newValue) -> {
    String keyword = newValue.toLowerCase();
    for(int i = 0; i < paymentCardList.size(); i++){
        VBox card = paymentCardList.get(i);
        String data = paymentSearchData.get(i);
        boolean found = keyword.isEmpty() || data.contains(keyword);
        card.setVisible(found);
        card.setManaged(found);
    }

});

    HBox searchBox = new HBox(
            10,
            searchField,
            searchButton
    );

    HBox.setHgrow(
            searchField,
            Priority.ALWAYS
    );

    //---------------------------------------------------------
    // Summary Cards
    //---------------------------------------------------------

    HBox summaryCards = new HBox(18);

    summaryCards.getChildren().addAll(

            createSummaryCard(
                    "Total Payments",
                    "18",
                    "💳",
                    "#4A2C20"
            ),

            createSummaryCard(
                    "Paid",
                    "₹32,500",
                    "🟢",
                    "#4CAF50"
            ),

            createSummaryCard(
                    "Pending",
                    "₹5,600",
                    "🟠",
                    "#FF9800"
            ),

            createSummaryCard(
                    "Failed",
                    "2",
                    "🔴",
                    "#D32F2F"
            )
    );

    VBox filterSection = createPaymentFilter();

    Text historyTitle = new Text(
            "Payment History"
    );

    historyTitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:20px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text historySubtitle = new Text(
            "View all your completed and pending payments"
    );

    historySubtitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#7A6658;"
    );

    VBox historyHeading = new VBox(
            4,
            historyTitle,
            historySubtitle
    );

    VBox paymentCards = new VBox(18);


    
    VBox card1 = createPaymentCard(
        "Mahindra Tractor",
        "TXN-100234",
        "₹1500",
        "UPI",
        "12 Aug 2026",
        "Paid",
        "file:farm/src/main/resources/assets/Images/tractor.png"
    );

    paymentCardList.add(card1);
    paymentSearchData.add("mahindra tractor txn-100234");
    paymentStatusList.add("Paid");
    paymentMethodList.add("UPI");

    VBox card2 = createPaymentCard(
            "John Deere",
            "TXN-100240",
            "₹1800",
            "Card",
            "15 Aug 2026",
            "Pending",
            "file:farm/src/main/resources/assets/Images/background.jpeg"
    );

    paymentCardList.add(card2);
    paymentSearchData.add("john deere txn-100240");
    paymentStatusList.add("Pending");
    paymentMethodList.add("Card");

    VBox card3 = createPaymentCard(
            "Seeder",
            "TXN-100260",
            "₹900",
            "Net Banking",
            "18 Aug 2026",
            "Failed",
            "file:farm/src/main/resources/assets/Images/logo.png"
    );

    paymentCardList.add(card3);
    paymentSearchData.add("seeder txn-100260");
    paymentStatusList.add("Failed");
    paymentMethodList.add("Net Banking");

    paymentCards.getChildren().addAll(card1, card2, card3);

        VBox paymentSection = new VBox(
                18,
                historyHeading,
                paymentCards
        );

        HBox mainContent = new HBox(
                20,
                filterSection,
                paymentSection
        );

        HBox.setHgrow(
                paymentSection,
                Priority.ALWAYS
        );

        VBox root = new VBox(
                22,
                heading,
                searchBox,
                summaryCards,
                mainContent
        );

        root.setPadding(
                new Insets(25,30,30,30)
        );

        root.setStyle(
                "-fx-background-color:#FCF9F5;"
        );

        ScrollPane scrollPane = new ScrollPane(root);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background:transparent;" +
                "-fx-background-color:transparent;"
        );

        return scrollPane;

    }

    private static VBox createPaymentCard(
            String equipmentName,
            String transactionId,
            String amount,
            String paymentMethod,
            String paymentDate,
            String paymentStatus,
            String imagePath) {

        Image image = new Image(imagePath);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setFitHeight(110);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        StackPane imagePane = new StackPane(imageView);

        imagePane.setStyle(
                "-fx-background-color:#E8DED2;" +
                "-fx-background-radius:10;"
        );

    //-----------------------------------------------------
    // Equipment Name
    //-----------------------------------------------------

    Text name = new Text(equipmentName);
    name.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:18px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text txn = new Text("Transaction ID : " + transactionId);
    txn.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#6F5A4D;"
    );

    Text date = new Text("Payment Date : " + paymentDate);
    date.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#6F5A4D;"
    );

    Text method = new Text("Method : " + paymentMethod);
    method.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#6F5A4D;"
    );

    Text price = new Text(amount);

    price.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:20px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#6B8E23;"
    );

    //-----------------------------------------------------
    // Status Badge
    //-----------------------------------------------------

    Label status = new Label(paymentStatus);

    if(paymentStatus.equalsIgnoreCase("Paid")){

        status.setStyle(
                "-fx-background-color:#DFF5E1;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#2E7D32;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;"
        );

    }else if(paymentStatus.equalsIgnoreCase("Pending")){

        status.setStyle(
                "-fx-background-color:#FFF4D6;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#EF6C00;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;"
        );

    }else{

        status.setStyle(
                "-fx-background-color:#FDE2E2;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#C62828;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;"
        );

    }

    //-----------------------------------------------------
    // Buttons
    //-----------------------------------------------------

   Button invoiceButton = new Button("Download Invoice");

invoiceButton.setOnAction(e -> {

    FileChooser chooser = new FileChooser();

    chooser.setTitle("Save Invoice");

    chooser.setInitialFileName(transactionId + ".txt");

    chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                    "Text File",
                    "*.txt"
            )
    );

    Window window = invoiceButton.getScene().getWindow();

    File file = chooser.showSaveDialog(window);

    if(file != null){

        try{

            FileWriter writer = new FileWriter(file);

            writer.write("=====================================\n");
            writer.write("         FARM RENTAL INVOICE\n");
            writer.write("=====================================\n\n");

            writer.write("Equipment      : " + equipmentName + "\n");
            writer.write("Transaction ID : " + transactionId + "\n");
            writer.write("Amount         : " + amount + "\n");
            writer.write("Payment Method : " + paymentMethod + "\n");
            writer.write("Payment Date   : " + paymentDate + "\n");
            writer.write("Status         : " + paymentStatus + "\n");

            writer.write("\n-------------------------------------\n");
            writer.write("Thank you for using our platform.\n");
            writer.write("-------------------------------------");

            writer.close();

            System.out.println("Invoice Downloaded Successfully");

        }catch(IOException ex){

            ex.printStackTrace();

        }

    }

});

    invoiceButton.setStyle(
            "-fx-background-color:#4A2C20;" +
            "-fx-background-radius:8;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    invoiceButton.setPrefHeight(38);

    invoiceButton.setOnMouseEntered(e->{

        invoiceButton.setStyle(
                "-fx-background-color:#6B4A3A;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    invoiceButton.setOnMouseExited(e->{

        invoiceButton.setStyle(
                "-fx-background-color:#4A2C20;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    Button payNowButton = new Button("Pay Now");

    payNowButton.setStyle(
            "-fx-background-color:#6B8E23;" +
            "-fx-background-radius:8;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    payNowButton.setPrefHeight(38);

    payNowButton.setVisible(
            paymentStatus.equalsIgnoreCase("Pending")
    );

    payNowButton.setManaged(
            paymentStatus.equalsIgnoreCase("Pending")
    );

    payNowButton.setOnMouseEntered(e->{

        payNowButton.setStyle(
                "-fx-background-color:#55751C;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    payNowButton.setOnMouseExited(e->{

        payNowButton.setStyle(
                "-fx-background-color:#6B8E23;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    payNowButton.setOnAction(e -> {

         showPaymentDialog(
            equipmentName,
            amount,
            status,
            payNowButton
    );

});

    HBox buttonBox = new HBox(
            12,
            invoiceButton,
            payNowButton
    );

    VBox details = new VBox(
            8,
            name,
            txn,
            date,
            method,
            price,
            status,
            buttonBox
    );

    HBox content = new HBox(
            20,
            imagePane,
            details
    );

    content.setAlignment(Pos.CENTER_LEFT);

    VBox card = new VBox(content);

    card.setPadding(new Insets(18));

    card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:14;" +
            "-fx-border-color:#E0D4C7;" +
            "-fx-border-radius:14;" +
            "-fx-border-width:1;"
    );

    return card;
}

//---------------------------------------------------------
// Summary Card
//---------------------------------------------------------

private static VBox createSummaryCard(
        String title,
        String value,
        String icon,
        String color){

    Text iconText = new Text(icon);
    iconText.setStyle("-fx-font-size:24px;");

    Text titleText = new Text(title);
    titleText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#7A6658;"
    );

    Text valueText = new Text(value);
    valueText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:24px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:" + color + ";"
    );

    VBox card = new VBox(
            10,
            iconText,
            titleText,
            valueText
    );

    card.setPadding(new Insets(18));

    card.setPrefWidth(190);

    card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-border-color:#E0D4C7;" +
            "-fx-border-radius:12;" +
            "-fx-border-width:1;"
    );

    return card;
}

private static void showPaymentDialog(

        String equipmentName,
        String amount,
        Label statusLabel,
        Button payNowButton){

    Stage stage = new Stage();

    stage.setTitle("Complete Payment");

    VBox root = new VBox(15);

    root.setPadding(new Insets(20));

    root.setAlignment(Pos.CENTER);

    Text title = new Text("Payment");

    title.setStyle(
            "-fx-font-size:22;" +
            "-fx-font-weight:bold;"
    );

    Label equipment = new Label(
            "Equipment : " + equipmentName
    );

    Label price = new Label(
            "Amount : " + amount
    );

    ComboBox<String> paymentMethod = new ComboBox<>();

    paymentMethod.getItems().addAll(

            "UPI",
            "Card",
            "Net Banking"

    );

    paymentMethod.setPromptText("Select Payment Method");

    TextField details = new TextField();

    details.setPromptText(
            "Enter UPI ID / Card Number"
    );

    Button confirm = new Button("Confirm Payment");

    confirm.setStyle(
            "-fx-background-color:#6B8E23;" +
            "-fx-text-fill:white;"
    );

    confirm.setOnAction(event->{

        if(paymentMethod.getValue()==null){

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText(null);

            alert.setContentText(
                    "Please select payment method."
            );

            alert.show();

            return;
        }

        if(details.getText().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText(null);

            alert.setContentText(
                    "Enter payment details."
            );

            alert.show();

            return;
        }

        statusLabel.setText("Paid");

        statusLabel.setStyle(

                "-fx-background-color:#DFF5E1;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#2E7D32;" +
                "-fx-font-weight:bold;"
        );

        payNowButton.setVisible(false);

        payNowButton.setManaged(false);

        Alert success = new Alert(Alert.AlertType.INFORMATION);

        success.setHeaderText(null);

        success.setContentText(
                "Payment Successful!"
        );

        success.showAndWait();

        stage.close();

    });

    root.getChildren().addAll(

            title,
            equipment,
            price,
            paymentMethod,
            details,
            confirm

    );

    Scene scene = new Scene(root,350,300);

    stage.setScene(scene);

    stage.show();
}
private static VBox createPaymentFilter(){

    Text title = new Text("Filters");

    title.setStyle(
            "-fx-font-size:20px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;" +
            "-fx-font-family:'Poppins';"
    );

    Text statusTitle = new Text("Payment Status");

    statusTitle.setStyle(
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    paid = new CheckBox("Paid");
    pending = new CheckBox("Pending");
    failed = new CheckBox("Failed");

    styleCheckBox(paid);
    styleCheckBox(pending);
    styleCheckBox(failed);

    VBox statusBox = new VBox(
            10,
            statusTitle,
            paid,
            pending,
            failed
    );

    Text methodTitle = new Text("Payment Method");

    methodTitle.setStyle(
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

        upi = new CheckBox("UPI");
        card = new CheckBox("Card");
        net = new CheckBox("Net Banking");

    styleCheckBox(upi);
    styleCheckBox(card);
    styleCheckBox(net);

    VBox methodBox = new VBox(
            10,
            methodTitle,
            upi,
            card,
            net
    );

    

   Button apply = new Button("Apply Filters");
styleGreenButton(apply);

apply.setOnAction(e -> {

    boolean paidSelected = paid.isSelected();
    boolean pendingSelected = pending.isSelected();
    boolean failedSelected = failed.isSelected();

    boolean upiSelected = upi.isSelected();
    boolean cardSelected = card.isSelected();
    boolean netSelected = net.isSelected();

    for(int i=0;i<paymentCardList.size();i++){

        VBox cardBox = paymentCardList.get(i);

        String status = paymentStatusList.get(i);
        String method = paymentMethodList.get(i);

        boolean statusMatch =
                (!paidSelected && !pendingSelected && !failedSelected)
                || (paidSelected && status.equalsIgnoreCase("Paid"))
                || (pendingSelected && status.equalsIgnoreCase("Pending"))
                || (failedSelected && status.equalsIgnoreCase("Failed"));

        boolean methodMatch =
                (!upiSelected && !cardSelected && !netSelected)
                || (upiSelected && method.equalsIgnoreCase("UPI"))
                || (cardSelected && method.equalsIgnoreCase("Card"))
                || (netSelected && method.equalsIgnoreCase("Net Banking"));

        boolean visible = statusMatch && methodMatch;

        cardBox.setVisible(visible);
        cardBox.setManaged(visible);
    }

});

    

    Button reset = new Button("Reset");

        reset.setMaxWidth(Double.MAX_VALUE);
        reset.setPrefHeight(42);

        reset.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:#D8C7B5;" +
                "-fx-border-radius:9;" +
                "-fx-background-radius:9;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    reset.setOnAction(e -> {

    // Clear search
    searchField.clear();

    // Clear payment status filters
    paid.setSelected(false);
    pending.setSelected(false);
    failed.setSelected(false);

    // Clear payment method filters
    upi.setSelected(false);
    card.setSelected(false);
    net.setSelected(false);

    // Optional: reload all payment cards
    System.out.println("Filters Reset Successfully");
});

    VBox buttons = new VBox(
            10,
            apply,
            reset
    );

    VBox filter = new VBox(
            22,
            title,
            statusBox,
            methodBox,
            buttons
    );

    filter.setPadding(new Insets(20));

    filter.setPrefWidth(235);

    filter.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:14;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-radius:14;"
    );

    return filter;
}

private static void stylePaymentTextField(TextField field){

    field.setPrefHeight(45);

    field.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:10;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-radius:10;" +
            "-fx-padding:0 15;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;"
    );
}
    private static void styleCheckBox(CheckBox checkBox) {

    checkBox.setStyle(
            "-fx-text-fill: #5C4033;" +
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;"
    );

}

private static void styleGreenButton(Button button){

    button.setPrefHeight(45);

    button.setStyle(
            "-fx-background-color:#6B8E23;" +
            "-fx-background-radius:10;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    button.setOnMouseEntered(e->{

        button.setStyle(
                "-fx-background-color:#55751C;" +
                "-fx-background-radius:10;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    button.setOnMouseExited(e->{

        button.setStyle(
                "-fx-background-color:#6B8E23;" +
                "-fx-background-radius:10;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });
}
}

