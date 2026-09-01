package org.example.screen;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.exceptions.InvalidNumberInputException;
import org.example.repos.ItemRepos;
import org.example.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class EventAddController {

    private static final Logger logger = LoggerFactory.getLogger(EventAddController.class);

    @FXML private TextField addEventNameInput;
    @FXML private TextField addEventPriceInput;
    @FXML private Button addBtn;

    private final ItemRepos itemRepo = new ItemRepos();

    @FXML
    public void addEvent() {
        String name = safe(addEventNameInput.getText());
        String priceText = safe(addEventPriceInput.getText());

        if (name.isEmpty() || priceText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in event name and price.").showAndWait();
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidNumberInputException("Price must be greater than zero.");

            }
        } catch (InvalidNumberInputException iniex) {
            logger.error("Invalid price input", iniex);
            new Alert(Alert.AlertType.WARNING, iniex.getMessage()).showAndWait();
            return;
        }
        catch (NumberFormatException _) {
            logger.error("Invalid price input");
            new Alert(Alert.AlertType.WARNING, "Price must be a number (e.g. 200 or 200.50).").showAndWait();
            return;
        }

        if (itemRepo.eventExists(name)) {
            logger.error("Duplicate event name");
            new Alert(Alert.AlertType.WARNING, "That event already exists.").showAndWait();
            return;
        }

        Item newItem = new Item(0, name.toUpperCase(), price);

        try {
            itemRepo.insert(newItem);
            logger.info("Event added successfully");
            new Alert(Alert.AlertType.INFORMATION, "Event added successfully!").showAndWait();

            addEventNameInput.clear();
            addEventPriceInput.clear();

        } catch (Exception e) {
            logger.error("Failed to save event", e);
            new Alert(Alert.AlertType.ERROR, "Failed to save event: " + e.getMessage()).showAndWait();
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}