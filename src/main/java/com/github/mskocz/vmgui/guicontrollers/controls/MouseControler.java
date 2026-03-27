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

package com.github.mskocz.vmgui.guicontrollers.controls;


import com.github.mskocz.vmgui.guicontrollers.Drowing.CartesianPoint;
import com.github.mskocz.vmgui.guicontrollers.Drowing.Render;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;


public final class MouseControler {
    private static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private static final Thread workerThread = new Thread(MouseControler::runNext);
    private static final AtomicReference<Boolean> run = new AtomicReference<>(true);
    private static final AtomicReference<Double> scale = new AtomicReference<>(1.0);

    static {
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public static void kill() {
        run.set(false);
        try {
            if (workerThread.isAlive()) {
                workerThread.join(200);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            workerThread.interrupt();
        }

    }

    public static void handlMuve(SoftReference<MouseEvent> mouseEvent){
        var p = getXY(mouseEvent);
        if (p != null)
            queue.add(() -> Muve(
                    Objects.requireNonNull(p.get(), "Empty point").x(),
                    Objects.requireNonNull(p.get(), "Empty point").y()));
    }

    public static void handlPresed(SoftReference<MouseEvent> mouseEvent) {
        var p = getXY(mouseEvent);
        if (p != null)
            queue.add(() -> Presed(
                    Objects.requireNonNull(p.get(), "Empty point").x(),
                    Objects.requireNonNull(p.get(), "Empty point").y()));
    }

    public static void handleReleased(SoftReference<MouseEvent> mouseEvent) {
        var p = getXY(mouseEvent);
        if (p != null)
            queue.add(() -> Release(
                    Objects.requireNonNull(p.get(), "Empty point").x(),
                    Objects.requireNonNull(p.get(), "Empty point").y()));
    }

    public static void handleClicked(SoftReference<MouseEvent> mouseEvent) {
        var p = getXY(mouseEvent);
        if (p != null)
            queue.add(() -> Clicked(
                    Objects.requireNonNull(p.get(), "Empty point").x(),
                    Objects.requireNonNull(p.get(), "Empty point").y(),
                    Objects.requireNonNull(mouseEvent.get()).getButton()));
    }

    public static void handleScroll(SoftReference<ScrollEvent> scrollEvent) {
        if (scrollEvent != null) queue.add(() -> Scroll(Objects.requireNonNull(scrollEvent.get(),"Empty point").getDeltaY()));
    }

    public static Double getScale() {
        return scale.get();
    }

    private static void  Muve(double x, double y) {

    }

    private static void Presed(double x, double y) {
        System.out.println("Presed x: " + x  + " y: " + y);
    }
    private static void Release(double x, double y) {
        System.out.println("Release x: " + x  + " y: " + y);
    }

    private static void Clicked(double x, double y, MouseButton button) {
        //Render.clean();
    }

    private static void Scroll(double deltaY) {
        double old = scale.get();
        double current;
        if (deltaY > 0) {
            current = Math.min(old + 0.1, 5.0);
        } else if (deltaY < 0) {
            current = Math.max(0.2, old - 0.1);
        } else {
            current = old;
        }

        if (Math.abs(current - old) > 0.0001) {
            scale.set(current);
            Render.redrow();
        }
    }


    private static SoftReference<CartesianPoint> getXY(SoftReference<MouseEvent> mouseEvent) {
        if (mouseEvent == null || mouseEvent.get() == null) return null;
        double x = Objects.requireNonNull(mouseEvent.get()).getX();
        double y = Objects.requireNonNull(mouseEvent.get()).getY();
        var p = new CartesianPoint(x, y);
        return new SoftReference<>(p);
    }

    private static void runNext() {
        try {
            while (run.get() && ! workerThread.isInterrupted()) {
                Runnable task = queue.take();
                task.run();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
