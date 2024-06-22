package com.Alzarrar.UniversityManagement.controller;

import com.Alzarrar.UniversityManagement.Entity.DepartmentEntity;
import com.Alzarrar.UniversityManagement.Service.DepartmentService;
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
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ShowDepartmentsController implements Initializable {

    @FXML
    private TableView<DepartmentEntity> departmentsDetail;

    @FXML
    private TableColumn<DepartmentEntity, String> departmentCodeColumn;

    @FXML
    private TableColumn<DepartmentEntity, String> departmentNameColumn;

    @FXML
    private TableColumn<DepartmentEntity, String> establishedYearColumn;

    @FXML
    private TableColumn<DepartmentEntity, String> contactNumberColumn;

    @FXML
    private TextField departmentCodeTextField;

    @FXML
    private TextField departmentNameTextField;

    @FXML
    private TextField establishedYearTextField;

    @FXML
    private TextField contactNumberTextField;

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

    private final DepartmentService departmentService;
    private final StageManager stageManager;

    public ShowDepartmentsController(@Lazy DepartmentService departmentService, @Lazy StageManager stageManager) {
        this.departmentService = departmentService;
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupDepartmentsTable();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        departmentsDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);

                departmentCodeTextField.setText(newSelection.getDepartmentCode());
                departmentNameTextField.setText(newSelection.getDepartmentName());
                establishedYearTextField.setText(String.valueOf(newSelection.getEstablishedYear()));
                contactNumberTextField.setText(newSelection.getContactNumber());
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);

                departmentCodeTextField.clear();
                departmentNameTextField.clear();
                establishedYearTextField.clear();
                contactNumberTextField.clear();
            }
        });

        // Event filter to allow only integer values in the established year text field
        establishedYearTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        // Event filter to allow only integer values in the contact number text field
        contactNumberTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
    }

    private List<DepartmentEntity> findAllDepartments() {
        return departmentService.findAll();
    }

    private void setupDepartmentsTable() {
        List<DepartmentEntity> departmentsData = findAllDepartments();

        departmentCodeColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getDepartmentCode())
        );

        departmentNameColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getDepartmentName())
        );

        establishedYearColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(String.valueOf(cellData.getValue().getEstablishedYear()))
        );

        contactNumberColumn.setCellValueFactory(
                (cellData) -> new SimpleStringProperty(cellData.getValue().getContactNumber())
        );

        departmentsDetail.setItems(FXCollections.observableList(departmentsData));
    }

    @FXML
    public void onAddButtonPressed() {
        try {
            String departmentCode = departmentCodeTextField.getText();
            String departmentName = departmentNameTextField.getText();
            String establishedYearText = establishedYearTextField.getText();
            String contactNumber = contactNumberTextField.getText();

            if (isEmpty(departmentCode) || isEmpty(departmentName) || isEmpty(establishedYearText) || isEmpty(contactNumber)) {
                StringBuilder errorMessage = new StringBuilder("Fields cannot be empty: ");
                if (isEmpty(departmentCode)) errorMessage.append("Department Code, ");
                if (isEmpty(departmentName)) errorMessage.append("Department Name, ");
                if (isEmpty(establishedYearText)) errorMessage.append("Established Year, ");
                if (isEmpty(contactNumber)) errorMessage.append("Contact Number, ");

                JavaFXUtils.showError(errorMessage.toString().replaceAll(", $", ""));
            } else if (departmentService.existsByDepartmentCode(departmentCode)) {
                JavaFXUtils.showError("Department Code already exists");
                departmentCodeTextField.clear();
            } else {
                DepartmentEntity departmentEntity = new DepartmentEntity();
                departmentEntity.setDepartmentCode(departmentCode);
                departmentEntity.setDepartmentName(departmentName);
                departmentEntity.setEstablishedYear(Integer.parseInt(establishedYearText));
                departmentEntity.setContactNumber(contactNumber);

                departmentService.save(departmentEntity);

                JavaFXUtils.showSuccessMessage("Department added successfully!");

                refreshAndSortTableView();

                clearTextFields();
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while adding department!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onUpdateButtonPressed() {
        try {
            DepartmentEntity selectedDepartment = departmentsDetail.getSelectionModel().getSelectedItem();

            if (selectedDepartment != null) {
                boolean isEmpty = false;
                StringBuilder errorMessage = new StringBuilder("These fields cannot be empty: ");

                if (isEmpty(departmentCodeTextField.getText())) {
                    errorMessage.append("Department Code, ");
                    isEmpty = true;
                }
                if (isEmpty(departmentNameTextField.getText())) {
                    errorMessage.append("Department Name, ");
                    isEmpty = true;
                }
                if (isEmpty(establishedYearTextField.getText())) {
                    errorMessage.append("Established Year, ");
                    isEmpty = true;
                }
                if (isEmpty(contactNumberTextField.getText())) {
                    errorMessage.append("Contact Number, ");
                    isEmpty = true;
                }

                if (isEmpty) {
                    JavaFXUtils.showError(errorMessage.toString().replaceAll(", $", ""));
                    return;
                }

                selectedDepartment.setDepartmentCode(departmentCodeTextField.getText());
                selectedDepartment.setDepartmentName(departmentNameTextField.getText());
                selectedDepartment.setEstablishedYear(Integer.parseInt(establishedYearTextField.getText()));
                selectedDepartment.setContactNumber(contactNumberTextField.getText());

                departmentService.save(selectedDepartment);

                JavaFXUtils.showSuccessMessage("Department updated successfully!");

                refreshAndSortTableView();

                clearTextFields();
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            } else {
                JavaFXUtils.showError("No department selected for update!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while updating department!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteButtonPressed() {
        try {
            DepartmentEntity selectedDepartment = departmentsDetail.getSelectionModel().getSelectedItem();

            if (selectedDepartment != null) {
                departmentService.delete(selectedDepartment.getDepartmentName());

                departmentsDetail.getItems().remove(selectedDepartment);

                JavaFXUtils.showSuccessMessage("Department deleted successfully!");
                deleteButton.setDisable(true);
                updateButton.setDisable(true);
                clearTextFields();
            } else {
                JavaFXUtils.showError("No department selected for deletion!");
            }
        } catch (Exception e) {
            JavaFXUtils.showError("Error occurred while deleting department!");
            e.printStackTrace();
        }
    }

    @FXML
    public void onClearButtonPressed() {
        clearTextFields();
    }

    private void clearTextFields() {
        departmentCodeTextField.clear();
        departmentNameTextField.clear();
        establishedYearTextField.clear();
        contactNumberTextField.clear();
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void refreshAndSortTableView() {
        List<DepartmentEntity> departmentsData = findAllDepartments();
        departmentsDetail.setItems(FXCollections.observableList(departmentsData));
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
}
