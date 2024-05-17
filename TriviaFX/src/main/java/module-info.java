module com.miguelangel.triviafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires QuestionModel;


    opens com.miguelangel.triviafx to javafx.fxml;
    exports com.miguelangel.triviafx;
}