package com.uca.core;

import com.uca.dao.ArticleDAO;
import com.uca.dao.CommentDAO;
import com.uca.entity.ArticleEntity;
import com.uca.entity.CommentEntity;

import java.util.ArrayList;

public class CommentCore {
    public static ArrayList<CommentEntity> getAllComments() {
        return new CommentDAO().getAllComments();
    }

    public static CommentEntity getCommentById(int id) {return new CommentDAO().getCommentById(id); }

    public static CommentEntity create(CommentEntity obj) {
        return new CommentDAO().create(obj);
    }

    public static void delete(int id) {
        new CommentDAO().delete(id);
    }

    public static void update(CommentEntity obj) {new CommentDAO().update(obj);}
}
