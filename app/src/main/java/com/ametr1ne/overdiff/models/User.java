package com.ametr1ne.overdiff.models;


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




}
