package com.uca.core;

import com.uca.dao.ArticleDAO;
import com.uca.entity.ArticleEntity;

import java.util.ArrayList;

public class ArticleCore {

    public static ArrayList<ArticleEntity> getAllArticles() {
        return new ArticleDAO().getAllArticles();
    }

}
