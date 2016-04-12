package com.evanschambers.botboard.subcomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.evanschambers.botboard.R;

/**
 * Created by timvalentine on 4/4/16.
 * did this to override getSolidColor() because scrollview does not
 * use the theme colors set in the projects theme
 */
public class ECScrollView extends ScrollView {
    public ECScrollView(Context context) {
        super(context);
    }

    public ECScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ECScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ECScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getSolidColor() {
        return getResources().getColor(R.color.scrollview_fading_edge_light_orange);
    }
}
