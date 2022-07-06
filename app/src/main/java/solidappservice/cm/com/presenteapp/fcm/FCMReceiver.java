package solidappservice.cm.com.presenteapp.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.ActivityMainView;

public class FCMReceiver extends FirebaseMessagingService{

    private static final String TAG = "FCMReceiver";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "Message Data PayLoad: " + remoteMessage.getData());
        }

        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendMyNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());

    }

    private void sendMyNotification(String message, String title) {

        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, ActivityMainView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager != null){
            //String channel_id = "presente_channel_id";
            //notificationManager.createNotificationChannel(new NotificationChannel(channel_id, channel_id, NotificationManager.IMPORTANCE_MAX));
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ico_notificaciones_push)
                    .setColor(Color.WHITE)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(0, notificationBuilder.build());
        }


    }

}
