package com.sumitgouthaman.socsend.app.socket_utils;

import android.util.Log;

import com.sumitgouthaman.socsend.app.general_utils.EndPoint;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by sumit on 9/6/14.
 */
public class UDPSender {
    public static void send(EndPoint endPoint, byte[] array) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(endPoint.ipAddress);
            byte[] sendData = array;
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, endPoint.port);
            clientSocket.send(sendPacket);
            clientSocket.close();
        } catch (Exception e) {
            Log.d("UDPSender", e.getMessage());
        }
    }

    public static byte[] sendWait(EndPoint endPoint, byte[] array) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(endPoint.ipAddress);
            byte[] sendData = array;
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, endPoint.port);
            clientSocket.send(sendPacket);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            byte[] result = receivePacket.getData();
            clientSocket.close();
            return result;
        } catch (Exception e) {
            Log.d("UDPSender", e.getMessage());
        }
        return null;
    }

    public static void send(EndPoint endPoint, String string) {
        send(endPoint, string.getBytes());
    }

    public static byte[] sendWait(EndPoint endPoint, String string) {
        return sendWait(endPoint, string.getBytes());
    }
}
