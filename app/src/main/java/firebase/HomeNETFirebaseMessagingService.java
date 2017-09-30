package firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.koeksworld.homenet.FlaggedPostActivity;
import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.NewAnnouncementActivity;
import com.koeksworld.homenet.NewPostActivity;
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

        JSONObject object = null;
        try {
            Map<String,String> mapData = message.getData();
            object = new JSONObject(mapData);
            int requestCode = 0;
            switch (object.getString("keyword")) {
                case "new_post":
                    Intent newPostIntent = new Intent(this, NewPostActivity.class);
                    newPostIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle newBundle = new Bundle();
                    newBundle.putString("mode", "new_post");
                    newBundle.putInt("housePostID", object.getInt("dataID"));
                    newPostIntent.putExtra("notificationBundle", newBundle);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, newPostIntent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                    notificationBuilder.setSmallIcon(R.drawable.ic_home_black_24dp).setContentTitle(object.getString("title")).setContentText(object.getString("body")).setDefaults(Notification.DEFAULT_ALL).setPriority(Notification.PRIORITY_HIGH).setAutoCancel(true);
                    NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_message_black_24dp, "Open", pendingIntent);
                    notificationBuilder.addAction(action);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification box = notificationBuilder.build();
                    notificationManager.notify(0, box);
                    break;
                case "flagged_post":
                    Intent flagIntent = new Intent(this, FlaggedPostActivity.class);
                    flagIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle flagBundle = new Bundle();
                    flagBundle.putString("mode", "flagged_post");
                    flagBundle.putInt("housePostID", object.getInt("housePostID"));
                    flagIntent.putExtra("notificationBundle", flagBundle);
                    PendingIntent pendingFlagIntent = PendingIntent.getActivity(this, requestCode, flagIntent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder pendingBuilder = new NotificationCompat.Builder(this);
                    pendingBuilder.setSmallIcon(R.drawable.ic_home_black_24dp).setContentTitle(object.getString("title")).setContentText(object.getString("body")).setDefaults(Notification.DEFAULT_ALL).setPriority(Notification.PRIORITY_HIGH).setAutoCancel(true);
                    NotificationCompat.Action actionTwo = new NotificationCompat.Action(R.drawable.ic_message_black_24dp, "Open", pendingFlagIntent);
                    pendingBuilder.addAction(actionTwo);
                    NotificationManager nextManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification boxTwo = pendingBuilder.build();
                    nextManager.notify(0, boxTwo);
                    break;
                case "new_announcement":
                    Intent announcementIntent = new Intent(this, NewAnnouncementActivity.class);
                    announcementIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle announcementBundle = new Bundle();
                    announcementBundle.putString("mode", "new_announcement");
                    announcementBundle.putInt("announcementID", object.getInt("announcementID"));
                    announcementIntent.putExtra("notificationBundle", announcementBundle);
                    PendingIntent announcementPendingFlagIntent = PendingIntent.getActivity(this, requestCode, announcementIntent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder announcementPendingBuilder = new NotificationCompat.Builder(this);
                    announcementPendingBuilder.setSmallIcon(R.drawable.ic_home_black_24dp).setContentTitle(object.getString("title")).setContentText(object.getString("body")).setDefaults(Notification.DEFAULT_ALL).setPriority(Notification.PRIORITY_HIGH).setAutoCancel(true);
                    NotificationCompat.Action actionThree = new NotificationCompat.Action(R.drawable.ic_message_black_24dp, "Open", announcementPendingFlagIntent);
                    announcementPendingBuilder.addAction(actionThree);
                    NotificationManager announcementManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification boxThree = announcementPendingBuilder.build();
                    announcementManager.notify(0, boxThree);
                    break;
                case "new_comment":

                    break;
                case "new_house_member":

                    break;
                case "membership_approved":


                    break;
                case "membership_declined":

                    break;
                case "new_message":

                    break;
                case "message_reply":

                    break;



            }



        } catch (Exception error) {

        }

    }
}
