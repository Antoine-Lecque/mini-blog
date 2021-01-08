package com.uca.dao;

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
        //TODO !
    }

    @Override
    public void delete(int id) {
        //TODO !
    }
}
