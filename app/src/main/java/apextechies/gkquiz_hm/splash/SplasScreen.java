package apextechies.gkquiz_hm.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.activity.MainActivity;
import apextechies.gkquiz_hm.common.ClsGeneral;
import apextechies.gkquiz_hm.interfaces.OnTaskCompleted;
import apextechies.gkquiz_hm.login.SignUp;
import apextechies.gkquiz_hm.utilz.Download_web;
import apextechies.gkquiz_hm.utilz.Utility;
import apextechies.gkquiz_hm.utilz.WebServices;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Shankar on 1/2/2018.
 */

public class SplasScreen extends AppCompatActivity {
    public static final int RC_SIGN_IN = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splas_activity);

        if (ClsGeneral.getPreferences(SplasScreen.this, "id").equalsIgnoreCase(""))
        {
            checkSession();
        }
       else if (ClsGeneral.getPreferences(SplasScreen.this, "userEmail").equalsIgnoreCase(""))
        {
            startActivity(new Intent(SplasScreen.this, SignUp.class));
            finish();
        }
        else
        {
            Thread lTimer = new Thread() {

                public void run() {

                    try {
                        int lTimer1 = 0;
                        while (lTimer1 < 3000) {
                            sleep(100);
                            lTimer1 = lTimer1 + 100;
                        }
                        startActivity(new Intent(SplasScreen.this, MainActivity.class));
                        finish();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    finally {
                        finish();
                    }
                }
            };
            lTimer.start();
        }


    }

    private void checkSession() {
        AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
                new OnSuccessListener() {

                    @Override
                    public void onSuccess(Object o) {
                        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                        if (u != null) {

                            callApi(u.getPhoneNumber());

                        } else {
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setProviders(
                                                    //  Arrays.asList(
                                                    Collections.singletonList(
                                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                                    )
                                            )
                                            .setTheme(R.style.LoginTheme)
                                            .setLogo(R.mipmap.ic_launcher)
                                            .build(),
                                    RC_SIGN_IN);
                        }
                    }
                }).addOnFailureListener(this,
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == ResultCodes.OK) {
                callApi(response.getPhoneNumber());
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login", "Login canceled by User");
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error");
                }
            }
            Log.e("Login", "Unknown sign in response");

        }
    }

    private void callApi(final String mobileNumber) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        Utility.showDailog(SplasScreen.this, getResources().getString(R.string.pleaseWait));

        Download_web web = new Download_web(SplasScreen.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utility.closeDialog();
                if (response.length()>0)
                {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true"))
                        {
                            JSONArray array = object.getJSONArray("data");
                            JSONObject jo = array.getJSONObject(0);
                            ClsGeneral.setPreferences(SplasScreen.this, "id", jo.optString("user_id"));
                            ClsGeneral.setPreferences(SplasScreen.this, "userName", jo.optString("userName"));
                            ClsGeneral.setPreferences(SplasScreen.this, "userEmail", jo.optString("userEmail"));
                            ClsGeneral.setPreferences(SplasScreen.this, "userMobile", jo.optString("userMobile"));
                            if (ClsGeneral.getPreferences(SplasScreen.this, "userEmail").equalsIgnoreCase(""))
                            {
                                startActivity(new Intent(SplasScreen.this, SignUp.class));
                                finish();
                            }
                            else
                            {
                                startActivity(new Intent(SplasScreen.this, MainActivity.class));
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        nameValuePairs.add(new BasicNameValuePair("mobile",mobileNumber));
        web.setReqType("post");
        web.setData(nameValuePairs);
        web.execute(WebServices.LOGIN);
    }
}
