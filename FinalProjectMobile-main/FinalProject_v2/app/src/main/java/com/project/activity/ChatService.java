package com.project.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.models.Chat;
import com.project.util.Const;

public class ChatService extends Service {
    private boolean firstBuilt = false;
    public ChatService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            Log.e("Service", "Service is running...");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();


        NotificationChannel channel = new NotificationChannel(Const.CHANNEL_ID, Const.CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Const.CHANNEL_ID)
                .setContentText("Service is running")
                        .setContentTitle("Service Enable")
                                .setSmallIcon(R.mipmap.ic_notification);

        startForeground(1001, builder.build());

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCE, MODE_PRIVATE);
            String key = sharedPreferences.getString(Const.USER_KEY, "");
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = db.getReference("/chats/" + key);

            dbRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getName().equals("Admin")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(Const.CHANNEL_ID, Const.CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Const.CHANNEL_ID)
                                    .setContentTitle("Message from Admin")
                                    .setSmallIcon(R.mipmap.ic_notification)
                                    .setAutoCancel(true)
                                    .setContentText(chat.getMessage());

                            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                            resultIntent.setAction(Intent.ACTION_MAIN);
                            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 0);
                            builder.setContentIntent(pendingIntent);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                            managerCompat.notify(999, builder.build());
                        }
                    }
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
        startForeground(1001, builder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}