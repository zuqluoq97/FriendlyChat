package com.ltdung.friendlychat.library.presentation.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<BINDING extends ViewDataBinding, MODEL> extends RecyclerView.ViewHolder{

    protected BINDING binding;

    public BaseViewHolder(BINDING binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    public abstract void bind(MODEL model, int position);
}
