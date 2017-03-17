package com.wensoft.ojeku.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.pojo.FoodCategory;

import java.util.List;

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

        TextView title = (TextView) convertView.findViewById(R.id.text);

        // getting movie data for the row
        FoodCategory m = categoryItems.get(position);
        title.setText(m.getNm());

        return convertView;
    }
}
