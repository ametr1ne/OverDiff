package com.ametr1ne.overdiff.utils;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceId {

    public static String getDeviceId() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ip", GlobalProperties.IP);
            jsonObject.put("id", GlobalProperties.DEVICE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new String(Base64.encode(jsonObject.toString().getBytes(),Base64.DEFAULT));
    }

}
