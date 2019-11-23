package com.ltdung.friendlychat.presentation.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.databinding.ItemUserBinding;
import com.ltdung.friendlychat.presentation.mvp.model.UserModel;
import com.ltdung.friendlychat.presentation.mvp.view.UsersView;
import com.ltdung.friendlychat.presentation.ui.viewholder.UserViewHolder;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<UserModel> items = new ArrayList<>();

    private UsersView usersView;

    public UsersAdapter(UsersView usersView){
        this.usersView = usersView;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_user, parent, false);
        return new UserViewHolder(usersView, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<UserModel> users){
        items.clear();
        items.addAll(users);
        notifyDataSetChanged();
    }
}
