package com.ametr1ne.overdiff.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

public class DeviceId {

    public static String getDeviceId() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ip", GlobalProperties.IP);
            jsonObject.put("id", GlobalProperties.DEVICE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new String(Base64.getEncoder().encode(jsonObject.toString().getBytes()));
    }

}
