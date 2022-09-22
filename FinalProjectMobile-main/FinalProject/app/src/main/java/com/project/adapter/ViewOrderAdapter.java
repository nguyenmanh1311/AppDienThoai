package com.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.activity.R;
import com.project.model.ItemProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewOrderAdapter extends BaseAdapter {

    private Map<String, ItemProduct> listProduct;
    private Context context;
    private List<ItemProduct> itemProductsList;
    private TextView txtProductName, txtQuantityAndPrice;

    public ViewOrderAdapter(Context context, Map<String, ItemProduct> map){
        this.listProduct = map;
        this.context = context;
        itemProductsList = new ArrayList<>();
        for (Map.Entry<String, ItemProduct> ele: map.entrySet()){
            itemProductsList.add(ele.getValue());
        }
    }
    @Override
    public int getCount() {
        return itemProductsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view  = inflater.inflate(R.layout.order_item, null);

        ItemProduct item = itemProductsList.get(pos);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtQuantityAndPrice = view.findViewById(R.id.txtQuantityAndPrice);

        String s1 = item.getProduct().getProductName() + " x"+ item.getQuantity();
        String s2 = item.getProduct().getProductPrice() + " x " + item.getQuantity() + " = " + item.getTotal();
        txtProductName.setText(s1);
        txtQuantityAndPrice.setText(s2);


        return view;
    }
}
