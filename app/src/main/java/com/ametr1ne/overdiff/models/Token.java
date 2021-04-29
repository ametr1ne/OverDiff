package com.ametr1ne.overdiff.models;

import java.util.Date;


public class Token {

    private Long id;
    private String token;
    private Date date;
    private User user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", tokenId='" + token + '\'' +
                ", date=" + date +
                ", user=" + user +
                '}';
    }
}
