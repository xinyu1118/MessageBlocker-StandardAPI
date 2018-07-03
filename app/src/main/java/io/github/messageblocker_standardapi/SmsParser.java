package io.github.messageblocker_standardapi;


import android.util.Log;

/**
 * Check whether new incoming messages are from a certain phone number, and phone number and messages are passed.
 */
public class SmsParser {

    public static void processReceivedSms(String smsOriginatingAddress, String smsDisplayMessage) {
        Log.d("Log", "SMS from: "+smsOriginatingAddress);
        Log.d("Log", "SMS body: "+smsDisplayMessage);

        if (smsOriginatingAddress.equals("15555215556"))
            Log.d("Log", "Unwanted calls!");
    }
}
