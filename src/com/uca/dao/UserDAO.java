package com.uca.dao;

import com.uca.entity.ArticleEntity;
import com.uca.entity.UserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends _Generic<UserEntity>{

    public ArrayList<UserEntity> getAllUsers() {
        ArrayList<UserEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users ORDER BY username DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserEntity entity = new UserEntity();
                entity.setUsername(resultSet.getString("username"));
                entity.setPassword(resultSet.getString("password"));
                entity.setAdmin(resultSet.getBoolean("isAdmin"));
                entity.setBanned(resultSet.getBoolean("isBanned"));

                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

        @Override
        public UserEntity create(UserEntity obj) {
            //TODO !
            return null;
        }

        @Override
        public void delete(UserEntity obj) {
            //TODO !
        }
}
