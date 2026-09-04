package com.desgin.view.farmer.harshal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookingDetails {

        public VBox getBookingDetails(Runnable closeAction, Runnable cancelAction) {
                return getBookingDetails("BK00123", "John Deere Tractor", "Heavy Duty Tractor", "15 Aug 2026", "18 Aug 2026", "₹2,500/day", "₹7,500", "ACTIVE", null, closeAction, cancelAction);
        }

        public VBox getBookingDetails(String bId, String name, String cat, String sDate, String eDate, String dRate, String tAmt, String stat, Runnable closeAction, Runnable cancelAction) {
                return getBookingDetails(bId, name, cat, sDate, eDate, dRate, tAmt, stat, null, closeAction, cancelAction);
        }

        public VBox getBookingDetails(String bId, String name, String cat, String sDate, String eDate, String dRate, String tAmt, String stat, String imagePath, Runnable closeAction, Runnable cancelAction) {

                VBox mainBox = new VBox(20);
                mainBox.setPadding(new Insets(25));
                mainBox.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 15;");

                HBox header = new HBox();
                header.setAlignment(Pos.CENTER_LEFT);

                VBox titleBox = new VBox(5);

                Label title = new Label("Booking Details");
                title.setStyle(
                                "-fx-font-size: 24px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                Label bookingId = new Label("Booking ID: " + (bId != null ? bId : "BK00123"));
                bookingId.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");

                titleBox.getChildren().addAll(
                                title,
                                bookingId);

                HBox.setHgrow(titleBox, javafx.scene.layout.Priority.ALWAYS);

                Button closeButton = new Button("✕");
                closeButton.setOnAction(e -> closeAction.run());
                closeButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #6B7280;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                header.getChildren().addAll(
                                titleBox,
                                closeButton);

                HBox equipmentBox = new HBox(20);
                equipmentBox.setPadding(new Insets(15));
                equipmentBox.setAlignment(Pos.CENTER_LEFT);
                equipmentBox.setStyle(
                                "-fx-background-color: #F5F7F5;" +
                                                "-fx-background-radius: 10;");

                // Lookup equipment image if not directly provided
                String resolvedImg = imagePath;
                if (resolvedImg == null || resolvedImg.trim().isEmpty()) {
                    for (com.desgin.view.farmer.Swapnil.BookingDataStore.BookingItem b : com.desgin.view.farmer.Swapnil.BookingDataStore.getAllBookings()) {
                        if (bId != null && bId.equalsIgnoreCase(b.bookingId) && b.imagePath != null && !b.imagePath.trim().isEmpty()) {
                            resolvedImg = b.imagePath;
                            break;
                        }
                    }
                }
                if (resolvedImg == null || resolvedImg.trim().isEmpty()) {
                    com.desgin.view.farmer.Swapnil.EquipmentDataStore.EquipmentItem eq = com.desgin.view.farmer.Swapnil.EquipmentDataStore.findByNameOrId(name);
                    if (eq != null && eq.imagePath != null && !eq.imagePath.trim().isEmpty()) {
                        resolvedImg = eq.imagePath;
                    }
                }

                VBox imageBox = new VBox();
                imageBox.setPrefWidth(130);
                imageBox.setPrefHeight(95);
                imageBox.setMinSize(130, 95);
                imageBox.setMaxSize(130, 95);
                imageBox.setAlignment(Pos.CENTER);
                imageBox.setStyle(
                                "-fx-background-color: #E8F1EB;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #C2E0CE;" +
                                                "-fx-border-radius: 10;");

                if (resolvedImg != null && !resolvedImg.trim().isEmpty()) {
                    try {
                        javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView();
                        iv.setFitWidth(120);
                        iv.setFitHeight(85);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(120, 85);
                        clip.setArcWidth(10);
                        clip.setArcHeight(10);
                        iv.setClip(clip);

                        javafx.scene.image.Image img = new javafx.scene.image.Image(resolvedImg, true);
                        iv.setImage(img);
                        img.errorProperty().addListener((obs, oldV, err) -> {
                            if (err) {
                                imageBox.getChildren().clear();
                                Label fallbackLabel = new Label("🚜 Machinery");
                                fallbackLabel.setStyle("-fx-text-fill: #52796F; -fx-font-weight: bold;");
                                imageBox.getChildren().add(fallbackLabel);
                            }
                        });
                        imageBox.getChildren().add(iv);
                    } catch (Exception ex) {
                        Label imageLabel = new Label("🚜 Machinery");
                        imageLabel.setStyle("-fx-text-fill: #52796F; -fx-font-weight: bold;");
                        imageBox.getChildren().add(imageLabel);
                    }
                } else {
                    Label imageLabel = new Label("🚜 Machinery");
                    imageLabel.setStyle("-fx-text-fill: #52796F; -fx-font-weight: bold;");
                    imageBox.getChildren().add(imageLabel);
                }

                VBox equipmentInfo = new VBox(7);

                Label equipmentName = new Label(name != null ? name : "John Deere Tractor");
                equipmentName.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                Label equipmentType = new Label(cat != null ? cat : "Heavy Duty Tractor");
                equipmentType.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");

                equipmentInfo.getChildren().addAll(
                                equipmentName,
                                equipmentType);

                equipmentBox.getChildren().addAll(
                                imageBox,
                                equipmentInfo);

                Label rentalTitle = new Label("Rental Information");
                rentalTitle.setStyle(
                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                VBox rentalBox = new VBox(10);
                rentalBox.setPadding(new Insets(15));
                rentalBox.setStyle(
                                "-fx-background-color: #F8FAF9;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #E2E8E4;" +
                                                "-fx-border-radius: 10;");
                rentalBox.getChildren().addAll(
                                createInfoRow("Start Date", sDate != null ? sDate : "15 Aug 2026"),
                                createInfoRow("End Date", eDate != null ? eDate : "18 Aug 2026"),
                                createInfoRow("Rental Duration", "Active Period"),
                                createInfoRow("Price Per Day", dRate != null ? dRate : "₹2,500/day"),
                                createInfoRow("Total Amount", tAmt != null ? tAmt : "₹7,500"));

                HBox statusBox = new HBox(8);
                statusBox.setAlignment(Pos.CENTER_LEFT);

                String currentStatus = stat != null ? stat : "PENDING";
                Label status = new Label(currentStatus);
                status.setPadding(new Insets(7, 14, 7, 14));
                String stStyle = "COMPLETED".equalsIgnoreCase(currentStatus) ? "-fx-background-color: #EDE3D5; -fx-text-fill: #1B4332;" :
                                ("ACTIVE".equalsIgnoreCase(currentStatus) || "CONFIRMED".equalsIgnoreCase(currentStatus) ? "-fx-background-color: #DCFCE7; -fx-text-fill: #166534;" :
                                ("ACCEPTED".equalsIgnoreCase(currentStatus) ? "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;" :
                                "-fx-background-color: #FEE2E2; -fx-text-fill: #B91C1C;"));
                status.setStyle(stStyle +
                                                "-fx-background-radius: 20;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 12px;");

                com.desgin.view.farmer.Swapnil.BookingDataStore.BookingItem matched = null;
                for (com.desgin.view.farmer.Swapnil.BookingDataStore.BookingItem b : com.desgin.view.farmer.Swapnil.BookingDataStore.getAllBookings()) {
                    if (bId != null && bId.equalsIgnoreCase(b.bookingId)) {
                        matched = b;
                        break;
                    }
                }

                String paySt = (matched != null && matched.paymentStatus != null) ? matched.paymentStatus : "PENDING";
                Label payBadge = new Label("💳 Payment: " + paySt);
                payBadge.setPadding(new Insets(7, 14, 7, 14));
                payBadge.setStyle("PAID".equalsIgnoreCase(paySt) ?
                        "-fx-background-color: #DCFCE7; -fx-text-fill: #166534; -fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 12px;" :
                        "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 12px;");

                statusBox.getChildren().addAll(status, payBadge);

                HBox actions = new HBox(10);
                actions.setAlignment(Pos.CENTER_RIGHT);

                if (!("CANCELLED".equalsIgnoreCase(currentStatus) || "COMPLETED".equalsIgnoreCase(currentStatus))) {
                    Button cancelButton = new Button("Cancel Booking");
                    cancelButton.setOnAction(e -> {
                        if (bId != null) {
                            com.desgin.view.farmer.Swapnil.BookingDataStore.cancelBooking(bId);
                        }
                        showCancelConfirmation(cancelAction);
                    });
                    cancelButton.setPrefHeight(38);
                    cancelButton.setStyle(
                            "-fx-background-color: #FEE2E2;" +
                            "-fx-text-fill: #B91C1C;" +
                            "-fx-background-radius: 7;" +
                            "-fx-font-weight: bold;");
                    actions.getChildren().add(cancelButton);
                }

                mainBox.getChildren().addAll(
                                header,
                                equipmentBox,
                                rentalTitle,
                                rentalBox,
                                statusBox,
                                actions);

                return mainBox;
        }

        private HBox createInfoRow(
                        String title,
                        String value) {

                HBox row = new HBox();
                row.setAlignment(Pos.CENTER_LEFT);

                Label titleLabel = new Label(title);
                titleLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");

                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #374151;");

                HBox.setHgrow(
                                titleLabel,
                                javafx.scene.layout.Priority.ALWAYS);

                row.getChildren().addAll(
                                titleLabel,
                                valueLabel);

                return row;
        }

        private void showContactOwnerPopup() {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Contact Owner");
                alert.setHeaderText("Machinery Provider Contact Information");
                alert.setContentText("Provider: Rahul Patil\n📞 Phone: +91 98220 12345\n✉ Email: rahul@farmequip.com\n\nDirect support is available 8 AM - 8 PM.");
                alert.showAndWait();
        }

        private void showCancelConfirmation(Runnable cancelAction) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cancel Booking");
                alert.setHeaderText("Are you sure you want to cancel this booking?");
                alert.setContentText("This will release the reserved machinery and notify the provider.");
                alert.showAndWait().ifPresent(response -> {
                        if (response == javafx.scene.control.ButtonType.OK && cancelAction != null) {
                                cancelAction.run();
                        }
                });
        }
}
