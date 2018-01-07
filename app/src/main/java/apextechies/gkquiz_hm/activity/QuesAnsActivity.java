package apextechies.gkquiz_hm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.model.QuesAnsModel;

/**
 * Created by Shankar on 12/29/2017.
 */

public class QuesAnsActivity extends AppCompatActivity {

    private ArrayList<QuesAnsModel> queList = new ArrayList<>();
    ;
    private int queListCount = 0;
    private TextView question = null;
    private TextView answer1, answer2, answer3, answer4;
    private LinearLayout optionsLayout;
    private TextView finish = null, opt1, opt2, opt3, opt4;
    private TextView quesCount;
    private ImageView share;
    private int quesIndex = 0;
    private int selected[];
    private int correctAns[] = null;
    boolean review = false;
    Button prev, next = null;

    int select = -1, questionCount = 1;
    public int i = 0;
    boolean status = true;
    private String allqodata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quesans_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        initToolbar();
        initWidgit();
    }

    private void initWidgit() {
        question = (TextView) findViewById(R.id.question);
        answer1 = (TextView) findViewById(R.id.answer1);
        answer2 = (TextView) findViewById(R.id.answer2);
        answer3 = (TextView) findViewById(R.id.answer3);
        answer4 = (TextView) findViewById(R.id.answer4);

        quesCount = (TextView) findViewById(R.id.quesCount);

        optionsLayout = (LinearLayout) findViewById(R.id.answers);
        prev = (Button) findViewById(R.id.Prev);
        prev.setOnClickListener(prevListener);
        next = (Button) findViewById(R.id.Next);
        next.setOnClickListener(nextListener);
        finish = (TextView) findViewById(R.id.finish);
        opt1 = (TextView) findViewById(R.id.opt1);
        opt2 = (TextView) findViewById(R.id.opt2);
        opt3 = (TextView) findViewById(R.id.opt3);
        opt4 = (TextView) findViewById(R.id.opt4);
        share = (ImageView)findViewById(R.id.share);

        queList = getIntent().getParcelableArrayListExtra("list");
        Log.i("queList", queList.toString());

        queListCount = queList.size();
        selected = new int[queListCount];
        Arrays.fill(selected, -1);
        correctAns = new int[queListCount];
        Arrays.fill(correctAns, -1);
        this.showQuestion(0, review);
        findViewById(R.id.option1).setOnClickListener(option1);
        findViewById(R.id.option2).setOnClickListener(option2);
        findViewById(R.id.option3).setOnClickListener(option3);
        findViewById(R.id.option4).setOnClickListener(option4);
        finish.setOnClickListener(finishListener);


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alldata =((queList.get(i).getQuestion()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"","").replace("<p>","").replace("</p>","\n").replace("</span>", "</font>"));
                String ans1 = (queList.get(i).getOptionFirst()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"","").replace("<p>","").replace("</p>","\n").replace("</span>", "</font>");
                String ans2 = (queList.get(i).getOptionSecond()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"","").replace("<p>","").replace("</p>","\n").replace("</span>", "</font>");
                String ans3 = (queList.get(i).getOptionThird()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"","").replace("<p>","").replace("</p>","\n").replace("</span>", "</font>");
                String ans4 = (queList.get(i).getOptionFourth()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"","").replace("<p>","").replace("</p>","\n").replace("</span>", "</font>");

                allqodata= ( Html.fromHtml(alldata) +"\n \n"+Html.fromHtml(ans1)+"\n \n"+Html.fromHtml(ans2)+"\n \n"+Html.fromHtml(ans3)+"\n \n"+Html.fromHtml(ans4)+"\n \n"+
                        " Install 'Himachal Quiz' app from google play store or click on this link"+"\n"+"https://play.google.com/store/apps/details?id=apextechies.gkquiz_hm&hl=en");


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "IAS UCC");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, allqodata);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void showQuestion(int qIndex, boolean review) {

        try {
            ArrayList<String> options = new ArrayList<String>();
            if (qIndex == 0) {
                prev.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);

            } else if ((quesIndex + 1) == queListCount) {
                next.setVisibility(View.GONE);
                prev.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.VISIBLE);
                prev.setVisibility(View.VISIBLE);
            }

            quesCount.setText(getResources().getString(R.string.questions) + " " + (qIndex + 1) + "/" + queListCount);
            options.add(queList.get(i).getOptionFirst());
            options.add(queList.get(i).getOptionSecond());
            options.add(queList.get(i).getOptionThird());
            options.add(queList.get(i).getOptionFourth());

            if (correctAns[qIndex] == -1) {
                // String correctAnsStr = queList.get(i).getAnswer();
                String answer = queList.get(qIndex).getAnswer();
                int c = 1;
                for (String corr : options) {
                    if (corr.equalsIgnoreCase(answer) && c < 5) {
                        correctAns[qIndex] = c;
                    }
                    c++;
                }

            }
            String alldata = ((queList.get(i).getQuestion()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("<br />\r\n", "").replace("span style=\"background-color:", "font color=").replace("\"", "").replace("<p>", "").replace("</p>", "\n").replace("</span>", "</font>"));
            question.setText(Html.fromHtml(alldata));
            answer1.setText(Html.fromHtml((queList.get(i).getOptionFirst()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"", "").replace("<p>", "").replace("</p>", "\n").replace("</span>", "</font>")));
            answer2.setText(Html.fromHtml((queList.get(i).getOptionSecond()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"", "").replace("<p>", "").replace("</p>", "\n").replace("</span>", "</font>")));
            answer3.setText(Html.fromHtml((queList.get(i).getOptionThird()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"", "").replace("<p>", "").replace("</p>", "\n").replace("</span>", "</font>")));
            answer4.setText(Html.fromHtml((queList.get(i).getOptionFourth()).replace("\\n", "\n").replace("span style=\"color:", "font color=").replace("span style=\"background-color:", "font color=").replace("\"", "").replace("<p>", "").replace("</p>", "\n").replace("</span>", "</font>")));
            allqodata = (question.getText().toString() + "\n" + answer1.getText().toString() + "\n" + answer2.getText().toString() + "\n" + answer3.getText().toString() + "\n" + answer4.getText().toString() + "\n \n" +
                    " Install 'IAS UCC' app from google play store or click on this link" + "\n" + "https://play.google.com/store/apps/details?id=com.ucc.application&hl=en");


            setScoreTitle();
            if ((quesIndex + 1) == (queListCount)) {
                next.setClickable(false);
                next.setFocusable(false);
            }


            if (quesIndex == 0) {
                prev.setEnabled(false);
            }
            if (quesIndex > 0) {
                prev.setEnabled(true);
            }
            if ((quesIndex + 1) < (queListCount)) {

                next.setClickable(true);
                next.setFocusable(true);
            }

        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e.getCause());
        }
    }

    private View.OnClickListener finishListener = new View.OnClickListener() {
        public void onClick(View v) {

            Intent results = new Intent(QuesAnsActivity.this, Results.class);


            //Calculate Score
            int score = 0, notAnswered = 0;
            for (int i = 0; i < correctAns.length; i++) {
                if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
                    score++;
            }
            for (int i = 0; i < selected.length; i++) {
                if ((selected[i] == -1))
                    notAnswered++;
            }
            results.putExtra("category_id", String.valueOf(queList.get(0).getQues_id()));


            results.putExtra("no_of_correct", String.valueOf(score));
            results.putExtra("number_of_questions", String.valueOf(queListCount));
            results.putExtra("no_of_wrong", String.valueOf((queListCount - score - notAnswered)));
            results.putExtra("not_answered", String.valueOf(notAnswered));

            startActivity(results);
            finish();

        }
    };


    private View.OnClickListener nextListener = new View.OnClickListener() {
        public void onClick(View v) {

            quesIndex++;
            questionCount++;
            Log.i("total  count = ", String.valueOf(quesIndex));
            i++;
            if (selected[quesIndex] == -1) {
                status = true;
                optionsLayout.setClickable(true);
                optionsLayout.setFocusable(true);
                optionsLayout.setEnabled(true);
                for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                    optionsLayout.getChildAt(i).setClickable(true);
                    optionsLayout.getChildAt(i).setFocusable(true);

                }
            } else
                status = false;

            opt1.setBackgroundResource(R.drawable.rounded_circle);
            answer1.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt1.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt2.setBackgroundResource(R.drawable.rounded_circle);
            answer2.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt2.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt3.setBackgroundResource(R.drawable.rounded_circle);
            answer3.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt3.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt4.setBackgroundResource(R.drawable.rounded_circle);
            answer4.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt4.setTextColor(getResources().getColor(R.color.questionTextDefault));

            if (quesIndex >= queListCount + 1)
                quesIndex = queListCount;
            showQuestion(quesIndex, review);
            if (selected[quesIndex] != -1) {
                if (selected[quesIndex] == 1) {
                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt1.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt1.setTextColor(Color.WHITE);


                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);

                            opt1.setTextColor(Color.WHITE);

                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);


                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt1.setBackgroundResource(R.drawable.correct_answer_background);
                        opt1.setTextColor(Color.WHITE);

                    }

                }


                if (selected[quesIndex] == 2) {


                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt2.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt2.setTextColor(Color.WHITE);
                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);
                            opt1.setTextColor(Color.WHITE);

                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt2.setBackgroundResource(R.drawable.correct_answer_background);
                        opt2.setTextColor(Color.WHITE);
                    }

                }

                if (selected[quesIndex] == 3) {
                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt3.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt3.setTextColor(Color.WHITE);

                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);
                            opt1.setTextColor(Color.WHITE);


                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);

                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt3.setBackgroundResource(R.drawable.correct_answer_background);
                        opt3.setTextColor(Color.WHITE);

                    }

                }


                if (selected[quesIndex] == 4) {
                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt4.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt4.setTextColor(Color.WHITE);
                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);
                            opt1.setTextColor(Color.WHITE);


                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt4.setBackgroundResource(R.drawable.correct_answer_background);
                        opt4.setTextColor(Color.WHITE);
                    }

                }

            }

        }
    };


    private View.OnClickListener prevListener = new View.OnClickListener() {

        public void onClick(View v) {

            quesIndex--;
            questionCount--;
            i--;
            if (selected[quesIndex] == -1) {
                status = true;
                optionsLayout.setClickable(true);
                optionsLayout.setFocusable(true);
                optionsLayout.setEnabled(true);
                for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                    optionsLayout.getChildAt(i).setClickable(true);
                    optionsLayout.getChildAt(i).setFocusable(true);

                }
            } else
                status = false;

            opt1.setBackgroundResource(R.drawable.rounded_circle);
            opt1.setTextColor(getResources().getColor(R.color.questionTextDefault));
            answer1.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt2.setBackgroundResource(R.drawable.rounded_circle);
            answer2.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt2.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt3.setBackgroundResource(R.drawable.rounded_circle);
            answer3.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt3.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt4.setBackgroundResource(R.drawable.rounded_circle);
            answer4.setTextColor(getResources().getColor(R.color.questionTextDefault));
            opt4.setTextColor(getResources().getColor(R.color.questionTextDefault));

            if (quesIndex < 0)
                quesIndex = 0;


            showQuestion(quesIndex, review);
            if (selected[quesIndex] != -1) {
                if (selected[quesIndex] == 1) {
                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt1.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt1.setTextColor(Color.WHITE);

                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);
                            opt1.setTextColor(Color.WHITE);


                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt1.setBackgroundResource(R.drawable.correct_answer_background);
                        opt1.setTextColor(Color.WHITE);

                    }
                }


                if (selected[quesIndex] == 2) {


                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt2.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt2.setTextColor(Color.WHITE);
                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);
                            opt1.setTextColor(Color.WHITE);


                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt2.setBackgroundResource(R.drawable.correct_answer_background);
                        opt2.setTextColor(Color.WHITE);
                    }
                }

                if (selected[quesIndex] == 3) {
                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt3.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt3.setTextColor(Color.WHITE);

                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);

                            opt1.setTextColor(Color.WHITE);

                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt3.setBackgroundResource(R.drawable.correct_answer_background);
                        answer3.setTextColor(Color.WHITE);
                    }
                }


                if (selected[quesIndex] == 4) {
                    if (selected[quesIndex] != correctAns[quesIndex]) {


                        opt4.setBackgroundResource(R.drawable.wrong_answer_background);
                        opt4.setTextColor(Color.WHITE);
                        if ((correctAns[quesIndex] == 1)) {
                            opt1.setBackgroundResource(R.drawable.correct_answer_background);

                            opt1.setTextColor(Color.WHITE);

                        }
                        if ((correctAns[quesIndex] == 2)) {
                            opt2.setBackgroundResource(R.drawable.correct_answer_background);
                            opt2.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 3)) {
                            opt3.setBackgroundResource(R.drawable.correct_answer_background);
                            opt3.setTextColor(Color.WHITE);
                        }
                        if ((correctAns[quesIndex] == 4)) {
                            opt4.setBackgroundResource(R.drawable.correct_answer_background);
                            opt4.setTextColor(Color.WHITE);
                        }
                    }
                    if (selected[quesIndex] == correctAns[quesIndex]) {


                        opt4.setBackgroundResource(R.drawable.correct_answer_background);
                        opt4.setTextColor(Color.WHITE);
                    }
                }


            }
            //   setAnswer(quesIndex-1);
        }
    };


    private void setScoreTitle() {
        this.setTitle(R.string.SciQuiz3 + "     " + (quesIndex + 1) + "/" + queListCount);
    }


    private View.OnClickListener option1 = new View.OnClickListener() {

        public void onClick(View v) {

            if (status == false) {

                optionsLayout.setClickable(false);
                optionsLayout.setFocusable(false);
                optionsLayout.setEnabled(false);


                for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                    optionsLayout.getChildAt(i).setClickable(false);
                    optionsLayout.getChildAt(i).setFocusable(false);

                }
                // answers.clearCheck();


            } else {


                selected[quesIndex] = 1;

                status = false;

                if (select == correctAns[quesIndex]) {

                    opt1.setBackgroundResource(R.drawable.correct_answer_background);
                    opt1.setTextColor(Color.WHITE);


                }
                if (select != correctAns[quesIndex]) {


                    opt1.setBackgroundResource(R.drawable.wrong_answer_background);
                    opt1.setTextColor(Color.WHITE);

                    if ((correctAns[quesIndex] == 1)) {
                        opt1.setBackgroundResource(R.drawable.correct_answer_background);
                        opt1.setTextColor(Color.WHITE);


                    }
                    if ((correctAns[quesIndex] == 2)) {
                        opt2.setBackgroundResource(R.drawable.correct_answer_background);
                        opt2.setTextColor(Color.WHITE);
                    }
                    if ((correctAns[quesIndex] == 3)) {
                        opt3.setBackgroundResource(R.drawable.correct_answer_background);
                        opt3.setTextColor(Color.WHITE);
                    }
                    if ((correctAns[quesIndex] == 4)) {
                        opt4.setBackgroundResource(R.drawable.correct_answer_background);
                        opt4.setTextColor(Color.WHITE);
                    }


                }

            }
        }

    };
    private View.OnClickListener option2 = new View.OnClickListener() {

        public void onClick(View v) {
            if (status == false) {

                optionsLayout.setClickable(false);
                optionsLayout.setFocusable(false);
                optionsLayout.setEnabled(false);


                for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                    optionsLayout.getChildAt(i).setClickable(false);
                    optionsLayout.getChildAt(i).setFocusable(false);

                }
                // answers.clearCheck();


            } else {
                selected[quesIndex] = 2;


                status = false;
                if (select == correctAns[quesIndex]) {
                    opt2.setBackgroundResource(R.drawable.correct_answer_background);
                    opt2.setTextColor(Color.WHITE);

                }
                if (select != correctAns[quesIndex]) {

                    opt2.setBackgroundResource(R.drawable.wrong_answer_background);
                    opt2.setTextColor(Color.WHITE);

                    if ((correctAns[quesIndex] == 1)) {
                        opt1.setBackgroundResource(R.drawable.correct_answer_background);
                        opt1.setTextColor(Color.WHITE);


                    }
                    if ((correctAns[quesIndex] == 2)) {
                        opt2.setBackgroundResource(R.drawable.correct_answer_background);
                        opt2.setTextColor(Color.WHITE);
                    }
                    if ((correctAns[quesIndex] == 3)) {
                        opt3.setBackgroundResource(R.drawable.correct_answer_background);
                        opt3.setTextColor(Color.WHITE);
                    }
                    if ((correctAns[quesIndex] == 4)) {
                        opt4.setBackgroundResource(R.drawable.correct_answer_background);
                        opt4.setTextColor(Color.WHITE);
                    }


                }
            }

        }
    };
    private View.OnClickListener option3 = new View.OnClickListener() {

        public void onClick(View v) {
            if (status == false) {

                optionsLayout.setClickable(false);
                optionsLayout.setFocusable(false);
                optionsLayout.setEnabled(false);


                for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                    optionsLayout.getChildAt(i).setClickable(false);
                    optionsLayout.getChildAt(i).setFocusable(false);

                }
                // answers.clearCheck();


            } else {
                selected[quesIndex] = 3;

                status = false;
                if (select == correctAns[quesIndex]) {

                    opt3.setBackgroundResource(R.drawable.correct_answer_background);
                    opt3.setTextColor(Color.WHITE);

                }
                if (select != correctAns[quesIndex]) {


                    opt3.setBackgroundResource(R.drawable.wrong_answer_background);
                    opt3.setTextColor(Color.WHITE);

                    if ((correctAns[quesIndex] == 1)) {
                        opt1.setBackgroundResource(R.drawable.correct_answer_background);

                        opt1.setTextColor(Color.WHITE);

                    }
                    if ((correctAns[quesIndex] == 2)) {
                        opt2.setBackgroundResource(R.drawable.correct_answer_background);
                        opt2.setTextColor(Color.WHITE);
                    }
                    if ((correctAns[quesIndex] == 3)) {
                        opt3.setBackgroundResource(R.drawable.correct_answer_background);
                        opt3.setTextColor(Color.WHITE);
                    }
                    if ((correctAns[quesIndex] == 4)) {
                        opt4.setBackgroundResource(R.drawable.correct_answer_background);
                        opt4.setTextColor(Color.WHITE);
                    }
                }
            }
        }
    };
    private View.OnClickListener option4 = new View.OnClickListener() {

        public void onClick(View v) {
            if (status == false) {

                optionsLayout.setClickable(false);
                optionsLayout.setFocusable(false);
                optionsLayout.setEnabled(false);


                for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                    optionsLayout.getChildAt(i).setClickable(false);
                    optionsLayout.getChildAt(i).setFocusable(false);

                }
                // answers.clearCheck();


            } else {


                selected[quesIndex] = 4;


                status = false;
                if (select == correctAns[quesIndex]) {

                    opt4.setBackgroundResource(R.drawable.correct_answer_background);
                    opt4.setTextColor(Color.WHITE);

                }
                if (select != correctAns[quesIndex]) {

                    opt4.setBackgroundResource(R.drawable.wrong_answer_background);
                    opt4.setTextColor(Color.WHITE);
                    if ((correctAns[quesIndex] == 1)) {
                        opt1.setBackgroundResource(R.drawable.correct_answer_background);
                        opt1.setTextColor(Color.WHITE);


                    }
                    if ((correctAns[quesIndex] == 2)) {
                        opt2.setBackgroundResource(R.drawable.correct_answer_background);

                        opt2.setTextColor(Color.WHITE);

                    }
                    if ((correctAns[quesIndex] == 3)) {
                        opt3.setBackgroundResource(R.drawable.correct_answer_background);
                        opt3.setTextColor(Color.WHITE);
                    }
                    if ((correctAns[quesIndex] == 4)) {
                        opt4.setBackgroundResource(R.drawable.correct_answer_background);
                        opt4.setTextColor(Color.WHITE);
                    }
                }
            }
        }
    };
}