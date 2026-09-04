package com.desgin.view.operator;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OperatorDashboard {

    private Scene operatorDashboardScene;
    public static BorderPane borderPane;
    public static StackPane root;

    public Scene getOperatorDashboardScene(Runnable ref) {
        root = new StackPane();

        // Left Sidebar for Machinery Operator
        OperatorLeftSideBar objLeftSideBar = new OperatorLeftSideBar(this);
        VBox leftVB = objLeftSideBar.getSideBar(ref);

        borderPane = new BorderPane();
        borderPane.setPadding(Insets.EMPTY);
        borderPane.setStyle("-fx-background-color: #F4F9F4;");

        BorderPane subroot = new BorderPane();
        subroot.setLeft(leftVB);
        subroot.setCenter(borderPane);
        subroot.setStyle("-fx-background-color: #F4F9F4;");

        root.getChildren().addAll(subroot);

        // Top Profile Management Header (Notifications + Profile Card)
        OperatorProfileManagement objProfileManagement = new OperatorProfileManagement();
        borderPane.setTop(objProfileManagement.getProfile(root));

        // Initial Home Dashboard Page
        borderPane.setCenter(OperatorHome.getPage());
        OperatorProfileManagement.updateHeaderGreeting();
        OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.dashboardBtn, OperatorLeftSideBar.navigationButtons);

        root.setStyle("-fx-background-color: #F4F9F4;");

        subroot.prefWidthProperty().bind(root.widthProperty());
        subroot.prefHeightProperty().bind(root.heightProperty());
        operatorDashboardScene = new Scene(root);

        return operatorDashboardScene;
    }
}
