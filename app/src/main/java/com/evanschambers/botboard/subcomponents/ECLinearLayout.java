package com.evanschambers.botboard.subcomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.evanschambers.botboard.BotBoardApplication;
import com.evanschambers.botboard.BotBoardMain;

/**
 * Created by timvalentine on 4/7/16.
 */
public class ECLinearLayout extends LinearLayout {
    public ECLinearLayout(Context context) {
        super(context);
    }

    public ECLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ECLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ECLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if ((visibility == View.GONE || visibility == View.INVISIBLE) && !BotBoardMain.isActivityInCreation) {
            if (BotBoardApplication.userRecordIsDirty) {
                //save the record automatically??
                Toast.makeText(this.getContext(), "Record is dirty, should save.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
