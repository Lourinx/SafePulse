package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.PatientData;
import model.Medication;
import model.LabTest;
import model.Admission;
import model.Vaccine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MedicalRecordController {

        // Patient Basic Info
        @FXML
        private Label nameLabel, genderLabel, ageLabel, bloodLabel;
        @FXML
        private Label nationalIdLabel, insuranceLabel, contactLabel, dobLabel, scanTimeLabel;

        // Medical Summary
        @FXML
        private Label chiefComplaintLabel, currentConditionLabel, triageCategoryLabel, riskLevelLabel;

        // Tables
        @FXML
        private TableView<Medication> medicationTable;
        @FXML
        private TableColumn<Medication, String> medNameCol, dosageCol, frequencyCol, routeCol, startDateCol,
                        prescribedByCol;

        @FXML
        private TableView<LabTest> labTestTable;
        @FXML
        private TableColumn<LabTest, String> testNameCol, testResultCol, normalRangeCol, testDateCol;

        @FXML
        private TableView<Admission> admissionTable;
        @FXML
        private TableColumn<Admission, String> admissionDateCol, departmentCol, diagnosisCol, dischargeDateCol,
                        doctorCol;

        @FXML
        private TableView<Vaccine> vaccineTable;
        @FXML
        private TableColumn<Vaccine, String> vaccineCol, vaccineDateCol, vaccineStatusCol;

        // Lists
        @FXML
        private VBox allergyList, familyHistoryList, attachmentList;

        // Notes
        @FXML
        private Label erNoteContent, erNoteMeta;

        // AI Summary
        @FXML
        private Label aiSummaryLabel, specialistReferralLabel;

        // Buttons
        @FXML
        private Button backButton, printButton, savePdfButton;

        private PatientData patient;

        public void setPatientData(PatientData patient) {
                this.patient = patient;
                populateData();
        }

        public void initialize() {
                // Medication Table column setup
                medNameCol.setCellValueFactory(new PropertyValueFactory<>("medName"));
                dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));
                frequencyCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
                routeCol.setCellValueFactory(new PropertyValueFactory<>("route"));
                startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
                prescribedByCol.setCellValueFactory(new PropertyValueFactory<>("prescribedBy"));

                // LabTest columns
                testNameCol.setCellValueFactory(new PropertyValueFactory<>("testName"));
                testResultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
                normalRangeCol.setCellValueFactory(new PropertyValueFactory<>("normalRange"));
                testDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

                // Admission columns
                admissionDateCol.setCellValueFactory(new PropertyValueFactory<>("admissionDate"));
                departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
                diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
                dischargeDateCol.setCellValueFactory(new PropertyValueFactory<>("dischargeDate"));
                doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctor"));

                // Vaccine columns
                vaccineCol.setCellValueFactory(new PropertyValueFactory<>("vaccineName"));
                vaccineDateCol.setCellValueFactory(new PropertyValueFactory<>("dateTaken"));
                vaccineStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

                // Clear and prepare initial empty tables
                medicationTable.setItems(FXCollections.observableArrayList());
                labTestTable.setItems(FXCollections.observableArrayList());
                admissionTable.setItems(FXCollections.observableArrayList());
                vaccineTable.setItems(FXCollections.observableArrayList());

                // Clear dynamic VBox lists
                allergyList.getChildren().clear();
                familyHistoryList.getChildren().clear();
                attachmentList.getChildren().clear();

                // Set empty ER notes initially
                erNoteContent.setText("");
                erNoteMeta.setText("");

                // Set empty AI summaries
                aiSummaryLabel.setText("");
                specialistReferralLabel.setText("");
        }

        private void populateData() {
                if (patient == null)
                        return;

                // === Basic Info ==
                nameLabel.setText(patient.getName());
                ageLabel.setText(String.valueOf(patient.getAge()));
                genderLabel.setText(patient.getGender());
                bloodLabel.setText(patient.getBloodType());
                nationalIdLabel.setText(patient.getNationalId());
                insuranceLabel.setText(patient.getInsurance());
                contactLabel.setText(patient.getContact());
                dobLabel.setText(patient.getDob());
                scanTimeLabel.setText(patient.getTimeScanned());

                // === Medical Summary ===
                chiefComplaintLabel.setText("Complains of chest tightness and shortness of breath.");
                currentConditionLabel.setText("Condition improving. Still under cardiac monitoring.");
                triageCategoryLabel.setText("Red – Emergency");
                riskLevelLabel.setText("High");

                // === ER Notes ===
                erNoteContent.setText("Patient arrived with elevated BP and HR. ECG requested.");
                erNoteMeta.setText("Dr. Rizki – 15 Jul 2025 10:32 AM");

                // === AI Summary ===
                aiSummaryLabel.setText("Heart rhythm stabilized. Continue observation.");
                specialistReferralLabel.setText("Cardiologist referral required after discharge.");

                // === Medications ===
                ObservableList<Medication> meds = FXCollections.observableArrayList(
                                new Medication("Lisinopril", "10mg", "Once daily", "Oral", "10 Jul 2025", "Dr. Fadil"),
                                new Medication("Amlodipine", "5mg", "Once daily", "Oral", "12 Jul 2025", "Dr. Lina"),
                                new Medication("Metoprolol", "50mg", "Twice daily", "Oral", "13 Jul 2025", "Dr. Sara"));
                medicationTable.setItems(meds);

                // === Lab Tests ===
                labTestTable.getItems().setAll(
                                new LabTest("CBC", "Normal", "4.5–11 x10⁹/L", "13 Jul 2025"),
                                new LabTest("Blood Glucose", "110 mg/dL", "70–100 mg/dL", "13 Jul 2025"),
                                new LabTest("ECG", "Irregular rhythm", "Normal sinus", "15 Jul 2025"));

                // === Admissions ===
                admissionTable.getItems().setAll(
                                new Admission("15 Jul 2025", "ER", "Hypertensive Crisis", "17 Jul 2025", "Dr. Hani"),
                                new Admission("20 Mar 2025", "Cardiology", "Angina", "25 Mar 2025", "Dr. Lina"));

                // === Vaccinations ===
                vaccineTable.getItems().setAll(
                                new Vaccine("COVID-19 (Pfizer)", "01 Mar 2023", "Completed"),
                                new Vaccine("Tetanus Booster", "20 Feb 2024", "Completed"),
                                new Vaccine("Influenza", "10 Oct 2024", "Due"));

                // === Allergies ===
                allergyList.getChildren().setAll(
                                new Label("- Penicillin"),
                                new Label("- Seafood (shrimp, crab)"),
                                new Label("- Pollen"));

                // === Family History ===
                familyHistoryList.getChildren().setAll(
                                new Label("- Father: Hypertension"),
                                new Label("- Mother: Type 2 Diabetes"),
                                new Label("- Sibling: Asthma"));

                // === Attachments ===
                attachmentList.getChildren().setAll(
                                new Label("→ ECG Results.pdf"),
                                new Label("→ Lab Results - July 2025.pdf"),
                                new Label("→ Chest X-Ray Scan.jpeg"));
        }

}
