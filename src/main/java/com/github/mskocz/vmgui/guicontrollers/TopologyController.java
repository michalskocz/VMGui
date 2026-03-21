package com.github.mskocz.vmgui.guicontrollers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URI;


public class TopologyController {
    @FXML private VBox VMBar;

    @FXML private void setPrimerLight() { AppTheme.PRIMER_LIGHT.apply(); }
    @FXML private void setPrimerDark() { AppTheme.PRIMER_DARK.apply(); }
    @FXML private void setNordLight() { AppTheme.NORD_LIGHT.apply(); }
    @FXML private void setNordDark() { AppTheme.NORD_DARK.apply(); }
    @FXML private void setCupertinoLight() { AppTheme.CUPERTINO_LIGHT.apply(); }
    @FXML private void setCupertinoDark() { AppTheme.CUPERTINO_DARK.apply(); }
    @FXML private void setDracula() { AppTheme.DRACULA.apply(); }

    @FXML private void openGithub()  {
        var github = new Thread(() -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI("https://github.com/michalskocz/VMGui"));
                } else {
                    System.out.println("Desktop not supported");
                }
            } catch (Exception ignore) {}
        });
        github.start();
    }
}
