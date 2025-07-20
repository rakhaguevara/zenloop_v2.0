package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.PatientXmlHandler;

import java.time.LocalDate;

public class PatientService {

    private static ObservableList<Patient> activePatients = FXCollections.observableArrayList();
    private static ObservableList<Patient> pastPatients = FXCollections.observableArrayList();
    private static int nextId = 1;

    public static void addNewPatient(String name, LocalDate dob, String status, String issue, String createdBy) {
        validatePatientData(name, dob, status, issue);
        Patient patient = new Patient(name, dob, status, issue, createdBy);
        patient.setPatientId(nextId++);
        activePatients.add(patient);
    }

    public static boolean updatePatient(int id, String name, LocalDate dob, String status, String issue) {
        Patient p = findActivePatientById(id);
        if (p != null) {
            p.setName(name);
            p.setDateOfBirth(dob);
            p.setStatus(status);
            p.setPatientIssue(issue);
            // Optional: force refresh in table
            int idx = activePatients.indexOf(p);
            activePatients.set(idx, p);
            return true;
        }
        return false;
    }

    public static boolean deletePatient(int id) {
        Patient p = findActivePatientById(id);
        if (p != null)
            return activePatients.remove(p);
        p = findPastPatientById(id);
        return p != null && pastPatients.remove(p);
    }

    public static boolean moveToPast(int id) {
        Patient p = findActivePatientById(id);
        if (p != null) {
            activePatients.remove(p);
            pastPatients.add(p);
            return true;
        }
        return false;
    }

    public static Patient findActivePatientById(int id) {
        return activePatients.stream().filter(p -> p.getPatientId() == id).findFirst().orElse(null);
    }

    public static Patient findPastPatientById(int id) {
        return pastPatients.stream().filter(p -> p.getPatientId() == id).findFirst().orElse(null);
    }

    public static ObservableList<Patient> getActivePatients() {
        return activePatients;
    }

    public static ObservableList<Patient> getPastPatients() {
        return pastPatients;
    }

    public static int getTotalCount() {
        return activePatients.size() + pastPatients.size();
    }

    public static ObservableList<Patient> getActivePatientsByUser(String username) {
        return activePatients.filtered(p -> p.getCreatedBy().equals(username));
    }

    public static ObservableList<Patient> getPastPatientsByUser(String username) {
        return pastPatients.filtered(p -> p.getCreatedBy().equals(username));
    }

    public static void loadPatientsFromXml() {
        String username = SessionManager.getCurrentUser().getUsername(); // ambil user login
        ObservableList<Patient> loaded = PatientXmlHandler.loadFromXml(username);
        activePatients.setAll(loaded);
        nextId = loaded.stream().mapToInt(Patient::getPatientId).max().orElse(0) + 1;
    }

    public static void loadPatientsFromXml(String doctorUsername) {
        ObservableList<Patient> loaded = PatientXmlHandler.loadFromXml(doctorUsername);
        activePatients.setAll(loaded);
        nextId = loaded.stream().mapToInt(Patient::getPatientId).max().orElse(0) + 1;
    }

    public static void loadPastPatientsFromXml(String doctorUsername) {
        ObservableList<Patient> loaded = PatientXmlHandler.loadPastPatients(doctorUsername);
        pastPatients.setAll(loaded);
    }

    public static void clearAll() {
        activePatients.clear();
        pastPatients.clear();
        nextId = 1;
    }

    public static void validatePatientData(String name, LocalDate dob, String status, String issue) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is required");
        if (dob == null || dob.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date of birth is invalid");
        if (status == null || status.trim().isEmpty())
            throw new IllegalArgumentException("Status is required");
        if (issue == null || issue.trim().isEmpty())
            throw new IllegalArgumentException("Issue is required");
    }
}
