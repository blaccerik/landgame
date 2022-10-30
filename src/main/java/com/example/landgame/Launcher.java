package com.example.landgame;

import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        System.out.println("Memory");
        System.out.println(Runtime.getRuntime().totalMemory());
        System.out.println(Runtime.getRuntime().maxMemory());
        System.out.println(Runtime.getRuntime().freeMemory());
        System.out.println("------");

        MainApplication.main(args);
    }
}
