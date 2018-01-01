package apextechies.gkquiz_hm.model;

import java.util.ArrayList;

/**
 * Created by Shankar on 12/28/2017.
 */

public class SubCategoryModel {

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<SubCatListModel> getData() {
        return data;
    }

    private String status,msg;
    ArrayList<SubCatListModel> data;


}
