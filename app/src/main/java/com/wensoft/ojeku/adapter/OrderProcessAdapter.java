package com.wensoft.ojeku.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.pojo.FoodCategory;
import com.wensoft.ojeku.pojo.OrderComplete;

import java.util.List;

/**
 * Created by farhan on 3/9/17.
 */

public class OrderProcessAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<OrderComplete> categoryItems;

    public OrderProcessAdapter(Activity activity, List<OrderComplete> categoryItems) {
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
            convertView = inflater.inflate(R.layout.adapter_order_complete, null);

        TextView title = (TextView) convertView.findViewById(R.id.textComplete);
        TextView invoice = (TextView) convertView.findViewById(R.id.invoiceComplete);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivorderComplete);

        if (categoryItems.size()!=0){

        }else {

        }

        // getting movie data for the row
        OrderComplete m = categoryItems.get(position);
        String orderType = m.getOrderType();
        invoice.setText("Invoice : "+m.getIdOrder());
        title.setText(m.getAlamat());
        if(orderType.equals("1")){
            //title.setText("Kita Jemput");
            imageView.setImageResource(R.drawable.ic_motor);
        }else if (orderType.equals("2")){
            //title.setText("Mobil Kita");
            imageView.setImageResource(R.drawable.ic_motor);
        }else{
            //title.setText("Kita Antar");
            imageView.setImageResource(R.drawable.ic_motor);
        }

        return convertView;
    }
}
