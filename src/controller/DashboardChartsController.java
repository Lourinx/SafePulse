package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
// import javafx.scene.chart.CategoryAxis;
// import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.PatientData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardChartsController {

    @FXML
    private BarChart<String, Number> patientBarChart;
    @FXML
    private PieChart typeChart;
    @FXML
    private ComboBox<String> timeFilterCombo;

    @FXML
    private Label totalPatientsLabel;
    @FXML
    private Label avgAgeLabel;
    @FXML
    private Label newPatientsLabel;

    @FXML
    private VBox departmentBox;

    private List<PatientData> patients = new ArrayList<>();

    public void setPatients(List<PatientData> patients) {
        this.patients = patients != null ? new ArrayList<>(patients) : new ArrayList<>();
        System.out.println(" Received " + this.patients.size() + " patients");
        // Call updateStats after setting patients
        if (!this.patients.isEmpty()) {
            updateStats();
        }
    }

    @FXML
    public void initialize() {
        System.out.println(" Controller initialized");

        // Initialize combo box
        if (timeFilterCombo != null) {
            timeFilterCombo.getItems().addAll("Daily", "Weekly");
            timeFilterCombo.setValue("Daily");
            timeFilterCombo.setOnAction(e -> updateStats());
        }

        // Initialize chart properties
        if (patientBarChart != null) {
            patientBarChart.setTitle("Patient Admissions");
            patientBarChart.setLegendVisible(false);
        }

        if (typeChart != null) {
            typeChart.setTitle("Patient Types");
        }

        // Set default values for labels if they exist
        if (totalPatientsLabel != null)
            totalPatientsLabel.setText("0");
        if (avgAgeLabel != null)
            avgAgeLabel.setText("0.0 yrs");
        if (newPatientsLabel != null)
            newPatientsLabel.setText("0");
    }

    private void updateStats() {
        if (patients == null || patients.isEmpty()) {
            System.out.println("⚠️ No patients to display.");
            // Set default values when no data
            if (totalPatientsLabel != null)
                totalPatientsLabel.setText("0");
            if (avgAgeLabel != null)
                avgAgeLabel.setText("0.0 yrs");
            if (newPatientsLabel != null)
                newPatientsLabel.setText("0");
            return;
        }

        // Update total patients
        if (totalPatientsLabel != null) {
            totalPatientsLabel.setText(String.valueOf(patients.size()));
        }

        // Calculate and update average age
        double avgAge = patients.stream()
                .mapToInt(PatientData::getAge)
                .average()
                .orElse(0.0);
        if (avgAgeLabel != null) {
            avgAgeLabel.setText(String.format("%.1f yrs", avgAge));
        }

        // Count today's patients
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        long todayCount = patients.stream()
                .filter(p -> today.equals(p.getDateScanned()))
                .count();
        if (newPatientsLabel != null) {
            newPatientsLabel.setText(String.valueOf(todayCount));
        }

        // Update bar chart
        updateBarChart();

        // Update department list
        updateDepartmentList();

        // Update pie chart
        updatePieChart();
    }

    private void updateBarChart() {
        if (patientBarChart == null)
            return;

        String filter = timeFilterCombo != null ? timeFilterCombo.getValue() : "Daily";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        WeekFields weekFields = WeekFields.ISO;

        Map<String, Long> groupedCounts = new LinkedHashMap<>();

        for (PatientData p : patients) {
            try {
                LocalDate date = LocalDate.parse(p.getDateScanned(), formatter);
                String key = filter.equals("Weekly")
                        ? "Week " + date.get(weekFields.weekOfWeekBasedYear()) + " - " + date.getYear()
                        : p.getDateScanned();
                groupedCounts.put(key, groupedCounts.getOrDefault(key, 0L) + 1);
            } catch (Exception e) {
                System.err.println("Invalid date format: " + p.getDateScanned());
                // Use the original date string as fallback
                groupedCounts.put(p.getDateScanned(), groupedCounts.getOrDefault(p.getDateScanned(), 0L) + 1);
            }
        }

        patientBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Patients");

        groupedCounts.forEach((label, count) -> series.getData().add(new XYChart.Data<>(label, count)));

        patientBarChart.getData().add(series);
        System.out.println(" Bar chart entries: " + series.getData().size());
    }

    private void updateDepartmentList() {
        if (departmentBox == null)
            return;

        departmentBox.getChildren().clear();
        Map<String, Long> byDepartment = patients.stream()
                .filter(p -> p.getDepartment() != null && !p.getDepartment().trim().isEmpty())
                .collect(Collectors.groupingBy(PatientData::getDepartment, Collectors.counting()));

        if (byDepartment.isEmpty()) {
            Label noDataLabel = new Label("No department data available");
            noDataLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");
            departmentBox.getChildren().add(noDataLabel);
        } else {
            byDepartment.forEach((dept, count) -> {
                Label label = new Label(dept + ": " + count);
                label.setStyle("-fx-font-size: 14; -fx-text-fill: #333; -fx-padding: 2;");
                departmentBox.getChildren().add(label);
            });
        }
        System.out.println(" Departments listed: " + byDepartment.size());
    }

    private void updatePieChart() {
        if (typeChart == null)
            return;

        typeChart.getData().clear();
        Map<String, Long> byType = patients.stream()
                .filter(p -> p.getPatientType() != null && !p.getPatientType().trim().isEmpty())
                .collect(Collectors.groupingBy(PatientData::getPatientType, Collectors.counting()));

        if (byType.isEmpty()) {
            // Add a placeholder if no data
            typeChart.getData().add(new PieChart.Data("No Data", 1));
        } else {
            byType.forEach((type, count) -> {
                PieChart.Data slice = new PieChart.Data(type, count);
                typeChart.getData().add(slice);
            });
        }
        System.out.println("Pie chart slices: " + typeChart.getData().size());
    }

    // Method to refresh data manually if needed
    public void refreshCharts() {
        updateStats();
    }
}