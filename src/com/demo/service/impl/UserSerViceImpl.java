package com.demo.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.dao.UserDao;
import com.demo.enums.ErrorCodeEnum;
import com.demo.models.User;
import com.demo.service.UserService;
import com.demo.utils.DataWrapper;
import com.demo.utils.MD5Util;
import com.demo.utils.SessionManager;

@Service("userService")
public class UserSerViceImpl implements UserService {
	
	@Autowired
	UserDao userDao;

	@Override
	public DataWrapper<Void> login(String userName, String password) {
		// TODO Auto-generated method stub
		DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
		User user = userDao.getByUserName(userName);
		if (user != null) {
			if (user.getPassword().equals(MD5Util.getMD5String(password))) {
				SessionManager.removeSessionByUserId(user.getId());
				String token = SessionManager.newSession(user);
				dataWrapper.setToken(token);
			} else {
				dataWrapper.setErrorCode(ErrorCodeEnum.Error);
			}
		} else {
			dataWrapper.setErrorCode(ErrorCodeEnum.Error);
		}
		return dataWrapper;
	}

	@Override
	public DataWrapper<Void> register(User user) {
		// TODO Auto-generated method stub
		DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
		if (user.getUserName() == null || user.getUserName().trim().length() == 0 || user.getPassword() == null || user.getPassword().equals("")) {
			dataWrapper.setErrorCode(ErrorCodeEnum.Error);
		} else {
			user.setId(null);
			user.setRegisterDate(new Date());
			user.setPassword(MD5Util.getMD5String(user.getPassword()));
			if (!userDao.addUser(user)) {
				dataWrapper.setErrorCode(ErrorCodeEnum.Error);
			}
		}
		return dataWrapper;
	}

	@Override
	public DataWrapper<Void> getUserList(String token) {
		// TODO Auto-generated method stub
		DataWrapper<Void> dataWrapper = new DataWrapper<Void>();
		
		return dataWrapper;
	}

}
