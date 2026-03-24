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

package com.github.mskocz.vmgui.guicontrollers.Drowing;

import javafx.application.Platform;

import javax.swing.text.html.ImageView;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Render {
    private static final Object renderSync = new Object();
    private static WindowInfo window;
    private static final ConcurrentHashMap<CartesianPoint, ImageView> icons = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<CartesianPoint, CartesianPoint> lines =  new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<Runnable> renderQueue = new ConcurrentLinkedQueue<>();
    private static final AtomicBoolean scheduled = new AtomicBoolean(false);

    public static void setWindow(WindowInfo window) {
        Render.window = window;
    }


    public static void addLine(CartesianPoint a, CartesianPoint b, CartesianLine.CartesianLineParameters par) {
        lines.put(a, b);
        submit(() -> CartesianLine.drow(window, a, b, par));
    }

    public static void addIcon(CartesianPoint cord, ImageView icon) {
        icons.put(cord, icon);
    }


    private static void submit(Runnable task) {
        renderQueue.add(task);

        if (scheduled.compareAndSet(false, true)) {
            Platform.runLater(() -> {
                try {
                    Runnable r;
                    while ((r = renderQueue.poll()) != null) {
                        r.run();
                    }
                } finally {
                    scheduled.set(false);
                    if (!renderQueue.isEmpty()) {
                        submit(null);
                    }
                }
            });
        }
    }

    public static void clean() {
        if (window != null)
            submit(() -> window.gc().clearRect(0, 0, window.width(), window.height()));
    }

}
