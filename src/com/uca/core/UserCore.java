package com.uca.core;

import com.uca.dao.UserDAO;
import com.uca.dao._Connector;
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

    public static UserEntity getUserbyId(int id) {
        return new UserDAO().getUserbyId(id);
    }

    public static void delete(int id) {
        new UserDAO().delete(id);
    }

    public static String getIdbyName(String username) {
        Connection connect = _Connector.getInstance();
        String id = "";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement("SELECT id_user FROM users WHERE username = ? ;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getString("id_user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public static boolean isAdmin(String id) {
        Connection connect = _Connector.getInstance();
        Boolean isAdmin = false;
        try {
            PreparedStatement preparedStatement = connect.prepareStatement("SELECT isAdmin FROM users WHERE id_user = ? ;");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isAdmin = resultSet.getBoolean("isAdmin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAdmin;
    }

    public static boolean isBanned(String id) {
        Connection connect = _Connector.getInstance();
        Boolean isBanned = false;
        try {
            PreparedStatement preparedStatement = connect.prepareStatement("SELECT isBanned FROM users WHERE id_user = ? ;");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isBanned = resultSet.getBoolean("isBanned");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isBanned;
    }
}
