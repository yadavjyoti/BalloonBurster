package com.example.jyoti.balloons.utilis;
import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreHelper {

    private static final String PREFS_GLOBAL = "prefs_global"; //key to identify specific preferences
    private static final String PREF_TOP_SCORE = "pref_top_score";

//A SharedPreferences object points to a file containing key-value pairs and provides simple methods to read and write them.
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(
                PREFS_GLOBAL, Context.MODE_PRIVATE);
    }

    //  Setters and getters for global preferences
    // compares the value of current score and top score and returns boolean value
    public static boolean isTopScore(Context context, int newScore) {
        int topScore = getPreferences(context).getInt(PREF_TOP_SCORE, 0);
        return newScore > topScore;
    }

    public static int getTopScore(Context context) {
        return getPreferences(context).getInt(PREF_TOP_SCORE, 0);
    }

    public static void setTopScore(Context context, int score) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(PREF_TOP_SCORE, score);
        editor.apply();
    }

}
