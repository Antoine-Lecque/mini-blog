package com.uca.core;

import com.uca.dao.CommentDAO;
import com.uca.dao.UserDAO;
import com.uca.dao._Connector;
import com.uca.entity.CommentEntity;
import com.uca.entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserCore {
    public static ArrayList<UserEntity> getAllUsers() {
        return new UserDAO().getAllUsers();
    }

    public static UserEntity create(UserEntity obj) {
        return new UserDAO().create(obj);
    }

    public static Boolean check(String username, String password) {
        Connection connect = _Connector.getInstance();
        try {
            PreparedStatement preparedStatement = connect.prepareStatement("SELECT username, password FROM users ORDER BY username DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               if(resultSet.getString("username").equals(username)){
                   if(BCrypt.checkpw(password, resultSet.getString("password"))){
                       return true;
                   }
               }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
