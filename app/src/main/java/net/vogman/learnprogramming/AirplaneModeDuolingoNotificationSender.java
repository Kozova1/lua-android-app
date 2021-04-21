package net.vogman.learnprogramming;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;
import java.util.Random;

public class AirplaneModeDuolingoNotificationSender extends BroadcastReceiver {
    private final Random rng = new Random();
    private final static String CHANNEL_ID = "net.vogman.learnprogramming.airplaneModeNag";
    private final static String[] AIRPLANE_MESSAGES = new String[]{
            "If you are on an airplane, why not spend the time by learning a bit about programming?",
            "Seems like you're on an airplane. Complete some programming exercises or I will turn off its engines!",
            "I have noticed you're flying on an airplane. Complete a programming exercise or I will shoot it down!",
            "I have your family. If you want to see them again, complete a programming exercise before the plane lands!",
    };
    private boolean isInitialized = false;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isAirplaneModeOn = intent.getExtras().getBoolean("state");
        if (isAirplaneModeOn) {
            nagUser(context);
        }
    }
    
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Airplane Mode Evil Reminder";
            String description = "Reminds the user to program every time they fly on an airplane";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.createNotificationChannel(channel);
        }
        isInitialized = true;
    }
    
    private void nagUser(Context context) {
        if (!isInitialized) {
            createNotificationChannel(context);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        String airplaneRandomMessage = AIRPLANE_MESSAGES[rng.nextInt(AIRPLANE_MESSAGES.length)];
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Airplane Mode Detected")
                .setContentText(airplaneRandomMessage)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(airplaneRandomMessage))
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(context).notify(1, builder.build());
    }
}
