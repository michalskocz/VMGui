package com.github.mskocz.vmgui.guicontrollers.Drowing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CanvasUtilsTest {
    @DisplayName("Point validation")
    @Test
    public void poinValidation() {

        double width_valid = 1980;
        double height_valid = 1080;
        for(double i = 0; i < 100; i++) {
            double ax = width_valid * ((i + 1) / 200);
            double ay = height_valid * ((i +1)/ 200);

            assertFalse(CanvasUtils.isPointInvalid(new CartesianPoint(ax, ay), width_valid, height_valid));
        }

        for(double i = 0; i < 100; i++) {
            double ax = width_valid * (200 / (i +1)) + 2;
            double ay = height_valid * (200 / (i +1)) + 2;

            assertTrue(CanvasUtils.isPointInvalid(new CartesianPoint(ax, ay), width_valid, height_valid));
        }

        assertThrows(IllegalArgumentException.class, () -> CanvasUtils.isPointInvalid(new CartesianPoint(67, 67), -6, 7));

    }
}
