package com.uca.core;

import com.uca.dao.UserDao;
import com.uca.entity.UserEntity;

import java.util.ArrayList;

public class UserCore {
    public static ArrayList<UserEntity> getAllUsers() {
        return new UserDao().getAllUsers();
    }

}
