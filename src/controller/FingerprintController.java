package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
import javafx.scene.control.Label;
// import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
// import javafx.stage.Stage;
import model.PatientData;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;

public class FingerprintController {

    @FXML
    private ImageView fingerImage;
    
    @FXML
    private Label statusLabel;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private void handleScan() {
        // Set status
        statusLabel.setText("Scanning...");

        // Load and play the video
        String videoPath = getClass().getResource("/videos/fingerprint-animation.mp4").toExternalForm();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        videoView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);

        mediaPlayer.setOnEndOfMedia(() -> {
            statusLabel.setText("Scan complete! Patient added.");
            mediaPlayer.stop();

            // Add the mock patient
            PatientData mock = new PatientData("Fingerprint Patient", 65, "O+", "Pending", "BPJS", "10:34 AM",
                    "15 Jul 2025",
                    "Male", "+62 812-3456-7890", "05 May 1960", "IDN6543210");
            mock.setDepartment("Cardiology");
            mock.setPatientType("Inpatient");
            DashboardController.getPatientList().add(mock);

        });
    }

    @FXML
    private void handleOpenChart() {
        loadScene("/view/ChartView.fxml");
    }

    @FXML
    private void handleBackToDashboard() {
        loadScene("/view/Dashboard.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Node content = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainAnchor.getChildren().setAll(content);
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private MediaView videoView;
    private MediaPlayer mediaPlayer;

}
