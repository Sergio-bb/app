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

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainView;

public class FCMReceiverHMS extends HmsMessageService {

    private static final String TAG = "FCMReceiverHMS";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.i(TAG, "receive token:" + token);

        Intent intent = new Intent();
        intent.setAction("com.huawei.codelabpush.ON_NEW_TOKEN");
        intent.putExtra("token",token);
        sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        Log.i(TAG, "onMessageReceived is called");

        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }

        // Obtiene contenido del mensaje
        Log.i(TAG, "get Data: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getDataMap: " + message.getDataOfMap()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl()
                + "\n getBody: " + message.getNotification().getBody()
                + "\n getToken: " + message.getToken());

        sendMyNotification(message.getNotification().getBody(), message.getNotification().getTitle());
    }

    private void sendMyNotification(String message, String title)
    {
        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, ActivityMainView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
