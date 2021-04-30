package com.ametr1ne.overdiff.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private String author;

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


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
        this.icon = icon.replaceAll("localhost", "10.0.2.2")
                .replaceAll("\\/", "/");
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

    public static Article deserialize(JSONObject jsonObject) {
        Article article = new Article();

        try {

            article.setAuthor(jsonObject.getString("author"));
            article.setIcon(jsonObject.getString("icon"));
            article.setDescription(jsonObject.getString("description"));
            String dateCreate = jsonObject.getString("dateCreate");
            article.setDateCreate(parseDateTime(dateCreate));
            article.setHash(jsonObject.getString("hash"));
            article.setLikes(jsonObject.getInt("likes"));
            article.setText(jsonObject.getString("text"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return article;
    }


    private static Date parseDateTime(String dateString) {
        if (dateString == null) return null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            return fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
