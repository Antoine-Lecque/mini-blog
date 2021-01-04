package com.uca.dao;

import java.sql.*;

public class _Initializer {

    public static void Init(){
        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

            //Init articles table
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS articles (id int primary key auto_increment, name varchar(100), author varchar(100), created_at timestamp, content longnvarchar(25000)); ");
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (username varchar(100) primary key, password varchar(100), isAdmin boolean, isBanned boolean); ");
            statement.executeUpdate();


            //Todo Remove me !
            statement = connection.prepareStatement("INSERT INTO articles(name, author, created_at, content) VALUES(?, ?, ?, ?);");
            statement.setString(1, "Bonjour !");
            statement.setString(2, "user1");
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setString(4, "Voici le contenu de mon article");
            statement.executeUpdate();

            statement = connection.prepareStatement("INSERT INTO users(username, password, isAdmin, isBanned) VALUES(?, ?, ?, ?);");
            statement.setString(1, "user6");
            statement.setString(2, "password");
            statement.setBoolean(3, false);
            statement.setBoolean(4, false);
            statement.executeUpdate();

        } catch (Exception e){
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}
