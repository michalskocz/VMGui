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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartesianPointTest {
    @DisplayName("Point validation")
    @Test
    public void poinValidation() {

        double width_valid = 1980;
        double height_valid = 1080;
        for(double i = 0; i < 100; i++) {
            double ax = width_valid * ((i + 1) / 200);
            double ay = height_valid * ((i +1)/ 200);
            var p = new CartesianPoint(ax, ay);
            assertFalse(p.isPointInvalid(width_valid, height_valid));
        }

        for(double i = 0; i < 100; i++) {
            double ax = width_valid * (200 / (i +1)) + 2;
            double ay = height_valid * (200 / (i +1)) + 2;
            var p = new CartesianPoint(ax, ay);
            assertTrue(p.isPointInvalid(width_valid, height_valid));
        }
        var p = new CartesianPoint(67, 67);
        assertThrows(IllegalArgumentException.class, () -> p.isPointInvalid(-6, 7));

    }
}
