package apextechies.gkquiz_hm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.adapter.CateoryListAdapter;
import apextechies.gkquiz_hm.interfaces.ClickPosition;
import apextechies.gkquiz_hm.interfaces.OnTaskCompleted;
import apextechies.gkquiz_hm.model.QuesAnsModel;
import apextechies.gkquiz_hm.model.SubCatListModel;
import apextechies.gkquiz_hm.model.SubCategoryModel;
import apextechies.gkquiz_hm.utilz.Download_web;
import apextechies.gkquiz_hm.utilz.Utility;
import apextechies.gkquiz_hm.utilz.WebServices;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Shankar on 12/28/2017.
 */

public class CategoryList extends AppCompatActivity {

    private String catid;
    private RecyclerView rv_list;
    private ArrayList<SubCatListModel> list = new ArrayList<>();
    private ArrayList<QuesAnsModel> qnlist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callist);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        initWidgit();
        if (Utility.isNetworkAvailable(CategoryList.this)) {
            callCatApi();
        } else {
            Toast.makeText(CategoryList.this, "" + getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void callCatApi() {
        Utility.showDailog(CategoryList.this, getResources().getString(R.string.pleasewait));
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        Download_web web = new Download_web(CategoryList.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {

                Utility.closeDialog();
                if (response.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(response);
                        Gson gson = new Gson();
                        final SubCategoryModel subCategoryModel = gson.fromJson(response, SubCategoryModel.class);

                        if (subCategoryModel.getStatus().equals("true")) {

                            list = subCategoryModel.getData();
                            CateoryListAdapter adapter = new CateoryListAdapter(CategoryList.this, list, new ClickPosition() {
                                @Override
                                public void pos(int position) {
                                    if (subCategoryModel.getData().get(position).getAuesand().size() > 0) {
                                        qnlist = subCategoryModel.getData().get(position).getAuesand();
                                        startActivity(new Intent(CategoryList.this, QuesAnsActivity.class).putParcelableArrayListExtra("list", qnlist).
                                                putExtra("name", list.get(position).getSubCatNme()));
                                    } else {
                                        Toast.makeText(CategoryList.this, "No Questions found", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void pos(int position, String name, int count) {

                                }
                            });
                            rv_list.setAdapter(adapter);
                        } else {
                            Toast.makeText(CategoryList.this, "" + object.opt("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("id", catid));
        web.setData(nameValuePairs);
        web.setReqType("post");
        web.execute(WebServices.SUBCATEGORY);

    }

    private void initWidgit() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));

        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(CategoryList.this));
        catid = getIntent().getStringExtra("id");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
