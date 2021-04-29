package com.ametr1ne.overdiff;

import com.ametr1ne.overdiff.models.User;

public class UserFactory {

    private User currentUser;

    private UserFactory(){}

    private static UserFactory userFactory;

    public static UserFactory getInstance(){
        if(userFactory == null) userFactory = new UserFactory();
        return userFactory;
    }






    public void authCurrentUser(String user,String password){



    }

}
