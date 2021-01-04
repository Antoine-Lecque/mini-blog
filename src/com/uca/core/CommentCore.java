package com.uca.core;

import com.uca.dao.CommentDAO;
import com.uca.entity.CommentEntity;

import java.util.ArrayList;

public class CommentCore {
    public static ArrayList<CommentEntity> getAllComments() {
        return new CommentDAO().getAllComments();
    }
}
