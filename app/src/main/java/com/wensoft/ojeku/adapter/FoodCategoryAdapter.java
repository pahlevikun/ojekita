package com.wensoft.ojeku.adapter;

/**
 * Created by farhan on 4/3/17.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.pojo.FoodCategory;

import java.util.List;

import static com.wensoft.ojeku.singleton.AppController.context;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodCategoryAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<FoodCategory> categoryItems;

    public FoodCategoryAdapter(Activity activity, List<FoodCategory> categoryItems) {
        this.activity = activity;
        this.categoryItems = categoryItems;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    @Override
    public Object getItem(int location) {
        return categoryItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_food_category, null);

        TextView title = (TextView) convertView.findViewById(R.id.tvJudul);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivGambarKategori);

        // getting movie data for the row
        FoodCategory m = categoryItems.get(position);
        title.setText(m.getName());
        if(m.getBanner().equals(null)) {
            Picasso.with(context).load(R.drawable.ic_collections_black_24dp).into(imageView);
        }else{
            String urlAvatar = "http://ojekita.com/banner/"+ m.getBanner();
            Picasso.with(context).load(urlAvatar).into(imageView);
        }

        return convertView;
    }
}