package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.models.User;
import com.demo.service.UserService;
import com.demo.utils.DataWrapper;



@Controller
@RequestMapping(value="/api/user")
public class UserController {
	
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="test", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<User> test(
    		@RequestBody User u
    		) {
		DataWrapper<User> dataWrapper = new DataWrapper<User>();
		dataWrapper.setData(u);
    	return  dataWrapper;
    }
	
	
	/**
	* @api {post} api/user/login 登录
	* @apiName user_login
	* @apiGroup user
	*
	* @apiParam {String} userName * 用户名（必须）
	* @apiParam {String} password * 密码（必须）
	*
	* @apiSuccessExample {json} Success-Response:
	* 	HTTP/1.1 200 ok
	* 	{
	*		"callStatus": "SUCCEED",
	*		"errorCode": "No_Error",
	*  		"data": null,
	*  		"token": "SK1d7a4fe3-c2cd-417f-8f6f-bf7412592996",
	*  		"numberPerPage": 0,
	*  		"currentPage": 0,
	*  		"totalNumber": 0,
	*  		"totalPage": 0
	*	}
	*
	* @apiSuccessExample {json} Error-Response:
	* 	HTTP/1.1 200 ok
	* 	{
	* 		"callStatus": "FAILED",
	*		"errorCode": "Error",
	*  		"data": null,
	*  		"token": null,
	* 		"numberPerPage": 0,
	*  		"currentPage": 0,
	*  		"totalNumber": 0,
	*  		"totalPage": 0
	*	}
	**/
	@RequestMapping(value="login", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> login(
    		@RequestParam(value = "userName", required = true) String userName,
    		@RequestParam(value = "password",required = true) String password
    		) {
		
    	return  userService.login(userName, password);
    }
	
	
	/**
	* @api {post} api/user/register 注册
	* @apiName user_regist
	* @apiGroup user
	*
	* @apiParam {String} userName * 学号（必须）
	* @apiParam {String} password * 密码（必须）
	* @apiParam {String} name * 姓名（必须）
	*
	* @apiSuccessExample {json} Success-Response:
	* 	HTTP/1.1 200 ok
	* 	{
	*  		"callStatus": "SUCCEED",
	*  		"errorCode": "No_Error",
	*  		"data": null,
	*  		"token": null,
	*  		"numberPerPage": 0,
	*  		"currentPage": 0,
	*  		"totalNumber": 0,
	*  		"totalPage": 0
	*	}
	*
	* @apiSuccessExample {json} Error-Response:
	* 	HTTP/1.1 200 ok
	* 	{
	*  		"callStatus": "FAILED",
	*  		"errorCode": "Error",
	*  		"data": null,
	*  		"token": null,
	*  		"numberPerPage": 0,
	*  		"currentPage": 0,
	*  		"totalNumber": 0,
	*  		"totalPage": 0
	*	}
	**/
	@RequestMapping(value="register", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> addCareerPlan(
    		@ModelAttribute User user
    		) {
		
    	return  userService.register(user);
    }
	
	

}
