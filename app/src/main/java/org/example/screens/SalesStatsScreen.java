package org.example.screens;

import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.buttons.MidButton;
import org.example.kiosk.LanguageSetting;
import org.example.orders.OrderListWrapper;
import org.example.sql.SqlQueries;

/**
 * Admin screen for sales stats.
 */
public class SalesStatsScreen {

  /**
   * Scene to display the order history.
   *
   * @param primaryStage this window
   * @param prevScene    pevious scene to go back to
   * @return the scene itself
   */
  public Scene showStatsScene(Stage primaryStage, Scene prevScene) {

    // VBox for the charts
    VBox topRight = new VBox();
    topRight.setPadding(new Insets(10));
    topRight.setPrefWidth(1280);
    topRight.setAlignment(Pos.CENTER);
    HBox.setHgrow(topRight, Priority.ALWAYS);

    // So the admin doesnt forget where he is lol
    Label pageLabel = new Label("Sales Statistics:");
    pageLabel.setStyle(
        "-fx-font-size: 45px;"
            + "-fx-font-weight: bold;");

    // Had to somehow make List final, so a wrapper class is being used
    final OrderListWrapper wrapper = new OrderListWrapper();

    SqlQueries queries = new SqlQueries();

    try {

      // Populates the list wrapper
      wrapper.orders = queries.queryOrders();
      queries.queryOrderItemsFor(wrapper.orders);

    } catch (SQLException e) {

      e.printStackTrace();

    }

    Charts charts = new Charts();

    // Button for Product vs Quantity ordered - BarChart
    MidButton productSalesBtn = new MidButton("Sold Products", "rgb(255, 255, 255)", 30);
    productSalesBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      BarChart<String, Number> chart = charts.createProductSales(wrapper.orders);

      // Translate the chart before showing
      LanguageSetting lang = LanguageSetting.getInstance();
      lang.registerRoot(chart);
      lang.translateLabels(chart);

      topRight.getChildren().add(chart);

    });

    // Button for Orders vs Weekdays -- BarChart
    MidButton ordersPerDayBtn = new MidButton("Orders per Day", "rgb(255, 255, 255)", 30);
    ordersPerDayBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      BarChart<String, Number> chart = charts.createOrdersPerWeekday(wrapper.orders);

      // Translate the chart before showing
      LanguageSetting lang = LanguageSetting.getInstance();
      lang.registerRoot(chart);
      lang.translateLabels(chart);

      topRight.getChildren().add(chart);

    });

    // Button for Orders vs Hours -- Graph/LineChart
    MidButton ordersByHourBtn = new MidButton("Orders by Hour", "rgb(255, 255, 255)", 30);
    ordersByHourBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      LineChart<Number, Number> chart = charts.createOrdersByHour(wrapper.orders);

      // Translate the chart before showing
      LanguageSetting lang = LanguageSetting.getInstance();
      lang.registerRoot(chart);
      lang.translateLabels(chart);

      topRight.getChildren().add(chart);

    });

    // Button for Revenue Share of Products -- PieChart
    MidButton productRevenuesBtn = new MidButton("Revenue by Products", "rgb(255, 255, 255)", 30);
    productRevenuesBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      PieChart pieChart = charts.createProductRevenue(wrapper.orders);

      // Translate the chart before showing
      LanguageSetting lang = LanguageSetting.getInstance();
      lang.registerRoot(pieChart);
      lang.translateLabels(pieChart);

      topRight.getChildren().add(pieChart);

    });

    // Button for Revenue of last 12 month
    MidButton revenuesBtn = new MidButton("Revenue (last 12 Month)", "rgb(255, 255, 255)", 30);
    revenuesBtn.setOnAction(e -> {

      // Clears out the right side VBox for the new chart
      topRight.getChildren().clear();
      BarChart<String, Number> barChart = charts.createMonthlyRevenue(wrapper.orders);

      // Translate the chart before showing
      LanguageSetting lang = LanguageSetting.getInstance();
      lang.registerRoot(barChart);
      lang.translateLabels(barChart);

      topRight.getChildren().add(barChart);

    });

    // VBox for the Buttons
    VBox topLeft = new VBox();
    topLeft.getChildren().addAll(
        pageLabel, productSalesBtn, ordersPerDayBtn,
        ordersByHourBtn, productRevenuesBtn, revenuesBtn);
    topLeft.setPadding(new Insets(10));
    topLeft.setSpacing(25);
    topLeft.setPrefWidth(640);
    topLeft.setAlignment(Pos.TOP_LEFT);
    HBox.setHgrow(topLeft, Priority.NEVER);

    // Wrapping the two top side VBoxes
    // Fixed size
    HBox topContainer = new HBox();
    topContainer.getChildren().addAll(topLeft, topRight);
    topContainer.setPrefHeight(695);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    // Back button
    // Clicking button means user goes to previous screen
    var backButton = new BackBtnWithTxt();
    backButton.setOnAction(e -> {

      primaryStage.setScene(prevScene);

    });

    // Spacer for Bottom Row
    Region spacerBottom = new Region();
    HBox.setHgrow(spacerBottom, Priority.ALWAYS);

    // Language Button
    // Functionality below VBox Layout
    var langButton = new LangBtn();

    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setPrefHeight(335);
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    VBox layout = new VBox();
    layout.setPadding(new Insets(50));
    layout.getChildren().addAll(topContainer, bottomContainer);

    // Translate button action
    langButton.addAction(event -> {
      LanguageSetting lang = LanguageSetting.getInstance();
      String newLang;
      if (lang.getSelectedLanguage().equals("en")) {
        newLang = "sv";
      } else {
        newLang = "en";
      }
      lang.changeLanguage(newLang);
      lang.translateLabels(layout);
    });

    // Translate the whole layout before rendering
    LanguageSetting lang = LanguageSetting.getInstance();
    lang.registerRoot(layout);
    lang.translateLabels(layout);

    Scene scene = new Scene(layout, 1920, 1080);

    return scene;

  }

}
