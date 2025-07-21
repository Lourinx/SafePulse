package model;

public class Medication {
    private String medName, dosage, frequency, route, startDate, prescribedBy;

    public Medication(String medName, String dosage, String frequency, String route, String startDate,
            String prescribedBy) {
        this.medName = medName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.route = route;
        this.startDate = startDate;
        this.prescribedBy = prescribedBy;
    }

    public String getMedName() {
        return medName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getRoute() {
        return route;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getPrescribedBy() {
        return prescribedBy;
    }
}
