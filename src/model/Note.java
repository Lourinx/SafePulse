package model;

public class Note {
    private String doctorName;
    private String content;
    private String timestamp;

    public Note() {
    }

    public Note(String doctorName, String content, String timestamp) {
        this.doctorName = doctorName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
