package com.example.julia.escapethecity;

public class Log {

    public static void log(String message){
        System.out.println("[DEBUG] "+message);
    }
    public static void log(Object o){
        log(o.toString());
    }
}
