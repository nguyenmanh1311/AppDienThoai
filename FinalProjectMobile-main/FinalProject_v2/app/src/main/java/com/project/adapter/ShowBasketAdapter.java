package com.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.activity.R;
import com.project.activity.ShowBasketActivity;
import com.project.databaseDao.BasketDao;
import com.project.models.ItemProduct;
import com.project.models.Product;
import com.project.util.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SHOW PRODUCT IN BASKET
 */
public class ShowBasketAdapter extends BaseAdapter {

    private List<String> keys;
    private Map<String, ItemProduct> productMap;
    private Context context;
    private ImageView imageView;
    private TextView txtName, txtPrice;
    private EditText txtQuantity;
    private Button btnDelete;

    /**
     * INIT ADAPTER
     * @param context  APPLICATION CONTEXT
     * @param productMap MAP OF PRODUCT IN BASKET
     */
    public ShowBasketAdapter(Context context, Map<String, ItemProduct> productMap){
        this.context = context;
        this.productMap = productMap;
        this.keys = new ArrayList<>();
        for(Map.Entry<String, ItemProduct> ele : productMap.entrySet()){
            keys.add(ele.getKey());
        }
    }
    @Override
    public int getCount() {
        return keys.size();
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
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.basket_item, null);

        Product product = productMap.get(getItem(pos)).getProduct();
        int quantity = productMap.get(getItem(pos)).getQuantity();
        imageView = view.findViewById(R.id.imageBasketItem);
        txtName = view.findViewById(R.id.txtName);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        btnDelete = view.findViewById(R.id.btnDeleteItem);

        // load image into imageView
        Picasso.get().load(product.getProductImageUrl()).into(imageView);
        txtName.setText(product.getProductName());
        txtPrice.setText("Price: " + String.valueOf(product.getProductPrice()));
        txtQuantity.setText(String.valueOf(quantity));

        BasketDao basketDao = new BasketDao();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Const.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String basketKey = sharedPreferences.getString(Const.BASKET_KEY, "");
        String itemKey = keys.get(pos);

        // delete product out of basket
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basketDao.deleteItemOnBasket(basketKey, itemKey);
                Intent intent = new Intent(context, ShowBasketActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        // update quantity of product
        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int quantity = 0;
                try{
                    quantity = Integer.parseInt(editable.toString());
                }catch(NumberFormatException exception){
                    System.out.println(exception.getMessage());
                }

                ItemProduct  item = productMap.get(getItem(pos));
                item.setQuantity(quantity);
                item.updateTotal();

                basketDao.updateQuantityItemOnBasket(basketKey, itemKey, item);
                Toast.makeText(context, "Update quantity of item", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}