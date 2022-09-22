package com.project.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.adapter.MessageAdapter;
import com.project.model.Chat;
import com.project.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {

    private ListView listMessage;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public MessageFragment() {
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("/chats/");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_message, container, false);

        listMessage = view.findViewById(R.id.listView);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> idUser =  new ArrayList<>();
                    for(DataSnapshot d:snapshot.getChildren()){
                        idUser.add(d.getKey());
                    }

                    MessageAdapter adapter = new MessageAdapter(getContext(), idUser);
                    listMessage.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}