package com.siamin.fivestart.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import com.siamin.fivestart.helpers.LanguageHelper;

public class MyImageView extends AppCompatImageView {

    public MyImageView(Context context, AttributeSet attis) {
        super(context, attis);

        LanguageHelper lh = new LanguageHelper(context);
        if (lh.getLanguage().equals("fa")){
            this.setRotation(-180);
        }
    }
}
