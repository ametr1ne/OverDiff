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

    private final User currentUser;

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

        System.out.println("SAVE PASSWORD : "+ savePassword);

        try {
            SecretKeySpec aesKey = new SecretKeySpec(Base64.getDecoder().decode(SimpleCipher.PASSWORD_CIPHER_KEY.getBytes()), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            String s = DatatypeConverter.printBase64Binary(encrypted);

            new AuthUserTask(user, s, u -> {
                setCurrentUser(u);
                action.accept(currentUser);
                if (u.isAuthorization()) {
                    try {
                        FileProperties properties = MainActivity.getProperties();
                        properties.setProperties("refresh_token", !savePassword ? null : new String(Base64.getEncoder().encode(SimpleCipher.encodePassword(u.getRefreshToken().getBytes()))));
                        properties.setProperties("user_id", !savePassword ? null : u.getId().toString());
                        properties.setProperties("save", Boolean.toString(savePassword));
                        properties.save();
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
                setCurrentUser(u);
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

        System.out.println(properties);

        Optional<String> refreshTokenOptional = properties.getValue("refresh_token");
        Optional<String> userIdOptional = properties.getValue("user_id");
        if (refreshTokenOptional.isPresent() && userIdOptional.isPresent()) {
            try {
                String refreshToken = refreshTokenOptional.get();
                long userId = Long.parseLong(userIdOptional.get());

                refreshToken = new String(SimpleCipher.decodePassword(Base64.getDecoder().decode(refreshToken.getBytes())));

                new RefreshTokenTask(refreshToken, userId, u -> {
                    setCurrentUser(u);
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

            } catch (BadPaddingException | IllegalBlockSizeException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void exit() {
        setCurrentUser(User.getInstance());
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

    private void setCurrentUser(User user) {
        this.currentUser.setAuthStatus(user.getAuthStatus());
        this.currentUser.setAuthorization(user.isAuthorization());
        this.currentUser.setEmail(user.getEmail());
        this.currentUser.setRefreshToken(user.getRefreshToken());
        this.currentUser.setAccessToken(user.getAccessToken());
        this.currentUser.setBan(user.isBan());
        this.currentUser.setEnabled(user.isEnabled());
        this.currentUser.setUsername(user.getUsername());
        this.currentUser.setId(user.getId());
        this.currentUser.setArticle(user.getArticle());
        this.currentUser.setComment(user.getComment());
        this.currentUser.setLikeDislikes(user.getLikeDislikes());
        this.currentUser.setPassword(user.getPassword());
        this.currentUser.setRoles(user.getRoles());
        this.currentUser.setToken(user.getToken());
    }

}
