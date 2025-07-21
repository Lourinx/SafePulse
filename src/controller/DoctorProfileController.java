package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.DoctorProfile;

public class DoctorProfileController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label licenseLabel;
    @FXML
    private Label registrationLabel; // optional if you add registration date

    private DoctorProfile currentDoctor;

    public void setDoctor(DoctorProfile doctor) {
        this.currentDoctor = doctor;
        updateDoctorInfo();
    }

    private void updateDoctorInfo() {
        if (currentDoctor != null) {
            nameLabel.setText(currentDoctor.getName());
            usernameLabel.setText(currentDoctor.getName()); // or use a username field if you add one
            licenseLabel.setText(currentDoctor.getLicenseNumber());
            registrationLabel.setText(currentDoctor.getHospital()); // change to registration date if you add it
        }
    }

    @FXML
    private void handleSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DoctorProfile.fxml"));
            Parent root = loader.load();

            DoctorProfileController controller = loader.getController();
            controller.setDoctor(currentDoctor);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Doctor Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
