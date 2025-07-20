package model;

import java.time.LocalDate;
import java.util.Objects;

public class Patient {
    private int patientId;
    private String name;
    private LocalDate dateOfBirth;
    private String status;
    private String patientIssue;
    private LocalDate dateConsultation;
    private String createdBy; // ✅ Dokter yang membuat data pasien

    public Patient() {
        this.dateConsultation = LocalDate.now();
    }

    public Patient(String name, LocalDate dateOfBirth, String status, String patientIssue, String createdBy) {
        this();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.patientIssue = patientIssue;
        this.createdBy = createdBy;
    }

    // ===== Getter & Setter =====
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientIssue() {
        return patientIssue;
    }

    public void setPatientIssue(String patientIssue) {
        this.patientIssue = patientIssue;
    }

    public LocalDate getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDate dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // ===== toString, equals, hashCode =====

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", status='" + status + '\'' +
                ", patientIssue='" + patientIssue + '\'' +
                ", dateConsultation=" + dateConsultation +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Patient))
            return false;
        Patient patient = (Patient) o;
        return patientId == patient.patientId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId);
    }
}
