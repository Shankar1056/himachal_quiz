package apextechies.gkquiz_hm.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.common.ClsGeneral;

/**
 * Created by igcs-27 on 22/1/16.
 */
public class Results extends AppCompatActivity {
    TextView score, no_of_correct, no_of_wrong, not_answered, tryAgain, home;
    String correct, wrong, notAnswered, totalQuestions, category_id;
    Toolbar toolbar;
    private TextView actionbar_title,  total_candidates, best_score, rank;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        email = ClsGeneral.getPreferences(Results.this, "email");

        score = (TextView) findViewById(R.id.score);
        no_of_correct = (TextView) findViewById(R.id.no_of_correct);
        no_of_wrong = (TextView) findViewById(R.id.no_of_wrong);
        not_answered = (TextView) findViewById(R.id.not_answered);
        tryAgain = (TextView) findViewById(R.id.tryAgain);
        home = (TextView) findViewById(R.id.home);
//        actionbar_title = (TextView) findViewById(R.id.actionbar_title);
//        questionDate = (TextView) findViewById(R.id.questionDate);

        total_candidates = (TextView) findViewById(R.id.total_candidates);
        best_score = (TextView) findViewById(R.id.best_score);
        rank = (TextView) findViewById(R.id.rank);


        //actionbar_title.setText(R.string.results);

        correct = getIntent().getStringExtra("no_of_correct");
        totalQuestions = getIntent().getStringExtra("number_of_questions");
        notAnswered = getIntent().getStringExtra("not_answered");
        wrong = getIntent().getStringExtra("no_of_wrong");

        score.setText(correct + " /" + totalQuestions);
        no_of_correct.setText(" "+correct);
        no_of_wrong.setText(" "+wrong);
        not_answered.setText(" "+notAnswered);

        category_id = getIntent().getStringExtra("category_id");

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent home = new Intent(Results.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });
    }

}
