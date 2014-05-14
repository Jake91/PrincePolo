package se.agile.model;

import se.agile.activities.AboutFragment;
import se.agile.princepolo.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;

public class CreateNotificationActivity extends Activity {

  public void createNotification(View view) {
    // Prepare intent which is triggered if the
    // notification is selected
    Intent intent = new Intent(this, AboutFragment.class);
    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

    // Build notification
    // Actions are just fake
    Notification noti = new Notification.Builder(this)
        .setContentTitle("New mail from " + "test@gmail.com")
        .setContentText("Subject").setSmallIcon(R.drawable.warning)
        .setContentIntent(pIntent)
        .addAction(R.drawable.warning, "Call", pIntent)
        .addAction(R.drawable.warning, "More", pIntent)
        .addAction(R.drawable.warning, "And more", pIntent).build();
    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    // hide the notification after its selected
    noti.flags |= Notification.FLAG_AUTO_CANCEL;

    notificationManager.notify(0, noti);

  }
} 
