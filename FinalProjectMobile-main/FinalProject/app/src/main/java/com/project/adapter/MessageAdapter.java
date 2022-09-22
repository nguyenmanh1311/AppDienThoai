package com.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.activity.ChatActivity;
import com.project.activity.R;
import com.project.model.User;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private List<String> listId;
    private Context context;
    private TextView txtKey, txtUsername;

    public MessageAdapter(Context c, List<String> ids){
        this.context = c;
        this.listId = ids;
    }

    @Override
    public int getCount() {
        return listId.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.message_item, null);

        txtKey = view.findViewById(R.id.txtIdUser);
        txtUsername = view.findViewById(R.id.txtUsername);
        String userId = listId.get(i);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("/data/users/");
        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                txtUsername.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                txtUsername.setText("Error");
            }
        });
        txtKey.setText(userId);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("messKey", userId);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
