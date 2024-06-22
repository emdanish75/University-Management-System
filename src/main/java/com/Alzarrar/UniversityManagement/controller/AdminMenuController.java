package com.Alzarrar.UniversityManagement.controller;
import com.Alzarrar.UniversityManagement.config.StageManager;
import com.Alzarrar.UniversityManagement.enums.FxmlView;
import javafx.fxml.Initializable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AdminMenuController implements Initializable {

    private final StageManager stageManager ;

    public AdminMenuController(@Lazy StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void onShowStudentButtonPressed(){
        stageManager.switchScene(FxmlView.ShowStudentPage);
    }
    public void onShowTeachersButtonPressed(){
        stageManager.switchScene(FxmlView.ShowTeacherPage);
    }
    public void onShowCoursesButtonPressed(){
        stageManager.switchScene(FxmlView.ShowCoursesPage);
    }
    public void onShowDepartmentsButtonPressed(){
        stageManager.switchScene(FxmlView.ShowDepartmentPage);
    }
    public void onSignoutButtonpressed(){
        stageManager.switchScene(FxmlView.LoginPageUni);
    }
    public void onShowAdminButtonPressed(){
        stageManager.switchScene(FxmlView.ShowAdminPage);
    }

}