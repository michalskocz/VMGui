package com.github.mskocz.vmgui.guicontrollers;

import atlantafx.base.theme.*;
import javafx.application.Application;

public enum AppTheme {
    PRIMER_LIGHT(new PrimerLight()),
    PRIMER_DARK(new PrimerDark()),
    NORD_LIGHT(new NordLight()),
    NORD_DARK(new NordDark()),
    CUPERTINO_LIGHT(new CupertinoLight()),
    CUPERTINO_DARK(new CupertinoDark()),
    DRACULA(new Dracula());

    private final Theme theme;

    AppTheme(Theme theme) {
        this.theme = theme;
    }


    public void apply() {
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
    }
}