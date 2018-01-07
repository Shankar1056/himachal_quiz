package apextechies.gkquiz_hm.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import apextechies.gkquiz_hm.R;


/**
 * Created by shankar on 2/1/18.
 */

public class AboutContact extends AppCompatActivity {

    public static final int REQUEST_PHONE_CALL = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra("from").equalsIgnoreCase("about")) {
            setContentView(R.layout.about_us);
            initWidgit("About Us");
            settingSpannable();
            settinMyRecord();
        }
        else if (getIntent().getStringExtra("from").equalsIgnoreCase("contact")) {
            setContentView(R.layout.contact_us);
            initWidgit("Contact Us");
            settingEmailSpannable();
            //settingMobileSpannable();
            settingLinkSpannable();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private void initWidgit(String text) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(text);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void settingLinkSpannable() {
        TextView website = findViewById(R.id.website);
        SpannableString ssPhone = new SpannableString(getResources().getString(R.string.text_weblink));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = "http://rkcyber.in/contact.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        };
        ssPhone.setSpan(clickableSpan, 0, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        website.setText(ssPhone);
        website.setMovementMethod(LinkMovementMethod.getInstance());
        website.setBackgroundColor(getResources().getColor(R.color.transparent));
        website.setHighlightColor(getResources().getColor(R.color.transparent));

    }

    private void settingMobileSpannable() {
        TextView call = findViewById(R.id.call);
        SpannableString ssPhone = new SpannableString(getResources().getString(R.string.text_mobile));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:09805138387"));
                if (ContextCompat.checkSelfPermission(AboutContact.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AboutContact.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    startActivity(callIntent);
                }

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        };
        ssPhone.setSpan(clickableSpan, 11, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        call.setText(ssPhone);
        call.setMovementMethod(LinkMovementMethod.getInstance());
        call.setBackgroundColor(getResources().getColor(R.color.transparent));
        call.setHighlightColor(getResources().getColor(R.color.transparent));

    }

    private void settingEmailSpannable() {
        TextView email = findViewById(R.id.email);
        SpannableString ssPhone = new SpannableString(getResources().getString(R.string.text_email_id));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "rameshkhachi@gmail.com"});

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        };
        ssPhone.setSpan(clickableSpan, 10, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        email.setText(ssPhone);
        email.setMovementMethod(LinkMovementMethod.getInstance());
        email.setBackgroundColor(getResources().getColor(R.color.transparent));
        email.setHighlightColor(getResources().getColor(R.color.transparent));

    }

    private void settinMyRecord() {
        TextView myrecord = (TextView)findViewById(R.id.myrecord);
        TextView international = (TextView)findViewById(R.id.international);
        TextView text_inational = (TextView)findViewById(R.id.inational);
        TextView text_books = (TextView)findViewById(R.id.text_books);
        TextView text_apps = (TextView)findViewById(R.id.text_apps);


        SpannableString content = new SpannableString(getResources().getString(R.string.text_worldrecord));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        myrecord.setText(content);

        SpannableString content_2 = new SpannableString(getResources().getString(R.string.text_international));
        content_2.setSpan(new UnderlineSpan(), 0, content_2.length(), 0);
        international.setText(content_2);

        SpannableString content_3 = new SpannableString(getResources().getString(R.string.text_inational));
        content_3.setSpan(new UnderlineSpan(), 0, content_3.length(), 0);
        text_inational.setText(content_3);

        SpannableString content_4 = new SpannableString(getResources().getString(R.string.bookspublis));
        content_4.setSpan(new UnderlineSpan(), 0, content_4.length(), 0);
        text_books.setText(content_4);

        SpannableString content_5 = new SpannableString(getResources().getString(R.string.apps));
        content_5.setSpan(new UnderlineSpan(), 0, content_5.length(), 0);
        text_apps.setText(content_5);
    }


    private void settingSpannable() {
        TextView textweblink = findViewById(R.id.textweblink);
        SpannableString ssPhone = new SpannableString(getResources().getString(R.string.website_link));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = "http://rkcyber.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        };
        ssPhone.setSpan(clickableSpan, 11, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textweblink.setText(ssPhone);
        textweblink.setMovementMethod(LinkMovementMethod.getInstance());
        textweblink.setBackgroundColor(getResources().getColor(R.color.transparent));
        textweblink.setHighlightColor(getResources().getColor(R.color.transparent));

    }
}
