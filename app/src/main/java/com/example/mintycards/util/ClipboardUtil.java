package com.example.mintycards.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.example.mintycards.Constants;

public class ClipboardUtil {
    public static void copyText(Context context, String text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text label", Constants.TEMPLATE_URL);
        clipboard.setPrimaryClip(clip);
    }
}
