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
                entity.setId(resultSet.getInt("id_article"));
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

    public ArticleEntity getArticleById(int id) {
        ArticleEntity entity = new ArticleEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM articles WHERE id_article=" + id + ";");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            } else {
                do {
                    entity.setId(resultSet.getInt("id_article"));
                    entity.setAuthor(resultSet.getString("author"));
                    entity.setContent(resultSet.getString("content"));
                    entity.setCreated_time(resultSet.getTimestamp("created_at"));
                    entity.setName(resultSet.getString("name"));
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public ArticleEntity create(ArticleEntity obj) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("INSERT INTO articles(name, author, created_at, content) VALUES(?, ?, ?, ?);");
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getAuthor());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setString(4, obj.getContent());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public void update(ArticleEntity obj) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("UPDATE articles SET name = ?, author = ?, content = ? WHERE id_article = ?");
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getAuthor());
            statement.setString(3, obj.getContent());
            statement.setInt(4, obj.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("DELETE FROM articles WHERE id_article="+id+";");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
