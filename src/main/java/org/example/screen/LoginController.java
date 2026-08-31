package org.example.screen;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.app.BookingApp;
import org.example.entities.Person;
import org.example.repos.UserRepos;
import org.example.utility.Session;

public class LoginController {

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;
    @FXML private RadioButton userRadio;
    @FXML private RadioButton adminRadio;

    private final UserRepos userRepo = new UserRepos();

    @FXML
    private void handleLogin() {
        String username = usernameInput.getText() == null ? "" : usernameInput.getText().trim();
        String password = passwordInput.getText() == null ? "" : passwordInput.getText();
        String role = adminRadio.isSelected() ? "ADMIN" : "USER";

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter both username and password.").showAndWait();
            return;
        }

        Person person = userRepo.findByCredentials(username, password, role);

        if (person == null) {
            new Alert(Alert.AlertType.ERROR, "Invalid username, password or role.").showAndWait();
            return;
        }

        Session.login(person);
        BookingApp.showMainApp(person.canManageAllBookings() ? "OpeningScreen.fxml" : "Booking.fxml");
    }
}