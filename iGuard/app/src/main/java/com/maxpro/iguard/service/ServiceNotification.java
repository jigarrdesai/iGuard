package com.maxpro.iguard.service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.maxpro.iguard.R;
import com.maxpro.iguard.activity.ActPatrolling;
import com.maxpro.iguard.utility.Var;

/**
 * Created by Ketan on 5/20/2015.
 */
public class ServiceNotification extends Service
{

    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        String objId=intent.getStringExtra(Var.IntentObjId);
        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),ActPatrolling.class);
        intent1.putExtra(Var.IntentObjId,objId);
        Notification notification = new Notification(R.drawable.ic_launcher,"iGuard", System.currentTimeMillis());
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),(int) System.currentTimeMillis(), intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |=Notification.DEFAULT_SOUND;
        notification.setLatestEventInfo(this.getApplicationContext(), "iGuard", "Patrolling Notifier", pendingNotificationIntent);

        mManager.notify((int) System.currentTimeMillis(), notification);
        stopSelf();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}