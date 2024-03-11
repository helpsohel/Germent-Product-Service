package com.example.germentproductservice;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static SharedPreferences sharedPreferences;

    public static synchronized SharedPreferences getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }
}
