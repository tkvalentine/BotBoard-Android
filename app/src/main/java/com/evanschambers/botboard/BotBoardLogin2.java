package com.evanschambers.botboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
//public class BotBoardLogin2 extends AppCompatActivity implements
//        GoogleApiClient.OnConnectionFailedListener,
//        View.OnClickListener {
public class BotBoardLogin2 extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener  {

    private static final String TAG = BotBoardLogin2.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
//////////////////////////

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, ">>>onConnectionSuspended() : ENTERED : Login Suspended");
        // ignore
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, ">>>onConnected() : ENTERED");
        /* Connected with Google API, use this to authenticate with Firebase */
        Log.i(TAG, ">>>onConnected() : Login Connected : get auth token and login");
    }
/////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, ">>>onCreate() : ENTER");

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bot_board_login2);

            // Views
            mStatusTextView = (TextView) findViewById(R.id.status);

            // Button listeners
            findViewById(R.id.sign_in_button).setOnClickListener(this);
            findViewById(R.id.sign_out_button).setOnClickListener(this);
            findViewById(R.id.disconnect_button).setOnClickListener(this);

            // [START configure_signin]
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail().requestProfile()
                    .build();
            // [END configure_signin]

            // [START build_client]
            // Build a GoogleApiClient with access to the Google Sign-In API and the
            // options specified by gso.
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();
            // [END build_client]
        /* Setup the Google API object to allow Google+ logins */

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
//                    .addApi(Plus.API)
//                    .addScope(Plus.SCOPE_PLUS_LOGIN)
//                    .addScope(Plus.SCOPE_PLUS_PROFILE)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            // [START customize_button]
            // Customize sign-in button. The sign-in button can be displayed in
            // multiple sizes and color schemes. It can also be contextually
            // rendered based on the requested scopes. For example. a red button may
            // be displayed when Google+ scopes are requested, but a white button
            // may be displayed when only basic profile is requested. Try adding the
            // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
            // difference.
            SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setScopes(gso.getScopeArray());
            // [END customize_button]
        }catch (Exception e){
            Log.e(TAG, ">>>onCreate() : EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        hideProgressDialog();

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            BotBoardApplication.userToken = acct.getIdToken();
            BotBoardApplication.userName = acct.getDisplayName();
            BotBoardApplication.userEmail = acct.getEmail();
            BotBoardApplication.userId = acct.getId();
            BotBoardApplication.userPhotoURL = acct.getPhotoUrl();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);

            Intent intent = new Intent(BotBoardLogin2.this, BotBoardMain2.class);
            intent.putExtra("AUTH_TOKEN_EXTRA", BotBoardApplication.userToken);
            startActivity(intent);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }
}