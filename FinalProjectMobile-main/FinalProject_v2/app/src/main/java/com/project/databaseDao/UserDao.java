package com.project.databaseDao;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.activity.MainActivity;
import com.project.models.User;
import com.project.util.Const;

import java.util.Map;

public class UserDao {
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public UserDao(){
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("data/users/");
    }

    /**
     *  Login func
     * @param context
     * @param ref sharedPreferences to save data on phone.
     * @param email email of user
     * @param password password of user
     * @return id of user
     */
    public void login(Context context,SharedPreferences ref,String email, String password){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<Map<String,User>> genericTypeIndicator = new GenericTypeIndicator<Map<String, User>>() {};
                Map<String, User> userLists = snapshot.getValue(genericTypeIndicator);
                Boolean isSuccess = false;
                if(userLists != null){
                    for(Map.Entry<String, User> ele: userLists.entrySet()){
                        User user = ele.getValue();
                        if(user.getEmail().compareTo(email) == 0 && user.getPassword().compareTo(password) == 0){
                            Gson gson =  new Gson();
                            String json = gson.toJson(user);
                            SharedPreferences.Editor editor = ref.edit();
                            editor.putString(Const.USER_KEY, ele.getKey());
                            editor.putString(Const.USER_DATA, json);
                            editor.commit();

                            Toast.makeText(context, "Logged In", Toast.LENGTH_LONG).show();

                            isSuccess = true;
                            Intent i = new Intent(context, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                    if(!isSuccess){
                        Toast.makeText(context, "Email or password invalid", Toast.LENGTH_LONG).show();

                    }

                }else{
                    Toast.makeText(context, "User not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something wrong", Toast.LENGTH_LONG).show();

            }
        });
    }

    /**
     *
     * @param context
     * @param user
     */
    public void register(Context context, User user){
        dbRef.push().setValue(user);
        Toast.makeText(context, "Register successfully", Toast.LENGTH_LONG).show();

    }

    public void updateUser(String userKey, User user){
        dbRef.child(userKey).setValue(user);
    }
}
