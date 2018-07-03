package io.github.messageblocker_standardapi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Monitor incoming messages and invoke processing methods.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.d("Log", "BroadcastReceiver failed, no intent data to process.");
            return;
        }

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Log.d("Log", "SMS received.");
            String smsOriginatingAddress, smsDisplayMessage;

            // Choose which code snippet to use NEW (KitKat+), or legacy
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.d("Log", "Kitkat or newer + API "+Build.VERSION.SDK_INT);
                for (SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    if (message == null) {
                        Log.d("Log", "SMS message is null -- ABORT");
                        break;
                    }
                    smsOriginatingAddress = message.getDisplayOriginatingAddress();
                    smsDisplayMessage = message.getDisplayMessageBody();
                    SmsParser.processReceivedSms(smsOriginatingAddress, smsDisplayMessage);
                }
            } else {
                Log.d("Log", "legacy SMS implementation (before KitKat) API "+Build.VERSION.SDK_INT);
                Object[] data = (Object[])bundle.get("pdus");
                for (Object pdu : data) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[])pdu);
                    if (message == null) {
                        Log.d("Log", "SMS message is null -- ABORT");
                        break;
                    }
                    smsOriginatingAddress = message.getDisplayOriginatingAddress();
                    smsDisplayMessage = message.getDisplayMessageBody();
                    SmsParser.processReceivedSms(smsOriginatingAddress, smsDisplayMessage);
                }
            }
        }
    }
}
