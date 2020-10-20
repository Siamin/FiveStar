package com.siamin.fivestart.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;

import com.siamin.fivestart.helpers.LanguageHelper;

public class MyTextView extends AppCompatTextView {


    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LanguageHelper lh = new LanguageHelper(context);
        if (lh.getLanguage().equals("fa")){
            this.setGravity(View.FOCUS_RIGHT);
            this.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
