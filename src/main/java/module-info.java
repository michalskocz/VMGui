module com.github.mskocz.vmgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires java.desktop;


    opens com.github.mskocz.vmgui to javafx.fxml;
    exports com.github.mskocz.vmgui;
    exports com.github.mskocz.vmgui.guicontrollers;
    opens com.github.mskocz.vmgui.guicontrollers to javafx.fxml;

}