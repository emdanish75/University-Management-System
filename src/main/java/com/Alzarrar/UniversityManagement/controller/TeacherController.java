package com.Alzarrar.UniversityManagement.controller;

import com.Alzarrar.UniversityManagement.Entity.*;
import com.Alzarrar.UniversityManagement.Service.*;
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
public class TeacherController implements Initializable {
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField experienceTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TableView<TeacherEntity> teachersDetail;
    @FXML
    private TableColumn<TeacherEntity, String> fullNameColumn;
    @FXML
    private TableColumn<TeacherEntity, String> usernameColumn;
    @FXML
    private TableColumn<TeacherEntity, String> emailColumn;
    @FXML
    private TableColumn<TeacherEntity, String> experienceColumn;
    @FXML
    private TableColumn<TeacherEntity, String> cityColumn;
    @FXML
    private TableColumn<TeacherEntity, String> salaryColumn;
    @FXML
    private TableColumn<TeacherEntity, String> courseColumn2;
    @FXML
    private TableColumn<TeacherEntity, String> departmentColumn;
    @FXML
    PasswordField passcodePasswordField;
    @FXML
    private ChoiceBox<String> departmentChoice ;
    @FXML
    private ChoiceBox<String> courseChoice ;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    private final TeacherService teacherService;
    private final CoursesService coursesService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final StageManager stageManager;


    public TeacherController(@Lazy StageManager stageManager,TeacherService teacherService,UserService userService,DepartmentService departmentService,CoursesService coursesService) {
        this.teacherService = teacherService;
        this.stageManager = stageManager;
        this.userService = userService;
        this.departmentService = departmentService;
        this.coursesService = coursesService;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<String> departmentNames = findAllDepartmentNames();
        departmentChoice.setValue("Select Department");
        departmentChoice.getItems().addAll(departmentNames);

        List<String> courseNames  = findAllCourseNames() ;
        courseChoice.setValue("Select Course");
        courseChoice.getItems().addAll(courseNames);

        // Add event filter to allow only integer input for salaryTextField
        salaryTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        // Add event filter to allow only integer input for experienceTextField
        experienceTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
        setupTeacherTable();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        // Add listener to enable/disable buttons based on selection
        teachersDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
         if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);

                // Populate text fields with the selected AdminEntity's data
                fullNameTextField.setText(newSelection.getFullName());
                usernameTextField.setText(newSelection.getUsername());
                emailTextField.setText(newSelection.getEmail());
                cityTextField.setText(newSelection.getCity());
                salaryTextField.setText(String.valueOf(newSelection.getSalary()));
                experienceTextField.setText(String.valueOf(newSelection.getExperienceYears()));
                departmentChoice.setValue(newSelection.getDepartmentName());
                courseChoice.setValue(newSelection.getCourse());
                passwordTextField.setText("12345678");
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                clearTextFields();
            }
        });
    }

    private List<String> findAllDepartmentNames() {
        List<DepartmentEntity> departments = departmentService.findAll();
        return departments.stream()
                .map(DepartmentEntity::getDepartmentName)
                .collect(Collectors.toList());
    }

        private List<String> findAllCourseNames() {
        List<CoursesEntity> courses = coursesService.findAll();
        return courses.stream()
                .map(CoursesEntity::getCourseName)
                .collect(Collectors.toList());
    }

    private List<TeacherEntity> findAll() {
        return teacherService.findAll();
    }
    private void setupTeacherTable() {
        // Fetch data from users table for users with role 'Teacher'
        List<UserEntity> teachersFromUsersTable = userService.findByRole("Teacher");

        // Map usernames and emails for easy lookup
        Map<String, String> usernameEmailMap = teachersFromUsersTable.stream()
                .collect(Collectors.toMap(UserEntity::getUsername, UserEntity::getEmail));

        // Fetch all teacher entities
        List<TeacherEntity> teacherData = findAll();

        Map<String, TeacherEntity> teacherMap = new HashMap<>();
        for (TeacherEntity teacher : teacherData) {
            teacherMap.put(teacher.getUsername(), teacher);
        }
        // Filter out non-existent teachers based on usernames from the user table
        List<TeacherEntity> uniqueTeachersData = teacherData.stream()
                .map(teacherEntity -> {
                    TeacherEntity foundTeacher = teacherMap.get(teacherEntity.getUsername());
                    return foundTeacher != null ? foundTeacher : null; // Skip non-teacher users
                })
                .filter(Objects::nonNull) // Remove null elements
                .collect(Collectors.toList());

        // Update email for existing teachers from the user table
        uniqueTeachersData.forEach(teacher -> teacher.setEmail(usernameEmailMap.get(teacher.getUsername())));

        // Sort the teacher data based on username
        Collections.sort(uniqueTeachersData, Comparator.comparing(TeacherEntity::getUsername));

        // Set cell value factories for the table columns
        fullNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        cityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCity()));
        salaryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSalary())));
        experienceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getExperienceYears())));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartmentName()));
        courseColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse()));

        // Set the teacher data to the TableView
        teachersDetail.setItems(FXCollections.observableList(uniqueTeachersData));
    }

    @FXML
    public void onDeleteButtonPressed() {
        try {
            // Get the selected TeacherEntity
            TeacherEntity selectedTeacher = teachersDetail.getSelectionModel().getSelectedItem();

            if (selectedTeacher != null) {
                // Delete the selected TeacherEntity from the database
                teacherService.deleteByName(selectedTeacher.getFullName());

                // Delete the corresponding UserEntity from the database
                userService.deleteByUsernameAndEmail(selectedTeacher.getUsername(), selectedTeacher.getEmail());

                // Remove the selected item from the TableView
                teachersDetail.getItems().remove(selectedTeacher);

                JavaFXUtils.showSuccessMessage("Teacher deleted successfully!");

                // Disable buttons and clear text fields
                deleteButton.setDisable(true);
                updateButton.setDisable(true);
                clearTextFields();
            } else {
                JavaFXUtils.showError("No Teacher selected for deletion!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while deleting teacher!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddButtonPressed() {
        String fullName = fullNameTextField.getText();
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String city = cityTextField.getText();
        String salary = salaryTextField.getText();
        String departmentName = departmentChoice.getValue();
        String courseName = courseChoice.getValue();
        String experience = experienceTextField.getText();
        String password = passwordTextField.getText();

        // Check if all required fields are empty
        if (isEmpty(fullName) && isEmpty(username) && isEmpty(email) && isEmpty(city) && isEmpty(salary) && isEmpty(experience)
                && isEmpty(departmentName) && isEmpty(courseName) && isEmpty(password)) {
            JavaFXUtils.showError("All fields are empty.");
            return;
        }

        // Check password length constraint
        if (!passwordTextField.getText().isEmpty() && passwordTextField.getText().length() < 8) {
            JavaFXUtils.showError("Password must contain 8 or more characters.");
            return;
        }

        // Check email format constraint
        if (!emailTextField.getText().isEmpty() && (!emailTextField.getText().contains("@") || !emailTextField.getText().contains("."))) {
            JavaFXUtils.showError("Invalid email format. Please enter a valid email address.");
            return;
        }

        // Check if any required field is empty
        if (isEmpty(fullName) || isEmpty(username) || isEmpty(email) || isEmpty(city) || isEmpty(salary) || isEmpty(experience)
                || isEmpty(departmentName) || isEmpty(courseName) || isEmpty(password)) {
            // Display an error message for empty fields
            StringBuilder errorMessage = new StringBuilder("Fields cannot be empty: ");
            if (isEmpty(fullName)) errorMessage.append("Full Name, ");
            if (isEmpty(username)) errorMessage.append("Username, ");
            if (isEmpty(email)) errorMessage.append("Email, ");
            if (isEmpty(city)) errorMessage.append("City, ");
            if (isEmpty(experience)) errorMessage.append("Experience, ");
            if (isEmpty(salary)) errorMessage.append("Salary, ");
            if (isEmpty(departmentName)) errorMessage.append("Department, ");
            if (isEmpty(courseName)) errorMessage.append("Course, ");
            if (isEmpty(password)) errorMessage.append("Password, ");

            JavaFXUtils.showError(errorMessage.toString().replaceAll(", $", ""));
            return;
        }

        // Proceed with creating and saving the TeacherEntity and UserEntity
        try {
            // Parse salary value to double
            double salaryValue = Double.parseDouble(salary);

            TeacherEntity teacherEntity = new TeacherEntity();
            teacherEntity.setFullName(fullName);
            teacherEntity.setUsername(username);
            teacherEntity.setEmail(email);
            teacherEntity.setSalary(salaryValue);
            teacherEntity.setCity(city);
            teacherEntity.setExperienceYears(Integer.parseInt(experience));
            teacherEntity.setDepartmentName(departmentName);
            teacherEntity.setCourse(courseName);
            teacherEntity.setPassword(password);

            // Create and save UserEntity with password
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setUsername(username);
            userEntity.setRole("Teacher");
            userEntity.setPassword(password);
            userService.save(userEntity);
            teacherService.save(teacherEntity);

            JavaFXUtils.showSuccessMessage("Teacher added successfully!");

            // Fetch all records from the "Teacher" and "User" tables
            List<TeacherEntity> teacherData = teacherService.findAll();

            teacherData.sort(Comparator.comparing(TeacherEntity::getFullName));

            // Update the TableView with the combined data
            teachersDetail.setItems(FXCollections.observableList(teacherData));

            // Clear text fields and display placeholders
            clearTextFields();
            stageManager.switchScene(FxmlView.ShowTeacherPage);
        } catch (NumberFormatException e) {
            JavaFXUtils.showError("Fields cannot be empty");
        } catch (Exception e) {
            JavaFXUtils.showError("Fields cannot be empty!");
            e.printStackTrace(); // Print the exception details for debugging
        }
    }
    private boolean isTeacher(TeacherEntity teacherEntity) {
        return teacherEntity != null && teacherEntity.getFullName() != null && !teacherEntity.getFullName().equals("-");
    }
    private boolean isEmpty(String fullName) {
        return false;
    }
    private void refreshAndSortTableView() {
        // Fetch the latest data from the database
        List<TeacherEntity> teacherData = findAll();
        // Fetch data from users table
        List<UserEntity> usersData = userService.findEmailAndUsername();
        // Remove duplicates based on username from both admins and users
        Map<String, TeacherEntity> teacherMap = teacherData.stream()
                .collect(Collectors.toMap(TeacherEntity::getUsername, Function.identity()));
        List<TeacherEntity> uniqueTeacherData = usersData.stream()
                .map(userEntity -> {
                    TeacherEntity teacherEntity = teacherMap.get(userEntity.getUsername());
                    if (teacherEntity != null) {
                        return teacherEntity;
                    } else {
                        TeacherEntity userAsTeacher = new TeacherEntity();
                        userAsTeacher.setEmail(userEntity.getEmail());
                        userAsTeacher.setUsername(userEntity.getUsername());
                        userAsTeacher.setSalary(0.0);
                        userAsTeacher.setFullName("-");
                        userAsTeacher.setCity("-");
                        userAsTeacher.setExperienceYears(0);
                        userAsTeacher.setDepartmentName("-");
                        userAsTeacher.setCourse("-");
                        userAsTeacher.setPassword(userEntity.getPassword());
                        System.out.println(userEntity.getPassword());
                        return userAsTeacher;
                    }
                })
                .collect(Collectors.toList());

        // Combine unique admins and users data
        uniqueTeacherData.addAll(teacherData);
        // Remove duplicates based on username
        uniqueTeacherData = uniqueTeacherData.stream().collect(
                Collectors.toMap(TeacherEntity::getUsername, Function.identity(), (existing, replacement) -> existing)
        ).values().stream().collect(Collectors.toList());
        // Sort the data based on full name in ascending order
        uniqueTeacherData.sort(Comparator.comparing(TeacherEntity::getUsername));
        // Update the TableView with the combined data
        teachersDetail.setItems(FXCollections.observableList(uniqueTeacherData));
    }
    @FXML
    public void onUpdateButtonPressed() {
        try {
            // Get the selected AdminEntity
            TeacherEntity selectedTeacher = teachersDetail.getSelectionModel().getSelectedItem();

            if (selectedTeacher != null) {
                // Check if any of the text fields is empty
                boolean isEmpty = false;
                StringBuilder errorMessage = new StringBuilder("These fields cannot be empty: ");

                if (isEmpty(fullNameTextField.getText())) {
                    errorMessage.append("Full Name, ");
                    isEmpty = true;
                }
                if (isEmpty(usernameTextField.getText())) {
                    errorMessage.append("Username, ");
                    isEmpty = true;
                }
                if (isEmpty(emailTextField.getText())) {
                    errorMessage.append("Email, ");
                    isEmpty = true;
                }
                if (isEmpty(experienceTextField.getText())) {
                    errorMessage.append("Experience, ");
                    isEmpty = true;
                }
                if (isEmpty(cityTextField.getText())) {
                    errorMessage.append("City, ");
                    isEmpty = true;
                }
                if (isEmpty(salaryTextField.getText())) {
                    errorMessage.append("Salary, ");
                    isEmpty = true;
                }
                if (isEmpty(departmentChoice.getValue())) {
                    errorMessage.append("Department, ");
                    isEmpty = true;
                }
                if (isEmpty(courseChoice.getValue())) {
                    errorMessage.append("Course, ");
                    isEmpty = true;
                }

                if(isEmpty){
                    // Display an error message for empty fields
                    JavaFXUtils.showError(errorMessage.toString().replaceAll(", $", ""));
                    return;  // Exit the method if any field is empty
                }

                // Update the selected AdminEntity with the new data
                selectedTeacher.setFullName(fullNameTextField.getText());
//                selectedTeacher.setUsername(usernameTextField.getText());
                selectedTeacher.setEmail(emailTextField.getText());
                selectedTeacher.setCity(cityTextField.getText());
                selectedTeacher.setExperienceYears(Integer.parseInt(experienceTextField.getText()));
                selectedTeacher.setSalary(Double.parseDouble(salaryTextField.getText()));
                selectedTeacher.setDepartmentName(departmentChoice.getValue());
                selectedTeacher.setCourse(courseChoice.getValue());

                // Handle the salary input separately to check for a valid number
                try {
                    double salary = Double.parseDouble(salaryTextField.getText());
                    selectedTeacher.setSalary(salary);

                    // Update the AdminEntity in the database
                    teacherService.save(selectedTeacher);

                    // Update the corresponding UserEntity in the users table
                    List<UserEntity> correspondingUsers = userService.findByUsername(selectedTeacher.getUsername());
                    for (UserEntity correspondingUser : correspondingUsers) {
                        correspondingUser.setEmail(emailTextField.getText());
//                        correspondingUser.setUsername(usernameTextField.getText());
//                        correspondingUser.setRole(roleTextField.getText());
                        userService.save(correspondingUser);
                    }

                    JavaFXUtils.showSuccessMessage("Teacher and corresponding User updated successfully!");

                    // Refresh the TableView with updated and sorted data
                    clearTextFields();
                    // Clear text fields and disable buttons
                    updateButton.setDisable(true);
                    deleteButton.setDisable(true);
                    refreshAndSortTableView();
                    setupTeacherTable();
                    stageManager.switchScene(FxmlView.ShowTeacherPage);
                } catch (NumberFormatException e) {
                    JavaFXUtils.showError("Invalid salary value or Experience value. Please enter a valid number.");
                }
            } else {
                JavaFXUtils.showError("No Teacher selected for update!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while updating teacher and corresponding user!");
            e.printStackTrace();
        }
    }
    private void clearTextFields() {
        fullNameTextField.clear();
        usernameTextField.clear();
        emailTextField.clear();
        cityTextField.clear();
        salaryTextField.clear();
        experienceTextField.clear();
        departmentChoice.setValue("");
        courseChoice.setValue("");
        passwordTextField.clear();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    @FXML
    public void onBackButtonPressed (){
        stageManager.switchScene(FxmlView.AdminMenuPage);
    }
    @FXML
    public void onClearButtonPressed(){
        fullNameTextField.clear();
        usernameTextField.clear();
        emailTextField.clear();
        cityTextField.clear();
        experienceTextField.clear();
        salaryTextField.clear();
        departmentChoice.setValue("Select Department");
        courseChoice.setValue("Select Course");
        passwordTextField.clear();

    }

}
