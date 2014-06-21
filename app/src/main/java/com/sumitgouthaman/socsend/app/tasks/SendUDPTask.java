package com.sumitgouthaman.socsend.app.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sumitgouthaman.socsend.app.R;
import com.sumitgouthaman.socsend.app.general_utils.EndPoint;
import com.sumitgouthaman.socsend.app.socket_utils.UDPSender;

/**
 * Created by sumit on 21/6/14.
 */
public class SendUDPTask extends AsyncTask<EndPoint, Void, Void> {
    final private boolean sendingBytes;
    final private byte[] bytes;
    final private String str;
    private Context context = null;

    public SendUDPTask(byte[] bytes) {
        sendingBytes = true;
        this.bytes = bytes;
        this.str = null;
    }

    public SendUDPTask(String str) {
        sendingBytes = false;
        this.bytes = null;
        this.str = str;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(EndPoint... endPoints) {
        for (EndPoint endPoint : endPoints) {
            if (sendingBytes) {
                UDPSender.send(endPoint, bytes);
            } else {
                UDPSender.send(endPoint, str);
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
