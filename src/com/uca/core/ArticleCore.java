package com.uca.core;

import com.uca.dao.ArticleDAO;
import com.uca.entity.ArticleEntity;

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
}
