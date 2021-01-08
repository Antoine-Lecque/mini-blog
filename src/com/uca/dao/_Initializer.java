package com.uca.dao;

import java.sql.*;

public class _Initializer {

    public static void Init(){
        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

            //Init articles table
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS articles (id_article int primary key auto_increment, name varchar(100), author varchar(100), created_at timestamp, content longnvarchar(25000)); ");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS comments (id_comment int primary key auto_increment, content longnvarchar(25000), author varchar(100), created_at timestamp, article int); ");
            statement.executeUpdate();

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id_user int primary key auto_increment, username varchar(100), password varchar(100), isAdmin boolean, isBanned boolean); ");
            statement.executeUpdate();

        } catch (Exception e){
            System.out.println(e.toString());
            throw new RuntimeException("could not create database !");
        }
    }
}
