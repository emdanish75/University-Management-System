package com.Alzarrar.UniversityManagement.controller;

import com.Alzarrar.UniversityManagement.Entity.AdminEntity;
import com.Alzarrar.UniversityManagement.Entity.StudentEntity;
import com.Alzarrar.UniversityManagement.Entity.TeacherEntity;
import com.Alzarrar.UniversityManagement.Entity.UserEntity;
import com.Alzarrar.UniversityManagement.Service.*;
import com.Alzarrar.UniversityManagement.config.StageManager;
import com.Alzarrar.UniversityManagement.enums.FxmlView;
import com.Alzarrar.UniversityManagement.utils.JavaFXUtils;
import io.micrometer.common.util.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
@Controller
public class SignupController implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);
    private final UserServiceImpl userService;

    private final TeacherService teacherService;

    private final AdminService adminService;
    private final StudentService studentService;
    private final StageManager stageManager;
    @FXML
    private Label roleLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button SignUpButton;

    static String selectRole = "";

    SignupController(UserServiceImpl userService, @Lazy StageManager stageManager, TeacherService teacherService, AdminService adminService, StudentService studentService) {
        this.userService = userService;
        this.teacherService = teacherService;
        this.stageManager = stageManager;
        this.adminService = adminService;
        this.studentService = studentService;
    }


    public StageManager getStageManager() {
        return stageManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleLabel.setText(selectRole);
    }

    public void setSelectedRole(String selectedRole) {
        this.selectRole = selectedRole;
    }

    public void OnLoginButtonPressed() {
        stageManager.switchScene(FxmlView.LoginPageUni);
    }

    public void OnSignUpButtonPressed() {
        // Validate username
        if (StringUtils.isBlank(this.usernameTextField.getText())) {
            JavaFXUtils.showError("Username is required");
            this.usernameTextField.requestFocus();
            return;
        }
        // Validate email
        if (StringUtils.isBlank(this.emailTextField.getText())) {
            JavaFXUtils.showError("Email is required");
            this.emailTextField.requestFocus();
            return;
        }
        // Validate password
        String password = this.passwordField.getText();
        if (StringUtils.isBlank(password)) {
            JavaFXUtils.showError("Password is required");
            this.passwordField.requestFocus();
            return;
        }
        // Validate password length
        if (password.length() < 8) {
            JavaFXUtils.showError("Password must contain 8 or more characters");
            this.passwordField.requestFocus();
            return;
        }
        // Validate confirm password
        if (StringUtils.isBlank(this.confirmPasswordField.getText())) {
            JavaFXUtils.showError("Confirm password is required");
            this.confirmPasswordField.requestFocus();
            return;
        }
        // Check if password and confirm password fields match
        if (!password.equals(this.confirmPasswordField.getText())) {
            JavaFXUtils.showError("Passwords do not match");
            this.confirmPasswordField.requestFocus();
            return;
        }
        // Validate email format
        String email = this.emailTextField.getText();
        if (!isValidEmail(email)) {
            JavaFXUtils.showError("Invalid email format");
            this.emailTextField.requestFocus();
            return;
        }
        // Check if username already exists
        List<UserEntity> existingUser = userService.findByUsername(usernameTextField.getText());
        if (!existingUser.isEmpty()) {
            JavaFXUtils.showError("Username already exists. Please choose a different one.");
            this.usernameTextField.requestFocus();
            return;
        }

        UserEntity userEntity = new UserEntity(email, usernameTextField.getText());
        userEntity.setPassword(password);
        userEntity.setRole(selectRole);
        userService.save(userEntity);

        // Handle different roles
        switch (selectRole) {
            case "Teacher":
                // Create and save TeacherEntity
                TeacherEntity teacherEntity = new TeacherEntity();
                teacherEntity.setFullName("-");
                teacherEntity.setUsername(usernameTextField.getText());
                teacherEntity.setEmail(email);
                teacherEntity.setSalary(0.0);
                teacherEntity.setExperienceYears(0);
                teacherEntity.setCity("-");
                teacherEntity.setDepartmentName("-");
                teacherEntity.setPassword(password);
                teacherEntity.setCourse("-");
                teacherService.save(teacherEntity);
                break;
            case "Admin":
                // Create and save AdminEntity
                AdminEntity adminEntity = new AdminEntity();
                adminEntity.setName("-");
                adminEntity.setUsername(usernameTextField.getText());
                adminEntity.setEmail(email);
                adminEntity.setSalary(0.0);
                adminEntity.setRole(selectRole);
                adminEntity.setPassword(password);
                adminEntity.setCity("-");
                adminService.save(adminEntity);
                break;
            case "Student":
                // Create and save StudentEntity
                StudentEntity studentEntity = new StudentEntity();
                studentEntity.setFullName("-");
                studentEntity.setUsername(usernameTextField.getText());
                studentEntity.setEmail(email);
                studentEntity.setAge(18);
                studentEntity.setSemester(1);
                studentEntity.setDepartment("Select Department");
                studentEntity.setPhoneNo("-");
                studentEntity.setPassword(password);
                studentEntity.setGender("-");
                studentService.save(studentEntity);
                break;
            default:
                break;
        }

        JavaFXUtils.showSuccessMessage("You have successfully signed up.");
    }

    // Method to validate email format
    private boolean isValidEmail(String email) {
        // Basic email format validation
        if (StringUtils.isBlank(email)) {
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            return false;
        }
        return true;
    }
}
