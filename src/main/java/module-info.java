module org.oosd.chatappfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.oosd.chatappfx to javafx.fxml;
    exports org.oosd.chatappfx;
}