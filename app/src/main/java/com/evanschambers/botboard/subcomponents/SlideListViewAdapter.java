package com.evanschambers.botboard.subcomponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private static final String TAG = SlideListViewAdapter.class.getSimpleName();

    private Context mContext = null;
    private LayoutInflater li = null;
    public static Slide[] slides = null;

    public static int lastClickedItemIndex = -1;

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

        //listeners
        LinearLayout slideListItemContainer = (LinearLayout) listItemView.findViewById(R.id.slideListItemContainer);
        slideListItemContainer.setOnClickListener(new SlideListItemEditActionOnClickListener());
        slideListItemContainer.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));
        slideListItemContainer.setTag(R.integer.TAG_SLIDE_OBJECT, slides[position]);
        slideListItemContainer.setTag(R.integer.TAG_SLIDE_CONTENT_INITIATOR, R.integer.TAG_SLIDE_LIST_ITEM_CONTAINER_GOTO_EDIT_BUTTON);

        ImageButton slideListItemDetailButton = (ImageButton) listItemView.findViewById(R.id.slideListItemImageButton);
        slideListItemDetailButton.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));
        slideListItemDetailButton.setTag(R.integer.TAG_SLIDE_OBJECT, slides[position]);
        slideListItemDetailButton.setTag(R.integer.TAG_SLIDE_CONTENT_INITIATOR, R.integer.TAG_SLIDE_LIST_ITEM_BUTTON_GOTO_CONTENT);
        slideListItemDetailButton.setOnClickListener(new SlideListItemGoToSlideContentActionOnClickListener());

        //this is in the layout that results when clicking the slideListItemContainer
        ImageButton slideDetailContentButton = (ImageButton) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideContextBuildImageButton);
        slideDetailContentButton.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));
        slideDetailContentButton.setTag(R.integer.TAG_SLIDE_OBJECT, slides[position]);
        slideDetailContentButton.setTag(R.integer.TAG_SLIDE_CONTENT_INITIATOR, R.integer.TAG_SLIDE_EDIT_DETAIL_CONTENT_BUTTON_);
        slideDetailContentButton.setOnClickListener(new SlideListItemGoToSlideContentActionOnClickListener());

        TextView slideName = (TextView) listItemView.findViewById(R.id.slideListItemSlideName);
        slideName.setText(theSlide.getTitle());

        TextView slideType = (TextView) listItemView.findViewById(R.id.slideListItemSlideType);
        slideType.setText(theSlide.getType());

        TextView slideDuration = (TextView) listItemView.findViewById(R.id.slideListItemNameTextView);
        slideDuration.setText(((float) theSlide.getTiming().getSlideTime() / 10000.0) + "s");

        return listItemView;
    }
}