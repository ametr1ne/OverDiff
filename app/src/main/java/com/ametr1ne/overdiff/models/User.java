package com.ametr1ne.overdiff.models;


import com.ametr1ne.overdiff.utils.AuthStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private boolean ban;
    private Token token;
    private Set<Role> roles;
    private Collection<Comment> comment;
    private Collection<Article> article;
    private Collection<LikeDislike> likeDislikes;

    private String accessToken;
    private String refreshToken;

    private int authStatus;


    private boolean authorization = false;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public Collection<LikeDislike> getLikeDislikes() {
        if (likeDislikes == null)
            setLikeDislikes(new ArrayList<>());
        return likeDislikes;
    }

    public void setLikeDislikes(Collection<LikeDislike> likeDislikes) {
        this.likeDislikes = likeDislikes;
    }

    public Collection<Comment> getComment() {
        if (comment == null)
            setComment(new ArrayList<>());
        return comment;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public void setComment(Collection<Comment> comment) {
        this.comment = comment;
    }

    public Collection<Article> getArticle() {
        if (article == null)
            setArticle(new ArrayList<>());
        return article;
    }

    public void setArticle(Collection<Article> article) {
        this.article = article;
    }


    public boolean isAuthorization() {
        return authorization;
    }

    public void setAuthorization(boolean authorization) {
        this.authorization = authorization;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", ban=" + ban +
                ", roles=" + roles +
                '}';
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return !ban;
    }

    public boolean isCredentialsNonExpired() {
        return enabled;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static User deserialize(JSONObject jsonObject) {
        User user = getInstance();
        try {

            /*
              json.put("id", user.getId());
        json.put("username", user.getUsername());
        json.put("email", user.getEmail());
        json.put("enabled", user.isEnabled());
        json.put("ban", user.isBan());
        json.put("access_token", user.getAccessToken());
        json.put("refreshToken", user.getRefreshToken());

             */


            int authStatus = jsonObject.has("auth_status") ? jsonObject.getInt("auth_status") : AuthStatus.ERROR;

            user.setAuthStatus(authStatus);

            if(authStatus == AuthStatus.SUCCESSFUL_AUTHORIZATION) {
                user.setId(jsonObject.getLong("id"));
                user.setUsername(jsonObject.getString("username"));
                user.setEmail(jsonObject.getString("email"));
                user.setEnabled(jsonObject.getBoolean("enabled"));
                user.setBan(jsonObject.getBoolean("ban"));
                user.setAccessToken(jsonObject.getString("access_token"));
                user.setRefreshToken(jsonObject.getString("refresh_token"));
                user.setAuthStatus(authStatus);
                user.setAuthorization(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }


    public static User getInstance(){
        User user = new User();
        user.setAuthorization(false);
        user.setAuthStatus(AuthStatus.NOT_AUTHORIZED);

        return user;

    }


}
