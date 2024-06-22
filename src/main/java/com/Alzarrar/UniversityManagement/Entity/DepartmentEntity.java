package com.Alzarrar.UniversityManagement.Entity;

import lombok.Data;
import javafx.beans.property.*;

import jakarta.persistence.*;

import java.util.Objects;

@Data
@Entity
@Table(schema = "university", name = "departments")
public class DepartmentEntity {

    @Id
    @Column(name = "Department_Name", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "Department_Code", nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "Established_Year")
    private int establishedYear;

    @Column(name = "Contact_Number")
    private String contactNumber;

    // JavaFX property for departmentName
    public StringProperty departmentNameProperty() {
        return new SimpleStringProperty(this.departmentName);
    }

    // Getters, setters, equals, and hashCode methods
}
