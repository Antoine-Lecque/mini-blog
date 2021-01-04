package com.uca.dao;

import com.uca.entity.ArticleEntity;
import com.uca.entity.CommentEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentDAO extends _Generic<CommentEntity> {
    public ArrayList<CommentEntity> getAllComments() {
        ArrayList<CommentEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM comments ORDER BY created_at DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CommentEntity entity = new CommentEntity();
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

    @Override
    public CommentEntity create(CommentEntity obj) {
        //TODO !
        return null;
    }

    @Override
    public void delete(CommentEntity obj) {
        //TODO !
    }
}
