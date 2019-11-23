package com.ltdung.friendlychat.presentation.ui.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ltdung.friendlychat.data.store.firebase.FirebaseMessageEntityStore;
import com.ltdung.friendlychat.data.util.StringUtil;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.user.GetUser;
import com.ltdung.friendlychat.library.presentation.DefaultSubscriber;
import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.presentation.App;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.di.component.ViewComponent;
import com.ltdung.friendlychat.presentation.di.module.ViewModule;
import com.ltdung.friendlychat.presentation.mvp.presenter.DialogPresenter;
import com.ltdung.friendlychat.presentation.ui.activity.DialogActivity;

import java.util.Map;

import javax.inject.Inject;

public class BsFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    GetUser getUser;

    private ViewComponent viewComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        viewComponent =((App)getApplicationContext())
                .getAppComponent()
                .plus(new ViewModule(new ViewImpl(this)));
        viewComponent.inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (data == null) {
            return;
        }
        String senderId = data.get(FirebaseMessageEntityStore.KEY_FCM_SENDER_ID);
        if (TextUtils.isEmpty(senderId)) {
            return;
        }
        String text = data.get(FirebaseMessageEntityStore.KEY_FCM_TEXT);
        sendNotification(senderId, text);
        getUser.execute(senderId, new DefaultSubscriber<UserDto>(null) {
            @Override
            public void onNext(UserDto userDto) {
                super.onNext(userDto);
                updateNotification(senderId,
                        StringUtil.concatLinearly(" ", userDto.getFirstName(), userDto.getLastName()),
                        text, false);
            }
        });

    }

    private void sendNotification(String senderId, String message) {
        updateNotification(senderId, null, message, true);
    }

    private void updateNotification(String senderId, String senderFullName, String message, boolean needSound) {
        if (DialogPresenter.getCurrentPeerId() != null) {
            return;
        }
        Intent intent = new Intent(this, DialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DialogActivity.KEY_PEER_ID, senderId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(senderFullName == null ? getString(R.string.loading) : senderFullName)
                .setContentText(message)
                //.setSmallIcon(R.drawable.)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        if (needSound) {
            notificationBuilder.setSound(defaultSoundUri);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(senderId.hashCode(), notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewComponent = null;
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(getClass().getName(), "Refreshed token: " + s);
    }


}
