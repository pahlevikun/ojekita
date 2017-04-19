package com.wensoft.ojeku.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.pojo.FoodBanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.wensoft.ojeku.singleton.AppController.context;

public class FoodRestaurantsAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FoodBanner> categoryItems;
    private List<FoodBanner> filterItems;
    private ValueFilter valueFilter;
    public FoodRestaurantsAdapter(Activity activity, List<FoodBanner> categoryItems) {
        this.activity = activity;
        this.categoryItems = categoryItems;
        filterItems =  categoryItems;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
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

    public String getName(int position) {
        return categoryItems.get(position).getName();
    }

    public String getId(int position) {
        return categoryItems.get(position).getId();
    }

    public Double getLat(int position) {
        return categoryItems.get(position).getLatitude();
    }

    public Double getLng(int position) {
        return categoryItems.get(position).getLongitude();
    }

    public String getAlamat(int position) {
        return categoryItems.get(position).getAlamat();
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_food_restaurants, null);

        TextView title = (TextView) convertView.findViewById(R.id.tvRestaurants);
        title.setTextColor(context.getResources().getColor(R.color.colorText));

        // getting movie data for the row
        FoodBanner m = categoryItems.get(position);
        title.setText(m.getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {
            valueFilter=new ValueFilter();
        }
        notifyDataSetChanged();
        return valueFilter;
    }
    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<FoodBanner> filterList = new ArrayList<FoodBanner>();
                for (int i = 0; i < filterItems.size(); i++) {
                    if ((filterItems.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        FoodBanner contacts = new FoodBanner();
                        contacts.setName(filterItems.get(i).getName());
                        contacts.setId(filterItems.get(i).getId());
                        filterList.add(contacts);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filterItems.size();
                results.values = filterItems;
            }
            return results;
        }

        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categoryItems = (ArrayList<FoodBanner>) results.values;
            notifyDataSetChanged();
        }
    }

}
