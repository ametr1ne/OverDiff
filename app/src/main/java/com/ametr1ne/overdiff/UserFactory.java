package com.ametr1ne.overdiff;

import com.ametr1ne.overdiff.models.User;
import com.ametr1ne.overdiff.utils.AuthUserTask;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    }

    private static UserFactory userFactory;

    public static UserFactory getInstance() {
        if (userFactory == null) userFactory = new UserFactory();
        return userFactory;
    }

    public void authCurrentUser(String user, String password, Consumer<User> action) {
        try {


            String key = "Bar12345Bar12345Bar12345Bar12345";
            SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            String s = DatatypeConverter.printBase64Binary(encrypted);

            new AuthUserTask(user, s, user1 -> {
                currentUser = user1;
                action.accept(currentUser);
            }).execute();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }
}
