package com.example.rtsgame;

public class Utils {
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    public static boolean isBetween(double value, double low, double high) {
        return value >= low && value <= high;
    }
}
