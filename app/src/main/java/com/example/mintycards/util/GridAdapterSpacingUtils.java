package com.example.mintycards.util;

import android.content.Context;
import android.util.TypedValue;

public class GridAdapterSpacingUtils {
    public static int convertIntToDP(Context context, int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }
}
