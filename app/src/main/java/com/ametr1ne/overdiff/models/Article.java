package com.ametr1ne.overdiff.models;

import java.util.*;

public class Article {

    private Long id;

    private String text;
    private String description;
    private String hash;
    private int likes;
    private int dislikes;
    private String icon;

    private Date dateCreate;


    private Collection<Comment> comment;

    private User user;

    private Collection<LikeDislike> likeDislikes;

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Collection<Comment> getComment() {
        if (comment == null)
            setComment(new ArrayList<>());
        return comment;
    }

    public Collection<LikeDislike> getLikeDislikes() {
        if (likeDislikes == null)
            setLikeDislikes(new ArrayList<>());
        return likeDislikes;
    }

    public void setLikeDislikes(Collection<LikeDislike> likeDislikes) {
        this.likeDislikes = likeDislikes;
    }

    public void setComment(Collection<Comment> comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }


    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", description='" + description + '\'' +
                ", hash='" + hash + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article role = (Article) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
