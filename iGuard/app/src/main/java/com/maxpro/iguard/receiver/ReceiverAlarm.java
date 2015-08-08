package com.maxpro.iguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maxpro.iguard.service.ServiceNotification;
import com.maxpro.iguard.utility.Var;

/**
 * Created by Ketan on 5/20/2015.
 */
public class ReceiverAlarm extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service1 = new Intent(context, ServiceNotification.class);
        service1.putExtra(Var.IntentObjId,intent.getStringExtra(Var.IntentObjId));
        context.startService(service1);

    }
}
