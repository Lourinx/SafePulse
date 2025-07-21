package model;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DoctorXMLHandler {

    private static final String FILE_PATH = "Doctors.xml"; // Simplified to work in project root

    // Load doctors from XML
    public static List<DoctorProfile> loadDoctors() {
        List<DoctorProfile> doctors = new ArrayList<>();
        try {
            File xmlFile = new File(FILE_PATH);
            System.out.println("[DoctorXMLHandler] Looking for file at: " + xmlFile.getAbsolutePath());

            if (!xmlFile.exists()) {
                return doctors; // File not found, return empty list
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("doctor");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String name = getTagValue("name", eElement);
                    String password = getTagValue("password", eElement);
                    String license = getTagValue("licenseNumber", eElement);
                    String hospital = getTagValue("hospital", eElement);
                    doctors.add(new DoctorProfile(name, password, license, hospital));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Add a new doctor to XML
    public static void addDoctor(DoctorProfile doctor) {
        try {
            File xmlFile = new File(FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            if (!xmlFile.exists()) {
                doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("doctors");
                doc.appendChild(rootElement);
            } else {
                doc = dBuilder.parse(xmlFile);
                if (doc.getDocumentElement() == null) {
                    Element rootElement = doc.createElement("doctors");
                    doc.appendChild(rootElement);
                }
            }

            Node root = doc.getDocumentElement();
            Element newDoctor = doc.createElement("doctor");

            appendTextElement(doc, newDoctor, "name", doctor.getName());
            appendTextElement(doc, newDoctor, "password", doctor.getPassword());
            appendTextElement(doc, newDoctor, "licenseNumber", doctor.getLicenseNumber());
            appendTextElement(doc, newDoctor, "hospital", doctor.getHospital());

            root.appendChild(newDoctor);

            // Write to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("[DoctorXMLHandler] Doctor added: " + doctor.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Utility Methods =====

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return "";
    }

    private static void appendTextElement(Document doc, Element parent, String tag, String text) {
        Element elem = doc.createElement(tag);
        elem.appendChild(doc.createTextNode(text));
        parent.appendChild(elem);
    }
}
