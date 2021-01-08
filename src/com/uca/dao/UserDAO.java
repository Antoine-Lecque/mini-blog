package com.uca.dao;

import com.uca.entity.ArticleEntity;
import com.uca.entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;

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
                entity.setId(resultSet.getInt("id_user"));
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

    public UserEntity getUserbyId(int id) {
        UserEntity entity = new UserEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM users WHERE id_user =" + id + ";");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            } else {
                do {
                    entity.setId(resultSet.getInt("id_user"));
                    entity.setUsername(resultSet.getString("username"));
                    entity.setPassword(resultSet.getString("password"));
                    entity.setAdmin(resultSet.getBoolean("isAdmin"));
                    entity.setBanned(resultSet.getBoolean("isBanned"));
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public UserEntity create(UserEntity obj) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("INSERT INTO users (username, password, isAdmin, isBanned) VALUES(?, ?, ?, ?);");
            statement.setString(1, obj.getUsername());
            statement.setString(2, BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt()));
            statement.setBoolean(3, obj.getAdmin());
            statement.setBoolean(4, obj.getBanned());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public void update(UserEntity obj) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("UPDATE users SET username = ?, isAdmin = ?, isBanned = ? WHERE id_user = ?");
            statement.setString(1, obj.getUsername());
            statement.setBoolean(2, obj.getAdmin());
            statement.setBoolean(3, obj.getBanned());
            statement.setInt(4, obj.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement statement = this.connect.prepareStatement("DELETE FROM users WHERE id_user=" + id + ";");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
