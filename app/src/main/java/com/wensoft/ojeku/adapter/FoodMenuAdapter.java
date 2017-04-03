package com.wensoft.ojeku.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.pojo.FoodMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farhan on 3/9/17.
 */

public class FoodMenuAdapter extends ArrayAdapter<FoodMenu> {

    public ArrayList<FoodMenu> menuList;
    private Activity activity;

    public FoodMenuAdapter(Context context, int textViewResourceId, ArrayList<FoodMenu> countryList) {
        super(context, textViewResourceId, countryList);
        this.menuList = new ArrayList<FoodMenu>();
        this.menuList.addAll(countryList);
    }

    private class ViewHolder {
        TextView code,price;
        LinearLayout header;
        CheckBox name;
        ImageView iv;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menuList.size();
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return menuList.indexOf(getItem(pos));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));
        FoodMenu menu = menuList.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.adapter_food_menu, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.text);
            holder.price = (TextView) convertView.findViewById(R.id.textPrice);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.iv = (ImageView) convertView.findViewById(R.id.ivorder);
            convertView.setTag(holder);

            holder.code.setTextSize(15);
            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    FoodMenu menu = (FoodMenu) cb.getTag();
                    menu.setSelected(cb.isChecked());
                }
            });
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.code.setText(menu.getNm());
        holder.price.setText("Rp. "+menu.getPrice()+",-");
        holder.name.setChecked(menu.isSelected());
        holder.name.setTag(menu);

        return convertView;

    }

}

