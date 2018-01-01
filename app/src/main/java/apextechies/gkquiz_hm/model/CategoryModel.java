package apextechies.gkquiz_hm.model;

/**
 * Created by Shankar on 12/27/2017.
 */

public class CategoryModel {

    private String cat_id,catName,catIcon;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatIcon() {
        return catIcon;
    }

    public void setCatIcon(String catIcon) {
        this.catIcon = catIcon;
    }

    public CategoryModel(String cat_id, String catName, String catIcon)
    {
        this.cat_id = cat_id;
        this.catName = catName;
        this.catIcon = catIcon;
    }
}
