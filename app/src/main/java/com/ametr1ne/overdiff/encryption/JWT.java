package com.ametr1ne.overdiff.encryption;

import com.ametr1ne.overdiff.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SignatureException;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class JWT {
    public static boolean isAlive(String token) {
        String[] split = token.split("\\.");
        if (split.length == 3) {
            try {
                String header = split[0];
                String payload = split[1];
                String signatureExpected = split[2];
                long time = new JSONObject(new String(Base64.getDecoder().decode(payload))).getLong("exp");

                return SimpleCipher.verifySignature(
                        (header + "." + payload).getBytes(), Base64.getDecoder().decode(signatureExpected.getBytes())) && (time >= (System.currentTimeMillis() / 1000L));

            } catch (SignatureException | JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }




}
