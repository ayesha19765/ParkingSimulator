module com.example.ParkingSimulator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.logging;

    opens com.example.ParkingSimulator to javafx.fxml;
    exports com.example.ParkingSimulator;
}
