package com.project.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.activity.R;
import com.project.models.Chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<Chat> chatList;

    public ChatAdapter(Context c, Map<String, Chat> chat){
        this.chatList = new ArrayList<>();
        this.context = c;
        for(Map.Entry<String, Chat> ele: chat.entrySet()){
            chatList.add(ele.getValue());
        }

        chatList.sort(Comparator.comparing(Chat::getTimestamp));
    }
    @Override
    public int getCount() {
        return chatList.size();
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
        view = inflater.inflate(R.layout.list_msg, null);

        Chat chat = chatList.get(pos);
        TextView message_user, message_time, message_text;
        message_text = view.findViewById(R.id.message_text);
        message_time = view.findViewById(R.id.message_time);
        message_user = view.findViewById(R.id.message_user);

        message_text.setText(chat.getMessage());
        message_time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chat.getTimestamp()));
        if(chat.getName().equals("Admin")){
            message_user.setText("Admin");
        }else{
            message_user.setText("You");
        }
        return view;
    }
}
