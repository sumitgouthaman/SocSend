package com.sumitgouthaman.socsend.app.general_utils;

/**
 * Created by sumit on 9/6/14.
 */
public class EndPoint {
    public String ipAddress;
    public int port;

    public EndPoint(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public EndPoint(int port) {
        this.ipAddress = null;
        this.port = port;
    }
}
