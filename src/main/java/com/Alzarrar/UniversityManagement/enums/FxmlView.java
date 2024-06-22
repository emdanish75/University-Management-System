package com.Alzarrar.UniversityManagement.enums;

import java.util.ResourceBundle;

public enum FxmlView {
    LoginPageUni{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "LoginPageUni.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/LoginPageUni.fxml";
        }
    },
    SignUpPage{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "SignUpPage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/SignUpPage.fxml";
        }
    },
    AdminMenuPage{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "AdminMenuPage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/AdminMenu.fxml";
        }
    },
    ShowAdminPage{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "ShowAdminPage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/ShowAdmin.fxml";
        }
    },
    ShowDepartmentPage {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "ShowDepartmentPage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/ShowDepartment.fxml";
        }
    },

    ShowCoursesPage {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "ShowCoursesPage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/ShowCourses.fxml";
        }
    },
    ShowTeacherPage{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "ShowTeacherPage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/ShowTeacher.fxml";
        }
    },
    ShowStudentPage{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "ShowStudentPage.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/ShowStudentDetails.fxml";
        }
    },
    StudentFUTURE{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "StudentFUTURE.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/studentFuture.fxml";
        }
    },
    TeacherFUTURE{
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("" + "TeacherFUTURE.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/TeacherFuture.fxml";
        }
    }
    ;

    static String getStringFromResourceBundle(final String key)
    {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

    public abstract String getTitle();
    public abstract String getFxmlFile();
}
