package com.Alzarrar.UniversityManagement.controller;

import com.Alzarrar.UniversityManagement.Entity.CoursesEntity;
import com.Alzarrar.UniversityManagement.Entity.DepartmentEntity;
import com.Alzarrar.UniversityManagement.Service.CoursesService;
import com.Alzarrar.UniversityManagement.Service.DepartmentService;
import com.Alzarrar.UniversityManagement.config.StageManager;
import com.Alzarrar.UniversityManagement.enums.FxmlView;
import com.Alzarrar.UniversityManagement.utils.JavaFXUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class ShowCoursesController implements Initializable {

    private final DepartmentService departmentService;
    @FXML
    private TableView<CoursesEntity> coursesDetail;

    @FXML
    private TableColumn<CoursesEntity, String> courseCodeColumn;

    @FXML
    private TableColumn<CoursesEntity, String> courseNameColumn;

    @FXML
    private TableColumn<CoursesEntity, Integer> creditHoursColumn;

    @FXML
    private TableColumn<CoursesEntity, String> departmentName;

    @FXML
    private TextField courseCodeTextField;

    @FXML
    private TextField courseNameTextField;

    @FXML
    private TextField creditHoursTextField;

    @FXML
    private ChoiceBox<String> departmentChoiceBox;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private Button clearButton;

    private final CoursesService coursesService;
    private final StageManager stageManager;

    public ShowCoursesController(@Lazy @Autowired DepartmentService departmentService, @Lazy CoursesService coursesService, @Lazy StageManager stageManager) {
        this.coursesService = coursesService;
        this.stageManager = stageManager;
        this.departmentService = departmentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCoursesTable();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        // Populate the choice box with department names
        List<String> departmentNames = findAllDepartmentNames();
        departmentChoiceBox.setItems(FXCollections.observableArrayList(departmentNames));
        departmentChoiceBox.setValue("Select Department");


        coursesDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);

                courseCodeTextField.setText(newSelection.getCourseCode());
                courseNameTextField.setText(newSelection.getCourseName());
                creditHoursTextField.setText(String.valueOf(newSelection.getCreditHours()));
                departmentChoiceBox.setValue(newSelection.getDepartment().getDepartmentName());
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);

                courseCodeTextField.clear();
                courseNameTextField.clear();
                creditHoursTextField.clear();
                departmentChoiceBox.getSelectionModel().clearSelection();
            }
        });
        creditHoursTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
    }

    private List<String> findAllDepartmentNames() {
        List<DepartmentEntity> departments = departmentService.findAll();
        return departments.stream()
                .map(DepartmentEntity::getDepartmentName)
                .collect(Collectors.toList());
    }


    private List<CoursesEntity> findAllCourses() {
        return coursesService.findAll();
    }

    private void setupCoursesTable() {
        List<CoursesEntity> coursesData = findAllCourses();

        courseCodeColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getCourseCode())
        );

        courseNameColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getCourseName())
        );

        creditHoursColumn.setCellValueFactory(
                (cellData) -> new SimpleIntegerProperty(cellData.getValue().getCreditHours()).asObject()
        );

        departmentName.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getDepartment().getDepartmentName())
        );

        coursesDetail.setItems(FXCollections.observableList(coursesData));
    }

    @FXML
    public void onAddButtonPressed() {
        try {
            String courseCode = courseCodeTextField.getText();
            String courseName = courseNameTextField.getText();
            int creditHours = Integer.parseInt(creditHoursTextField.getText());
            String departmentName = departmentChoiceBox.getValue();

            if (isEmpty(courseCode) || isEmpty(courseName) || isEmpty(departmentName)) {
                JavaFXUtils.showError("Fields cannot be empty: Course Code, Course Name, Department Name");
            } else if (coursesService.existsByCourseCode(courseCode)) {
                JavaFXUtils.showError("Course Code already exists");
                courseCodeTextField.clear();
            } else {
                CoursesEntity coursesEntity = new CoursesEntity();
                coursesEntity.setCourseCode(courseCode);
                coursesEntity.setCourseName(courseName);
                coursesEntity.setCreditHours(creditHours);

                // Retrieve departmentEntity by departmentName from the database
                DepartmentEntity departmentEntity = departmentService.findByDepartmentName(departmentName).orElse(null);
                if (departmentEntity == null) {
                    JavaFXUtils.showError("Department not found: " + departmentName);
                    return;
                }
                coursesEntity.setDepartment(departmentEntity);

                coursesService.save(coursesEntity);

                JavaFXUtils.showSuccessMessage("Course added successfully!");

                refreshAndSortTableView();

                clearFields();
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while adding course!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onUpdateButtonPressed() {
        try {
            CoursesEntity selectedCourse = coursesDetail.getSelectionModel().getSelectedItem();

            if (selectedCourse != null) {
                String courseCode = courseCodeTextField.getText();
                String courseName = courseNameTextField.getText();
                int creditHours = Integer.parseInt(creditHoursTextField.getText());
                String departmentName = departmentChoiceBox.getValue();

                if (isEmpty(courseCode) || isEmpty(courseName) || isEmpty(departmentName)) {
                    JavaFXUtils.showError("Fields cannot be empty: Course Code, Course Name, Department Name");
                    return;
                }

                selectedCourse.setCourseCode(courseCode);
                selectedCourse.setCourseName(courseName);
                selectedCourse.setCreditHours(creditHours);

                // Retrieve departmentEntity by departmentName from the database
                DepartmentEntity departmentEntity = departmentService.findByDepartmentName(departmentName).orElse(null);
                if (departmentEntity == null) {
                    JavaFXUtils.showError("Department not found: " + departmentName);
                    return;
                }
                selectedCourse.setDepartment(departmentEntity);

                coursesService.save(selectedCourse);

                JavaFXUtils.showSuccessMessage("Course updated successfully!");

                refreshAndSortTableView();

                clearFields();
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            } else {
                JavaFXUtils.showError("No course selected for update!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while updating course!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteButtonPressed() {
        try {
            CoursesEntity selectedCourse = coursesDetail.getSelectionModel().getSelectedItem();

            if (selectedCourse != null) {
                coursesService.delete(selectedCourse.getCourseID());

                coursesDetail.getItems().remove(selectedCourse);

                JavaFXUtils.showSuccessMessage("Course deleted successfully!");
                deleteButton.setDisable(true);
                updateButton.setDisable(true);
                clearFields();
            } else {
                JavaFXUtils.showError("No course selected for deletion!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while deleting course!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onClearButtonPressed() {
        clearFields();
    }

    @FXML
    public void onBackButtonPressed() {
        try {
            stageManager.switchScene(FxmlView.AdminMenuPage);
        } catch (Exception e) {
            JavaFXUtils.showError("Error loading AdminMenu.fxml");
            e.printStackTrace();
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void clearFields() {
        courseCodeTextField.clear();
        courseNameTextField.clear();
        creditHoursTextField.clear();
        departmentChoiceBox.setValue("Select Department");
    }

    private void refreshAndSortTableView() {
        List<CoursesEntity> coursesData = findAllCourses();
        coursesDetail.setItems(FXCollections.observableList(coursesData));
    }
}
