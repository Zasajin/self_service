package org.example.screens;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.buttons.BackBtnWithTxt;
import org.example.buttons.LangBtn;
import org.example.kiosk.LanguageSetting;
import org.example.orders.Order;
import org.example.sql.SqlQueries;

/**
 * Order History class.
 */
public class AdminOrdHistoryScreen {

  private SqlQueries queries;

  /**
   * This is the constructor of the order history.
   * (Used only for assigning variables.)
   */
  public AdminOrdHistoryScreen() {
    queries = new SqlQueries();
  }

  /**
   * Scene to display the order history.
   *
   * @param primaryStage this window
   * @param prevScene    pevious scene to go back to
   * @return the scene itself
   */
  public Scene showHistoryScene(Stage primaryStage, Scene prevScene) {

    // So the admin doesnt forget where he is lol
    Label historyLabel = new Label("Order History:");
    historyLabel.setStyle(
        "-fx-font-size: 45px;"
            + "-fx-font-weight: bold;");

    // Setting up the table
    TableColumn<Order, Integer> idColumn = new TableColumn<>("Order ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    idColumn.setPrefWidth(150);
    idColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, Integer> kioskColumn = new TableColumn<>("Kiosk ID");
    kioskColumn.setCellValueFactory(new PropertyValueFactory<>("kioskId"));
    kioskColumn.setPrefWidth(150);
    kioskColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, String> productsColumn = new TableColumn<>("Products");
    productsColumn.setCellValueFactory(new PropertyValueFactory<>("productSummary"));

    // To enable multi line string in one cell
    productsColumn.setCellFactory(column -> {
      return new TableCell<>() {

        private final Label label = new Label();

        {

          label.setWrapText(true);
          label.setStyle(
              "-fx-padding: 5px;"
                  + "-fx-alignment: Center;");
          label.maxWidthProperty().bind(this.widthProperty().subtract(10));
          label.setMinHeight(Region.USE_PREF_SIZE);
          setGraphic(label);

        }

        // Overrides default cell update behaviour of javafx

        @Override
        protected void updateItem(String item, boolean empty) {

          super.updateItem(item, empty);

          if (empty || item == null) {

            label.setText(null);
            setGraphic(null);

          } else {

            label.setText(item);
            setGraphic(label);

          }

        }

      };

    });
    productsColumn.setPrefWidth(800);
    TableColumn<Order, Double> amountColumn = new TableColumn<>("Amount Total");
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountTotal"));
    amountColumn.setPrefWidth(250);
    amountColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, String> statusColumn = new TableColumn<>("Status");
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    statusColumn.setPrefWidth(200);
    statusColumn.setStyle("-fx-alignment: CENTER;");
    TableColumn<Order, Timestamp> dateColumn = new TableColumn<>("Order Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    dateColumn.setPrefWidth(250);
    dateColumn.setStyle("-fx-alignment: CENTER;");

    // Displaying the fetched data in a neat table
    TableView<Order> historyTable = new TableView<>();
    historyTable.setFixedCellSize(-1);
    historyTable.setMaxWidth(Double.MAX_VALUE);
    historyTable.setPrefWidth(Region.USE_COMPUTED_SIZE);

    // Puts the table together
    historyTable.getColumns().add(idColumn);
    historyTable.getColumns().add(kioskColumn);
    historyTable.getColumns().add(productsColumn);
    historyTable.getColumns().add(amountColumn);
    historyTable.getColumns().add(statusColumn);
    historyTable.getColumns().add(dateColumn);

    // Querys data into the table
    try {

      // Gets orders
      ArrayList<Order> orders = queries.queryOrders();
      // Gets Products for each order
      queries.queryOrderItemsFor(orders);
      // Inputs it into the table
      historyTable.getItems().addAll(orders);

    } catch (SQLException e) {

      e.printStackTrace();

    }

    // VBox for the table
    VBox orderHistory = new VBox(historyTable);
    VBox.setVgrow(historyTable, Priority.ALWAYS);
    historyTable.prefWidthProperty().bind(orderHistory.widthProperty());
    orderHistory.setPadding(new Insets(20, 0, 0, 0));

    // Upper part of the screen
    VBox topContainer = new VBox();
    topContainer.setMaxWidth(Double.MAX_VALUE);
    topContainer.setAlignment(Pos.TOP_CENTER);
    topContainer.setSpacing(40);
    VBox.setVgrow(orderHistory, Priority.ALWAYS);
    topContainer.getChildren().addAll(historyLabel, orderHistory);

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
    var langButton = new LangBtn();

    // Bottom row of the screen
    HBox bottomContainer = new HBox();
    bottomContainer.setAlignment(Pos.BOTTOM_LEFT);
    bottomContainer.getChildren().addAll(langButton, spacerBottom, backButton);

    // Setting positioning of all the elements
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(50));
    layout.setCenter(topContainer);
    layout.setBottom(bottomContainer);
    BorderPane.setMargin(bottomContainer, new Insets(40, 0, 0, 0));

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

    Scene historyScene = new Scene(layout, 1920, 1080);

    return historyScene;

  }

  // Query for the orders

}