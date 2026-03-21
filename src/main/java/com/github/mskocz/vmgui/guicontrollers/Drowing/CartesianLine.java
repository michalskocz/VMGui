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


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class CartesianLine extends CanvasUtils {

    public record CartesianLineParameters(Paint paint, double width) {}

    public static void drow(
            WindowInfo window,
            CartesianPoint a, CartesianPoint b,
            CartesianLineParameters parameters
           )
            throws NullPointerException, IllegalArgumentException{

        if (window == null || a == null || b == null || parameters == null) {
            throw new NullPointerException("Trying to draw a Cartesian line using empty parameters");
        }
        GraphicsContext gc = window.gc();
        Paint paint = parameters.paint();

        if (gc == null || paint == null) {
            throw new NullPointerException("Trying to draw a Cartesian line using empty sub parameters");
        }

        double width = window.width();
        double height = window.height();
        double lineWidth = parameters.width;

        if(isPointInvalid(a, width, height) || isPointInvalid(b, width, height)) {
            throw new IllegalArgumentException("The points value passed is invalid.");
        }

        gc.setStroke(paint);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(a.x(), a.y(), b.x(), b.y());
    }

    private CartesianLine() throws ExceptionInInitializerError{
        throw new ExceptionInInitializerError("Cannot create an instance of this class");
    }
}
