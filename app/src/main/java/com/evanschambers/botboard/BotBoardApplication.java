package com.evanschambers.botboard;

import android.app.Application;
import com.firebase.client.*;

/**
 * Created by timvalentine on 3/3/16.
 */

public class BotBoardApplication extends Application {
    //get a reference to a Firebase
    public static Firebase myFirebaseRef;

    @Override
    public void onCreate() {
        super.onCreate();

        //to write to a Firebase endpoint
        //myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");

        //to read from a Firebase endpoint
        //myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
        //
        //    @Override
        //    public void onDataChange(DataSnapshot snapshot) {
        //        System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
        //    }
        //
        //    @Override public void onCancelled(FirebaseError error) { }
        //
        //});

        //create a user on a Firebase endpoint
        //To get started with Email & Password auth, enable the Email & Password provider in your Firebase app's dashboard:
        //
        //Choose the Login & Auth tab.
        //Select the Email & Password tab and enable authentication.
        //
        //myFirebaseRef.createUser("bobtony@firebase.com", "correcthorsebatterystaple", new Firebase.ValueResultHandler<Map<String, Object>>() {
        //    @Override
        //    public void onSuccess(Map<String, Object> result) {
        //        System.out.println("Successfully created user account with uid: " + result.get("uid"));
        //    }
        //    @Override
        //    public void onError(FirebaseError firebaseError) {
        //        // there was an error
        //    }
        //});
        //
        //Once you've created your first user, you can log them in using the authWithPassword method.
    }
}
