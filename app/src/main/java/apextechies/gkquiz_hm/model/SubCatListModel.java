package apextechies.gkquiz_hm.model;

import java.util.ArrayList;

/**
 * Created by Shankar on 1/1/2018.
 */

public class SubCatListModel {
    public String getSubcat_id() {
        return subcat_id;
    }

    public String getSubCatNme() {
        return subCatNme;
    }

    public String getCatId() {
        return catId;
    }

    public ArrayList<QuesAnsModel> getAuesand() {
        return auesand;
    }

    private String subcat_id,subCatNme,catId;
    private ArrayList<QuesAnsModel> auesand;
}
