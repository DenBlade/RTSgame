module com.example.rtsgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.rtsgame to javafx.fxml;
    exports com.example.rtsgame;
    exports com.example.rtsgame.map.tiles;
    opens com.example.rtsgame.map.tiles to javafx.fxml;
    exports com.example.rtsgame.map;
    opens com.example.rtsgame.map to javafx.fxml;
}