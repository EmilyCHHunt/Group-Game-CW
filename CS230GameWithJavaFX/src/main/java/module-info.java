module com.example.cs230gamewithjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cs230gamewithjavafx to javafx.fxml;
    exports com.example.cs230gamewithjavafx;
}