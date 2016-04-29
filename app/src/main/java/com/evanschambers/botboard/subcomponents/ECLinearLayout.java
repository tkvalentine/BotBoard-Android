package com.evanschambers.botboard.subcomponents;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.evanschambers.botboard.BotBoardApplication;
import com.evanschambers.botboard.BotBoardMain;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by timvalentine on 4/7/16.
 */
public class ECLinearLayout extends LinearLayout {
    private static final String TAG = ECLinearLayout.class.getSimpleName();
    private static ArrayList<String> dataPanelDepth = new ArrayList<String>();
    private static final Hashtable<String, String> subpanelReversePaths = new Hashtable<String, String>();

    private boolean autoSaveSetting = false;
    //private boolean autoSaveSetting = true;

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
        String tag = (String)changedView.getTag();
        boolean isReturningFromChildEditPanel = false;

        //we only want to save if record is dirty and
        //if backing out of the edit hierarchy
        boolean doTheSave = true;

        if ((visibility == View.VISIBLE)) {
            Log.w(TAG, tag + "BECOMING VISIBLE");

            int subpanelIsInPath = dataPanelDepth.indexOf(tag);
            if (subpanelIsInPath > -1) {
                //WE ARE BACKING OUT
                for (int i = dataPanelDepth.size() - 1; i > subpanelIsInPath; i--) {
                    dataPanelDepth.remove(i);
                }

                isReturningFromChildEditPanel = true;
            } else {
                //WE DID AN ACTION TO GO FORWARD ONE EDIT LEVEL
                dataPanelDepth.add(tag);
            }
        }
        //LAST SUBPANEL TO INITIATE SO GO ACTIVE
        else if ((visibility == View.GONE || visibility == View.INVISIBLE)) {
            Log.w(TAG, tag + "BECOMING GONE or INVISIBLE");

            if (tag.equals("SLIDES_DETAIL_CONTENT_SUBPANEL")) {
                //CHANGE TO AFTER IMPLEMENTING SLIDES_DETAIL_CONTENT_SNAPSHOT_SUBPANEL
                if (BotBoardMain.isActivityIntializing) {
                    BotBoardMain.isActivityIntializing = false;
                }
            }
        }

        if (BotBoardApplication.userRecordIsDirty && isReturningFromChildEditPanel) {
//            if(tag.equals("DECK_SUBPANEL")){
//            else if(tag.equals("DECK_DETAIL_SUBPANEL")){
//            else if(tag.equals("SLIDES_SUBPANEL")){
//            else if(tag.equals("SLIDES_DETAIL_SUBPANEL")){
//            else if(tag.equals("SLIDES_DETAIL_CONTENT_SUBPANEL")){
//            //TODO: add later???
//            else if(tag.equals("SLIDES_DETAIL_CONTENT_LAYOUT_SUBPANEL")){
//            else if(tag.equals("SLIDES_DETAIL_CONTENT_SNAPSHOT_SUBPANEL")){
            if (tag != null && tag.endsWith("_SUBPANEL")) {
                Log.w(TAG, tag + " GOT A SUBPANEL FOR SAVING");
            } else {
                doTheSave = false;
            }

            if (doTheSave && autoSaveSetting) {
                //String newUserJSONValuesString = BotBoardApplication.userRecord.toJSONValueString();
                BotBoardApplication.myFirebaseRef.child("users")
                        .child(BotBoardApplication.userId)
                        .setValue(BotBoardApplication.userRecord, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    BotBoardApplication.userRecordIsDirty = false;
                                    Log.w(TAG, "Update failed! " + firebaseError.getMessage());
                                }
                            }
                        });

                BotBoardApplication.isInitialFirebaseAccess = false;
            } else if (doTheSave && !autoSaveSetting) {
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Record is dirty, push to cloud now?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //do the save stuff here depending on which panel was tagged
                                //String newUserJSONValuesString = BotBoardApplication.userRecord.toJSONValueString();
                                BotBoardApplication.myFirebaseRef.child("users")
                                        .child(BotBoardApplication.userId)
                                        .setValue(BotBoardApplication.userRecord, new Firebase.CompletionListener() {
                                            @Override
                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                if (firebaseError != null) {
                                                    BotBoardApplication.userRecordIsDirty = false;
                                                    Log.w(TAG, "Update failed! " + firebaseError.getMessage());
                                                }
                                            }
                                        });

                                BotBoardApplication.isInitialFirebaseAccess = false;

                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
            }
        }
    }
}
