package apextechies.gkquiz_hm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apextechies.gkquiz_hm.R;
import apextechies.gkquiz_hm.interfaces.ClickPosition;
import apextechies.gkquiz_hm.model.CategoryModel;

/**
 * Created by Shankar on 12/27/2017.
 */

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CategoryModel> imageList;
    private ClickPosition clickPosition;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> imageList, ClickPosition clickPosition) {
        this.context = context;
        this.imageList = imageList;
        this.clickPosition = clickPosition;
    }
    public void updateList(ArrayList<CategoryModel> temp) {
        imageList = temp;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        Picasso.with(context).load(imageList.get(position).getCatIcon()).into(holder.cat_image);
        holder.cat_text.setText(imageList.get(position).getCatName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition.pos(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cat_text;
        private ImageView cat_image;

        public ViewHolder(View itemView) {
            super(itemView);
            cat_text = (TextView)itemView.findViewById(R.id.cat_text);
            cat_image = (ImageView) itemView.findViewById(R.id.cat_image);

        }
    }
}
