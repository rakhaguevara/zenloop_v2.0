package util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.Patient;

import java.io.*;

public class PatientXmlHandler {

    private static final XStream xstream = new XStream(new DomDriver());

    static {
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] {
                "model.**",
                "util.**"
        });
        xstream.alias("patient", Patient.class);
        xstream.alias("patients", ArrayList.class); // alias root
        xstream.alias("PatientsFix", Object[].class);
    }

    public static void saveToXml(ArrayList<Patient> patientList, String doctorUsername) {
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

    public static ArrayList<Patient> loadFromXml(String doctorUsername) {
        String filePath = "data/pasien_doctor_" + doctorUsername + "/doctor_" + doctorUsername + "_dataPasien.xml";
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            ArrayList<Patient> loadedList = (ArrayList<Patient>) xstream.fromXML(fis);
            loadedList.rebuildArrayAfterLoad(); // penting agar array internal kembali valid
            return loadedList;
        } catch (Exception e) {
            System.err.println("❌ Gagal memuat data pasien dari XML: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void savePastPatients(ArrayList<Patient> pastPatientList, String doctorUsername) {
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

    public static ArrayList<Patient> loadPastPatients(String doctorUsername) {
        String filePath = "data/past_patient_doctor_" + doctorUsername + "/doctor_" + doctorUsername
                + "_pastPatients.xml";
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            ArrayList<Patient> loadedList = (ArrayList<Patient>) xstream.fromXML(fis);
            loadedList.rebuildArrayAfterLoad(); // ⬅️ Kembalikan array internal
            return loadedList;
        } catch (Exception e) {
            System.err.println("❌ Gagal memuat past patients dari XML: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void clearPastPatientsXml(String username) {
        try {
            String filePath = "data/past_patient_doctor_" + username + "/doctor_" + username + "_pastPatients.xml";
            File file = new File(filePath);
            if (file.exists()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    String emptyXml = "<patients></patients>";
                    fos.write(emptyXml.getBytes());
                }
                System.out.println("✅ Past patients XML dikosongkan: " + file.getPath());
            } else {
                System.out.println("⚠️ File tidak ditemukan: " + file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
