package com.monisha.samples.sloshed.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import static android.widget.Toast.makeText;

/**
 * Created by vazra on 3/25/2018.
 */

public class BlockOutgoing extends BroadcastReceiver {
    public static final String ABORT_PHONE_NUMBER = "+13134245612";

    private static final String OUTGOING_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
    private static final String INTENT_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("out", "OutgoingCallReceiver onReceive");
        if (intent.getAction().equals(BlockOutgoing.OUTGOING_CALL_ACTION)) {
            Log.d("out", "OutgoingCallReceiver NEW_OUTGOING_CALL received");

            // get phone number from bundle
            String phoneNumber = intent.getExtras().getString(BlockOutgoing.INTENT_PHONE_NUMBER);
           /* makeText(context,"blah"+phoneNumber,Toast.LENGTH_SHORT).show();
            makeText(context,"blue"+ABORT_PHONE_NUMBER,Toast.LENGTH_SHORT).show();*/
            if ((phoneNumber != null) && phoneNumber.trim().equals("+13134245612")) {
                 setResultData(null);
                 Toast.makeText(context, "Outgoing Call Blocke",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
