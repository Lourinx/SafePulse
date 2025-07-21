package model;

public class DoctorProfile {
    private String name;
    private String password;
    private String licenseNumber;
    private String hospital;

    // Constructor
    public DoctorProfile(String name, String password, String licenseNumber, String hospital) {
        this.name = name;
        this.password = password;
        this.licenseNumber = licenseNumber;
        this.hospital = hospital;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getHospital() {
        return hospital;
    }
}
