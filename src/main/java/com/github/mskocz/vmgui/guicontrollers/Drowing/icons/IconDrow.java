package com.github.mskocz.vmgui.guicontrollers.Drowing.icons;

import com.github.mskocz.vmgui.guicontrollers.Drowing.CartesianPoint;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class IconDrow {
    public static void draw(GraphicsContext gc, IconType t, CartesianPoint cord, double scale) {
        Objects.requireNonNull(cord, "Cords are empty");
        Objects.requireNonNull(t, "Icon are empty");
        Objects.requireNonNull(gc, "Graphics Context is empty");
        gc.drawImage(IconsFactory.getIcon(t, scale), cord.x(), cord.y());
    }
}
