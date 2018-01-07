package apextechies.gkquiz_hm.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.interfaces.OnTaskCompleted;
import apextechies.gkquiz_hm.utilz.Download_web;
import apextechies.gkquiz_hm.utilz.Utility;
import apextechies.gkquiz_hm.utilz.WebServices;

/**
 * Created by Shankar on 1/7/2018.
 */

public class SignUp extends AppCompatActivity {

    private Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        final EditText input_name = (EditText)findViewById(R.id.input_name);
        final EditText input_email = (EditText)findViewById(R.id.input_email);
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input_name.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(SignUp.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                 if (input_email.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(SignUp.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                 if (!Utility.isValidEmail1(input_email.getText().toString()))
                 {
                     Toast.makeText(SignUp.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 else

                 {
                     submitData(input_name.getText().toString(), input_email.getText().toString());
                 }
            }
        });
    }

    private void submitData(String name, String email) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        Utility.showDailog(SignUp.this, getResources().getString(R.string.pleaseWait));

        Download_web web = new Download_web(SignUp.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utility.closeDialog();
                if (response.length()>0)
                {

                }

            }
        });
        nameValuePairs.add(new BasicNameValuePair("name",name));
        nameValuePairs.add(new BasicNameValuePair("email",email));
        web.setReqType("post");
        web.setData(nameValuePairs);
        web.execute(WebServices.SIGNUP);
    }
}
