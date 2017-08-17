package firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.R;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Okuhle on 2017/06/23.
 */

//Source for Notifications: http://www.brevitysoftware.com/blog/how-to-get-heads-up-notifications-in-android/
    //Source for different types of notifications: http://www.brevitysoftware.com/blog/different-types-of-notification-android/
public class HomeNETFirebaseMessagingService extends FirebaseMessagingService {

    public HomeNETFirebaseMessagingService() {
    }
    //This is called when a message is received from the FCM
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

            showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage message) {
        //Redirect to the news feed - then load the messages fragment
        JSONObject object = null;
        try {
            Map<String,String> mapData = message.getData();
            object = new JSONObject(mapData);
        Intent readIntent = new Intent(this, HomeNetFeedActivity.class);
        readIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        readIntent.putExtra("mode", "notification_message");
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, readIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.homenetlogo).setContentTitle(object.getString("title")).setContentText(object.getString("body")).setDefaults(Notification.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.addAction(R.drawable.ic_message_black_24dp, "Read", pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        } catch (Exception error) {

        }

    }
}
