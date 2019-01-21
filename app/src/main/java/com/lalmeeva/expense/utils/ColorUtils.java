package com.lalmeeva.expense.utils;

import android.graphics.Color;

public class ColorUtils {

    public boolean isValidColor(String colorHex) {
        try {
            Color.parseColor(colorHex);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
