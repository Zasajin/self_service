package org.example.screens;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import org.example.menu.OrderItem;
import org.example.orders.Order;

/**
 * Class to render graphs.
 */
public class Charts {

  /**
   * Rendering the Products vs Quantity ordered chart.
   *
   * @param orders queried orders
   * @return the chart
   */
  public BarChart<String, Number> createProductSales(ArrayList<Order> orders) {

    // Data collection Part
    Map<String, Integer> productSales = new HashMap<>();

    for (Order order : orders) {

      for (OrderItem item : order.getProducts()) {

        String productName = item.getProduct().getName();
        int quantity = item.getQuantity();

        productSales.put(productName, productSales.getOrDefault(productName, 0) + quantity);

      }

    }

    // Rendering Part
    // Parameter is named xxAxis because of checkstyle naming conventions
    CategoryAxis xxAxis = new CategoryAxis();
    xxAxis.setLabel("Product");
    
    //Parameter is named yyAxis because of checkstyle naming conventions
    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Quantity Sold");

    BarChart<String, Number> barChart = new BarChart<>(xxAxis, yyAxis);
    barChart.setTitle("Product Sales");

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Sales Data");

    for (Map.Entry<String, Integer> entry : productSales.entrySet()) {

      if (entry.getValue() > 0) {

        series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));

      }

    }

    barChart.getData().add(series);
    
    return barChart;
  }

  /**
   * Rendering Orders vs Weekdays chart.
   *
   * @param orders queried orders
   * @return the chart
   */
  public BarChart<String, Number> createOrdersPerWeekday(ArrayList<Order> orders) {

    // Data collection part
    Map<DayOfWeek, Integer> weekdayCounts = new LinkedHashMap<>();

    for (Order order : orders) {

      LocalDateTime dateTime = order.getOrderDate().toLocalDateTime();
      DayOfWeek day = dateTime.getDayOfWeek();
      weekdayCounts.put(day, weekdayCounts.getOrDefault(day, 0) + 1);

    }

    // Rendering Part
    // Parameter is named xxAxis because of checkstyle naming conventions
    CategoryAxis xxAxis = new CategoryAxis();
    xxAxis.setLabel("Weekday");
    
    //Parameter is named yyAxis because of checkstyle naming conventions
    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Number of Orders");

    BarChart<String, Number> barChart = new BarChart<>(xxAxis, yyAxis);
    barChart.setTitle("Orders per Weekday");

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Orders");

    for (DayOfWeek day : DayOfWeek.values()) {

      String dayName = day.toString().substring(0, 1) + day.toString().substring(1).toLowerCase();
      int count = weekdayCounts.getOrDefault(day, 0);
      series.getData().add(new XYChart.Data<>(dayName, count));

    }

    barChart.getData().add(series);

    return barChart;

  }

  /**
   * Rendering Orders by Hours graph.
   *
   * @param orders queried orders
   * @return the chart
   */
  public LineChart<Number, Number> createOrdersByHour(ArrayList<Order> orders) {

    // Data collection Part
    Map<Double, Integer> hourCounts = new TreeMap<>();

    for (Order order : orders) {

      LocalDateTime dateTime = order.getOrderDate().toLocalDateTime();

      double hour = dateTime.getHour()
          + (dateTime.getMinute() / 60.0) + (dateTime.getSecond() / 3600.0);

      double roundedHour = Math.round(hour * 4) / 4.0;

      hourCounts.put(roundedHour, hourCounts.getOrDefault(roundedHour, 0) + 1);

    }

    // Rendering Part
    // Parameter is named xxAxis because of checkstyle naming conventions
    NumberAxis xxAxis = new NumberAxis(0, 24, 0.25);
    xxAxis.setLabel("Hour of Day");

    //Parameter is named yyAxis because of checkstyle naming conventions
    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Numbers of Orders");

    LineChart<Number, Number> lineChart = new LineChart<>(xxAxis, yyAxis);
    lineChart.setTitle("Orders by Hour");

    // Without this line, very order would create a dot on the graph
    lineChart.setCreateSymbols(false);

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName("Volume of Orders");

    hourCounts.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> {
          double hour = entry.getKey();
          int count = entry.getValue();
          series.getData().add(new XYChart.Data<>(hour, count));

        });

    lineChart.getData().add(series);

    return lineChart;

  }

  /**
   * Rendering revenue share of products chart.
   *
   * @param orders queried orders
   * @return the chart
   */
  public PieChart createProductRevenue(ArrayList<Order> orders) {

    // Data collection part
    Map<String, Double> revenueMap = new HashMap<>();

    for (Order order : orders) {

      for (OrderItem item : order.getProducts()) {

        String productName = item.getProduct().getName();
        double revenue = item.getQuantity() * item.getUnitPrice();
        revenueMap.put(productName, revenueMap.getOrDefault(productName, 0.0) + revenue);

      }

    }

    // Rendering part
    PieChart pieChart = new PieChart();
    pieChart.setTitle("Product Revenue Share");

    for (Map.Entry<String, Double> entry : revenueMap.entrySet()) {

      if (entry.getValue() > 0) {

        pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));

      }

    }

    return pieChart;

  }
  
  /**
   * Chart rendering month vs revenue (last 12 months).
   *
   * @param orders queried orders
   * @return the chart
   */
  public BarChart<String, Number> createMonthlyRevenue(ArrayList<Order> orders) {

    //Data collection part
    Map<String, Double> monthlyRevenue = new LinkedHashMap<>();

    LocalDateTime now = LocalDateTime.now();

    for (int i = 11; i >= 0; i--) {

      LocalDateTime month = now.minusMonths(i);
      String label = month.getYear() + "-" + String.format("%02d", month.getMonthValue());
      monthlyRevenue.put(label, 0.0);

    }

    for (Order order : orders) {

      LocalDateTime date = order.getOrderDate().toLocalDateTime();
      String label = date.getYear() + "-" + String.format("%02d", date.getMonthValue());

      if (monthlyRevenue.containsKey(label)) {

        double updated = monthlyRevenue.get(label) + order.getAmountTotal();
        monthlyRevenue.put(label, updated);

      }

    }

    //Rendering part
    CategoryAxis xxAxis = new CategoryAxis();
    xxAxis.setLabel("Month");

    NumberAxis yyAxis = new NumberAxis();
    yyAxis.setLabel("Revenue in SEK");

    BarChart<String, Number> barChart = new BarChart<>(xxAxis, yyAxis);
    barChart.setTitle("Total Revenue of the last 12 Months");

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Revenue");

    for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {

      series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));

    }

    barChart.getData().add(series);
    return barChart;

  }

    
}
