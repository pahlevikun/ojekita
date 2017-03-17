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
import com.wensoft.ojeku.pojo.FoodCategoryList;

import java.util.List;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodCategoryListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<FoodCategoryList> categoryItems;

    public FoodCategoryListAdapter(Activity activity, List<FoodCategoryList> categoryItems) {
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
            convertView = inflater.inflate(R.layout.adapter_food_category_list, null);

        TextView title = (TextView) convertView.findViewById(R.id.text);
        TextView open = (TextView) convertView.findViewById(R.id.open);
        TextView close = (TextView) convertView.findViewById(R.id.close);

        // getting movie data for the row
        FoodCategoryList m = categoryItems.get(position);
        title.setText(m.getNm());
        open.setText(m.getOpen());
        close.setText(m.getClose());

        return convertView;
    }
}
