package apextechies.gkquiz_hm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.activity.CategoryList;
import apextechies.gkquiz_hm.adapter.CategoryAdapter;
import apextechies.gkquiz_hm.interfaces.ClickPosition;
import apextechies.gkquiz_hm.interfaces.OnTaskCompleted;
import apextechies.gkquiz_hm.model.CategoryModel;
import apextechies.gkquiz_hm.utilz.Download_web;
import apextechies.gkquiz_hm.utilz.Utility;
import apextechies.gkquiz_hm.utilz.WebServices;

/**
 * Created by Shankar on 12/24/2017.
 */

public class MainFragment extends android.app.Fragment {
    private RecyclerView rv_category;
    private ArrayList<CategoryModel> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidgit(view);
        if (Utility.isNetworkAvailable(getActivity())) {
            callApi();
        }
        else
        {
            Toast.makeText(getActivity(), ""+getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }
    }

    private void callApi() {
        Utility.showDailog(getActivity(), getResources().getString(R.string.pleasewait));
        Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
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
                            CategoryAdapter adapter = new CategoryAdapter(getActivity(), list, new ClickPosition() {
                                @Override
                                public void pos(int position) {
                                    startActivity(new Intent(getActivity(), CategoryList.class).putExtra("id", list.get(position).getCat_id()).
                                            putExtra("name", list.get(position).getCatName()));
                                }

                                @Override
                                public void pos(int position, String name, int count) {

                                }
                            });
                            rv_category.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "" + object.optString("msg"), Toast.LENGTH_SHORT).show();
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

    private void initWidgit(View view) {
        rv_category = (RecyclerView) view.findViewById(R.id.rv_category);
        rv_category.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }
}
