package com.sumitgouthaman.socsend.app.socket_utils;

import android.util.Log;

import com.sumitgouthaman.socsend.app.general_utils.EndPoint;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by sumit on 9/6/14.
 */
public class UDPReceiver {
    public static byte[] receive(EndPoint endPoint) {
        try {
            byte[] receiveData = new byte[1024];
            DatagramSocket serverSocket = new DatagramSocket(endPoint.port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            return receivePacket.getData();
        } catch (Exception e) {
            Log.d("UDPReceiver", e.getMessage());
        }
        return null;
    }
}
