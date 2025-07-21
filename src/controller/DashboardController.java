package controller;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import model.Note;
import model.PatientData;
import model.DoctorProfile;
import util.NoteStorageUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableView<PatientData> table;
    @FXML
    private TableColumn<PatientData, String> colName;
    @FXML
    private TableColumn<PatientData, Integer> colAge;
    @FXML
    private TableColumn<PatientData, String> colBlood;
    @FXML
    private TableColumn<PatientData, String> colStatus;
    @FXML
    private TableColumn<PatientData, String> colInsurance;
    @FXML
    private TableColumn<PatientData, String> colTime;
    @FXML
    private TableColumn<PatientData, String> colDate;

    @FXML
    private TextField searchField;
    @FXML
    private Label mainUserInfoLabel;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private Label pendingCasesLabel;
    @FXML
    private Label handledCasesLabel;
    @FXML
    private Label totalCasesLabel;
    @FXML
    private VBox notesSection;
    @FXML
    private DatePicker dateFilter;
    @FXML
    private Label handleAddNote;
    @FXML
    private Label viewStatsLabel;
    @FXML
    private AnchorPane mainAnchor;

    private ObservableList<PatientData> data = FXCollections.observableArrayList();
    private PatientData currentSelectedPatient;
    private DoctorProfile loggedInDoctor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("🚀 Initializing Dashboard Controller...");
        initTable();
        loadData();
        setupSearchAndFilters();
        updateCaseCounts();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            currentSelectedPatient = newVal;
            if (newVal != null)
                displayNotes(newVal);
        });

        System.out.println("✅ Dashboard Controller initialized successfully");
    }

    private void initTable() {
        table.setRowFactory(tv -> {
            TableRow<PatientData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    PatientData clickedPatient = row.getItem();
                    openDetailScene(clickedPatient);
                }
            });
            return row;
        });

        table.setEditable(true);

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colBlood.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colInsurance.setCellValueFactory(new PropertyValueFactory<>("insurance"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("timeScanned"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateScanned"));

        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn("Pending", "Handled"));
        colStatus.setOnEditCommit(event -> {
            PatientData patient = event.getRowValue();
            patient.setStatus(event.getNewValue());
            table.refresh();
            updateCaseCounts();
        });
    }

    private void loadData() {
        System.out.println("Loading patient data...");

        // Clear existing data first
        // data.clear();
        patientList.clear();

        PatientData p1 = new PatientData("Ahmad Salem", 65, "O+", "Pending", "BPJS", "10:34 AM", "15 Jul 2025",
                "Male", "+62 812-3456-7890", "05 May 1960", "IDN6543210");
        p1.setDepartment("Cardiology");
        p1.setPatientType("Inpatient");
        try {
            p1.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p1.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p1.getName());
        }

        PatientData p2 = new PatientData("Huda M.", 72, "A-", "Handled", "Mandiri", "09:52 AM", "15 Jul 2025",
                "Female", "+62 813-5555-1234", "12 Dec 1952", "IDN8745123");
        p2.setDepartment("Neurology");
        p2.setPatientType("Outpatient");
        try {
            p2.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p2.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p2.getName());
        }

        PatientData p3 = new PatientData("Sami F.", 81, "B+", "Pending", "BPJS", "11:18 AM", "15 Jul 2025",
                "Male", "+62 811-2222-7777", "30 Aug 1943", "IDN9977423");
        p3.setDepartment("ER");
        p3.setPatientType("Inpatient");
        try {
            p3.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p3.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p3.getName());
        }

        PatientData p4 = new PatientData("Leen Mohd.", 29, "AB-", "Pending", "Prudential", "08:22 AM", "15 Jul 2025",
                "Female", "+62 814-9090-1111", "18 Mar 1996", "IDN2223344");
        p4.setDepartment("Dermatology");
        p4.setPatientType("Outpatient");
        try {
            p4.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p4.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p4.getName());
        }

        PatientData p5 = new PatientData("Salem Ali", 65, "A+", "Pending", "BPJS", "12:00 PM", "07 Jul 2025",
                "Male", "+62 812-1111-2222", "03 Mar 1960", "IDN8833221");
        p5.setDepartment("Cardiology");
        p5.setPatientType("Inpatient");
        try {
            p5.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p5.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p5.getName());
        }

        PatientData p6 = new PatientData("Dina R.", 44, "O-", "Handled", "Mandiri", "01:22 PM", "11 Jul 2025",
                "Female", "+62 815-3333-6666", "11 Jul 1981", "IDN5566778");
        p6.setDepartment("Neurology");
        p6.setPatientType("Outpatient");
        try {
            p6.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p6.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p6.getName());
        }

        PatientData p7 = new PatientData("Yusuf Z.", 55, "B-", "Pending", "BPJS", "03:18 PM", "14 Jul 2025",
                "Male", "+62 816-4444-5555", "22 Feb 1970", "IDN3344556");
        p7.setDepartment("ER");
        p7.setPatientType("Inpatient");
        try {
            p7.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p7.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p7.getName());
        }

        PatientData p8 = new PatientData("Nour K.", 31, "AB+", "Handled", "Prudential", "05:40 PM", "14 Jul 2025",
                "Female", "+62 817-6666-8888", "09 Nov 1993", "IDN6677889");
        p8.setDepartment("Dermatology");
        p8.setPatientType("Outpatient");
        try {
            p8.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p8.getNationalId()));
        } catch (Exception e) {
            System.out.println("⚠️ Could not load notes for " + p8.getName());
        }

        PatientData p9 = new PatientData("Aliyah Zain", 60, "B+", "Handled", "Mandiri", "07:20 AM", "14 Jul 2025",
                "Female", "+62 812-1111-9911", "14 Oct 1964", "IDN9988771");
        p9.setDepartment("Neurology");
        p9.setPatientType("Outpatient");
        try {
            p9.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p9.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p10 = new PatientData("Rizky Firmansyah", 70, "O-", "Pending", "BPJS", "02:45 PM", "14 Jul 2025",
                "Male", "+62 812-2222-3344", "02 Jan 1955", "IDN1122334");
        p10.setDepartment("Cardiology");
        p10.setPatientType("Inpatient");
        try {
            p10.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p10.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p11 = new PatientData("Fatima A.", 34, "A+", "Pending", "Prudential", "10:12 AM", "13 Jul 2025",
                "Female", "+62 813-1122-3344", "17 Mar 1991", "IDN4455667");
        p11.setDepartment("Dermatology");
        p11.setPatientType("Outpatient");
        try {
            p11.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p11.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p12 = new PatientData("Hasan Basri", 79, "AB+", "Handled", "BPJS", "03:30 PM", "12 Jul 2025",
                "Male", "+62 814-6677-8899", "28 Aug 1945", "IDN7788990");
        p12.setDepartment("ER");
        p12.setPatientType("Inpatient");
        try {
            p12.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p12.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p13 = new PatientData("Maya Sari", 49, "A-", "Pending", "Mandiri", "01:11 PM", "09 Jul 2025",
                "Female", "+62 815-6677-2233", "09 Jul 1976", "IDN8822113");
        p13.setDepartment("Neurology");
        p13.setPatientType("Outpatient");
        try {
            p13.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p13.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p14 = new PatientData("Dimas Reza", 67, "O+", "Pending", "BPJS", "11:00 AM", "09 Jul 2025",
                "Male", "+62 816-4567-1234", "03 Feb 1958", "IDN5533224");
        p14.setDepartment("Cardiology");
        p14.setPatientType("Inpatient");
        try {
            p14.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p14.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p15 = new PatientData("Zahra H.", 55, "B-", "Handled", "Prudential", "02:10 PM", "08 Jul 2025",
                "Female", "+62 817-7654-3211", "22 May 1970", "IDN3344225");
        p15.setDepartment("Dermatology");
        p15.setPatientType("Outpatient");
        try {
            p15.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p15.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p16 = new PatientData("Yahya Omar", 85, "AB-", "Pending", "BPJS", "04:55 PM", "08 Jul 2025",
                "Male", "+62 818-8888-0000", "30 Nov 1939", "IDN6677880");
        p16.setDepartment("ER");
        p16.setPatientType("Inpatient");
        try {
            p16.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p16.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p17 = new PatientData("Amina Hadi", 62, "O-", "Handled", "Mandiri", "08:10 AM", "08 Jul 2025",
                "Female", "+62 819-1122-3344", "01 Jan 1963", "IDN1237890");
        p17.setDepartment("Neurology");
        p17.setPatientType("Outpatient");
        try {
            p17.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p17.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p18 = new PatientData("Fikri Rahman", 58, "B+", "Pending", "BPJS", "09:30 AM", "08 Jul 2025",
                "Male", "+62 820-9988-7766", "17 Oct 1966", "IDN2314567");
        p18.setDepartment("Cardiology");
        p18.setPatientType("Inpatient");
        try {
            p18.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p18.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p19 = new PatientData("Salsabila T.", 37, "A+", "Handled", "Prudential", "10:25 AM", "08 Jul 2025",
                "Female", "+62 821-3344-5566", "04 Apr 1987", "IDN7788123");
        p19.setDepartment("Dermatology");
        p19.setPatientType("Outpatient");
        try {
            p19.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p19.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p20 = new PatientData("Mahmoud R.", 76, "B-", "Pending", "BPJS", "03:00 PM", "07 Jul 2025",
                "Male", "+62 822-4444-5555", "08 Aug 1949", "IDN9944771");
        p20.setDepartment("ER");
        p20.setPatientType("Inpatient");
        try {
            p20.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p20.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p21 = new PatientData("Rania A.", 43, "AB+", "Handled", "Mandiri", "11:45 AM", "03 Jul 2025",
                "Female", "+62 823-5544-3322", "19 Sep 1982", "IDN8889332");
        p21.setDepartment("Neurology");
        p21.setPatientType("Outpatient");
        try {
            p21.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p21.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p22 = new PatientData("Budi Hartono", 63, "O-", "Pending", "BPJS", "10:10 AM", "02 Jul 2025",
                "Male", "+62 824-1111-0000", "02 Jun 1962", "IDN9988443");
        p22.setDepartment("Cardiology");
        p22.setPatientType("Inpatient");
        try {
            p22.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p22.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p23 = new PatientData("Nisa Putri", 28, "A-", "Pending", "Prudential", "12:34 PM", "20 Jul 2025",
                "Female", "+62 825-1234-5678", "27 Jan 1997", "IDN1122456");
        p23.setDepartment("Dermatology");
        p23.setPatientType("Outpatient");
        try {
            p23.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p23.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p24 = new PatientData("Farid Zaki", 82, "B+", "Handled", "BPJS", "01:55 PM", "20 Jul 2025",
                "Male", "+62 826-7788-9988", "29 Dec 1943", "IDN7744889");
        p24.setDepartment("ER");
        p24.setPatientType("Inpatient");
        try {
            p24.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p24.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p25 = new PatientData("Yumna A.", 66, "O+", "Pending", "Mandiri", "09:05 AM", "12 Jul 2025",
                "Female", "+62 827-1122-3344", "20 Mar 1959", "IDN3388997");
        p25.setDepartment("Neurology");
        p25.setPatientType("Outpatient");
        try {
            p25.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p25.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p26 = new PatientData("Habib L.", 61, "AB-", "Pending", "BPJS", "11:30 AM", "11 Jul 2025",
                "Male", "+62 828-4455-6677", "11 Apr 1963", "IDN7755442");
        p26.setDepartment("Cardiology");
        p26.setPatientType("Inpatient");
        try {
            p26.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p26.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p27 = new PatientData("Nada W.", 33, "A+", "Handled", "Prudential", "02:22 PM", "10 Jul 2025",
                "Female", "+62 829-9988-1122", "15 Jun 1992", "IDN9900887");
        p27.setDepartment("Dermatology");
        p27.setPatientType("Outpatient");
        try {
            p27.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p27.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p28 = new PatientData("Imran S.", 73, "B-", "Pending", "BPJS", "04:10 PM", "09 Jul 2025",
                "Male", "+62 830-6677-7788", "18 Jul 1952", "IDN7711773");
        p28.setDepartment("ER");
        p28.setPatientType("Inpatient");
        try {
            p28.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p28.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p29 = new PatientData("Sarah Lestari", 45, "AB+", "Handled", "Mandiri", "12:00 PM", "06 Jul 2025",
                "Female", "+62 831-3344-5566", "25 Nov 1980", "IDN8899001");
        p29.setDepartment("Neurology");
        p29.setPatientType("Outpatient");
        try {
            p29.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p29.getNationalId()));
        } catch (Exception e) {
        }

        PatientData p30 = new PatientData("Tariq N.", 60, "O-", "Pending", "BPJS", "03:40 PM", "02 Jul 2025",
                "Male", "+62 832-8888-4444", "05 Sep 1964", "IDN5522116");
        p30.setDepartment("Cardiology");
        p30.setPatientType("Inpatient");
        try {
            p30.getNotes().setAll(NoteStorageUtil.loadNotesForPatient(p30.getNationalId()));
        } catch (Exception e) {
        }

        // Add to both lists
        data.addAll(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
                p21, p22, p23, p24, p25, p26, p27, p28, p29, p30);
        patientList.addAll(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
                p21, p22, p23, p24, p25, p26, p27, p28, p29, p30);

        table.setItems(data);

        // Debug: Print patient data
        for (PatientData patient : data) {
            System.out.println("Patient: " + patient.toString());
        }
    }

    private void setupSearchAndFilters() {
        FilteredList<PatientData> filteredData = new FilteredList<>(data, p -> true);

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterPatients(filteredData));
        }

        if (statusFilter != null) {
            statusFilter.setItems(FXCollections.observableArrayList("All", "Pending", "Handled"));
            statusFilter.setValue("All");
            statusFilter.setOnAction(e -> filterPatients(filteredData));
        }

        if (dateFilter != null) {
            dateFilter.valueProperty().addListener((obs, oldDate, newDate) -> filterPatients(filteredData));
        }

        SortedList<PatientData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void filterPatients(FilteredList<PatientData> filteredData) {
        String keyword = searchField != null && searchField.getText() != null ? searchField.getText().toLowerCase()
                : "";
        String selectedStatus = statusFilter != null ? statusFilter.getValue() : "All";
        String selectedDate = dateFilter != null && dateFilter.getValue() != null
                ? dateFilter.getValue().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                : "";

        filteredData.setPredicate(patient -> {
            boolean matchesSearch = patient.getName().toLowerCase().contains(keyword)
                    || patient.getInsurance().toLowerCase().contains(keyword)
                    || patient.getStatus().toLowerCase().contains(keyword);
            boolean matchesStatus = selectedStatus.equals("All")
                    || patient.getStatus().equalsIgnoreCase(selectedStatus);
            boolean matchesDate = selectedDate.isEmpty() || patient.getDateScanned().equals(selectedDate);
            return matchesSearch && matchesStatus && matchesDate;
        });

        updateCaseCounts(filteredData);
    }

    private void displayNotes(PatientData selected) {
        if (notesSection == null)
            return;

        notesSection.getChildren().clear();
        int index = 1;
        for (Note note : selected.getNotes()) {
            HBox noteRow = new HBox(10);
            noteRow.getStyleClass().add("note-card");
            VBox contentBox = new VBox(2);
            Label title = new Label("Note " + index);
            Label body = new Label(note.getContent());
            Label meta = new Label(note.getDoctorName() + " – " + note.getTimestamp());
            contentBox.getChildren().addAll(title, body, meta);
            HBox.setHgrow(contentBox, Priority.ALWAYS);
            VBox iconBox = new VBox();
            iconBox.setSpacing(5);
            if (loggedInDoctor != null && note.getDoctorName().equals(loggedInDoctor.getName())) {
                try {
                    ImageView editIcon = new ImageView(
                            new Image(getClass().getResource("/icons/edit.png").toExternalForm()));
                    editIcon.setFitHeight(13);
                    editIcon.setFitWidth(13);
                    editIcon.setOnMouseClicked(e -> editNote(note));

                    ImageView deleteIcon = new ImageView(
                            new Image(getClass().getResource("/icons/delete.png").toExternalForm()));
                    deleteIcon.setFitHeight(13);
                    deleteIcon.setFitWidth(13);
                    deleteIcon.setOnMouseClicked(e -> {
                        selected.getNotes().remove(note);
                        try {
                            NoteStorageUtil.saveNotesForPatient(selected.getNationalId(), selected.getNotes());
                        } catch (Exception ex) {
                            System.out.println("⚠️ Could not save notes");
                        }
                        displayNotes(selected);
                    });

                    iconBox.getChildren().addAll(editIcon, deleteIcon);
                } catch (Exception e) {
                    System.out.println("⚠️ Could not load icons for notes");
                }
            }
            noteRow.getChildren().addAll(contentBox, iconBox);
            notesSection.getChildren().add(noteRow);
            index++;
        }
    }

    private void editNote(Note note) {
        TextInputDialog dialog = new TextInputDialog(note.getContent());
        dialog.setTitle("Edit Note");
        dialog.setHeaderText("Update the note content:");
        dialog.setContentText("Note:");
        dialog.showAndWait().ifPresent(updatedText -> {
            note.setContent(updatedText);
            try {
                NoteStorageUtil.saveNotesForPatient(currentSelectedPatient.getNationalId(),
                        currentSelectedPatient.getNotes());
            } catch (Exception e) {
                System.out.println("⚠️ Could not save notes");
            }
            displayNotes(currentSelectedPatient);
        });
    }

    @FXML
    private void handleAddNote() {
        if (currentSelectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a patient before adding a note.");
            alert.showAndWait();
            return;
        }

        if (loggedInDoctor == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Doctor Logged In");
            alert.setHeaderText(null);
            alert.setContentText("Please log in as a doctor to add notes.");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Note");
        dialog.setHeaderText("Write your note below:");
        dialog.setContentText("Note:");

        dialog.showAndWait().ifPresent(noteText -> {
            if (!noteText.trim().isEmpty()) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
                Note newNote = new Note(loggedInDoctor.getName(), noteText.trim(), timestamp);
                currentSelectedPatient.getNotes().add(newNote);
                try {
                    NoteStorageUtil.saveNotesForPatient(currentSelectedPatient.getNationalId(),
                            currentSelectedPatient.getNotes());
                } catch (Exception e) {
                    System.out.println("⚠️ Could not save notes");
                }
                displayNotes(currentSelectedPatient);
            }
        });
    }

    private void updateCaseCounts() {
        updateCaseCounts(table.getItems());
    }

    private void updateCaseCounts(ObservableList<PatientData> list) {
        long pending = list.stream().filter(p -> "Pending".equalsIgnoreCase(p.getStatus())).count();
        long handled = list.stream().filter(p -> "Handled".equalsIgnoreCase(p.getStatus())).count();
        long total = list.size();

        if (pendingCasesLabel != null)
            pendingCasesLabel.setText("Pending Cases: " + pending);
        if (handledCasesLabel != null)
            handledCasesLabel.setText("Handled Cases: " + handled);
        if (totalCasesLabel != null)
            totalCasesLabel.setText("Total Cases: " + total);
    }

    private void openDetailScene(PatientData patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MedicalRecordDetail.fxml"));
            Parent root = loader.load();
            MedicalRecordController controller = loader.getController();
            controller.setPatientData(patient);
            Stage stage = new Stage();
            stage.setTitle("Medical Record - " + patient.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not open detail scene: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenChart() {
        try {
            System.out.println(" Opening chart view with " + data.size() + " patients...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChartView.fxml"));
            AnchorPane chartPane = loader.load();
            DashboardChartsController controller = loader.getController();

            // Pass the current patient data to the chart controller
            List<PatientData> currentPatients = new ArrayList<>(data);
            controller.setPatients(currentPatients);

            System.out.println("Chart controller received " + currentPatients.size() + " patients");

            if (mainAnchor != null) {
                mainAnchor.getChildren().setAll(chartPane);
                AnchorPane.setTopAnchor(chartPane, 0.0);
                AnchorPane.setBottomAnchor(chartPane, 0.0);
                AnchorPane.setLeftAnchor(chartPane, 0.0);
                AnchorPane.setRightAnchor(chartPane, 0.0);
                System.out.println(" Chart view loaded successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Could not load chart view: " + e.getMessage());
        }
    }

    @FXML
    private void handleGoToPatients() {
        loadScene("/view/InnerDashboard.fxml");
    }

    @FXML
    private void handleGoToStatistics() {
        handleOpenChart(); // Use the same method as handleOpenChart
    }

    @FXML
    private void handleGoToFingerprint() {
        loadScene("/view/Fingerprint.fxml");
    }

    @FXML // still working on this to connect it with the login page !!!!!
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();

            stage.setScene(scene);
            stage.show();
            stage.setFullScreen(true); // Set the application to full screen mode

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadScene(String fxmlPath) {
        try {
            System.out.println("🔄 Loading scene: " + fxmlPath);
            Node content = FXMLLoader.load(getClass().getResource(fxmlPath));
            if (mainAnchor != null) {
                mainAnchor.getChildren().setAll(content);
                AnchorPane.setTopAnchor(content, 0.0);
                AnchorPane.setRightAnchor(content, 0.0);
                AnchorPane.setBottomAnchor(content, 0.0);
                AnchorPane.setLeftAnchor(content, 0.0);
                System.out.println("✅ Scene loaded successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load scene " + fxmlPath + ": " + e.getMessage());
        }
    }

    public void setLoggedInDoctor(DoctorProfile doctor) {
        this.loggedInDoctor = doctor;
        if (mainUserInfoLabel != null && doctor != null) {
            mainUserInfoLabel.setText("Logged in as: " + doctor.getName() + " – " + doctor.getHospital());
        }
        System.out.println(" Doctor logged in: " + (doctor != null ? doctor.getName() : "None"));
    }

    // Static patient list for sharing data across controllers
    public static ObservableList<PatientData> patientList = FXCollections.observableArrayList();

    public static ObservableList<PatientData> getPatientList() {
        return patientList;
    }

    // Method to get current data for external use
    public ObservableList<PatientData> getCurrentPatients() {
        return data;
    }

}
