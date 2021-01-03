package com.uca.dao;

import com.uca.entity.ArticleEntity;

import java.sql.*;
import java.util.ArrayList;

public class ArticleDAO extends _Generic<ArticleEntity> {

    public ArrayList<ArticleEntity> getAllArticles() {
        ArrayList<ArticleEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM articles ORDER BY created_at DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ArticleEntity entity = new ArticleEntity();
                entity.setId(resultSet.getInt("id"));
                entity.setAuthor(resultSet.getString("author"));
                entity.setContent(resultSet.getString("content"));
                entity.setCreated_time(resultSet.getTimestamp("created_at"));
                entity.setName(resultSet.getString("name"));

                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    @Override
    public ArticleEntity create(ArticleEntity obj) {
        //TODO !
        return null;
    }

    @Override
    public void delete(ArticleEntity obj) {
        //TODO !
    }
}
