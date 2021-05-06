package com.ametr1ne.overdiff;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ametr1ne.overdiff.encryption.SimpleCipher;
import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.AuthUserTask;
import com.ametr1ne.overdiff.utils.FileProperties;
import com.ametr1ne.overdiff.utils.RefreshTokenTask;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Consumer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class UserFactory {

    private User currentUser;

    private UserFactory() {
        currentUser = User.getInstance();
    }

    private static UserFactory userFactory;

    public static UserFactory getInstance() {
        if (userFactory == null) userFactory = new UserFactory();
        return userFactory;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void authCurrentUser(String user, String password, boolean savePassword, Consumer<User> action) {
        try {
            String key = "Bar12345Bar12345Bar12345Bar12345";
            SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            String s = DatatypeConverter.printBase64Binary(encrypted);

            new AuthUserTask(user, s, u -> {
                currentUser = u;
                action.accept(currentUser);
                if (u.isAuthorization()) {
                    try {
                        FileProperties properties = MainActivity.getProperties();
                        if (savePassword) {
                            properties.setProperties("refresh_token", new String(Base64.getEncoder().encode(SimpleCipher.encodePassword(u.getRefreshToken().getBytes()))));
                            properties.setProperties("user_id", u.getId().toString());
                            properties.setProperties("save", Boolean.toString(savePassword));
                            properties.save();
                        }
                    } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void refreshCurrentUser(Consumer<User> action) {
        if (currentUser != null && currentUser.getRefreshToken() != null) {
            new RefreshTokenTask(currentUser.getRefreshToken(), currentUser.getId(), u -> {
                currentUser = u;
                action.accept(u);
                if (u.isAuthorization()) {
                    try {
                        FileProperties properties = MainActivity.getProperties();
                        boolean save = Boolean.parseBoolean(properties.getValue("save").orElse("false"));
                        if (save) {
                            properties.setProperties("refresh_token", new String(Base64.getEncoder().encode(SimpleCipher.encodePassword(u.getRefreshToken().getBytes()))));
                            properties.setProperties("user_id", u.getId().toString());
                            properties.setProperties("save", Boolean.toString(save));
                            properties.save();
                        }
                    } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }).execute();
        }
    }

    public void refreshSavedUser(Consumer<User> action) {
        FileProperties properties = MainActivity.getProperties();

        Optional<String> refreshTokenOptional = properties.getValue("refresh_token");
        Optional<String> userIdOptional = properties.getValue("user_id");

        if (refreshTokenOptional.isPresent() && userIdOptional.isPresent()) {
            try {
                String refreshToken = refreshTokenOptional.get();
                long userId = Long.parseLong(userIdOptional.get());

                refreshToken = new String(SimpleCipher.decodePassword(Base64.getDecoder().decode(refreshToken.getBytes())));

                new RefreshTokenTask(refreshToken, userId, u -> {
                    currentUser = u;
                    action.accept(u);
                    if (u.isAuthorization()) {
                        try {
                            properties.setProperties("refresh_token", new String(Base64.getEncoder().encode(SimpleCipher.encodePassword(u.getRefreshToken().getBytes()))));
                            properties.setProperties("user_id", u.getId().toString());
                            properties.setProperties("save", properties.getValue("save").orElse("false"));
                            properties.save();
                        } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
                            e.printStackTrace();
                        }
                    }
                }).execute();

            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void exit() {
        currentUser = User.getInstance();
        FileProperties properties = MainActivity.getProperties();
        try {
            properties.remove("refresh_token");
            properties.remove("user_id");
            properties.remove("save");

            properties.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
