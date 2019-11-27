package com.ltdung.friendlychat.presentation.ui.viewholder;

import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.ltdung.friendlychat.library.presentation.ui.viewholder.BaseViewHolder;
import com.ltdung.friendlychat.presentation.databinding.ItemMessageBinding;
import com.ltdung.friendlychat.presentation.mvp.model.MessageModel;
import com.ltdung.friendlychat.presentation.mvp.view.DialogView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MessageViewHolder extends BaseViewHolder<ItemMessageBinding, MessageModel> {

    private static final String TAG = "MessageViewHolder";
    private String currentUserId;

    private DialogView dialogView;

    public MessageViewHolder(DialogView dialogView,
                             String currentUserId,
                             ItemMessageBinding binding) {
        super(binding);
        this.dialogView = dialogView;
        this.currentUserId = currentUserId;
    }

    @Override
    public void bind(MessageModel messageModel, int position) {

        LinearLayout root = (LinearLayout) binding.getRoot();
        if(messageModel.getSenderId().equals(currentUserId)){
            root.setGravity(Gravity.RIGHT);
        }else{
            root.setGravity(Gravity.LEFT);
        }
        root.setOnClickListener(view -> {
            Log.d(TAG, String.valueOf(position));
            dialogView.showMessageMenu(messageModel, position);
        });
        binding.tvText.setText(messageModel.getText());
        binding.tvTime.setText(formatTime(messageModel.getTimestamp()));
    }

    private String formatTime(long timesStamp){
        DateFormat df = new SimpleDateFormat("dd MMM, HH:mm");
        return df.format(timesStamp);
    }
}
