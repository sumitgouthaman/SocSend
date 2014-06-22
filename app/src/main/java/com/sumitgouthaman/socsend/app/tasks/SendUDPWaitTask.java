package com.sumitgouthaman.socsend.app.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.sumitgouthaman.socsend.app.R;
import com.sumitgouthaman.socsend.app.general_utils.EndPoint;
import com.sumitgouthaman.socsend.app.socket_utils.UDPSender;

/**
 * Created by sumit on 21/6/14.
 */
public class SendUDPWaitTask extends AsyncTask<EndPoint, Void, Void> {
    final private boolean sendingBytes;
    final private byte[] bytes;
    final private String str;
    private Context context = null;
    private Handler handler;

    public SendUDPWaitTask(byte[] bytes, Handler handler) {
        sendingBytes = true;
        this.bytes = bytes;
        this.str = null;
        this.handler = handler;
    }

    public SendUDPWaitTask(String str, Handler handler) {
        sendingBytes = false;
        this.bytes = null;
        this.str = str;
        this.handler = handler;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(EndPoint... endPoints) {
        for (EndPoint endPoint : endPoints) {
            byte[] result;
            if (sendingBytes) {
                result = UDPSender.sendWait(endPoint, bytes);
            } else {
                result = UDPSender.sendWait(endPoint, str);
            }
            if (handler != null) {
                Message message = Message.obtain();
                message.obj = result;
                handler.sendMessage(message);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (context != null) {
            Toast.makeText(context, R.string.sent, Toast.LENGTH_SHORT).show();
        }
    }
}
