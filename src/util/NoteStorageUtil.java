package util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import model.Note;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class NoteStorageUtil {

    private static final String BASE_FOLDER = "notes/";

    static {
        // Make sure the folder exists
        File folder = new File(BASE_FOLDER);
        if (!folder.exists()) folder.mkdirs();
    }

    // Save notes for a specific patient
    public static void saveNotesForPatient(String patientId, List<Note> notes) {
        XStream xstream = new XStream(new StaxDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{Note.class});

        try (FileOutputStream fos = new FileOutputStream(BASE_FOLDER + patientId + ".xml")) {
            xstream.toXML(notes, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load notes for a specific patient
    public static List<Note> loadNotesForPatient(String patientId) {
        File file = new File(BASE_FOLDER + patientId + ".xml");
        if (!file.exists()) return new ArrayList<>();

        XStream xstream = new XStream(new StaxDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{Note.class});

        try (FileInputStream fis = new FileInputStream(file)) {
            return (List<Note>) xstream.fromXML(fis);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

