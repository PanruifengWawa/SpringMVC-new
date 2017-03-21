package com.demo.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.annotation.CheckUser;
import com.demo.enums.UserType;
import com.demo.models.User;
import com.demo.service.UserService;
import com.demo.utils.DataWrapper;



@Controller
@RequestMapping(value="/api/user")
public class UserController {
	
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="testJsonInput", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<User> testJsonInput(
    		@RequestBody User u
    		) {
		DataWrapper<User> dataWrapper = new DataWrapper<User>();
		dataWrapper.setData(u);
    	return  dataWrapper;
		
    }
	
	@CheckUser(type=UserType.Admin)
	@RequestMapping(value="getUserList", method = RequestMethod.GET)
    @ResponseBody
    public DataWrapper<Void> getUserList(
    		HttpServletRequest request,
    		HttpServletResponse response,
    		@RequestParam(value = "token",required = true) String token
    		) {
		
    	return  userService.getUserList(token);
    }
	
	
	
	@RequestMapping(value="login", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> login(
    		@RequestParam(value = "userName", required = true) String userName,
    		@RequestParam(value = "password",required = true) String password
    		) {
		
    	return  userService.login(userName, password);
    }
	
	
	@RequestMapping(value="register", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> addCareerPlan(
    		@ModelAttribute User user
    		) {
		
    	return  userService.register(user);
    }
	
	

}
