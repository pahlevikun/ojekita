package com.wensoft.ojeku.adapter;

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
import com.wensoft.ojeku.pojo.FoodBanner;
import com.wensoft.ojeku.pojo.FoodCategory;

import java.util.List;

import static com.wensoft.ojeku.singleton.AppController.context;

public class FoodBannerAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<FoodBanner> categoryItems;

    public FoodBannerAdapter(Activity activity, List<FoodBanner> categoryItems) {
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
            convertView = inflater.inflate(R.layout.adapter_food_banner, null);

        ImageView title = (ImageView) convertView.findViewById(R.id.ivBanner);

        // getting movie data for the row
        FoodBanner m = categoryItems.get(position);
        String urlAvatar = "http://ojekita.com/banner/"+ m.getImage();
        Picasso.with(context).load(urlAvatar).into(title);

        return convertView;
    }
}
