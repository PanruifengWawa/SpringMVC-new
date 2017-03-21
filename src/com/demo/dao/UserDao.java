package com.demo.dao;

import com.demo.models.User;

public interface UserDao {
	boolean addUser(User user);
	User getByUserName(String userName);
}
