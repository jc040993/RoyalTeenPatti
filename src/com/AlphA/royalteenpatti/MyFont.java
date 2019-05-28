package com.AlphA.royalteenpatti;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class MyFont extends TextView {
	
	public MyFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                                               "aparaj.ttf");
        setTypeface(tf);
    }

}
