package org.example.screen;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.app.BookingApp;
import org.example.entities.Admin;
import org.example.entities.Person;
import org.example.repos.UserRepos;
import org.example.utility.CredentialsFileService;
import org.example.utility.Session;

/**Kontroler ekrana za prijavu. Sadrzi metode validacije podataka korisnika i povezivanja s bazom podataka.*/
public class LoginController {

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;
    @FXML private RadioButton userRadio;
    @FXML private RadioButton adminRadio;

    private final UserRepos userRepo = new UserRepos();

    /**Obrada pokusaja ulogiravanja korisnika. Provjerava ispravnost unesenih podataka i provjerava
     * je li korisnik {@link Admin} ili {@link org.example.entities.User}, postavlja sesiju i prebacuje
     * na glavni ekran prema ulozi*/
    @FXML
    private void handleLogin() {
        String username = usernameInput.getText() == null ? "" : usernameInput.getText().trim();
        String password = passwordInput.getText() == null ? "" : passwordInput.getText();
        String role = adminRadio.isSelected() ? "ADMIN" : "USER";

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter both username and password.").showAndWait();
            return;
        }

        boolean valid = CredentialsFileService.validate(username, password, role);
        if (!valid) {
            new Alert(Alert.AlertType.ERROR, "Invalid username, password or role.").showAndWait();
            return;
        }

        Person person = "ADMIN".equals(role)
                ? new Admin(0, username, "", "", "")
                : userRepo.findByUsername(username);

        if (person == null) {
            new Alert(Alert.AlertType.ERROR,
                    "Login accepted, but no matching user record exists in the database.").showAndWait();
            return;
        }

        Session.login(person);
        BookingApp.showMainApp(person.canManageAllBookings() ? "OpeningScreen.fxml" : "Booking.fxml");
    }
}