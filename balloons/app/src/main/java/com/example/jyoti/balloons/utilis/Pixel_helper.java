package com.example.jyoti.balloons.utilis;
import android.content.Context;
import android.util.TypedValue;

public class Pixel_helper {

    public static int pixelsToDp(int px, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics());
    }

}
