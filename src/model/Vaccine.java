package model;

public class Vaccine {
    private String vaccineName, dateTaken, status;

    public Vaccine(String vaccineName, String dateTaken, String status) {
        this.vaccineName = vaccineName;
        this.dateTaken = dateTaken;
        this.status = status;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public String getStatus() {
        return status;
    }
}
