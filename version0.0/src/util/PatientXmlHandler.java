package util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Patient;

import java.io.*;
import java.util.List;

public class PatientXmlHandler {

    private static final XStream xstream = new XStream(new DomDriver());

    static {
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] {
                "model.**"
        });
        xstream.alias("patient", Patient.class);
        xstream.alias("patients", List.class);
    }

    /**
     * Simpan daftar pasien ke file XML di dalam folder
     * data/pasien_doctor_<username>
     */
    public static void saveToXml(List<Patient> patientList, String doctorUsername) {
        try {
            String folderName = "data/pasien_doctor_" + doctorUsername;
            String fileName = "doctor_" + doctorUsername + "_dataPasien.xml";

            File folder = new File(folderName);
            folder.mkdirs();

            File file = new File(folder, fileName);
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
                xstream.marshal(patientList, new PrettyPrintWriter(writer));
            }

            System.out.println("✅ Data pasien disimpan ke: " + file.getPath());
        } catch (Exception e) {
            System.err.println("❌ Gagal menyimpan data pasien: " + e.getMessage());
        }
    }

    /**
     * Load data pasien aktif dari folder data/pasien_doctor_<username>
     */
    public static ObservableList<Patient> loadFromXml(String doctorUsername) {
        String filePath = "data/pasien_doctor_" + doctorUsername + "/doctor_" + doctorUsername + "_dataPasien.xml";
        File file = new File(filePath);
        if (!file.exists()) {
            return FXCollections.observableArrayList();
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            List<Patient> list = (List<Patient>) xstream.fromXML(fis);
            return FXCollections.observableArrayList(list);
        } catch (Exception e) {
            System.err.println("❌ Gagal memuat data pasien dari XML: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    /**
     * Simpan daftar pasien arsip ke folder data/past_patient_doctor_<username>
     */
    public static void savePastPatients(List<Patient> pastPatientList, String doctorUsername) {
        try {
            String folderName = "data/past_patient_doctor_" + doctorUsername;
            String fileName = "doctor_" + doctorUsername + "_pastPatients.xml";

            File folder = new File(folderName);
            folder.mkdirs();

            File file = new File(folder, fileName);
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
                xstream.marshal(pastPatientList, new PrettyPrintWriter(writer));
            }

            System.out.println("✅ Past patients disimpan ke: " + file.getPath());
        } catch (Exception e) {
            System.err.println("❌ Gagal menyimpan past patients: " + e.getMessage());
        }
    }

    /**
     * Load daftar pasien arsip dari folder data/past_patient_doctor_<username>
     */
    public static ObservableList<Patient> loadPastPatients(String doctorUsername) {
        String filePath = "data/past_patient_doctor_" + doctorUsername + "/doctor_" + doctorUsername
                + "_pastPatients.xml";
        File file = new File(filePath);
        if (!file.exists()) {
            return FXCollections.observableArrayList();
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            List<Patient> list = (List<Patient>) xstream.fromXML(fis);
            return FXCollections.observableArrayList(list);
        } catch (Exception e) {
            System.err.println("❌ Gagal memuat past patients dari XML: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    public static void clearPastPatientsXml(String username) {
        try {
            String folderPath = "data/past_patient_doctor_" + username;
            String filePath = folderPath + "/doctor_" + username + "_pastPatients.xml";
            File file = new File(filePath);
            if (file.exists()) {
                FileOutputStream fos = new FileOutputStream(file);
                String emptyXml = "<patients></patients>";
                fos.write(emptyXml.getBytes());
                fos.close();
                System.out.println("✅ Past patients XML dikosongkan: " + file.getPath());
            } else {
                System.out.println("⚠️ File tidak ditemukan: " + file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
