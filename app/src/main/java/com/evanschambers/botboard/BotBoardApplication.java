package com.evanschambers.botboard;

import android.app.Application;

import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;

/**
 * Created by timvalentine on 3/3/16.
 */

public class BotBoardApplication extends Application {
    //get a reference to a Firebase
    public static com.firebase.client.Firebase myFirebaseRef;
    public static String userName;
    public static String userEmail;
    public static String userId;
    public static String userToken;
    public static android.net.Uri userPhotoURL;
    public static String userAuthProvider;
    public static String appSecret = "BBwlPFTEMISGXNTvQz5nheil1SC3PgyPqIWxEvIV";

    public static boolean isInitialFirebaseAccess = true;
    public static BotBoardFirebaseRecord userRecord;

    public static boolean userRecordIsDirty = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
