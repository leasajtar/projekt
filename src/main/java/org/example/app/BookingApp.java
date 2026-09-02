package org.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.utility.CredentialsFileService;
import org.example.utility.DbUtil;
import org.example.utility.Session;

/**
 * Glavna JavaFX klasa aplikacije. Pri pokretanju inicijalizira bazu i zadani
 * admin račun, prikazuje ekran za prijavu, te nakon prijave gradi izbornik
 * ovisno o ulozi prijavljene osobe (administrator vidi sve opcije, obični
 * korisnik samo vlastite rezervacije i vlastitu cijenu).
 */
public class BookingApp extends Application {

    private static Stage primaryStage;
    private static BorderPane mainRoot;



    /**
     * Pokreće aplikaciju: inicijalizira bazu i zadani admin račun, te prikazuje ekran za prijavu.
     *
     * @param stage glavni prozor aplikacije
     */
    public void start(Stage stage) {
        DbUtil.init();
        CredentialsFileService.ensureDefaultAdmin();
        setPrimaryStage(stage);

        showLoginScreen();

        primaryStage.setTitle("Booking Management System");
        primaryStage.show();
    }

    /** @param stage glavni prozor aplikacije koji treba zapamtiti za kasnije promjene scene */
    private static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /** Prikazuje ekran za prijavu i briše postojeći glavni izbornik (npr. nakon odjave). */
    public static void showLoginScreen() {
        mainRoot = null;
        Parent login = loadFXML("Login.fxml");
        primaryStage.setScene(new Scene(login, 480, 420));
    }

    /** Prikazuje početni (administratorski) ekran izvan glavnog izbornika. */
    public static void showOpeningScreen() {
        Parent opening = loadFXML("OpeningScreen.fxml");
        primaryStage.setScene(new Scene(opening, 1024, 720));
    }

    /**
     * Prikazuje glavni prozor aplikacije (s izbornikom pri vrhu) s danim FXML-om u
     * središnjem dijelu. Izbornik se ponovno gradi pri svakom pozivu kako bi
     * odražavao trenutnu ulogu prijavljene osobe.
     *
     * @param centerFxml naziv FXML datoteke koju treba prikazati u središnjem dijelu prozora
     */
    public static void showMainApp(String centerFxml) {
        if (mainRoot == null) {
            mainRoot = new BorderPane();
        }
        mainRoot.setTop(buildMenuBar());
        mainRoot.setCenter(loadFXML(centerFxml));
        primaryStage.setScene(new Scene(mainRoot, 1024, 720));
    }

    /**
     * Gradi izbornik prilagođen ulozi trenutno prijavljene osobe, administrator
     * dobiva pristup svim funkcijama, obični korisnik samo vlastitim rezervacijama.
     *
     * @return izgrađeni izbornik
     */
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
            MenuItem history = new MenuItem("History / Backup Monitor");
            history.setOnAction(e -> showMainApp("History.fxml"));
            reports.getItems().add(history);
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

    /**
     * Učitava FXML datoteku.
     *
     * @param fxmlFile naziv FXML datoteke
     * @return učitani korijenski čvor, ili oznaka s porukom o grešci ako učitavanje ne uspije
     */
    private static Parent loadFXML(String fxmlFile) {
        try {
            return FXMLLoader.load(
                    BookingApp.class.getResource("/org/example/" + fxmlFile)
            );
        } catch (Exception _) {
            return new Label("Failed to load: " + fxmlFile);
        }
    }

    /**
     * Ulazna točka aplikacije.
     *
     * @param args argumenti komandne linije
     */
    public static void main(String[] args) {
        launch(args);
    }
}