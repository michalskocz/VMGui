package com.github.mskocz.vmgui.guicontrollers.Drowing.icons;

public enum IconType {
    ROUTER("router.svg"),
    SWITCH("switch.svg"),
    VM("vm.svg");

    private final String fileName;

    IconType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}