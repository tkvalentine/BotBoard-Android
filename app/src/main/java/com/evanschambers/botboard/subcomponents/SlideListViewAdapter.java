package com.evanschambers.botboard.subcomponents;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.evanschambers.botboard.BotBoardMain;
import com.evanschambers.botboard.R;
import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;
import com.evanschambers.botboard.datamodels.Slide;

import java.util.Hashtable;

/**
 * Created by timvalentine on 3/30/16.
 */
public class SlideListViewAdapter extends ArrayAdapter<Slide> {//BaseAdapter {
    private Context mContext = null;
    private LayoutInflater li = null;
    private Slide[] slides = null;

    private static int lastClickedItemIndex = -1;

    public SlideListViewAdapter(Context c, Slide[] slides) {
        super(c, -1, slides);
        mContext = c;
        li = LayoutInflater.from(mContext);
        this.slides = slides;
    }

    public void setSlideArray(Hashtable<Integer, Slide> newSlides) {
        slides = BotBoardFirebaseRecord.convertSlidesHashtableToSlideArray(newSlides);
        this.notifyDataSetChanged();
    }

    public void setSlideArray(Slide[] newSlides) {
        slides = newSlides;
        this.notifyDataSetChanged();
    }

    public int getLastClickedItemIndex() {
        return lastClickedItemIndex;
    }

    public int getCount() {
        int size = 0;

        if (slides != null) {
            size = slides.length;
        }

        return size;
    }

    public Slide getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            View theNewView = li.inflate(R.layout.botboard_slide_list_item_layout, null);
            listItemView = theNewView;
        } else {
            listItemView = (View) convertView;
        }

        //get the deck and populate the grid item
        Slide theSlide = null;
        if (slides != null || slides.length > 0) {
            theSlide = slides[position];
        }

        //listener
        ImageButton slideItemDetailButton = (ImageButton) listItemView.findViewById(R.id.slideListItemImageButton);
        slideItemDetailButton.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));
        slideItemDetailButton.setTag(R.integer.TAG_SLIDE_OBJECT, slides[position]);
        slideItemDetailButton.setOnClickListener(new SlideListItemOnClickListener());

        TextView slideName = (TextView) listItemView.findViewById(R.id.slideListItemSlideName);
        slideName.setText(theSlide.getTitle());

        TextView slideType = (TextView) listItemView.findViewById(R.id.slideListItemSlideType);
        slideType.setText(theSlide.getType());

        TextView slideDuration = (TextView) listItemView.findViewById(R.id.slideListItemNameTextView);
        slideDuration.setText(((float) theSlide.getTiming().getSlideTime() / 10000.0) + "s");

        return listItemView;
    }

    private class SlideListItemOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getTag().equals("SLIDE_DETAIL_BUTTON")) {
                //go to detail activity for a slide
                int slideItemIndex = ((Integer) v.getTag(R.integer.TAG_POSITION_INDEX)).intValue();
                lastClickedItemIndex = slideItemIndex;

                Slide theSlide = ((Slide) v.getTag(R.integer.TAG_SLIDE_OBJECT));

                String barItemSlideName = theSlide.getTitle();
                TextView slideNameForSlideDetailGraphicBar = (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerSlideName);
                slideNameForSlideDetailGraphicBar.setText(barItemSlideName);

                String barItemSlideTime = "What??";
                TextView slideTimeForSlideDetailGraphicBar = (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerTime);
                slideTimeForSlideDetailGraphicBar.setText(barItemSlideTime);

                String barItemSlideTemperature = "Who??";
                TextView slideTemperatureForSlideDetailGraphicBar = (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerTemperature);
                slideTemperatureForSlideDetailGraphicBar.setText(barItemSlideTemperature);

                //PROBABLY NEED TO MOVE THIS ELSEWHERE
                if (!theSlide.getType().isEmpty() && theSlide.getType().toLowerCase().equals("picture")) {
                    ImageView slideImage = (ImageView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicImageView);
                    new DownloadImageForImageViewAsyncTask(slideImage).execute(BotBoardFirebaseRecord.convertContentsHashtableToContentsArray(theSlide.getContent())[0].getImageUrl());
                }

                adjustVisibleInputFieldsForSlideDetail(theSlide);

                Handler handler = new Handler();
                Runnable r;
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL);
                    }
                };
                handler.postDelayed(r, 250);
            }
        }

        private void adjustVisibleInputFieldsForSlideDetail(Slide theSlide) {
            String slideType = theSlide.getType();

            View slideTypeViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTypeListItemContainer);
            Spinner slideTypeSpinner = (Spinner) slideTypeViewContainer.findViewById(R.id.slideTypeSpinner);
            slideTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ;
                }

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String slideTypeText = ((TextView) view).getText().toString();
                    SlideListItemOnClickListener.this.adjustVisibleInputFieldsForSlideDetailType(slideTypeText);
                }
            });

            View slideContextViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideContentListItemContainer);
            ImageButton slideContextImageButton = (ImageButton) slideContextViewContainer.findViewById(R.id.slideContextBuildImageButton);
            slideContextImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View slideTypeViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTypeListItemContainer);
                    Spinner slideTypeSpinner = (Spinner) slideTypeViewContainer.findViewById(R.id.slideTypeSpinner);
                    String slideType = slideTypeSpinner.getSelectedItem().toString();
                    adjustVisibleInputFieldsForSlideContent(slideType);

                    //put in delayed Runnable so button will flash before action
                    Handler handler = new Handler();
                    Runnable r;
                    r = new Runnable() {
                        public void run() {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL_CONTENT);
                        }
                    };
                    handler.postDelayed(r, 250);
                }
            });

            adjustVisibleInputFieldsForSlideDetailType(slideType);
        }

        private void adjustVisibleInputFieldsForSlideContent(String slideType) {

        }

        private void adjustVisibleInputFieldsForSlideDetailType(String slideType) {
//            View slideTypeViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTypeListItemContainer);

            View slideTitleViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTitleListItemContainer);
            slideTitleViewContainer.setVisibility(View.GONE);
            View slideDurationViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideDurationListItemContainer);
            slideDurationViewContainer.setVisibility(View.VISIBLE);
            View slideTransistionDurationViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionDurationListItemContainer);
            slideTransistionDurationViewContainer.setVisibility(View.VISIBLE);
            View slideTransitionInViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionInListItemContainer);
            slideTransitionInViewContainer.setVisibility(View.VISIBLE);
            View slideTransitionOutViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionOutListItemContainer);
            slideTransitionOutViewContainer.setVisibility(View.VISIBLE);
            View slideActiveViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideActiveListItemContainer);
            slideActiveViewContainer.setVisibility(View.GONE);
            View slideLogoViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideLogoListItemContainer);
            slideLogoViewContainer.setVisibility(View.GONE);
            View slideShowFooterViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideShowFooterListItemContainer);
            slideShowFooterViewContainer.setVisibility(View.GONE);
            View slideContextViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideContentListItemContainer);
            slideContextViewContainer.setVisibility(View.VISIBLE);

            if (!slideType.isEmpty() && slideType.toLowerCase().equals("picture")) {
                slideShowFooterViewContainer.setVisibility(View.VISIBLE);
                //and content
            } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("logo")) {
                slideLogoViewContainer.setVisibility(View.VISIBLE);
                slideContextViewContainer.setVisibility(View.GONE);
                //no content
            } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("sms")) {
                slideShowFooterViewContainer.setVisibility(View.VISIBLE);
                //and content
            } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("biopanels")) {
                //and content
            } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("weather")) {
                //and content
            } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("staticvideooverlay")) {
                //and content
            } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("dropin")) {
                slideShowFooterViewContainer.setVisibility(View.VISIBLE);
                //and content
            } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("dashboard")) {
                //and content
            }
        }
    }
}