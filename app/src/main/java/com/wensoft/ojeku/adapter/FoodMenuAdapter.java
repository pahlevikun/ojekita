package com.wensoft.ojeku.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.main.fragments.handle_home.food_service.FoodMenuActivity;
import com.wensoft.ojeku.pojo.FoodCategory;
import com.wensoft.ojeku.pojo.FoodMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodMenuAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<FoodMenu> categoryItems;
    private FoodMenu m;

    public FoodMenuAdapter(Activity activity, List<FoodMenu> categoryItems) {
        this.activity = activity;
        this.categoryItems = categoryItems;
    }

    public class ViewHolder {
        private TextView jumlah,title,harga;
        private Button buttonMax, buttonMin;
    }
    public ViewHolder holder;

    public synchronized void refresAdapter(ArrayList<FoodMenu> items) {
        categoryItems.clear();
        categoryItems.addAll(items);
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        holder = null;
        m = categoryItems.get(position);

        if (convertView == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_food_menu, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.textMenu);
            holder.harga = (TextView) convertView.findViewById(R.id.textHarga);
            holder.jumlah = (TextView) convertView.findViewById(R.id.textJumlah);
            holder.buttonMin = (Button) convertView.findViewById(R.id.buttonMin);
            holder.buttonMax = (Button) convertView.findViewById(R.id.buttonMax);
            convertView.setTag(holder);

            holder.jumlah.setText(m.getSum());

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(m.getNm());
        holder.harga.setText("Rp. " + m.getPrice());

        holder.buttonMax.setTag(convertView);
        holder.buttonMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(categoryItems.get(position).getSum());
                n = n + 1;
                categoryItems.get(position).setSum(String.valueOf(n));
                holder.jumlah.setText(categoryItems.get(position).getSum());
            }
        });

        holder.buttonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m.getSum().equals("0")) {
                    notifyDataSetChanged();
                } else {
                    int n = Integer.parseInt(categoryItems.get(position).getSum());
                    n = n - 1;
                    categoryItems.get(position).setSum(String.valueOf(n));
                    notifyDataSetChanged();
                    holder.jumlah.setText(categoryItems.get(position).getSum());
                }
            }
        });

        // getting movie data for the row
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
