/*
 * AsyncTask.fx
 *
 * Created on Jan 09, 2010, 2:44:57 PM
 */
package com.jfxtools.database.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class
 * @author Winston Prakash
 */
public class Util {

    private static void printTimeStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss:SS Z");
        System.out.print(formatter.format(new Date()) + " ==> ");
    }

    public static void log(Exception ex) {
        if (enableDebug()) {
            log(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void log(String string) {
            println(string);
    }

    public static void println(String string) {
        if (enableDebug()) {
            printTimeStamp();
            System.out.println(string);
        }
    }

    public static boolean enableDebug() {
        return true;
        //return Boolean.getBoolean("debug");
    }
}
