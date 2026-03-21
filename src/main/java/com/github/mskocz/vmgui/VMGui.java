package com.github.mskocz.vmgui;

import com.github.mskocz.vmgui.guicontrollers.AppTheme;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class VMGui extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(Objects.requireNonNull(VMGui.class.getResource("gui/Topology.fxml"), "Problem loading Topology.fxml configuration file"));
        Scene scene = new Scene(fxmlLoader.load());

        AppTheme.DRACULA.apply();
        stage.setTitle("VM-Gui");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

}
