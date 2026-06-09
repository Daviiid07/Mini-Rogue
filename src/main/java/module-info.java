module org.example.garrido_david_u6a1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.garrido_david_u6a1 to javafx.fxml;
    opens org.example.garrido_david_u6a1.controller to javafx.fxml;

    exports org.example.garrido_david_u6a1;
    exports org.example.garrido_david_u6a1.controller;
    exports org.example.garrido_david_u6a1.service;
    exports org.example.garrido_david_u6a1.model;
    exports org.example.garrido_david_u6a1.model.carta;
}
