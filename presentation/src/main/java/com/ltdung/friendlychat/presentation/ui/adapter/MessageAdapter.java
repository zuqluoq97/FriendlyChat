package com.ltdung.friendlychat.presentation.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.databinding.ItemMessageBinding;
import com.ltdung.friendlychat.presentation.mvp.model.MessageModel;
import com.ltdung.friendlychat.presentation.mvp.view.DialogView;
import com.ltdung.friendlychat.presentation.ui.viewholder.MessageViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<MessageModel> items = new ArrayList<>();

    private String currentUserId;

    private DialogView dialogView;

    public MessageAdapter(DialogView dialogView, String currentUserId){
        this.dialogView = dialogView;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_message, parent, false);
        return new MessageViewHolder(dialogView, currentUserId, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<MessageModel> messages){
        items.clear();
        items.addAll(messages);
        notifyDataSetChanged();
    }
}
