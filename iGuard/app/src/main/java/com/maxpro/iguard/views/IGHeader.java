package com.maxpro.iguard.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.maxpro.iguard.R;

/**
 * Created by Ketan on 3/22/2015.
 */
public class IGHeader extends RelativeLayout {
    public IGHeader(Context context) {
        super(context);
        init();
    }

    public IGHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IGHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view=inflate(getContext(), R.layout.header, null);
        addView(view);
    }
}
