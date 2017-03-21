package com.demo.dao.impl;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.demo.dao.BaseDao;
import com.demo.dao.UserDao;
import com.demo.models.User;

@Repository
public class UserDaoImpl extends BaseDao<User> implements UserDao {

	@Override
	public boolean addUser(User user) {
		// TODO Auto-generated method stub
		return save(user);
	}

	@Override
	public User getByUserName(String userName) {
		// TODO Auto-generated method stub
		List<User> users = null;
		User user = null;
		Session session = getSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("userName", userName));
		try {
			users = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}

}
