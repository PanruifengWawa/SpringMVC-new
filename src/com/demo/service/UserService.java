package com.demo.service;

import com.demo.models.User;
import com.demo.utils.DataWrapper;

public interface UserService {
	DataWrapper<Void> login(String userName,String password);
	DataWrapper<Void> register(User user);

}
