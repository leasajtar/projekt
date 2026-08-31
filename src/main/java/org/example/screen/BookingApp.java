package org.example.screen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.utility.DbUtil;

public class BookingApp extends Application {

    private static Stage primaryStage;
    private static BorderPane mainRoot;

    public void start(Stage stage) {
        DbUtil.init();
        primaryStage = stage;

        showOpeningScreen();

        primaryStage.setTitle("Booking Management System");
        primaryStage.show();
    }


    public static void showOpeningScreen() {
        Parent opening = loadFXML("OpeningScreen.fxml");
        primaryStage.setScene(new Scene(opening, 1024, 720));
    }


    public static void showMainApp(String centerFxml) {
        if (mainRoot == null) {
            mainRoot = new BorderPane();
            mainRoot.setTop(buildMenuBar());
        }
        mainRoot.setCenter(loadFXML(centerFxml));
        primaryStage.setScene(new Scene(mainRoot, 1024, 720));
    }

    private static MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu info = new Menu("Info");
        Menu add = new Menu("Add");

        MenuItem home = new MenuItem("Home");
        MenuItem booking = new MenuItem("Booking");
        MenuItem price = new MenuItem("Min/Max Price");

        MenuItem userAdd = new MenuItem("Add User");
        MenuItem itemAdd = new MenuItem("Add Event");
        MenuItem bookingAdd = new MenuItem("Add Booking");

        home.setOnAction(e -> showMainApp("OpeningScreen.fxml"));
        booking.setOnAction(e -> showMainApp("Booking.fxml"));
        price.setOnAction(e -> showMainApp("Price.fxml"));

        userAdd.setOnAction(e -> showMainApp("UserAdd.fxml"));
        itemAdd.setOnAction(e -> showMainApp("EventAdd.fxml"));
        bookingAdd.setOnAction(e -> showMainApp("BookingAdd.fxml"));

        info.getItems().addAll(home, booking, price);
        add.getItems().addAll(userAdd, itemAdd, bookingAdd);

        menuBar.getMenus().addAll(info, add);
        return menuBar;
    }


    private static Parent loadFXML(String fxmlFile) {
        try {
            return FXMLLoader.load(
                    BookingApp.class.getResource("/org/example/" + fxmlFile)
            );
        } catch (Exception _) {
            return new Label("Failed to load: " + fxmlFile);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
