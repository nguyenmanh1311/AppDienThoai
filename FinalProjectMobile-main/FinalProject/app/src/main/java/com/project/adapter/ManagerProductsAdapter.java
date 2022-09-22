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
import com.project.activity.UpdateProductActivity;
import com.project.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerProductsAdapter extends BaseAdapter {
    private Map<String, Product> object;
    private List<Product> products;
    private List<String> keys;
    private Context context;
    private ImageView imageView;
    private TextView txtName, txtPrice;


    public ManagerProductsAdapter(Context c, Map<String, Product> ob){
        this.context = c;
        this.object = ob;
        products =  new ArrayList<>();
        keys = new ArrayList<>();
        if(ob != null){
            for(Map.Entry<String, Product> ele:this.object.entrySet()){
                products.add(ele.getValue());
                keys.add(ele.getKey());
            }
        }


    }
    @Override
    public int getCount() {
        if(object == null){
            return 0;
        }
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
                Toast.makeText(view.getContext(), keys.get(position).toString(), Toast.LENGTH_SHORT).show();
                Product pro = products.get(position);
                Intent intent = new Intent(context, UpdateProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", keys.get(position));
                bundle.putString("link", pro.getProductImageUrl());
                bundle.putString("productName", pro.getProductName());
                bundle.putInt("productPrice", pro.getProductPrice());
                bundle.putString("productDescription", pro.getProductDescription());
                bundle.putString("productType", pro.getProductType());

                intent.putExtras(bundle);
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
