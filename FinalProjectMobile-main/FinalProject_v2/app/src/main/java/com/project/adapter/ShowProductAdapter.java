package com.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.activity.R;
import com.project.activity.ViewDetailsActivity;
import com.project.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowProductAdapter extends BaseAdapter {

    private Map<String, Product> object;
    private List<Product> products;
    private List<String> keys;
    private Context context;
    private ImageView imageView;
    private TextView txtView;
    private TextView txtName;
    private TextView txtPrice;


    /**
     * INIT ADAPTER
     * @param c APPLICATION CONTEXT
     * @param ob MAP OF ALL PRODUCT
     */
    public ShowProductAdapter(Context c, Map<String, Product> ob){
        this.context = c;
        this.object = ob;
        products =  new ArrayList<>();
        keys = new ArrayList<>();

        for(Map.Entry<String, Product> ele:this.object.entrySet()){
            products.add(ele.getValue());
            keys.add(ele.getKey());
        }

    }
    @Override
    public int getCount() {
        return object.size();
    }

    @Override
    public Object getItem(int i) {
        return keys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product pro = products.get(position);
                Intent intent = new Intent(context, ViewDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", keys.get(position));
                bundle.putString("link", pro.getProductImageUrl());
                bundle.putString("productName", pro.getProductName());
                bundle.putInt("productPrice", pro.getProductPrice());
                bundle.putString("productDescription", pro.getProductDescription());
                bundle.putString("productType", pro.getProductType());

                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        txtName = (TextView) view.findViewById(R.id.textName);
        txtPrice = (TextView) view.findViewById(R.id.textPrice);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        Product pr = products.get(position);
        txtName.setText(pr.getProductName());
        txtPrice.setText("Giá: " + String.valueOf(pr.getProductPrice()) + " Đ");
        Picasso.get().load(pr.getProductImageUrl()).into(imageView);

        return view;
    }

}
