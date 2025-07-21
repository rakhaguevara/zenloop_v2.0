package model;

import util.ArrayList;
import util.PatientXmlHandler;

import java.time.LocalDate;

public class PatientService {

    private static ArrayList<Patient> activePatients = new ArrayList<>();
    private static ArrayList<Patient> pastPatients = new ArrayList<>();
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
            int idx = activePatients.indexOf(p);
            activePatients.set(idx, p);
            return true;
        }
        return false;
    }

    public static boolean deletePatient(int id) {
        Patient p = findActivePatientById(id);
        if (p != null) {
            activePatients.remove(p);
            return true;
        }
        p = findPastPatientById(id);
        if (p != null) {
            pastPatients.remove(p);
            return true;
        }
        return false;
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
        for (Patient p : activePatients) {
            if (p.getPatientId() == id)
                return p;
        }
        return null;
    }

    public static Patient findPastPatientById(int id) {
        for (Patient p : pastPatients) {
            if (p.getPatientId() == id)
                return p;
        }
        return null;
    }

    public static ArrayList<Patient> getActivePatients() {
        return activePatients;
    }

    public static ArrayList<Patient> getPastPatients() {
        return pastPatients;
    }

    public static int getTotalCount() {
        return activePatients.size() + pastPatients.size();
    }

    public static ArrayList<Patient> getActivePatientsByUser(String username) {
        ArrayList<Patient> result = new ArrayList<>();
        for (Patient p : activePatients) {
            if (p.getCreatedBy().equals(username)) {
                result.add(p);
            }
        }
        return result;
    }

    public static ArrayList<Patient> getPastPatientsByUser(String username) {
        ArrayList<Patient> result = new ArrayList<>();
        for (Patient p : pastPatients) {
            if (p.getCreatedBy().equals(username)) {
                result.add(p);
            }
        }
        return result;
    }

    public static void loadPatientsFromXml() {
        String username = SessionManager.getCurrentUser().getUsername();
        ArrayList<Patient> loaded = PatientXmlHandler.loadFromXml(username);
        activePatients.clear();
        for (Patient p : loaded) {
            activePatients.add(p);
        }
        nextId = getMaxId(activePatients) + 1;
    }

    public static void loadPatientsFromXml(String doctorUsername) {
        ArrayList<Patient> loaded = PatientXmlHandler.loadFromXml(doctorUsername);
        activePatients.clear();
        for (Patient p : loaded) {
            activePatients.add(p);
        }
        nextId = getMaxId(activePatients) + 1;
    }

    public static void loadPastPatientsFromXml(String doctorUsername) {
        ArrayList<Patient> loaded = PatientXmlHandler.loadPastPatients(doctorUsername);
        pastPatients.clear();
        for (Patient p : loaded) {
            pastPatients.add(p);
        }
    }

    private static int getMaxId(ArrayList<Patient> list) {
        int max = 0;
        for (Patient p : list) {
            if (p.getPatientId() > max) {
                max = p.getPatientId();
            }
        }
        return max;
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
