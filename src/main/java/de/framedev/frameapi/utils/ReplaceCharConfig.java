package de.framedev.frameapi.utils;/*
 *This Plugin was Created by FramDev
 * Copyrighted by FramDev
 */

public class ReplaceCharConfig {


    public static String replaceParagraph(String text) {
        text = text.replace('&','�');
        return text;
    }
    public static Boolean convertStringToBoolean(String text) {
        return Boolean.parseBoolean(text);
    }
    public static Double convertIntegerToDouble(int number) {
        return Double.valueOf(number);
    }
    public static Integer convertDoubleToInteger(double number) {
        return (int)number;
    }
    public static Integer convertStringToInteger(String text) {
        return Integer.parseInt(text);
    }
    public static String replaceObjectWithData(String text,String object,String data) {
        text = text.replace(object,data);
        return text;
    }
    public static Integer convertObjectToInteger(Object object) {
        return Integer.parseInt(object.toString());
    }
    public static String convertObjectToString(Object object) {
        return String.valueOf(object.toString());
    }
    public static Double convertObjectToDouble(Object object) {
        return Double.parseDouble(object.toString());
    }
    public static Float convertObjectToFloat(Object object) {
        return Float.parseFloat(object.toString());
    }
}
