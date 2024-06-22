package com.Alzarrar.UniversityManagement.controller;

import com.Alzarrar.UniversityManagement.Entity.DepartmentEntity;
import com.Alzarrar.UniversityManagement.Entity.StudentEntity;
import com.Alzarrar.UniversityManagement.Entity.UserEntity;
import com.Alzarrar.UniversityManagement.Service.DepartmentService;
import com.Alzarrar.UniversityManagement.Service.StudentService;
import com.Alzarrar.UniversityManagement.Service.UserService;
import com.Alzarrar.UniversityManagement.config.StageManager;
import com.Alzarrar.UniversityManagement.enums.FxmlView;
import com.Alzarrar.UniversityManagement.utils.JavaFXUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ShowStudentController implements Initializable {
    @FXML
    private TableView<StudentEntity> studentDetails;
    @FXML
    private TableColumn<StudentEntity, String> fullNameColumn;
    @FXML
    private TableColumn<StudentEntity, String> usernameColumn;
    @FXML
    private TableColumn<StudentEntity, String> emailColumn;
    @FXML
    private TableColumn<StudentEntity, String> ageColumn;
    @FXML
    private TableColumn<StudentEntity, String> genderColumn;
    @FXML
    private TableColumn<StudentEntity, String> phoneNoColumn;
    @FXML
    private TableColumn<StudentEntity, String> semesterColumn;
    @FXML
    private TableColumn<StudentEntity, String> departmentColumn;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private ChoiceBox<String> departmentChoice;
    @FXML
    private ChoiceBox<String> semesterChoice;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private Button AddButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;
    @FXML
    private Button clearButton;
    private final StageManager stageManager;
    private final StudentService studentService;
    private final UserService userService;
    private final DepartmentService departmentService;
    String []Semesters = {"1","2","3","4","5","6","7","8"};
    String selectedGender="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> departmentNames = findAllDepartmentNames();
        departmentChoice.setValue("Select Department");
        departmentChoice.getItems().addAll(departmentNames);
        semesterChoice.setValue("Select Semester");
        semesterChoice.getItems().addAll(Semesters);

        ToggleGroup genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        // Add event filters to restrict input in age and phone number text fields

        ageTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        phoneNumberTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        genderToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                 selectedGender = selectedRadioButton.getText();
            }
        });

        setupStudentsTable();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        // Add listener to enable/disable buttons based on selection
        studentDetails.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                // Populate text fields with the selected AdminEntity's data
                fullNameTextField.setText(newSelection.getFullName());
                usernameTextField.setText("");
                emailTextField.setText(newSelection.getEmail());
                semesterChoice.setValue(String.valueOf(newSelection.getSemester()));
                departmentChoice.setValue(newSelection.getDepartment());
                ageTextField.setText(String.valueOf(newSelection.getAge()));
                phoneNumberTextField.setText(newSelection.getPhoneNo());

                if (newSelection.getGender().equals("Female")) {
                    maleRadioButton.setSelected(true);
                } else if (newSelection.getGender().equals("Male")) {
                    femaleRadioButton.setSelected(true);
                }

                passwordTextField.setText("12345678");

            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);

                // Clear text fields if no row is selected
                fullNameTextField.clear();
                usernameTextField.clear();
                emailTextField.clear();
                departmentChoice.setValue("");
                semesterChoice.setValue("");
                ageTextField.clear();
                phoneNumberTextField.clear();
                passwordTextField.clear();
            }
        });
    }
    private List<String> findAllDepartmentNames() {
        List<DepartmentEntity> departments = departmentService.findAll();
        return departments.stream()
                .map(DepartmentEntity::getDepartmentName)
                .collect(Collectors.toList());
    }



    public ShowStudentController(@Lazy StageManager stageManager, StudentService studentService, UserService userService,DepartmentService departmentService) {
        this.studentService = studentService;
        this.stageManager = stageManager;
        this.userService = userService;
        this.departmentService = departmentService;
    }
    private List<StudentEntity> findAll() {
        return studentService.findAll();
    }

    private void setupStudentsTable (){
        List<StudentEntity> studentsData = findAll();
// Fetch data from users table
        List<UserEntity> usersData = userService.findEmailAndUsername();

// Remove duplicates based on username from both students and users
        Map<String, StudentEntity> studentMap = studentsData.stream()
                .collect(Collectors.toMap(StudentEntity::getUsername, Function.identity()));

        List<StudentEntity> uniqueStudentsData = usersData.stream()
                .map(userEntity -> {
                    StudentEntity studentEntity = studentMap.get(userEntity.getUsername());
                    if (studentEntity != null) {
                        return studentEntity;
                    } else {
                        return null; // Skip non-student users
                    }
                })
                .filter(Objects::nonNull) // Remove null elements
                .collect(Collectors.toList());

        // Combine unique admins and users data
        uniqueStudentsData.addAll(studentsData);

        // Remove duplicates based on username
        uniqueStudentsData = uniqueStudentsData.stream().collect(
                Collectors.toMap(StudentEntity::getUsername, Function.identity(), (existing, replacement) -> existing)
        ).values().stream().collect(Collectors.toList());

        // Sort the data based on fullname in ascending order
        uniqueStudentsData.sort(Comparator.comparing(StudentEntity::getFullName));
        fullNameColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getFullName())
        );
        usernameColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getUsername())
        );
        emailColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getEmail())
        );
        ageColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAge()))
        );
        phoneNoColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPhoneNo()))
        );
        genderColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getGender())
        );
        semesterColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSemester()))
        );
        departmentColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getDepartment())
        );
        studentDetails.setItems(FXCollections.observableList(uniqueStudentsData));
    }
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    @FXML
    public void onAddButtonPressed() {
        try {
            String fullName = fullNameTextField.getText();
            String username = usernameTextField.getText();
            String email = emailTextField.getText();
            String phoneNo = phoneNumberTextField.getText();
            String department = departmentChoice.getValue();
            String semester = semesterChoice.getValue();
            String age = ageTextField.getText();
            String gender = selectedGender;
            String password = passwordTextField.getText();

            if (studentService.existsByUsername(username)) {
                JavaFXUtils.showError("Username already exists");
                usernameTextField.clear();
                return;
            }

            // Validate password length
            if (password.length() < 8) {
                JavaFXUtils.showError("Password must contain 8 or more characters.");
                return;
            }

            // Validate email format
            if (!email.contains("@") || !email.contains(".")) {
                JavaFXUtils.showError("Invalid email format.");
                return;
            }

            if (isEmpty(fullName) || isEmpty(username) || isEmpty(email) || isEmpty(age) || isEmpty(phoneNo) || isEmpty(semester) || isEmpty(password) || isEmpty(department) || isEmpty(gender)) {
                // Display an error message for empty fields
                StringBuilder errorMessage = new StringBuilder("Fields cannot be empty: ");
                if (isEmpty(fullName)) errorMessage.append("Full Name, ");
                if (isEmpty(username)) errorMessage.append("Username, ");
                if (isEmpty(email)) errorMessage.append("Email, ");
                if (isEmpty(age)) errorMessage.append("Age, ");
                if (isEmpty(phoneNo)) errorMessage.append("Phone number, ");
                if (isEmpty(semester)) errorMessage.append("Semester, ");
                if (isEmpty(department)) errorMessage.append("Department, ");
                if (isEmpty(gender)) errorMessage.append("Gender, ");
                if (isEmpty(password)) errorMessage.append("Password, ");

                JavaFXUtils.showError(errorMessage.toString().replaceAll(", $", ""));
            } else {
                // Proceed with creating and saving the AdminEntity
                StudentEntity student = new StudentEntity();
                student.setFullName(fullName);
                student.setUsername(username);
                student.setEmail(email);
                student.setAge(Integer.parseInt(age));
                student.setPhoneNo(phoneNo);
                student.setPassword(password);
                student.setGender(gender);
                student.setSemester(Integer.parseInt(semester));
                student.setDepartment(department);

                studentService.save(student);

                // Store username, email, and role in the user table using userService
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(username);
                userEntity.setEmail(email);
                userEntity.setPassword(password);
                userEntity.setRole("Student");

                userService.save(userEntity);

                JavaFXUtils.showSuccessMessage("Student added successfully!");

                // Fetch all records from the "admins" table
                List<StudentEntity> studentsData = findAll();

                // Sort the data based on full name in ascending order
                studentsData.sort(Comparator.comparing(StudentEntity::getFullName));

                // Update the TableView with the combined data
                studentDetails.setItems(FXCollections.observableList(studentsData));

                // Clear text fields and display placeholders
                clearTextFields();
                stageManager.switchScene(FxmlView.ShowStudentPage);
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while adding Student!");
            e.printStackTrace();
        }
    }
    @FXML
    public void onUpdateButtonPressed(){
        try {
            // Get the selected StudentEntity
            StudentEntity selectedStudent = studentDetails.getSelectionModel().getSelectedItem();
//            usernameTextField.setDisable(true);
            if (selectedStudent != null) {
                // Update the fields of the selected AdminEntity with the new data
                if (isEmpty(fullNameTextField.getText())) {
                    JavaFXUtils.showError("Full Name cannot be empty.");
                    return;
                }
                selectedStudent.setFullName(fullNameTextField.getText());
                if (isEmpty(phoneNumberTextField.getText())) {
                    JavaFXUtils.showError("Phone number cannot be empty.");
                    return;
                }
                selectedStudent.setPhoneNo(phoneNumberTextField.getText());
                if (isEmpty(emailTextField.getText())) {
                    JavaFXUtils.showError("Email cannot be empty.");
                    return;
                }
                selectedStudent.setEmail(emailTextField.getText());
                if (isEmpty(semesterChoice.getValue())) {
                    JavaFXUtils.showError("Semester can't be empty.");
                    return;
                }
                selectedStudent.setSemester(Integer.parseInt(semesterChoice.getValue()));
                if (isEmpty(departmentChoice.getValue())) {
                    JavaFXUtils.showError("Department can't be empty.");
                    return;
                }
                selectedStudent.setDepartment(departmentChoice.getValue());

                selectedStudent.setGender(selectedGender);

                String password = passwordTextField.getText();
                // Validate password length
                if (password.length() < 8) {
                    JavaFXUtils.showError("Password must contain 8 or more characters.");
                    return;
                }

                String email = emailTextField.getText();
                // Validate email format
                if (!email.contains("@") || !email.contains(".")) {
                    JavaFXUtils.showError("Invalid email format.");
                    return;
                }


                // Update the AdminEntity in the database
                studentService.save(selectedStudent);

                // Update the corresponding UserEntity in the users table
                List<UserEntity> correspondingUsers = userService.findByUsername(selectedStudent.getUsername());
                for (UserEntity correspondingUser : correspondingUsers) {
                    correspondingUser.setEmail(emailTextField.getText());
                    // No need to update username as it's disabled
                    correspondingUser.setRole("Student");
                    userService.save(correspondingUser);
                }
                JavaFXUtils.showSuccessMessage("Student and corresponding User updated successfully!");
                clearTextFields();
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                setupStudentsTable();
                stageManager.switchScene(FxmlView.ShowStudentPage);

            } else {
                JavaFXUtils.showError("No Student selected for update!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while updating student and corresponding user!");
            e.printStackTrace();
        }
    }
    private void clearTextFields() {
        fullNameTextField.clear();
        usernameTextField.clear();
        emailTextField.clear();
        departmentChoice.setValue("");
        semesterChoice.setValue("");
        ageTextField.clear();
        phoneNumberTextField.clear();
        passwordTextField.clear();
    }
    @FXML
    public void onDeleteButtonPressed(){
        try {
            // Get the selected StudentEntity
            StudentEntity selectedStudent = studentDetails.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                // Delete the selected AdminEntity from the database
                studentService.deleteByName(selectedStudent.getFullName());
                userService.deleteByUsernameAndEmail(selectedStudent.getUsername(),selectedStudent.getEmail());
                // Remove the selected item from the TableView
                studentDetails.getItems().remove(selectedStudent);
                JavaFXUtils.showSuccessMessage("Student deleted successfully!");
                deleteButton.setDisable(true);
                updateButton.setDisable(true);
                clearTextFields();
            } else {
                JavaFXUtils.showError("No Student selected for deletion!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while deleting Student!");
            e.printStackTrace();
        }
    }
    @FXML
    public void onClearButtonPressed(){
        clearTextFields();
    }
    @FXML
    public void onBackButtonPressed(){
        stageManager.switchScene(FxmlView.AdminMenuPage);
    }

}
