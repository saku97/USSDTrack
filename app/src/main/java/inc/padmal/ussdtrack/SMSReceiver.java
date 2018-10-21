package inc.padmal.ussdtrack;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by knight on 10/21/18.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (Object pdu : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read
            String messageBody = smsMessage.getMessageBody();
            if (sender.equals("+94712755777") && messageBody.contains("DATA:")) {
                messageBody = messageBody.substring(messageBody.indexOf("DATA:") + 6);
                mListener.messageReceived(messageBody.substring(0, messageBody.indexOf("MB") - 1));
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}