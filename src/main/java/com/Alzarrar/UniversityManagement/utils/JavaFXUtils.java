package com.Alzarrar.UniversityManagement.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class JavaFXUtils {

    public static void showAlert(final AlertType alertType, final String title,
                                 final String errorMessage) {
        final Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public static void showSuccessMessage(final String successMessage) {
        final Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(successMessage);
        alert.showAndWait();
    }

    public static void showWarningMessage(final String errorMessage) {
        final Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public static void showError(final String errorMessage) {
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}