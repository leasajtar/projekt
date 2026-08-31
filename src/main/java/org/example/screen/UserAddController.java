package org.example.screen;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.entities.User;
import org.example.utility.Util;
import org.example.repos.UserRepos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAddController {

    private static final Logger logger = LoggerFactory.getLogger(UserAddController.class);

    @FXML private TextField usernameAdd;
    @FXML private TextField emailAdd;
    @FXML private TextField passwordAdd;
    @FXML private TextField phoneAdd;
    @FXML private Button addBtn;

    private final UserRepos userRepo = new UserRepos();

    @FXML
    public void addUser() {
        String username = safe(usernameAdd.getText());
        String password = safe(passwordAdd.getText());
        String email = safe(emailAdd.getText());
        String phone = safe(phoneAdd.getText());

        if (username.isEmpty() || password.isEmpty() || (email.isEmpty() && phone.isEmpty())) {
            new Alert(Alert.AlertType.WARNING,
                    "Please fill username, password, and either email or phone."
            ).showAndWait();
            return;
        }

        if (userRepo.usernameExists(username)) {
            new Alert(Alert.AlertType.WARNING, "Username already exists.").showAndWait();
            return;
        }

        if (!email.isEmpty() && !Util.emailValidate(email)) {
            new Alert(Alert.AlertType.WARNING, "Invalid email address.\n\t example: email@email.com").showAndWait();
            return;
        }

        if (!phone.isEmpty()) {
            if (phone.length() != 10 && phone.length() != 11) {
                new Alert(Alert.AlertType.WARNING, "Invalid phone number.\n\t example: 0911231234").showAndWait();
                return;
            }
        }

        if (!Util.passwordValidate(password)) {
            new Alert(Alert.AlertType.WARNING,
                    "Password is invalid.\nMust have:\n\t - 1 uppercase\n\t - 1 lowercase\n\t - 1 number\n\t - 1 special character"
            ).showAndWait();
            return;
        }

        User newUser = new User.UserBuilder(0, username, password)
                .email(email.isEmpty() ? null : email)
                .phone(phone.isEmpty() ? null : phone)
                .build();

        try {
            userRepo.insert(newUser);
            new Alert(Alert.AlertType.INFORMATION, "User added successfully! ✅").showAndWait();

            usernameAdd.clear();
            passwordAdd.clear();
            emailAdd.clear();
            phoneAdd.clear();

        } catch (Exception e) {
            logger.error("Failed to save user", e);
            new Alert(Alert.AlertType.ERROR, "Failed to save user: " + e.getMessage()).showAndWait();
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}