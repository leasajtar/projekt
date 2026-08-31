package org.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.utility.DbUtil;
import org.example.utility.Session;

public class BookingApp extends Application {

    private static Stage primaryStage;
    private static BorderPane mainRoot;

    public void start(Stage stage) {
        DbUtil.init();
        primaryStage = stage;

        showLoginScreen();

        primaryStage.setTitle("Booking Management System");
        primaryStage.show();
    }

    public static void showLoginScreen() {
        mainRoot = null;
        Parent login = loadFXML("Login.fxml");
        primaryStage.setScene(new Scene(login, 480, 420));
    }

    public static void showOpeningScreen() {
        Parent opening = loadFXML("OpeningScreen.fxml");
        primaryStage.setScene(new Scene(opening, 1024, 720));
    }

    public static void showMainApp(String centerFxml) {
        if (mainRoot == null) {
            mainRoot = new BorderPane();
        }
        mainRoot.setTop(buildMenuBar());
        mainRoot.setCenter(loadFXML(centerFxml));
        primaryStage.setScene(new Scene(mainRoot, 1024, 720));
    }

    private static MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        boolean admin = Session.isAdmin();

        Menu info = new Menu("Info");
        MenuItem home = new MenuItem(admin ? "Home" : "My Bookings");
        home.setOnAction(e -> showMainApp(admin ? "OpeningScreen.fxml" : "Booking.fxml"));
        info.getItems().add(home);

        if (admin) {
            MenuItem allBookings = new MenuItem("All Bookings");
            allBookings.setOnAction(e -> showMainApp("Booking.fxml"));
            info.getItems().add(allBookings);
        }
        menuBar.getMenus().add(info);

        if (admin) {
            Menu add = new Menu("Add");
            MenuItem userAdd = new MenuItem("Add User");
            MenuItem itemAdd = new MenuItem("Add Event");
            MenuItem bookingAdd = new MenuItem("Add Booking");
            userAdd.setOnAction(e -> showMainApp("UserAdd.fxml"));
            itemAdd.setOnAction(e -> showMainApp("EventAdd.fxml"));
            bookingAdd.setOnAction(e -> showMainApp("BookingAdd.fxml"));
            add.getItems().addAll(userAdd, itemAdd, bookingAdd);
            menuBar.getMenus().add(add);

            Menu reports = new Menu("Reports");
            MenuItem price = new MenuItem("Min/Max Price");
            price.setOnAction(e -> showMainApp("Price.fxml"));
            reports.getItems().add(price);
            menuBar.getMenus().add(reports);
        } else {
            Menu myAccount = new Menu("My Account");
            MenuItem bookEvent = new MenuItem("Book an Event");
            MenuItem myPrice = new MenuItem("My Min/Max Price");
            bookEvent.setOnAction(e -> showMainApp("BookingAdd.fxml"));
            myPrice.setOnAction(e -> showMainApp("Price.fxml"));
            myAccount.getItems().addAll(bookEvent, myPrice);
            menuBar.getMenus().add(myAccount);
        }

        Menu account = new Menu("Account");
        MenuItem logout = new MenuItem("Log out");
        logout.setOnAction(e -> {
            Session.logout();
            showLoginScreen();
        });
        account.getItems().add(logout);
        menuBar.getMenus().add(account);

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