package com.ltdung.friendlychat.presentation.ui.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.databinding.ActivityDialogBinding;
import com.ltdung.friendlychat.presentation.di.component.ViewComponent;
import com.ltdung.friendlychat.presentation.mvp.model.MessageModel;
import com.ltdung.friendlychat.presentation.mvp.presenter.DialogPresenter;
import com.ltdung.friendlychat.presentation.mvp.view.DialogView;
import com.ltdung.friendlychat.presentation.mvp.view.impl.DialogViewImpl;
import com.ltdung.friendlychat.presentation.ui.adapter.MessageAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class DialogActivity extends BaseDaggerActivity<DialogView, DialogPresenter, ActivityDialogBinding> {

    private static final String TAG = "DialogActivity";
    public static final String KEY_PEER_ID = "peer_id";

    @Inject
    Lazy<DialogPresenter> dialogPresenter;

    private MessageAdapter messageAdapter;

    public static void start(Context context, String peerId) {
        Intent intent = BaseActivity.getBaseStartIntent(context, DialogActivity.class, false);
        intent.putExtra(KEY_PEER_ID, peerId);
        context.startActivity(intent);
    }

    @Override
    public void onLoadFinished() {
        super.onLoadFinished();

        initPeerId();
        initUI();
    }

    private void initUI() {
        initSendMessageButton();
        initMessagesRecyclerView();

        view.clearMessageNotification();
    }

    @Override
    protected void injectViewComponent(ViewComponent viewComponent) {
        viewComponent.inject(this);
    }

    @Override
    protected DialogView initView() {
        return new DialogViewImpl(this) {
            @Override
            public void renderMessages(List<MessageModel> messages) {
                messageAdapter.setItems(messages);
                if(messageAdapter.getItemCount() > 0){
                    binding.rvUsers.scrollToPosition(messageAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void setTitle(String title) {
                DialogActivity.this.setTitle(title);
            }

            @Override
            public void clearInput() {
                binding.tvInputMessage.getText().clear();
            }

            @Override
            public void showMessageMenu(MessageModel message, int position) {
                showMessagePopup(message, position);
            }

            @Override
            public void clearMessageNotification() {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(presenter.getPeerId().hashCode());
            }
        };
    }

    @Override
    protected Lazy<DialogPresenter> initPresenter() {
        return dialogPresenter;
    }

    @Override
    protected ActivityDialogBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_dialog);
    }

    private void showMessagePopup(MessageModel message, int position){
        View item = binding.rvUsers.getLayoutManager().findViewByPosition(position);
        View text = item.findViewById(R.id.tv_text);
        PopupMenu popupMenu = new PopupMenu(text.getContext(), text);
        boolean findItemVisibility = message.getSenderId().equals(presenter.getAuthManager().getCurrentUserId())
                && position == messageAdapter.getItemCount() - 1;
        popupMenu.inflate(R.menu.menu_message_item);
        popupMenu.getMenu().findItem(R.id.item_edit).setVisible(findItemVisibility);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.item_delete:
                    presenter.deleteMessage(message);
                    return true;
                case R.id.item_edit:
                    showEditMessageDialog(message);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void showEditMessageDialog(MessageModel messageModel){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        final EditText editText = new EditText(this);
        editText.setText(messageModel.getText());
        dialog.setView(editText);
        dialog.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
            messageModel.setText(editText.getText().toString());
            presenter.editMessage(messageModel);
        });
        dialog.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {});
        dialog.create().show();
    }

    private void initMessagesRecyclerView() {
        messageAdapter = new MessageAdapter(view, presenter.getAuthManager().getCurrentUserId());
        binding.rvUsers.setAdapter(messageAdapter);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initPeerId() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String peerId = extras.getString(KEY_PEER_ID);
            presenter.setPeerId(peerId);
        }
    }

    private void initSendMessageButton() {
        binding.btnSend.setOnClickListener(view1 -> {
            String message = binding.tvInputMessage.getText().toString();
            if (!TextUtils.isEmpty(message)) {
                presenter.sendMessage(message);
            }
        });
    }

}
