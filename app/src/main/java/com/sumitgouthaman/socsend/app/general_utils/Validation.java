package com.sumitgouthaman.socsend.app.general_utils;

import android.content.Context;

import com.sumitgouthaman.socsend.app.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sumit on 14/6/14.
 */
public class Validation {
    public static String validateIP(Context context, String ipAddress) {
        ipAddress = ipAddress.trim();
        String returnMessage = null;
        if (ipAddress.equals("")) {
            returnMessage = context.getString(R.string.ip_blank);
        } else {
            Pattern pattern;
            Matcher matcher;

            String IPADDRESS_PATTERN =
                    "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
            pattern = Pattern.compile(IPADDRESS_PATTERN);
            matcher = pattern.matcher(ipAddress);
            if (!matcher.matches()) {
                returnMessage = context.getString(R.string.ip_pattern_incorrect);
            }
        }
        return returnMessage;
    }

    public static String validatePort(Context context, String port) {
        port = port.trim();
        if (port.equals("")) {
            return context.getString(R.string.port_empty);
        } else {
            try {
                int portInt = Integer.parseInt(port);
                if (portInt >= 0 && portInt <= 65535) {
                    return null;
                } else {
                    return context.getString(R.string.port_value_invalid);
                }
            } catch (NumberFormatException nfe) {
                return context.getString(R.string.port_not_integer);
            }
        }
    }
}
