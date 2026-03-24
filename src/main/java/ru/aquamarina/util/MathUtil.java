package ru.aquamarina.util;

public class MathUtil {


    public static Long round(double value, long precision) {
        return round(Math.round(value), precision);
    }

    public static Long round(long value, long precision) {
        long remainder = value % precision;
        if (remainder == 0) {
            return value;
        }
        return value - remainder + precision;
    }

}
