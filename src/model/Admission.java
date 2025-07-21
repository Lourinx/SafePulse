package model;

public class Admission {
    private String admissionDate, department, diagnosis, dischargeDate, doctor;

    public Admission(String admissionDate, String department, String diagnosis, String dischargeDate, String doctor) {
        this.admissionDate = admissionDate;
        this.department = department;
        this.diagnosis = diagnosis;
        this.dischargeDate = dischargeDate;
        this.doctor = doctor;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public String getDepartment() {
        return department;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public String getDoctor() {
        return doctor;
    }
}
