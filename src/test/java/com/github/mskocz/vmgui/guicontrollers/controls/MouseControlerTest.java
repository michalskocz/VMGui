package com.github.mskocz.vmgui.guicontrollers.controls;


import com.github.mskocz.vmgui.guicontrollers.Drowing.Render;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MouseControlerTest {
    @BeforeAll
    static void diableRender() {
        Render.diableRender();
    }


    @BeforeEach
    void resetScale() throws Exception {
        setScale(1.0);
    }

    @AfterAll
    static void cleanup() {
        MouseControler.kill();
    }

    @Test
    void scroll_up_increases_scale_by_point_one() throws Exception {
        invokeScroll(1.0);
        assertEquals(1.1, MouseControler.getScale(), 0.0001);
    }

    @Test
    void scroll_down_decreases_scale_by_point_one() throws Exception {
        invokeScroll(-1.0);
        assertEquals(0.9, MouseControler.getScale(), 0.0001);
    }

    @Test
    void scroll_does_not_go_above_max_scale() throws Exception {
        setScale(5.0);
        invokeScroll(1.0);
        assertEquals(5.0, MouseControler.getScale(), 0.0001);
    }

    @Test
    void scroll_does_not_go_below_min_scale() throws Exception {
        setScale(0.2);
        invokeScroll(-1.0);
        assertEquals(0.2, MouseControler.getScale(), 0.0001);
    }

    private static void invokeScroll(double deltaY) throws Exception {
        Method method = MouseControler.class.getDeclaredMethod("Scroll", double.class);
        method.setAccessible(true);
        method.invoke(null, deltaY);
    }

    @SuppressWarnings("unchecked")
    private static void setScale(double value) throws Exception {
        Field field = MouseControler.class.getDeclaredField("scale");
        field.setAccessible(true);
        AtomicReference<Double> scaleRef = (AtomicReference<Double>) field.get(null);
        scaleRef.set(value);
    }

}
