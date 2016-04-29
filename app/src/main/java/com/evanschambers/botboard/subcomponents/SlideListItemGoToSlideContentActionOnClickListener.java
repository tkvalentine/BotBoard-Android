package com.evanschambers.botboard.subcomponents;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.evanschambers.botboard.BotBoardMain;
import com.evanschambers.botboard.R;
import com.evanschambers.botboard.datamodels.Slide;

/**
 * Created by timvalentine on 4/27/16.
 */
public class SlideListItemGoToSlideContentActionOnClickListener extends SlideBaseOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        //get the slides for the deck clicked/selected
        String theTag = (String) v.getTag();
        if (theTag.equals("SLIDE_DETAIL_CONTENT_BUTTON")) {
            TextView slideName = (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerSlideName);
            slideName.setText(slideName.getText());
        } else {// if (theTag.equals("SLIDE_DETAIL_CONTAINER") || theTag.equals("SLIDE_LIST_CREATE_SLIDE_BUTTON")) {
            View parentView = (View) v.getParent();
            TextView slideName = (TextView) parentView.findViewById(R.id.slideListItemSlideName);
            TextView slideNameForContent = (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.mainScreenTitleTextViewSlidesDetailContentList);
            slideNameForContent.setText(slideName.getText());
        }

        int slideItemIndex = ((Integer) v.getTag(R.integer.TAG_POSITION_INDEX)).intValue();
        SlideListViewAdapter.lastClickedItemIndex = slideItemIndex;

        Slide theSlide = (Slide) v.getTag(R.integer.TAG_SLIDE_OBJECT);
        SlideListItemEditActionOnClickListener.currentEditSlide = theSlide;

        final int contentInitiator = ((Integer) v.getTag(R.integer.TAG_SLIDE_CONTENT_INITIATOR)).intValue();

        populateSlideContentFields(contentInitiator);

        adjustVisibleInputFieldsForSlideContent();

        //go to detail activity for slide content
        Handler handler = new Handler();
        Runnable r;
        //put in delayed Runnable so button will flash before action
        r = new Runnable() {
            public void run() {
                BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL_CONTENT);
            }
        };
        handler.postDelayed(r, 250);
    }
}
