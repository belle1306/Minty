package com.example.mintycards.util;

import androidx.fragment.app.Fragment;

import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

public class ReadStorageManager {
    public static void selectPhoto(Fragment fragment, int requestCode) {
        Matisse.from(fragment)
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .maxSelectable(9)
                .originalEnable(true)
                .maxOriginalSize(10)
                .imageEngine(new GlideEngine())
                .forResult(requestCode);
    }
}
