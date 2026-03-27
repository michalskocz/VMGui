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

import com.github.mskocz.vmgui.guicontrollers.Drowing.icons.IconDrow;
import com.github.mskocz.vmgui.guicontrollers.Drowing.icons.IconType;
import com.github.mskocz.vmgui.guicontrollers.controls.MouseControler;
import javafx.application.Platform;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.paint.Color.BLUE;

public class Render {
    private static final ConcurrentHashMap<CartesianPoint, IconType> icons = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<CartesianPoint, CartesianPoint> lines =  new ConcurrentHashMap<>();

    private static final ConcurrentLinkedQueue<Runnable> renderQueue = new ConcurrentLinkedQueue<>();
    private static final AtomicBoolean scheduled = new AtomicBoolean(false);

    private static WindowInfo window = null;
    private static AtomicBoolean disableRender = new AtomicBoolean(false);

    public static void setWindow(WindowInfo window) {
        Render.window = window;
    }
    public static void clean() {
        if (disableRender.get()) return;
        if (window != null) submit(() -> window.gc().clearRect(0, 0, window.width(), window.height()));
    }

    public static void addLine(CartesianPoint a, CartesianPoint b, CartesianLine.CartesianLineParameters par) {
        lines.put(a, b);
        submit(() -> CartesianLine.drow(window, a, b, par));
    }

    public static void addIcon(CartesianPoint cord, IconType icon) {
        icons.put(cord, icon);
        submit(() -> IconDrow.draw(window.gc(), icon, cord, MouseControler.getScale()));
    }
    public static void removeIcon(CartesianPoint cord) {
        if (cord != null) submit(() -> _removeIcon(cord));
    }

    public static void removeLine(CartesianPoint a, CartesianPoint b) {
        if (a != null && b != null) submit(() -> removeLine(a, b));
    }


    private static void submit(Runnable task) {
        if (task != null || !disableRender.get()) {
            renderQueue.add(task);
        } else return;

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
                        submit(() -> {}); // dummy task
                    }
                }
            });
        }
    }



    private static void _removeIcon(CartesianPoint cord) {
        icons.remove(cord);
        redrow();
    }

    private static void _removeLine(CartesianPoint a, CartesianPoint b) {
        lines.remove(a, b);
        redrow();
    }

    public static void redrow() {
        if (window == null || disableRender.get()) return;

        submit(() -> {
            window.gc().clearRect(0, 0, window.width(), window.height());

            for (Map.Entry<CartesianPoint, IconType> entry : icons.entrySet()) {
                CartesianPoint point = entry.getKey();
                IconType icon = entry.getValue();
                IconDrow.draw(window.gc(), icon, point, MouseControler.getScale());
            }

            for (Map.Entry<CartesianPoint, CartesianPoint> entry : lines.entrySet()) {
                CartesianPoint a = entry.getKey();
                CartesianPoint b = entry.getValue();
                CartesianLine.drow(window, a, b, new CartesianLine.CartesianLineParameters(BLUE, MouseControler.getScale()));
            }
        });
    }

    public static void diableRender() {
        disableRender.set(true);
    }
}
