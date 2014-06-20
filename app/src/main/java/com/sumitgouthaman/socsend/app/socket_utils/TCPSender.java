package com.sumitgouthaman.socsend.app.socket_utils;

import android.os.Handler;

import com.sumitgouthaman.socsend.app.general_utils.EndPoint;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sumit on 20/6/14.
 */
public class TCPSender {
    public static void send(EndPoint endPoint, String str){
        try {
            Socket s = new Socket(endPoint.ipAddress, endPoint.port);
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            pw.println(str);
            pw.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void send(EndPoint endPoint, byte[] bytes){
        try {
            Socket s = new Socket(endPoint.ipAddress, endPoint.port);
            OutputStream outputStream = s.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
