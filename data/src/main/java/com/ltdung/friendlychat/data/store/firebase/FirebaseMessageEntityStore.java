package com.ltdung.friendlychat.data.store.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.ltdung.friendlychat.data.R;
import com.ltdung.friendlychat.data.entity.MessageEntity;
import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.data.store.MessageEntityStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import rx.Observable;

public class FirebaseMessageEntityStore extends FirebaseEntityStore implements MessageEntityStore {

    private static final String TAG = "FirebaseMessageEntityStore";

    public static final String KEY_FCM_SENDER_ID = "sender_id";
    public static final String KEY_FCM_TEXT = "text";

    public static final String CHILD_MESSAGES = "messages";
    public static final String CHILD_USERS = "users";

    private AuthManager authManager;
    private Context context;

    @Inject
    public FirebaseMessageEntityStore(AuthManager authManager, Context context){
        this.authManager = authManager;
        this.context = context;
    }

    @Override
    public Observable<List<MessageEntity>> getMessages(String peerId) {
        Query query = database.child(CHILD_USERS)
                .child(authManager.getCurrentUserId())
                .child(CHILD_MESSAGES)
                .child(peerId);
        return getList(query, MessageEntity.class, false);
    }

    @Override
    public Observable<Void> postMessage(MessageEntity message) {
        DatabaseReference ref1 = database.child(CHILD_USERS)
                .child(message.getSenderId())
                .child(CHILD_MESSAGES)
                .child(message.getReceiverId());

        Observable o1 = create(ref1, message, null);

        DatabaseReference ref2 = database.child(CHILD_USERS)
                .child(message.getReceiverId())
                .child(CHILD_MESSAGES)
                .child(message.getSenderId());
        Observable o2 = create(ref2, message, null);

        Observable sendPushObservable = Observable.create(subscriber -> {
            sendNotification(message);
        });
        return o1.mergeWith(o2).mergeWith(sendPushObservable);
    }

    /**
     * Use own FCM app server to communicate with FCM connection server
     * instead of sent requests directly from client, because it is dangerous to pass the FCM API key
     * in request from client side
     */
    private void sendNotification(MessageEntity messageEntity){
        // Send push notification
        HttpsURLConnection connection = null;
        try{
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            // Put below API key
            connection.setRequestProperty("Authorization", "key=" + context.getString(R.string.server_key));
            JSONObject root = new JSONObject();
            JSONObject data = new JSONObject();
            data.put(KEY_FCM_TEXT, messageEntity.getText());
            data.put(KEY_FCM_SENDER_ID, messageEntity.getSenderId());
            root.put("to", "/topics/user_" + messageEntity.getReceiverId());

            byte[] outputBytes = root.toString().getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();
            connection.getInputStream(); //do not remove this line. request will not work without it gg
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    @Override
    public Observable<Void> editMessage(MessageEntity editedMessage) {
        //Allow to edit only last message and only this last message is yours;
        DatabaseReference ref1 = database
                .child(CHILD_USERS)
                .child(editedMessage.getSenderId())
                .child(CHILD_MESSAGES)
                .child(editedMessage.getReceiverId())
                .child(editedMessage.getId());
        Observable o1 = update(ref1, editedMessage, null);

        DatabaseReference ref2 = database
                .child(CHILD_USERS)
                .child(editedMessage.getReceiverId())
                .child(CHILD_MESSAGES)
                .child(editedMessage.getSenderId())
                .child(editedMessage.getId());
        Observable o2 = update(ref2, editedMessage, null);

        return o1.mergeWith(o2);
    }

    @Override
    public Observable<Void> deleteMessage(MessageEntity message) {
        String peerId = message.getSenderId().equals(authManager.getCurrentUserId())
                ? message.getReceiverId() : message.getSenderId();
        DatabaseReference reference = database.child(CHILD_USERS)
                .child(authManager.getCurrentUserId())
                .child(CHILD_MESSAGES)
                .child(peerId)
                .child(message.getId());
        Log.d(TAG, "deleteMessage - Firebase database reference" + reference.toString());
        return delete(reference, null);
    }
}
