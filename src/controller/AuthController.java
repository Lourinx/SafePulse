package controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DoctorProfile;
import model.DoctorXMLHandler;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class AuthController {

    // Login fields
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button togglePasswordBtn;

    // Sign-up fields
    @FXML
    private PasswordField signUpPasswordField;
    @FXML
    private TextField signUpPasswordTextField;
    @FXML
    private Button toggleSignUpPasswordBtn;
    @FXML
    private TextField signUpNameField;
    @FXML
    private TextField licenseNumberField;
    @FXML
    private TextField hospitalNameField;

    private static final Duration FADE_DURATION = Duration.millis(500);

    // ===== LOGIN =====
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordTextField.isVisible()
                ? passwordTextField.getText()
                : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter both username and password!");
            return;
        }

        List<DoctorProfile> doctors = DoctorXMLHandler.loadDoctors();
        if (doctors == null || doctors.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "No registered doctors found.");
            return;
        }

        DoctorProfile loggedInDoctor = null;
        for (DoctorProfile doc : doctors) {
            if (doc.getName().equalsIgnoreCase(username) && doc.getPassword().equals(password)) {
                loggedInDoctor = doc;
                break;
            }
        }

        if (loggedInDoctor != null) {
            System.out.println("[AuthController] Login successful for: " + loggedInDoctor.getName());
            switchToDashboard(event, loggedInDoctor);
        } else {
            showAlert(Alert.AlertType.ERROR, "Incorrect username or password!");
        }
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (passwordTextField.isVisible()) {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);

            togglePasswordBtn.setText("Show");
        } else {
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);

            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);

            togglePasswordBtn.setText("Hide");
        }
    }

    // ===== SIGN UP =====
    @FXML
    private void handleSignUp(ActionEvent event) {
        String name = signUpNameField.getText();
        String password = signUpPasswordField.isVisible()
                ? signUpPasswordField.getText()
                : signUpPasswordTextField.getText();
        String license = licenseNumberField.getText();
        String hospital = hospitalNameField.getText();

        if (name.isEmpty() || password.isEmpty() || license.isEmpty() || hospital.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        List<DoctorProfile> doctors = DoctorXMLHandler.loadDoctors();
        for (DoctorProfile doc : doctors) {
            if (doc.getName().equalsIgnoreCase(name) || doc.getLicenseNumber().equals(license)) {
                showAlert(Alert.AlertType.ERROR, "Doctor with this name or license number already exists.");
                return;
            }
        }

        DoctorProfile newDoctor = new DoctorProfile(name, password, license, hospital);
        DoctorXMLHandler.addDoctor(newDoctor);

        showAlert(Alert.AlertType.INFORMATION, "Doctor registered successfully. Please login.");
        switchToLogin(event);
    }

    @FXML
    private void toggleSignUpPasswordVisibility(ActionEvent event) {
        if (signUpPasswordField.isVisible()) {
            signUpPasswordTextField.setText(signUpPasswordField.getText());
            signUpPasswordField.setVisible(false);
            signUpPasswordField.setManaged(false);
            signUpPasswordTextField.setVisible(true);
            signUpPasswordTextField.setManaged(true);
            toggleSignUpPasswordBtn.setText("Hide");
        } else {
            signUpPasswordField.setText(signUpPasswordTextField.getText());
            signUpPasswordTextField.setVisible(false);
            signUpPasswordTextField.setManaged(false);
            signUpPasswordField.setVisible(true);
            signUpPasswordField.setManaged(true);
            toggleSignUpPasswordBtn.setText("Show");
        }
    }

    // ===== SCENE SWITCHING =====

    @FXML
    private void switchToSignUp(ActionEvent event) {
        switchSceneWithFade(event, "/view/Signup.fxml", "SafePulseID - Sign Up");
    }

    @FXML
    private void switchToLogin(ActionEvent event) {
        switchToLoginWithFade(event);
    }

    private void switchToLoginWithFade(ActionEvent event) {
        switchSceneWithFade(event, "/view/Login.fxml", "SafePulseID - Login");
    }

    private void switchToDashboardWithFade(ActionEvent event, DoctorProfile doctor) {
        try {
            // Get current scene and stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Store current stage dimensions
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            boolean wasFullScreen = stage.isFullScreen();

            // Create fade out transition for current scene
            FadeTransition fadeOut = new FadeTransition(FADE_DURATION, currentScene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                try {
                    // Load new scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
                    Parent dashboardRoot = loader.load();

                    // Set up the controller with logged in doctor
                    DashboardController controller = loader.getController();
                    controller.setLoggedInDoctor(doctor);

                    // Create new scene with same dimensions as current scene
                    Scene newScene = new Scene(dashboardRoot, currentWidth, currentHeight);
                    dashboardRoot.setOpacity(0.0);

                    // Set new scene without changing window state
                    stage.setScene(newScene);
                    stage.setTitle("SafePulseID - Dashboard");

                    // Restore fullscreen state if it was fullscreen
                    if (wasFullScreen) {
                        stage.setFullScreen(true);
                    }

                    // Create fade in transition for new scene
                    FadeTransition fadeIn = new FadeTransition(FADE_DURATION, dashboardRoot);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error loading dashboard.");
                }
            });

            fadeOut.play();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error during scene transition.");
        }
    }

    private void switchSceneWithFade(ActionEvent event, String fxmlPath, String title) {
        try {
            // Get current scene and stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Store current stage dimensions and state
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            boolean wasFullScreen = stage.isFullScreen();

            // Create fade out transition for current scene
            FadeTransition fadeOut = new FadeTransition(FADE_DURATION, currentScene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                try {
                    // Load new scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                    Parent newRoot = loader.load();

                    // Create new scene with same dimensions as current scene
                    Scene newScene = new Scene(newRoot, currentWidth, currentHeight);
                    newRoot.setOpacity(0.0);

                    // Set new scene without changing window state
                    stage.setScene(newScene);
                    stage.setTitle(title);

                    // Restore fullscreen state if it was fullscreen
                    if (wasFullScreen) {
                        stage.setFullScreen(true);
                    }

                    // Create fade in transition for new scene
                    FadeTransition fadeIn = new FadeTransition(FADE_DURATION, newRoot);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    String sceneName = fxmlPath.substring(fxmlPath.lastIndexOf('/') + 1, fxmlPath.lastIndexOf('.'));
                    showAlert(Alert.AlertType.ERROR, "Error loading " + sceneName + " page.");
                }
            });

            fadeOut.play();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error during scene transition.");
        }
    }

    private void switchToDashboard(ActionEvent event, DoctorProfile doctor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            DashboardController controller = loader.getController();
            controller.setLoggedInDoctor(doctor);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("SafePulseID - Dashboard");
            stage.setFullScreen(true); // Set to full screen mode
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading dashboard.");
        }
    }

    // ===== ALERT UTILITY =====
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("SafePulseID");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
