package com.ltdung.friendlychat.presentation.mvp.view;

import com.ltdung.friendlychat.library.presentation.mvp.view.View;
import com.ltdung.friendlychat.presentation.mvp.model.MessageModel;

import java.util.List;

public interface DialogView extends View {
    void renderMessages(List<MessageModel> messages);

    void setTitle(String title);

    void clearInput();

    void showMessageMenu(MessageModel message, int position);

    void clearMessageNotification();
}
