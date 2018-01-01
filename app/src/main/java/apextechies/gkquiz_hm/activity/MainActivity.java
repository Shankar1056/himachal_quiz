package apextechies.gkquiz_hm.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.adapter.CategoryAdapter;
import apextechies.gkquiz_hm.fragment.MainFragment;
import apextechies.gkquiz_hm.interfaces.ClickPosition;
import apextechies.gkquiz_hm.interfaces.OnTaskCompleted;
import apextechies.gkquiz_hm.model.CategoryModel;
import apextechies.gkquiz_hm.utilz.Download_web;
import apextechies.gkquiz_hm.utilz.Utility;
import apextechies.gkquiz_hm.utilz.WebServices;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity/* implements SearchView.OnQueryTextListener*/{
    private RecyclerView rv_category;
    private ArrayList<CategoryModel> list = new ArrayList<>();
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
       initWidgit();

        callFirstFrament();
        //callApi();

    }

    private void initWidgit() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Himachal Quiz");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        rv_category = (RecyclerView) findViewById(R.id.rv_category);
        rv_category.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));

       /* InputFilter[] editFilters = searchField.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        searchField.setFilters(newFilters);*/

    }

    private void callFirstFrament() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        MainFragment mf = new MainFragment();
        ft.replace(R.id.listFragment, mf);
        ft.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        //	searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);


        return true;
    }

    void filter(String text){
        if (text.length()>0) {
            String upperString = text.substring(0, 1).toUpperCase() + text.substring(1);
            ArrayList<CategoryModel> temp = new ArrayList<>();
            for (CategoryModel d : list) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getCatName().contains(upperString)) {
                    temp.add(d);
                }
            }
            //update recyclerview
            adapter.updateList(temp);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filter(newText);
        return false;
    }*/
    private void callApi() {
        Utility.showDailog(MainActivity.this, getResources().getString(R.string.pleasewait));
        Download_web web = new Download_web(MainActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utility.closeDialog();
                if (response.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equals("true")) {
                            JSONArray array = object.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo = array.getJSONObject(i);
                                String cat_id = jo.optString("cat_id");
                                String catName = jo.optString("catName");
                                String catIcon = jo.optString("catIcon");
                                list.add(new CategoryModel(cat_id, catName, catIcon));
                            }
                             adapter = new CategoryAdapter(MainActivity.this, list, new ClickPosition() {
                                @Override
                                public void pos(int position) {
                                    startActivity(new Intent(MainActivity.this, CategoryList.class).putExtra("id", list.get(position).getCat_id()));
                                }

                                @Override
                                public void pos(int position, String name, int count) {

                                }
                            });
                            rv_category.setAdapter(adapter);
                        } else {
                            Toast.makeText(MainActivity.this, "" + object.optString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        web.setReqType("get");
        web.execute(WebServices.GETCATEGORY);
    }
  }