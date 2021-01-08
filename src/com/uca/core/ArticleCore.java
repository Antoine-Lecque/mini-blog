package com.uca.core;

import com.uca.dao.ArticleDAO;
import com.uca.dao._Connector;
import com.uca.entity.ArticleEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArticleCore {

    public static ArrayList<ArticleEntity> getAllArticles() {
        return new ArticleDAO().getAllArticles();
    }

    public static ArticleEntity getArticleById(int id) {
        return new ArticleDAO().getArticleById(id);
    }

    public static ArticleEntity create(ArticleEntity obj) {
        return new ArticleDAO().create(obj);
    }

    public static void delete(int id) {
         new ArticleDAO().delete(id);
    }

    public static void update(ArticleEntity obj) {new ArticleDAO().update(obj);}
}
