// === PatientData.java ===
package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PatientData {
    private final StringProperty name;
    private final IntegerProperty age;
    private final StringProperty bloodType;
    private final StringProperty status;
    private final StringProperty insurance;
    private final StringProperty timeScanned;
    private final StringProperty dateScanned;
    private final StringProperty gender;
    private final StringProperty contact;
    private final StringProperty dob;
    private final StringProperty nationalId;
    private final ObservableList<Note> notes = FXCollections.observableArrayList();

    private String department;
    private String patientType;

    public PatientData(String name, int age, String bloodType, String status, String insurance,
            String timeScanned, String dateScanned,
            String gender, String contact, String dob, String nationalId) {
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.age = new SimpleIntegerProperty(age);
        this.bloodType = new SimpleStringProperty(bloodType != null ? bloodType : "");
        this.status = new SimpleStringProperty(status != null ? status : "");
        this.insurance = new SimpleStringProperty(insurance != null ? insurance : "");
        this.timeScanned = new SimpleStringProperty(timeScanned != null ? timeScanned : "");
        this.dateScanned = new SimpleStringProperty(dateScanned != null ? dateScanned : "");
        this.gender = new SimpleStringProperty(gender != null ? gender : "");
        this.contact = new SimpleStringProperty(contact != null ? contact : "");
        this.dob = new SimpleStringProperty(dob != null ? dob : "");
        this.nationalId = new SimpleStringProperty(nationalId != null ? nationalId : "");

        // Set default values for department and patient type if not set
        this.department = "";
        this.patientType = "";
    }

    // Getters and Setters
    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value != null ? value : "");
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int value) {
        age.set(value);
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public String getBloodType() {
        return bloodType.get();
    }

    public void setBloodType(String value) {
        bloodType.set(value != null ? value : "");
    }

    public StringProperty bloodTypeProperty() {
        return bloodType;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value != null ? value : "");
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getInsurance() {
        return insurance.get();
    }

    public void setInsurance(String value) {
        insurance.set(value != null ? value : "");
    }

    public StringProperty insuranceProperty() {
        return insurance;
    }

    public String getTimeScanned() {
        return timeScanned.get();
    }

    public void setTimeScanned(String value) {
        timeScanned.set(value != null ? value : "");
    }

    public StringProperty timeScannedProperty() {
        return timeScanned;
    }

    public String getDateScanned() {
        return dateScanned.get();
    }

    public void setDateScanned(String value) {
        dateScanned.set(value != null ? value : "");
    }

    public StringProperty dateScannedProperty() {
        return dateScanned;
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String value) {
        gender.set(value != null ? value : "");
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String value) {
        contact.set(value != null ? value : "");
    }

    public StringProperty contactProperty() {
        return contact;
    }

    public String getDob() {
        return dob.get();
    }

    public void setDob(String value) {
        dob.set(value != null ? value : "");
    }

    public StringProperty dobProperty() {
        return dob;
    }

    public String getNationalId() {
        return nationalId.get();
    }

    public void setNationalId(String value) {
        nationalId.set(value != null ? value : "");
    }

    public StringProperty nationalIdProperty() {
        return nationalId;
    }

    public ObservableList<Note> getNotes() {
        return notes;
    }

    public String getDepartment() {
        return department != null ? department : "";
    }

    public void setDepartment(String department) {
        this.department = department != null ? department : "";
    }

    public String getPatientType() {
        return patientType != null ? patientType : "";
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType != null ? patientType : "";
    }

    @Override
    public String toString() {
        return String.format("PatientData{name='%s', age=%d, department='%s', type='%s'}",
                getName(), getAge(), getDepartment(), getPatientType());
    }
}
