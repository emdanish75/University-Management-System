package com.Alzarrar.UniversityManagement.Entity;

import lombok.Data;
import javafx.beans.property.*;

import jakarta.persistence.*;

import java.util.Objects;

@Data
@Entity
@Table(schema = "university", name = "courses")
public class CoursesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Course_ID")
    private int courseID;

    @Column(name = "Course_Code", nullable = false)
    private String courseCode;

    @Column(name = "Course_Name", nullable = false)
    private String courseName;

    @Column(name = "Credit_Hours", nullable = false)
    private int creditHours;

    @ManyToOne
    @JoinColumn(name = "Department_Name", nullable = false)
    private DepartmentEntity department;

    // JavaFX properties
    public StringProperty courseCodeProperty() {
        return new SimpleStringProperty(this.courseCode);
    }

    public StringProperty courseNameProperty() {
        return new SimpleStringProperty(this.courseName);
    }

    public IntegerProperty creditHoursProperty() {
        return new SimpleIntegerProperty(this.creditHours);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CoursesEntity that = (CoursesEntity) obj;
        return Objects.equals(courseCode, that.courseCode) && Objects.equals(courseName, that.courseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, courseName);
    }
}
