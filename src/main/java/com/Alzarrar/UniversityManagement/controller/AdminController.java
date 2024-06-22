package com.Alzarrar.UniversityManagement.controller;

import com.Alzarrar.UniversityManagement.Entity.AdminEntity;
import com.Alzarrar.UniversityManagement.Service.AdminService;
import com.Alzarrar.UniversityManagement.Service.UserService;
import com.Alzarrar.UniversityManagement.Entity.UserEntity;
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
public class AdminController implements Initializable {
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    PasswordField passcodePasswordField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TableView<AdminEntity> adminsDetail;
    @FXML
    private TableColumn<AdminEntity, String> fullNameColumn;
    @FXML
    private TableColumn<AdminEntity, String> usernameColumn;
    @FXML
    private TableColumn<AdminEntity, String> emailColumn;
    @FXML
    private TableColumn<AdminEntity, String> roleColumn;
    @FXML
    private TableColumn<AdminEntity, String> cityColumn;
    @FXML
    private TableColumn<AdminEntity, String> salaryColumn;
    @FXML
    private ChoiceBox<String> rolesChoice;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    private final AdminService adminService;
    private final UserService userService;
    private final StageManager stageManager;

    String []roles = {"Chief Administrator","Principal Administrator","Managerial Administrator","Senior Administrator"};
    public AdminController(@Lazy StageManager stageManager, AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.stageManager = stageManager;
        this.userService = userService;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rolesChoice.setValue("Administration Role");
        rolesChoice.getItems().addAll(roles);

        salaryTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
        setupAdminTable();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        // Add listener to enable/disable buttons based on selection
        adminsDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                // Populate text fields with the selected AdminEntity's data
                fullNameTextField.setText(newSelection.getName());
                usernameTextField.setText(newSelection.getUsername());
                emailTextField.setText(newSelection.getEmail());
                rolesChoice.setValue(newSelection.getRole());
                cityTextField.setText(newSelection.getCity());
                salaryTextField.setText(String.valueOf(newSelection.getSalary()));
                passcodePasswordField.setText("12345678");
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                // Clear text fields if no row is selected
                fullNameTextField.clear();
                usernameTextField.clear();
                emailTextField.clear();
                rolesChoice.setValue("");
                cityTextField.clear();
                salaryTextField.clear();
            }
        });
    }
    private List<AdminEntity> findAll() {
        return adminService.findAll();
    }
    private void setupAdminTable() {
        List<AdminEntity> adminsData = findAll();

// Filter out entities with role "Admin"
        adminsData = adminsData.stream()
                .filter(admin -> {
                    String role = admin.getRole();
                    return role != null && (role.equals("Admin") ||
                            role.equals("Chief Administrator") ||
                            role.equals("Principal Administrator") ||
                            role.equals("Managerial Administrator") ||
                            role.equals("Senior Administrator"));
                })
                .collect(Collectors.toList());

// Fetch data from users table
        List<UserEntity> usersData = userService.findEmailAndUsername();

// Remove duplicates based on username from both admins and users
        Map<String, AdminEntity> adminMap = adminsData.stream()
                .collect(Collectors.toMap(AdminEntity::getUsername, Function.identity()));

        List<AdminEntity> uniqueAdminsData = usersData.stream()
                .map(userEntity -> {
                    AdminEntity adminEntity = adminMap.get(userEntity.getUsername());
                    if (adminEntity != null) {
                        return adminEntity;
                    } else {
                        return null; // Skip non-admin users
                    }
                })
                .filter(Objects::nonNull) // Remove null elements
                .collect(Collectors.toList());

        // Combine unique admins and users data
        uniqueAdminsData.addAll(adminsData);

        // Remove duplicates based on username
        uniqueAdminsData = uniqueAdminsData.stream().collect(
                Collectors.toMap(AdminEntity::getUsername, Function.identity(), (existing, replacement) -> existing)
        ).values().stream().collect(Collectors.toList());

        // Sort the data based on fullname in ascending order
        uniqueAdminsData.sort(Comparator.comparing(AdminEntity::getName));

        fullNameColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getName())
        );
        usernameColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getUsername())
        );
        emailColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getEmail())
        );
        roleColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getRole())
        );
        salaryColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSalary()))
        );
        cityColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getCity())
        );

        adminsDetail.setItems(FXCollections.observableList(uniqueAdminsData));
    }
    @FXML
    public void onAddButtonPressed() {
        try {
            String fullName = fullNameTextField.getText();
            String username = usernameTextField.getText();
            String email = emailTextField.getText();
            String role = rolesChoice.getValue();
            String city = cityTextField.getText();
            String salaryText = salaryTextField.getText();
            String password = passcodePasswordField.getText();

            if (adminService.existsByUsername(username)) {
                JavaFXUtils.showError("Username already exists");
                usernameTextField.clear();
                return;
            }

            if (password.length() < 8) {
                JavaFXUtils.showError("Password must contain 8 or more characters");
                return;
            }

            if (!emailTextField.getText().contains("@") || !emailTextField.getText().contains(".")) {
                JavaFXUtils.showError("Invalid email format");
                return;
            }

            if (isEmpty(fullName) || isEmpty(username) || isEmpty(email) || isEmpty(role) || isEmpty(city) || isEmpty(salaryText) || isEmpty(password)) {
                // Display an error message for empty fields
                StringBuilder errorMessage = new StringBuilder("Fields cannot be empty: ");
                if (isEmpty(fullName)) errorMessage.append("Full Name, ");
                if (isEmpty(username)) errorMessage.append("Username, ");
                if (isEmpty(email)) errorMessage.append("Email, ");
                if (isEmpty(role)) errorMessage.append("Role, ");
                if (isEmpty(city)) errorMessage.append("City, ");
                if (isEmpty(salaryText)) errorMessage.append("Salary, ");
                if (isEmpty(password)) errorMessage.append("Password, ");

                JavaFXUtils.showError(errorMessage.toString().replaceAll(", $", ""));
            } else {
                // Proceed with creating and saving the AdminEntity
                AdminEntity adminEntity = new AdminEntity();
                adminEntity.setName(fullName);
                adminEntity.setUsername(username);
                adminEntity.setEmail(email);
                adminEntity.setRole(rolesChoice.getValue());
                adminEntity.setCity(city);
                adminEntity.setPassword(password);
                adminEntity.setSalary(Double.parseDouble(salaryText));

                adminService.save(adminEntity);

                // Store username, email, and role in the user table using userService
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(username);
                userEntity.setEmail(email);
                userEntity.setPassword(password);
                userEntity.setRole(rolesChoice.getValue());

                userService.save(userEntity);

                JavaFXUtils.showSuccessMessage("Admin added successfully!");

                // Fetch all records from the "admins" table
                List<AdminEntity> adminsData = findAll();

                // Sort the data based on full name in ascending order
                adminsData.sort(Comparator.comparing(AdminEntity::getName));

                // Update the TableView with the combined data
                adminsDetail.setItems(FXCollections.observableList(adminsData));

                // Clear text fields and display placeholders
                fullNameTextField.clear();
                usernameTextField.clear();
                emailTextField.clear();
                rolesChoice.setValue("");
                cityTextField.clear();
                salaryTextField.clear();
                stageManager.switchScene(FxmlView.ShowAdminPage);
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while adding admin!");
            e.printStackTrace();
        }
    }
    private boolean isAdmin(AdminEntity adminEntity) {
        return adminEntity != null && adminEntity.getName() != null && !adminEntity.getName().equals("-");
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    @FXML
    public void onDeleteButtonPressed(){
        try {
            // Get the selected AdminEntity
            AdminEntity selectedAdmin = adminsDetail.getSelectionModel().getSelectedItem();

            if (selectedAdmin != null) {
                // Delete the selected AdminEntity from the database
                adminService.deleteByName(selectedAdmin.getName());
                userService.deleteByUsernameAndEmail(selectedAdmin.getUsername(),selectedAdmin.getEmail());


                // Remove the selected item from the TableView
                adminsDetail.getItems().remove(selectedAdmin);

                JavaFXUtils.showSuccessMessage("Admin deleted successfully!");
                deleteButton.setDisable(true);
                updateButton.setDisable(true);
                clearTextFields();
            } else {
                JavaFXUtils.showError("No admin selected for deletion!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while deleting admin!");
            e.printStackTrace();
        }
    }
    private void refreshAndSortTableView() {
        // Fetch the latest data from the database
        List<AdminEntity> adminsData = findAll();

        // Fetch data from users table
        List<UserEntity> usersData = userService.findEmailAndUsername();

        // Remove duplicates based on username from both admins and users
        Map<String, AdminEntity> adminMap = adminsData.stream()
                .collect(Collectors.toMap(AdminEntity::getUsername, Function.identity()));

        List<AdminEntity> uniqueAdminsData = usersData.stream()
                .map(userEntity -> {
                    AdminEntity adminEntity = adminMap.get(userEntity.getUsername());
                    if (adminEntity != null) {
                        return adminEntity;
                    } else {
                        AdminEntity userAsAdmin = new AdminEntity();
                        userAsAdmin.setEmail(userEntity.getEmail());
                        userAsAdmin.setUsername(userEntity.getUsername());
                        userAsAdmin.setSalary(0.0);
                        userAsAdmin.setName("-");
                        userAsAdmin.setCity("-");
                        userAsAdmin.setPassword(userEntity.getPassword());
                        System.out.println(userEntity.getPassword());
                        userAsAdmin.setRole(userEntity.getRole());
                        return userAsAdmin;
                    }
                })
                .collect(Collectors.toList());

        // Combine unique admins and users data
        uniqueAdminsData.addAll(adminsData);

        // Remove duplicates based on username
        uniqueAdminsData = uniqueAdminsData.stream().collect(
                Collectors.toMap(AdminEntity::getUsername, Function.identity(), (existing, replacement) -> existing)
        ).values().stream().collect(Collectors.toList());

        // Sort the data based on full name in ascending order
        uniqueAdminsData.sort(Comparator.comparing(AdminEntity::getName));

        // Update the TableView with the combined data
        adminsDetail.setItems(FXCollections.observableList(uniqueAdminsData));
    }
    @FXML
    public void onUpdateButtonPressed() {
        try {
            // Get the selected AdminEntity
            AdminEntity selectedAdmin = adminsDetail.getSelectionModel().getSelectedItem();

            if (selectedAdmin != null) {
                // Update the fields of the selected AdminEntity with the new data
                if (isEmpty(fullNameTextField.getText())) {
                    JavaFXUtils.showError("Full Name cannot be empty.");
                    return;
                }
                selectedAdmin.setName(fullNameTextField.getText());
                usernameTextField.setDisable(true);

                if (isEmpty(emailTextField.getText())) {
                    JavaFXUtils.showError("Email cannot be empty.");
                    return;
                }
                selectedAdmin.setEmail(emailTextField.getText());

                if (isEmpty((String) rolesChoice.getValue())) {
                    JavaFXUtils.showError("Role cannot be empty.");
                    return;
                }
                selectedAdmin.setRole(rolesChoice.getValue());

                if (isEmpty(cityTextField.getText())) {
                    JavaFXUtils.showError("City cannot be empty.");
                    return;
                }
                selectedAdmin.setCity(cityTextField.getText());

                // Handle the salary input separately to check for a valid number
                try {
                    double salary = Double.parseDouble(salaryTextField.getText());
                    selectedAdmin.setSalary(salary);
                } catch (NumberFormatException e) {
                    JavaFXUtils.showError("Invalid salary value. Please enter a valid number.");
                    return;
                }
                String password = passcodePasswordField.getText();
                if (password.length() < 8) {
                    JavaFXUtils.showError("Password must contain 8 or more characters");
                    return;
                }

                if (!emailTextField.getText().contains("@") || !emailTextField.getText().contains(".")) {
                    JavaFXUtils.showError("Invalid email format");
                    return;
                }
                // Update the AdminEntity in the database
                adminService.save(selectedAdmin);

                // Update the corresponding UserEntity in the users table
                List<UserEntity> correspondingUsers = userService.findByUsername(selectedAdmin.getUsername());
                for (UserEntity correspondingUser : correspondingUsers) {
                    correspondingUser.setEmail(emailTextField.getText());
                    // No need to update username as it's disabled
                    correspondingUser.setRole(rolesChoice.getValue());
                    userService.save(correspondingUser);
                }

                JavaFXUtils.showSuccessMessage("Admin and corresponding User updated successfully!");

                // Refresh the TableView with updated and sorted data
                // Clear text fields and disable buttons
                clearTextFields();
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                refreshAndSortTableView();
                setupAdminTable();
                stageManager.switchScene(FxmlView.ShowAdminPage);

            } else {
                JavaFXUtils.showError("No admin selected for update!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while updating admin and corresponding user!");
            e.printStackTrace();
        }
    }
@FXML
public void onClearButtonPressed (){
    fullNameTextField.clear();
    usernameTextField.clear();
    emailTextField.clear();
    cityTextField.clear();
    salaryTextField.clear();
    passcodePasswordField.clear();
    updateButton.setDisable(true);
    deleteButton.setDisable(true);
    rolesChoice.setValue("Administrator Role");

}
    private void clearTextFields() {
        rolesChoice.setValue("Administrator Role");
        fullNameTextField.clear();
        usernameTextField.clear();
        emailTextField.clear();
        cityTextField.clear();
        salaryTextField.clear();
        passcodePasswordField.setText("");

    }
    @FXML
    public void onBackButtonPressed (){
        stageManager.switchScene(FxmlView.AdminMenuPage);
    }

}