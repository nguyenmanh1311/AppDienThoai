package com.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.activity.R;
import com.project.activity.ViewOrderActivity;
import com.project.model.Order;
import com.project.util.Const;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowItemOrderAdapter extends BaseAdapter{

    private Map<String, Order> orderList;
    private List<String> keys;
    private Context context;

    public ShowItemOrderAdapter(Context context,Map<String, Order> map ){
        this.context = context;
        this.orderList = map;
        this.keys = new ArrayList<>();

        for(Map.Entry<String, Order> ele: this.orderList.entrySet()){
            this.keys.add(ele.getKey());
        }
    }

    @Override
    public int getCount() {
        return keys.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(keys.get(i));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.infor_order_item, null);

        Order order = (Order) getItem(i);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Forward to update status
                Intent intent = new Intent(context, ViewOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("orderKey", keys.get(i));
                intent.putExtra("basketKey", order.getBasketId());
                intent.putExtra("userKey", order.getUserId());
                intent.putExtra("status", order.getStatus() + "");
                intent.putExtra("total", order.getTotalMoney() + "");
                context.startActivity(intent);
            }
        });

        TextView txtKey, txtStatus;
        txtKey = view.findViewById(R.id.txtOrderKey);
        txtKey.setText(keys.get(i));

        txtStatus = view.findViewById(R.id.txtStatus);
        if(order.getStatus() == Const.PACKING){
            txtStatus.setText("Waiting for the package");
        }else if(order.getStatus() == Const.SHIPPING){
            txtStatus.setText("SHIPPING");
        }else if(order.getStatus() == Const.DONE) {
            txtStatus.setText("Done");
        }else{
            txtStatus.setText("Canceled");
        }

        return view;
    }
}
