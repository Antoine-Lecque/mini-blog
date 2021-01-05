package com.uca.dao;

import com.uca.entity.ArticleEntity;
import com.uca.entity.CommentEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CommentDAO extends _Generic<CommentEntity> {
    public ArrayList<CommentEntity> getAllComments() {
        ArrayList<CommentEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM comments ORDER BY created_at DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CommentEntity entity = new CommentEntity();
                entity.setId(resultSet.getInt("id_comment"));
                entity.setContent(resultSet.getString("content"));
                entity.setAuthor(resultSet.getString("author"));
                entity.setCreated_time(resultSet.getTimestamp("created_at"));
                entity.setArticle(resultSet.getInt("article"));

                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    public CommentEntity getCommentById(int id) {
        CommentEntity entity = new CommentEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM comments WHERE id_comment=" + id + ";");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            } else {
                do {
                    entity.setId(resultSet.getInt("id_comment"));
                    entity.setAuthor(resultSet.getString("author"));
                    entity.setContent(resultSet.getString("content"));
                    entity.setCreated_time(resultSet.getTimestamp("created_at"));
                    entity.setArticle(resultSet.getInt("article"));
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public CommentEntity create(CommentEntity obj) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("INSERT INTO comments(content, author, created_at, article) VALUES(?, ?, ?, ?);");
            statement.setString(1, obj.getContent());
            statement.setString(2, obj.getAuthor());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setInt(4, obj.getArticle());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public void update(CommentEntity obj) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("UPDATE comments SET content = ? WHERE id_comment = ?");
            statement.setString(1, obj.getContent());
            statement.setInt(2, obj.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("DELETE FROM comments WHERE id_comment=" + id + ";");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
