package com.evanschambers.botboard.subcomponents;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.evanschambers.botboard.BotBoardApplication;
import com.evanschambers.botboard.BotBoardMain;
import com.evanschambers.botboard.R;
import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;
import com.evanschambers.botboard.datamodels.Deck;
import com.evanschambers.botboard.datamodels.Slide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by timvalentine on 3/28/16.
 */
public class DeckGridViewAdapter extends BaseAdapter {
    private static final String TAG = DeckGridViewAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater li;
    private Deck[] deckArray;

    private static int lastClickedItemIndex = -1;

    public DeckGridViewAdapter(Context c, Deck[] deckArray) {
        mContext = c;
        li = LayoutInflater.from(mContext);
        this.deckArray = deckArray;
    }

    public void setDeckArray(Hashtable<Integer, Deck> newDecks) {
        deckArray = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(newDecks);
        this.notifyDataSetChanged();
    }

    public void setDeckArray(Deck[] newDecks) {
        deckArray = newDecks;
        this.notifyDataSetChanged();
    }

    public int getLastClickedItemIndex() {
        return lastClickedItemIndex;
    }

    public int getCount() {
        int size = 0;

        if (BotBoardApplication.userRecord != null) {
            if (deckArray != null) {
                size = deckArray.length;
            }
        }

        return size;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            View theNewView = li.inflate(R.layout.botboard_deck_grid_item_layout, null);
            gridItemView = theNewView;
        } else {
            gridItemView = (View) convertView;
        }
        gridItemView.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));

        //get the deck and populate the grid item
        Deck theDeck;
        if (deckArray == null || deckArray.length <= 0) {
            theDeck = Deck.createDefaultDeck();
        } else {
            theDeck = deckArray[position];
        }

        TextView deckName = (TextView) gridItemView.findViewById(R.id.deckTableItemNameTextView);
        deckName.setText(theDeck.getTitle());

        ImageButton deckImage = (ImageButton) gridItemView.findViewById(R.id.deckTableItemImageButton);
        deckImage.setOnClickListener(new DeckGridItemGoToSlidesOnClickListener());

        deckImage.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));
        new DownloadImageForImageButtonAsyncTask(deckImage).execute(theDeck.getThumbnail());

        LinearLayout deckItemContainer = (LinearLayout) gridItemView.findViewById(R.id.deckTableItemContainer);
        deckItemContainer.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));
        deckItemContainer.setOnClickListener(new DeckGridItemEditOnClickListener());

        return gridItemView;
    }

    private class DeckGridItemGoToSlidesOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //get the slides for the deck clicked/selected
            View parentView = (View) v.getParent();
            TextView deckName = (TextView) parentView.findViewById(R.id.deckTableItemNameTextView);
            TextView deckNameForSlides = (TextView) BotBoardMain.mSlidesSubcontentContainer.findViewById(R.id.mainScreenTitleTextViewSlidesPlayDeckName);
            deckNameForSlides.setText(deckName.getText());

            int deckItemIndex = ((Integer) v.getTag(R.integer.TAG_POSITION_INDEX)).intValue();
            lastClickedItemIndex = deckItemIndex;
            Slide[] slides = BotBoardFirebaseRecord.convertSlidesHashtableToSlideArray(DeckGridViewAdapter.this.deckArray[lastClickedItemIndex].getSlides());
            ((SlideListViewAdapter) BotBoardMain.mSlidesBrowserListview.getAdapter()).setSlideArray(slides);

            //go to detail activity for deck
            Handler handler = new Handler();
            Runnable r;
            //put in delayed Runnable so button will flash before action
            r = new Runnable() {
                public void run() {
                BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_LIST);
                }
            };
            handler.postDelayed(r, 250);
        }
    }

    private class DeckGridItemEditOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View viewIn) {
            Log.i(TAG, ">>>DeckGridItemEditOnClickListener.onClick() : ENTER");
            final View v = viewIn;

            int deckItemIndex = ((Integer) v.getTag(R.integer.TAG_POSITION_INDEX)).intValue();
            lastClickedItemIndex = deckItemIndex;
            Hashtable<Integer, Deck> decks = BotBoardApplication.userRecord.getDecks();
            Deck[] decksArray = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(decks);
            final Deck editDeck = decksArray[deckItemIndex];

            populateDeckDetailFields(editDeck);

            //go to detail activity for deck
            Handler handler = new Handler();
            Runnable r;
            //put in delayed Runnable so button will flash before action
            r = new Runnable() {
                public void run() {
                    BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.DECK_DETAIL);
                }
            };
            handler.postDelayed(r, 250);
        }

        private void saveDeckDetailFields(Deck editDeck) {
            Spinner active1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckActiveSpinner);
            String activeSelection = (String) (active1.getSelectedItem());
            if (activeSelection.equals("yes")) {
                editDeck.setActive(true);
            } else {
                editDeck.setActive(false);
            }

            EditText description1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckDescriptionEditText);
            editDeck.setDescription(description1.getText().toString());

            EditText thumbnail1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckThumbnailEditText);
            editDeck.setThumbnail(thumbnail1.getText().toString());

            EditText title1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckTitleEditText);
            editDeck.setTitle(title1.getText().toString());

            Spinner logo1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckLogoSpinner);
            String logoSelection = (String) logo1.getSelectedItem();
            editDeck.setLogo(logoSelection);

            Button created1 = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckCreatedButton);
            Button updated1 = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckUpdatedButton);
            Date createdDate = null;
            Date updatedDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                createdDate = dateFormatter.parse(created1.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                updatedDate = dateFormatter.parse(updated1.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calCreated = Calendar.getInstance();
            calCreated.setTime(createdDate);
            Calendar calUpdated = Calendar.getInstance();
            calUpdated.setTime(updatedDate);

            editDeck.setCreatedDate(calCreated.getTimeInMillis());
            editDeck.setUpdatedDate(calUpdated.getTimeInMillis());

            Hashtable<Integer, Deck> decks = BotBoardApplication.userRecord.getDecks();
            decks.put(Integer.parseInt(editDeck.getUuid()), editDeck);

            BotBoardApplication.userRecord.setDecks(decks);

            ((DeckGridViewAdapter) BotBoardMain.mDecksBrowserGridview.getAdapter()).setDeckArray(decks);

            BotBoardApplication.userRecordIsDirty = true;
        }

        private void populateDeckDetailFields(Deck editDeck) {
            Spinner active1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckActiveSpinner);
            boolean activeSelection = editDeck.getActive();
            if (activeSelection) {
                active1.setSelection(0);
            } else {
                active1.setSelection(0);
            }

            EditText description1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckDescriptionEditText);
            description1.setText(editDeck.getDescription());

            EditText thumbnail1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckThumbnailEditText);
            thumbnail1.setText(editDeck.getThumbnail());

            EditText title1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckTitleEditText);
            title1.setText(editDeck.getTitle());

            Spinner logo1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckLogoSpinner);
            String logoSelection = editDeck.getLogo();
            if (logoSelection.equals("settings")) {
                logo1.setSelection(0);
            } else {
                logo1.setSelection(1);
            }

            final Button created = (Button)BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckCreatedButton);
            final Calendar newCalendar = Calendar.getInstance();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            created.setText(dateFormatter.format(newCalendar.getTime()));

            created.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog =
                            new DatePickerDialog(v.getContext(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            Calendar newDate = Calendar.getInstance();
                                            newDate.set(year, monthOfYear, dayOfMonth);
                                            created.setText(dateFormatter.format(newDate.getTime()));
                                        }
                                    },
                                    newCalendar.get(Calendar.YEAR),
                                    newCalendar.get(Calendar.MONTH),
                                    newCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });

            final Button updated = (Button)BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckUpdatedButton);
            updated.setText(dateFormatter.format(newCalendar.getTime()));

            updated.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog =
                            new DatePickerDialog(v.getContext(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            Calendar newDate = Calendar.getInstance();
                                            newDate.set(year, monthOfYear, dayOfMonth);
                                            updated.setText(dateFormatter.format(newDate.getTime()));
                                        }
                                    },
                                    newCalendar.get(Calendar.YEAR),
                                    newCalendar.get(Calendar.MONTH),
                                    newCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });

            Button save = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckSaveButton);
            final Deck aDeck = editDeck;
            save.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDeckDetailFields(aDeck);

                    //go to detail activity for deck
                    Handler handler = new Handler();
                    Runnable r;
                    //put in delayed Runnable so button will flash before action
                    r = new Runnable() {
                        public void run() {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.DECK_GRID);
                        }
                    };
                    handler.postDelayed(r, 250);
                }
            });

            Button cancel = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckCancelButton);
            cancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to detail activity for deck
                    Handler handler = new Handler();
                    Runnable r;
                    //put in delayed Runnable so button will flash before action
                    r = new Runnable() {
                        public void run() {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.DECK_GRID);
                        }
                    };
                    handler.postDelayed(r, 250);
                }
            });
        }
    }

    public static class DeckGridItemCreateOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View viewIn) {
            Log.i(TAG, ">>>DeckGridItemCreateOnClickListener.onClick() : ENTER");

            Hashtable<Integer, Deck> decks = BotBoardApplication.userRecord.getDecks();
            Deck[] decksArray = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(decks);
            final Deck editDeck = new Deck();

            populateDeckDetailFields(editDeck);

            //go to detail activity for deck
            Handler handler = new Handler();
            Runnable r;
            //put in delayed Runnable so button will flash before action
            r = new Runnable() {
                public void run() {
                BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.DECK_DETAIL);
                }
            };
            handler.postDelayed(r, 250);
        }

        private void saveDeckDetailFields(Deck editDeck) {
            Spinner active1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckActiveSpinner);
            String activeSelection = (String) (active1.getSelectedItem());
            if (activeSelection.equals("yes")) {
                editDeck.setActive(true);
            } else {
                editDeck.setActive(false);
            }

            EditText description1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckDescriptionEditText);
            editDeck.setDescription(description1.getText().toString());

            EditText thumbnail1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckThumbnailEditText);
            editDeck.setThumbnail(thumbnail1.getText().toString());

            EditText title1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckTitleEditText);
            editDeck.setTitle(title1.getText().toString());

            Spinner logo1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckLogoSpinner);
            String logoSelection = (String) logo1.getSelectedItem();
            editDeck.setLogo(logoSelection);

            Button created1 = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckCreatedButton);
            Button updated1 = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckUpdatedButton);
            Date createdDate = null;
            Date updatedDate = null;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                createdDate = dateFormatter.parse(created1.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                updatedDate = dateFormatter.parse(updated1.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calCreated = Calendar.getInstance();
            calCreated.setTime(createdDate);
            Calendar calUpdated = Calendar.getInstance();
            calUpdated.setTime(updatedDate);

            editDeck.setCreatedDate(calCreated.getTimeInMillis());
            editDeck.setUpdatedDate(calUpdated.getTimeInMillis());

            Hashtable<Integer, Deck> decks = BotBoardApplication.userRecord.getDecks();
            decks.put(Integer.parseInt(editDeck.getUuid()), editDeck);

            BotBoardApplication.userRecord.setDecks(decks);

            ((DeckGridViewAdapter) BotBoardMain.mDecksBrowserGridview.getAdapter()).setDeckArray(decks);

            BotBoardApplication.userRecordIsDirty = true;
        }

        private void populateDeckDetailFields(Deck editDeck) {
            Spinner active1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckActiveSpinner);
            if(editDeck.getActive() != null) {
                boolean activeSelection = editDeck.getActive();
                if (activeSelection) {
                    active1.setSelection(0);
                } else {
                    active1.setSelection(1);
                }
            }else{
                active1.setSelection(1);
            }

            EditText description1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckDescriptionEditText);
            description1.setText(editDeck.getDescription());

            EditText thumbnail1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckThumbnailEditText);
            thumbnail1.setText(editDeck.getThumbnail());

            EditText title1 = (EditText) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckTitleEditText);
            title1.setText(editDeck.getTitle());

            Spinner logo1 = (Spinner) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckLogoSpinner);
            if(editDeck.getLogo() != null) {
                String logoSelection = editDeck.getLogo();
                if (logoSelection.equals("settings")) {
                    logo1.setSelection(0);
                } else {
                    logo1.setSelection(1);
                }
            }else{
                logo1.setSelection(1);
            }

            final Button created = (Button)BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckCreatedButton);
            final Calendar newCalendar = Calendar.getInstance();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            created.setText(dateFormatter.format(newCalendar.getTime()));

            created.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog =
                            new DatePickerDialog(v.getContext(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            Calendar newDate = Calendar.getInstance();
                                            newDate.set(year, monthOfYear, dayOfMonth);
                                            created.setText(dateFormatter.format(newDate.getTime()));
                                        }
                                    },
                                    newCalendar.get(Calendar.YEAR),
                                    newCalendar.get(Calendar.MONTH),
                                    newCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });

            final Button updated = (Button)BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckUpdatedButton);
            updated.setText(dateFormatter.format(newCalendar.getTime()));

            updated.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog =
                            new DatePickerDialog(v.getContext(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            Calendar newDate = Calendar.getInstance();
                                            newDate.set(year, monthOfYear, dayOfMonth);
                                            updated.setText(dateFormatter.format(newDate.getTime()));
                                        }
                                    },
                                    newCalendar.get(Calendar.YEAR),
                                    newCalendar.get(Calendar.MONTH),
                                    newCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });

            Button save = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckSaveButton);
            final Deck aDeck = editDeck;
            save.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDeckDetailFields(aDeck);

                    //go to detail activity for deck
                    Handler handler = new Handler();
                    Runnable r;
                    //put in delayed Runnable so button will flash before action
                    r = new Runnable() {
                        public void run() {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.DECK_GRID);
                        }
                    };
                    handler.postDelayed(r, 250);
                }
            });

            Button cancel = (Button) BotBoardMain.mDecksDetailSubcontentContainer.findViewById(R.id.createDeckCancelButton);
            cancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to detail activity for deck
                    Handler handler = new Handler();
                    Runnable r;
                    //put in delayed Runnable so button will flash before action
                    r = new Runnable() {
                        public void run() {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.DECK_GRID);
                        }
                    };
                    handler.postDelayed(r, 250);
                }
            });
        }
    }
}