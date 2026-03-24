/*
MIT License

Copyright (c) 2026 Michał Skoczylas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.github.mskocz.vmgui.guicontrollers;

import com.github.mskocz.vmgui.guicontrollers.Drowing.Render;
import com.github.mskocz.vmgui.guicontrollers.Drowing.WindowInfo;
import com.github.mskocz.vmgui.guicontrollers.controls.MouseControler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


import java.awt.Desktop;
import java.lang.ref.SoftReference;
import java.net.URI;

public final class TopologyController {
    @FXML BorderPane menu;
    @FXML private StackPane canvasContainer;
    @FXML private VBox VMBar;

    private Canvas canvas;
    private GraphicsContext gc;

    @FXML private void setPrimerLight() { AppTheme.PRIMER_LIGHT.apply(); }
    @FXML private void setPrimerDark() { AppTheme.PRIMER_DARK.apply(); }
    @FXML private void setNordLight() { AppTheme.NORD_LIGHT.apply(); }
    @FXML private void setNordDark() { AppTheme.NORD_DARK.apply(); }
    @FXML private void setCupertinoLight() { AppTheme.CUPERTINO_LIGHT.apply(); }
    @FXML private void setCupertinoDark() { AppTheme.CUPERTINO_DARK.apply(); }
    @FXML private void setDracula() { AppTheme.DRACULA.apply(); }

    @FXML private void openGithub()  {
        var github = new Thread(() -> {
            try { if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI("https://github.com/michalskocz/VMGui")); }
            catch (Exception ignore) {}
        });
        github.start();

    }

    @FXML private void initialize() {
        setUpCanvas();
        setUpMouse();
    }

    private void setUpCanvas() {
        canvas = new Canvas();
        canvas.setManaged(false);
        gc = canvas.getGraphicsContext2D();
        canvasContainer.getChildren().add(canvas);

        canvasContainer.widthProperty().addListener((obs, oldVal, newVal) -> redraw());
        canvasContainer.heightProperty().addListener((obs, oldVal, newVal) -> redraw());
    }


    private void setUpMouse() {
        canvasContainer.setOnMouseClicked(mouseEvent ->
                MouseControler.handleClicked(new SoftReference<>(mouseEvent)));
        canvasContainer.setOnMousePressed(mouseEvent ->
                MouseControler.handlPresed(new SoftReference<>(mouseEvent)));
        canvasContainer.setOnMouseReleased(mouseEvent ->
                MouseControler.handleReleased(new SoftReference<>(mouseEvent)));
        canvasContainer.setOnMouseMoved(mouseEvent ->
                MouseControler.handlMuve(new SoftReference<>(mouseEvent)));
        canvasContainer.setOnScroll(scrollEvent ->
                MouseControler.handleScroll(new SoftReference<>(scrollEvent)));
    }

    private void redraw() {
        double width = canvasContainer.getWidth();
        double height = canvasContainer.getHeight();

        if (width <= 1.0 || height <= 1.0) {
            return;
        }

        canvas.setWidth(width);
        canvas.setHeight(height);

        gc.clearRect(0, 0, width, height);
        Render.setWindow(new WindowInfo(gc, canvas.getWidth(), canvas.getHeight()));
    }


}