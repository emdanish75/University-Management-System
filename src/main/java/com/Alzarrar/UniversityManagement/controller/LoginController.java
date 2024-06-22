package com.Alzarrar.UniversityManagement.controller;

import com.Alzarrar.UniversityManagement.Entity.UserEntity;
import com.Alzarrar.UniversityManagement.Service.*;
import com.Alzarrar.UniversityManagement.config.StageManager;
import com.Alzarrar.UniversityManagement.enums.FxmlView;
import com.Alzarrar.UniversityManagement.repositry.UserRepo;
import com.Alzarrar.UniversityManagement.utils.JavaFXUtils;
import io.micrometer.common.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
@Controller
public class LoginController implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
    private final StageManager stageManager ;
    private final UserServiceImpl userService ;

    private UserService userServicee;
    private TeacherService teacherService;
    private StudentService studentService;
    private AdminService adminService;
    private final UserRepo userRepo;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private ChoiceBox<String> choiceBox;
    public LoginController(@Lazy StageManager stageManager, UserServiceImpl userService,final UserRepo userRepo) {
        this.stageManager = stageManager;
        this.userService = userService;
        this.userRepo = userRepo;
    }
    public StageManager getStageManager() {
        return stageManager;
    }
    private String[] roles = {"Student", "Teacher","Admin"};
    public static String role = "";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> roleOptions = new ArrayList<>(Arrays.asList("Select Role"));
        roleOptions.addAll(Arrays.asList(roles));

        choiceBox.setItems(FXCollections.observableArrayList(roleOptions));
        // Set default selections
        choiceBox.getSelectionModel().selectFirst();

        // Add listener to handle prompt text
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Select Role")) {
                choiceBox.getSelectionModel().clearSelection();
            }
        });
        this.loginButton.setDefaultButton(true);
        choiceBox.setOnAction(event -> {
            String selectedRole = choiceBox.getValue();
            if (selectedRole != null) {
                openSignupPage(selectedRole);
            }
        });
    }
    public void openSignupPage(String selectedRole){
            SignupController signupController = new SignupController(userService, stageManager,teacherService,adminService,studentService);
            signupController.setSelectedRole(selectedRole);
            stageManager.switchScene(FxmlView.SignUpPage);
        }

    public void OnLoginButtonPressed() {
        if (StringUtils.isBlank(usernameTextField.getText())) {
            JavaFXUtils.showError("Username is required");
            usernameTextField.requestFocus();
            return;
        }
        if (StringUtils.isBlank(passwordField.getText())) {
            JavaFXUtils.showError("Password is required");
            passwordField.requestFocus();
            return;
        }

        // Validate password length
        String password = passwordField.getText();
        if (password.length() < 8) {
            JavaFXUtils.showError("Password must contain 8 or more characters");
            passwordField.requestFocus();
            return;
        }

        List<UserEntity> users = userRepo.findAll();
        boolean userFound = false;

        for (UserEntity user : users) {
            if (user != null && user.getUsername().equals(usernameTextField.getText()) && user.getPassword().equals(passwordField.getText())) {
                this.usernameTextField.requestFocus();
                role = user.getRole();
                userFound = true;
                break;
            }
        }
        if (!userFound) {
            usernameTextField.setText("");
            passwordField.setText("");
            JavaFXUtils.showError("Wrong username or password");
        } else {
            JavaFXUtils.showSuccessMessage("You have successfully Login");

            if (role.equals("Student")) {
                stageManager.dialogScene(FxmlView.StudentFUTURE);
            } else if (role.equals("Teacher")) {
                stageManager.dialogScene(FxmlView.TeacherFUTURE);
            } else if (role.equals("Admin") || role.equals("Chief Administrator") || role.equals("Principal Administrator") || role.equals("Managerial Administrator") || role.equals("Senior Administrator")) {
                stageManager.switchScene(FxmlView.AdminMenuPage);
            }
        }
    }
}


