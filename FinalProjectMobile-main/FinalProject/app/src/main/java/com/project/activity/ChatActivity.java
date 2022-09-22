package com.project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.adapter.ChatAdapter;
import com.project.model.Chat;

import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ImageView sendImage;
    EditText message;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    ListView list_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //get intent

        Intent intent = getIntent();
        String messKey = intent.getStringExtra("messKey");

        if(messKey == null){
            Toast.makeText(this, "Have some error", Toast.LENGTH_SHORT).show();
            messKey = "";
        }

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("/chats/"+messKey);

        list_message = findViewById(R.id.list_message);
        sendImage = findViewById(R.id.send_image);
        message = findViewById(R.id.user_message);

        displayChatMessage();
        sendImage.bringToFront();
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message.getText() != null){
                    if(!message.getText().toString().equals("")){
                        dbRef.push().setValue(new Chat("Admin", message.getText().toString(), System.currentTimeMillis()));
                        message.setText("");
                        displayChatMessage();
                    }
                }
            }
        });

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                displayChatMessage();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayChatMessage(){
       dbRef.limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String, Chat>> indicator = new GenericTypeIndicator<Map<String, Chat>>() {};
                Map<String, Chat> result = snapshot.getValue(indicator);
                if(result != null){
                    ChatAdapter adapter =  new ChatAdapter(getApplicationContext(), result);
                    list_message.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}