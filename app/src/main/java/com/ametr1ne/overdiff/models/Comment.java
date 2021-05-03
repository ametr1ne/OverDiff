package com.ametr1ne.overdiff.models;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Comment {

    private Long id;
    private String comment;
    private long articleId;
    private long authorId;

    public Comment() {
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment role = (Comment) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Comment deserialize(JSONObject jsonObject) {
        Comment comment = new Comment();

        try {
            comment.setId(Long.parseLong(jsonObject.getString("id")));
            comment.setComment(jsonObject.getString("text"));
            comment.setArticleId(Long.parseLong(jsonObject.getString("article")));
            comment.setAuthorId(Long.parseLong(jsonObject.getString("author")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comment;
    }

}
