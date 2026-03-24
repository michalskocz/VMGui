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

package com.github.mskocz.vmgui;

import com.github.mskocz.vmgui.guicontrollers.AppTheme;
import com.github.mskocz.vmgui.guicontrollers.controls.MouseControler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public final class VMGui extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(VMGui.class.getResource("gui/Topology.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        AppTheme.DRACULA.apply();
        stage.setTitle("VM-Gui");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            MouseControler.kill();
        });
        stage.show();

    }

    public static void launch() {
        Application.launch(VMGui.class);
    }

}
