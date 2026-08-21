package com.desgin.view.admin;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminDashboard {

    private Scene adminDashboardScene;
    public static BorderPane borderPane;
    public static StackPane root;

    public Scene getAdminDashboardScene(Runnable ref) {
        root = new StackPane();

        // Left Sidebar for Admin
        AdminLeftSideBar objLeftSideBar = new AdminLeftSideBar(this);
        VBox leftVB = objLeftSideBar.getSideBar(ref);

        borderPane = new BorderPane();
        borderPane.setPadding(Insets.EMPTY);
        borderPane.setMinWidth(0);
        borderPane.setStyle("-fx-background-color: #F4F9F4;");

        BorderPane subroot = new BorderPane();
        subroot.setLeft(leftVB);
        subroot.setCenter(borderPane);
        subroot.setMinWidth(0);
        subroot.setStyle("-fx-background-color: #F4F9F4;");

        root.getChildren().addAll(subroot);

        // Top Admin Profile & System Header
        AdminProfileManagement objProfileManagement = new AdminProfileManagement();
        borderPane.setTop(objProfileManagement.getProfile(root));

        // Initial Home Page
        borderPane.setCenter(AdminHome.getPage(root));

        root.setStyle("-fx-background-color: #F4F9F4;");

        subroot.prefWidthProperty().bind(root.widthProperty());
        subroot.prefHeightProperty().bind(root.heightProperty());
        adminDashboardScene = new Scene(root);

        return adminDashboardScene;
    }
}
